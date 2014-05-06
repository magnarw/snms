package com.example.snms;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

public class SnmsTextView extends TextView {

	
	
	public SnmsTextView(Context context, AttributeSet attrs) {
        super(context, attrs);

        Typeface typeface = Typeface.createFromAsset(context.getAssets(), "euphemia-1361509542.ttf");
        setTypeface(typeface);
    }

}
