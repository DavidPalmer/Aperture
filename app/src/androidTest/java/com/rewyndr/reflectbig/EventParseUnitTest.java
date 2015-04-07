package com.rewyndr.reflectbig;

import android.test.AndroidTestCase;
import android.util.Log;

import com.parse.ParseUser;
import io.rewyndr.reflectbig.common.YNType;
import io.rewyndr.reflectbig.interfaces.EventService;
import io.rewyndr.reflectbig.model.Event;
import io.rewyndr.reflectbig.model.EventStatus;
import io.rewyndr.reflectbig.parse.impl.EventServiceParse;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by Satish on 9/2/2014.
 */
public class EventParseUnitTest extends AndroidTestCase {

    EventService instance = null;

    public void setUp() throws Exception {
        instance = EventServiceParse.getInstance(getContext());
        ParseUser.logIn("rajaramr@andrew.cmu.edu", "pass");
    }

    public void testGetEvents() throws Exception {
        /*ParseUser usr = new ParseUser();
        usr.setUsername("rajaramr");
        usr.setPassword("password");
        usr.signUp();
        Log.d("TestCase", String.valueOf(ParseUser.getCurrentUser()));*/

        //System.out.println(instance.getEvents());
        Map<EventStatus, List<Event>> events = instance.getEvents();
        Log.d("Events----", String.valueOf(events));
    }

    public void testGetAttendees() throws Exception {
        Log.d("Attendees", String.valueOf(instance.getAttendees("Fgw57rJi7w")));
    }

    public void testInviteParticipants() throws Exception {
        List<String> emailIds = new ArrayList<String>();
        emailIds.add("satishra@andrew.cmu.edu");
        emailIds.add("satishmufc@gmail.com");
        instance.inviteParticipants("BAB2EswEUn", emailIds);
    }

    public void testCreateEvent() throws Exception {
        Event e = new Event();
        e.setEventName("Dummy Event");
        e.setEventDesc("Dummy event for fence radius");
        e.setStartDate(new Date());
        e.setEndDate(new Date());
        e.setLocation("201 Conover Road, Pittsburgh, PA 15208");
        e.setShortLocation("Pittsburgh, PA");
        e.setLatitude(40.452842);
        e.setLongitude(-79.911363);
        e.setFenceRadius(10);
        Log.d("Event Id---", instance.createEvent(e));
    }

    public void testRespondToEvent() throws Exception {
        instance.respondToEvent("e8fulZ42Q1", YNType.N);
    }

    public void testGetEvent() throws Exception {
        String eventId = "X5KrTWQxlD";
        Log.d(this.getClass().getName(), "User--" + ParseUser.getCurrentUser().getObjectId());
        Log.d(this.getClass().getName(), instance.getEvent(eventId).toString());
    }
 }
