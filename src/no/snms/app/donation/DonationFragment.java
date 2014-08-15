package no.snms.app.donation;

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
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
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
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.TextView.OnEditorActionListener;
import android.widget.TextView;
import android.widget.Toast;

public class DonationFragment extends Fragment implements OnClickListener {

	private TextView picker;
	private Button donationButton;
	private String[] nums = new String[10];
	private TextView dontationText;
	private TextView infoButton;
	private ImageView infoImage;
	private Integer currentValue = 200;
	private ImageButton up;
	private ImageButton down;
	private TextView donationSumPickerPrev;
	private TextView donationSumPickerNext;

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View root = inflater.inflate(R.layout.donation_wigdet, null);
		picker = (TextView) root.findViewById(R.id.donationSumPicker);
		// picker.seton
		// picker.seton

		donationSumPickerPrev = (TextView) root
				.findViewById(R.id.donationSumPickerPrev);
		donationSumPickerNext = (TextView) root
				.findViewById(R.id.donationSumPickerNext);

		donationSumPickerPrev.setText("250");
		picker.setText("200");
		donationSumPickerNext.setText("150");
		donationButton = (Button) root.findViewById(R.id.donerButton);
		infoButton = (TextView) root.findViewById(R.id.moreInfo);
		dontationText = (TextView) root.findViewById(R.id.donationSum);

		dontationText.setText("200kr");
		up = (ImageButton) root.findViewById(R.id.up);
		down = (ImageButton) root.findViewById(R.id.down);

		up.setOnClickListener(this);
		down.setOnClickListener(this);

		infoImage = (ImageView) root.findViewById(R.id.placeHolder3);
		infoButton.setOnClickListener(this);
		infoImage.setOnClickListener(this);

		return root;
	}

	/*
	 * image.setImageUrl(newsItem.getImgUrl(),
	 * ImageCacheManager.getInstance().getImageLoader());
	 */

	@Override
	public void onResume() {
		super.onResume();
		donationButton.setOnClickListener(this);
		// picker.setMaxValue(nums.length-1);
		// picker.setMinValue(0);
		// picker.setWrapSelectorWheel(false);
		// picker.setDisplayedValues(nums);
		// picker.setValue(0);
		dontationText.setText("200kr");

	}

	@Override
	public void onPause() {
		super.onPause();

	}

	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
	}

	public DonationFragment() {
		super();

	}

	@Override
	public void onClick(View v) {
		if (v.equals(donationButton)) {
			Intent smsIntent = new Intent(Intent.ACTION_VIEW);
			smsIntent.putExtra("sms_body", "Masjid " + currentValue);
			smsIntent.putExtra("address", "2380");
			smsIntent.setType("vnd.android-dir/mms-sms");
			startActivity(smsIntent);
		}

		// SharedPreferences p = getActivity().get
		if (v.equals(infoButton) || v.equals(infoImage)) {
			// 1. Instantiate an AlertDialog.Builder with its constructor
			AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
			// 2. Chain together various setter methods to set the dialog
			// characteristics
			builder.setMessage("Kostnaden for donasjon varierer med beløpets størrelse fra 4,6% til ca. 7%. Dersom ditt bidrag er større enn 500 kr, må du donere flere ganger eller overføre til konto: 6062.05.31599. Merk beløpet 'Donasjon til SNMS'.");
			// 3. Get the AlertDialog from create()
			AlertDialog dialog = builder.create();
			dialog.show();
		}
		if (v.equals(up) && currentValue - 50 > 0) {

			currentValue = currentValue - 50;
			picker.setText(String.valueOf(currentValue));
			dontationText.setText(currentValue + "kr");
			donationSumPickerPrev.setText(String.valueOf(currentValue + 50));
			donationSumPickerNext.setText(String.valueOf(currentValue - 50));

		} else if (v.equals(up) && currentValue == 50) {
			Context context = getActivity().getApplicationContext();
			CharSequence text = "Du har nådd minimumsbeløpet";
			int duration = Toast.LENGTH_SHORT;

			Toast toast = Toast.makeText(context, text, duration);
			toast.show();

		}
		if (v.equals(down)) {
			if ((currentValue + 50) <= 500) {
				currentValue = currentValue + 50;
				picker.setText(String.valueOf(currentValue));
				dontationText.setText(currentValue + "kr");
				donationSumPickerPrev
						.setText(String.valueOf(currentValue + 50));
				donationSumPickerNext
						.setText(String.valueOf(currentValue - 50));
			} else {
				Context context = getActivity().getApplicationContext();
				CharSequence text = "Du har nådd maksimumsbeløpet";
				int duration = Toast.LENGTH_SHORT;

				Toast toast = Toast.makeText(context, text, duration);
				toast.show();
			}
		}

	}
}
