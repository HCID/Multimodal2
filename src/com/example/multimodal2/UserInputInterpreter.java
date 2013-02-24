package com.example.multimodal2;

import java.util.Locale;

import multimodal.FuzzyTime;
import multimodal.schedule.Room;
import android.annotation.SuppressLint;
import android.util.Log;

@SuppressLint("DefaultLocale")
public class UserInputInterpreter {
	public class UserInput{
	   // public CommandType commandType;
	    public String time;
	    FuzzyTime exactTime;	    
	}
	public enum CommandType{
	    DISPLAY, CANCEL, MOVE, BOOK, WHEN, WHERE, WHO
	};
	Room exactLocation;
	FuzzyTime time;
	CommandType command;
	
	@SuppressLint("DefaultLocale")
	public UserInputInterpreter(String text) {
		text = text.toLowerCase();		
		try {
			this.time = this.interpreteTime(text);
		} catch (UserInputNotUnderstoodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(text.contains("when")) {
			this.command = CommandType.WHEN;
		} else if(text.matches("where")) {
			this.command = CommandType.WHERE;
		} else if(text.matches("show")) {
			this.command = CommandType.DISPLAY;
		} else if(text.matches("move")) {
			this.command = CommandType.MOVE;
		} else if(text.matches("cancel")) {
			this.command = CommandType.CANCEL;
		}
	}

	
	private FuzzyTime interpreteTime(String time) throws UserInputNotUnderstoodException {
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
	public static void userSaidCancel() {
		Log.d("SpeechRepeatActivity", "matched cancel!");
	}

	public static void userSaidMove() {
		Log.d("SpeechRepeatActivity", "matched move!");	
	}

	public static void userSaidShow() {
		Log.d("SpeechRepeatActivity", "matched show!");
	}

	public static void userSaidWhere() {
		
		Log.d("SpeechRepeatActivity", "matched where!");
	}

	public static void userSaidWhen() {
		Log.d("SpeechRepeatActivity", "matched when!");
	}
	
}
