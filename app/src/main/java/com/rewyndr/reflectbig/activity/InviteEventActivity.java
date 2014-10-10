package com.rewyndr.reflectbig.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.rewyndr.reflectbig.R;
import com.rewyndr.reflectbig.interfaces.EventService;
import com.rewyndr.reflectbig.service.ServiceFactory;

import java.util.Arrays;
import java.util.List;


public class InviteEventActivity extends Activity {
    String eventId = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invite_event);
        Intent intent = getIntent();
        eventId = (String) intent.getSerializableExtra("eventId");
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.invite_event, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void inviteEmail(View v) {
        final EditText emails = (EditText) findViewById(R.id.email_invite);
        List<String> emailsList = Arrays.asList(emails.getText().toString().replace(" ", "").split(";"));
        EventService eventService = ServiceFactory.getEventServiceInstance(this);
        try {
            eventService.inviteParticipants(eventId, emailsList);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Intent intent = new Intent(this, EventsListActivity.class);
        startActivity(intent);
    }
}
