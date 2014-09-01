package com.rewyndr.reflectbig.parse.impl;

import android.content.Context;

import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.rewyndr.reflectbig.R;
import com.rewyndr.reflectbig.common.PhotoType;
import com.rewyndr.reflectbig.interfaces.ViewPhoto;
import com.rewyndr.reflectbig.parse.model.FieldNames;
import com.rewyndr.reflectbig.parse.model.Photo;

import java.util.ArrayList;
import java.util.List;

/**
 * This provides an implementation of <tt>ViewPhoto</tt> by retrieving photos from Parse
 * Created by Satish on 7/16/2014.
 */
public class ViewPhotoParse extends ParseBase implements ViewPhoto {

    private static ViewPhotoParse instance = null;

    protected ViewPhotoParse() {
        // Exists only to defeat instantiation.
    }

    public static ViewPhotoParse getInstance(Context context) {
        if (instance == null) {
            initParse(context);
            instance = new ViewPhotoParse();
        }
        return instance;
    }

    @Override
    public List<String> getPhotos(int start, int end, PhotoType photoType) throws ParseException {
        List<String> result = new ArrayList<String>();
        List<Photo> queryResult = null;
        ParseQuery<Photo> query = ParseQuery.getQuery(Photo.class);
        List<Integer> idList = new ArrayList<Integer>();
        for (int i = start; i <= end; i++) {
            idList.add(i);
        }
        query.whereContainedIn(FieldNames.PHOTO_NO, idList);
        queryResult = query.find();
        for (Photo photo : queryResult) {
            if (photoType == PhotoType.ACTUAL) {
                result.add(photo.getPhotoFile().getUrl());
            } else if (photoType == PhotoType.THUMBNAIL) {
                result.add(photo.getPhotoFile640().getUrl());
            } else if (photoType == PhotoType.SMALL) {
                result.add(photo.getPhotoFile1024().getUrl());
            }
        }
        return result;
    }

    @Override
    public String getPhoto(int num, PhotoType photoType) throws ParseException {
        List<String> results = getPhotos(num, num, photoType);
        if (results.size() > 0) {
            return results.get(0);
        } else {
            return null;
        }
    }

    @Override
    public int getCount() throws ParseException {
        ParseQuery<Photo> query = ParseQuery.getQuery(Photo.class);
        return query.count();
    }
}
