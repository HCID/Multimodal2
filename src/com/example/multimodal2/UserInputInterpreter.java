package com.example.multimodal2;

public class UserInputInterpreter {
	class UserInput{
	    public CommandType commandType;
	    public String time;
	    //Date exactTime;
	    public String location;
	    //Room exactLocation;
	}

	enum CommandType{
	    DISPLAY, CANCEL, MOVE, BOOK, WHEN, WHERE, WHO
	}
	
	
}
