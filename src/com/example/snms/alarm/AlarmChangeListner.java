package com.example.snms.alarm;

import android.content.Context;
import android.content.Intent;

import com.example.snms.domain.PreyItem;

public interface AlarmChangeListner {
	
	

	public void alarmChanged(String name, Boolean value); 
	
}
