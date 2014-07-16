package com.rewyndr.reflectbig.parse.model;

import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.rewyndr.reflectbig.parse.Constants;

/**
 * Created by Satish on 7/14/2014.
 */
@ParseClassName("Photo")
public class Photo extends ParseObject {

    private int photoNo;

    private ParseFile photoFile;

    public int getPhotoNo() {
        return getInt(Constants.PHOTO_NO);
    }

    public void setPhotoNo(int photoNo) {
        put(Constants.PHOTO_NO, photoNo);
    }

    public ParseFile getPhotoFile() {
        return getParseFile(Constants.PHOTO_FILE);
    }

    public void setPhotoFile(ParseFile photoFile) {
        put(Constants.PHOTO_FILE, photoFile);
    }

}
