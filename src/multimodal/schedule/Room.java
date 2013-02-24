package multimodal.schedule;

import java.util.Arrays;
import java.util.LinkedList;

import multimodal.Constraint;
import multimodal.Property;

public class Room {
	public static final Room DUMMY_ROOM = new Room();
	private int uid;
	public Room(){
		this.uid = getNextUID();
	}

	private static int nextUID = 0;
	private int getNextUID() {
		return Room.nextUID++;
	}
	LinkedList<Property> properties;
	private String name;
	private Schedule schedule;
	public Room(String name, Property ...props){
		this(name);
		this.properties.addAll(Arrays.asList(props));
	}
	
	public Room(String localName) {
		this.properties = new LinkedList<Property>();
		this.name = localName;
		this.schedule = new Schedule();
	}

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
	
	public LinkedList<Booking> getPossibleBookings(Constraint c) {
		return this.schedule.getPossibleBookings(c);
	}

	public void addPropertyByName(String localName) {
		Property prop = Property.valueOf(localName);
		if(prop == null){
			throw new IllegalArgumentException(localName+" is not part of the enum Property!");
		}
		this.properties.add(prop);
	}
}
