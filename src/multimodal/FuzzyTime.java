package multimodal;

import java.io.Serializable;
import java.util.Date;

/**
 * 
 * FuzzyTime tries to account for incomplete or vague time information given by the user.
 * Depending on the input, it can define a range or a vagueness (deviation)
 *
 */
public class FuzzyTime implements Cloneable, Serializable{
	private static final long DEFAULT_DEVIATION_SEC = 10*60;
	long deviationSeconds;
	Date startTime;
	Date endTime;
	long durationSeconds;

	/**
	 * Initializer
	 * @param startTime
	 */
	public FuzzyTime(Date startTime){
		this.startTime = startTime;
		this.deviationSeconds = FuzzyTime.DEFAULT_DEVIATION_SEC;
	}

	/**
	 * 
	 * @param startTime
	 * @param deviationSeconds defines how many seconds the time may
	 * deviate from the reality when matching
	 */
	public FuzzyTime(Date startTime, long deviationSeconds){
		this(startTime);
		this.deviationSeconds = deviationSeconds;
	}

	/**
	 * Define a fuzzy time range
	 * @param startTime
	 * @param endTime
	 */
	public FuzzyTime(Date startTime, Date endTime){
		this(startTime);
		this.endTime = endTime;
	}

	/**
	 * define a fuzzy time range with a user specified deviation
	 * @param startTime
	 * @param endTime
	 * @param deviation
	 */
	public FuzzyTime(Date startTime, Date endTime, long deviation){
		this(startTime, endTime);
		this.deviationSeconds = deviation;
	}

	/**
	 * setend time give a duration and a start time
	 * @param durationSeconds
	 * @return
	 */
	public FuzzyTime setDuration(long durationSeconds){
		this.endTime = new Date(this.startTime.getTime()+durationSeconds*1000);
		return this;
	}

	/**
	 * get a FuzzyTime object that describes now
	 * @return
	 */
	public static FuzzyTime now() {
		return new FuzzyTime(new Date());
	}

	/**
	 * Get a FuzzyTime object describing the time x seconds in the future.
	 * Use negative values to go to the past
	 * @param seconds
	 * @return
	 */
	public static FuzzyTime nowPlusSeconds(long seconds) {
		Date now = new Date();
		return new FuzzyTime(new Date(now.getTime()+seconds*1000));
	}
	
	/**
	 * get a FuzzyTime object, that is x seconds from the start of the day
	 * 00:00
	 * @param seconds
	 * @return
	 */
	public static FuzzyTime todayPlusSeconds(long seconds) {
		Date now = new Date();
		Date today = new Date(now.getYear(),now.getMonth(),now.getDate());
		return new FuzzyTime(new Date(today.getTime()+seconds*1000));
	}
	
	/**
	 * clone FuzzyTime
	 */
	@Override
	protected Object clone() throws CloneNotSupportedException {
		return new FuzzyTime((Date)this.startTime.clone(), (Date)this.endTime.clone(), this.deviationSeconds);
	}

	/**
	 * gets the time between the start and the endtime
	 * @return
	 */
	public Date getCenter() {
		return new Date((this.endTime.getTime()+this.startTime.getTime())/2);
	}

	/**
	 * get the deviation from the center
	 * @return
	 */
	public long getCenterDeviation() {
		return this.durationSeconds+this.deviationSeconds;
	}

	public Date getExactStartTime() {
		return (Date)this.startTime.clone();
	}

}
