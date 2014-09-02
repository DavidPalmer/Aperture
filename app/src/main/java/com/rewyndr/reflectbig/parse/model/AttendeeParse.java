package com.rewyndr.reflectbig.parse.model;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.rewyndr.reflectbig.model.Event;

/**
 * Created by Satish on 9/2/2014.
 */
@ParseClassName("Attendee")
public class AttendeeParse extends ParseObject {
    private EventParse event;

    private ParseUser attendee;

    private ParseUser invitedBy;

    private String status;

    public EventParse getEvent() {
        return (EventParse) getParseObject(FieldNames.ATTENDEE_EVENT);
    }

    public void setEvent(EventParse event) {
        put(FieldNames.ATTENDEE_EVENT, event);
    }

    public ParseUser getAttendee() {
        return getParseUser(FieldNames.ATTENDEE);
    }

    public void setAttendee(ParseUser attendee) {
        put(FieldNames.ATTENDEE, attendee);
    }

    public ParseUser getInvitedBy() {
        return getParseUser(FieldNames.ATTENDEE_INVITED_BY);
    }

    public void setInvitedBy(ParseUser invitedBy) {
        put(FieldNames.ATTENDEE_INVITED_BY, invitedBy);
    }

    public String getStatus() {
        return getString(FieldNames.ATTENDEE_STATUS);
    }

    public void setStatus(String status) {
        put(FieldNames.ATTENDEE_STATUS, status);
    }
}
