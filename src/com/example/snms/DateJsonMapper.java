package com.example.snms;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;


import java.lang.reflect.Type;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

import android.util.Log;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;



public class DateJsonMapper implements JsonDeserializer<DateTime> {

    public DateTime deserialize(JsonElement json, Type typeOfT,
            JsonDeserializationContext context) throws JsonParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        try {
        	DateTimeZone zone = DateTimeZone.forID(TimeZone.getDefault().getID());
        	return new DateTime(json.getAsLong(),zone);
		
		} catch (Exception e) {
			e.printStackTrace();
			SimpleDateFormat dateFormat2 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
			try {
				return DateTime.parse("2013-10-27T00:00:33:00");
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
			return new DateTime(); 
		}
    }
}