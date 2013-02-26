package multimodal;

import java.io.Serializable;
import java.util.Date;
import java.util.LinkedList;

import multimodal.schedule.Room;

/**
 * 
 * {@link Constraint} provides a way to constrain the possible
 * bookings in time.
 *
 */
public class Constraint implements Serializable {
	private LinkedList<Property> properties;
	private Date startTime;
	private Date endTime;
	private int minimalDurationSec;

	public Constraint(){
		this.properties = new LinkedList<Property>();
		//huge time window; No time restriction
		this.startTime = new Date(0); //far in the past
		this.endTime = new Date(Long.MAX_VALUE); //far in the future
		this.minimalDurationSec = 1; 
	}
	
	@Deprecated
	public void constrain(Property mustHave){
		this.properties.add(mustHave);
	}
	
	/**
	 * the Booking must start after startTime
	 * @param startTime
	 */
	public void constrainStart(Date startTime){
		this.startTime = startTime;
	}
	
	/**
	 * the booking must start in at least inSeconds
	 * @param inSeconds
	 */
	public void constrainStartNowPlusSec(long inSeconds){
		constrainStart(new Date(System.currentTimeMillis()+inSeconds*1000));
	}
	
	/**
	 * the booking must at before now + inSeconds
	 * @param inSeconds
	 */
	public void constrainEndNowPlusSec(long inSeconds){
		this.constrainEnd(new Date(System.currentTimeMillis()+inSeconds*1000));
	}
	
	/**
	 * set the time before the booking must have ended
	 * @param endTime
	 */
	public void constrainEnd(Date endTime){
		if(startTime.getTime()+this.minimalDurationSec*1000l>endTime.getTime()){
			throw new IllegalArgumentException("end time cannot be shorter than starttime + duration!");
		}
		this.endTime = endTime;
	}
	
	/**
	 * set the minimal Duration of the booking
	 * @param minimalDurationSec
	 */
	public void constrainDuration(int minimalDurationSec){
		if(minimalDurationSec<0){
			throw new IllegalArgumentException("Can't create a negative time span!");
		}
		if((endTime.getTime()-startTime.getTime())/1000<minimalDurationSec){
			throw new IllegalArgumentException("duration is longer than the specified end and start time!");
		}
		this.minimalDurationSec = minimalDurationSec;
	}
	
	/**
	 * checks if Date d fulfills the constraint
	 * @param d the Date
	 * @return true or false
	 */
	public boolean fulfillsTimeConstraint(Date d){
		return this.startTime.before(d) && this.endTime.after(d);
	}
	
	/**
	 * checks if the room fulfills the property (Deprecated)
	 * @param r a room
	 * @return true or false
	 */
	@Deprecated
	public boolean fulfillsPropertyConstraints(Room r){
		return this.properties.containsAll(r.getProperties());
	}

	/**
	 * get the start time of the constraint
	 * @return
	 */
	public Date getStartTime() {
		return (Date) this.startTime.clone();
	}
	
	/**
	 * get the end time of the constraint
	 * @return
	 */
	public Date getEndTime() {
		if(endTime == null){
			this.endTime = new Date(Long.MAX_VALUE);
		}
		return (Date) endTime.clone();
	}

	/**
	 * get the duration of this constraint in seconds
	 * @return
	 */
	public long getDuration() {
		return this.minimalDurationSec;
	}

	/**
	 * Create a constraint from a FuzzyTime object
	 * @param ft the fuzzy time object
	 * @return a Constraint
	 */
	public Constraint fuzzyTimeConstrain(FuzzyTime ft) {
		this.startTime = ft.startTime;
		this.endTime = ft.endTime;
		return this;
	}
}
