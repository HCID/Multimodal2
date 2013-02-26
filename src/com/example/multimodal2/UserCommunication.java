package com.example.multimodal2;

import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import multimodal.Constraint;
import multimodal.FuzzyTime;
import multimodal.RoomFactory;
import multimodal.schedule.Booking;
import multimodal.schedule.Room;
import android.content.Intent;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.OnUtteranceCompletedListener;
import android.util.Log;
import android.widget.Toast;

public class UserCommunication {
	
	private static final String OUTPUT_TYPE_QUESTION = "http://imi.org/Question";
	private static final String OUTPUT_TYPE_STATEMENT = "http://imi.org/Statement";
	private static final String OUTPUT_TYPE_YES_NO_QUESTION = "http://imi.org/YesNoQuestion";
	private static final String OUTPUT_TYPE_REMINDER = "http://imi.org/Reminder";
	MainActivity ma;
	
	private LinkedList<Room> roomList;
	public String currentRoom;
	private boolean confirm;
	private UserInputInterpreter currentCommand;

	public UserCommunication(MainActivity ma) {
		
		this.ma = ma;
		
			
		roomList= RoomFactory.createRoomsFromRDF(this.ma.rdfModel.getModel());
	}
	
	public void InputFromUser(String text) {
		Log.d("SpeechRepeatActivity", text);
		//Toast.makeText(this.ma, "You said: "+text, Toast.LENGTH_LONG).show();
		//if(this.tts != null) {
			//this.tts.speak("You said: "+text, TextToSpeech.QUEUE_FLUSH, null);
		//}
		if(this.currentCommand == null) {
			this.currentCommand = new UserInputInterpreter(text, roomList);
		}
		if(currentCommand.command == UserInputInterpreter.CommandType.REMINDER) {
			if(this.currentCommand.time != null){
				final Date reminderTime = this.currentCommand.time.getExactStartTime();
				final MainActivity mainActivity = this.ma;
				new Thread(new Runnable() {
					Date remindAtTime = reminderTime;
					MainActivity activity = mainActivity;
					@Override
					public void run() {
						while(remindAtTime.getTime()<new Date().getTime()){
							try {
								this.wait(1000, 0);
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
						}
						Toast.makeText(mainActivity, "============ WAKE UP! =========",Toast.LENGTH_LONG).show();
						Log.e("REMINDER","============ WAKE UP! =========");
					}
				}).start();
			}
		} else
		if(currentCommand.command == UserInputInterpreter.CommandType.WHEN) {
		

		} else 
		if(currentCommand.command == UserInputInterpreter.CommandType.BOOK) {
			if(this.confirm) {
				if(text.contains("yes")) {
					Log.d("SpeechRepeatActivity", "booked");
				}
				this.confirm = false;
				this.currentCommand = null;
			} else {
				
				Room associatedRoom = null;
				if(currentCommand.associatedRoom != null){
					associatedRoom = currentCommand.associatedRoom;
				} else {
					for(Room room : roomList ) {
						//TODO constrain rooms based on RDF
						associatedRoom = room;
					}
				}

				Constraint constr = new Constraint();
				if(currentCommand.time != null){
					constr.fuzzyTimeConstrain(currentCommand.time);
				}
				LinkedList<Booking> possibleBookings = associatedRoom.getPossibleBookings(constr);
				Booking b = possibleBookings.getFirst();
				
		
				outputToUser("Do you want to book a meeting in the "+associatedRoom.getSpeechName()+b.getSpeechStartTime()+"?", OUTPUT_TYPE_YES_NO_QUESTION);	
				}
			}
		}
		
		//get some room
		//Room someRoom = roomList.iterator().next();
		//create constraint
		//Constraint c = new Constraint();
		//int deviation = 60*60; //one hour
		//constrain to meetings plus minus one hour
		//c.fuzzyTimeConstrain(new FuzzyTime(new Date(), deviation)); 
		
//		LinkedList<Booking> possibleBookings = someRoom.getPossibleBookings(c);
//		Log.i(this.getClass().getSimpleName(), "Booking :"+possibleBookings.getFirst());
//		possibleBookings.getFirst().book();	
		

	
	public String getModalitiesForRoom(String type) {

		HashMap<String, Integer> modalities = null;	
		
		for(Room room : this.roomList ) {
			if(room.getName().equals(currentRoom)) {
				modalities = this.ma.rdfModel.getModalityForRoom(room, OUTPUT_TYPE_QUESTION);				
				break;
			}
		}	
		String modality = null;
		Integer modalityVal = -1;
		for(Map.Entry<String, Integer>mod : modalities.entrySet()) {
			if(mod.getValue() > modalityVal) {
				modality = mod.getKey();
				modalityVal = mod.getValue();
			}
		}
		return modality;
	}
	
	public void setRoom(String room) {
		this.currentRoom = room;
	}
	
	public void outputToUser(String msg, String type) {
		if(getModalitiesForRoom(type).equals("Speech")) {
			if(type == OUTPUT_TYPE_YES_NO_QUESTION) {
				this.ma.repeatTTS.setOnUtteranceCompletedListener(new OnUtteranceCompletedListener() {
			        @Override
			        public void onUtteranceCompleted(String utteranceId) {
			        	askForUserSpeechInput();
			            
			        }
			    });
				this.confirm = true;
			}	
			outputToUserByVoice(msg);
		}					
	}
	
	private void outputToUserByVoice(String msg) {		
		HashMap<String, String> myHashAlarm = new HashMap<String, String>();
		myHashAlarm.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, "SOME MESSAGE");
		this.ma.repeatTTS.speak(msg, TextToSpeech.QUEUE_FLUSH, myHashAlarm);
	}
	
	
	public void askForUserSpeechInput() {
		Intent listenIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
		listenIntent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, getClass().getPackage().getName());
		listenIntent.putExtra(RecognizerIntent.EXTRA_PROMPT, "What do you want to do?");
		listenIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
		listenIntent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 1);
		this.ma.startActivityForResult(listenIntent, this.ma.VR_REQUEST);
	}
	
	
}
