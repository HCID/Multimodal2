package multimodal.schedule;

import java.util.Arrays;
import java.util.LinkedList;

import multimodal.Property;

public class Person {
	static int nextUID = 0;
	private int uid;
	private LinkedList<Property> capabilities;
	public Person(){
		this.uid = getNewUID();
		capabilities.addAll(Arrays.asList(Property.values()));
	}
	
	private static int getNewUID(){
		return Person.nextUID++;
	}
	
	public void removeCapability(Property c){
		if(capabilities.contains(c)){
			capabilities.remove(c);
		}
	}
}
