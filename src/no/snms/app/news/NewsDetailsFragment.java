package no.snms.app.news;

import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import no.snms.app.domain.NewsItem;
import no.snms.app.images.ImageCacheManager;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import com.android.volley.toolbox.NetworkImageView;
import no.snms.app.R;
import no.snms.app.R.id;
import no.snms.app.R.layout;

import android.annotation.SuppressLint;
import android.app.Dialog;
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
import android.view.Window;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

@SuppressLint("ValidFragment")
public class NewsDetailsFragment extends Fragment implements OnClickListener {

	NewsItem newsItem;

	// News stuff
	TextView createdDate;
	RelativeLayout addToCalender;
	TextView text;
	TextView newstext2;
	TextView title;
//	TextView authorTag;
	TextView ingress;
	NetworkImageView imageHeader;
	NetworkImageView image;
	NetworkImageView mapImage;
	TextView imageText;
	TextView articleImageText;
	// Event stuff
	TextView timeFrom;
	TextView addressLine1;
	TextView addressLine2;
	TextView monthText; 
	TextView monthNumber; 
	ImageView addTocal;
	RelativeLayout latestNewsContainer;
	public NewsDetailsFragment() {
		super();
	}

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		Bundle bundle = this.getArguments();
		this.newsItem  =(NewsItem) bundle.getSerializable("newsItem");
		if (this.newsItem.getCat()==1
				|| this.newsItem.getCat()==2) {
			View root = inflater.inflate(R.layout.news_widget, null);
			 latestNewsContainer =(RelativeLayout) root.findViewById(R.id.latestNewsContainer);
			imageHeader = (NetworkImageView) root
					.findViewById(R.id.headerImage1);
			image = (NetworkImageView) root.findViewById(R.id.newsImage);
		//	authorTag =(TextView) root.findViewById(R.id.authorTag);
			imageText = (TextView) root.findViewById(R.id.headerText1);
			createdDate = (TextView) root.findViewById(R.id.createdTag);
			ingress = (TextView) root.findViewById(R.id.newsIngress);
			text = (TextView) root.findViewById(R.id.Newstext);
			newstext2 =  (TextView) root.findViewById(R.id.Newstext2); 
			articleImageText = (TextView)root.findViewById(R.id.articleImageText); 
			// ingress = (TextView) root.findViewById(R.id.newsIngress);
			// text = (TextView) root.findViewById(R.id.newsIngress);
			return root;
		} else {
			View root = inflater.inflate(R.layout.event_widget, null);
			addToCalender = (RelativeLayout) root.findViewById(R.id.timeWrapper);
			addToCalender.setOnClickListener(this);
			latestNewsContainer =(RelativeLayout) root.findViewById(R.id.latestNewsContainer);
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
			addTocal = (ImageView)root.findViewById(R.id.addTocal); 
			addTocal.setOnClickListener(this);
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
			
			imageHeader.setImageUrl(getVersion("small", newsItem.getImgUrl()), ImageCacheManager
					.getInstance().getImageLoader());
			
			
			imageHeader.setOnClickListener(this);
			//150;
			if(newsItem.getArticleImageUrl()!=null) {
				image.setImageUrl(getVersion("lagre", newsItem.getArticleImageUrl()), ImageCacheManager
						.getInstance().getImageLoader());
				image.setOnClickListener(this);
			}
			imageText.setText(newsItem.getTitle());
			
			DateTimeFormatter formatter = DateTimeFormat.forPattern("'Publisert' dd.MMM yyyy");
			String created = formatter.print(newsItem.getCreatedDate());
			
			
			
			String newsText1 = "";
			String newsText2 = "";
			articleImageText.setText(newsItem.getArticleImageText());
			
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
		//	authorTag.setText(newsItem.getAuthor());
			
			
			text.setText(newsText1);
			newstext2.setText(newsText2);
		} else {
		String gmapsUrl = "http://maps.googleapis.com/maps/api/staticmap?center="+newsItem.getLat()+","+newsItem.getLng()+"&zoom=15&size=600x500&sensor=false&markers=color:blue%7Clabel:S%7C"+newsItem.getLat()+","+newsItem.getLng();	
			mapImage.setImageUrl(gmapsUrl, ImageCacheManager
					.getInstance().getImageLoader());
			mapImage.setOnClickListener(this);

			imageHeader.setImageUrl(getVersion("medium", newsItem.getImgUrl()), ImageCacheManager
					.getInstance().getImageLoader());
			
			imageHeader.setOnClickListener(this);
			
			DateTime from = newsItem.getFrom();
			DateTime to = newsItem.getTo();
			imageText.setText(newsItem.getTitle());
			DateTimeFormatter formatter = DateTimeFormat.forPattern("EEEE dd.MMM 'kl' HH:mm");
			timeFrom.setText(formatter.print(from));
			text.setText(newsItem.getText());
			String [] address = {""};
			if( newsItem.getAddress()!=null)
				address =  newsItem.getAddress().split(",");
			
			DateTimeFormatter formatterMonth = DateTimeFormat.forPattern("MMM");
			DateTimeFormatter formatterDay = DateTimeFormat.forPattern("dd");
			String formatedMonth = formatterMonth.print(newsItem.getFrom());
			String formatedDay = formatterDay.print(newsItem.getFrom());

			monthNumber.setText(formatedDay);
			monthText.setText(formatedMonth.toUpperCase());
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
	
	public void createImageDialog(String url){
		Dialog settingsDialog = new Dialog(getActivity());
		settingsDialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
		
		View root = getActivity().getLayoutInflater().inflate(R.layout.imagepopup, null);
		NetworkImageView v =(NetworkImageView) root.findViewById(R.id.image);
		v.setImageUrl(url, ImageCacheManager
				.getInstance().getImageLoader());
		settingsDialog.setContentView(root);
		settingsDialog.show();
		
	}
	

	@Override
	public void onClick(View v) {
		if(v.equals(mapImage)){
			String uri = String.format(Locale.ENGLISH, "geo:%f,%f?z=%d&q=%f,%f (%s)", newsItem.getLat(), newsItem.getLng(),10,  newsItem.getLat(), newsItem.getLng(), newsItem.getTitle());
			Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
			getActivity().startActivity(intent);
		}
		if(v.equals(timeFrom) || v.equals(addTocal)){
				Intent intent = new Intent(Intent.ACTION_EDIT);
				addToCalender.requestFocus();
				intent.setType("vnd.android.cursor.item/event");
				intent.putExtra("beginTime",newsItem.getFrom().getMillis());
				intent.putExtra("allDay", false);
				intent.putExtra("rrule", "FREQ=YEARLY");
				if(newsItem.getTo()!=null)
					intent.putExtra("endTime", newsItem.getTo().getMillis());
				intent.putExtra("title", newsItem.getTitle());
				intent.putExtra("eventLocation", newsItem.getAddress());
				startActivity(intent);
				addToCalender.setPressed(true);
			}
	
		if(v.equals(imageHeader)){
			this.createImageDialog(getVersion("lagre", newsItem.getImgUrl()));
		}
		if(v.equals(image)){
			this.createImageDialog(getVersion("lagre", newsItem.getArticleImageUrl()));
		}
		
		
	}
	
	
	private String getVersion(String version,String imageUrl){
		
		String [] temp = imageUrl.split("\\.");
		
		String toReturn = "";
		for(int i = 0; i<temp.length;i++){
			if(i == temp.length-2)
				toReturn +=  temp[i] + "_" + version + ".";
			else if(i == temp.length -1) 
				toReturn+= temp[i];
			else 
				toReturn+= temp[i] + ".";
		}
		
		return toReturn;
		
	}


}
