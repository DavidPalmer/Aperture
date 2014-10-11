package com.rewyndr.reflectbig;

import android.test.AndroidTestCase;
import android.util.Log;

import com.parse.ParseUser;
import com.rewyndr.reflectbig.parse.impl.LoginServiceParse;

/**
 * Created by Satish on 9/6/2014.
 */
public class LoginServiceParseUnitTest extends AndroidTestCase {

    LoginServiceParse instance = null;

    public void setUp() {
        instance = LoginServiceParse.getInstance(getContext());
    }

    public void testLogin() throws Exception{
        instance.logIn("satishra@andrew.cmu.edu", "password");
        assertEquals("Satish R", ParseUser.getCurrentUser().get("name"));
    }

    public void testSignUp() throws Exception {
        instance.signUp("Satish", "satishr@sase.ssn.edu.in", "password");
        Log.d("Current User----", ParseUser.getCurrentUser().getUsername());
    }

    public void testLogout() throws Exception {
        instance.logIn("satishra@andrew.cmu.edu", "password");
        assertEquals("Satish R", ParseUser.getCurrentUser().get("name"));
        instance.logOut();
        assertNull(ParseUser.getCurrentUser());
    }
}
