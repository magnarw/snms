package no.snms.app.qibla;

import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import no.snms.app.images.ImageCacheManager;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import com.android.volley.toolbox.NetworkImageView;
import no.snms.app.R;
import no.snms.app.R.id;
import no.snms.app.R.layout;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.TextView;

public class QiblaFragment extends Fragment implements SensorEventListener,
		LocationListener {

	/*
	 * 
	 * var y = Math.sin(dLon) * Math.cos(lat2); var x =
	 * Math.cos(lat1)*Math.sin(lat2) -
	 * Math.sin(lat1)*Math.cos(lat2)*Math.cos(dLon); var brng = Math.atan2(y,
	 * x).toDeg();
	 */
	private LocationManager locationManager;
	// define the display assembly compass picture
	private QuiblaCompassImage image;

	// record the compass picture angle turned
	private float currentDegree = 0f;

	// device sensor manager
	private SensorManager mSensorManager;

	private Location location;

	TextView tvHeading;

	double qiblaLat = 21d;
	double qiblaLong = 39d;

	ImageView qiblaArrow;
	double qiblaAngle = 0.0; 

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View root = inflater.inflate(R.layout.qibla, null);

		//
		image = (QuiblaCompassImage) root.findViewById(R.id.imageViewCompass);
		// TextView that will tell the user what degree is he heading

		// initialize your android device sensor capabilities
		mSensorManager = (SensorManager) this.getActivity().getSystemService(
				Activity.SENSOR_SERVICE);
//		locationManager = (LocationManager) this.getActivity()
//				.getSystemService(Activity.LOCATION_SERVICE);
		return root;
	}

	/*
	 * image.setImageUrl(newsItem.getImgUrl(),
	 * ImageCacheManager.getInstance().getImageLoader());
	 */
	
	
	
	
	@Override
	public void onResume() {
		super.onResume();
		float angle = (float) getQiblaAngle();
	 //	rotateArrow(angle);
		
		// for the system's orientation sensor registered listeners
		mSensorManager.registerListener(this,
				mSensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION),
				SensorManager.SENSOR_DELAY_GAME);
//		locationManager.requestLocationUpdates(
//				LocationManager.NETWORK_PROVIDER, 0, 0, this);

		// getQiblaAngle();
	
		this.image.setAngle(angle);

	}
	
	private void rotateArrow(Float angle){
		RotateAnimation ra = new RotateAnimation(currentDegree, -angle,
				Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
				0.5f);
		System.out.println("this is angle:" + angle);
		ra.setDuration(210);
		ra.setFillAfter(true);
	
		qiblaArrow.startAnimation(ra);
		qiblaAngle = angle; 
		
	}
	

	@Override
	public void onPause() {
		super.onPause();

		// to stop the listener and save battery
		mSensorManager.unregisterListener(this);
	}

	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
	}

	public QiblaFragment() {
		super();

	}

	@Override
	public void onAccuracyChanged(Sensor arg0, int arg1) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onSensorChanged(SensorEvent event) {
		// get the angle around the z-axis rotated
		float degree = Math.round(event.values[0]);


		// create a rotation animation (reverse turn degree degrees)
		RotateAnimation ra = new RotateAnimation(currentDegree, degree,
				Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
				0.5f);
		
		
	
		// how long the animation will take place
		ra.setDuration(210);
		// set the animation after the end of the reservation status
		ra.setFillAfter(true);

		// Start the animation
		image.startAnimation(ra);
	//	qiblaArrow.setRotationX(image.getRotationX());
		currentDegree = -degree;

	}

	private double getQiblaAngle() {
		/*
		 * var y = Math.sin(dLon) * Math.cos(lat2); var x =
		 * Math.cos(lat1)*Math.sin(lat2) -
		 * Math.sin(lat1)*Math.cos(lat2)*Math.cos(dLon); var brng =
		 * Math.atan2(y, x).toDeg();
		 */

		/*
		 * 
		 * var y = Math.sin(dLon) * Math.cos(lat2); var x =
		 * Math.cos(lat1)*Math.sin(lat2) -
		 * Math.sin(lat1)*Math.cos(lat2)*Math.cos(dLon); var brng =
		 * Math.atan2(y, x).toDeg();
		 */

		// double lat1 = this.location.getLatitude();
		// double lon1 = this.location.getLongitude();
		
		Location targetLocation = new Location("");//provider name is unecessary
		targetLocation.setLatitude(59d);//your coords of course
		targetLocation.setLongitude(10d);
		
		Location qibla = new Location("");
		qibla.setLatitude(qiblaLat);
		qibla.setLongitude(qiblaLong);
		
		return qibla.bearingTo(targetLocation);
		
//		float lat1 = 1.35208f;
//		float lon1 = 103.819f;
//		float lat2 = qiblaLat;
//		float lon2 = qiblaLong;

	}

	@Override
	public void onLocationChanged(Location location) {
		if (location != null) {
			this.location = location;
			locationManager.removeUpdates(this);
		}

	}

	@Override
	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub

	}

	// 1) Get location
	// 2) Calculate vetvor
	// 3) Draw vetvor

}
