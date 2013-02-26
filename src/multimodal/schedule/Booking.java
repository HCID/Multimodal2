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

	public Booking(Schedule schedule, Date startTime, Date endTime){
		this.schedule = schedule;
		this.startTime = startTime;
		this.endTime = endTime;
		this.booked = false;
		this.persons = new LinkedList<Person>();
	}
	
	public void addPerson(Person person){
		this.persons.add(person);
	}
	
	public void removePerson(Person person){
		this.persons.remove(person);
	}
	
	public void book(){
		if(this.schedule.book(this)){
			this.booked = true;
		} else {
			this.booked = false;
		}
	}
	
	public void unbook(){
		if(this.schedule.unbook(this)){
			this.booked = false;
		} else {
			this.booked = true;
		}
	}

	public boolean overlaps(Date startTime, Date endTime) {
		//overlaps in time?
		if(	startTime.after(this.getEndTime()) || 
			this.getStartTime().after(endTime) ){
			return false;
		}
		return true;
	}

	public Date getEndTime() {
		return (Date) endTime.clone();
	}

	public Date getStartTime() {
		return (Date) startTime.clone();
	}
	
	@Override
	public String toString() {
		return "Booking: "+this.startTime.toString()+" - "+this.endTime.toString();
	}

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

}
