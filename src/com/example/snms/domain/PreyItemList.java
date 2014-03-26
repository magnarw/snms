package com.example.snms.domain;

import java.util.ArrayList;
import java.util.List;

public class PreyItemList {
	
	List <PreyItem> preylist; 
	Integer day; 
	
	public PreyItemList(){
		preylist = new ArrayList<PreyItem>();
	}
	
	public PreyItemList(List <PreyItem> preylist,int day) {
		this.preylist = preylist; 
		this.day = day;
	}

	public List<PreyItem> getPreylist() {
		return preylist;
	}

	public void setPreylist(List<PreyItem> preylist) {
		this.preylist = preylist;
	}

	public Integer getDay() {
		return this.day;
	}

	public void setDay(Integer day) {
		this.day = day;
	}
	
	
	
}
