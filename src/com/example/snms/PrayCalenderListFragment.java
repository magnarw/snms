package com.example.snms;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import org.joda.time.DateTime;

import com.android.volley.toolbox.NetworkImageView;
import com.example.snms.domain.NewsItem;
import com.example.snms.domain.PreyItem;
import com.example.snms.domain.PreyItemList;
import com.example.snms.news.NewsListFragment.NewsListAdapter;
import com.example.snms.utils.SnmsPrayTimeAdapter;
import android.annotation.SuppressLint;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class PrayCalenderListFragment extends Fragment implements
		OnClickListener ,  android.app.DatePickerDialog.OnDateSetListener {

	ListView prayGridForMonth;
	SnmsPrayTimeAdapter prayTimeAdapter;
	PreyCalenderAdapter adapter;
	   public static final String DATEPICKER_TAG = "datepicker";
	TextView date;
	TextView fajr;
	TextView sol;
	TextView duhr;

	TextView asr;
	TextView mag;
	TextView ish;

	private TextView currentDay;
	private ImageView nextDay;
	private ImageView prevDay;
	private TextView calender;
	private DateTime currentDateTime;
	private DateTime currentDate;
	private DateTime timeCurrentlyUsedInPreyOverView;
	List<PreyItemList> list;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.calenderwidget, container,
				false);

		prayGridForMonth = (ListView) rootView.findViewById(R.id.preyCalender);
		prayGridForMonth.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
		adapter = new PreyCalenderAdapter(getActivity());
		currentDay = (TextView) rootView.findViewById(R.id.prey_current_day);
		currentDay.setOnClickListener(this);
		nextDay = (ImageView) rootView.findViewById(R.id.prey_next_day);
		nextDay.setOnClickListener(this);
		prevDay = (ImageView) rootView.findViewById(R.id.prey_prev_day);
		prevDay.setOnClickListener(this);
		currentDate = new DateTime();
		timeCurrentlyUsedInPreyOverView = currentDate;
		return rootView;
	}

	private String getMonthAsText(int monthOfYear) {
		String month;
		switch (monthOfYear) {

		case 1:
			month = "Januar";
			break;
		case 2:
			month = "Februar";
			break;
		case 3:
			month = "Mars";
			break;
		case 4:
			month = "April";
			break;
		case 5:
			month = "Mai";
			break;
		case 6:
			month = "Juni";
			break;
		case 7:
			month = "Juli";
			break;
		case 8:
			month = "August";
			break;
		case 9:
			month = "September";
			break;
		case 10:
			month = "Oktober";
			break;
		case 11:
			month = "November";
			break;
		case 12:
			month = "Desember";
			break;

		default:
			month = "August";
			break;

		}

		return month;
	}

	private void setUpCurrentDay() {

		int monthOfYear = timeCurrentlyUsedInPreyOverView.getMonthOfYear();
		String month = getMonthAsText(monthOfYear);
		String toSet = month + " " + timeCurrentlyUsedInPreyOverView.getYear();
		currentDay.setText(toSet);

	}

	@SuppressLint("NewApi")
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		prayTimeAdapter = new SnmsPrayTimeAdapter(getActivity().getAssets(),((PreyOverView) getActivity()).getDAO());
		prayGridForMonth.setAdapter(adapter);
		setUpCurrentDay();
		try {
			list = prayTimeAdapter.getPrayGridForMonthIndYear(
					timeCurrentlyUsedInPreyOverView.getMonthOfYear(), 2013,
					false);
			for (PreyItemList o : list) {
				adapter.add(o);
			}
		} catch (Exception e) {
			Log.e("PreyListCalender", "Bygge kalender fra preylist adatper:"
					+ e.getLocalizedMessage());
		}
		adapter.notifyDataSetChanged();
	}

	public class PreyCalenderAdapter extends ArrayAdapter<PreyItemList> {

		HashMap<Integer, View> hasBeenRenderedMap = new HashMap<Integer, View>();

		public PreyCalenderAdapter(Context context) {
			super(context, 0);

		}

		boolean hasBeenRendered(int day) {
			if (hasBeenRenderedMap.containsKey(day))
				return true;

			return false;
		}
		
		
		@Override
		public void clear() {
			super.clear();
			hasBeenRenderedMap = new HashMap<Integer, View>();
		}

		public View getView(int position, View convertView, ViewGroup parent) {

			PreyItemList h = getItem(position);

			if (!hasBeenRendered(h.getDay())) {
				convertView = LayoutInflater.from(getContext()).inflate(
						R.layout.calender_row, parent, false);

				date = (TextView) convertView.findViewById(R.id.row_date);
				asr = (TextView) convertView.findViewById(R.id.row_asr);
				fajr = (TextView) convertView.findViewById(R.id.row_fajr);
				sol = (TextView) convertView.findViewById(R.id.row_soloppgang);
				duhr = (TextView) convertView.findViewById(R.id.row_duhr);
				mag = (TextView) convertView.findViewById(R.id.row_magrihb);
				ish = (TextView) convertView.findViewById(R.id.row_isha);

				List<TextView> lables = new ArrayList<TextView>();

				lables.add(fajr);
				lables.add(sol);
				lables.add(duhr);
				lables.add(asr);
				lables.add(mag);
				lables.add(ish);
				date.setText(h.getDay().toString());
				int counter = 0;
				try {
					for (PreyItem item : h.getPreylist()) {
						if (counter == 6) {
							break;
						}
						TextView prey = lables.get(counter);
						String ZeroPlusHour = Integer.toString(item.getTime()
								.getHourOfDay());
						if (item.getTime().getHourOfDay() < 10) {
							ZeroPlusHour = "0" + ZeroPlusHour;
						}
						String ZeroPlusMin = Integer.toString(item.getTime()
								.getMinuteOfHour());
						if (item.getTime().getMinuteOfHour() < 10) {
							ZeroPlusMin = "0" + ZeroPlusMin;
						}
						prey.setText(ZeroPlusHour + ":" + ZeroPlusMin);
						
						
						counter++;
					}
				} catch (Exception exception) {
					Log.e("PreyListCalender", "Kunne ikke lage calender:"
							+ exception.getLocalizedMessage());
				}

				hasBeenRenderedMap.put(h.getDay(), convertView);
				return convertView;
			}

			return hasBeenRenderedMap.get(h.getDay());

		}


	}

	@Override
	public void onClick(View v) {
		
		if (v.equals(currentDay)) {
		     android.app.DatePickerDialog pika = new android.app.DatePickerDialog(this.getActivity(), this, 
                       DateTime.now().getYear(),DateTime.now().getMonthOfYear(),1);
		  
		     DatePicker dp = pika.getDatePicker();
		     try {
		    	    Field f[] = dp.getClass().getDeclaredFields();
		    	    for (Field field : f) {
		    	        if (field.getName().equals("mDaySpinner")) {
		    	            field.setAccessible(true);
		    	            Object dayPicker = new Object();
		    	            dayPicker = field.get(dp);
		    	            ((View) dayPicker).setVisibility(View.GONE);
		    	        }
		    	    }
		    	} catch (SecurityException e) {
		    	    Log.d("ERROR", e.getMessage());
		    	} 
		    	catch (IllegalArgumentException e) {
		    	    Log.d("ERROR", e.getMessage());
		    	} catch (IllegalAccessException e) {
		    	    Log.d("ERROR", e.getMessage());
		    	}
		     
		     pika.show();
		}
		if (v.equals(nextDay)) {
			adapter.clear();
			timeCurrentlyUsedInPreyOverView = timeCurrentlyUsedInPreyOverView
					.plusMonths(1);
			setUpCurrentDay();
			
			prayGridForMonth.clearChoices();
			try {
				list = prayTimeAdapter.getPrayGridForMonthIndYear(
						timeCurrentlyUsedInPreyOverView.getMonthOfYear(), 2013,
						false);
				for (PreyItemList o : list) {
					adapter.add(o);
				}
			} catch (Exception e) {
				Log.e("PreyListCalender",
						"Bygge kalender fra preylist adatper:"
								+ e.getLocalizedMessage());
			}

	
			adapter.notifyDataSetChanged();

		}
		if (v.equals(prevDay)) {
			adapter.clear();
			timeCurrentlyUsedInPreyOverView = timeCurrentlyUsedInPreyOverView
					.minusMonths(1);
			setUpCurrentDay();
			adapter.clear();
			prayGridForMonth.clearChoices();
			try {
				list = prayTimeAdapter.getPrayGridForMonthIndYear(
						timeCurrentlyUsedInPreyOverView.getMonthOfYear(), 2013,
						false);
				for (PreyItemList o : list) {
					adapter.add(o);
				}
			} catch (Exception e) {
				Log.e("PreyListCalender",
						"Bygge kalender fra preylist adatper:"
								+ e.getLocalizedMessage());
			}

			adapter.notifyDataSetChanged();

		}
		

	}

	

	public void onDateSet(DatePicker datePickerDialog, int year,
			int month, int day) {
		
		timeCurrentlyUsedInPreyOverView = new DateTime(year,month+1,day,0,0);
		setUpCurrentDay();
		adapter.clear();
		try {
			list = prayTimeAdapter.getPrayGridForMonthIndYear(
					timeCurrentlyUsedInPreyOverView.getMonthOfYear(), 2013,
					false);
			for (PreyItemList o : list) {
				adapter.add(o);
			}
		} catch (Exception e) {
			Log.e("PreyListCalender",
					"Bygge kalender fra preylist adatper:"
							+ e.getLocalizedMessage());
		}
	
		adapter.notifyDataSetChanged();
		
	}



}
