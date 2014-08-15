package no.snms.app.domain;

import org.joda.time.DateTime;

public class Jumma {
	
	int fromMonth;
	int toMonth;
	int fromDay;
	int toDay;
	DateTime updated;
	int hours; 
	int minuttes; 
	
	public Jumma( int fromDay,int fromMonth,int toDay,int toMonth,int hours, int minuttes) {
		this.fromMonth = fromMonth; 
		this.toMonth = toMonth; 
		this.fromDay = fromDay; 
		this.toDay = toDay; 
		this.hours = hours;
		this.minuttes = minuttes;
	}
	
	
	public boolean isBetween(DateTime time) {
		
		int offset = fromMonth>toMonth?12:0;
		DateTime c = time.withYear(2000); 
		DateTime jummaFrom =new DateTime(2000, fromMonth, fromDay, 0,0); 
		DateTime jummaTo = new DateTime(2000+offset, toMonth, toDay, 23,59); 
		
		String timeComp = c.toString();
	String timefrom = jummaFrom.toString();
	String timeTo = jummaTo.toString();

		if(c.isAfter(jummaFrom) && c.isBefore(jummaTo)) {
			return true;
		}
		return false; 
	}


	public int getFromMonth() {
		return fromMonth;
	}


	public void setFromMonth(int fromMonth) {
		this.fromMonth = fromMonth;
	}


	public int getToMonth() {
		return toMonth;
	}


	public void setToMonth(int toMonth) {
		this.toMonth = toMonth;
	}


	public int getFromDay() {
		return fromDay;
	}


	public void setFromDay(int fromDay) {
		this.fromDay = fromDay;
	}


	public int getToDay() {
		return toDay;
	}


	public void setToDay(int toDay) {
		this.toDay = toDay;
	}


	public int getHours() {
		return hours;
	}


	public void setHours(int hours) {
		this.hours = hours;
	}


	public int getMinuttes() {
		return minuttes;
	}


	public void setMinuttes(int minuttes) {
		this.minuttes = minuttes;
	}


	public DateTime getUpdated() {
		// TODO Auto-generated method stub
		return updated;
	}
	
	

}
