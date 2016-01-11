package com.darknessinc.duelmasters.cards;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Context;
import android.util.Log;

import com.darknessinc.duelmasters.R;
import com.darknessinc.duelmasters.fileutils.FilePathManager;

public class DatabaseCardRetriever {
    private Card[] mAllCards;
    Context mContext;

    public Card getCard(int dbid) {
        for (Card c : mAllCards) {
            if (c.getDbId() == dbid)
                return c.clone();
        }
        return null;
    }

    public Card[] getAllCards() {
        return mAllCards;
    }

    public ArrayList<Card> getCards(int dbids[]) {
        ArrayList<Card> cards = new ArrayList<>();
        for(int dbid: dbids) {
            for(Card card: mAllCards) {
                if (card.getDbId() == dbid) {
                    cards.add(card.clone());
                }
            }
        }
        return cards;
    }

    public DatabaseCardRetriever(Context ctx) {
        mContext = ctx;
        ;
        StringBuilder contents = new StringBuilder();
        String sep = System.getProperty("line.separator");

        try {
            InputStream is = mContext.getResources().openRawResource(R.raw.dmjson);

            BufferedReader input = new BufferedReader(new InputStreamReader(is), 1024 * 8);
            try {
                String line;
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
        Card[] allCards = null;
        try {
            JSONObject jsonObj = new JSONObject(s);
            JSONArray cards = jsonObj.getJSONArray("cards");
            allCards = new Card[cards.length()];
            for (int i = 0; i < cards.length(); i++) {
                JSONObject c = cards.getJSONObject(i);
                int id = Integer.parseInt(c.getString("dbid"));
                String name = c.getString("name");
                String imagepath = c.getString("img_name");
                String type = c.getString("type");
                String text = "";
                allCards[i] = new Card(id, name, imagepath, type, text);
            }
        } catch (Exception e) {
            Log.d("json", s);
        }
        mAllCards = allCards;
    }
}
