package com.example.multimodal2;

import java.util.HashMap;
import java.util.LinkedList;

import multimodal.Constraint;
import multimodal.RoomFactory;
import multimodal.schedule.Booking;
import multimodal.schedule.Room;
import android.content.Intent;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.OnUtteranceCompletedListener;
import android.util.Log;

public class UserCommunication {
	
	MainActivity ma;
	TextToSpeech tts;
	RDFModel rdfModel;
	private LinkedList<Room> roomList;
	public String currentRoom;
	private boolean confirm;
	private UserInputInterpreter currentCommand;
	HashMap<String, Integer> modalities;
	public UserCommunication(MainActivity ma) {
		
		this.ma = ma;
		
		this.rdfModel = new RDFModel(this.ma);		
		roomList= RoomFactory.createRoomsFromRDF(this.rdfModel.getModel());
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
		
		if(currentCommand.command == UserInputInterpreter.CommandType.WHEN) {
		

		} else if(currentCommand.command == UserInputInterpreter.CommandType.BOOK) {
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
				String msg = "Do you want to book a meeting in the "+associatedRoom.getSpeechName()+b.getSpeechStartTime()+"?";
				this.tts.setOnUtteranceCompletedListener(new OnUtteranceCompletedListener() {

			        @Override
			        public void onUtteranceCompleted(String utteranceId) {
			        	Log.d("SpeechRepeatActivity", "123");
			        	askForUserSpeechInput();
			            
			        }
			    });
				
				HashMap<String, String> myHashAlarm = new HashMap<String, String>();
				myHashAlarm.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, "SOME MESSAGE");
				this.tts.speak(msg, TextToSpeech.QUEUE_FLUSH, myHashAlarm);
				
				
				
					
					this.confirm = true;
					
					
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
		

	public void updateTTS(TextToSpeech repeatTTS) {
		this.tts = repeatTTS;
		
	}
	
	
	public void updateRoom(String cRoom) {
		this.currentRoom = cRoom;
		Log.d(this.getClass().getSimpleName(), "Parsed "+roomList.size()+" rooms from RDF" );
		
		for(Room room : this.roomList ) {
			Log.d("SpeechRepeatActivity", "is: " + room.getName() + " and " + cRoom + " the same thing?");
			if(room.getName().equals(currentRoom)) {
				modalities = this.rdfModel.getModalityForRoom(room);				
				break;
			}
		}
		//Log.d("SpeechRepeatActivity", "we are in: " + modalities.);
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
