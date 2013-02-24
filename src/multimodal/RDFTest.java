package multimodal;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.HashMap;
import java.util.LinkedList;

import multimodal.Constraint;
import multimodal.schedule.Booking;
import multimodal.schedule.Room;

import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Resource;

public class RDFTest {
	public static void main(String[] args){
		//dirty log init
		org.apache.log4j.BasicConfigurator.configure();
		
		Model model = ModelFactory.createDefaultModel();
		File f = new File("rdf-model.xml");
		FileReader fr;
		try {
			fr = new FileReader(f);
			model.read(fr, "http://imi.org/");
		} catch (FileNotFoundException e) {
			System.out.println("IO ERROR: "+f.getAbsolutePath());
			System.exit(1);
		}

		//creating Rooms
		Query query = QueryFactory.create("PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> " +
				"PREFIX ex: <http://imi.org/> " +
				"SELECT ?room ?constraint WHERE { ?room ex:hasConstraint ?constraint . ?room rdf:type ex:Room }");
		QueryExecution qe = QueryExecutionFactory.create(query, model);
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

	    Constraint c = new Constraint();
	    c.constrainStartNowPlusSec(600);
	    c.constrainDuration(10*60);
	    
	    HashMap<Room,LinkedList<Booking>> possibleBookings = new HashMap<Room, LinkedList<Booking>>();
	    for(Room r : roomMap.values()){
			possibleBookings.put(r,r.getPossibleBookings(c));
			System.out.println(r.toString()+possibleBookings.get(r).toString());
			//book everything, it's free!
			for(Booking b : possibleBookings.get(r)){
				b.book();
			}
		}
	    
	    
	    	
	    
	}
}
