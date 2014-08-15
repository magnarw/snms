package no.snms.app.settings;

import java.util.TimeZone;

import no.snms.app.PreyOverView;
import no.snms.app.database.SnmsDAO;
import no.snms.app.domain.Geolocation;
import no.snms.app.domain.GeolocationSearchResult;
import no.snms.app.domain.Geometry;
import no.snms.app.domain.NewsItem;
import no.snms.app.news.NewsListFragment.NewsListAdapter;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import no.snms.app.R;
import no.snms.app.R.id;
import no.snms.app.R.layout;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class SettingsFragment extends Fragment implements
		OnItemSelectedListener, OnCheckedChangeListener, OnClickListener {

	SnmsDAO dao;
	
	boolean showDilaog = true; 
	EditText location;
	Spinner calcMethod;
	Spinner jurMethod;
	Spinner cities;
	Spinner adjustMethod;
	RadioButton cityAsr1;
	RadioButton cityAsr2;
	CheckBox hanaFi;
	CheckBox icc;
	CheckBox city;
	Button searchButton;
	RelativeLayout avansertContainer;
	ArrayAdapter<CharSequence> locationAdapter;
	ArrayAdapter<CharSequence> citiesAdapter;
	ArrayAdapter<CharSequence> calcAdapter;
	ArrayAdapter<CharSequence> adjustMethodAdapter;
	ArrayAdapter<CharSequence> jurMethodAdapter;
	GeolocationManager geolocationManager = GeolocationManager.getInstance();
	ProgressBar progressBar;
	TextView locationText;
	
	TextView timeZoneContainer;
	
	//cities

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		
		dao = ((PreyOverView) getActivity()).getDAO();
		View root = inflater.inflate(R.layout.settings, null);
		hanaFi = (CheckBox) root.findViewById(R.id.hanafi);
		city = (CheckBox) root.findViewById(R.id.cityCheckBox);
		city.setOnCheckedChangeListener(this);
		icc = (CheckBox) root.findViewById(R.id.icc);
		progressBar = (ProgressBar) root.findViewById(R.id.progressBar);
		progressBar.setVisibility(View.GONE);
		locationText = (TextView) root.findViewById(R.id.locationContainer);
		timeZoneContainer = (TextView) root
				.findViewById(R.id.timeZoneContainer);
		hanaFi.setOnCheckedChangeListener(this);
	
		hanaFi.setOnClickListener(this);
		hanaFi.setOnClickListener(this);
		hanaFi.setOnClickListener(this);
		city.setOnClickListener(this);
		icc.setOnClickListener(this);
		icc.setOnCheckedChangeListener(this);
		avansertContainer = (RelativeLayout) root
				.findViewById(R.id.avansertContainer);
		searchButton = (Button) root.findViewById(R.id.search);
		cityAsr1 = (RadioButton)root.findViewById(R.id.asr1);
		cityAsr2 = (RadioButton)root.findViewById(R.id.asr2);
		
		cityAsr1.setOnClickListener(this);
		cityAsr2.setOnClickListener(this);
		searchButton.setOnClickListener(this);
		location = (EditText) root.findViewById(R.id.location);
		cities = (Spinner) root.findViewById(R.id.cities);
		citiesAdapter =  ArrayAdapter.createFromResource(getActivity(),
				R.array.cities,
				android.R.layout.simple_spinner_item);
		cities.setAdapter(citiesAdapter);
		cities.setSelection(1); 
		calcMethod = (Spinner) root.findViewById(R.id.calcMethod);
		
		adjustMethod = (Spinner) root.findViewById(R.id.adjustMethod);
		jurMethod = (Spinner) root.findViewById(R.id.jurMethod);
		//cities.setOnClickListener(this);
		calcAdapter = ArrayAdapter.createFromResource(getActivity(),
				R.array.calculation_methods,
				android.R.layout.simple_spinner_item);
		calcAdapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		calcMethod.setAdapter(calcAdapter);

		adjustMethodAdapter = ArrayAdapter.createFromResource(getActivity(),
				R.array.adjust_methods, android.R.layout.simple_spinner_item);
		adjustMethodAdapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		adjustMethod.setAdapter(adjustMethodAdapter);

		jurMethodAdapter = ArrayAdapter.createFromResource(getActivity(),
				R.array.jurstic_methods, android.R.layout.simple_spinner_item);
		jurMethodAdapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		jurMethod.setAdapter(jurMethodAdapter);

		calcMethod.setOnItemSelectedListener(this);
		adjustMethod.setOnItemSelectedListener(this);
		jurMethod.setOnItemSelectedListener(this);
		cities.setOnItemSelectedListener(this);

		renderSettingState();

		return root;
	}

	void renderSettingState() {
		String region = TimeZone.getDefault().getID().replaceAll(".*/", "")
				.replaceAll("_", " ");
		int hours = Math.abs(TimeZone.getDefault().getRawOffset()) / 3600000;
		int minutes = Math.abs(TimeZone.getDefault().getRawOffset() / 60000) % 60;
		String sign = TimeZone.getDefault().getRawOffset() >= 0 ? "+" : "-";

		String timeZonePretty = String.format("(UTC %s %02d:%02d) %s", sign,
				hours, minutes, region);
		System.out.println(timeZonePretty);

		timeZoneContainer.setText(TimeZone.getDefault().getDisplayName() + " "
				+ timeZonePretty);
		if(dao.getSettingsValue("cityAsr2")!=null){
			cityAsr1.setChecked(false);
			cityAsr2.setChecked(true);
		}else {
			cityAsr1.setChecked(true);
			cityAsr2.setChecked(false);
		}
		if (dao.getSettingsValue("hanfi") != null) {
			hanaFi.setChecked(true);
			icc.setChecked(false);
			cities.setEnabled(false);
			city.setChecked(false);
			cityAsr1.setEnabled(false);
			cityAsr2.setEnabled(false);
		} else if (dao.getSettingsValue("icc") != null) {
			hanaFi.setChecked(false);
			icc.setChecked(true);
			cities.setEnabled(false);
			city.setChecked(false);
			cityAsr1.setEnabled(false);
			cityAsr2.setEnabled(false);
		}
		else if (dao.getSettingsValue("citysetting") != null) {
			hanaFi.setChecked(false);
			icc.setChecked(false);
			city.setChecked(true);
			cities.setEnabled(true);
			cityAsr1.setEnabled(true);
			cityAsr2.setEnabled(true);
			showDilaog = false;
			if (dao.getSettingsValue("city") != null) {
				cities.setSelection(citiesAdapter.getPosition(dao
						.getSettingsValue("city")));
			}else {
				cities.setSelection(citiesAdapter.getPosition("Bergen"));
				dao.saveSetting("city","Bergen");
				
			}
		}
		else if (dao.getSettingsValue("avansert") != null) {
			hanaFi.setChecked(false);
			icc.setChecked(false);
			cities.setEnabled(false);
			city.setChecked(false);
			cityAsr1.setEnabled(false);
			cityAsr2.setEnabled(false);
			
			if(dao.getSettingsValue("cityAsr2")!=null){
				cityAsr2.setChecked(true);
				cityAsr1.setChecked(false);
			}else {
				cityAsr2.setChecked(false);
				cityAsr1.setChecked(true);	
			}
			if (dao.getSettingsValue("jurmethod") != null) {
				jurMethod.setSelection(jurMethodAdapter.getPosition(dao
						.getSettingsValue("jurmethod")));
			}
			if (dao.getSettingsValue("adjustmethod") != null) {
				adjustMethod.setSelection(adjustMethodAdapter.getPosition(dao
						.getSettingsValue("adjustmethod")));
			}
			if (dao.getSettingsValue("location") != null) {
				locationText.setText(dao.getSettingsValue("location"));
			}
			if (dao.getSettingsValue("calcmethod") != null) {
				calcMethod.setSelection(calcAdapter.getPosition(dao
						.getSettingsValue("calcmethod")));
			}

		} else {
			hanaFi.setChecked(true);
			icc.setChecked(false);
			cities.setEnabled(false);
			city.setChecked(false);
			cityAsr1.setEnabled(false);
			cityAsr2.setEnabled(false);
			dao.saveSetting("icc", "true");
		}
	}

	@Override
	public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
			long arg3) {

		if (arg0 == calcMethod) {
			String calcMethod = (String) arg0.getItemAtPosition(arg2);
			dao.saveSetting("calcmethod", calcMethod);
		}
		if (arg0 == adjustMethod) {
			String adjustMethod = (String) arg0.getItemAtPosition(arg2);
			dao.saveSetting("adjustmethod", adjustMethod);
		}
		if (arg0 == jurMethod) {
			String jurMethod = (String) arg0.getItemAtPosition(arg2);
			dao.saveSetting("jurmethod", jurMethod);
		}
		if (arg0 == cities) {
			String city = (String) arg0.getItemAtPosition(arg2);
			dao.saveSetting("city", city);
			Context context = getActivity().getApplicationContext();
			String output = city.substring(0, 1).toUpperCase() + city.substring(1).toLowerCase();
			int duration = Toast.LENGTH_SHORT;
			Toast toast = Toast.makeText(context, output + " er valgt" , duration);
			if(showDilaog && dao.getSettingsValue("citysetting") != null)
				toast.show();
			showDilaog = true;
			
		}

	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
		// TODO Auto-generated method stub

	}

	private Response.Listener<GeolocationSearchResult> createSuccessListener() {
		return new Response.Listener<GeolocationSearchResult>() {
			@Override
			public void onResponse(GeolocationSearchResult response) {

				if (response.getStatus().equals("OK")
						&& response.getResults().length > 0) {
					Geolocation loc = response.getResults()[0];
					dao.saveSetting("location", loc.getFormatted_address());
					dao.saveSetting(
							"lat",
							String.valueOf(loc.getGeometry().getLocation()
									.getLat()));
					dao.saveSetting(
							"lng",
							String.valueOf(loc.getGeometry().getLocation()
									.getLng()));
					locationText.setText(loc.getFormatted_address());
				}
				progressBar.setVisibility(View.GONE);

			}
		};
	}

	private Response.ErrorListener createErrorListener() {
		return new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				progressBar.setVisibility(View.GONE);
				// TODO : Log error and get prey times from local storage
				// error.getStackTrace();
				Log.e("Kunne ikke hente geolcation", error.toString());
			}
		};
	}


	
	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
	
		if (buttonView.equals(hanaFi) && isChecked) {
			dao.saveSetting("hanfi", "true");
			dao.deleteSetting("avansert");
			dao.deleteSetting("citysetting");
			cities.setEnabled(false);
			cityAsr1.setEnabled(false);
			cityAsr2.setEnabled(false);
			city.setChecked(false);
			avansertContainer.setVisibility(View.GONE);
			dao.deleteSetting("icc");
			icc.setChecked(false);
				}
		if (buttonView.equals(icc) && isChecked) {
			dao.saveSetting("icc", "true");
			dao.deleteSetting("hanfi");
			cityAsr1.setEnabled(false);
			cityAsr2.setEnabled(false);
			
			dao.deleteSetting("citysetting");
			cities.setEnabled(false);
			city.setChecked(false);
			dao.deleteSetting("avansert");
			avansertContainer.setVisibility(View.GONE);
			hanaFi.setChecked(false);
				}
		if (buttonView.equals(city) && isChecked) {
			dao.saveSetting("citysetting","true");
			dao.deleteSetting("hanfi");
			dao.deleteSetting("avansert");
			dao.deleteSetting("icc");
			avansertContainer.setVisibility(View.GONE);
			hanaFi.setChecked(false);
			icc.setChecked(false);
			cityAsr1.setEnabled(true);
			cityAsr2.setEnabled(true);
			city.setChecked(true);
			cities.setEnabled(true);
				}
		
		
		if(!icc.isChecked() && !hanaFi.isChecked() && !city.isChecked())
			hanaFi.setChecked(true);
		
		
		

	}

	@Override
	public void onClick(View v) {
		if (v.equals(searchButton)) {
			progressBar.setVisibility(View.VISIBLE);
			String address = location.getText().toString();
			location.setText("");
			geolocationManager.getGeolocation(createSuccessListener(),
					createErrorListener(), address);

		}
		if(v.equals(icc)) {
			Context context = getActivity().getApplicationContext();
			CharSequence text = "Shafi tider er valgt";
			int duration = Toast.LENGTH_SHORT;

			Toast toast = Toast.makeText(context, text, duration);
			toast.show();
		}
		if(v.equals(hanaFi)) {
			Context context = getActivity().getApplicationContext();
			CharSequence text = "Hanafi tider er valgt";
			int duration = Toast.LENGTH_SHORT;

			Toast toast = Toast.makeText(context, text, duration);
			toast.show();
		}
		if(v.equals(cities)){
			String city = dao.getSettingsValue("city");
			Context context = getActivity().getApplicationContext();
			String output = city.substring(0, 1).toUpperCase() + city.substring(1).toLowerCase();
			int duration = Toast.LENGTH_SHORT;
			Toast toast = Toast.makeText(context, output + " er valgt" , duration);
			toast.show();
		}
		if(v.equals(cityAsr1)){
			boolean checked = ((RadioButton) v).isChecked();
			  if (checked){
	            	dao.deleteSetting("cityAsr2");
	            	dao.saveSetting("cityAsr1", "true");
	            }
		}
		if(v.equals(cityAsr2)){
			boolean checked = ((RadioButton) v).isChecked();
			  if (checked){
	            	dao.deleteSetting("cityAsr1");
	            	dao.saveSetting("cityAsr2", "true");
	            }
			
		}

	}

}
