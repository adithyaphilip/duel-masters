package com.darknessinc.duelmasters.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;

import com.darknessinc.duelmasters.R;
import com.darknessinc.duelmasters.cards.GameCard;

/**
 * expects KEY_OPTIONS extra which is a String array populating options
 * expects KEY_CARD extra which is the GameCard for which options are being displayed
 * returns chosen index in KEY_OPTIONS array as int in KEY_RETURN to calling activity
 * IMP:- Remember that
 *
 * @author USER
 *         TODO (Ignore) Return ACTIVITY_CANCELLED as status code if no supplied option is chosen. Alternatively position can be -1.
 *         TODO Return passed Card also, as onActivityResult in calling Activity will have no clue what was passed by the function
 *         which called CardOptionChooserActivity
 */
public class CardOptionChooserActivity extends ListActivity {
    /*
     * final Strings are used for inter-activity communication as now
     * a) Anyone calling would just need to reference KEY_OPTIONS to know what key to pass instead
     * 	of having to check the code or documentation
     * b) Reduces spelling mistakes while entering keys both in calling this activity
     *  and returning from this activity, if constants are used
     * c) Allows easy refactoring i.e if for some reason you want to change the key name from "options"
     * 	(like if you get another parameter which would better suit that key name) to something else you only need
     * 	to change it in one place
     * Mainly b) and then a). To a lesser extent c)
     */
    /*
	 * Why we need to have the _RETURN_ part instead of re-using keys
	 * a) Just with a glance, someone looking at the code can figure out what the activity requires 
	 * 	and what it doesn't, and what it returns, without having to go through all of it
	 * b) When writing code, you just need to say .KEY_RETURN and all values returned by it appear via autocomplete
	 * c) Refactoring? A little less than for using constant Strings but still allows a nice logical dissociation between
	 * 	the two
	 */
    public final static String KEY_OPTIONS = "options";
    public final static String KEY_CARD = "card";
    public final static String KEY_RETURN_OPTION = "option";
    public final static String KEY_RETURN_CARD = "card";
    public final static String KEY_RETURN_CARD_VIEWED = "viewed";
    public final static String KEY_RETURN_POSITION = "position";
    private String[] options;//not options array passed to it, includes View option at first index
    private GameCard mGameCard;
    boolean viewed = false;
    Intent returnIntent = new Intent();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_option_chooser);

        Bundle b = this.getIntent().getExtras();
        String[] temp = b.getStringArray(KEY_OPTIONS);
        mGameCard = (GameCard) getIntent().getParcelableExtra(KEY_CARD);
        options = new String[temp.length + 1];
        for (int i = 0; i < temp.length; i++) {
            options[i + 1] = temp[i];
        }
        options[0] = "View Card";
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, options);
        setListAdapter(adapter);

        returnIntent.putExtra(KEY_RETURN_CARD, mGameCard);
        setResult(Activity.RESULT_CANCELED, returnIntent);//default return value
    }

    protected void onListItemClick(ListView l, View v, int position, long id) {
        //String g=(String)getListAdapter().getItem(position); //KAI necessary?
        if (position == 0) {
            viewed = true;
            returnIntent.putExtra(KEY_RETURN_CARD_VIEWED, viewed);
            setResult(Activity.RESULT_CANCELED, returnIntent);
            try {
                ImageView image = new ImageView(this);
                Bitmap bitmapOrig = (mGameCard.getDbCard().getCardBitmap(222, 307, this));
                Bitmap bitmapScaled = Bitmap.createScaledBitmap(bitmapOrig, 723, 1000, true);
                image.setImageBitmap(bitmapScaled);

                AlertDialog.Builder builder =
                        new AlertDialog.Builder(this).

                                setPositiveButton("OK", new OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                }).
                                setView(image);


                AlertDialog a = builder.create();

                a.show();
            } catch (Exception e) {
                ImageView im = new ImageView(this);
                im.setImageResource(R.drawable.dmcardback);
                AlertDialog.Builder builder =
                        new AlertDialog.Builder(this).
                                setMessage("NO IMAGE AVAILABLE").
                                setPositiveButton("OK", new OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                })
                                .setView(im);
                AlertDialog a = builder.create();
                a.show();
            }
        } else {

            Intent i = new Intent();//KAI no need for parameters in the returning intent
            i.putExtra(KEY_RETURN_POSITION, position - 1);//KAI it's position-1 to resolve to the correct index right?
            i.putExtra(KEY_RETURN_CARD, mGameCard);
            i.putExtra(KEY_RETURN_OPTION, options[position]);
            setResult(Activity.RESULT_OK, i);//KAI RESULT_OK is the standard return if activity was successful
            finish();
            //KAI in case activity does not close by itself, consider super.onBackPressed()
        }
    }

}
