package multimodal.schedule;

import java.io.Serializable;
import java.util.Arrays;
import java.util.LinkedList;

import multimodal.Constraint;
import multimodal.Property;

/**
 * 
 * Room is a class that is used to split multiple
 * Schedules and to separate the different properties of each room
 *
 */
public class Room implements Serializable {
	private static int nextUID = 0;
	public static final Room DUMMY_ROOM = new Room();
	LinkedList<Property> properties;
	private String name;
	private Schedule schedule;
	private String uri;
	private LinkedList<String> aliases = new LinkedList<String>();	
	private int uid;
	
	/**
	 * Default constructor
	 */
	public Room(){
		this.uid = Room.nextUID++;
	}

	/**
	 * construct a room
	 * @param uri The RDF URI describing the room
	 * @param name The readable room name
	 * @param props list of properties
	 */
	public Room(String uri, String name, Property ...props){
		this(uri, name);
		this.properties.addAll(Arrays.asList(props));
	}

	public Room(String uri, String name) {
		this.uri = uri;
		this.properties = new LinkedList<Property>();
		this.name = name;
		this.schedule = new Schedule(this);
	}

	/**
	 * returns the list of properties, which originate from the RDF
	 * @return list of properties
	 */
	public LinkedList<Property> getProperties(){
		return (LinkedList<Property>) this.properties.clone();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + uid;
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Room other = (Room) obj;
		if (uid != other.uid)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "[Room: "+this.name+"]";
	}

	/**
	 * returns the possbiel Bookings from the schedule of this room
	 * see: {@link Schedule}
	 * @param c
	 * @return a list of "unbooked" bookings
	 */
	public LinkedList<Booking> getPossibleBookings(Constraint c) {
		return this.schedule.getPossibleBookings(c);
	}

	/**
	 * add property to this room by giving its name as string
	 * @param propertyName 
	 */
	public void addPropertyByName(String propertyName) {
		Property prop = Property.valueOf(propertyName);
		if(prop == null){
			throw new IllegalArgumentException(propertyName+" is not part of the enum Property!");
		}
		this.properties.add(prop);
	}

	/**
	 * 
	 * @return the pretty name of this room
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * return the RDF URI of this room
	 * @return
	 */
	public String getURI() {
		return this.uri;
	}

	/**
	 * add alias names for this room. Might be useful for better speech matching
	 * @param aliases list of aliases
	 * @return the room the aliases were added to
	 */
	public Room setAliases(LinkedList<String> aliases) {
		this.aliases  = aliases;
		return this;
	}

	/**
	 * return the list of aliases this room has
	 * @return list of aliases
	 */
	public LinkedList<String> getAliases() {
		return this.aliases;
	}

	public String getSpeechName() {
		if(this.aliases.size()>0){
			return this.aliases.getFirst();
		}
		return this.name;
	}
}
