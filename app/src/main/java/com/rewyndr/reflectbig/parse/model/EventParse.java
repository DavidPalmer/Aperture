package com.rewyndr.reflectbig.parse.model;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseUser;

import java.util.Date;

/**
 * Created by Satish on 9/1/2014.
 */
@ParseClassName("Event")
public class EventParse extends ParseObject {

    private String eventName;

    private Date startDateTime;

    private Date endDateTime;

    private String location;

    private ParseUser createdBy;

    public String getEventName() {
        return getString(FieldNames.EVENT_NAME);
    }

    public void setEventName(String eventName) {
        put(FieldNames.EVENT_NAME, eventName);
    }

    public Date getStartDateTime() {
        return getDate(FieldNames.EVENT_START_DATE_TIME);
    }

    public void setStartDateTime(Date startDateTime) {
        put(FieldNames.EVENT_START_DATE_TIME, startDateTime);
    }

    public Date getEndDateTime() {
        return getDate(FieldNames.EVENT_END_DATE_TIME);
    }

    public void setEndDateTime(Date endDateTime) {
        put(FieldNames.EVENT_END_DATE_TIME, endDateTime);
    }

    public String getLocation() {
        return getString(FieldNames.EVENT_LOCATION);
    }

    public void setLocation(String location) {
        put(FieldNames.EVENT_LOCATION, location);
    }

    public ParseUser getCreatedBy() {
        return getParseUser(FieldNames.EVENT_CREATED_BY);
    }

    public void setCreatedBy(ParseUser createdBy) {
        put(FieldNames.EVENT_CREATED_BY, createdBy);
    }
}
