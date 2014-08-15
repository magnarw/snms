package no.snms.app;

import java.util.HashMap;

import no.snms.app.database.SnmsDAO;
import no.snms.app.network.RequestManager;
import no.snms.app.news.NewsListFragment;
import no.snms.app.preylist.PreyOverviewFragment;

import org.joda.time.DateTime;

import android.annotation.SuppressLint;
import android.app.SearchManager.OnCancelListener;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager.BackStackEntry;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.ListFragment;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;
import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
//import com.fourmob.datetimepicker.date.DatePickerDialog;
//import com.fourmob.datetimepicker.date.DatePickerDialog.OnDateSetListener;
//import com.sleepbot.datetimepicker.time.RadialPickerLayout;
//import com.sleepbot.datetimepicker.time.TimePickerDialog;

@SuppressLint("NewApi")
public abstract class BaseActivity extends SlidingFragmentActivity implements
		OnClickListener, TimePickerDialog.OnTimeSetListener {

	// final DatePickerDialog datePickerDialog =
	// DatePickerDialog.newInstance(this, 2007, 10, 1, false);

	// final TimePickerDialog timePickerDialog =
	// TimePickerDialog.newInstance(this, 0 ,0, false);

	private int mTitleRes;	
	protected ListFragment mFrag;
	
	
	HashMap<String, Fragment> custombackStack = new HashMap<String,Fragment>();
	
	Fragment currentFragment1;
	Fragment currentFragment2;
	ImageView homeButton;
	TextView padder;

	public BaseActivity(int titleRes) {
		mTitleRes = titleRes;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setTitle(mTitleRes);

		// set the Behind View
		setBehindContentView(R.layout.menu_frame);
		if (savedInstanceState == null) {
			FragmentTransaction t = this.getSupportFragmentManager()
					.beginTransaction();
			mFrag = new SampleListFragment();
			t.replace(R.id.menu_frame, mFrag);
			t.commit();
		} else {
			mFrag = (ListFragment) this.getSupportFragmentManager()
					.findFragmentById(R.id.menu_frame);
		}

		// customize the SlidingMenu
		SlidingMenu sm = getSlidingMenu();

		sm.setShadowWidthRes(R.dimen.shadow_width);
		sm.setShadowDrawable(R.drawable.shadow);
		sm.setBehindOffsetRes(R.dimen.slidingmenu_offset);
		Display display = getWindowManager().getDefaultDisplay();
		DisplayMetrics outMetrics = new DisplayMetrics();
		display.getMetrics(outMetrics);

		int padding = (int) (outMetrics.widthPixels / 5);

		// sm.setSelectorDrawable(R.drawable.flamingo);
		sm.setSelectorEnabled(false);
		sm.setFadeDegree(0.35f);
		sm.setTouchModeAbove(SlidingMenu.LEFT);

		getSupportActionBar().setDisplayShowCustomEnabled(true);
		getSupportActionBar().setDisplayShowTitleEnabled(false);
		getSupportActionBar().setIcon(R.drawable.smal);
		getSupportActionBar().setDisplayShowCustomEnabled(true);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		// getSupportActionBar() ab.setIcon(R.drawable.your_left_action_icon);
		LayoutInflater inflator = (LayoutInflater) this
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View v = inflator.inflate(R.layout.snmsactionbarlayout, null);

		homeButton = (ImageView) v.findViewById(R.id.homebutton);
		ActionBar.LayoutParams params = new ActionBar.LayoutParams(
				ActionBar.LayoutParams.MATCH_PARENT,
				ActionBar.LayoutParams.MATCH_PARENT, Gravity.CENTER);
		getSupportActionBar().setCustomView(v, params);

		homeButton.setOnClickListener(this);

	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			toggle();
			return true;
		case R.id.github:
			switchContent(new PrayCalenderListFragment(), null);
			// datePickerDialog.setYearRange(1985, 2028);
			// datePickerDialog.show(getSupportFragmentManager(), "datepicker");
			return true;
		case R.id.homebutton:
			switchContent(new PrayCalenderListFragment(), null);
			// datePickerDialog.setYearRange(1985, 2028);
			// datePickerDialog.show(getSupportFragmentManager(), "datepicker");
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	public void switchContent(Fragment fragment1, Fragment fragment2) {
		RequestManager.getRequestQueue().cancelAll(
				new RequestQueue.RequestFilter() {
					@Override
					public boolean apply(Request<?> request) {
						return true;
					}
		});
		
		if(getSupportFragmentManager().getBackStackEntryCount()>5){
			BackStackEntry f = getSupportFragmentManager().getBackStackEntryAt(1);
			getSupportFragmentManager();
		}
		
		if (fragment2 == null) {
			if (currentFragment2 != null) {
				FragmentTransaction t = getSupportFragmentManager().beginTransaction();
						 t.remove(currentFragment1).remove(currentFragment2)
						.replace(R.id.content_frame, fragment1);
						
						if(getSupportFragmentManager().getBackStackEntryCount()<3)
							t.addToBackStack("tag").commit();
						else
							t.commit();
			} else {
				getSupportFragmentManager().beginTransaction();
				FragmentTransaction t = getSupportFragmentManager().beginTransaction();
				 t.remove(currentFragment1).replace(R.id.content_frame, fragment1);
				if(getSupportFragmentManager().getBackStackEntryCount()<3)
					t.addToBackStack("tag").commit();
				else
					t.commit();
			}
		} else {
			getSupportFragmentManager().beginTransaction()
					.remove(currentFragment1)
					.replace(R.id.content_frame, fragment1);
					
		}

		currentFragment1 = fragment1;
		currentFragment2 = fragment2;

		getSlidingMenu().showContent();
	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getSupportMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	



	@Override
	public void onClick(View v) {
		if (v.equals(homeButton)) {
			switchContent(new PreyOverviewFragment(), null);
		}

	}

	// @Override
	// public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute)
	// {
	// // TODO Auto-generated method stub
	//
	// }

}
