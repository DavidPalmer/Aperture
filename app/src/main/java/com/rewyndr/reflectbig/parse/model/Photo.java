package com.rewyndr.reflectbig.parse.model;

import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseObject;

/**
 * This class represents the model for the <tt>Photo</tt> datatype in Parse
 * Created by Satish on 7/14/2014.
 */
@ParseClassName("Photo")
public class Photo extends ParseObject {

    private int photoNo;

    private ParseFile photoFile;

    public int getPhotoNo() {
        return getInt(FieldNames.PHOTO_NO);
    }

    public void setPhotoNo(int photoNo) {
        put(FieldNames.PHOTO_NO, photoNo);
    }

    public ParseFile getPhotoFile() {
        return getParseFile(FieldNames.PHOTO_FILE);
    }

    public void setPhotoFile(ParseFile photoFile) {
        put(FieldNames.PHOTO_FILE, photoFile);
    }

}
