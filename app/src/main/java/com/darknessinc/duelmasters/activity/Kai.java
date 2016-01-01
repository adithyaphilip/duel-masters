package com.darknessinc.duelmasters.activity;

import java.util.Random;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.darknessinc.duelmasters.R;
import com.darknessinc.duelmasters.cards.Card;
import com.darknessinc.duelmasters.cards.DatabaseCardRetriever;
import com.darknessinc.duelmasters.cards.GameCard;

public class Kai extends Activity {
    private GameCard[] g;
    private String[] op;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.kai);
        Button b = (Button) this.findViewById(R.id.b);
        Button a = (Button) this.findViewById(R.id.op);
        g = new GameCard[100];

        Random r = new Random();
        for (int i = 0; i < 100; i++) {
            int gh = r.nextInt(2);             //Randomly create array
            //Card s=DatabaseCardRetriever.getCard(gh,this);
            //g[i]=new GameCard(i,s,(r.nextInt(5)+1));
        }
        //	Toast.makeText(getApplicationContext(), g[0].getDbCard().getName(), Toast.LENGTH_LONG);

    }

    public void wow(View v) {
        Intent i = new Intent(this, CardChooserActivity.class);
        i.putExtra("array", g);  //pass array
        startActivityForResult(i, 2);

    }

    public void choose(View v) {
        op = new String[2];
        op[0] = "Tap";
        op[1] = "Attack";
        Bundle b = new Bundle();
        b.putStringArray("options", op);
        Intent i = new Intent(this, CardOptionChooserActivity.class);
        i.putExtras(b);
        //i.putExtra("card",g[0]);
        startActivityForResult(i, 1);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Toast.makeText(Kai.this,"WOW", Toast.LENGTH_LONG);
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == 1) {
            TextView tv = (TextView) findViewById(R.id.textView1);
            int in = data.getIntExtra("position", 0);
            tv.setText("Option returned is:" + op[in - 1]);
        }
        // check if the request code is same as what is passed  here it is 2
        if (resultCode == 2)//simply :P
        {
            // fetch the message String
            GameCard g = (GameCard) data.getParcelableExtra("card");
            String i = g.getDbCard().getType();
            TextView tv = (TextView) findViewById(R.id.textView1);
            // Set the message string in textView
            tv.setText("Returned Game card ,The type is " + (i));

        }

    }

}
