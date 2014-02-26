package com.example.snms.donation;

import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import com.android.volley.toolbox.NetworkImageView;
import com.example.snms.R;
import com.example.snms.R.id;
import com.example.snms.R.layout;
import com.example.snms.images.ImageCacheManager;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.NumberPicker;
import android.widget.TextView;

public class DonationFragment extends Fragment implements OnClickListener, NumberPicker.OnValueChangeListener  {
	
	
	private NumberPicker picker; 
	private Button donationButton; 
	private String[] nums = new String[10];
	private TextView dontationText; 
	private TextView infoButton;
	private ImageView infoImage;
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View root = inflater.inflate(R.layout.donation_wigdet, null);
		picker = (NumberPicker)root.findViewById(R.id.donationPicker);
		donationButton =(Button)root.findViewById(R.id.donerButton);
		infoButton =(TextView)root.findViewById(R.id.moreInfo);
		dontationText = (TextView)root.findViewById(R.id.donationSum);
		infoImage = (ImageView)root.findViewById(R.id.placeHolder3);
		picker.setOnValueChangedListener(this);
		infoButton.setOnClickListener(this);
		infoImage.setOnClickListener(this);
		for(int i=0; i<nums.length; i++)
			nums[i] = Integer.toString(i*50 + 50);

		
		return root;
	}

	/*
	 * image.setImageUrl(newsItem.getImgUrl(),
	 * ImageCacheManager.getInstance().getImageLoader());
	 */

	@Override
	public void onResume() {
		super.onResume();
		donationButton.setOnClickListener(this);
		picker.setMaxValue(nums.length-1);
		picker.setMinValue(0);
		picker.setWrapSelectorWheel(false);
		picker.setDisplayedValues(nums);
		picker.setValue(0);
		dontationText.setText("50kr");

	}

	@Override
	public void onPause() {
		super.onPause();

	}

	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
	}

	public DonationFragment() {
		super();

	}

	@Override
	public void onClick(View v) {
		if(v.equals(donationButton)){
			 Intent smsIntent = new Intent(Intent.ACTION_VIEW);
			    smsIntent.putExtra("sms_body", "Masjid " + (picker.getValue()*50 +50)); 
			    smsIntent.putExtra("address", "1963");
			    smsIntent.setType("vnd.android-dir/mms-sms");
			    startActivity(smsIntent);
		}
		
		//SharedPreferences p = getActivity().get
		
		if(v.equals(infoButton) ||v.equals(infoImage)){
			// 1. Instantiate an AlertDialog.Builder with its constructor
			AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
			// 2. Chain together various setter methods to set the dialog characteristics
			builder.setMessage("Kostnaden for donasjon varierer med bel�pets st�rrelse fra 4,6% til ca. 7%. Donasjonsbel�pet varierer fra 50 kr - 500 kr. Dersom ditt bidrag er st�rre enn 500 kr, m� du donere flere ganger eller overf�re til konto: 6062.05.31599. Merk bel�pet 'Donasjon til SNMS'.");
			// 3. Get the AlertDialog from create()
			AlertDialog dialog = builder.create();
			dialog.show();
		}
		
		
		
	}

	@Override
	public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
		  dontationText.setText(newVal*50+50 + "kr");
	}


}
