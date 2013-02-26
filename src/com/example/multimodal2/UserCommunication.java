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
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.widget.Toast;

public class UserCommunication {
	Activity ma;
	TextToSpeech tts;
	RDFModel rdfModel;
	private Collection<Room> roomList;
	public UserCommunication(Activity ma) {
		roomList= RoomFactory.createRoomsFromRDF(this.rdfModel.getModel());
		this.ma = ma;
		
		this.rdfModel = new RDFModel(this.ma);		
	}
	
	public void InputFromUser(String text) {
		Toast.makeText(this.ma, "You said: "+text, Toast.LENGTH_LONG).show();
		if(this.tts != null) {
			this.tts.speak("You said: "+text, TextToSpeech.QUEUE_FLUSH, null);
		}
		UserInputInterpreter uii = new UserInputInterpreter(text);
		if(uii.command == UserInputInterpreter.CommandType.WHEN) {
		
		} else if(uii.command == UserInputInterpreter.CommandType.BOOK) {
			Room bestRoom = null;
			for(Room room : roomList ) {
				//TODO constrain rooms based on RDF
				bestRoom = room;
			}
			if(bestRoom!=null){
				Constraint constr = new Constraint();
				if(uii.time != null){
					constr.fuzzyTimeConstrain(uii.time);
				}
				LinkedList<Booking> possibleBookings = bestRoom.getPossibleBookings(constr);
				Booking b = possibleBookings.getFirst();
				String msg = "Do you want to book a meeting in the "+bestRoom.getName()+b.getSpeechStartTime()+"?";
				this.tts.speak(msg, TextToSpeech.QUEUE_FLUSH, null);
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
	
	
}
