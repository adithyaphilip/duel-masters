package com.darknessinc.duelmasters.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;

import com.darknessinc.duelmasters.R;

public class PlayActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);
        Button decks = (Button) findViewById(R.id.decksbtn);
        Button play = (Button) findViewById(R.id.playbtn);
    }


    public void decks(View v) {
        Intent i = new Intent(this, Decks.class);
        startActivity(i);
    }

    public void play(View v) {
        Intent i = new Intent(this, DeckChooser.class);
        startActivity(i);
    }
}
