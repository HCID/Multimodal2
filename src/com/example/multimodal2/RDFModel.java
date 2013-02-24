package com.example.multimodal2;

import java.io.IOException;
import java.io.InputStream;

import android.content.Context;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;

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
	
	public Model getModel() {
		return this.model;
	}
}
