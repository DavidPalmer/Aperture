package io.rewyndr.reflectbig.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;
import android.media.ExifInterface;

import com.rewyndr.reflectbig.R;
import io.rewyndr.reflectbig.adapter.ImageAdapter;
import io.rewyndr.reflectbig.common.PhotoType;
import io.rewyndr.reflectbig.interfaces.PhotoService;
import io.rewyndr.reflectbig.model.Event;
import io.rewyndr.reflectbig.model.EventStatus;
import io.rewyndr.reflectbig.model.Photo;
import io.rewyndr.reflectbig.service.RotateImage;
import io.rewyndr.reflectbig.service.ServiceFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Dileeshvar on 7/16/2014.
 */
public class PhotoMultiViewActivity extends Activity {
    public static final int MEDIA_TYPE_IMAGE = 1;
    private static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 100;
    private static final int BUFFER = 100;
    private final String CURRENT_CLASS_LOG = this.getClass().getName();
    private final Context context = this;
    ImageAdapter imageAdapter;
    ArrayList<Photo> photos = new ArrayList<Photo>();
    GridView gridViewImage;
    private Uri fileUri;
    private int start = 0;
    private int size = 0;
    private Event event;

    /**
     * Create a File for saving an image or video
     */
    private static File getOutputMediaFile(int type){
        // To be safe, you should check that the SDCard is mounted
        // using Environment.getExternalStorageState() before doing this.

        // File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
        // Environment.DIRECTORY_PICTURES), "reflectBig");
        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), "reflectBig");
        // This location works best if you want the created images to be shared
        // between applications and persist after your app has been uninstalled.

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d("MyCameraApp", "failed to create directory");
                return null;
            }
        }

        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File mediaFile;
        if (type == MEDIA_TYPE_IMAGE) {

            String imageURI = mediaStorageDir.getPath() + File.separator +
                    "IMG_" + timeStamp + ".jpg";


            mediaFile = new File(mediaStorageDir.getPath() + File.separator +
                    "IMG_" + timeStamp + ".jpg");
        } else {
            return null;
        }

        return mediaFile;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_multi_view);
        displayPhotos();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.photo_multi_view, menu);
        if (event.getStatus().equals(EventStatus.CURRENT)) {
            menu.findItem(R.id.camera).setVisible(true);
        }
        if (event.getStatus().equals(EventStatus.CURRENT)) {
            menu.findItem(R.id.photo_refresh).setVisible(true);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.camera) {
        //            if(event.getEndDate().equals(new Date())) {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            fileUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE);

            intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
            startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
//            }
//            else{
//                item.setVisible(false);
//            }
        }
        if (id == R.id.event_details) {
            Intent intent = new Intent(this, EventDetailActivity.class);
            intent.putExtra("event", event);
            startActivity(intent);
        }
        if (id == R.id.photo_refresh) {
            addPhotoList();
            imageAdapter.notifyDataSetChanged();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                // Image captured and saved to fileUri specified in the Intent
//                Log.i("keyset", data.getExtras().keySet());
                Toast.makeText(this, "Image saved to reflectBig", Toast.LENGTH_SHORT).show();
                new FetchFiles().execute();
            } else if (resultCode == RESULT_CANCELED) {
                // User cancelled the image capture
            } else {
                // Image capture failed, advise user
            }
        }
    }

    private void displayPhotos() {
        try {
            Intent intent = getIntent();
            event = (Event) intent.getSerializableExtra("event");
            setTitle(event.getEventName());
//            addPhotoList();
            new DataSaveProgress(this).execute();
            TextView n = new TextView(context);
            imageAdapter = new ImageAdapter(this, photos);
            gridViewImage = (GridView) findViewById(R.id.grid_view1);
            gridViewImage.setAdapter(imageAdapter);
            gridViewImage.setOnScrollListener(new AbsListView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(AbsListView view, int scrollState) {
                }

                @Override
                public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                    if (visibleItemCount + firstVisibleItem == totalItemCount && size > totalItemCount) {
                        addPhotoList();
                        imageAdapter.notifyDataSetChanged();
                    }
                }
            });
            gridViewImage.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View v,
                                        int position, long id) {
                    // Sending image id to FullScreenActivity
                    Intent i = new Intent(getApplicationContext(), SinglePhotoViewActivity.class);
                    // passing array index
                    i.putExtra("id", position);
                    i.putExtra("event", event);
                    startActivity(i);
                }
            });
        } catch (Exception e) {
            Log.i("exception in parse", e.toString());
        }
    }

    public void addPhotoList() {
        PhotoService viewPhoto = ServiceFactory.getPhotoServiceInstance(this);

        try {
            size = viewPhoto.getCount(event.getEventId());
            List<String> urls = null;
            if (photos.size() < start) {
                start = 0;
            }
            if (start < size) {
                urls = viewPhoto.getPhotos(event.getEventId(), start + 1, start + BUFFER > size ? size : start + BUFFER, PhotoType.THUMBNAIL);
                start += BUFFER;
                photos.clear();
                for (String url : urls) {
                    Photo p = new Photo(url);
                    photos.add(p);
                }
                Log.d("Photo", "" + photos.size());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Create a file Uri for saving an image or video
     */
    private Uri getOutputMediaFileUri(int type) {
        return Uri.fromFile(getOutputMediaFile(type));
    }

    private class FetchFiles extends AsyncTask<String, Void, List<String>> {

        @Override

        protected List<String> doInBackground(String... strings) {

            List<String> fileNames = new ArrayList<String>();

            File yourDir = new File(Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_PICTURES), "reflectBig");

            Log.d(CURRENT_CLASS_LOG, "Dir path : " + yourDir);

            for (File f : yourDir.listFiles()) {
                uploadFile(f);

                fileNames.add(f.getName());

            }
            return fileNames;

        }

        // This method looks for the Exif Orientation in the file and returns the degrees of
        // rotation needed to right the image.
        private int getExifOrientation(File f) {
            int degree = 0;
            ExifInterface exif = null;
            try {
                exif = new ExifInterface(f.getAbsolutePath());
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            if (exif != null) {
                int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, -1);
                if (orientation != -1) {
                    switch (orientation) {
                        case ExifInterface.ORIENTATION_ROTATE_90:
                            degree = 90;
                            break;
                        case ExifInterface.ORIENTATION_ROTATE_180:
                            degree = 180;
                            break;
                        case ExifInterface.ORIENTATION_ROTATE_270:
                            degree = 270;
                            break;
                    }

                }
            }
            return degree;
        }

        private boolean uploadFile(File f) {
            PhotoService uploadPhoto = ServiceFactory.getPhotoServiceInstance(context);
            try {
                int degree = getExifOrientation(f);
                Log.d("EXIF ORIENTATION", "Photo has degrees: " + Integer.toString(degree));
                if(degree != 0){
                    RotateImage.rotateImage(f, degree);
                }
                uploadPhoto.uploadPhoto(event.getEventId(), f);
                f.delete();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return false;
        }

        private byte[] getDataFromFile(File file) throws IOException {

            FileInputStream fileInputStream = null;

            byte[] bFile = new byte[(int) file.length()];

            fileInputStream = new FileInputStream(file);

            fileInputStream.read(bFile);

            fileInputStream.close();

            return bFile;

        }


        @Override

        protected void onPostExecute(List<String> result) {

            Log.d(CURRENT_CLASS_LOG, "" + result);

        }
    }

    private class DataSaveProgress extends AsyncTask<String, Void, Boolean> {
        private ProgressDialog dialog;
        private Activity activity;

        public DataSaveProgress(Activity activity) {
            this.activity = activity;
            dialog = new ProgressDialog(activity);
        }

        @Override
        protected Boolean doInBackground(String... params) {
            addPhotoList();
            return true;
        }

        protected void onPreExecute() {
            this.dialog.setMessage("Loading Images");
            this.dialog.show();
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            if (dialog.isShowing()) {
                imageAdapter.notifyDataSetChanged();
                dialog.dismiss();
            }
        }
    }
}