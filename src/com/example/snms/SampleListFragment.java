package com.example.snms;





import com.example.snms.about.AboutSnmsFragment;
import com.example.snms.donation.DonationFragment;
import com.example.snms.news.BuildProjectListFragment;
import com.example.snms.news.EventListFragment;
import com.example.snms.news.NewsListFragment;
import com.example.snms.preylist.PreyOverviewFragment;
import com.example.snms.qibla.QiblaFragment;
import com.example.snms.settings.SettingsFragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class SampleListFragment extends ListFragment {

	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.list, null);
	}

	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		SampleAdapter adapter = new SampleAdapter(getActivity());
	    adapter.add(new SampleItem("Nyheter", R.drawable.ic_news));
	    adapter.add(new SampleItem("Byggeprosjektet", R.drawable.ic_buildproject));
	    adapter.add(new SampleItem("Events", R.drawable.ic_events));
	    adapter.add(new SampleItem("Om snms", R.drawable.ic_logo));
	    adapter.add(new SampleItem("Qibla", R.drawable.ic_qibla));
	    adapter.add(new SampleItem("Donasjon", R.drawable.ic_donation));
	    adapter.add(new SampleItem("Instillinger", R.drawable.ic_settings));
	
		setListAdapter(adapter);
	}

	private class SampleItem {
		public String tag;
		public int iconRes;
		public SampleItem(String tag, int iconRes) {
			this.tag = tag; 
			this.iconRes = iconRes;
		}
	}
	
	@Override
	public void onListItemClick(ListView lv, View v, int position, long id) {
		Fragment newContent1 = null;
		Fragment newContent2 = null;
		switch (position) {
		

		case 0:
			newContent1 = new NewsListFragment();
			break;
		case 1:
			newContent1 = new BuildProjectListFragment();
			break;	
		case 2:
			newContent1 = new EventListFragment();
			break;	
		case 3:
			newContent1 = new AboutSnmsFragment();
			break;		
		case 4:
			newContent1 = new QiblaFragment();
			break;		
		case 5:
			newContent1 = new DonationFragment();
			break;	
		case 6:
			newContent1 = new SettingsFragment();
			break;	
		}
		
		if (newContent1 != null)
			switchFragment(newContent1,newContent2);
	}
	

	public class SampleAdapter extends ArrayAdapter<SampleItem> {

		public SampleAdapter(Context context) {
			super(context, 0);
		}

		public View getView(int position, View convertView, ViewGroup parent) {
			if (convertView == null) {
				convertView = LayoutInflater.from(getContext()).inflate(R.layout.row, null);
			}
			ImageView icon = (ImageView) convertView.findViewById(R.id.row_icon);
			icon.setImageResource(getItem(position).iconRes);
			TextView title = (TextView) convertView.findViewById(R.id.row_title);
			title.setText(getItem(position).tag);

			return convertView;
		}

	}
	
	private void switchFragment(Fragment fragment1, Fragment fragment2) {
		if (getActivity() == null)
			return;
		
		if (getActivity() instanceof BaseActivity) {
			BaseActivity fca = (BaseActivity) getActivity();
			fca.switchContent(fragment1, fragment2);
		} 
	}
	
}
