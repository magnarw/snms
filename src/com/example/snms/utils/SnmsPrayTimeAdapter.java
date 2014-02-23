package com.example.snms.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import org.joda.time.DateMidnight;
import org.joda.time.DateTime;
import org.joda.time.DateTimeField;
import org.joda.time.LocalDate;
import org.joda.time.LocalTime;
import org.joda.time.MonthDay;
import org.joda.time.chrono.GregorianChronology;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.content.Context;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.database.CursorIndexOutOfBoundsException;
import android.util.Xml;
import android.widget.ImageView;

//import com.example.snms.DBAdapter;
import com.example.snms.PreyOverView;
import com.example.snms.alarm.AlarmUtilities;
import com.example.snms.database.SnmsDAO;
import com.example.snms.domain.Jumma;
import com.example.snms.domain.PreyItem;
import com.example.snms.domain.PreyItemList;
import com.example.snms.settings.PreySettings;

public class SnmsPrayTimeAdapter {
	
	
	AssetManager assetManager;
	private static final String ns = null;
	SnmsDAO dao; 


	public SnmsPrayTimeAdapter(AssetManager assetManager, SnmsDAO dao) {
		this.assetManager = assetManager;
		this.dao = dao; 
	}
	
	
	public List<PreyItem> getPrayListForDate(DateTime time) {
		
		PreySettings settings = dao.getAllSettings();
		
		if(!settings.getHasAvansertPreyCalenderSet()){
			if(settings.getHasShafiPreyCalenderSet())
				return readPrayItemFormXml(time,"shafi");
			else 
				return readPrayItemFormXml(time,"hanafi");
		}else {
			PrayTime prayers = new PrayTime();
			prayers.setTimeFormat(prayers.Time24);
			
			int timezone = Math.abs(TimeZone.getDefault().getRawOffset()) / 3600000;
			Calendar cal = Calendar.getInstance();
			cal.set(time.getYear(),time.getMonthOfYear(), time.getDayOfMonth());
			prayers.setCalcMethod(settings.getCalculationMethodNo());
			prayers.setAsrJuristic(settings.getJuristicMethodsNo());
			prayers.setLat(settings.getLat());
			prayers.setLng(settings.getLng());
			//prayers.setAsrJuristic(settings.getAdjustingMethodNo());
			ArrayList<String> prayerTimes = prayers.getPrayerTimes(cal, settings.getLat(),
					settings.getLng(), timezone);
			ArrayList<String> prayerNames = prayers.getTimeNames();
			List<PreyItem> listToReturn = new ArrayList<PreyItem>();
			for (int i = 0; i < prayerTimes.size(); i++) {
				if(!prayerNames.get(i).equals("Sunset")) {
				DateTime timeToAdd = time.plusHours(
						Integer.valueOf(prayerTimes.get(i).split(":")[0]))
						.plusMinutes(
								Integer.valueOf(prayerTimes.get(i).split(":")[1]));
				PreyItem preyItem = new PreyItem(prayerNames.get(i), timeToAdd,
						false);
				listToReturn.add(preyItem);
//				checkAlarmStateAtStartup(preyItem);
				}
			}
			List temp = listToReturn;
			return listToReturn;
		}

	}

	
	
	public List<PreyItemList> getPrayGridForMonthIndYear(int month, int year, boolean includeAlarm) {
		DateTime dateTime = new DateTime(year, month, 1, 1, 0, 0, 000);
		List<PreyItemList> dayPreyListMap = new ArrayList<PreyItemList>();
		for (int i = 1; i <= dateTime.dayOfMonth().getMaximumValue(); i++) {
			DateTime dateTime2 = new DateTime(year, month, i, 1, 0, 0, 000);
			List<PreyItem> items = this.getPrayListForDate(dateTime2);
			PreyItemList list = new PreyItemList(items, i);
			dayPreyListMap.add(list);
		}
		
		return dayPreyListMap;
	}

	private List<PreyItem> readFeed(XmlPullParser parser, DateTime time)
			throws XmlPullParserException, IOException {
		List<PreyItem> entries = new ArrayList<PreyItem>();
		parser.require(XmlPullParser.START_TAG, ns, "Records");
		while (parser.next() != XmlPullParser.END_TAG) {
			if (parser.getEventType() != XmlPullParser.START_TAG) {
				continue;
			}
			String name = parser.getName();
			// Starts by looking for the entry tag
			if (name.equals("Row")
					&& parser.getAttributeValue(0).equals(String.valueOf(time.getDayOfMonth()))) {
				entries.addAll(readEntry(parser,time));
			} else {
				skip(parser);
			}
		}
		return entries;
	}

	private void skip(XmlPullParser parser) throws XmlPullParserException,
			IOException {
		if (parser.getEventType() != XmlPullParser.START_TAG) {
			throw new IllegalStateException();
		}
		int depth = 1;
		while (depth != 0) {
			switch (parser.next()) {
			case XmlPullParser.END_TAG:
				depth--;
				break;
			case XmlPullParser.START_TAG:
				depth++;
				break;
			}
		}
	}
	
		
		/*
	    public static void main(String[] args) {
	        double latitude = 59;
	        double longitude = 10;
	        double timezone = 1;
	        // Test Prayer times here
	        PrayTime prayers = new PrayTime();

	        prayers.setTimeFormat(prayers.Time24);
	        prayers.setCalcMethod(prayers.Jafari);
	        prayers.setAsrJuristic(prayers.Shafii);
	        prayers.setAdjustHighLats(prayers.AngleBased);
	        int[] offsets = {0, 0, 0, 0, 0, 0, 0}; // {Fajr,Sunrise,Dhuhr,Asr,Sunset,Maghrib,Isha}
	        prayers.tune(offsets);

	        Date now = new Date();
	        Calendar cal = Calendar.getInstance();
	        cal.setTime(now);

	        ArrayList<String> prayerTimes = prayers.getPrayerTimes(cal,
	                latitude, longitude, timezone);
	        ArrayList<String> prayerNames = prayers.getTimeNames();

	        for (int i = 0; i < prayerTimes.size(); i++) {
	            System.out.println(prayerNames.get(i) + " - " + prayerTimes.get(i));
	        }

	    }
		*/
		

	private DateTime getPrayTimeFromString(DateTime time, String timeToParse) {
		DateTimeFormatter fmt = DateTimeFormat.forPattern("h:mm:ss aa");
		LocalTime timeFromString = LocalTime.parse(timeToParse,fmt);
		return time.plusHours(timeFromString.getHourOfDay()).plusMinutes(timeFromString.getMinuteOfHour());

	}
	
	
	
	
	private List<PreyItem> readEntry(XmlPullParser parser,DateTime time) throws XmlPullParserException, IOException {
	    parser.require(XmlPullParser.START_TAG, ns, "Row");
	    List <PreyItem> preyList = new ArrayList<PreyItem>();
	  	DateTime fajrTime = getPrayTimeFromString(time,parser.getAttributeValue(1));
	  	PreyItem fajr = new PreyItem("Fajr", fajrTime, false);	 
	 	DateTime soloppgangTime = getPrayTimeFromString(time,parser.getAttributeValue(2));
	  	PreyItem soloppgang = new PreyItem("Soloppgang", soloppgangTime, false);		  	 
	  	DateTime dhuhrTime = getPrayTimeFromString(time,parser.getAttributeValue(3));
	  	PreyItem duhr = new PreyItem("Dhuhr", dhuhrTime, false);	
		DateTime asrTime = getPrayTimeFromString(time,parser.getAttributeValue(4));
		PreyItem asr = new PreyItem("Asr", asrTime, false);	
	    DateTime maghribTime = getPrayTimeFromString(time,parser.getAttributeValue(5));  	
		PreyItem maghrib = new PreyItem("Maghrib", maghribTime, false);	
		DateTime ishaTime = getPrayTimeFromString(time,parser.getAttributeValue(6));
		PreyItem isha = new PreyItem("Isha", ishaTime, false);	
		preyList.add(fajr);
		preyList.add(soloppgang);
		preyList.add(duhr);
		preyList.add(asr);
		preyList.add(maghrib);
		preyList.add(isha);
	
	    return preyList;
	}

	
	
	
	private List<PreyItem> readPrayItemFormXml(DateTime time, String lovSkole) {
		InputStream inputStream = null;
		try {
			String test = String.valueOf(time.getMonthOfYear());
			System.out.println("Dette er lovskole" + lovSkole);
			inputStream = assetManager.open(lovSkole + "/"+String.valueOf(time.getMonthOfYear()) + ".xml");
			//inputStream = assetManager.open("1.xml");
			XmlPullParser parser = Xml.newPullParser();
			parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
			parser.setInput(inputStream, null);
			parser.nextTag();
			return readFeed(parser,time);}
		catch(Exception e) {
			e.printStackTrace();
		}
		 finally {
			try {
				inputStream.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return null;
	}


	

}
