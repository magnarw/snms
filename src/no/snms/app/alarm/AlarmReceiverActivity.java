package no.snms.app.alarm;

import java.io.IOException;

import no.snms.app.R;
import no.snms.app.R.*;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * This activity will be called when the alarm is triggered.
 * 
 * @author Dag-Martin Nilsen
 */
public class AlarmReceiverActivity extends Activity {
    private MediaPlayer mMediaPlayer; 
    private static Context mContext;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.alarm);
        Button stopAlarm = (Button) findViewById(R.id.stopAlarm);
        stopAlarm.setText("Stopp alarm");
        stopAlarm.setOnTouchListener(new OnTouchListener() {
            public boolean onTouch(View arg0, MotionEvent arg1) {
                mMediaPlayer.stop();
                //sett opp neste alarm
                
                finish();
                return false;
            }
        });
        
//        Button startsnms = (Button) findViewById(R.id.startsnms);
//        startsnms.setOnTouchListener(new OnTouchListener() {
//            public boolean onTouch(View arg0, MotionEvent arg1) {
//                mMediaPlayer.stop();
//                
//        		Intent intent = new Intent("com.example.snms");
//				try {
//					startActivity(intent);
//				} catch (ActivityNotFoundException e) {
//					
//				}
//                finish();
//                return false;
//            }
//        });

        playSound(this, getAlarmUri());
    }

    private void playSound(Context context, Uri alert) {
        mMediaPlayer = new MediaPlayer();
        try {
            mMediaPlayer.setDataSource(context, alert);
            final AudioManager audioManager = (AudioManager) context
                    .getSystemService(Context.AUDIO_SERVICE);
            if (audioManager.getStreamVolume(AudioManager.STREAM_ALARM) != 0) {
                mMediaPlayer.setAudioStreamType(AudioManager.STREAM_ALARM);
                mMediaPlayer.prepare();
                mMediaPlayer.start();
            }
        } catch (IOException e) {
            System.out.println("OOPS");
        }
    }

    //Get an alarm sound. Try for an alarm. If none set, try notification, 
    //Otherwise, ringtone.
    private Uri getAlarmUri() {
        Uri alert = RingtoneManager
                .getDefaultUri(RingtoneManager.TYPE_ALARM);
        if (alert == null) {
            alert = RingtoneManager
                    .getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            if (alert == null) {
                alert = RingtoneManager
                        .getDefaultUri(RingtoneManager.TYPE_RINGTONE);
            }
        }
        return alert;
    }
//    public static Context getContext() {
//        return mContext;
//    }

}
