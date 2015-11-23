package com.darknessinc.duelmasters.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;

import com.darknessinc.duelmasters.R;
import com.darknessinc.duelmasters.cards.GameCard;

public class CardChooserActivity extends ListActivity {
	public static final String KEY_GAME_CARD_ARRAY = "array";
	public static final String KEY_RETURN_CARD = "card";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_card_chooser);
		ListView lv=getListView();
		Parcelable[] allParcelables = getIntent().getExtras().getParcelableArray(KEY_GAME_CARD_ARRAY);
		GameCard[] allCards = new GameCard[allParcelables.length];
		for (int i =0;i< allParcelables.length; i++) {
		    allCards[i] = (GameCard)allParcelables[i];
		}
	
		ArrayAdapter<GameCard> adapter = new ArrayAdapter<GameCard>(this,
		        android.R.layout.simple_list_item_1, allCards);
		    setListAdapter(adapter);
		
		    lv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
		        @Override
		        public boolean onItemLongClick(AdapterView<?> av, View v, int pos, long id) {
		            return onLongListItemClick(v,pos,id);
		        }
		    });

	}
	protected boolean onLongListItemClick(View v, int pos, long id) {
	
		ImageView image = new ImageView(this);
        image.setImageResource(R.drawable.images);

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

	 protected void onListItemClick(ListView l, View v, int position, long id) {
		 GameCard g=(GameCard)getListAdapter().getItem(position);
		 Intent i= new Intent(this,Kai.class);
			i.putExtra(KEY_RETURN_CARD,g );
			setResult(Activity.RESULT_OK,i);
			finish();
		  }
	
}
