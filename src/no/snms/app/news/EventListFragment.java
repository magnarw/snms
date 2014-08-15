package no.snms.app.news;

import java.util.List;

import no.snms.app.BaseActivity;
import no.snms.app.HolidayListFragment.HolidayListAdapter;
import no.snms.app.domain.HolydayItem;
import no.snms.app.domain.NewsItem;
import no.snms.app.images.ImageCacheManager;
import no.snms.app.network.GsonRequest;
import no.snms.app.news.NewsListFragment.NewsListAdapter;

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

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.Rect;
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

public class EventListFragment extends ListFragment {

	private NewsListAdapter adapter;
	boolean isLoading = false;
	ProgressBar progressBar;
	TextView errorMessage;
	TextView newslistheader;
	private static Integer PAGE_SIZE_FOR_EVENTS = 5; 
	Integer lastLoadedPage =-1;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);

		View root = inflater.inflate(R.layout.listnews, container, false);
		newslistheader= (TextView) root.findViewById(R.id.newslistheader);
		progressBar = (ProgressBar) root.findViewById(R.id.progress);
		return root;

	}

	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		NewsItem clickedDetail = (NewsItem) l.getItemAtPosition(position);
		/*
		 * startActivity( new Intent( android.content.Intent.ACTION_VIEW,
		 * Uri.parse("geo:51.49234,7.43045")));
		 */
		  Bundle args = new Bundle();
		   args.putSerializable("newsItem", clickedDetail);
		NewsDetailsFragment myDetailFragment = new NewsDetailsFragment(
				clickedDetail);
		myDetailFragment.setArguments(args);
		switchFragment(myDetailFragment, null);

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
		newslistheader.setText("EVENTS");
		if(getListView().getAdapter() == null) {
			// Get the first page
			isLoading = true;
			lastLoadedPage = 0; 
			
			NewsManager.getInstance().getNews(createSuccessListener(), createErrorListener(),PAGE_SIZE_FOR_EVENTS,0,3,false);
			progressBar.setVisibility(View.VISIBLE);
		}else {
			progressBar.setVisibility(View.GONE);
		}
	
	}
	

	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		this.getListView().setOnScrollListener(new NewsScrollListner());
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
				// TODO : Log error and get prey times from local storage
				// error.getStackTrace();
				Log.e("error", error.toString());
			}
		};
	}

	public class NewsListAdapter extends ArrayAdapter<NewsItem> {

		public NewsListAdapter(Context context) {
			super(context, 0);

		}
		private boolean shouldLoadMoreData(int count, int position){
			// If showing the last set of data, request for the next set of data
			boolean scrollRangeReached = (position > (count - PAGE_SIZE_FOR_EVENTS));
			NewsItem item = getItem(position);
			boolean value =  (scrollRangeReached && !isLoading && item.getHasMoreElements() && item.getNextPage()>lastLoadedPage);
			return value;
		}

		private void loadMoreData(int nextPage){
			progressBar.setVisibility(View.GONE);
			isLoading = true;
			lastLoadedPage = nextPage;
			Log.v(getClass().toString(), "Load more tweets");
			NewsManager.getInstance().getNews(createSuccessListener(), createErrorListener(),PAGE_SIZE_FOR_EVENTS,nextPage,3,false);
		}


		public View getView(int position, View convertView, ViewGroup parent) {
			if (convertView == null) {
				convertView = LayoutInflater.from(getContext()).inflate(
						R.layout.event_row, null);
			}
			if(shouldLoadMoreData(this.getCount(), position) ) {
				loadMoreData(getItem(position).getNextPage());
			}
			TextView text = (TextView) convertView.findViewById(R.id.row_news_created);
			
			TextView title = (TextView) convertView
					.findViewById(R.id.row_news_title);
			// TextView text = (TextView)
			// convertView.findViewById(R.id.row_news_ingress);
			NetworkImageView image = (NetworkImageView) convertView
					.findViewById(R.id.newsImage);
			NewsItem h = getItem(position);
			title.setText(getItem(position).getTitle());
			Uri uri = Uri.parse(h.getImgUrl()+"?w=" + image.getWidth() +"&h="+ image.getHeight()); 
			// text.setText(h.getText());
			image.setImageUrl(getVersion("extrasmall", h.getImgUrl()), ImageCacheManager.getInstance().getImageLoader());
			
			DateTimeFormatter formatter = DateTimeFormat.forPattern("EEEE dd.MMM 'kl' HH:mm");
			//Onsdag 30.april, kl 20:30
			String created = formatter.print(h.getFrom());
		//	text.
			text.setText(created);
			text.setTextColor(Color.BLACK);
			if(h.getTo()!=null && h.getTo().isBeforeNow()){
				image.setColorFilter(Color.argb(450, 48, 48, 48),   Mode.SRC_ATOP);
				title.setTextColor(Color.rgb(108, 108, 108));
				title.setText("Event har pasert\n"+ h.getTitle());
			}
			
			return convertView;
		}

	}

	public class NewsScrollListner implements OnScrollListener {

		int currentFirstVisibleItem;
		int currentVisibleItemCount;
		int currentScrollState;

		@Override
		public void onScroll(AbsListView view, int firstVisibleItem,
				int visibleItemCount, int totalItemCount) {
			this.currentFirstVisibleItem = firstVisibleItem;
			this.currentVisibleItemCount = visibleItemCount;
		}

		@Override
		public void onScrollStateChanged(AbsListView view, int scrollState) {
			this.currentScrollState = scrollState;
			this.isScrollCompleted();
		}

		void loadMoreData() {
			// putPreyItemsOnRequestQueue();
		}

		private void isScrollCompleted() {
			if (this.currentVisibleItemCount > 0
					&& this.currentScrollState == SCROLL_STATE_IDLE) {
				/***
				 * In this way I detect if there's been a scroll which has
				 * completed
				 ***/
				/*** do the work for load more date! ***/
				if (!isLoading) {
					isLoading = true;
					loadMoreData();
				}
			}
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
