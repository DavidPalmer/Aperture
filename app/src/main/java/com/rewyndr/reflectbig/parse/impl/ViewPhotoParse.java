package com.rewyndr.reflectbig.parse.impl;

import android.content.Context;

import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.rewyndr.reflectbig.R;
import com.rewyndr.reflectbig.interfaces.ViewPhoto;
import com.rewyndr.reflectbig.parse.Constants;
import com.rewyndr.reflectbig.parse.model.Photo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Satish on 7/16/2014.
 */
public class ViewPhotoParse implements ViewPhoto {

    private static ViewPhotoParse instance = null;

    protected ViewPhotoParse() {
        // Exists only to defeat instantiation.
    }

    public static ViewPhotoParse getInstance(Context context) {
        if (instance == null) {
            ParseObject.registerSubclass(Photo.class);
            Parse.initialize(context, context.getResources().getString(R.string.parse_app_id), context.getResources().getString(R.string.parse_client_key));
            instance = new ViewPhotoParse();
        }
        return instance;
    }

    @Override
    public List<String> getPhotos(int start, int end) throws ParseException {
        List<String> result = new ArrayList<String>();
        List<Photo> queryResult = null;
        ParseQuery<Photo> query = ParseQuery.getQuery(Photo.class);
        List<Integer> idList = new ArrayList<Integer>();
        for (int i = start; i <= end; i++) {
            idList.add(i);
        }
        query.whereContainedIn(Constants.PHOTO_NO, idList);
        queryResult = query.find();
        for (Photo photo : queryResult) {
            result.add(photo.getPhotoFile().getUrl());
        }
        return result;
    }

    @Override
    public List<String> getPhoto(int num) throws ParseException {
        return getPhotos(num, num);
    }
}
