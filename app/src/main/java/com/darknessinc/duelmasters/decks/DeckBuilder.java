package com.darknessinc.duelmasters.decks;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Scanner;
import java.util.StringTokenizer;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.darknessinc.duelmasters.R;
import com.darknessinc.duelmasters.activity.KaiActivity;
import com.darknessinc.duelmasters.cards.Card;
import com.darknessinc.duelmasters.fileutils.FilePathManager;

public class DeckBuilder extends Activity {
    ArrayList<Card> cards = new ArrayList<Card>();//the one we should work on?//yes
    ListView lv;
    ArrayAdapter<Card> adapter;
    public final static String KEY_RETURN_DBIDS = "dbids";
    public final static String KEY_DBIDS = "dbidsin";
    public final static String KEY_DECK_NAME = "deckname";
    public final static String KEY_RETURN_DECK_NAME = "decknamer";
    String deckName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_deck_builder);
        Intent g = getIntent();
        deckName = g.getStringExtra(KEY_DECK_NAME);
        TextView teevee = (TextView) this.findViewById(R.id.name);
        teevee.setText(deckName);                                  //Empty wait
        //Toast.makeText(getApplicationContext(), deckName, Toast.LENGTH_SHORT).show();
        populateDeck();
        AlertDialog.Builder builder =
                new AlertDialog.Builder(this).
                        setMessage("Use the + button to add cards to your deck\n\nPress and hold for Card info.\n\nSingle tap to remove from deck").
                        setPositiveButton("Got It!", new OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
        builder.create().show();

        //dude also
        //populate listview in decks.give string array of file names with filenames


        TextView tv = (TextView) findViewById(R.id.size);
        tv.setText("SIZE OF DECK : " + cards.size());
        if (cards.size() > 40) tv.setTextColor(Color.RED);
        else tv.setTextColor(Color.WHITE);
        lv = (ListView) findViewById(R.id.list_view);

        adapter = new ArrayAdapter<Card>(this,
                android.R.layout.simple_list_item_1, cards);
        lv.setAdapter(adapter);

        lv.setOnItemClickListener(new OnItemClickListener() {

            public void onItemClick(AdapterView adapterView, View view, int position, long id) {
                //TODO Removes it


                Card card = (Card) adapter.getItem(position);
                remove(card);


                // cards.remove(card);

            }
        });
        lv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> av, View v, int pos, long id) {
                return onLongListItemClick(v, pos, id);
            }
        });

    }

    public int[] getDbIdsFromFile(String filename) { //works wait
        //I see what's wrong
        //clicked on existing deck . shows success:5 success
        File f = new File(FilePathManager.getDeckDirectory(this).getPath() + "/" + filename);
        //only last card is shown :P wait only 1 card is shown
        try {
            Scanner sc = new Scanner(f);
            ArrayList<Integer> al = new ArrayList<Integer>();

            String s = sc.nextLine();
            Toast.makeText(getApplicationContext(), "SUCCESS: " + s, Toast.LENGTH_LONG).show();
            sc.close();
            sc = new Scanner(s);
            StringBuilder sb = new StringBuilder();
            while (sc.hasNext()) {
                sb.append(sc.next());
            }
            sc.close();
            String str = sb.toString();
            StringTokenizer st = new StringTokenizer(str, ",");
            ArrayList<Integer> dbid = new ArrayList<Integer>();
            int i = 0;
            Toast.makeText(getApplicationContext(), "WORKS 1", Toast.LENGTH_LONG).show();


            while (st.hasMoreTokens()) {
                Toast.makeText(getApplicationContext(), "WORKS 2", Toast.LENGTH_LONG).show();
             /*Toast.makeText(getApplicationContext(), st.nextToken(), Toast.LENGTH_LONG).show();
			 Toast.makeText(getApplicationContext(), st.nextToken(), Toast.LENGTH_LONG).show();
			 Toast.makeText(getApplicationContext(), st.nextToken(), Toast.LENGTH_LONG).show(); */
                int m = (Integer.valueOf(st.nextToken())).intValue();
                Toast.makeText(getApplicationContext(), "WORKS 3", Toast.LENGTH_LONG).show();
                dbid.add(m);


            }
            int[] temp = new int[al.size()];
            for (i = 0; i < dbid.size(); i++) {
                temp[i] = (int) al.get(i);

            }
            //whats happening?
            //it never shows success thing
            // Toast.makeText(getApplicationContext(), "SUCCESS: ", Toast.LENGTH_LONG).show();
            return temp;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }


    }


    //there. arraylength 0//works when 1 card is there. fails for more than 1
    public void populateDeck() {
        int[] temp = new int[100];
        Intent it = getIntent();
        String ste = it.getStringExtra(KEY_DECK_NAME);
        File f = new File(FilePathManager.getDeckDirectory(this).getPath() + "/" + ste);
        //only last card is shown :P wait only 1 card is shown
        try {
            Scanner sc = new Scanner(f);

            String s = sc.nextLine();
            //Toast.makeText(getApplicationContext(), "SUCCESS: "+s, Toast.LENGTH_LONG).show();
            sc.close();
            sc = new Scanner(s);
            StringBuilder sb = new StringBuilder();
            while (sc.hasNext()) {
                sb.append(sc.next());
            }
            sc.close();
            String str = sb.toString();
            StringTokenizer st = new StringTokenizer(str, ",");
            ArrayList<Integer> dbid = new ArrayList<Integer>();
            int i = 0;
            //Toast.makeText(getApplicationContext(), "WORKS 1", Toast.LENGTH_LONG).show();


            while (st.hasMoreTokens()) {
                //Toast.makeText(getApplicationContext(), "WORKS 2", Toast.LENGTH_LONG).show();
				 /*Toast.makeText(getApplicationContext(), st.nextToken(), Toast.LENGTH_LONG).show();
				 Toast.makeText(getApplicationContext(), st.nextToken(), Toast.LENGTH_LONG).show();
				 Toast.makeText(getApplicationContext(), st.nextToken(), Toast.LENGTH_LONG).show(); */
                int m = (Integer.valueOf(st.nextToken())).intValue();
                //Toast.makeText(getApplicationContext(), "WORKS 3", Toast.LENGTH_LONG).show();
                dbid.add(m);


            }
            temp = new int[dbid.size()];
            for (i = 0; i < dbid.size(); i++) {
                temp[i] = (int) dbid.get(i);

            }
            //whats happening?
            //it never shows success thing
            // Toast.makeText(getApplicationContext(), "SUCCESS: ", Toast.LENGTH_LONG).show();

        } catch (Exception e) {
            e.printStackTrace();

        }

        //int[] dbids=getDbIdsFromFile(s);
        //	Toast.makeText(getApplicationContext(), dbids[0], Toast.LENGTH_LONG).show();
        if (temp == null)
            return;
        //Toast.makeText(this, "arraylength"+temp.length, Toast.LENGTH_SHORT).show();
        cardmaker(getApplicationContext(), temp);                                          //correct? yea but i dunno about the cast to String in the previous part. You know it works right?nope. show?as in? run? no the previous activity
    }

    void cardmaker(Context ctx, int[] dbids) {

        StringBuilder contents = new StringBuilder();
        String sep = System.getProperty("line.separator");

        try {
            InputStream is = ctx.getResources().openRawResource(R.raw.dmjson);

            BufferedReader input = new BufferedReader(new InputStreamReader(is), 1024 * 8);
            try {
                String line = null;
                while ((line = input.readLine()) != null) {
                    contents.append(line);
                    contents.append(sep);
                }
            } finally {
                input.close();
            }
        } catch (FileNotFoundException ex) {
            //	Log.e(TAG, "Couldn't find the file " + resourceID  + " " + ex);

        } catch (IOException ex) {
            //Log.e(TAG, "Error reading file " + resourceID + " " + ex);

        }


        String s = contents.toString();
        try {

            JSONObject jsonObj = new JSONObject(s);
            JSONArray cards = jsonObj.getJSONArray("cards");
            int j = 0;//to loop through dbid
            for (int i = 0; i < cards.length() && j < dbids.length; i++) {
                JSONObject c = cards.getJSONObject(i);
                Log.e("JSON", "" + c.getInt("dbid")); //getInt parses for you
                int id = Integer.parseInt(c.getString("dbid"));
                if (dbids[j] == id) {
                    String name = c.getString("name");
                    String nopun = name.replaceAll("[^\\w\\s]", "");//removes all special characters
                    String x = nopun.replaceAll(" ", "_").toLowerCase();//replaces space with "_" and converts to lowercase
                    String y = FilePathManager.getImageDirectory(ctx).getPath();
                    String z = "/";
                    String imagepath = y + x;
                    String type = c.getString("type");
                    String text = "";
                    Card temp = new Card(id, name, imagepath, type, text);
                    j++;
                    i--; //shadowingthis.
                    this.cards.add(temp);
                    Log.e("DeckBuilder", "added: " + id);

                }
            }
        } //phone wasn't connected //arraylength0 wtf
        //wtf. How ere we able to click on an option if the files doesnt exist?why does it show/New when I just made New
        //passing filename is the problem check
        catch (Exception e) {//here?
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "Something went wrong", Toast.LENGTH_LONG).show();
        }

    }

    protected boolean onLongListItemClick(View v, int pos, long id) {
        //TODO make it display the correct image instead of Doge
        Card card = (Card) adapter.getItem(pos);
        ImageView image = new ImageView(this);
        // image.setImageResource(R.drawable.images);
        Bitmap bitmapOrig = (card.getCardBitmap(222, 307));
        Bitmap bitmapScaled = Bitmap.createScaledBitmap(bitmapOrig, 723, 1000, true);
        image.setImageBitmap(bitmapScaled);
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

    public void remove(Card c) {
        cards.remove(c);
        Collections.sort(cards, new Comparator<Card>() {
            @Override
            public int compare(Card card1, Card card2) {

                return card1.getName().compareTo(card2.getName());
            }
        });
        adapter = new ArrayAdapter<Card>(this,
                android.R.layout.simple_list_item_1, cards);
        lv.setAdapter(adapter);
        TextView tv = (TextView) findViewById(R.id.size);
        tv.setText("SIZE OF DECK : " + cards.size());
        if (cards.size() > 40) tv.setTextColor(Color.RED);
        else tv.setTextColor(Color.WHITE);
    }

    public void add(View v) {
        Intent i = new Intent(this, KaiActivity.class);
        startActivityForResult(i, 1);

    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Toast.makeText(Kai.this,"WOW", Toast.LENGTH_LONG);
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);
        try {  //Just in case the user presses back.
            if (true) {
                ArrayList<Card> rc = (ArrayList<Card>) data.getSerializableExtra("list");
                //  Card card=(Card)data.getParcelableExtra("card");
                for (int j = 0; j < rc.size(); j++) {
                    cards.add(rc.get(j));
                }
                Collections.sort(cards, new Comparator<Card>() {
                    @Override
                    public int compare(Card card1, Card card2) {

                        return card1.getName().compareTo(card2.getName());
                    }
                });
                adapter = new ArrayAdapter<Card>(this,
                        android.R.layout.simple_list_item_1, cards);
                lv.setAdapter(adapter);

                TextView tv = (TextView) findViewById(R.id.size);
                tv.setText("SIZE OF DECK:" + cards.size());
                if (cards.size() > 40) tv.setTextColor(Color.RED);
                else tv.setTextColor(Color.WHITE);
            }
        } catch (Exception e) {

        }

    }

    public void save(View v) {//here 1 sec
        if (!(cards.size() > 40)) {
            int[] dbids = new int[cards.size()];
            for (int i = 0; i < dbids.length; i++) {
                dbids[i] = cards.get(i).getDbId();
            }
            Intent i = new Intent();
            i.putExtra(KEY_RETURN_DBIDS, dbids);
            i.putExtra(KEY_RETURN_DECK_NAME, deckName);
            setResult(Activity.RESULT_OK, i);
            finish();
            Toast.makeText(getApplicationContext(), "Save Successful", Toast.LENGTH_SHORT).show();
            //TODO save the deck
        } else
            Toast.makeText(getApplicationContext(), "Deck has more than 40 cards,can't save", Toast.LENGTH_SHORT).show();
    }
//try it? yup

}
