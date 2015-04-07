package io.rewyndr.reflectbig.parse.impl;

import android.content.Context;

import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import io.rewyndr.reflectbig.interfaces.LoginService;
import io.rewyndr.reflectbig.parse.model.AttendeeParse;
import io.rewyndr.reflectbig.parse.model.FieldNames;
import io.rewyndr.reflectbig.parse.model.InviteeParse;

import java.util.ArrayList;
import java.util.List;

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
        ParseInstallation installation = ParseInstallation.getCurrentInstallation();
        installation.put("userId",ParseUser.getCurrentUser().getObjectId());
        installation.saveInBackground();
    }

    @Override
    public void signUp(String name, String email, String password) throws ParseException {
        ParseUser user = new ParseUser();
        user.setUsername(email);
        user.setPassword(password);
        user.put(FieldNames.USER_NAME, name);
        user.signUp();
        convertInviteeToAttendee(user);
    }

    @Override
    public void logOut() throws ParseException {
        ParseUser.logOut();
    }

    /**
     * This method is used to convert new users from invitee to attendee
     *
     * @param user
     * @throws ParseException
     */
    private void convertInviteeToAttendee(ParseUser user) throws ParseException {
        ParseQuery<InviteeParse> inviteeQuery = ParseQuery.getQuery(InviteeParse.class);
        inviteeQuery.whereEqualTo(FieldNames.INVITEE_EMAIL, user.getUsername());
        List<InviteeParse> matchingInvitees = inviteeQuery.find();
        List<AttendeeParse> attendees = new ArrayList<AttendeeParse>();
        for (InviteeParse invitee : matchingInvitees) {
            AttendeeParse attendee = new AttendeeParse();
            attendee.setEvent(invitee.getEvent());
            attendee.setAttendee(user);
            if (invitee.getInvitedBy() != null) {
                attendee.setInvitedBy(invitee.getInvitedBy());
            }
            attendees.add(attendee);
        }
        AttendeeParse.saveAll(attendees);
        InviteeParse.deleteAll(matchingInvitees);
    }
}
