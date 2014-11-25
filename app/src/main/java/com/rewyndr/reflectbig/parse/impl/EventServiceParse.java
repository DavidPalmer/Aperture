package com.rewyndr.reflectbig.parse.impl;

import android.content.Context;
import android.util.Log;

import com.parse.ParseCloud;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.rewyndr.reflectbig.common.Constants;
import com.rewyndr.reflectbig.common.YNType;
import com.rewyndr.reflectbig.interfaces.EventService;
import com.rewyndr.reflectbig.model.Event;
import com.rewyndr.reflectbig.model.EventStatus;
import com.rewyndr.reflectbig.parse.model.AttendeeParse;
import com.rewyndr.reflectbig.parse.model.EventParse;
import com.rewyndr.reflectbig.parse.model.FieldNames;
import com.rewyndr.reflectbig.parse.model.InviteeParse;
import com.rewyndr.reflectbig.util.DateUtils;
import com.rewyndr.reflectbig.util.Utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        query.include(FieldNames.ATTENDEE_EVENT_CREATED_BY);
        List<AttendeeParse> attendeeList = query.find();
        for (AttendeeParse attendee : attendeeList) {
            Event event = getEventInfo(attendee);
            switch (event.getStatus()) {
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
        Collections.sort(pastEvents);
        Collections.sort(currentEvents);
        Collections.sort(upcomingEvents);
        eventList.put(EventStatus.PAST, pastEvents);
        eventList.put(EventStatus.CURRENT, currentEvents);
        eventList.put(EventStatus.UPCOMING, upcomingEvents);
        return eventList;
    }

    @Override
    public List<String> getAttendees(String eventId) throws Exception {
        List<String> attendees = new ArrayList<String>();
        ParseQuery<AttendeeParse> query = ParseQuery.getQuery(AttendeeParse.class);
        query.whereEqualTo(FieldNames.ATTENDEE_EVENT, new EventParse(eventId));
        query.whereEqualTo(FieldNames.ATTENDEE_STATUS, Constants.YES);
        query.include(FieldNames.ATTENDEE);
        List<AttendeeParse> attendeeParseList = query.find();
        for (AttendeeParse attendee : attendeeParseList) {
            attendees.add(attendee.getAttendee().getString(FieldNames.USER_NAME));
        }
        return attendees;
    }

    @Override
    public String createEvent(Event event) throws ParseException {
        EventParse eventParse = convertEventToEventParse(event);
        // Saving attendee will automatically save linked event
        AttendeeParse attendeeParse = new AttendeeParse();
        attendeeParse.setEvent(eventParse);
        attendeeParse.setInvitedBy(ParseUser.getCurrentUser());
        attendeeParse.setAttendee(ParseUser.getCurrentUser());
        attendeeParse.setStatus(Constants.YES);
        attendeeParse.save();
        return eventParse.getObjectId();
    }

    @Override
    public void inviteParticipants(String eventId, List<String> inviteeEmailIds) throws Exception {
        EventParse eventParse = new EventParse(eventId);
        eventParse.fetchIfNeeded();

        // Query for getting existing users
        ParseQuery<ParseUser> query = ParseQuery.getQuery(ParseUser.class);
        query.whereContainedIn(FieldNames.USER_USERNAME, inviteeEmailIds);
        List<ParseUser> results = query.find();

        // Query for finding existing users that are already invited
        ParseQuery<AttendeeParse> attendeeQuery = ParseQuery.getQuery(AttendeeParse.class);
        attendeeQuery.whereContainedIn(FieldNames.ATTENDEE, results);
        attendeeQuery.whereEqualTo(FieldNames.ATTENDEE_EVENT, eventParse);
        attendeeQuery.include(FieldNames.ATTENDEE);
        List<AttendeeParse> alreadyInvited = attendeeQuery.find();

        // Get only those users that aren't invited and invite them
        List<ParseUser> nonInvitedExistingUsers = getNonInvitedExistingUsers(alreadyInvited, results);
        List<AttendeeParse> attendeeList = new ArrayList<AttendeeParse>();
        for (ParseUser user: nonInvitedExistingUsers)  {
            Log.d("InviteParticipants", "Inviting existing user: " + user.getUsername());
            AttendeeParse attendee = new AttendeeParse();
            attendee.setAttendee(user);
            attendee.setInvitedBy(ParseUser.getCurrentUser());
            attendee.setEvent(eventParse);
            attendeeList.add(attendee);
        }
        if (attendeeList.size() > 0) {
            AttendeeParse.saveAll(attendeeList);
        }

        // Get non existing users
        List<String> nonExistingUsers = getNonExistingInvitees(inviteeEmailIds, results);
        Log.d("InviteParticipants", "Non existing users: " + nonExistingUsers);

        // Query for getting non existing users that are already invited
        ParseQuery<InviteeParse> inviteeQuery = ParseQuery.getQuery(InviteeParse.class);
        inviteeQuery.whereEqualTo(FieldNames.INVITEE_EVENT, eventParse);
        inviteeQuery.whereContainedIn(FieldNames.INVITEE_EMAIL, nonExistingUsers);
        List<InviteeParse> alreadyInvitedInvitees = inviteeQuery.find();

        // Invite the non existing non invited users
        List<String> nonExistingNonInvitedUsers = getNonExistingNonInvitedUsers(nonExistingUsers, alreadyInvitedInvitees);
        List<InviteeParse> inviteeList = new ArrayList<InviteeParse>();
        for (String nonInvitedUser : nonExistingNonInvitedUsers) {
            Log.d("InviteParticipants", "Inviting non existing user: " + nonInvitedUser);
            InviteeParse invitee = new InviteeParse();
            invitee.setEvent(eventParse);
            invitee.setEmail(nonInvitedUser);
            invitee.setInvitedBy(ParseUser.getCurrentUser());
            inviteeList.add(invitee);
        }
        if (inviteeList.size() > 0) {
            InviteeParse.saveAll(inviteeList);
        }

        if (attendeeList.size() > 0 || inviteeList.size() > 0) {
            notifyInvitees(eventParse, attendeeList, inviteeList);
        }
    }

    private void notifyInvitees(EventParse eventParse, List<AttendeeParse> attendeeList, List<InviteeParse> inviteeList) throws ParseException {
        Map<String, Object> params = new HashMap<String, Object>();
        eventParse = eventParse.fetch();
        params.put("event", eventParse.getEventName());
        params.put("eventloc", eventParse.getLocation());
        params.put("eventtime", DateUtils.getDateInFormat(eventParse.getStartDateTime(), "E, MM/dd/yyyy, HH:mm"));
        List<String> attendeeId = new ArrayList<String>();
        for (AttendeeParse attendee : attendeeList) {
            attendeeId.add(attendee.getAttendee().get(FieldNames.USER_NAME) + ":" + attendee.getAttendee().getUsername() + ":" + attendee.getAttendee().getObjectId());
        }
        List<String> inviteeId = new ArrayList<String>();
        for (InviteeParse invitee : inviteeList) {
            inviteeId.add(invitee.getEmail());
        }
        params.put("attendees", attendeeId);
        params.put("invitees", inviteeId);
        ParseCloud.callFunction("notifyInvitees", params);
    }

    @Override
    public void respondToEvent(String eventId, YNType response) throws Exception {
        ParseQuery<AttendeeParse> attendeeQuery = ParseQuery.getQuery(AttendeeParse.class);
        EventParse eventParse = new EventParse(eventId);
        attendeeQuery.whereEqualTo(FieldNames.ATTENDEE_EVENT, new EventParse(eventId));
        attendeeQuery.whereEqualTo(FieldNames.ATTENDEE, ParseUser.getCurrentUser());
        attendeeQuery.include(FieldNames.ATTENDEE_EVENT);
        List<AttendeeParse> attendeeParseList = attendeeQuery.find();
        AttendeeParse attendee = attendeeParseList.get(0);
        if (attendee.getStatus() == null && YNType.Y.equals(response)) {
            eventParse = attendee.getEvent();
            eventParse.increment(FieldNames.EVENT_ATTENDEES_COUNT);
        }
        attendee.setStatus(response.toString());
        attendee.save();
    }

    @Override
    public Event getEvent(String eventId) throws ParseException {
        ParseQuery<AttendeeParse> attendeeQuery = ParseQuery.getQuery(AttendeeParse.class);
        EventParse eventParse = new EventParse(eventId);
        Log.d(this.getClass().getName(), "User---" + ParseUser.getCurrentUser().getObjectId());
        attendeeQuery.whereEqualTo(FieldNames.ATTENDEE_EVENT, new EventParse(eventId));
        attendeeQuery.whereEqualTo(FieldNames.ATTENDEE, ParseUser.getCurrentUser());
        attendeeQuery.include(FieldNames.ATTENDEE_EVENT);
        attendeeQuery.include(FieldNames.ATTENDEE_INVITED_BY);
        attendeeQuery.include(FieldNames.ATTENDEE_EVENT_CREATED_BY);
        List<AttendeeParse> attendeeParseList = attendeeQuery.find();
        AttendeeParse attendee = attendeeParseList.get(0);
        return getEventInfo(attendee);
    }

    private Event getEventInfo(AttendeeParse attendee) throws ParseException {
        Event event = new Event();
        EventParse eventParse = attendee.getEvent();
        event.setEventId(eventParse.getObjectId());
        event.setEventName(eventParse.getEventName());
        event.setEventDesc(eventParse.getEventDescription());
        event.setStartDate(eventParse.getStartDateTime());
        event.setEndDate(eventParse.getEndDateTime());
        event.setLocation(eventParse.getLocation());
        event.setShortLocation(eventParse.getShortLocation());
        event.setStatus(DateUtils.getEventType(event.getStartDate(), event.getEndDate()));
        event.setMyStatus(Utils.getAttendeeStatus(event.getStatus(), attendee.getStatus()));
        event.setInvitedBy((String) attendee.getInvitedBy().get(FieldNames.USER_NAME));
        event.setCreatedBy((String) eventParse.getCreatedBy().get(FieldNames.USER_NAME));
        event.setPhotosCount(eventParse.getPhotosCount());
        event.setAttendeesCount(eventParse.getAttendeesCount());
        event.setLatitude(eventParse.getGeoLocation().getLatitude());
        event.setLongitude(eventParse.getGeoLocation().getLongitude());
        event.setFenceRadius(eventParse.getFenceRadius());
        return event;
    }

    private EventParse convertEventToEventParse(Event event) {
        EventParse eventParse = new EventParse();
        eventParse.setEventName(event.getEventName());
        eventParse.setEventDescription(event.getEventDesc());
        eventParse.setLocation(event.getLocation());
        eventParse.setStartDateTime(event.getStartDate());
        eventParse.setEndDateTime(event.getEndDate());
        eventParse.setShortLocation(event.getShortLocation());
        eventParse.setCreatedBy(ParseUser.getCurrentUser());
        eventParse.setGeoLocation(new ParseGeoPoint(event.getLatitude(), event.getLongitude()));
        eventParse.setAttendeesCount(1);
        eventParse.setFenceRadius(event.getFenceRadius());
        return eventParse;
    }

    private List<ParseUser> getNonInvitedExistingUsers(List<AttendeeParse> alreadyInvited, List<ParseUser> existingUsers) {
        List<ParseUser> nonInvitedExistingUsers = new ArrayList<ParseUser>();
        List<String> invitedUserIds = new ArrayList<String>();
        if (alreadyInvited.size() == 0) {
            nonInvitedExistingUsers.addAll(existingUsers);
        } else {
            for (AttendeeParse attendee : alreadyInvited) {
                invitedUserIds.add(attendee.getAttendee().getObjectId());
            }
            for (ParseUser user : existingUsers) {
                if (!invitedUserIds.contains(user.getObjectId())) {
                    nonInvitedExistingUsers.add(user);
                }
            }
        }
        return nonInvitedExistingUsers;
    }

    private List<String> getNonExistingInvitees(List<String> invitees, List<ParseUser> existingUsers) {
        List<String> existingEmails = new ArrayList<String>();
        List<String> nonExistingInvitees = new ArrayList<String>(invitees);
        for (ParseUser user : existingUsers) {
            existingEmails.add(user.getUsername());
        }
        nonExistingInvitees.removeAll(existingEmails);
        return nonExistingInvitees;
    }

    private List<String> getNonExistingNonInvitedUsers(List<String> invitees, List<InviteeParse> alreadyInvited) {
        List<String> nonInvited = new ArrayList<String>(invitees);
        List<String> invited = new ArrayList<String>();
        for (InviteeParse invitedUser : alreadyInvited) {
            invited.add(invitedUser.getEmail());
        }
        nonInvited.removeAll(invited);
        return nonInvited;
    }
}
