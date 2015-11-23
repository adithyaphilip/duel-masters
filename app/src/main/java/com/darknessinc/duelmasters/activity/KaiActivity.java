package com.darknessinc.duelmasters.activity;



import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.darknessinc.duelmasters.R;
import com.darknessinc.duelmasters.cards.Card;
import com.darknessinc.duelmasters.fileutils.FilePathManager;

public class KaiActivity extends Activity {
	ArrayList<Card> cs=new ArrayList<Card>();
	Card[] allCards;
	ListView lv;
	EditText inputSearch;
	ArrayAdapter<Card> adapter;
	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_kai);
	//	Button b=(Button)findViewById(R.id.returnbtn);
		cs=new ArrayList<Card>();
		cardmaker(getApplicationContext());
		lv = (ListView) findViewById(R.id.list_view);
		inputSearch = (EditText) findViewById(R.id.inputSearch);
		 adapter = new ArrayAdapter<Card>(this,
		        android.R.layout.simple_list_item_1, allCards);
		    lv.setAdapter(adapter);
		    lv.setOnItemClickListener(new OnItemClickListener() {

		    	  public void onItemClick(AdapterView adapterView, View view, int position, long id) {
	  
                        Card card=(Card)adapter.getItem(position);
                        cs.add(card);
                     Toast.makeText(getApplicationContext(), "CARD ADDED", Toast.LENGTH_SHORT).show();                 
                        
		    	  }
		    	});
		    lv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
		        @Override
		        public boolean onItemLongClick(AdapterView<?> av, View v, int pos, long id) {
		            return onLongListItemClick(v,pos,id);
		        }
		    });
		    inputSearch.addTextChangedListener(new TextWatcher() {
	             
	            @Override
	            public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
	                // When user changed the Text
	                KaiActivity.this.adapter.getFilter().filter(cs);   
	            }
	             
	            @Override
	            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
	                    int arg3) {
	                // TODO Auto-generated method stub
	                 
	            }
	             
	            @Override
	            public void afterTextChanged(Editable arg0) {
	                // TODO Auto-generated method stub                          
	            }
	        });
		    
		    
		    
	}
protected boolean onLongListItemClick(View v, int pos, long id) {
	//TODO make it display the correct image instead of Doge	
      	Card card=(Card)adapter.getItem(pos);
		ImageView image = new ImageView(this);
       // image.setImageResource(R.drawable.images);
        Bitmap bitmapOrig=(card.getCardBitmap(222,307));
		 Bitmap bitmapScaled = Bitmap.createScaledBitmap(bitmapOrig,723,1000, true);
		 image.setImageBitmap(bitmapScaled) ; 
        AlertDialog.Builder builder = 
                new AlertDialog.Builder(this).
                setMessage("The Card is").
                setPositiveButton("OK", new OnClickListener() {                     
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                    }
                }).
                setView(image);
        builder.create().show();
		
	
	    return true;
	}

	
	void cardmaker(Context ctx){
		
		StringBuilder contents = new StringBuilder();
	    String sep = System.getProperty("line.separator");
 
	    try {			
	      InputStream is = ctx.getResources().openRawResource(R.raw.dmjson);
	      
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
	    int j=0;
	    JSONObject jsonObj = new JSONObject(s);
	    JSONArray cards=jsonObj.getJSONArray("cards");
	    allCards=new Card[cards.length()];
	    for(int i=0;i<cards.length();i++){
	    	JSONObject c= cards.getJSONObject(i);
	    	int id=Integer.parseInt(c.getString("dbid"));
	    	String name=c.getString("name");
	    	String  nopun = name.replaceAll("[^\\w\\s]","");//removes all special characters
	    	String x=nopun.replaceAll(" ","_").toLowerCase();//replaces space with "_" and converts to lowercase
	    	String y=FilePathManager.getImageDirectory(ctx).getPath();
	    	String z="/";
	    	String imagepath=y+x;
	    	if(j==0){
	    	Toast.makeText(this,imagepath,Toast.LENGTH_LONG).show();
	    	j++;}
	    	String type=c.getString("type");
	    	String text="";
	    	allCards[i]=new Card(id,name,imagepath,type,text);
	    	
	    }
	    }
	    
	    catch(Exception e){
	   Toast.makeText(getApplicationContext(), "Something went wrong", Toast.LENGTH_LONG).show();
	    }
	  
	}
	
	public void retdeck(View v){
		 Intent i= new Intent();
         
         i.putExtra("list",cs);
         setResult(Activity.RESULT_OK,i);
         finish();
	}
	
	
}
