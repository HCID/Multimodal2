package com.example.multimodal2;

import java.io.IOException;
import java.io.InputStream;

import android.content.Context;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;

public class RDFModel {
	RDFModel(Context context){
		
		
		Model model = ModelFactory.createDefaultModel();
		InputStream f;
		try {
			f = context.getAssets().open("rdfmodel.xml");
			model.read(f, "http://imi.org/");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		
	}
}
