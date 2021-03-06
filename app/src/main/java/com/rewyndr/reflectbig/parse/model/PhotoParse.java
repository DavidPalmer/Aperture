package com.rewyndr.reflectbig.parse.model;

import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;

/**
 * This class represents the model for the <tt>Photo</tt> datatype in Parse
 * Created by Satish on 7/14/2014.
 */
@ParseClassName("Photo")
public class PhotoParse extends ParseObject {

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

    public ParseFile getPhotoFile640() {
        return getParseFile(FieldNames.PHOTO_FILE_640);
    }

    public void setPhotoFile640(ParseFile photoFile) {
        put(FieldNames.PHOTO_FILE_640, photoFile);
    }

    public ParseFile getPhotoFile1024() {
        return getParseFile(FieldNames.PHOTO_FILE_1024);
    }

    public void setPhotoFile1024(ParseFile photoFile) {
        put(FieldNames.PHOTO_FILE_1024, photoFile);
    }

    public EventParse getEvent() {
        return (EventParse) getParseObject(FieldNames.PHOTO_EVENT);
    }

    public void setEvent(EventParse event) {
        put(FieldNames.PHOTO_EVENT, event);
    }

    public ParseUser getTakenBy() {
        return getParseUser(FieldNames.PHOTO_TAKEN_BY);
    }

    public void setTakenBy(ParseUser user) {
        put(FieldNames.PHOTO_TAKEN_BY, user);
    }
}
