package com.rewyndr.reflectbig.interfaces;

import com.rewyndr.reflectbig.model.Event;

import java.util.List;

/**
 * Created by Satish on 9/1/2014.
 */
public interface EventService {

    public List<Event> getEvents() throws Exception;
}
