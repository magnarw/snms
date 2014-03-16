package com.example.snms;

import org.joda.time.DateTime;


import com.example.snms.preylist.PreyOverviewFragment;

import android.os.CountDownTimer;
import android.widget.TextView;

public class PreyCountDownTimer extends CountDownTimer {
	
	TextView textViewToUpdate; 
	PreyOverviewFragment adapter;
	
	public PreyCountDownTimer(long time, long interval, TextView textView,PreyOverviewFragment adapter) {
		super(time,interval);
		this.textViewToUpdate = textView;
		this.adapter = adapter;
		
	}
	
	@Override
	public void onFinish() {
		this.adapter.renderPreyList();
	}

	@Override
	public void onTick(long millisUntilFinished) {
		
		int seconds = (int) (millisUntilFinished / 1000) % 60 ;
		int minutes = (int) ((millisUntilFinished / (1000*60)) % 60);
		int hours   = (int) ((millisUntilFinished / (1000*60*60)) % 24);
		
		String minAsString = "";
		String hourAsString = "";
		String secondsAsString = "";
		
		if(hours<10){
			hourAsString = "0" + String.valueOf(hours);
		}else {
			hourAsString = String.valueOf(hours);
		}
		if(minutes<10){
			minAsString = "0" + String.valueOf(minutes);
		}else {
			minAsString = String.valueOf(minutes);
		}
		if(seconds<10){
			secondsAsString = "0" + String.valueOf(seconds);
		}else {
			secondsAsString =  String.valueOf(seconds);
		}
		String preyText = hourAsString + ":" + minAsString + ":" + secondsAsString;
		this.textViewToUpdate.setText(preyText);
	}

}
