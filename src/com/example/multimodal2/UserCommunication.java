package com.example.multimodal2;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.widget.Toast;

@SuppressLint("DefaultLocale")
public class UserCommunication {
	@SuppressLint("DefaultLocale")
	public void userSaid(String text, Activity act, TextToSpeech repeatTTS) {
		Toast.makeText(act, "You said: "+text, Toast.LENGTH_LONG).show();
		repeatTTS.speak("You said: "+text, TextToSpeech.QUEUE_FLUSH, null);
		text = text.toLowerCase();
		if(text.contains("when")) {
			this.userSaidWhen();
		} else if(text.matches("where")) {
			this.userSaidWhere();
		} else if(text.matches("show")) {
			this.userSaidShow();
		} else if(text.matches("move")) {
			this.userSaidMove();
		} else if(text.matches("cancel")) {
			this.userSaidCancel();
		}		
	}
	
	private void userSaidCancel() {
		Log.d("SpeechRepeatActivity", "matched cancel!");
	}

	private void userSaidMove() {
		Log.d("SpeechRepeatActivity", "matched move!");	
	}

	private void userSaidShow() {
		Log.d("SpeechRepeatActivity", "matched show!");
	}

	private void userSaidWhere() {
		Log.d("SpeechRepeatActivity", "matched where!");
	}

	public void userSaidWhen() {
		Log.d("SpeechRepeatActivity", "matched when!");
	}
	
}