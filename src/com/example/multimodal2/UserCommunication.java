package com.example.multimodal2;

import java.util.HashMap;

import multimodal.schedule.Room;
import android.app.Activity;
import android.speech.tts.TextToSpeech;
import android.widget.Toast;

import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.Resource;

public class UserCommunication {
	Activity ma;
	TextToSpeech tts;
	RDFModel rdfModel;
	public UserCommunication(Activity ma) {
		
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
			
		}
		
		String room = "Bathroom";
		Query query = QueryFactory.create("PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> " +
				"PREFIX ex: <http://imi.org/> " +
				"SELECT ?room ?constraint WHERE { ?room ex:hasConstraint ?constraint . ?room rdf:type ex:Room }");
		QueryExecution qe = QueryExecutionFactory.create(query, rdfModel.getModel());
	    ResultSet rs = qe.execSelect();
	
	    HashMap<String,Room> roomMap =  new HashMap<String, Room>();
	    while(rs.hasNext())
	    {
	    	QuerySolution sol = rs.next();
	    	Resource resroom = sol.getResource("room");
	    	Resource resconstraint = sol.getResource("constraint");
	    	if(!roomMap.containsKey(resroom.getLocalName())){
	    		roomMap.put(resroom.getLocalName(), new Room(resroom.getLocalName()));
	    	}
	    	//adding each constraint (or property) to the given room
	    	roomMap.get(resroom.getLocalName()).addPropertyByName(resconstraint.getLocalName());
	    }
	    qe.close();
		
		
		
		
	}

	public void updateTTS(TextToSpeech repeatTTS) {
		this.tts = repeatTTS;
		
	}
	
	
}
