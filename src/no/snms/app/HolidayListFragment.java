package no.snms.app;

import java.util.ArrayList;
import java.util.List;

import no.snms.app.domain.HolydayItem;
import no.snms.app.domain.PreyItem;
import no.snms.app.domain.PreyItemList;
import no.snms.app.network.GsonRequest;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.HeaderViewListAdapter;
import android.widget.TextView;

import com.android.volley.*;
import com.android.volley.Request.Method;
import com.android.volley.toolbox.Volley;
import no.snms.app.R;

public class HolidayListFragment extends ListFragment {
	
private RequestQueue requestQueue;
	
	private HolidayListAdapter adapter;
	private View mheaderView;
	
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		this.requestQueue = Volley.newRequestQueue(this.getActivity());
		mheaderView = inflater.inflate(R.layout.holyday_header, null);
		return inflater.inflate(R.layout.list, null);
	
	}
	
	
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		adapter = new HolidayListAdapter(getActivity());
		putPreyItemsOnRequestQueue();
		getListView().addHeaderView(mheaderView);
		setListAdapter(adapter);
	}
	
	private void putPreyItemsOnRequestQueue() {
	    GsonRequest<HolydayItem[]> jsObjRequest = new GsonRequest<HolydayItem[]> (
	        Method.GET,
	        "http://46.137.184.176:3000/api/holidays",
	        HolydayItem[].class,
	        this.createSuccessListener(),
	        this.createErrorListener());
	    this.requestQueue.add(jsObjRequest);
	}
			
	private Response.Listener <HolydayItem[]> createSuccessListener() {
	    return new Response.Listener <HolydayItem[]>() {
	       
	    	@Override
			public void onResponse(HolydayItem[] response) {
				adapter.addAll(response);
				
			}
	    };	
	}
	
	private Response.ErrorListener createErrorListener() {
	    return new Response.ErrorListener() {
	        @Override
	        public void onErrorResponse(VolleyError error) {
	        	//TODO : Log error and get prey times from local storage
	            error.getStackTrace();
	        	Log.e("error", error.getMessage());
	        }
	    };
	}
	
	
	public class HolidayListAdapter extends ArrayAdapter<HolydayItem> {

		public HolidayListAdapter(Context context) {
			super(context, 0);
		}

		public View getView(int position, View convertView, ViewGroup parent) {
			if (convertView == null) {
				convertView = LayoutInflater.from(getContext()).inflate(R.layout.holyday_row, null);
			}
			TextView title = (TextView) convertView.findViewById(R.id.row_holyday_title);
			
			HolydayItem h =  getItem(position);
			
			title.setText(getItem(position).getName() +" - Fra" +h.getFrom().toString() + " til "  +h.getTo().toString());
			return convertView;
		}
		

	}
	

}
