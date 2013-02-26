package com.example.multimodal2;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import multimodal.RoomFactory;
import multimodal.schedule.Room;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.OnInitListener;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

public class MainActivity extends Activity implements OnClickListener, OnInitListener {

	private static final int VR_REQUEST = 999;
	private final String LOG_TAG = "SpeechRepeatActivity";
	private int MY_DATA_CHECK_CODE = 0;
	private TextToSpeech repeatTTS;
	private UserCommunication uc;
	protected String currentRoom;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		uc = new UserCommunication(MainActivity.this);
		Button speechBtn = (Button) findViewById(R.id.speech_btn);
		PackageManager packManager = getPackageManager();
		List<ResolveInfo> intActivities = packManager.queryIntentActivities(new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH), 0);
		if (intActivities.size() != 0) {
			speechBtn.setOnClickListener(this);
		}
		else
		{
			speechBtn.setEnabled(false);
			Toast.makeText(this, "Oops - Speech recognition not supported!", Toast.LENGTH_LONG).show();
		}
		Intent checkTTSIntent = new Intent();
		checkTTSIntent.setAction(TextToSpeech.Engine.ACTION_CHECK_TTS_DATA);
		startActivityForResult(checkTTSIntent, MY_DATA_CHECK_CODE);
		
		Spinner spinner = (Spinner) findViewById(R.id.current_place);
		RDFModel rdfModel = new RDFModel(this);	
		
		
		ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item);
		for(Room room : RoomFactory.createRoomsFromRDF(rdfModel.getModel()) ) {
			spinnerArrayAdapter.add(room.getName());
		}
		
		spinner.setAdapter(spinnerArrayAdapter);
		
		spinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				
				Log.d(LOG_TAG, arg1.getClass().toString());
				//currentRoom
				
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
				
			}
			
		});
		
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		//getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

	@Override
	public void onInit(int arg0) {
		if (arg0 == TextToSpeech.SUCCESS)
			repeatTTS.setLanguage(Locale.UK);
	}

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.speech_btn) {
			Intent listenIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
			listenIntent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, getClass().getPackage().getName());
			listenIntent.putExtra(RecognizerIntent.EXTRA_PROMPT, "What do you want to do?");
			listenIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
			listenIntent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 1);
			startActivityForResult(listenIntent, VR_REQUEST);
		}
	}


	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == VR_REQUEST && resultCode == RESULT_OK)
		{
			ArrayList<String> suggestedWords = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
			uc.InputFromUser(suggestedWords.get(0));
		} 
		else if (requestCode == MY_DATA_CHECK_CODE)
		{
			if (resultCode == TextToSpeech.Engine.CHECK_VOICE_DATA_PASS) {
				repeatTTS = new TextToSpeech(this, this);
				uc.updateTTS(repeatTTS);
			} 
			else 
			{
				Intent installTTSIntent = new Intent();
				installTTSIntent.setAction(TextToSpeech.Engine.ACTION_INSTALL_TTS_DATA);
				startActivity(installTTSIntent);
			}
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

}
