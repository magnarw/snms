package no.snms.app;


import java.util.ArrayList;
import java.util.List;

import no.snms.app.alarm.AlarmChangeListner;
import no.snms.app.alarm.AlarmDialogFragment;
import no.snms.app.database.SnmsDAO;
import no.snms.app.domain.PreyItem;
import no.snms.app.news.NewsListFragment;
import no.snms.app.preylist.PreyOverviewFragment;
import no.snms.app.utils.SnmsPrayTimeAdapter;

import org.joda.time.DateTime;
import org.json.JSONException;
import org.json.JSONObject;

import no.snms.app.R;






//import com.fourmob.datetimepicker.date.DatePickerDialog;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.parse.Parse;
import com.parse.ParseInstallation;
import com.parse.PushService;

import android.net.Uri;
import android.opengl.Visibility;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
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
		ParseInstallation.getCurrentInstallation().saveInBackground();
		
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
		Intent intent = getIntent();
		Bundle extra  = intent.getExtras();
		if(extra !=null) {
			String jsonData = extra.getString("com.parse.Data");
			try {
				JSONObject obj = new JSONObject(jsonData);
				String alert = obj.getString("alert");
				String tep = "";
				// 1. Instantiate an AlertDialog.Builder with its constructor
				AlertDialog.Builder builder = new AlertDialog.Builder(this);

				// 2. Chain together various setter methods to set the dialog characteristics
				builder.setMessage(alert).setTitle("Pushvarsel fra SNMS");

				// 3. Get the AlertDialog from create()
				AlertDialog dialog = builder.create();
				dialog.show();
				
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
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
