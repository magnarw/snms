package com.example.snms.news;

import android.net.Uri;
import android.util.Log;

import com.android.volley.Request.Method;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.example.snms.domain.NewsItem;
import com.example.snms.network.GsonRequest;
import com.example.snms.network.RequestManager;

public class NewsManager {

	private final String TAG = getClass().getSimpleName();
	private static NewsManager mInstance;

	private static String NEWS_BASE =  "http://54.217.1.169:8080/snmscms/rest/json/news";
	private static String NEWS_NUMBER_OF_RESULTS = "CapTech";
	private static String NEWS_PAGENING = "CapTech";


	public static NewsManager getInstance(){
		if(mInstance == null) {
			mInstance = new NewsManager();
		}

		return mInstance;
	}

	public void getNews(Listener< NewsItem[]> listener, ErrorListener errorListener, int pageSize, int pageNum,int filter){
		Uri.Builder uriBuilder = Uri.parse(NEWS_BASE).buildUpon().appendQueryParameter("filter",String.valueOf(filter)).
		appendQueryParameter("pageSize", String.valueOf(pageSize)).
		appendQueryParameter("pageNumber", String.valueOf(pageNum));
		
		
		String uri = uriBuilder.build().toString();
		Log.i(TAG, "getTweetForHashtag: uri = " + uri);

		GsonRequest< NewsItem[]> request = new GsonRequest< NewsItem[]>(Method.GET
				, uri
				,  NewsItem[].class
				, listener
				, errorListener);
		Log.v(TAG, request.toString());
		RequestManager.getRequestQueue().add(request);
	}

}
