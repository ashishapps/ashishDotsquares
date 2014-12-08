package com.tamilshout.fragments;

import android.content.Context;
import android.widget.LinearLayout;

public class CustomView  extends LinearLayout {

	public CustomView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}
	
	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
	    final int proposedheight = MeasureSpec.getSize(heightMeasureSpec);
	    final int actualHeight = getHeight();

	    if (actualHeight > proposedheight){
	        // Keyboard is shown
	    } else {
	        // Keyboard is hidden
	    }

	    super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	}

}
