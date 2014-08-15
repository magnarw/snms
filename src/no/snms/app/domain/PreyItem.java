package no.snms.app.domain;

import java.sql.Timestamp;
import java.util.Comparator;
import java.util.Date;

import org.joda.time.DateTime;
import org.joda.time.LocalTime;

public class PreyItem implements Comparable<PreyItem> {
	
	private String name; 
	
	private DateTime time; 
	
	private boolean alarm;
	
	public PreyItem(String name, DateTime time, boolean alarm) {
		this.name = name; 
		this.time = time; 
		this.alarm = alarm;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public DateTime getTime() {
		return time;
	}

	public void setTime(DateTime time) {
		this.time = time;
	}
	
	public void setAlarmBoolean(boolean alarm){
		this.alarm = alarm;
	}
	
	public boolean getAlarmBoolean(){
		return alarm;
	}

	@Override
	public int compareTo( PreyItem arg1) {
		return this.getTime().compareTo(arg1.getTime());
	}
	
	public String getTimeOfDayAsString() {
		LocalTime time = new LocalTime(this.getTime().getHourOfDay(),this.getTime().getMinuteOfHour());
		return time.toString("HH:mm");
	}

}
