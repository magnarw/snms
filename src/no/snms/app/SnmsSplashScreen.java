package no.snms.app;

import java.util.Timer;
import java.util.TimerTask;

import no.snms.app.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;

public class SnmsSplashScreen extends Activity
{
    private static final long DELAY = 6000;
    private boolean scheduled = false;
    private Timer splashTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.snmssplashscreen);
        splashTimer = new Timer();
        
        splashTimer.schedule(new TimerTask()
        {
            @Override
            public void run()
            {
            	SnmsSplashScreen.this.finish();
                startActivity(new Intent(SnmsSplashScreen.this, no.snms.app.PreyOverView.class));
            }
         }, DELAY);
       scheduled = true;
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        if (scheduled)
            splashTimer.cancel();
        splashTimer.purge();
    }
}