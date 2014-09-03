package com.rewyndr.reflectbig.interfaces;

import com.rewyndr.reflectbig.model.Event;
import com.rewyndr.reflectbig.model.EventStatus;

import java.util.List;
import java.util.Map;

/**
 * Created by Satish on 9/1/2014.
 */
public interface EventService {

    public Map<EventStatus, List<Event>> getEvents() throws Exception;
}
