package com.darknessinc.duelmasters.activity;

import java.io.File;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.StringTokenizer;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.darknessinc.duelmasters.R;
import com.darknessinc.duelmasters.decks.DeckBuilderActivity;
import com.darknessinc.duelmasters.fileutils.FilePathManager;
//ok. what next? its working?no I wanted to write new comment
//continue later?
//or I'll push? push, but i dont seem to be able to connect oto bitbuckwt one sec though lemme logcat

public class Decks extends Activity {
    private static final int ACTIVITY_DECK_BUILDER = 1;
    ListView lv;
    ArrayList<String> decks = new ArrayList<String>();
    ArrayAdapter<String> adapter;
    private int user_option = -1;            //Adi, this is used to store the position of item in list.

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_decks);
        decks = getDeckNames();//what else do we have to change?
        //well, upon clicking new. It has to go to deckbuilder and then return ids from there
        lv = (ListView) findViewById(R.id.list_view);
        adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, decks);
        lv.setAdapter(adapter);
        //readdecklist(getApplicationContext());
        lv.setOnItemClickListener(new OnItemClickListener() {

            public void onItemClick(AdapterView adapterView, View view, int position, long id) {
                //TODO opens it


                String selected_deck = decks.get(position);


                //okay? yup
                //yeah.does not get name
                gotodeck(selected_deck);
                //correct? yu
                //what's wrong? //how to do? :P
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

    protected boolean onLongListItemClick(View v, int pos, long id) {
        user_option = pos;
        //TODO make it display the correct image instead of Doge
        //To check if he is sure
        AlertDialog.Builder builder =
                new AlertDialog.Builder(this).
                        setMessage("Are You Sure you want to delete this deck?").
                        setPositiveButton("OK", new OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                sure();
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // if this button is clicked, just close
                                // the dialog box and do nothing

                            }
                        });
        builder.create().show();


        return true;
    }

    public void sure() {
        int pos = user_option;
        String selected_deck = decks.get(pos);
        File f = new File(FilePathManager.getDeckDirectory(this).getPath() + "/" + selected_deck);
        f.delete();
        decks.remove(selected_deck);
        adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, decks);
        lv.setAdapter(adapter);
        Toast.makeText(getApplicationContext(), "DELETED DECK", Toast.LENGTH_SHORT).show();

    }


    public void gotodeck(String deckname) {
        Toast.makeText(this, "selected deck: " + deckname, Toast.LENGTH_SHORT).show();
        Intent i = new Intent(this, DeckBuilderActivity.class);

        i.putExtra(DeckBuilderActivity.KEY_DECK_NAME, deckname);//try now it is getting name
        startActivityForResult(i, ACTIVITY_DECK_BUILDER);

    }

    public int[] getDbIdsFromFile(String filename) { //works wait

        File f = new File(FilePathManager.getDeckDirectory(this).getPath() + "/" + filename);

        try {
            Scanner sc = new Scanner(f);
            String s = sc.nextLine();
            // Toast.makeText(getApplicationContext(), "SUCCESS: "+s, Toast.LENGTH_LONG).show();
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
                // Toast.makeText(getApplicationContext(), "WORKS 2", Toast.LENGTH_LONG).show();

                int m = (Integer.valueOf(st.nextToken())).intValue();
                //Toast.makeText(getApplicationContext(), "WORKS 3", Toast.LENGTH_LONG).show();
                dbid.add(m);


            }
            int[] temp = new int[40];
            for (i = 0; i < dbid.size(); i++) {
                temp[i] = (int) dbid.get(i);

            }


            return temp;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public ArrayList<String> getDeckNames() {
        ArrayList<String> al = new ArrayList<String>();
        for (File f : getDeckFiles()) {
            //cx		Toast.makeText(this, f.getName(),Toast.LENGTH_SHORT).show();//correct it no

            al.add(f.getName());
        }
        return al;
    }

    public File[] getDeckFiles() {

        //	Toast.makeText(this,FilePathManager.getDeckDirectory(this).getPath(),Toast.LENGTH_SHORT).show();
        return FilePathManager.getDeckDirectory(this).listFiles();
    }

    public void writeNewDeck(String name, int[] dbids) {
        try {
            File deck = new File(FilePathManager.getDeckDirectory(this) + "/" + name);
            //Toast.makeText(this,deck.getPath(),Toast.LENGTH_SHORT).show();
            PrintWriter pw = new PrintWriter(deck);
            StringBuilder sb = new StringBuilder("" + dbids[0]);
            for (int i = 1; i < dbids.length; i++) {
                sb.append("," + dbids[i]);
            }
            pw.print(sb.toString());

            pw.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void create(View v) { // call that here
        Intent i = new Intent(this, DeckBuilderActivity.class);
        EditText myText = (EditText) findViewById(R.id.myText);
        String name = myText.getText().toString();
        myText.setText("");
        if (name.length() > 0) {
            i.putExtra(DeckBuilderActivity.KEY_DECK_NAME, name);
            startActivityForResult(i, ACTIVITY_DECK_BUILDER);
            decks.add(name);
        } else
            Toast.makeText(getApplicationContext(), "Please enter deck name", Toast.LENGTH_SHORT).show();
    }

    public void randomdeck(View v) {
        /*
		int[] b=new int[40];
		Random r=new Random();
		for(int i=0;i<40;i++){
			b[i]=r.nextInt(800);
		}
		Toast.makeText(getApplicationContext(), "wow", Toast.LENGTH_LONG).show();
		EditText	myText =(EditText)findViewById(R.id.myText);
		String name=myText.getText().toString();
		myText.setText("");
		if(name.length()>0){
			writeNewDeck(name,b);
		}
		decks.add(name);
		*/
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == Activity.RESULT_OK) {
                String name = data.getStringExtra(DeckBuilderActivity.KEY_RETURN_DECK_NAME);
                writeNewDeck(name, data.getIntArrayExtra("dbids"));

            }
        }
    }


}
