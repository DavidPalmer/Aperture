package com.rewyndr.reflectbig.interfaces;

import com.rewyndr.reflectbig.common.YNType;
import com.rewyndr.reflectbig.model.Event;
import com.rewyndr.reflectbig.model.EventStatus;

import java.util.List;
import java.util.Map;

/**
 * Created by Satish on 9/1/2014.
 */
public interface EventService {

    /**
     * This method gets all events of the current user
     *
     * @return
     * @throws Exception
     */
    Map<EventStatus, List<Event>> getEvents() throws Exception;

    /**
     * This method gets all attendees of a given event
     *
     * @param eventId
     * @return
     * @throws Exception
     */
    List<String> getAttendees(String eventId) throws Exception;

    /**
     * This method creates an event
     *
     * @param event
     * @return the eventId
     * @throws Exception
     */
    String createEvent(Event event) throws Exception;

    /**
     * This method adds invitees to the event
     *
     * @param eventId
     * @param inviteeEmailIds
     * @throws Exception
     */
    void inviteParticipants(String eventId, List<String> inviteeEmailIds) throws Exception;

    /**
     * This method updates the status of the attendee in an event
     *
     * @param eventId
     * @param response
     * @throws Exception
     */
    void respondToEvent(String eventId, YNType response) throws Exception;

    /**
     * This method gets event details from event object id
     *
     * @param eventId
     * @return
     * @throws Exception
     */
    Event getEvent(String eventId) throws Exception;
}
