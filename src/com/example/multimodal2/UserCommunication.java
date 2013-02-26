package com.example.multimodal2;

import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;

import multimodal.Constraint;
import multimodal.FuzzyTime;
import multimodal.RoomFactory;
import multimodal.schedule.Booking;
import multimodal.schedule.Room;
import android.app.Activity;
import android.content.Intent;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.widget.Toast;

public class UserCommunication {
	
	MainActivity ma;
	TextToSpeech tts;
	RDFModel rdfModel;
	private LinkedList<Room> roomList;
	public String currentRoom;
	public UserCommunication(MainActivity ma) {
		
		this.ma = ma;
		
		this.rdfModel = new RDFModel(this.ma);		
		roomList= RoomFactory.createRoomsFromRDF(this.rdfModel.getModel());
	}
	
	public void InputFromUser(String text) {
		Log.d("SpeechRepeatActivity", text);
		Toast.makeText(this.ma, "You said: "+text, Toast.LENGTH_LONG).show();
		if(this.tts != null) {
			//this.tts.speak("You said: "+text, TextToSpeech.QUEUE_FLUSH, null);
		}
		UserInputInterpreter uii = new UserInputInterpreter(text, roomList);
		if(uii.command == UserInputInterpreter.CommandType.WHEN) {
		

		} else if(uii.command == UserInputInterpreter.CommandType.BOOK) {
			Room associatedRoom = null;
			if(uii.associatedRoom != null){
				associatedRoom = uii.associatedRoom;
			} else {
				for(Room room : roomList ) {
					//TODO constrain rooms based on RDF
					associatedRoom = room;
				}
			}

			Constraint constr = new Constraint();
			if(uii.time != null){
				constr.fuzzyTimeConstrain(uii.time);
			}
			LinkedList<Booking> possibleBookings = associatedRoom.getPossibleBookings(constr);
			Booking b = possibleBookings.getFirst();
			String msg = "Do you want to book a meeting in the "+associatedRoom.getSpeechName()+b.getSpeechStartTime()+"?";
			this.tts.speak(msg, TextToSpeech.QUEUE_FLUSH, null);


		}
//		Collection<Room> roomList= RoomFactory.createRoomsFromRDF(this.rdfModel.getModel());
		Log.d(this.getClass().getSimpleName(), "Parsed "+roomList.size()+" rooms from RDF" );
		for(Room room : roomList ) {
			if(room.getName() == currentRoom) {
				HashMap<String, Integer> modalities = this.rdfModel.getModalityForRoom(room);
				Log.d("SpeechRepeatActivity", "we are in: " + room.getName());
			}

		}
		//get some room
		Room someRoom = roomList.iterator().next();
		//create constraint
		Constraint c = new Constraint();
		int deviation = 60*60; //one hour
		//constrain to meetings plus minus one hour
		c.fuzzyTimeConstrain(new FuzzyTime(new Date(), deviation)); 
		
//		LinkedList<Booking> possibleBookings = someRoom.getPossibleBookings(c);
//		Log.i(this.getClass().getSimpleName(), "Booking :"+possibleBookings.getFirst());
//		possibleBookings.getFirst().book();	
		
	}

	public void updateTTS(TextToSpeech repeatTTS) {
		this.tts = repeatTTS;
		
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
