package no.snms.app.about;

import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import no.snms.app.images.ImageCacheManager;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import com.android.volley.toolbox.NetworkImageView;
import no.snms.app.R;
import no.snms.app.R.id;
import no.snms.app.R.layout;

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
import android.widget.RelativeLayout;
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
	RelativeLayout likeOnFaceWraper;
	RelativeLayout webpageContainer;

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

			View root = inflater.inflate(R.layout.about_widget, null);
			faceText = (TextView)root.findViewById(R.id.faceText); 
			image = (NetworkImageView) root.findViewById(R.id.newsImage);
			imageText = (TextView) root.findViewById(R.id.headerText1);
			imageHeader = (ImageView) root
					.findViewById(R.id.headerImage1);
			mapImage = (ImageView) root
					.findViewById(R.id.mapImage);
			
			likeOnFaceWraper = (RelativeLayout)root.findViewById(R.id.timeWrapper);
			likeOnFaceWraper.setOnClickListener(this);
			
			webpageContainer = (RelativeLayout)root.findViewById(R.id.webpageContainer);
			webpageContainer.setOnClickListener(this);
			
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
			String uri = String.format(Locale.ENGLISH, "geo:%f,%f?z=%d&q=%f,%f (%s)",  10.828062300000056,59.84703700000001,10,  10.828062300000056, 59.84703700000001, "SNMS");
			Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
			webpageContainer.setPressed(true);
			getActivity().startActivity(intent);
		}
		if(v.equals(webImage)){
			Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.muslimskesenter.no"));
			startActivity(browserIntent);
			webpageContainer.setPressed(true);
		}
		if(v.equals(webpageContainer)){
			Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.muslimskesenter.no"));
			startActivity(browserIntent);
		}
		if(v.equals(faceImage)){
			Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/pages/S%C3%B8ndre-Nordstrand-Muslimske-Senter/312656635444443?fref=ts"));
			startActivity(browserIntent);
			likeOnFaceWraper.setPressed(true);
		}if(v.equals(faceText)){
			Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/pages/S%C3%B8ndre-Nordstrand-Muslimske-Senter/312656635444443?fref=ts"));
			startActivity(browserIntent);
			likeOnFaceWraper.setPressed(true);
		}if(v.equals(likeOnFaceWraper)){
			Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/pages/S%C3%B8ndre-Nordstrand-Muslimske-Senter/312656635444443?fref=ts"));
			startActivity(browserIntent);
			
		}
	}


}
