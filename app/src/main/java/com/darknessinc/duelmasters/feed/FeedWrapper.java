package com.darknessinc.duelmasters.feed;

import java.util.ArrayList;

import android.content.Context;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class FeedWrapper {
    private ListView lv;
    private Context ctx;
    ArrayAdapter<String> adapter;
    ArrayList<String> messages = new ArrayList<String>();

    public FeedWrapper(Context c, ListView lv) {   //Sets context and listView
        this.lv = lv;
        ctx = c;
        setAdapter();
    }

    public void addMessage(String message) {  //Method to add new message
        messages.add(message);
        setAdapter();
    }

    public void setAdapter() {               //Used to set the adapter
        adapter = new ArrayAdapter<String>(ctx,
                android.R.layout.simple_list_item_1, messages);
        lv.setAdapter(adapter);
        lv.setSelection(lv.getAdapter().getCount() - 1);
    }
}
