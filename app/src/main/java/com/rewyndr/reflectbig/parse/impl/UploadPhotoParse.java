package com.rewyndr.reflectbig.parse.impl;

import android.content.Context;

import com.parse.ParseFile;
import com.rewyndr.reflectbig.interfaces.UploadPhoto;
import com.rewyndr.reflectbig.parse.model.Photo;
import com.rewyndr.reflectbig.util.IOUtils;

import java.io.File;

/**
 * This provides an implementation of <tt>UploadPhoto</tt> by uploading photos to Parse
 * Created by Satish on 7/24/2014.
 */
public class UploadPhotoParse extends ParseBase implements UploadPhoto {
    private static UploadPhotoParse instance = new UploadPhotoParse();

    public static UploadPhotoParse getInstance(Context context) {
        if (instance == null) {
            initParse(context);
            instance = new UploadPhotoParse();
        }
        return instance;
    }

    protected UploadPhotoParse() {

    }

    @Override
    public void uploadPhoto(File file) throws Exception {
        ParseFile pFile = new ParseFile(file.getName(), IOUtils.convertFileToBytes(file));
        pFile.save();
        Photo photo = new Photo();
        photo.setPhotoFile(pFile);
        photo.save();
    }
}
