package com.example.multimodal2;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;

import android.content.Context;

public class RDFModel {
	RDFModel(Context context){
		org.apache.log4j.BasicConfigurator.configure();
		
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
