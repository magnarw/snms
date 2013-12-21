package com.example.snms;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import org.joda.time.DateTime;

import com.android.volley.toolbox.NetworkImageView;
import com.example.snms.domain.PreyItem;
import com.example.snms.domain.PreyItemList;
import com.example.snms.news.NewsItem;
import com.example.snms.news.NewsListFragment.NewsListAdapter;
import com.example.snms.news.NewsListFragment.NewsScrollListner;
import com.example.snms.utils.SnmsPrayTimeAdapter;
import com.fourmob.datetimepicker.date.DatePickerDialog;
import com.fourmob.datetimepicker.date.DatePickerDialog.OnDateSetListener;
import com.sleepbot.datetimepicker.time.RadialPickerLayout;
import com.sleepbot.datetimepicker.time.TimePickerDialog;

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
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class PrayCalenderListFragment extends Fragment implements
		OnClickListener ,  OnDateSetListener, TimePickerDialog.OnTimeSetListener {

	ListView prayGridForMonth;
	SnmsPrayTimeAdapter prayTimeAdapter;
	PreyCalenderAdapter adapter;
	  DatePickerDialog datePickerDialog;
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
		   final Calendar calendar = Calendar.getInstance();
	        datePickerDialog = DatePickerDialog.newInstance(this, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), false);
			

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

	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		prayTimeAdapter = new SnmsPrayTimeAdapter(getActivity().getAssets());
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

		prayGridForMonth.setItemChecked(
				timeCurrentlyUsedInPreyOverView.getDayOfMonth() - 1, true);
		prayGridForMonth.smoothScrollToPosition(timeCurrentlyUsedInPreyOverView
				.getDayOfMonth() - 1);
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
						R.layout.calender_row, null);

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
				date.setText(h.getDay().toString()
						+ " "
						+ getMonthAsText(timeCurrentlyUsedInPreyOverView
								.getMonthOfYear()));
				int counter = 0;
				try {
					for (PreyItem item : h.getPreylist()) {
						if (counter == 6) {
							break;
						}
						TextView prey = lables.get(counter);
						prey.setText(addWhiteSpaces(item.getName())
								+ item.getTimeOfDayAsString());
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

		private String addWhiteSpaces(String preyText) {
			String s = preyText;
			while (s.length() < 11) {
				s += " ";
			}

			return s;
		}

	}

	@Override
	public void onClick(View v) {
		
		if (v.equals(currentDay)) {
		       datePickerDialog.setYearRange(1985, 2028);
            datePickerDialog.show(getFragmentManager(), DATEPICKER_TAG);
			/*
			timeCurrentlyUsedInPreyOverView = timeCurrentlyUsedInPreyOverView.plusDays(1);
			setUpCurrentDay();
			preyTimes = loadPrayTimes(timeCurrentlyUsedInPreyOverView);
			adapter.setActivePreys(preyTimes);
			adapter.clear();
			adapter.setActivePreys(preyTimes);
			for (PreyItem preyItem : preyTimes) {
				adapter.add(preyItem);
				// checkAlarmStateAtStartup(preyItem);
			}
			adapter.notifyDataSetChanged();
			*/
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

	@Override
	public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onDateSet(DatePickerDialog datePickerDialog, int year,
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
		prayGridForMonth.setItemChecked(
				day-1, true);
		prayGridForMonth.smoothScrollToPosition(day-1);
		
	}

}
