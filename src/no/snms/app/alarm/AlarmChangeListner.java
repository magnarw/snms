package no.snms.app.alarm;

import no.snms.app.domain.PreyItem;
import android.content.Context;
import android.content.Intent;

public interface AlarmChangeListner {
	
	

	public void alarmChanged(String name, Boolean value); 
	
}
