package com.rewyndr.reflectbig.model;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Log;

import com.rewyndr.reflectbig.Service.ImageService;
import com.rewyndr.reflectbig.adapter.ImageAdapter;


/**
 * Created by dil on 7/16/14.
 */
public class Photo {
    private String imageURL;
    private Bitmap image;
    private ImageAdapter imageAdapter;

    public Photo(String imageURL){
        this.imageURL = imageURL;
        this.image = null;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }

    public ImageAdapter getImageAdapter() {
        return imageAdapter;
    }

    public void setImageAdapter(ImageAdapter imageAdapter) {
        this.imageAdapter = imageAdapter;
    }

    public void loadImage(ImageAdapter imageAdapter) {
        // HOLD A REFERENCE TO THE ADAPTER
        this.imageAdapter = imageAdapter;
        if (imageURL != null && !imageURL.equals("")) {
            new ImageLoadTask().execute(imageURL);
        }
    }

    private class ImageLoadTask extends AsyncTask<String, String, Bitmap> {

        @Override
        protected void onPreExecute() {
            Log.i("ImageLoadTask", "Loading image...");
        }

        // param[0] is img url
        protected Bitmap doInBackground(String... param) {
            Log.i("ImageLoadTask", "Attempting to load image URL: " + param[0]);
            try {
                Bitmap b = ImageService.getBitmapFromURL(param[0]);
                return b;
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        protected void onProgressUpdate(String... progress) {
            // NO OP
        }

        protected void onPostExecute(Bitmap ret) {
            if (ret != null) {
                Log.i("ImageLoadTask", "Successfully loaded image");
                image = ret;
                if (imageAdapter != null) {
                    // WHEN IMAGE IS LOADED NOTIFY THE ADAPTER
                    imageAdapter.notifyDataSetChanged();
                }
            } else {
                Log.e("ImageLoadTask", "Failed to load image");
            }
        }
    }
}
