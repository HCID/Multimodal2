package multimodal.schedule;

import java.io.Serializable;
import java.util.Date;
import java.util.LinkedList;

public class Booking implements Serializable {
	final private Date startTime;
	final private Date endTime;
	final private Schedule schedule;
	private boolean booked;
	private LinkedList<Person> persons;
	private Room room;

	/**
	 * A booking
	 * 
	 * @param schedule
	 * @param startTime
	 * @param endTime
	 * @param room
	 */
	public Booking(Schedule schedule, Date startTime, Date endTime, Room room){
		this.schedule = schedule;
		this.startTime = startTime;
		this.endTime = endTime;
		this.booked = false;
		this.persons = new LinkedList<Person>();
		this.room = room;
	}
	
	/**
	 * add a person to this booking
	 * @param person
	 */
	@Deprecated
	public void addPerson(Person person){
		this.persons.add(person);
	}
	/**
	 * remove a person from this booking
	 * @param person
	 */
	@Deprecated
	public void removePerson(Person person){
		this.persons.remove(person);
	}
	
	/**
	 * book this booking.
	 * calls it's corresponding schedule to see if it is still valid.
	 */
	public void book(){
		if(this.schedule.book(this)){
			this.booked = true;
		} else {
			this.booked = false;
		}
	}

	/**
	 * unbooks this booking, also from it's corresponding schedule
	 */
	public void unbook(){
		if(this.schedule.unbook(this)){
			this.booked = false;
		} else {
			this.booked = true;
		}
	}

	/**
	 * check if a booking overlaps a given time range
	 * @param startTime the start of the time range
	 * @param endTime the end of the time range
	 * @return true if the booking is inside or overlaps the time range, otherwise false
	 */
	public boolean overlaps(Date startTime, Date endTime) {
		//overlaps in time?
		if(	startTime.after(this.getEndTime()) || 
			this.getStartTime().after(endTime) ){
			return false;
		}
		return true;
	}

	/**
	 * get the end time of this booking
	 * @return a date
	 */
	public Date getEndTime() {
		return (Date) endTime.clone();
	}

	/**
	 * get the start tome of this booking
	 * @return
	 */
	public Date getStartTime() {
		return (Date) startTime.clone();
	}
	
	@Override
	public String toString() {
		return "Booking: "+this.startTime.toString()+" - "+this.endTime.toString();
	}

	/**
	 * get a pretty pring version of this booking,
	 * which canbe used for speech output
	 * @return
	 */
	public String getSpeechStartTime() {
		StringBuilder sb = new StringBuilder();
		Date now = new Date();
		Date today = new Date(now.getYear(), now.getMonth(), now.getDate());
		Date thisDay = new Date(this.startTime.getYear(), this.startTime.getMonth(), this.startTime.getDate());
		long offset = this.startTime.getTime() - today.getTime();
		if(offset > 0){
			if(offset > 1000*60*60*24*3){
				sb.append(" in "+(offset/1000*60*60*24)+" days ");
			} else if(offset > 1000*60*60*24*2){
				sb.append(" the day after tomorrow ");
			} else if(offset > 1000*60*60*24){
				sb.append(" tomorrow ");
			} else if(offset <= 1000*60*60*24){
				sb.append(" today ");
			}
		}
		sb.append(" at "+startTime.getHours()+" o'clock");
		return sb.toString();
	}

	public Room getRoom() {
		return this.room;
	}

}
