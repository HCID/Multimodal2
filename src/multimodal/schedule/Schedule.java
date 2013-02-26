package multimodal.schedule;

import java.io.Serializable;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.LinkedList;

import multimodal.Constraint;
import multimodal.FuzzyTime;

/**
 * this class implements a schedule in which multiple 
 * bookings can be added, which can not overlap
 *
 */
public class Schedule implements Serializable {
	
	//has to be sorted along the time axis!
	private LinkedList<Booking> bookings;
	private Room room;
	
	/**
	 * Create a new empty schedule
	 * @param room the room this schedule belongs to
	 */
	public Schedule(Room room){
		this.bookings = new LinkedList<Booking>();
		this.room = room;
	}
	
	/**
	 * remove a booking from the schedule
	 * @param booking
	 */
	private void removeFromSchedule(Booking booking) {
		this.bookings.remove(booking);
	}

	/**
	 * test if this booking can be unbooked from this
	 * schedule
	 * @param booking the booking to test
	 * @return true if this booking can be unbooked from this schedule
	 */
	private boolean canUnbook(Booking booking) {
		return this.bookings.contains(booking);
	}
	
	/**
	 * find a booking in this schedule that is near to the given date
	 * @param near a date near the booking you want to find
	 * @param deviationSeconds the maximum deviation from that date 
	 * @return a Booking or null if none found
	 */
	public Booking findBookingNear(Date near, long deviationSeconds){
		for(Booking b : bookings){
			long diff = Math.abs((b.getStartTime().getTime() - near.getTime())/1000);
			if(diff<deviationSeconds){
				return b;
			}
		}
		return null;
	}
	
	/**
	 * see findBookingNear, with standard deviation of 45 minutes
	 * @param near
	 * @return
	 */
	public Booking findBookingNear(Date near){
		//default deviation 45min.
		return findBookingNear(near, 45*60);
	}
	
	
	/**
	 * find a booking in this schedule that is near to the given fuzzyTime
	 * @param fuzzyTime a date near the booking you want to find
	 * @return a Booking or null if none found
	 */
	public Booking findBookingNear(FuzzyTime fuzzyTime){
		//default deviation 45min.
		return findBookingNear(fuzzyTime.getCenter(), fuzzyTime.getCenterDeviation());
	}
	
	/**
	 * add a booking to this schedule
	 * @param booking the booking to add
	 */
	private void addToSchedule(Booking booking) {
		this.bookings.add(booking);
		Collections.sort(this.bookings, new Comparator<Booking>(){
			@Override
			public int compare(Booking b1, Booking b2) {
				return (int) (b1.getStartTime().getTime()-b2.getStartTime().getTime());
			}
			
		});
	}

	/**
	 * tests of this booking can be booked
	 * @param booking the booking to test
	 * @return true if it can be booked, else false
	 */
	private boolean canBook(Booking booking) {
		for(Booking b : this.bookings){
			if(b.overlaps(booking.getStartTime(),booking.getEndTime())){
				return false;
			}
		}
		return true;
	}

	/**
	 * book a booking
	 * @param booking the booking to book
	 * @return true if successfully booked
	 */
	protected boolean book(Booking booking) {
		if(canBook(booking)){
			addToSchedule(booking);
			return true;
		}
		return false;
	}
	
	/**
	 * unbook the given booking
	 * @param booking the booking to unbook
	 * @return true if it was unbooked sucessfully
	 */
	protected boolean unbook(Booking booking) {
		if(canUnbook(booking)){
			removeFromSchedule(booking);
			return true;
		}
		return false;
	}

	/**
	 * return a list of possible bookings that fulfill the given constraint
	 * @param c a time constraint
	 * @return a list of possible bookings
	 */
	public LinkedList<Booking> getPossibleBookings(Constraint c) {
		Date now = new Date();
		if(c.getEndTime().before(now)){
			throw new IllegalArgumentException("can't get a booking in the past!");
		}
		Date minimumStartDate = c.getStartTime().after(now) ? c.getStartTime() : now;
		return getNextPossibleBookings(minimumStartDate, c.getEndTime(), c.getDuration());		
	}
	
	/**
	 * get the next possible booking for a certain tie period
	 * @param startDate the start of the time period
	 * @param endDate the end of the time period
	 * @param duration the duration of the booking
	 * @return
	 */
	private LinkedList<Booking> getNextPossibleBookings(Date startDate, Date endDate, long duration){
		LinkedList<Booking> possibleBookings = new LinkedList<Booking>();
		Date walkAlongEndDate = new Date(startDate.getTime()+duration*1000);
		if(this.bookings.size()==0){
			if(startDate.getTime()>= endDate.getTime()){
				return possibleBookings;
			}
			//if there are no bookings, there can be no collision
			possibleBookings.add(new Booking(this, startDate, walkAlongEndDate, this.room));
		} else {
			for(Booking b : this.bookings){
				//current Start Date after latest end date?
				if(startDate.getTime()>= endDate.getTime()){
					return possibleBookings;
				}
				//jump over booking if it overlaps
				if(b.overlaps(startDate, walkAlongEndDate)){
					startDate = new Date(b.getEndTime().getTime()+1);
					walkAlongEndDate = new Date(startDate.getTime()+duration*1000);
				}
				if(!b.overlaps(startDate, walkAlongEndDate)){
					//we've got a booking. That's freaking awesome.
					possibleBookings.add(new Booking(this, startDate, walkAlongEndDate, this.room));
				}
			}			
		}
		return possibleBookings;
	}

}
