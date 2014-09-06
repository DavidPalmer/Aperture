package com.rewyndr.reflectbig.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.ParseUser;
import com.rewyndr.reflectbig.R;
import com.rewyndr.reflectbig.interfaces.EventService;
import com.rewyndr.reflectbig.model.AttendeeStatus;
import com.rewyndr.reflectbig.model.Event;
import com.rewyndr.reflectbig.model.EventStatus;
import com.rewyndr.reflectbig.service.ServiceFactory;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;


public class EventDetailActivity extends Activity {
    private String logClass = this.getClass().getName();
    Event event = null;
    private List<String> listOfAttendes = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_detail_layout);
        event = (Event) getIntent().getSerializableExtra("event");
        EventService fetchEventAttendees = ServiceFactory.getEventServiceInstance(this);
        try {
            listOfAttendes = fetchEventAttendees.getAttendees(event.getEventId());
        } catch (Exception e) {
            e.printStackTrace();
        }
        TableLayout tl = (TableLayout) findViewById(R.id.table);
        TableRow decisionRow = (TableRow) findViewById(R.id.decisionRow);
        if(event.getStatus().equals(EventStatus.PAST) || (!event.getMyStatus().equals(AttendeeStatus.NOT_RESPONDED))) {
            tl.removeView(decisionRow);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        feedEventDetails();
    }

    private void feedEventDetails() {
        Event thisEvent = event;
        TextView text_eventName = (TextView) findViewById(R.id.text_event_name);
        TextView text_eventDesc = (TextView) findViewById(R.id.text_event_description);
        TextView text_invited_by = (TextView) findViewById(R.id.text_invited_by);
        TextView text_startDate = (TextView) findViewById(R.id.text_startDate);
        TextView text_endDate = (TextView) findViewById(R.id.text_endDate);
        TextView text_where = (TextView) findViewById(R.id.text_where);
        TextView text_status = (TextView) findViewById(R.id.text_status);
        TextView text_attendees = (TextView) findViewById(R.id.text_attendees);

        text_eventDesc.setText(thisEvent.getEventDesc());
        text_eventName.setText(thisEvent.getEventName());
        text_invited_by.setText(thisEvent.getInvitedBy());
        text_startDate.setText(thisEvent.getStartDate().toString());
        text_endDate.setText(thisEvent.getEndDate().toString());
        text_where.setText(thisEvent.getLocation());
        text_status.setText(thisEvent.getMyStatus().toString());
        text_attendees.setText(thisEvent.getAttendeesCount() + " people");
    }

    public void attendeeList(View view) {
        Intent intent = new Intent(this, AttendeeListActivity.class);
        intent.putStringArrayListExtra("Attendees", (ArrayList<String>) listOfAttendes);
        startActivity(intent);
    }

    public void onClickAccept(View view) {
        Toast.makeText(this, "Accepting", Toast.LENGTH_SHORT).show();
    }

    public void onClickDecline(View view) {
        Toast.makeText(this, "Rejecting", Toast.LENGTH_SHORT).show();
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
