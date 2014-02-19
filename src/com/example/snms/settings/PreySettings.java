package com.example.snms.settings;

import java.util.HashMap;

public class PreySettings {

	
	
	private Boolean hasAvansertPreyCalenderSet; 
	private Integer calculationMethodNo; 
	private Integer juristicMethodsNo;
	private Integer adjustingMethodNo; 
	
	private Float lng;
	private Float lat;
	
	private Boolean hasHanafiPreyCalenderSet; 
	private Boolean hasShafiPreyCalenderSet;
	
	public static PreySettings createFromSettingsMap(HashMap <String,String> settings) {
		PreySettings preySettings = new PreySettings();
		
		if(settings.get("hanfi")!=null) {
			preySettings.setHasAvansertPreyCalenderSet(false);
			preySettings.setHasHanafiPreyCalenderSet(true);
			preySettings.setHasShafiPreyCalenderSet(false);
			return preySettings;
		}
		else if(settings.get("icc")!=null) {
			preySettings.setHasAvansertPreyCalenderSet(false);
			preySettings.setHasHanafiPreyCalenderSet(false);
			preySettings.setHasShafiPreyCalenderSet(true);
			return preySettings;
		}
		else if(settings.get("avansert")!=null) {
			preySettings.setHasAvansertPreyCalenderSet(true);
			preySettings.setHasHanafiPreyCalenderSet(false);
			preySettings.setHasShafiPreyCalenderSet(true);
			
			if(settings.get("jurmethod").equals("Shafii")){
				preySettings.setJuristicMethodsNo(0);
			}else {
				preySettings.setJuristicMethodsNo(1);
			}
			
			if(settings.get("adjustmethod").equals("Midnatt")){
				preySettings.setAdjustingMethodNo(1);
			}else if(settings.get("adjustmethod").equals("1/7 av natten")) {
				preySettings.setJuristicMethodsNo(2);
			}
			else if(settings.get("adjustmethod").equals("Grader")) {
				preySettings.setJuristicMethodsNo(3);
			}else {
				preySettings.setJuristicMethodsNo(0);
			}
			
			if(settings.get("calcmethod").equals("Jafari")){
				preySettings.setCalculationMethodNo(0);
			}else if(settings.get("calcmethod").equals("Karachi")) {
				preySettings.setCalculationMethodNo(1);
			}
			else if(settings.get("calcmethod").equals("Islamic Society of North America (ISNA)")) {
				preySettings.setCalculationMethodNo(2);;
			}
			else if(settings.get("calcmethod").equals("Muslim World League (MWL)")) {
				preySettings.setCalculationMethodNo(3);;
			}
			else if(settings.get("calcmethod").equals("Umm al-Qura, Makkah")) {
				preySettings.setCalculationMethodNo(4);
			}
			else if(settings.get("calcmethod").equals("Egyptian General Authority of Survey")) {
				preySettings.setCalculationMethodNo(5);;
			}else if(settings.get("calcmethod").equals("Institute of Geophysics, University of Tehran")) {
				preySettings.setCalculationMethodNo(6);;
			}
			


		
			
			return preySettings;
		}
		preySettings.setHasAvansertPreyCalenderSet(false);
		preySettings.setHasHanafiPreyCalenderSet(true);
		preySettings.setHasShafiPreyCalenderSet(false);
		return preySettings;
		
	}
	
	public Boolean getHasAvansertPreyCalenderSet() {
		return hasAvansertPreyCalenderSet;
	}
	public void setHasAvansertPreyCalenderSet(Boolean hasAvansertPreyCalenderSet) {
		this.hasAvansertPreyCalenderSet = hasAvansertPreyCalenderSet;
	}
	public Integer getCalculationMethodNo() {
		return calculationMethodNo;
	}
	public void setCalculationMethodNo(Integer calculationMethodNo) {
		this.calculationMethodNo = calculationMethodNo;
	}
	public Integer getJuristicMethodsNo() {
		return juristicMethodsNo;
	}
	public void setJuristicMethodsNo(Integer juristicMethodsNo) {
		this.juristicMethodsNo = juristicMethodsNo;
	}
	public Integer getAdjustingMethodNo() {
		return adjustingMethodNo;
	}
	public void setAdjustingMethodNo(Integer adjustingMethodNo) {
		this.adjustingMethodNo = adjustingMethodNo;
	}
	public Float getLng() {
		return lng;
	}
	public void setLng(Float lng) {
		this.lng = lng;
	}
	public Float getLat() {
		return lat;
	}
	public void setLat(Float lat) {
		this.lat = lat;
	}
	public Boolean getHasHanafiPreyCalenderSet() {
		return hasHanafiPreyCalenderSet;
	}
	public void setHasHanafiPreyCalenderSet(Boolean hasHanafiPreyCalenderSet) {
		this.hasHanafiPreyCalenderSet = hasHanafiPreyCalenderSet;
	}
	public Boolean getHasShafiPreyCalenderSet() {
		return hasShafiPreyCalenderSet;
	}
	public void setHasShafiPreyCalenderSet(Boolean hasShafiPreyCalenderSet) {
		this.hasShafiPreyCalenderSet = hasShafiPreyCalenderSet;
	} 
	
	
	
	
	
}
