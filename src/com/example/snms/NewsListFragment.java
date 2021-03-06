package com.example.snms;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.Request.Method;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.Volley;
import com.example.snms.HolidayListFragment.HolidayListAdapter;
import com.example.snms.domain.HolydayItem;
import com.example.snms.domain.NewsItem;
import com.example.snms.domain.image.ImageCacheManager;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView.FindListener;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class NewsListFragment extends ListFragment {

	private NewsListAdapter adapter;
	private View mheaderView;
	boolean isLoading = false; 

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		   super.onCreateView(inflater, container,
			        savedInstanceState);

		   mheaderView = inflater.inflate(R.layout.news_header, null);
		   return inflater.inflate(R.layout.listnews, container,false);

	}
	
	
	@Override
	  public void onListItemClick(ListView l, View v, int position, long id) {
	   NewsItem clickedDetail = (NewsItem)l.getItemAtPosition(position);
	   NewsDetailsFragment myDetailFragment = new NewsDetailsFragment(clickedDetail);
	   switchFragment(myDetailFragment,null);

	  }
	
	
	private void switchFragment(Fragment fragment1, Fragment fragment2) {
		if (getActivity() == null)
			return;
		
		if (getActivity() instanceof BaseActivity) {
			BaseActivity fca = (BaseActivity) getActivity();
			fca.switchContent(fragment1, fragment2);
		} 
	}
	

	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		adapter = new NewsListAdapter(getActivity());
		putPreyItemsOnRequestQueue();
	//	ListView v = (ListView)getView().findViewById(android.R.id.list);
	//	v.addHeaderView(mheaderView);
	     getListView().addHeaderView(mheaderView);
		//getListView().addHeaderView(mheaderView);
		setListAdapter(adapter);
		this.getListView().setOnScrollListener(new NewsScrollListner());
	}
	
	private void putPreyItemsOnRequestQueue() {
	    GsonRequest<NewsItem[]> jsObjRequest = new GsonRequest<NewsItem[]> (
	        Method.GET,
	        "http://46.137.184.176:3000/api/news",
	        NewsItem[].class,
	        this.createSuccessListener(),
	        this.createErrorListener());
	    RequestManager.getRequestQueue().add(jsObjRequest);
	}
			
	private Response.Listener <NewsItem[]> createSuccessListener() {
	    return new Response.Listener <NewsItem[]>() {
	       
	    	@Override
			public void onResponse(NewsItem[] response) {
	    		adapter.clear();
				adapter.addAll(response);
				adapter.notifyDataSetChanged();
				isLoading = false;
			}
	    };	
	}
	
	private Response.ErrorListener createErrorListener() {
	    return new Response.ErrorListener() {
	        @Override
	        public void onErrorResponse(VolleyError error) {
	        	//TODO : Log error and get prey times from local storage
	            //error.getStackTrace();
	        	Log.e("error", "could not fetch data");
	        }
	    };
	}
	
	
	public class NewsListAdapter extends ArrayAdapter<NewsItem> {

		public NewsListAdapter(Context context) {
			super(context, 0);
			
		}
		
		public View getView(int position, View convertView, ViewGroup parent) {
			if (convertView == null) {
				convertView = LayoutInflater.from(getContext()).inflate(R.layout.news_row, null);
			}
			TextView title = (TextView) convertView.findViewById(R.id.row_news_title);
		//	TextView text = (TextView) convertView.findViewById(R.id.row_news_ingress);
			NetworkImageView image = (NetworkImageView)convertView.findViewById(R.id.newsImage);
			NewsItem h =  getItem(position);
			title.setText(getItem(position).getTitle());
			Uri uri = Uri.parse(h.getImgUrl());
		//	text.setText(h.getText());
			image.setImageUrl(h.getImgUrl(), ImageCacheManager.getInstance().getImageLoader());
			return convertView;
		}
}
	
	public class NewsScrollListner implements OnScrollListener  {
		
		int currentFirstVisibleItem;
		int  currentVisibleItemCount;
		int currentScrollState;
	
		
		
		@Override
		public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
		    this.currentFirstVisibleItem = firstVisibleItem;
		    this.currentVisibleItemCount = visibleItemCount;
		}
		
		@Override
		public void onScrollStateChanged(AbsListView view, int scrollState) {
		    this.currentScrollState = scrollState;
		    this.isScrollCompleted();
		 }
		
		void loadMoreData() {
			putPreyItemsOnRequestQueue();
		}

		private void isScrollCompleted() {
		    if (this.currentVisibleItemCount > 0 && this.currentScrollState == SCROLL_STATE_IDLE) {
		        /*** In this way I detect if there's been a scroll which has completed ***/
		        /*** do the work for load more date! ***/
		        if(!isLoading){
		             isLoading = true;
		             loadMoreData();
		        }
		    }
		}



		
		
	}
	
	
}
