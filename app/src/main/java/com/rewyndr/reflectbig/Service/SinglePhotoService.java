package com.rewyndr.reflectbig.Service;

import android.content.Context;
import android.util.Log;
import android.widget.ImageView;

import com.rewyndr.reflectbig.interfaces.ViewPhoto;
import com.rewyndr.reflectbig.parse.impl.ViewPhotoParse;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Utility class which is used across the viewing single image.
 *
 * Created by Raja on 7/16/2014.
 */
public class SinglePhotoService {
    /**
     * This utility function is used for fetching the URL's from the Parse API
     *  @param context
     * @param start
     * @param end
     */
    public static List<String> getImageUrls(Context context, int start, int end){
        List<String> urls = new ArrayList<String>();
        ViewPhoto viewPhoto = ViewPhotoParse.getInstance(context);
        try {
            urls = viewPhoto.getPhotosThumbnail(start, end);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return urls;
    }

    /**
     * This method is used to get the total image count at the time of hit to the server.
     * @param context
     * @return
     */
    public static int getImageCount(Context context){
        ViewPhoto viewPhoto = ViewPhotoParse.getInstance(context);
        int totalImages = 0;
        try {
            totalImages = viewPhoto.getCount();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return totalImages;
    }

    /**
     * This method is used to load the image in the given view from the provided URL.
     * Resize option can be provided if needed using the boolean.
     * NOTE: Not resizing the image when fetching.Assuming of getting 1024 image.
     * @param view
     * @param url
     * @param needsResize
     */
    public static void imageLoad(ImageView view, String url, boolean needsResize) {
        Context context = view.getContext();
        if(needsResize)
            Picasso.with(context).load(url).resize(1024,768).centerInside().into(view);
        else
            Picasso.with(context).load(url).into(view);
    }
}