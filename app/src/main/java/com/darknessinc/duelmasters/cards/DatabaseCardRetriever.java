package com.darknessinc.duelmasters.cards;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Context;
import android.util.Log;

import com.darknessinc.duelmasters.R;
import com.darknessinc.duelmasters.fileutils.FilePathManager;

public class DatabaseCardRetriever {
	private Card[] mAllCards;
	Context mContext;	
	/**
	 * assumes JSON is sorted according to id
	 * @param dbIds
	 * @param c
	 * @return
	 */
	public static ArrayList<Card> getCards(int dbIds[], Context c){
		ArrayList<Card> allCards = new ArrayList<Card>();
		Arrays.sort(dbIds);
		int dbCtr = 0;
		StringBuilder contents = new StringBuilder();
	    String sep = System.getProperty("line.separator");
 
	    try {			
	      InputStream is = c.getResources().openRawResource(R.raw.dmjson);
	      
	      BufferedReader input =  new BufferedReader(new InputStreamReader(is), 1024*8);
	      try {
	        String line = null; 
	        while (( line = input.readLine()) != null){
	          contents.append(line);
	          contents.append(sep);
	        }
	      }
	      finally {
	        input.close();
	      }
	    }
	    catch (FileNotFoundException ex) {
	    	ex.printStackTrace();	   
	    }
	    catch (IOException ex){
	    	ex.printStackTrace();
	    } 
	    try{		    
	    	JSONObject jsonObj = new JSONObject(contents.toString());
	    	JSONArray cards=jsonObj.getJSONArray("cards");
	    	for(int i=0;i<cards.length();i++){
	    		JSONObject jCard = cards.getJSONObject(i);
	    		int id=Integer.parseInt(jCard.getString("dbid"));
	    		String name=jCard.getString("name");
	    		String imagepath="";
	    		String type=jCard.getString("type");
	    		String text="";
	    		if(dbIds[dbCtr]==id){
	    			dbCtr++;
	    			allCards.add(new Card(id,name,imagepath,type,text)); 
	    		}
	    	}
	    }catch(Exception e){
	    	e.printStackTrace();
	    }
	    return allCards;
	}
	public Card getCard(int dbid){
		for(Card c: mAllCards){
			if(c.getDbId()==dbid)
				return c;
		}
		return null;
	}
	public DatabaseCardRetriever(Context ctx){
		mContext = ctx;
		Card[] allCards = null;;
		StringBuilder contents = new StringBuilder();
	    String sep = System.getProperty("line.separator");
 
	    try {			
	      InputStream is = mContext.getResources().openRawResource(R.raw.dmjson);
	      
	      BufferedReader input =  new BufferedReader(new InputStreamReader(is), 1024*8);
	      try {
	        String line = null; 
	        while (( line = input.readLine()) != null){
	          contents.append(line);
	          contents.append(sep);
	        }
	      }
	      finally {
	        input.close();
	      }
	    }
	    catch (FileNotFoundException ex) {
	    //	Log.e(TAG, "Couldn't find the file " + resourceID  + " " + ex);
	   
	    }
	    catch (IOException ex){
	    	//Log.e(TAG, "Error reading file " + resourceID + " " + ex);
	   
	    } 
	    
	    String s= contents.toString();
	    try{
	    
	    	JSONObject jsonObj = new JSONObject(s);
	    	JSONArray cards=jsonObj.getJSONArray("cards");
	    	allCards=new Card[cards.length()];
	    	for(int i=0;i<cards.length();i++){
	    		JSONObject c= cards.getJSONObject(i);
	    		int id=Integer.parseInt(c.getString("dbid"));
	    		String name=c.getString("name");
	    		String imagepath=getImagePathFromName(name);
	    		String type=c.getString("type");
	    		String text="";
	    		allCards[i]=new Card(id,name,imagepath,type,text);	    	
	    	}
	    }catch(Exception e){
	    	Log.d("json",s);
	    }
	    this.mAllCards=allCards;
	}
	public String getImagePathFromName(String name){
		String  nopun = name.replaceAll("[^\\w\\s]","");//removes all special characters
    	String x=nopun.replaceAll(" ","_").toLowerCase();//replaces space with "_" and converts to lowercase
    	String y=FilePathManager.getImageDirectory(mContext).getPath();
    	String imagepath=y+x;
    	return imagepath;
	}
}
