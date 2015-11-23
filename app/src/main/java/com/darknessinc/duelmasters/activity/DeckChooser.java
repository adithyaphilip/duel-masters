package com.darknessinc.duelmasters.activity;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.StringTokenizer;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.darknessinc.duelmasters.R;
import com.darknessinc.duelmasters.bluetooth.ConnectActivity;
import com.darknessinc.duelmasters.cards.Card;
import com.darknessinc.duelmasters.cards.DatabaseCardRetriever;
import com.darknessinc.duelmasters.cards.GameCard;
import com.darknessinc.duelmasters.fileutils.FilePathManager;
import com.darknessinc.duelmasters.imageutils.ImageWriter;
import com.darknessinc.duelmasters.player.Player;

public class DeckChooser extends Activity {
	ListView lv;
	ArrayList<String> decks=new ArrayList<String>();
	ArrayAdapter<String> adapter;
	DatabaseCardRetriever mDBCR;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_deck_chooser);
		 mDBCR = new DatabaseCardRetriever(this);
		decks = getDeckNames();
		lv = (ListView) findViewById(R.id.list_view);
		adapter = new ArrayAdapter<String>(this,
		        android.R.layout.simple_list_item_1, decks);
		 lv.setAdapter(adapter);
		
		 lv.setOnItemClickListener(new OnItemClickListener() {
			
	    	  public void onItemClick(AdapterView adapterView, View view, int position, long id) {
                    //TODO opens it    		  
	    		  
	    		  
                 String selected_deck=decks.get(position);
                 
                 Toast.makeText(getApplicationContext(), "selected deck: "+selected_deck, Toast.LENGTH_SHORT).show();
                 populateDeck(selected_deck);
                 
                 //okay? yup
                //yeah.does not get name

                 //correct? yu
                //what's wrong? //how to do? :P
                // cards.remove(card);
                 
	    	  }
	    	});
		 
		
	}

	
public int[] getDbIdsFromFile(String filename){ //works wait
		
		File f = new File(FilePathManager.getDeckDirectory(this).getPath()+"/"+filename);
	 
		try{
		 Scanner sc = new Scanner(f);
		 String s = sc.nextLine();
		// Toast.makeText(getApplicationContext(), "SUCCESS: "+s, Toast.LENGTH_LONG).show();
		 sc.close();
		 sc = new Scanner(s);
		 StringBuilder sb=new StringBuilder();
		 while(sc.hasNext()){
			 sb.append(sc.next());
		 }
		 sc.close();
		 String str=sb.toString();
		 StringTokenizer st=new StringTokenizer(str,",");
		 ArrayList<Integer> dbid=new ArrayList<Integer>();
		 int i=0;
		//Toast.makeText(getApplicationContext(), "WORKS 1", Toast.LENGTH_LONG).show();
				
			
		 while(st.hasMoreTokens()){
			// Toast.makeText(getApplicationContext(), "WORKS 2", Toast.LENGTH_LONG).show();
			 
			int m=(Integer.valueOf(st.nextToken())).intValue();
			//Toast.makeText(getApplicationContext(), "WORKS 3", Toast.LENGTH_LONG).show();
			dbid.add(m);
			
			
		 }
		 int[]temp=new int[40];
		 for( i=0;i<dbid.size();i++){
			temp[i]=(int)dbid.get(i);
			 
		 }
		 
		 
		 
		 return temp;
		}
		
		
		catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}
	public ArrayList<String> getDeckNames(){
		ArrayList<String> al = new ArrayList<String>();
		for(File f: getDeckFiles()){
	//cx		Toast.makeText(this, f.getName(),Toast.LENGTH_SHORT).show();//correct it no
			
			al.add(f.getName());
		} 
		return al;
	}
	public File[] getDeckFiles(){

	//	Toast.makeText(this,FilePathManager.getDeckDirectory(this).getPath(),Toast.LENGTH_SHORT).show();
		return FilePathManager.getDeckDirectory(this).listFiles();
	}
	public void writeNewDeck(String name, int[] dbids){
		try{
			File deck = new File(FilePathManager.getDeckDirectory(this)+"/"+name);
			//Toast.makeText(this,deck.getPath(),Toast.LENGTH_SHORT).show();
			PrintWriter pw = new PrintWriter(deck);
			StringBuilder sb = new StringBuilder(""+dbids[0]);
			for(int i=1;i<dbids.length;i++){
				sb.append(","+dbids[i]);
			}
			pw.print(sb.toString());
			
						pw.close();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	public void populateDeck(String ste){
		int[] temp=new int[100];
		
		File f = new File(FilePathManager.getDeckDirectory(this).getPath()+"/"+ste);
		//only last card is shown :P wait only 1 card is shown 
			try{
			 Scanner sc = new Scanner(f);
			
			 String s = sc.nextLine();
			 //Toast.makeText(getApplicationContext(), "SUCCESS: "+s, Toast.LENGTH_LONG).show();
			 sc.close();
			 sc = new Scanner(s);
			 StringBuilder sb=new StringBuilder();
			 while(sc.hasNext()){
				 sb.append(sc.next());
			 }
			 sc.close();
			 String str=sb.toString();
			 StringTokenizer st=new StringTokenizer(str,",");
			 ArrayList<Integer> dbid=new ArrayList<Integer>();
			 int i=0;
			//Toast.makeText(getApplicationContext(), "WORKS 1", Toast.LENGTH_LONG).show();
					
				
			 while(st.hasMoreTokens()){
				 //Toast.makeText(getApplicationContext(), "WORKS 2", Toast.LENGTH_LONG).show();
				 /*Toast.makeText(getApplicationContext(), st.nextToken(), Toast.LENGTH_LONG).show();
				 Toast.makeText(getApplicationContext(), st.nextToken(), Toast.LENGTH_LONG).show();
				 Toast.makeText(getApplicationContext(), st.nextToken(), Toast.LENGTH_LONG).show(); */
				int m=(Integer.valueOf(st.nextToken())).intValue();
				//Toast.makeText(getApplicationContext(), "WORKS 3", Toast.LENGTH_LONG).show();
				dbid.add(m);
				
				
			 }
			 temp=new int[dbid.size()];
			 for( i=0;i<dbid.size();i++){
				temp[i]=(int)dbid.get(i);
				 
			 }
			 //whats happening?
			 //it never shows success thing
			// Toast.makeText(getApplicationContext(), "SUCCESS: ", Toast.LENGTH_LONG).show();
			 
			}
			
			
			catch(Exception e){
				e.printStackTrace();
				
			}	
			if(temp==null)
				return;
			//Toast.makeText(this, "arraylength"+temp.length, Toast.LENGTH_SHORT).show();
			gamecardmaker(getApplicationContext(),temp);                                          //correct? yea but i dunno about the cast to String in the previous part. You know it works right?nope. show?as in? run? no the previous activity
		}

void gamecardmaker(Context ctx,int[] dbids){
	final ImageView iv = new ImageView(this);
	File f = new File(FilePathManager.getImageDirectory(this).getPath()+"zorvaz_the_bonecrusher");
	if(f.exists()){
		ArrayList<GameCard>gcs=new ArrayList<GameCard>();
		int cardCtr = 1;
		for(int i=0;i<dbids.length;i++){
			GameCard gc11 = new GameCard(cardCtr++*10+2, mDBCR.getCard(dbids[i]));
			gcs.add(gc11);
		}
		Player p1 = new Player(2, "kai", gcs);
		Intent i = new Intent(this, ConnectActivity.class);
		i.putExtra(ConnectActivity.KEY_OWN_CARDS, gcs);
		startActivity(i);
	}
	else{
		Toast.makeText(getApplicationContext(), "CAN'T",Toast.LENGTH_LONG).show();
	}
	
	
}



}
