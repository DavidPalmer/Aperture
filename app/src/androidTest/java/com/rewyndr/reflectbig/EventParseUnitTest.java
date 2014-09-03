package com.rewyndr.reflectbig;

import android.test.AndroidTestCase;
import android.util.Log;

import com.parse.ParseException;
import com.parse.ParseUser;
import com.rewyndr.reflectbig.interfaces.EventService;
import com.rewyndr.reflectbig.parse.impl.EventServiceParse;

/**
 * Created by Satish on 9/2/2014.
 */
public class EventParseUnitTest extends AndroidTestCase {

    EventService instance = null;

    public void setUp() {
        instance = EventServiceParse.getInstance(getContext());
    }

    public void testGetEvents() throws Exception {
        /*ParseUser usr = new ParseUser();
        usr.setUsername("rajaramr");
        usr.setPassword("password");
        usr.signUp();
        Log.d("TestCase", String.valueOf(ParseUser.getCurrentUser()));*/
        ParseUser.logIn("satishra", "password");
        //System.out.println(instance.getEvents());
        Log.d("Events", String.valueOf(instance.getEvents()));
    }

    public void testGetAttendees() throws Exception {
        Log.d("Attendees", String.valueOf(instance.getAttendees("Fgw57rJi7w")));
    }
 }
