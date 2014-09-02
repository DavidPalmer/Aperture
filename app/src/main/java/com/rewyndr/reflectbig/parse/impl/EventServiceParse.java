package com.rewyndr.reflectbig.parse.impl;

import android.content.Context;

import com.rewyndr.reflectbig.interfaces.EventService;
import com.rewyndr.reflectbig.model.Event;

import java.util.List;

/**
 * Created by Satish on 9/2/2014.
 */
public class EventServiceParse extends ParseBase implements EventService {

    private static EventServiceParse instance = null;

    protected EventServiceParse() {
        // Exists only to defeat instantiation.
    }

    public static EventServiceParse getInstance(Context context) {
        if (instance == null) {
            initParse(context);
            instance = new EventServiceParse();
        }
        return instance;
    }

    @Override
    public List<Event> getEvents() {
        return null;
    }
}
