package com.example.snms.qibla;

import com.example.snms.R;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.opengl.Matrix;
import android.util.AttributeSet;
import android.view.ViewGroup.MarginLayoutParams;
import android.widget.ImageView;

public class QuiblaCompassImage extends ImageView{
	
	
	   Float angle = null;  
	
	   public QuiblaCompassImage(Context context,AttributeSet attributeSet) {
	        super(context,attributeSet);
	        // TODO Auto-generated constructor stub
	    }
	   


	    @SuppressLint("ResourceAsColor")
		@Override
	    protected void onDraw(Canvas canvas) {
	        super.onDraw(canvas);
	        if(angle!=null) {
	            final Paint p = new Paint(Paint.ANTI_ALIAS_FLAG);
	            final int paddingLeft = getPaddingLeft();
	            final int paddingTop = getPaddingTop();

	            canvas.save();

	            // do my rotation and other adjustments
	            canvas.rotate(-angle,canvas.getWidth()/2, canvas.getHeight()/2);
	            
	            if( paddingLeft!=0 )
	                canvas.translate(paddingLeft,0);

	            if( paddingTop!=0 )
	                canvas.translate(0,paddingTop);

	            canvas.drawBitmap( BitmapFactory.decodeResource(getContext().getResources(),
	          			R.drawable.ic_compass_arrow),0,0,p );
	            canvas.restore();
	        }

	    }
	    
	    
	    public void setAngle(Float angle){
	    	this.angle = angle; 
	    }
	
	    
	
}
