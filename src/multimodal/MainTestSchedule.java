package multimodal;

import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;

import multimodal.schedule.Booking;
import multimodal.schedule.Room;

public class MainTestSchedule {
	public static void main(String[] args){
		Date now = new Date();
		LinkedList<Room> rooms = new LinkedList<Room>();
		rooms.add(new Room("chilloutRoom",Property.Dim,Property.Private,Property.Sitting));
		rooms.add(new Room("officeRoom",Property.Bright,Property.Public,Property.Sitting));
		Constraint c = new Constraint();
		c.constrain(Property.Bright);
		c.constrain(Property.Public);
		c.constrainStart(new Date(now.getTime()+10*60*1000)); //in ten minutes
		c.constrainDuration(60*15); //15minutes
		c.constrainEnd(new Date(now.getTime()+26*60*1000)); //in 26 minutes
		HashMap<Room,LinkedList<Booking>> possibleBookings = new HashMap<Room, LinkedList<Booking>>();
		for(Room r : rooms){
			possibleBookings.put(r,r.getPossibleBookings(c));
			System.out.println(r.toString()+possibleBookings.get(r).toString());
			System.out.println("book them!");
			for(Booking b : possibleBookings.get(r)){
				b.book();
			}
			
		}
		System.out.println("check whats still free:");
		for(Room r : rooms){
			possibleBookings.put(r,r.getPossibleBookings(c));
			System.out.println(possibleBookings.get(r));
		}
	}
}

