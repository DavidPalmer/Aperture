package com.rewyndr.reflectbig.parse.model;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseUser;

/**
 * Created by Satish on 9/7/2014.
 */
@ParseClassName("Invitee")
public class InviteeParse extends ParseObject {

    public String getEmail() {
        return getString(FieldNames.INVITEE_EMAIL);
    }

    public void setEmail(String email) {
        put(FieldNames.INVITEE_EMAIL, email);
    }

    public EventParse getEvent() {
        return (EventParse) getParseObject(FieldNames.INVITEE_EVENT);
    }

    public void setEvent(EventParse event) {
        put(FieldNames.INVITEE_EVENT, event);
    }

    public ParseUser getInvitedBy() {
        return getParseUser(FieldNames.ATTENDEE_INVITED_BY);
    }

    public void setInvitedBy(ParseUser invitedBy) {
        put(FieldNames.ATTENDEE_INVITED_BY, invitedBy);
    }
}
