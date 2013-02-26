package com.example.multimodal2;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

import multimodal.schedule.Room;

import android.content.Context;

import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Resource;

public class RDFModel {
	private Model model = ModelFactory.createDefaultModel();
	
	RDFModel(Context context){
		InputStream f;
		try {
			f = context.getAssets().open("rdfmodel.xml");
			model.read(f, "http://imi.org/");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	}
	
	public HashMap<String,Integer> getModalityForRoom(Room room){
		Query query = QueryFactory.create(
		"PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> \n" +
		"PREFIX ex: <http://imi.org/> \n" +
		"SELECT ?modality ?constraint WHERE { \n" +
		"	?modality ex:hasConstraint ?constraint . \n" +
		"	?modality rdf:type ex:Output . \n" +
		"	{" +
		"		SELECT ?constraint WHERE {\n "+
		"			<"+room.getURI()+"> ex:hasConstraint ?constraint  \n" +
		"		} \n" +
		"	} \n"+
		"} \n");

		QueryExecution qe = QueryExecutionFactory.create(query, this.model);
		ResultSet rs = qe.execSelect();
		
		HashMap<String,Integer> modalityPreference =  new HashMap<String, Integer>();
		while(rs.hasNext())
		{
			QuerySolution sol = rs.next();
			Resource resmodality = sol.getResource("modality");		
			String modality = resmodality.toString();
			if(modalityPreference.containsKey(modality)){
				modalityPreference.put(modality, modalityPreference.get(modality) + 1);
			} else {
				modalityPreference.put(modality, 1);
			}
		}
		return modalityPreference;
	}
	
	public Model getModel() {
		return this.model;
	}
}
