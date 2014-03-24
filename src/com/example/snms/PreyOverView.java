package com.example.snms;


import java.util.ArrayList;
import java.util.List;

import org.joda.time.DateTime;

import com.example.snms.alarm.AlarmChangeListner;
import com.example.snms.alarm.AlarmDialogFragment;
import com.example.snms.database.SnmsDAO;
import com.example.snms.domain.PreyItem;
import com.example.snms.news.NewsListFragment;
import com.example.snms.preylist.PreyOverviewFragment;
import com.example.snms.utils.SnmsPrayTimeAdapter;
//import com.fourmob.datetimepicker.date.DatePickerDialog;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

import android.net.Uri;
import android.opengl.Visibility;
import android.os.Bundle;
import android.app.Activity;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.Menu;
import android.widget.TimePicker;

public class PreyOverView extends  BaseActivity {
	
	SnmsDAO snmsDAO; 
	ArrayList<AlarmChangeListner> alarmChangeListners = new ArrayList<AlarmChangeListner>();
	
	
	public PreyOverView() {
		super(R.string.left_and_right);
		snmsDAO = new SnmsDAO(this);
		
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		
//		Context context = getApplicationContext();		//Dag-Martin
		getSlidingMenu().setMode(SlidingMenu.LEFT_RIGHT);

		getSlidingMenu().setMode(SlidingMenu.LEFT);

		getSlidingMenu().setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
		
		if(currentFragment1 == null) {
			currentFragment1 =  new PreyOverviewFragment();
		alarmChangeListners.add((AlarmChangeListner) currentFragment1);
			
		setContentView(R.layout.content_frame);
		getSupportFragmentManager()
		.beginTransaction()
		.add(R.id.content_frame, currentFragment1)
		.commit();
		
		getSlidingMenu().setSecondaryMenu(R.layout.menu_frame_two);
		getSlidingMenu().setSecondaryShadowDrawable(R.drawable.shadowright);
		getSupportFragmentManager()
		.beginTransaction()
		.replace(R.id.menu_frame_two, new SampleListFragment())
		.commit();
		}
	}
	
	public void setAlarm(String alarm,Boolean value) {
		for(AlarmChangeListner alarmChangeListner : alarmChangeListners){
			alarmChangeListner.alarmChanged(alarm,value);
		}
	}
	
	public SnmsDAO getDAO() {
		return snmsDAO;
	}
	


//	public static Context getAppContext() {
//	    return PreyOverView.getAppContext();
//	}

	@Override
	public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
		// TODO Auto-generated method stub
		
	}
	



	
    
//	@Override
//	public void onDateSet(DatePickerDialog datePickerDialog, int year,
//			int month, int day) {
//		// TODO Auto-generated method stub
//		
//	}

	
}
