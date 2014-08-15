package no.snms.app.domain;

import java.util.Date;

import org.joda.time.DateTime;

public class HolydayItem {

	public String name;
	
	DateTime from;
	
	DateTime to;

	public String getName() {
		return name;
	}
	
	

	public DateTime getFrom() {
		return from;
	}



	public void setFrom(DateTime from) {
		this.from = from;
	}



	public DateTime getTo() {
		return to;
	}



	public void setTo(DateTime to) {
		this.to = to;
	}



	public void setName(String name) {
		this.name = name;
	} 
}
