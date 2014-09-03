package com.rewyndr.reflectbig.parse.impl;

import android.content.Context;

import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.rewyndr.reflectbig.interfaces.EventService;
import com.rewyndr.reflectbig.model.AttendeeStatus;
import com.rewyndr.reflectbig.model.Event;
import com.rewyndr.reflectbig.model.EventStatus;
import com.rewyndr.reflectbig.parse.model.AttendeeParse;
import com.rewyndr.reflectbig.parse.model.EventParse;
import com.rewyndr.reflectbig.parse.model.FieldNames;
import com.rewyndr.reflectbig.util.DateUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.rewyndr.reflectbig.model.EventStatus.*;

/**
 * Created by Satish on 9/2/2014.
 */
public class EventServiceParse extends ParseBase implements EventService {

    private static EventServiceParse instance = null;

    protected EventServiceParse() {
        // Exists only to defeat instantiation.
    }

    public static EventServiceParse getInstance(Context context) {
        if (instance == null) {
            initParse(context);
            instance = new EventServiceParse();
        }
        return instance;
    }

    @Override
    public Map<EventStatus, List<Event>> getEvents() throws ParseException {
        Map<EventStatus, List<Event>> eventList = new HashMap<EventStatus, List<Event>>();
        List<Event> pastEvents = new ArrayList<Event>();
        List<Event> currentEvents = new ArrayList<Event>();
        List<Event> upcomingEvents = new ArrayList<Event>();
        ParseUser current = ParseUser.getCurrentUser();
        ParseQuery<AttendeeParse> query = ParseQuery.getQuery(AttendeeParse.class);
        query.whereEqualTo(FieldNames.ATTENDEE, current);
        query.include(FieldNames.ATTENDEE_EVENT);
        query.include(FieldNames.ATTENDEE_INVITED_BY);
        List<AttendeeParse> attendeeList = query.find();
        for (AttendeeParse attendee : attendeeList) {
            Event event = getEventInfo(attendee);
            switch(event.getStatus()) {
                case PAST:
                    pastEvents.add(event);
                    break;
                case CURRENT:
                    currentEvents.add(event);
                    break;
                case UPCOMING:
                    upcomingEvents.add(event);
                    break;
            }
        }
        return eventList;
    }

    private Event getEventInfo(AttendeeParse attendee) throws ParseException {
        Event event = new Event();
        EventParse eventParse = attendee.getEvent();
        event.setEventId(eventParse.getObjectId());
        event.setEventName(eventParse.getEventName());
        event.setStartDate(eventParse.getStartDateTime());
        event.setEndDate(eventParse.getEndDateTime());
        event.setLocation(eventParse.getLocation());
        event.setStatus(DateUtils.getEventType(event.getStartDate(), event.getEndDate()));
        event.setMyStatus(AttendeeStatus.valueOf(attendee.getStatus()));
        event.setInvitedBy(attendee.getInvitedBy().getUsername());
        eventParse.getCreatedBy().fetchIfNeeded();
        event.setCreatedBy(eventParse.getCreatedBy().getUsername());
        event.setPhotosCount(eventParse.getPhotosCount());
        event.setAttendeesCount(eventParse.getAttendeesCount());
        return event;
    }
}
