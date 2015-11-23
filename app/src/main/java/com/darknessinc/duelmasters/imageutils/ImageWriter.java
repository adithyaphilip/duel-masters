package com.darknessinc.duelmasters.imageutils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.widget.ImageView;

public class ImageWriter {
	public static void writeImage(Bitmap bmp, File path){
		FileOutputStream out = null;
		try {
		    out = new FileOutputStream(path);
		    bmp.compress(Bitmap.CompressFormat.JPEG, 90, out);
		} catch (Exception e) {
		    e.printStackTrace();
		} finally {
		    try {
		        if (out != null) {
		            out.close();
		        }
		    } catch (IOException e) {
		        e.printStackTrace();
		    }
		}
	}
	public static void writeImage(ImageView iv, File path){
		Bitmap bitmap = ((BitmapDrawable)iv.getDrawable()).getBitmap();
		writeImage(bitmap, path);
	}
}
