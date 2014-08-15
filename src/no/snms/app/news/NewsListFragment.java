package no.snms.app.news;

import java.util.List;

import no.snms.app.BaseActivity;
import no.snms.app.HolidayListFragment.HolidayListAdapter;
import no.snms.app.domain.HolydayItem;
import no.snms.app.domain.NewsItem;
import no.snms.app.images.ImageCacheManager;
import no.snms.app.network.GsonRequest;

import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.Request.Method;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.Volley;
import no.snms.app.R;
import no.snms.app.R.id;
import no.snms.app.R.layout;
import com.google.gson.Gson;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
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
	   
	   Bundle args = new Bundle();
	   args.putSerializable("newsItem", clickedDetail);
	   
	   NewsDetailsFragment myDetailFragment = new NewsDetailsFragment(clickedDetail);
	   myDetailFragment.setArguments(args);
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
			
			NewsManager.getInstance().getNews(createSuccessListener(), createErrorListener(),PAGE_SIZE_FOR_NEWS,0,1,false);
			progressBar.setVisibility(View.VISIBLE);
		}else {
			progressBar.setVisibility(View.GONE);
		}
	
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
					if(notInListAllready(item))
						adapter.add(item);
				}
				adapter.notifyDataSetChanged();
				isLoading = false;
			}
	    };	
	}
	
	private boolean notInListAllready(NewsItem item) {
		int count = adapter.getCount();
		
		for(int i = 0;i<count;i++){
			NewsItem tempItem = adapter.getItem(i);
			if(tempItem.get_id().equals(item.get_id()))
					return false;
		}
		return true;
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
			NewsManager.getInstance().getNews(createSuccessListener(), createErrorListener(),PAGE_SIZE_FOR_NEWS,nextPage,1,false);
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
			
			//Publisert 04.jan 2014 
			
			DateTimeFormatter formatter = DateTimeFormat.forPattern("'Publisert' dd.MMM yyyy");
			String created = formatter.print(h.getCreatedDate());
			text.setTextColor(Color.BLACK);
			text.setText(created);
			try {
			image.setImageUrl(getVersion("extrasmall", h.getImgUrl()), ImageCacheManager.getInstance().getImageLoader());
			} catch(Exception e){
				e.printStackTrace();
			}
		//	text.setText(h.getText());
			return convertView;
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
