package multimodal.schedule;

import java.util.Collection;
import java.util.LinkedList;

import multimodal.Constraint;
import multimodal.FuzzyTime;

public class ScheduleManager {
	private Collection<Room> rooms;

	public ScheduleManager(Collection<Room> rooms){
		this.rooms = rooms;
	}
	
	class BookingRoomPair{
		final public Booking booking;
		final public Room room;
		BookingRoomPair(Booking booking, Room room){
			this.booking = booking;
			this.room = room;		
		}
	}
	
	public LinkedList<BookingRoomPair> getBookingsNear(FuzzyTime t){
		LinkedList<BookingRoomPair> roombook = new LinkedList<ScheduleManager.BookingRoomPair>();
		for(Room r : this.rooms){
			for(Booking b : r.getPossibleBookings(new Constraint().fuzzyTimeConstrain(t))){
				roombook.add(new BookingRoomPair(b, r));
			}
		}
		return roombook;
	}
}
