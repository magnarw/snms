package com.example.snms.news;

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
import android.widget.TextView;

@SuppressLint("ValidFragment")
public class NewsDetailsFragment extends Fragment implements OnClickListener {

	NewsItem newsItem;

	// News stuff
	TextView createdDate;
	TextView text;
	TextView newstext2;
	TextView title;
	TextView authorTag;

	TextView ingress;
	NetworkImageView imageHeader;
	NetworkImageView image;
	NetworkImageView mapImage;
	TextView imageText;

	// Event stuff
	TextView timeFrom;
	TextView addressLine1;
	TextView addressLine2;
	TextView monthText; 
	TextView monthNumber; 

	public NewsDetailsFragment() {
		super();
	}

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		if (this.newsItem.getCat()==1
				|| this.newsItem.getCat()==2) {
			View root = inflater.inflate(R.layout.news_widget, null);
			imageHeader = (NetworkImageView) root
					.findViewById(R.id.headerImage1);
			image = (NetworkImageView) root.findViewById(R.id.newsImage);
			authorTag =(TextView) root.findViewById(R.id.authorTag);
			imageText = (TextView) root.findViewById(R.id.headerText1);
			createdDate = (TextView) root.findViewById(R.id.createdTag);
			ingress = (TextView) root.findViewById(R.id.newsIngress);
			text = (TextView) root.findViewById(R.id.Newstext);
			newstext2 =  (TextView) root.findViewById(R.id.Newstext2); 
			// ingress = (TextView) root.findViewById(R.id.newsIngress);
			// text = (TextView) root.findViewById(R.id.newsIngress);
			return root;
		} else {
			View root = inflater.inflate(R.layout.event_widget, null);
			image = (NetworkImageView) root.findViewById(R.id.newsImage);
			imageText = (TextView) root.findViewById(R.id.headerText1);
			imageHeader = (NetworkImageView) root
					.findViewById(R.id.headerImage1);
			mapImage = (NetworkImageView) root
					.findViewById(R.id.mapImage);
			addressLine1 = (TextView) root.findViewById(R.id.addressLine1);
			addressLine2 = (TextView) root.findViewById(R.id.addressLine2);
			monthText = (TextView) root.findViewById(R.id.dateWrapMonthText);
			monthNumber = (TextView) root.findViewById(R.id.dateWrapMonthNumber); 
			
			timeFrom = (TextView) root.findViewById(R.id.timeText);
			timeFrom.setOnClickListener(this);
			text = (TextView) root.findViewById(R.id.Newstext);
			return root;
		}

		/*
		 * image.setImageUrl(newsItem.getImgUrl(),
		 * ImageCacheManager.getInstance().getImageLoader());
		 */

	}

	@Override
	public void onResume() {
		super.onResume();
		if (this.newsItem.getCat()==1
				|| this.newsItem.getCat()==2) {
			Uri uri = Uri.parse(newsItem.getImgUrl());
			imageHeader.setImageUrl(newsItem.getImgUrl(), ImageCacheManager
					.getInstance().getImageLoader());
			if(newsItem.articleImageUrl!=null) {
				image.setImageUrl(newsItem.getArticleImageUrl(), ImageCacheManager
						.getInstance().getImageLoader());
			}
			imageText.setText(newsItem.getTitle());
			DateTimeFormatter formatter = DateTimeFormat.forPattern("EEEE MM.dd hh:mm");
			String created = formatter.print(newsItem.getCreatedDate());
			
			String newsText1 = "";
			String newsText2 = "";
		
			
			if(newsItem.getText().split("#avsnitt").length>0){
				newsText1 =newsItem.getText().split("#avsnitt")[0];
			    for(int i = 1;i<newsItem.getText().split("#avsnitt").length;i++) {
			    	newsText2+=newsItem.getText().split("#avsnitt")[i];
			    }
			}else {
				newsText1 = newsItem.getText();
			}
			
			createdDate.setText(created);
			ingress.setText(newsItem.getIngress());
			authorTag.setText(newsItem.getAuthor());
			text.setText(newsText1);
			newstext2.setText(newsText2);
		} else {
		String gmapsUrl = "http://maps.googleapis.com/maps/api/staticmap?center="+newsItem.getLat()+","+newsItem.getLng()+"&zoom=15&size=600x500&sensor=false&markers=color:blue%7Clabel:S%7C"+newsItem.getLat()+","+newsItem.getLng();	
			mapImage.setImageUrl(gmapsUrl, ImageCacheManager
					.getInstance().getImageLoader());
			mapImage.setOnClickListener(this);
			imageHeader.setImageUrl(newsItem.getImgUrl(), ImageCacheManager
					.getInstance().getImageLoader());
			DateTime from = newsItem.getFrom();
			DateTime to = newsItem.getTo();
			imageText.setText(newsItem.getTitle());
			from = from.plusHours(1);
			
			DateTimeFormatter formatter = DateTimeFormat.forPattern("EEEE, MMM d");
			DateTimeFormatter formatter2 = DateTimeFormat.forPattern("kk:mm");
			DateTimeFormatter formatterMonth = DateTimeFormat.forPattern("MM");
			DateTimeFormatter formatterDay = DateTimeFormat.forPattern("dd");
			
			String formattedDate = formatter.print(from);
			String formatedTime = formatter2.print(from);
			
			String formatedMonth = formatterMonth.print(from);
			String formatedDay = formatterDay.print(from);
			
			monthNumber.setText(formatedDay);
			monthText.setText(formatedMonth.toUpperCase());
			
			timeFrom.setText(formattedDate + " klokken " + formatedTime);
			text.setText(newsItem.getText());
			String [] address =  newsItem.getAddress().split(",");
			if(address.length>1){
				addressLine1.setText(address[0]);
				String addressLine2 = "";
				for(int i = 1;i<address.length;i++){
					addressLine2+=address[i].trim()+"\n";
				}
				this.addressLine2.setText(addressLine2);
			}else {
				addressLine1.setText(newsItem.getAddress());
			}
			
		}
		// ingress.setText(newsItem.getIngress());
	}

	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
	}

	public NewsDetailsFragment(NewsItem newsItem) {
		super();
		this.newsItem = newsItem;

	}

	@Override
	public void onClick(View v) {
		if(v.equals(mapImage)){
			String uri = String.format(Locale.ENGLISH, "geo:%f,%f?z=%d&q=%f,%f (%s)", newsItem.getLat(), newsItem.getLng(),10,  newsItem.getLat(), newsItem.getLng(), newsItem.getTitle());
			Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
			getActivity().startActivity(intent);
		}
		if(v.equals(timeFrom)){
			String[] projection = new String[] { "_id", "name" };
			Uri calendars = Uri.parse("content://calendar/calendars");
			     
			Cursor managedCursor =
			   getActivity().managedQuery(calendars, projection, null, null, null);
			if (managedCursor.moveToFirst()) {
				 String calName; 
				 String calId; 
				 int nameColumn = managedCursor.getColumnIndex("name"); 
				 int idColumn = managedCursor.getColumnIndex("_id");
				 do {
				    calName = managedCursor.getString(nameColumn);
				    calId = managedCursor.getString(idColumn);
				 } while (managedCursor.moveToNext());
				 ContentValues event = new ContentValues();
				 event.put("calendar_id", calId);
				 event.put("title", newsItem.getTitle());
				 event.put("description", newsItem.getText());
				 event.put("eventLocation",newsItem.getAddress());
				 event.put("dtstart", newsItem.getFrom().getMillis());
				 event.put("dtend", newsItem.getTo().getMillis());
				  Uri eventsUri = Uri.parse("content://calendar/events");
				  Uri url = getActivity().getContentResolver().insert(eventsUri, event);
			}
		}
		
	}


}
