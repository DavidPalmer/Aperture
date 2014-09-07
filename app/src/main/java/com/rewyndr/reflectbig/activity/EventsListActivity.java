package com.rewyndr.reflectbig.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.applidium.headerlistview.HeaderListView;
import com.parse.ParseException;
import com.rewyndr.reflectbig.R;
import com.rewyndr.reflectbig.adapter.EventListAdapter;
import com.rewyndr.reflectbig.common.PreferenceConstants;
import com.rewyndr.reflectbig.interfaces.EventService;
import com.rewyndr.reflectbig.model.Event;
import com.rewyndr.reflectbig.model.EventStatus;
import com.rewyndr.reflectbig.service.ServiceFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class EventsListActivity extends Activity {
    EventListAdapter eventListAdapter;
    HeaderListView headerListView;
    EventService eventService;
    private ArrayList<Event> pastEventList = new ArrayList<Event>();
    private ArrayList<Event> currentEventList = new ArrayList<Event>();
    private ArrayList<Event> upcomingEventList = new ArrayList<Event>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_events_list);
        eventService = ServiceFactory.getEventServiceInstance(this);
        headerListView = new HeaderListView(this);
        getEventData();
        eventListAdapter = new EventListAdapter(this, pastEventList, currentEventList, upcomingEventList);
        headerListView.setAdapter(eventListAdapter);
//        swipeRefreshLayout = (SwipeRefreshLayout)getLayoutInflater().inflate(R.layout.activity_events_list,null).findViewById(R.id.swipe_container);
//        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
//            @Override
//            public void onRefresh() {
//                new Handler().postDelayed(new Runnable() {
//                    @Override public void run() {
//                        swipeRefreshLayout.setRefreshing(false);
//                        getEventData();
//                    }
//                }, 5000);
//            }
//        });
        setContentView(headerListView);
    }

    private void getEventData() {
        try {
            EventService eventService = ServiceFactory.getEventServiceInstance(this);
            Map<EventStatus, List<Event>> eventStatusListHashMap = eventService.getEvents();
            pastEventList = (ArrayList<Event>) eventStatusListHashMap.get(EventStatus.PAST);
            currentEventList = (ArrayList<Event>) eventStatusListHashMap.get(EventStatus.CURRENT);
            upcomingEventList = (ArrayList<Event>) eventStatusListHashMap.get(EventStatus.UPCOMING);
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
//        eventListAdapter.notifyDataSetChanged();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.events_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.user_logout) {
            SharedPreferences preferences = getSharedPreferences(PreferenceConstants.PREFERENCE_NAME, MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString(PreferenceConstants.LOGIN_EMAIL_ID, "");
            editor.putString(PreferenceConstants.LOGIN_PASSWORD, "");
            editor.commit();
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        } if (id == R.id.addEvent) {
            Intent intent = new Intent(this, CreateEventActivity.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }
}