package com.example.multimodal2;

import android.app.Activity;
import android.speech.tts.TextToSpeech;
import android.widget.Toast;

public class UserCommunication {
	Activity ma;
	TextToSpeech tts;
	public UserCommunication(Activity ma) {
		
		this.ma = ma;
	}
	
	public void InputFromUser(String text) {
		Toast.makeText(this.ma, "You said: "+text, Toast.LENGTH_LONG).show();
		if(this.tts != null) {
			this.tts.speak("You said: "+text, TextToSpeech.QUEUE_FLUSH, null);
		}
		UserInputInterpreter uii = new UserInputInterpreter(text);
		if(uii.command == UserInputInterpreter.CommandType.WHEN) {
			
		}
		
	}

	public void updateTTS(TextToSpeech repeatTTS) {
		this.tts = repeatTTS;
		
	}
	
	
}
