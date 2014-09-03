package com.rewyndr.reflectbig.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.ParseUser;
import com.rewyndr.reflectbig.R;
import com.rewyndr.reflectbig.model.Event;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;


public class EventDetailActivity extends Activity {
    private String logClass = this.getClass().getName();
    private String eventId = "";
    private ArrayList<String> listOfAttendes = new ArrayList<String>();
    public EventDetailActivity() {
    }

    public EventDetailActivity(String eventId) {
        this.eventId = eventId;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_detail_layout);
    }

    @Override
    protected void onStart() {
        super.onStart();
        feedEventDetails();
    }

    private void feedEventDetails() {
        try {
            Event thisEvent = getEventDetails();
            TextView text_eventName = (TextView) findViewById(R.id.text_event_name);
            TextView text_eventDesc = (TextView) findViewById(R.id.text_event_description);
            TextView text_invited_by = (TextView) findViewById(R.id.text_invited_by);
            TextView text_startDate = (TextView) findViewById(R.id.text_startDate);
            TextView text_endDate = (TextView) findViewById(R.id.text_endDate);
            TextView text_where = (TextView) findViewById(R.id.text_where);
            TextView text_status = (TextView) findViewById(R.id.text_status);
            TextView text_attendees = (TextView) findViewById(R.id.text_attendees);

            text_eventDesc.setText(thisEvent.getEvent_description());
            text_eventName.setText(thisEvent.getEvent_name());
            text_invited_by.setText(thisEvent.getEvent_invited_by());
            text_startDate.setText(thisEvent.getEvent_startDate());
            text_endDate.setText(thisEvent.getEvent_endDate());
            text_where.setText(thisEvent.getEvent_where());
            String myStatus;
            if (thisEvent.getEvent_myStatus() == null) {
                myStatus = "Not responded";
            } else {
                myStatus = "Going/Not Going";
            }
            text_status.setText(myStatus);
            text_attendees.setText(thisEvent.getEvent_attendees().size() + " people");
        } catch (ParseException e) {
            Log.d(logClass, "");
        }
    }

    public void attendeeList(View view) {
        Intent intent = new Intent(this, AttendeeListActivity.class);
        intent.putStringArrayListExtra("Attendees", listOfAttendes);
        startActivity(intent);
    }

    public void onClickAccept(View view) {
        Toast.makeText(this, "Accepting", Toast.LENGTH_SHORT).show();
    }

    public void onClickDecline(View view) {
        Toast.makeText(this, "Rejecting", Toast.LENGTH_SHORT).show();
    }

    private Event getEventDetails() throws ParseException {
        ArrayList<String> a = new ArrayList<String>();
        a.add("R");
        a.add("R");
        a.add("R");
        listOfAttendes = a;
        Log.d(logClass, String.valueOf(ParseUser.getCurrentUser()));
        Date d = new Date();
        return new Event("1", "KennyWood", "KennyWood - Amazing Fun land", "Raja",d.toString(),d.toString() ,"Test Test Test Test Test Test Test Test Test Test Test Test Test Test ", true, true, a);
    }

    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.event_detail_layout, menu);
        return true;
    }*/

    /*@Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }*/
}
