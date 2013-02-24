package com.example.multimodal2;

import android.annotation.SuppressLint;
import android.util.Log;

@SuppressLint("DefaultLocale")
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
	@SuppressLint("DefaultLocale")
	public static void userSaid(String text) {

		text = text.toLowerCase();
		if(text.contains("when")) {
			UserInputInterpreter.userSaidWhen();
		} else if(text.matches("where")) {
			UserInputInterpreter.userSaidWhere();
		} else if(text.matches("show")) {
			UserInputInterpreter.userSaidShow();
		} else if(text.matches("move")) {
			UserInputInterpreter.userSaidMove();
		} else if(text.matches("cancel")) {
			UserInputInterpreter.userSaidCancel();
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
