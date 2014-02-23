package com.example.snms.settings;

import java.util.TimeZone;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.snms.PreyOverView;
import com.example.snms.R;
import com.example.snms.database.SnmsDAO;
import com.example.snms.domain.Geolocation;
import com.example.snms.domain.GeolocationSearchResult;
import com.example.snms.domain.Geometry;
import com.example.snms.news.NewsItem;
import com.example.snms.news.NewsListFragment.NewsListAdapter;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;
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
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

public class SettingsFragment extends Fragment implements
		OnItemSelectedListener, OnCheckedChangeListener, OnClickListener {

	SnmsDAO dao;

	EditText location;
	Spinner calcMethod;
	Spinner jurMethod;
	Spinner adjustMethod;
	CheckBox hanaFi;
	CheckBox icc;
	CheckBox avansert;
	Button searchButton;
	RelativeLayout avansertContainer;
	ArrayAdapter<CharSequence> locationAdapter;
	ArrayAdapter<CharSequence> calcAdapter;
	ArrayAdapter<CharSequence> adjustMethodAdapter;
	ArrayAdapter<CharSequence> jurMethodAdapter;
	GeolocationManager geolocationManager = GeolocationManager.getInstance();
	ProgressBar progressBar;
	TextView locationText;
	TextView timeZoneContainer;

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);

		dao = ((PreyOverView) getActivity()).getDAO();
		View root = inflater.inflate(R.layout.settings, null);

		hanaFi = (CheckBox) root.findViewById(R.id.hanafi);
		icc = (CheckBox) root.findViewById(R.id.icc);
		avansert = (CheckBox) root.findViewById(R.id.avansert);
		progressBar = (ProgressBar) root.findViewById(R.id.progressBar);
		progressBar.setVisibility(View.GONE);
		locationText = (TextView) root.findViewById(R.id.locationContainer);
		timeZoneContainer = (TextView) root
				.findViewById(R.id.timeZoneContainer);
		hanaFi.setOnCheckedChangeListener(this);
		icc.setOnCheckedChangeListener(this);
		avansert.setOnCheckedChangeListener(this);
		avansertContainer = (RelativeLayout) root
				.findViewById(R.id.avansertContainer);
		searchButton = (Button) root.findViewById(R.id.search);
		searchButton.setOnClickListener(this);
		location = (EditText) root.findViewById(R.id.location);
		calcMethod = (Spinner) root.findViewById(R.id.calcMethod);
		adjustMethod = (Spinner) root.findViewById(R.id.adjustMethod);
		jurMethod = (Spinner) root.findViewById(R.id.jurMethod);

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

		if (dao.getSettingsValue("hanfi") != null) {
			hanaFi.setChecked(true);
			icc.setChecked(false);
			avansert.setChecked(false);
		} else if (dao.getSettingsValue("icc") != null) {
			hanaFi.setChecked(false);
			icc.setChecked(true);
			avansert.setChecked(false);
		} else if (dao.getSettingsValue("avansert") != null) {
			hanaFi.setChecked(false);
			icc.setChecked(false);
			avansert.setChecked(true);

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
			hanaFi.setChecked(false);
			icc.setChecked(true);
			avansert.setChecked(false);
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
		if (buttonView.equals(avansert) && isChecked) {
			dao.saveSetting("avansert", "true");
			dao.deleteSetting("hanfi");
			avansertContainer.setVisibility(View.VISIBLE);
			dao.deleteSetting("icc");
			icc.setChecked(false);
			hanaFi.setChecked(false);
		}
		if (buttonView.equals(hanaFi) && isChecked) {
			dao.saveSetting("hanfi", "true");
			dao.deleteSetting("avansert");
			avansertContainer.setVisibility(View.GONE);
			dao.deleteSetting("icc");
			icc.setChecked(false);
			avansert.setChecked(false);
		}
		if (buttonView.equals(icc) && isChecked) {
			dao.saveSetting("icc", "true");
			dao.deleteSetting("hanfi");
			dao.deleteSetting("avansert");
			avansertContainer.setVisibility(View.GONE);
			hanaFi.setChecked(false);
			avansert.setChecked(false);
		}

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

	}

}
