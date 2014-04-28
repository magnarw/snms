package com.example.snms.settings;

import java.util.HashMap;

public class PreySettings {

	private boolean hasAvansertPreyCalenderSet;
	private Integer calculationMethodNo;
	private Integer juristicMethodsNo;
	private Integer adjustingMethodNo;
	
	private boolean hasBeta; 

	public boolean isHasBeta() {
		return hasBeta;
	}

	public void setHasBeta(boolean hasBeta) {
		this.hasBeta = hasBeta;
	}

	private Float lng;
	private Float lat;

	private boolean hasHanafiPreyCalenderSet;
	private boolean hasShafiPreyCalenderSet;
	private boolean hasCityCalednerSet; 
	private String city; 

	public static PreySettings createFromSettingsMap(
			HashMap<String, String> settings) {
		PreySettings preySettings = new PreySettings();
		
		if(settings.get("beta")!=null){
			preySettings.setHasBeta(true);
		}
		
		if (settings.get("hanfi") != null) {
			preySettings.setHasAvansertPreyCalenderSet(false);
			preySettings.setHasHanafiPreyCalenderSet(true);
			preySettings.setHasShafiPreyCalenderSet(false);
			preySettings.setHasCityCalednerSet(false);
			return preySettings;
		}
		else if(settings.get("citysetting")!=null){
			preySettings.setHasAvansertPreyCalenderSet(false);
			preySettings.setHasHanafiPreyCalenderSet(false);
			preySettings.setHasShafiPreyCalenderSet(false);
			preySettings.setHasCityCalednerSet(true);
			preySettings.setCity(settings.get("city"));
			return preySettings;
		}
		
		else if (settings.get("icc") != null) {
			preySettings.setHasAvansertPreyCalenderSet(false);
			preySettings.setHasHanafiPreyCalenderSet(false);
			preySettings.setHasShafiPreyCalenderSet(true);
			preySettings.setHasCityCalednerSet(false);
			return preySettings;
		} else if (settings.get("avansert") != null) {
			preySettings.setHasAvansertPreyCalenderSet(true);
			preySettings.setHasHanafiPreyCalenderSet(false);
			preySettings.setHasShafiPreyCalenderSet(true);

			if (settings.get("jurmethod").equals("Shafii")) {
				preySettings.setJuristicMethodsNo(0);
			} else {
				preySettings.setJuristicMethodsNo(1);
			}

			if (settings.get("adjustmethod").equals("Midnatt")) {
				preySettings.setAdjustingMethodNo(1);
			} else if (settings.get("adjustmethod").equals("1/7 av natten")) {
				preySettings.setJuristicMethodsNo(2);
			} else if (settings.get("adjustmethod").equals("Grader")) {
				preySettings.setJuristicMethodsNo(3);
			} else {
				preySettings.setJuristicMethodsNo(0);
			}

			if (settings.get("calcmethod").equals("Jafari")) {
				preySettings.setCalculationMethodNo(0);
			} else if (settings.get("calcmethod").equals("Karachi")) {
				preySettings.setCalculationMethodNo(1);
			} else if (settings.get("calcmethod").equals(
					"Islamic Society of North America (ISNA)")) {
				preySettings.setCalculationMethodNo(2);
				;
			} else if (settings.get("calcmethod").equals(
					"Muslim World League (MWL)")) {
				preySettings.setCalculationMethodNo(3);
				;
			} else if (settings.get("calcmethod").equals("Umm al-Qura, Makkah")) {
				preySettings.setCalculationMethodNo(4);
			} else if (settings.get("calcmethod").equals(
					"Egyptian General Authority of Survey")) {
				preySettings.setCalculationMethodNo(5);
				;
			} else if (settings.get("calcmethod").equals(
					"Institute of Geophysics, University of Tehran")) {
				preySettings.setCalculationMethodNo(6);
			} else {
				preySettings.setCalculationMethodNo(0);
			}

			if (settings.get("lat") != null) {
				preySettings.setLat(Float.valueOf(settings.get("lat")));
			}

			else {
				preySettings.setLat(59f);
			}
			if (settings.get("lng") != null) {
				preySettings.setLng(Float.valueOf(settings.get("lng")));
			} else {
				preySettings.setLng(10f);
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

	
	
	public boolean getHasCityCalednerSet() {
		return hasCityCalednerSet;
	}

	public void setHasCityCalednerSet(Boolean hasCityCalednerSet) {
		this.hasCityCalednerSet = hasCityCalednerSet;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
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
