package com.example.snms;

import android.content.Context;
import android.widget.ArrayAdapter;

import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.NetworkImageView;
import com.example.snms.domain.NewsItem;

public class NewsArrayAdpater extends ArrayAdapter<String> implements Listener<NewsItem>, ErrorListener {

	public NewsArrayAdpater(Context context, int resource) {
		super(context, resource);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onErrorResponse(VolleyError error) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onResponse(NewsItem response) {
		// TODO Auto-generated method stub
		
	}



}
