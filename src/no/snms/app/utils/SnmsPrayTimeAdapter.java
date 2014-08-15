package no.snms.app.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import no.snms.app.PreyOverView;
import no.snms.app.alarm.AlarmUtilities;
import no.snms.app.database.SnmsDAO;
import no.snms.app.domain.Jumma;
import no.snms.app.domain.PreyItem;
import no.snms.app.domain.PreyItemList;
import no.snms.app.settings.PreySettings;

import org.joda.time.DateMidnight;
import org.joda.time.DateTime;
import org.joda.time.DateTimeConstants;
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

public class SnmsPrayTimeAdapter {
	
	
	AssetManager assetManager;
	private static final String ns = null;
	SnmsDAO dao; 


	public SnmsPrayTimeAdapter(AssetManager assetManager, SnmsDAO dao) {
		this.assetManager = assetManager;
		this.dao = dao; 
	}
	
	/*
	- I det opprinnelig Excel arket med bønnetider, settes det 1 time tilbake fra og med siste søndagen i mars til og med siste lørdagen i oktober.

	- Kalenderen vil nå være i normaltid, som igjen er vintertid.

	- Det må lages en algoritme/funksjon, som setter sommertid. Dvs. beregner frem siste søndagen i mars og setter alle bønnetidene 1 time frem. 
	Dette skal vare til og med siste lørdagen i oktober. Fra og med siste søndag i oktober skal vi begynne å bruke normaltid (vintertid)
	
	*/
	
	private int getLastSundayInMarch(DateTime time) {
		int lastSundayInMarch = -1;
		DateTime march = new DateTime(time.getYear(),3, 1,0,0);
		while(march.getMonthOfYear()==3){
			if(march.getDayOfWeek()==DateTimeConstants.SUNDAY){
				lastSundayInMarch = march.getDayOfYear();
			}
			//Keep adding days to march until we reach april
			march = march.plusDays(1);
		}
		return lastSundayInMarch;
		
	}
	
	private int getLastSundayInOctober(DateTime time) {
		int lastSaturdayInOctober = -1;
		DateTime october = new DateTime(time.getYear(),10, 1,0,0);
		while(october.getMonthOfYear()==10){
			if(october.getDayOfWeek()==DateTimeConstants.SUNDAY){
				lastSaturdayInOctober = october.getDayOfYear();
			}
			//Keep adding days to march until we reach 
			october = october.plusDays(1);
		}
		return lastSaturdayInOctober;
	}
	
	private void cleanUpOrignalPreyCalender(DateTime currentDate, List <PreyItem> preyTimes) {
		//Summer time in 2013 startet at 2013-03-31 and lasted to 2013-10-27
		int month = currentDate.getMonthOfYear();
		int day = currentDate.getDayOfMonth();
		
		DateTime startOfSummerTime2013 = new DateTime(2013, 3, 31, 5,0);
		DateTime endOfSummerTime2013 = new DateTime(2013, 10, 27, 5,0);
		System.out.println("This is start of summer time 2013:" + startOfSummerTime2013.getDayOfYear());
		System.out.println("This is end of summer time 2013:" + endOfSummerTime2013.getDayOfYear());
		
		int yearOffset = 2013 -currentDate.getYear();
		
		DateTime transformedCurrentDate = currentDate.minusYears(yearOffset);
		
		System.out.println("day of year:" + currentDate.getDayOfYear());
		
		if(currentDate.getDayOfYear()>=90 && currentDate.getDayOfYear()<=300){
			for(PreyItem prey : preyTimes){
				prey.setTime(prey.getTime().minusHours(1));
			}
		}
	}
	
	
	private List<PreyItem> adjustForDaylightSavings(DateTime time, List <PreyItem> items){
	//	cleanUpOrignalPreyCalender(time, items);
		
	
		
		System.out.println("This is last sunday in march:" + getLastSundayInMarch(time));
		
		/*
		if(time.getDayOfYear()>=getLastSundayInMarch(time) && time.getDayOfYear()<=getLastSaturdayInOctober(time)){
			for(PreyItem prey : items){
				prey.setTime(prey.getTime().plusHours(1));
			}
		}
		*/
		return items;
	}
	
	
	public List<PreyItem> getPrayListForDate(DateTime time) {
		
		PreySettings settings = dao.getAllSettings();
		
		if(!settings.getHasAvansertPreyCalenderSet()){
			if(settings.getHasShafiPreyCalenderSet())
				return adjustForDaylightSavings(time,readPrayItemFormXml(time,"asr1x"));
			else if(settings.getHasCityCalednerSet()){
				if(settings.isAsr1City())
					return adjustForDaylightSavings(time,getPreyItemBasedOnCity(settings.getCity(),time,false,"asr1x"));
				else 
				 return adjustForDaylightSavings(time,getPreyItemBasedOnCity(settings.getCity(),time,false, "asr2x"));
			}
			else {
				return adjustForDaylightSavings(time,readPrayItemFormXml(time,"asr2x"));
			}
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
		PreySettings settings = dao.getAllSettings();
		if(settings.getHasCityCalednerSet()){
			List<PreyItemList> dayPreyListMap = new ArrayList<PreyItemList>();
			DateTime dateTime2 = new DateTime(year, month,1, 1, 0, 0, 000);
			DateTime midnight = dateTime2.minusHours(dateTime2.getHourOfDay())
					.minusMinutes(dateTime2.getMinuteOfHour())
					.minusSeconds(dateTime2.getSecondOfMinute());
			List<PreyItem> items = null;
			if(settings.isAsr1City() == true){
				items =getPreyItemBasedOnCity(settings.getCity(),midnight,true,"asr1x");
			}
			else {
				items =getPreyItemBasedOnCity(settings.getCity(),midnight,true,"asr2x");
			} 
			
			int counter = 0; 
			PreyItemList list = new PreyItemList();
			int day = 1; 
			list.setDay(day);
			Iterator it = items.iterator();
			while(it.hasNext()){
				if(counter!=6){
					list.getPreylist().add((PreyItem) it.next());
					counter++; 
				}else {
					dayPreyListMap.add(list);
					list = new PreyItemList();
					day++; 
					list.setDay(day);
					counter = 0; 
				}
				
			}
			
			return dayPreyListMap;
			
		}
		
		
		DateTime dateTime = new DateTime(year, month, 1, 1, 0, 0, 000);
		List<PreyItemList> dayPreyListMap = new ArrayList<PreyItemList>();
		for (int i = 1; i <= dateTime.dayOfMonth().getMaximumValue(); i++) {
			DateTime dateTime2 = dateTime.plusDays(i-1);
			DateTime midnight = dateTime2.minusHours(dateTime2.getHourOfDay())
					.minusMinutes(dateTime2.getMinuteOfHour())
					.minusSeconds(dateTime2.getSecondOfMinute());
			List<PreyItem> items = this.getPrayListForDate(midnight);
			PreyItemList list = new PreyItemList(items, i);
			dayPreyListMap.add(list);
		}
		
		return dayPreyListMap;
	}

	private List<PreyItem> readFeed(XmlPullParser parser, DateTime time)
			throws XmlPullParserException, IOException {
		List<PreyItem> entries = new ArrayList<PreyItem>();
		parser.require(XmlPullParser.START_TAG, ns, "Records");
		
		int dayCounter = 1;
		int month = time.getMonthOfYear();
		while (parser.next() != XmlPullParser.END_TAG) {
			if (parser.getEventType() != XmlPullParser.START_TAG) {
				continue;
			}
			String name = parser.getName();
			// Starts by looking for the entry tag
			if (name.equals("Row")
					&& parser.getAttributeValue(0).equals(String.valueOf(time.getDayOfMonth()))) {
				entries.addAll(readEntry(parser,time,dayCounter,month));
				dayCounter++;
			} else {
				skip(parser);
			}
		}
		return entries;
	}
	
	

	
	private List<PreyItem> readCityFeed(XmlPullParser parser, DateTime time,String city, Boolean returnMonth)
			throws XmlPullParserException, IOException {
		List<PreyItem> entries = new ArrayList<PreyItem>();
		parser.require(XmlPullParser.START_TAG, ns, "Records");
		DateTime firstDayOfMonth = time.dayOfMonth().withMinimumValue();
		DateTime lastDayOfMonth = time.dayOfMonth().withMaximumValue();
		int start = returnMonth?firstDayOfMonth.getDayOfYear():time.getDayOfYear();
		int end = returnMonth?lastDayOfMonth.getDayOfYear()+1:-1;
		int counter = 0; 
		boolean startCounting = false; 
		startCounting = true; 
		while (parser.next() != XmlPullParser.END_TAG) {
			if (parser.getEventType() != XmlPullParser.START_TAG) {
				continue;
			}
			String name = parser.getName();
			if(startCounting)
				counter++;
			// Starts by looking for the entry tag
			 if(returnMonth && (counter>=start && counter<=end)){
				entries.addAll(readCityEntry(parser,time));
				skip(parser);
			}else if(!returnMonth && counter==start){
				entries.addAll(readCityEntry(parser,time));
			}else {
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
	
		
	
	private List<PreyItem> getPreyItemBasedOnCity(String city, DateTime date, Boolean calender,String asr){
		InputStream inputStream = null;
		try {
			inputStream = assetManager.open("cities/" +asr + "/" + city +".xml");
			XmlPullParser parser = Xml.newPullParser();
			parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
			parser.setInput(inputStream, null);
			parser.nextTag();
			
			return readCityFeed(parser,date,city,calender);}
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
	
	
	

	private DateTime getPrayTimeFromString(DateTime time, String timeToParse,int day, int month) {
		DateTimeFormatter fmt = DateTimeFormat.forPattern("h:mm:ss aa");
		LocalTime timeFromString = LocalTime.parse(timeToParse,fmt);
		System.out.println("Month, day, year:" + time.getMonthOfYear() + "," + time.getDayOfMonth() + "," + time.getYear());
		
		//clean up orginal calender
		if(time.getMonthOfYear()>=3 && time.getMonthOfYear()<=10){
			if(time.getMonthOfYear()==3 && time.getDayOfMonth()>=30){
				timeFromString = timeFromString.minusHours(1);
			}else if(time.getMonthOfYear()==10 && time.getDayOfMonth()<26){
				timeFromString = timeFromString.minusHours(1);
			}else if (time.getMonthOfYear()>3 && time.getMonthOfYear()<10){
				timeFromString = timeFromString.minusHours(1);
			}
		}
		
		
		DateTime toReturn = time.plusHours(timeFromString.getHourOfDay()).plusMinutes(timeFromString.getMinuteOfHour());
		
		//adjust for summer time
		if(toReturn.getDayOfYear()>getLastSundayInMarch(time) && toReturn.getDayOfYear()<getLastSundayInOctober(time)){
			toReturn = toReturn.plusHours(1);
		}
		if(time.getDayOfYear() == getLastSundayInOctober(time))
			toReturn = toReturn.plusHours(1);
		
		
		
		//stuff to clean up summer time in the orginal calender(from 2013)

		
		return toReturn;
	
	}
	
	
	
	
	private List<PreyItem> readEntry(XmlPullParser parser,DateTime time,int day,int month) throws XmlPullParserException, IOException {
	    parser.require(XmlPullParser.START_TAG, ns, "Row");
	    List <PreyItem> preyList = new ArrayList<PreyItem>();
	  	DateTime fajrTime = getPrayTimeFromString(time,parser.getAttributeValue(1), day,month);
	  	PreyItem fajr = new PreyItem("Fajr", fajrTime, false);	 
	 	DateTime soloppgangTime = getPrayTimeFromString(time,parser.getAttributeValue(2),day,month);
	  	PreyItem soloppgang = new PreyItem("Soloppgang", soloppgangTime, false);		  	 
	  	DateTime dhuhrTime = getPrayTimeFromString(time,parser.getAttributeValue(3),day,month);
	  	PreyItem duhr = new PreyItem("Dhuhr", dhuhrTime, false);	
		DateTime asrTime = getPrayTimeFromString(time,parser.getAttributeValue(4),day,month);
		PreyItem asr = new PreyItem("Asr", asrTime, false);	
	    DateTime maghribTime = getPrayTimeFromString(time,parser.getAttributeValue(5),day,month);  	
		PreyItem maghrib = new PreyItem("Maghrib", maghribTime, false);	
		DateTime ishaTime = getPrayTimeFromString(time,parser.getAttributeValue(6),day,month);
		PreyItem isha = new PreyItem("Isha", ishaTime, false);	
		preyList.add(fajr);
		preyList.add(soloppgang);
		preyList.add(duhr);
		preyList.add(asr);
		preyList.add(maghrib);
		preyList.add(isha);
	
	    return preyList;
	}
	
	private List<PreyItem> readCityEntry(XmlPullParser parser,DateTime time) throws XmlPullParserException, IOException {
	    parser.require(XmlPullParser.START_TAG, ns, "Row");
	    List <PreyItem> preyList = new ArrayList<PreyItem>();
	  	DateTime fajrTime = getPrayTimeFromString(time,parser.getAttributeValue(2),-1,-1);
	  	PreyItem fajr = new PreyItem("Fajr", fajrTime, false);	 
	 	DateTime soloppgangTime = getPrayTimeFromString(time,parser.getAttributeValue(3),-1,-1);
	  	PreyItem soloppgang = new PreyItem("Soloppgang", soloppgangTime, false);		  	 
	  	DateTime dhuhrTime = getPrayTimeFromString(time,parser.getAttributeValue(4),-1,-1);
	  	PreyItem duhr = new PreyItem("Dhuhr", dhuhrTime, false);	
		DateTime asrTime = getPrayTimeFromString(time,parser.getAttributeValue(5),-1,-1);
		PreyItem asr = new PreyItem("Asr", asrTime, false);	
	    DateTime maghribTime = getPrayTimeFromString(time,parser.getAttributeValue(6),-1,-1);  	
		PreyItem maghrib = new PreyItem("Maghrib", maghribTime, false);	
		DateTime ishaTime = getPrayTimeFromString(time,parser.getAttributeValue(7),-1,-1);
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
