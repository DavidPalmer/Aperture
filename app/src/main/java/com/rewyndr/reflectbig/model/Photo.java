package com.rewyndr.reflectbig.model;

import android.graphics.Bitmap;

import com.rewyndr.reflectbig.adapter.ImageAdapter;


/**
 * Created by Dileeshvar on 7/16/14.
 */
public class Photo {
    private String imageURL;
    private Bitmap image;
    private ImageAdapter imageAdapter;

    public Photo(String imageURL) {
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
}
