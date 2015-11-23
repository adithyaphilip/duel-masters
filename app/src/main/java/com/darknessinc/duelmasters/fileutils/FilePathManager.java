package com.darknessinc.duelmasters.fileutils;

import java.io.File;

import android.content.Context;

public class FilePathManager {
	public final static String CARD_IMAGES_NAME="cardimages";
	public final static String DECK_DIRECTORY="deck";
	
	public static File getImageDirectory(Context c){
		File f = c.getDir(CARD_IMAGES_NAME, Context.MODE_PRIVATE);
		return f;
	}
	public static File getDeckDirectory(Context c){
		File f = c.getDir(DECK_DIRECTORY, Context.MODE_PRIVATE);
		return f;
	}
}
