package com.example.snms.jumma;

import java.util.ArrayList;
import java.util.List;

import org.joda.time.DateTime;

import android.content.Context;
import android.net.Uri;
import android.util.Log;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.snms.database.SnmsDAO;
import com.example.snms.domain.Jumma;
import com.example.snms.domain.PreyItem;
import com.example.snms.images.ImageCacheManager;
import com.example.snms.news.NewsItem;
import com.example.snms.news.NewsManager;

public class JummaAdaptor {

	List<JummaListner> listners = new ArrayList<JummaListner>(); 
	SnmsDAO jummaDao; 
	DateTime currentTime;
	List <Jumma> currentJummaList = new ArrayList<Jumma>();
	
	public JummaAdaptor(Context context) {
		jummaDao = new SnmsDAO(context);
	}
	
	public void tryFethingJummaRemote(DateTime timeForJumma) {
		currentTime = timeForJumma;
		JummaManager.getInstance().getJumma(createSuccessListener(), createErrorListener());
	}
	
	public void tryFetchningJummaLocally(DateTime timeForJumma) {
		currentTime = timeForJumma;
		//Go remote and to db if jumma list is empty
		if(currentJummaList.size() == 0) {
		JummaManager.getInstance().getJumma(createSuccessListener(), createErrorListener());}
		List <Jumma> jummas = currentJummaList;
    	if(jummas.size() == 0){
    		jummas = getFredagsbonnListe();
    		jummaDao.updateJummaList(jummas);
    	}
    	currentJummaList = jummas;
    	PreyItem jumma = findFredagsBonn(jummas);
    	notifyListners(jumma);
	}
	
	
	public void addJummaListner(JummaListner jummaListner){
		listners.add(jummaListner); 
	}
	
	
	private void notifyListners(PreyItem jumma){
		for( JummaListner listner : listners) {
			listner.updateJumma(jumma);
		}
	}
	
	
	private Response.ErrorListener createErrorListener() {
	    return new Response.ErrorListener() {
	        @Override
	        public void onErrorResponse(VolleyError error) {
	        	List <Jumma> jummas = jummaDao.getJummaList();
	        	if(jummas.size() == 0){
	        		jummas = getFredagsbonnListe();
	        		jummaDao.updateJummaList(jummas);
	        	}
	        	currentJummaList = jummas;
	        	PreyItem jumma = findFredagsBonn(jummas);
	        	jummaDao.closeDB();
	        	notifyListners(jumma); 
	        }
	    };
	}
	
	private PreyItem findFredagsBonn(List <Jumma> fredagsBonns ) {
		
		int day = currentTime.getDayOfWeek();
    	int daysToFriday = 5 - day;
    	if(daysToFriday<0){
    		daysToFriday = 5 + day;
    	}
    	DateTime salatTime = new DateTime().plusYears(999);
    	try {
		for(int i = 0;i<fredagsBonns.size();i++) {
			DateTime nextFriday = currentTime.plusDays(daysToFriday);
	    	if(fredagsBonns.get(i).isBetween(nextFriday)){
	    		Jumma timeOfThaOne = fredagsBonns.get(i);
	    		DateTime midnightTime = currentTime.minusHours(currentTime.getHourOfDay()).minusMinutes(currentTime.getMinuteOfHour());
	    		salatTime = midnightTime.plusDays(daysToFriday).plusHours(timeOfThaOne.getHours()).plusMinutes(timeOfThaOne.getMinuttes()).plusYears(999);
	    		break;
	    	}
		}
    	}
    	catch(Exception exception) {
    		exception.printStackTrace();
    	}
		PreyItem salat = new PreyItem("Jummah", salatTime, false);	
		return salat;
	}
	
	private List<Jumma> getFredagsbonnListe() {
		List <Jumma> fredagsbonnLiset = new ArrayList<Jumma>();
		fredagsbonnLiset.add(new Jumma(1,11,10,1,13,0));
		fredagsbonnLiset.add(new Jumma(11,1,31,1,13,0));
		fredagsbonnLiset.add(new Jumma(1,2,10,3,14,00));
		fredagsbonnLiset.add(new Jumma(11,3,31,3,14,30));
		fredagsbonnLiset.add(new Jumma(1,4,31,7,15,30));
		fredagsbonnLiset.add(new Jumma(1,8,10,10,15,00));
		return fredagsbonnLiset;
	}
	
			
	private Response.Listener <Jumma[]> createSuccessListener() {
	    return new Response.Listener <Jumma[]>() {
	       
	    	@Override
			public void onResponse(Jumma[] response) {
	    		List jummas = new ArrayList<Jumma>();
	    		for(Jumma item : response){
	    			jummas.add(item);
	    		}
	    		jummaDao.updateJummaList(jummas);
	    		PreyItem jumma = findFredagsBonn(jummas);
	        	jummaDao.closeDB();
	        	currentJummaList = jummas;
	        	notifyListners(jumma); 
	    		
			}
	    };	
	}
	
	
}
