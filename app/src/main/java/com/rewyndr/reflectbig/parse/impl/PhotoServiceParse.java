package com.rewyndr.reflectbig.parse.impl;

import android.content.Context;

import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseQuery;
import com.rewyndr.reflectbig.common.PhotoType;
import com.rewyndr.reflectbig.interfaces.PhotoService;
import com.rewyndr.reflectbig.parse.model.EventParse;
import com.rewyndr.reflectbig.parse.model.FieldNames;
import com.rewyndr.reflectbig.parse.model.PhotoParse;
import com.rewyndr.reflectbig.util.IOUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * This provides an implementation of <tt>PhotoService</tt> to upload, retrieve photos with Parse
 * Created by Satish on 9/2/2014.
 */
public class PhotoServiceParse extends ParseBase implements PhotoService {

    private static PhotoServiceParse instance = null;

    protected PhotoServiceParse() {
        // Exists only to defeat instantiation.
    }

    public static PhotoServiceParse getInstance(Context context) {
        if (instance == null) {
            initParse(context);
            instance = new PhotoServiceParse();
        }
        return instance;
    }

    @Override
    public List<String> getPhotos(String eventId, int start, int end, PhotoType photoType) throws ParseException {
        List<String> result = new ArrayList<String>();
        List<PhotoParse> queryResult = null;
        ParseQuery<PhotoParse> query = ParseQuery.getQuery(PhotoParse.class);
        List<Integer> idList = new ArrayList<Integer>();
        for (int i = start; i <= end; i++) {
            idList.add(i);
        }
        query.whereContainedIn(FieldNames.PHOTO_NO, idList);
        query.whereEqualTo(FieldNames.PHOTO_EVENT, new EventParse(eventId));
        queryResult = query.find();
        for (PhotoParse photoParse : queryResult) {
            if (photoType == PhotoType.ACTUAL) {
                result.add(photoParse.getPhotoFile().getUrl());
            } else if (photoType == PhotoType.THUMBNAIL) {
                result.add(photoParse.getPhotoFile640().getUrl());
            } else if (photoType == PhotoType.SMALL) {
                result.add(photoParse.getPhotoFile1024().getUrl());
            }
        }
        return result;
    }

    @Override
    public String getPhoto(String eventId, int num, PhotoType photoType) throws ParseException {
        List<String> results = getPhotos(eventId, num, num, photoType);
        if (results.size() > 0) {
            return results.get(0);
        } else {
            return null;
        }
    }

    @Override
    public int getCount(String eventId) throws ParseException {
        ParseQuery<PhotoParse> query = ParseQuery.getQuery(PhotoParse.class);
        query.whereEqualTo(FieldNames.PHOTO_EVENT, new EventParse(eventId));
        return query.count();
    }

    @Override
    public void uploadPhoto(String eventId, File file) throws Exception {
        ParseFile pFile = new ParseFile(file.getName(), IOUtils.convertFileToBytes(file));
        pFile.save();
        PhotoParse photoParse = new PhotoParse();
        photoParse.setPhotoFile(pFile);
        photoParse.setEvent(new EventParse(eventId));
        photoParse.save();
    }
}
