package com.example.snms.news;

import java.util.List;

import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.Request.Method;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.Volley;
import com.example.snms.BaseActivity;
import com.example.snms.R;
import com.example.snms.HolidayListFragment.HolidayListAdapter;
import com.example.snms.R.id;
import com.example.snms.R.layout;
import com.example.snms.domain.HolydayItem;
import com.example.snms.domain.NewsItem;
import com.example.snms.images.ImageCacheManager;
import com.example.snms.network.GsonRequest;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

public class NewsListFragment extends ListFragment {

	private NewsListAdapter adapter;
	boolean isLoading = false; 
	ProgressBar progressBar;
	TextView errorMessage;
	TextView newslistheader;
	Integer lastLoadedPage =-1;
	
	private static Integer PAGE_SIZE_FOR_NEWS = 5; 
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		   super.onCreateView(inflater, container,
			        savedInstanceState);

		  View root = inflater.inflate(R.layout.listnews, container,false);
		  newslistheader= (TextView) root.findViewById(R.id.newslistheader);
		  progressBar = (ProgressBar)root.findViewById(R.id.progress);
		  return root; 

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
	
	@Override
	public void onResume() {
		super.onResume();
		newslistheader.setText("NYHETER");
		if(getListView().getAdapter() == null) {
			// Get the first page
			isLoading = true;
			lastLoadedPage = 0; 
			
			NewsManager.getInstance().getNews(createSuccessListener(), createErrorListener(),PAGE_SIZE_FOR_NEWS,0,1);
		}
		progressBar.setVisibility(View.VISIBLE);
	}
	
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
	}
	

			
	private Response.Listener <NewsItem[]> createSuccessListener() {
	    return new Response.Listener <NewsItem[]>() {
	    	@Override
			public void onResponse(NewsItem[] response) {
	    		progressBar.setVisibility(View.GONE);
	    		if(adapter==null) {
	    			adapter = new NewsListAdapter(getActivity());
	    			setListAdapter(adapter);
	    		}
				for(NewsItem item : response) {
					adapter.add(item);
				}
				adapter.notifyDataSetChanged();
				isLoading = false;
			}
	    };	
	}
	
	private Response.ErrorListener createErrorListener() {
	    return new Response.ErrorListener() {
	        @Override
	        public void onErrorResponse(VolleyError error) {
	        	progressBar.setVisibility(View.GONE);
	        	//TODO : Log error and get prey times from local storage
	            //error.getStackTrace();
	        	Log.e("error",error.toString());
	        }
	    };
	}
	
	
	public class NewsListAdapter extends ArrayAdapter<NewsItem> {
		
		
		private boolean shouldLoadMoreData(int count, int position){
			// If showing the last set of data, request for the next set of data
			boolean scrollRangeReached = (position > (count - PAGE_SIZE_FOR_NEWS));
			NewsItem item = getItem(position);
			boolean value =  (scrollRangeReached && !isLoading && item.getHasMoreElements() && item.getNextPage()>lastLoadedPage);
			return value;
		}

		private void loadMoreData(int nextPage){
			progressBar.setVisibility(View.VISIBLE);
			isLoading = true;
			lastLoadedPage = nextPage;
			Log.v(getClass().toString(), "Load more tweets");
			NewsManager.getInstance().getNews(createSuccessListener(), createErrorListener(),PAGE_SIZE_FOR_NEWS,nextPage,1);
		}

		
		
		public NewsListAdapter(Context context) {
			super(context, 0);
			
		}
		
		public View getView(int position, View convertView, ViewGroup parent) {
			if (convertView == null) {
				convertView = LayoutInflater.from(getContext()).inflate(R.layout.news_row, null);
			}
			if(shouldLoadMoreData(this.getCount(), position) ) {
				loadMoreData(getItem(position).getNextPage());
			}
			
			TextView title = (TextView) convertView.findViewById(R.id.row_news_title);
			TextView text = (TextView) convertView.findViewById(R.id.row_news_created);
			
			NetworkImageView image = (NetworkImageView)convertView.findViewById(R.id.newsImage);
			NewsItem h =  getItem(position);
			title.setText(getItem(position).getTitle());
			DateTimeFormatter formatter = DateTimeFormat.forPattern("EEEE MM.dd hh:mm");
			String created = formatter.print(h.getCreatedDate());
			text.setText(created);
			try {
			Uri uri = Uri.parse(h.getImgUrl()+"?w=" + image.getWidth() +"&h="+ image.getHeight()); 
			image.setImageUrl(h.getImgUrl()+"?w=300&h=300", ImageCacheManager.getInstance().getImageLoader());
			} catch(Exception e){
				e.printStackTrace();
			}
		//	text.setText(h.getText());
		
			return convertView;
		}
	
		
}
	

	
	
}
