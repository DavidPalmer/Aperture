package com.rewyndr.reflectbig.interfaces;

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
     * @return
     * @throws Exception
     */
    Map<EventStatus, List<Event>> getEvents() throws Exception;

    /**
     * This method gets all attendees of a given event
     * @param eventId
     * @return
     * @throws Exception
     */
    List<String> getAttendees(String eventId) throws Exception;

    /**
     * This method creates an event
     * @param event
     * @throws Exception
     */
    void createEvent(Event event) throws Exception;
}
