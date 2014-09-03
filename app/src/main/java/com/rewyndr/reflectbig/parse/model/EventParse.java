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

    private int photosCount;

    private int attendeesCount;

    private String eventShortLocation;

    private String eventDescription;

    public String getEventDescription() {
        return getString(FieldNames.EVENT_DESCRIPTION);
    }

    public void setEventDescription(String eventDescription) {
        this.eventDescription = eventDescription;
    }

    public String getEventShortLocation() {
        return getString(FieldNames.EVENT_SHORT_LOCATION);
    }

    public void setEventShortLocation(String eventShortLocation) {
        this.eventShortLocation = eventShortLocation;
    }

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

    public int getPhotosCount() {
        return getInt(FieldNames.EVENT_PHOTOS_COUNT);
    }

    public void setPhotosCount(int photosCount) {
        put(FieldNames.EVENT_PHOTOS_COUNT, photosCount);
    }

    public int getAttendeesCount() {
        return getInt(FieldNames.EVENT_ATTENDEES_COUNT);
    }

    public void setAttendeesCount(int attendeesCount) {
        put(FieldNames.EVENT_ATTENDEES_COUNT, attendeesCount);
    }
}
