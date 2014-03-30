package com.example.snms.about;

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
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class AboutSnmsFragment extends Fragment implements OnClickListener {

	// Event stuff
	TextView timeFrom;
	TextView addressLine1;
	TextView addressLine2;
	TextView monthText; 
	TextView monthNumber; 
	ImageView image;
	TextView imageText;
	ImageView imageHeader;
	ImageView mapImage;
	ImageView faceImage;
	ImageView webImage;
	TextView faceText;



	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

			View root = inflater.inflate(R.layout.about_widget, null);
			faceText = (TextView)inflater.inflate(R.id.faceText, null); 
			image = (NetworkImageView) root.findViewById(R.id.newsImage);
			imageText = (TextView) root.findViewById(R.id.headerText1);
			imageHeader = (ImageView) root
					.findViewById(R.id.headerImage1);
			mapImage = (ImageView) root
					.findViewById(R.id.mapImage);
			
			
			addressLine1 = (TextView) root.findViewById(R.id.addressLine1);
			addressLine2 = (TextView) root.findViewById(R.id.addressLine2);
			monthText = (TextView) root.findViewById(R.id.dateWrapMonthText);
			monthNumber = (TextView) root.findViewById(R.id.dateWrapMonthNumber); 
			faceImage = (ImageView)root.findViewById(R.id.facebookImage); 
			webImage = (ImageView)root.findViewById(R.id.webpageImageLink);
			webImage.setOnClickListener(this);
			faceImage.setOnClickListener(this);
			faceText.setOnClickListener(this);
			//text = (TextView) root.findViewById(R.id.Newstext);
			return root;
		}

		/*
		 * image.setImageUrl(newsItem.getImgUrl(),
		 * ImageCacheManager.getInstance().getImageLoader());
		 */


	@Override
	public void onResume() {
		super.onResume();
		mapImage.setOnClickListener(this);
			
		
	}

	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
	}

	public AboutSnmsFragment() {
		super();

	}

	@Override
	public void onClick(View v) {
		if(v.equals(mapImage)){
			String uri = String.format(Locale.ENGLISH, "geo:%f,%f?z=%d&q=%f,%f (%s)", 59.84703700000001, 10.828062300000056,10,  10.828062300000056, 59.84703700000001, "SNMS");
			Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
			getActivity().startActivity(intent);
		}
		if(v.equals(webImage)){
			Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.muslimskesenter.no"));
			startActivity(browserIntent);
		}
		if(v.equals(faceImage)){
			Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/pages/S%C3%B8ndre-Nordstrand-Muslimske-Senter/312656635444443?fref=ts"));
			startActivity(browserIntent);
		}if(v.equals(faceText)){
			Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/pages/S%C3%B8ndre-Nordstrand-Muslimske-Senter/312656635444443?fref=ts"));
			startActivity(browserIntent);
		}
	}


}
