package com.darknessinc.duelmasters.activity;

import android.app.Activity;
import android.app.AlertDialog;
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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.darknessinc.duelmasters.R;
import com.darknessinc.duelmasters.cards.Card;
import com.darknessinc.duelmasters.cards.DatabaseCardRetriever;

import java.util.ArrayList;


public class KaiActivity extends Activity {
    ArrayList<Card> cs = new ArrayList<>();
    Card[] allCards;
    ListView lv;
    EditText inputSearch;
    ArrayAdapter<Card> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kai);
        //	Button b=(Button)findViewById(R.id.returnbtn);

        allCards = new DatabaseCardRetriever(this).getAllCards();

        cs = new ArrayList<>();
        lv = (ListView) findViewById(R.id.list_view);
        inputSearch = (EditText) findViewById(R.id.inputSearch);
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, allCards);
        lv.setAdapter(adapter);
        lv.setOnItemClickListener(new OnItemClickListener() {

            public void onItemClick(AdapterView adapterView, View view, int position, long id) {

                Card card = adapter.getItem(position);
                cs.add(card);
                Toast.makeText(getApplicationContext(), "CARD ADDED", Toast.LENGTH_SHORT).show();

            }
        });
        lv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> av, View v, int pos, long id) {
                return onLongListItemClick(pos);
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

    protected boolean onLongListItemClick(int pos) {
        //TODO make it display the correct image instead of Doge
        Card card = adapter.getItem(pos);
        ImageView image = new ImageView(this);
        // image.setImageResource(R.drawable.images);
        Bitmap bitmapOrig = (card.getCardBitmap(222, 307, this));
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

    public void retdeck(View v) {
        Intent i = new Intent();

        i.putExtra("list", cs);
        setResult(Activity.RESULT_OK, i);
        finish();
    }


}
