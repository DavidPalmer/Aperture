package io.rewyndr.reflectbig.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.applidium.headerlistview.HeaderListView;
import com.parse.ParseException;
import com.rewyndr.reflectbig.R;
import io.rewyndr.reflectbig.adapter.EventListAdapter;
import io.rewyndr.reflectbig.common.PreferenceConstants;
import io.rewyndr.reflectbig.interfaces.EventService;
import io.rewyndr.reflectbig.interfaces.PhotoService;
import io.rewyndr.reflectbig.model.Event;
import io.rewyndr.reflectbig.model.EventStatus;
import io.rewyndr.reflectbig.service.ServiceFactory;

import java.io.File;
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
    private String saved;
    private File photoFile;
    private PhotosObserver instUploadObserver = new PhotosObserver();

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
//        this.getApplicationContext()
//                .getContentResolver()
//                .registerContentObserver(
//                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI, false,
//                        instUploadObserver);
    }

    private void getEventData() {
        try {
            EventService eventService = ServiceFactory.getEventServiceInstance(this);
            Map<EventStatus, List<Event>> eventStatusListHashMap = eventService.getEvents();
            pastEventList.clear();
            currentEventList.clear();
            upcomingEventList.clear();
            pastEventList.addAll(eventStatusListHashMap.get(EventStatus.PAST));
            currentEventList.addAll(eventStatusListHashMap.get(EventStatus.CURRENT));
            upcomingEventList.addAll(eventStatusListHashMap.get(EventStatus.UPCOMING));
            Log.d("test", pastEventList.size() + " " + currentEventList.size() + " " + upcomingEventList.size());
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
        if (id == R.id.user_refresh) {
            getEventData();
            eventListAdapter.notifyDataSetChanged();
            Log.d("err", eventListAdapter.numberOfRows(0) + " " + eventListAdapter.numberOfRows(1) + " " + eventListAdapter.numberOfRows(2) + " ");
        }
        if (id == R.id.action_settings) {
            Intent intent = new Intent(getApplicationContext(), SettingsActivity.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        Intent setIntent = new Intent(Intent.ACTION_MAIN);
        setIntent.addCategory(Intent.CATEGORY_HOME);
        setIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(setIntent);
    }

//    @Override
//    public void onResume() {
//        super.onResume();
//        if (saved != null) {
//            uploadFile(photoFile);
//            Toast.makeText(this, saved, Toast.LENGTH_SHORT).show();
//        }
//    }
//    @Override
//    public void onDestroy() {
//        super.onDestroy();
//        this.getApplicationContext().getContentResolver()
//                .unregisterContentObserver(instUploadObserver);
//        Log.d("INSTANT", "unregistered content observer");
//    }

    private boolean uploadFile(File f) {
        PhotoService uploadPhoto = ServiceFactory.getPhotoServiceInstance(getApplicationContext());
        try {
            uploadPhoto.uploadPhoto(pastEventList.get(0).getEventId(), f);
            f.delete();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;

    }

    private Media readFromMediaStore(Context context, Uri uri) {
        Cursor cursor = context.getContentResolver().query(uri, null, null,
                null, "date_added DESC");
        Media media = null;
        if (cursor.moveToNext()) {
            int dataColumn = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
            String filePath = cursor.getString(dataColumn);
            int mimeTypeColumn = cursor
                    .getColumnIndexOrThrow(MediaStore.MediaColumns.MIME_TYPE);
            String mimeType = cursor.getString(mimeTypeColumn);
            media = new Media(new File(filePath), mimeType);
        }
        cursor.close();
        return media;
    }

    private class PhotosObserver extends ContentObserver {
        public PhotosObserver() {
            super(null);
        }

        @Override
        public void onChange(boolean selfChange) {
            super.onChange(selfChange);
            Media media = readFromMediaStore(getApplicationContext(),
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            saved = "I detected " + media.file.getName();
            photoFile = media.file;
            Log.d("INSTANT", "detected picture");
            Log.d("name", saved);
        }
    }

    private class Media {
        private File file;
        @SuppressWarnings("unused")
        private String type;

        public Media(File file, String type) {
            this.file = file;
            this.type = type;
        }
    }
}