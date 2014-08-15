package no.snms.app.jumma;

import no.snms.app.domain.Jumma;
import no.snms.app.network.GsonRequest;
import no.snms.app.network.RequestManager;
import android.net.Uri;
import android.util.Log;

import com.android.volley.Request.Method;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;

public class JummaManager {

	private final String TAG = getClass().getSimpleName();
	private static JummaManager mInstance;

	private static String NEWS_BASE =  "http://app.muslimskesenter.no/snmscms/rest/json/jumma";


	public static JummaManager getInstance(){
		if(mInstance == null) {
			mInstance = new JummaManager();
		}

		return mInstance;
	}

	public void getJumma(Listener< Jumma[]> listener, ErrorListener errorListener){
		Uri.Builder uriBuilder = Uri.parse(NEWS_BASE).buildUpon();
		String uri = uriBuilder.build().toString();
		Log.i(TAG, "getTweetForHashtag: uri = " + uri);

		GsonRequest< Jumma[]> request = new GsonRequest< Jumma[]>(Method.GET
				, uri
				,  Jumma[].class
				, listener
				, errorListener);
		Log.v(TAG, request.toString());
		RequestManager.getRequestQueue().add(request);
	}

}
