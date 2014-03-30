package com.example.snms.alarm;


import com.actionbarsherlock.app.SherlockDialogFragment;
import com.example.snms.MainApplication;
import com.example.snms.PreyOverView;
import com.example.snms.R;
import com.example.snms.database.SnmsDAO;
import com.example.snms.domain.PreyItem;
import com.example.snms.preylist.PreyOverviewFragment;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.NumberPicker;
import android.widget.TextView;

@SuppressLint("ValidFragment")
public class AlarmDialogFragment extends SherlockDialogFragment implements OnClickListener  {
	
		PreyItem prey; 
		PreyOverView preyActivity;
		Button okButton; 
		private NumberPicker picker; 
		private String[] nums = new String[300];
		TextView alarmTitle;
		ImageButton button; 
		
		AlarmDialogFragment(PreyItem preyItem, ImageButton button){
			this.prey = preyItem;
			this.button = button; 
		}
	 	
	    @SuppressLint("ValidFragment")
		public static AlarmDialogFragment newInstance(PreyItem preyItem, ImageButton button) {
	    	AlarmDialogFragment f = new AlarmDialogFragment(preyItem,button);
	        return f;
	    }
	 
	 
	   @Override
	    public Dialog onCreateDialog(Bundle savedInstanceState) {
	        //getting proper access to LayoutInflater is the trick
	        LayoutInflater inflater = getActivity().getLayoutInflater();
	        preyActivity = (PreyOverView) getActivity();
	        View view = inflater.inflate(R.layout.alarmdialog, null);
	        okButton = (Button)view.findViewById(R.id.okAlarm);
	        alarmTitle = (TextView)view.findViewById(R.id.prey_current_day);
	        alarmTitle.setText("Sett alarm for " + prey.getName());
	        okButton.setOnClickListener(this);
	        AlertDialog.Builder builder = new AlertDialog.Builder(getSherlockActivity());
	        picker = (NumberPicker)view.findViewById(R.id.donationPicker);
	        for(int i=0; i<nums.length; i++)
	 		   nums[i] = Integer.toString(i);
	        
	    	picker.setMaxValue(nums.length-1);
			picker.setMinValue(0);
			picker.setWrapSelectorWheel(false);
			picker.setDisplayedValues(nums);
			picker.setValue(1);
	        
	        builder.setView(view);
	        
	        
	        return builder.create();
	    }

	@Override
	public void onClick(View v) {
		if(v.equals(okButton)){
			
			
			AlarmUtilities Util = new AlarmUtilities(((PreyOverView) getActivity()).getDAO());
//			alarm.setOffset(10);
//			prey.getTime();
//			alarm.setPrey();
			
//			alarm.setId(Util.getAlarmId(prey.getTime()));7
			Alarm alarm = new Alarm(prey.getName(), picker.getValue(), Util.getAlarmId(prey.getTime()));
//			Intent intent = new Intent(getAppContext(), AlarmDialogFragment.class);
			Util.SetRepeatingAlarm(prey, alarm.getId(), getAppContext(),prey.getName(), alarm.getOffset());
			
			((PreyOverView) getActivity()).setAlarm(prey.getName(),true);
			
			button.setSelected(true);
			
			this.dismiss();
		}
		
	}
	public static Context getAppContext() {
	    return MainApplication.getAppContext();
	}

	

}
