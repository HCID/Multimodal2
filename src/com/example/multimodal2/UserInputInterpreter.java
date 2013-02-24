package com.example.multimodal2;

import java.util.Locale;

import multimodal.FuzzyTime;
import multimodal.schedule.Room;

public class UserInputInterpreter {
	public class UserInput{
	    public CommandType commandType;
	    public String time;
	    FuzzyTime exactTime;
	    public String location;
	    Room exactLocation;
	}

	enum CommandType{
	    DISPLAY, CANCEL, MOVE, BOOK, WHEN, WHERE, WHO
	};
	
	
	
	public FuzzyTime interpreteTime(String time) throws UserInputNotUnderstoodException{
		time = time.toLowerCase(Locale.getDefault());
		if(time.contains("yesterday")){
			return FuzzyTime.nowPlusSeconds(-24*60*60);
		} else if(time.contains("day after tomorrow")){
			return FuzzyTime.nowPlusSeconds(48*60*60);
		} else if(time.contains("tomorrow")){
			return FuzzyTime.nowPlusSeconds(24*60*60);
		} else if(time.equals("next")){
			return FuzzyTime.now();
		} else {
			throw new UserInputNotUnderstoodException("Time: "+time);
		}
	}
	
	class UserInputNotUnderstoodException extends Exception{
		UserInputNotUnderstoodException(String msg){
			super(msg);
		}
	}
	
}
