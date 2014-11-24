package com.rewyndr.reflectbig.parse.model;

/**
 * This provides the names of fields in Parse
 *
 * Created by Satish on 7/16/2014.
 */
public class FieldNames {

    // Photo
    public static final String PHOTO_NO = "photoNo";

    public static final String PHOTO_FILE = "photoFile";

    public static final String PHOTO_FILE_640 = "photoFile640";

    public static final String PHOTO_FILE_1024 = "photoFile1024";

    public static final String PHOTO_EVENT = "event";

    public static final String PHOTO_TAKEN_BY = "takenBy";

    // Event
    public static final String EVENT_NAME = "eventName";

    public static final String EVENT_START_DATE_TIME = "startDateTime";

    public static final String EVENT_END_DATE_TIME = "endDateTime";

    public static final String EVENT_LOCATION = "location";

    public static final String EVENT_CREATED_BY = "createdBy";

    public static final String EVENT_PHOTOS_COUNT = "photoCount";

    public static final String EVENT_ATTENDEES_COUNT = "attendeeCount";

    public static final String EVENT_SHORT_LOCATION = "shortLocation";

    public static final String EVENT_DESCRIPTION = "eventDescription";

    public static final String EVENT_GEO_LOCATION = "geoLocation";

    // Attendee
    public static final String ATTENDEE_EVENT = "event";

    public static final String ATTENDEE = "attendee";

    public static final String ATTENDEE_INVITED_BY = "invitedBy";

    public static final String ATTENDEE_STATUS = "status";

    public static final String ATTENDEE_EVENT_CREATED_BY = ATTENDEE_EVENT + "." + EVENT_CREATED_BY;

    // User
    public static final String USER_NAME = "name";

    public static final String USER_USERNAME = "username";

    // Invitee
    public static final String INVITEE_EMAIL = "email";

    public static final String INVITEE_EVENT = "event";
}
