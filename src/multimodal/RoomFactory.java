package multimodal;

import java.util.Collection;
import java.util.HashMap;

import multimodal.schedule.Room;

import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.Resource;

public class RoomFactory {
	public static Collection<Room> createRoomsFromRDF(Model model){
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
	    return roomMap.values();
	}
}
