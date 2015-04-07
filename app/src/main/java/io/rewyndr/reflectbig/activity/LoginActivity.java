package io.rewyndr.reflectbig.activity;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.ContentObserver;
import android.database.Cursor;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationCompat.Builder;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.ParseException;
import com.rewyndr.reflectbig.R;
import io.rewyndr.reflectbig.common.PreferenceConstants;
import io.rewyndr.reflectbig.interfaces.EventService;
import io.rewyndr.reflectbig.interfaces.LoginService;
import io.rewyndr.reflectbig.interfaces.PhotoService;
import io.rewyndr.reflectbig.model.AttendeeStatus;
import io.rewyndr.reflectbig.model.Event;
import io.rewyndr.reflectbig.model.EventStatus;
import io.rewyndr.reflectbig.service.ServiceFactory;
import io.rewyndr.reflectbig.util.Utils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;


public class LoginActivity extends Activity {
    int id = 1;
    private AutoCompleteTextView mEmailView;
    private EditText mPasswordView;
    private Context context = this;
    private String saved;
    private File photoFile;
    private PhotosObserver instUploadObserver = new PhotosObserver();
    private ArrayList<Event> currentEventList = new ArrayList<Event>();
    private NotificationManager mNotifyManager;
    private Builder mBuilder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mEmailView = (AutoCompleteTextView) findViewById(R.id.txt_autoCompleteLogin);
        populateAutoComplete();
        mPasswordView = (EditText) findViewById(R.id.txt_password);
        Button mEmailSignInButton = (Button) findViewById(R.id.email_sign_in_button);
        mEmailSignInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String emailId = mEmailView.getText().toString();
                String password = mPasswordView.getText().toString();
                loginIntoReflectBig(emailId, password);
            }
        });
//        this.getApplicationContext()
//                .getContentResolver()
//                .registerContentObserver(
//                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI, false,
//                        instUploadObserver);
    }

    private void loginIntoReflectBig(String emailId, String password) {
        LoginService service = ServiceFactory.getLoginServiceInstance(context);
        try {
            service.logIn(emailId, password);
            SharedPreferences preferences = getSharedPreferences(PreferenceConstants.PREFERENCE_NAME, MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString(PreferenceConstants.LOGIN_EMAIL_ID, emailId);
            editor.putString(PreferenceConstants.LOGIN_PASSWORD, password);
            editor.commit();
            moveToEventsPage();
        } catch (Exception e) {
            Toast.makeText(context, "Invalid Credentials", Toast.LENGTH_SHORT).show();
        }
    }

    private void moveToEventsPage() {
        Intent intent = new Intent(this, EventsListActivity.class);
        startActivity(intent);
    }

    @Override
    public void onStart() {
        super.onStart();
        SharedPreferences preferences = getSharedPreferences(PreferenceConstants.PREFERENCE_NAME, MODE_PRIVATE);
        String emailId = preferences.getString(PreferenceConstants.LOGIN_EMAIL_ID, "");
        String password = preferences.getString(PreferenceConstants.LOGIN_PASSWORD, "");
        if(emailId.equals("") || emailId.equals("")) {
            // Do nothing. Just load the page
        } else {
            loginIntoReflectBig(emailId, password);
        }
    }

    private void populateAutoComplete() {
        Set<String> emails = new HashSet<String>();
        Pattern emailPattern = Patterns.EMAIL_ADDRESS;
        Account[] accounts = AccountManager.get(this).getAccounts();
        for (Account account : accounts) {
            if (emailPattern.matcher(account.name).matches()) {
                emails.add(account.name);
            }
        }

        List<String> emailList = new ArrayList<String>();
        emailList.addAll(emails);

        ArrayAdapter<String> adapter =
                new ArrayAdapter<String>(LoginActivity.this,
                        android.R.layout.simple_dropdown_item_1line, emailList);
        mEmailView.setAdapter(adapter);
    }

    public void signUp(View view) {
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
    }

    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.login, menu);
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
    }*/

    @Override
    public void onBackPressed() {
        Intent setIntent = new Intent(Intent.ACTION_MAIN);
        setIntent.addCategory(Intent.CATEGORY_HOME);
        setIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(setIntent);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (saved != null) {
//            uploadFile(photoFile);
            Toast.makeText(this, saved, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
//        this.getApplicationContext().getContentResolver()
//                .unregisterContentObserver(instUploadObserver);
//        Log.d("INSTANT", "unregistered content observer");
    }

    private Media readFromMediaStore(Context context, Uri uri, Event event) {
        Date dateStart = event.getStartDate();
        Date dateEnd = event.getEndDate();
        String filter = "date_added<=" + dateEnd.getTime();
        Cursor cursor = context.getContentResolver().query(uri, null, filter,
                null, "date_added DESC");
        Media media = null;
        if (cursor.moveToNext()) {
            int dataColumn = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
            String filePath = cursor.getString(dataColumn);
            int mimeTypeColumn = cursor
                    .getColumnIndexOrThrow(MediaStore.MediaColumns.MIME_TYPE);
            String mimeType = cursor.getString(mimeTypeColumn);
            media = new Media(new File(filePath), mimeType);
            Log.d("startD", dateStart.getTime() + "");
            Log.d("date", media.file.lastModified() + "");
            if (media.file.lastModified() >= dateStart.getTime()) {
                mNotifyManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                mBuilder = new NotificationCompat.Builder(LoginActivity.this);
                mBuilder.setContentTitle(event.getEventName() + " Upload")
                        .setContentText("Upload in progress")
                        .setSmallIcon(R.drawable.ic_launcher);
                new DataSaveProgress(this, media.file, event).execute();
            }

        }
        cursor.close();
        return media;
    }

    private Event getEventLive() {
        Event currentEvent = null;
        List<Event> currentEventList = getEventData();
        for (int i = 0; currentEventList != null && i < currentEventList.size(); i++) {
            if (currentEventList.get(i).getMyStatus().equals(AttendeeStatus.ACCEPTED))
                currentEvent = currentEventList.get(i);
        }
        return currentEvent;
    }

    private void doUpload(File file, Event event) {
        double latitudeV;
        double longitudeV;
        try {
            ExifInterface exifInterface = new ExifInterface(file.getAbsolutePath());
            String latitude = exifInterface.getAttribute(ExifInterface.TAG_GPS_LATITUDE);
            String latitudeRef = exifInterface.getAttribute(ExifInterface.TAG_GPS_LATITUDE_REF);
            String longitude = exifInterface.getAttribute(ExifInterface.TAG_GPS_LONGITUDE);
            String longitudeRef = exifInterface.getAttribute(ExifInterface.TAG_GPS_LONGITUDE_REF);
            if (latitude != null && latitudeRef != null && longitude != null && longitudeRef != null) {
                latitudeV = Utils.convertToDegree(latitude, latitudeRef);
                longitudeV = Utils.convertToDegree(longitude, longitudeRef);
                Log.d("Latitude", Utils.convertToDegree(latitude, latitudeRef).toString());
                Log.d("Longitude", Utils.convertToDegree(longitude, longitudeRef).toString());
            }
            Log.d("TIME", exifInterface.getAttribute(ExifInterface.TAG_DATETIME));
            PhotoService uploadPhoto = ServiceFactory.getPhotoServiceInstance(context);
            uploadPhoto.uploadPhoto(event.getEventId(), file);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private List<Event> getEventData() {
        try {
            EventService eventService = ServiceFactory.getEventServiceInstance(this);
            Map<EventStatus, List<Event>> eventStatusListHashMap = eventService.getEvents();
            return eventStatusListHashMap.get(EventStatus.CURRENT);
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private class PhotosObserver extends ContentObserver {
        public PhotosObserver() {
            super(null);
        }

        @Override
        public void onChange(boolean selfChange) {
            super.onChange(selfChange);
            Event liveEvent = getEventLive();
            if (liveEvent != null) {
                readFromMediaStore(getApplicationContext(),
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI, liveEvent);
                Log.d("INSTANT", "here");
            }
            Log.d("INSTANT", "detected picture");
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

    private class DataSaveProgress extends AsyncTask<String, Void, Boolean> {
        private Activity activity;
        private File file;
        private Event event;

        public DataSaveProgress(Activity activity, File file, Event event) {
            this.activity = activity;
            this.file = file;
            this.event = event;
        }

        @Override
        protected Boolean doInBackground(String... params) {
            doUpload(file, event);
            return true;
        }

        protected void onPreExecute() {
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            mBuilder.setContentText("Upload complete");
            mNotifyManager.notify(id, mBuilder.build());
        }
    }
}
