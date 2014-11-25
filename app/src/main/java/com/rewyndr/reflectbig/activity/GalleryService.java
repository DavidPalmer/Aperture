package com.rewyndr.reflectbig.activity;

import android.app.NotificationManager;
import android.app.Service;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.ContentObserver;
import android.database.Cursor;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.IBinder;
import android.provider.MediaStore;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.parse.ParseException;
import com.rewyndr.reflectbig.R;
import com.rewyndr.reflectbig.interfaces.EventService;
import com.rewyndr.reflectbig.interfaces.PhotoService;
import com.rewyndr.reflectbig.model.AttendeeStatus;
import com.rewyndr.reflectbig.model.Event;
import com.rewyndr.reflectbig.model.EventStatus;
import com.rewyndr.reflectbig.service.ServiceFactory;
import com.rewyndr.reflectbig.util.Utils;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by dil on 11/24/14.
 */
public class GalleryService extends Service {
    public static final String mAction = "PhotoService";
    int id = 1;
    ContentResolver content;
    ContentResolver contentResolver;
    private NotificationManager mNotifyManager;
    private NotificationCompat.Builder mBuilder;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i("Status", "Service Start");
        contentResolver = this.getContentResolver();
        contentResolver.registerContentObserver(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, true, new PhotoObserver(new Handler()));
        Event event = getEventLive();
        if (event != null) {
            mNotifyManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            mBuilder = new NotificationCompat.Builder(getApplicationContext());
            mBuilder.setContentTitle("ReflectBig Event")
                    .setContentText("Photos are being uploaded")
                    .setSmallIcon(R.drawable.ic_launcher);
            mNotifyManager.notify(id, mBuilder.build());
        } else {
            mBuilder.setOngoing(false);
            mNotifyManager.cancelAll();
        }
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onRebind(Intent intent) {
        Event event = getEventLive();
        if (event == null) {
            mBuilder.setOngoing(false);
            mNotifyManager.cancelAll();
        }
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
                new DataSaveProgress(media.file, event).execute();
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
            if (exifInterface.getAttribute("isReflect") != null) {
                Log.d("uploaded", "true");
                return;
            }
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
            PhotoService uploadPhoto = ServiceFactory.getPhotoServiceInstance(getApplicationContext());
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

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i("Status", "Service Destroy");
    }

    @Override
    public IBinder onBind(Intent intent) {
        Log.i("Status", "Service Bind");
        return null;
    }

    /**
     * Observer for fetching new photos
     */
    class PhotoObserver extends ContentObserver {

        public PhotoObserver(Handler handler) {
            super(handler);
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
        private File file;
        private Event event;

        public DataSaveProgress(File file, Event event) {
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
            try {
                ExifInterface exifInterface = new ExifInterface(file.getAbsolutePath());
                exifInterface.setAttribute("isReflect", "yes");
                exifInterface.saveAttributes();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
