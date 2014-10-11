package com.rewyndr.reflectbig;

import android.location.Location;
import android.test.AndroidTestCase;
import android.util.Log;

import com.parse.ParseUser;
import com.rewyndr.reflectbig.common.YNType;
import com.rewyndr.reflectbig.interfaces.EventService;
import com.rewyndr.reflectbig.model.Event;
import com.rewyndr.reflectbig.model.EventStatus;
import com.rewyndr.reflectbig.parse.impl.EventServiceParse;

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
        ParseUser.logIn("yparthas@andrew.cmu.edu", "password");
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
        emailIds.add("rajaramr@andrew.cmu.edu");
        emailIds.add("satishmufc@gmail.com");
        emailIds.add("yparthas@andrew.cmu.edu");
        emailIds.add("satishr@sase.ssn.edu.in");
        instance.inviteParticipants("Sr0jtVyma6", emailIds);
    }

    public void testCreateEvent() throws Exception {
        Event e = new Event();
        e.setEventName("Dummy Event123");
        e.setEventDesc("Dummy event description");
        e.setStartDate(new Date());
        e.setEndDate(new Date());
        e.setLocation("201 Conover Road, Pittsburgh, PA 15208");
        e.setShortLocation("Pittsburgh, PA");
        e.setLatitude(40.452842);
        e.setLongitude(-79.911363);
        Log.d("Event Id---", instance.createEvent(e));
    }

    public void testRespondToEvent() throws Exception {
        instance.respondToEvent("e8fulZ42Q1", YNType.N);
    }
 }
