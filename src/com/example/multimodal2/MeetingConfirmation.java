package com.example.multimodal2;

import multimodal.schedule.Booking;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

public class MeetingConfirmation  extends Activity implements OnClickListener{
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.bookingconfirmation);
		 Booking b = (Booking) getIntent().getSerializableExtra("booking");
		 TextView startTimeView = (TextView) findViewById(R.id.start_time);
		 TextView endTimeView = (TextView) findViewById(R.id.end_time);
		 TextView roomView = (TextView) findViewById(R.id.confirmationroom);
		 startTimeView.setText(b.getStartTime().toLocaleString());		 
		 endTimeView.setText(b.getEndTime().toLocaleString());
		 roomView.setText(b.getRoom().getName());
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
	}

}
