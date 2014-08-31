package no.snms.app;



import com.parse.Parse;
import com.parse.ParseInstallation;
import com.parse.PushService;

import no.snms.app.images.ImageCacheManager;
import no.snms.app.network.RequestManager;
import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap.CompressFormat;

public class MainApplication extends Application {

	private static int DISK_IMAGECACHE_SIZE = 1024*1024*10;
	private static CompressFormat DISK_IMAGECACHE_COMPRESS_FORMAT = CompressFormat.PNG;
	private static int DISK_IMAGECACHE_QUALITY = 100;  //PNG is lossless so quality is ignored but must be provided
	private static Context context;
	 
	@Override
	public void onCreate() {
		super.onCreate();
		Parse.initialize(this, "wYQcUGs8yWdREXFkiVAjnov1OtFgaHnUuTeVBTXw", "cuN51kRM0uUoCSf1OCvNQg7HXSeNOFL7mED7Z5tc");
		PushService.setDefaultPushCallback(this, PreyOverView.class);
		
		MainApplication.context = getApplicationContext();

		init();
		
		  

		
	}

	/**
	 * Intialize the request manager and the image cache 
	 */
	private void init() {
		RequestManager.init(this);
		createImageCache();
	}
	
	/**
	 * Create the image cache. 
	 */
	private void createImageCache(){
		ImageCacheManager.getInstance().init(this,
				this.getPackageCodePath()
				, DISK_IMAGECACHE_SIZE
				, DISK_IMAGECACHE_COMPRESS_FORMAT
				, DISK_IMAGECACHE_QUALITY);
	}
	
    public static Context getAppContext() {
        return MainApplication.context;
    }
	
	
}
