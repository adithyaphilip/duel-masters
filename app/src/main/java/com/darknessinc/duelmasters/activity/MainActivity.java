package com.darknessinc.duelmasters.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.darknessinc.duelmasters.R;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


    }

    public void kaiButtonClicked(View v) {
        Intent i = new Intent(this, PlayActivity.class);
        startActivity(i);
    }

    public void adiButtonClicked(View v) {
        Intent i = new Intent(this, AdiActivity.class);
        startActivity(i);
    }
}
