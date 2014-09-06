package com.rewyndr.reflectbig.parse.impl;

import android.content.Context;

import com.parse.ParseException;
import com.parse.ParseUser;
import com.rewyndr.reflectbig.interfaces.LoginService;
import com.rewyndr.reflectbig.parse.model.FieldNames;

/**
 * This provides an implementation of <tt>LoginService</tt> with Parse as backend
 * Created by Satish on 9/6/2014.
 */
public class LoginServiceParse extends ParseBase implements LoginService {

    private static LoginServiceParse instance = null;

    protected LoginServiceParse() {
        // Exists only to defeat instantiation.
    }

    public static LoginServiceParse getInstance(Context context) {
        if (instance == null) {
            initParse(context);
            instance = new LoginServiceParse();
        }
        return instance;
    }

    @Override
    public void logIn(String email, String password) throws ParseException {
        ParseUser.logIn(email, password);
    }

    @Override
    public void signUp(String name, String email, String password) throws ParseException {
        ParseUser user = new ParseUser();
        user.setUsername(email);
        user.setPassword(password);
        user.put(FieldNames.USER_NAME, name);
        user.signUp();
    }

    @Override
    public void logOut() throws ParseException {
        ParseUser.logOut();
    }
}
