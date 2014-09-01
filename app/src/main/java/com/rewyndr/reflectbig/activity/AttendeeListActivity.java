package com.rewyndr.reflectbig.activity;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.rewyndr.reflectbig.R;

import java.util.List;


public class AttendeeListActivity extends Activity {
    List<String> attendees;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendee_list);
        attendees = getIntent().getStringArrayListExtra("Attendees");
        ListView view = (ListView) findViewById(R.id.list);
        final ArrayAdapter adapter = new ArrayAdapter(this,
                android.R.layout.simple_list_item_1, attendees);
        view.setAdapter(adapter);

    }
}
