package com.darknessinc.duelmasters.imageutils;

import android.graphics.Color;
import android.widget.ImageView;

public class ImageOverlayer {
	//TODO change overlay color
	public static void indicateOverlay(ImageView iv){
		iv.setColorFilter(Color.rgb(100, 0, 0), android.graphics.PorterDuff.Mode.MULTIPLY);
	}
	public static void tapOverlay(ImageView iv){
		iv.setColorFilter(Color.rgb(100, 100, 100), android.graphics.PorterDuff.Mode.MULTIPLY);
	}
	public static void removeOverlay(ImageView iv){
		iv.setColorFilter(Color.rgb(255,255,255), android.graphics.PorterDuff.Mode.MULTIPLY);
	}
}
