package com.rewyndr.reflectbig.service;

import android.content.Context;

import com.rewyndr.reflectbig.interfaces.EventService;
import com.rewyndr.reflectbig.interfaces.PhotoService;
import com.rewyndr.reflectbig.parse.impl.EventServiceParse;
import com.rewyndr.reflectbig.parse.impl.PhotoServiceParse;

/**
 * Created by Satish on 9/1/2014.
 */
public class ServiceFactory {

    public static PhotoService getPhotoServiceInstance(Context context) {
        return PhotoServiceParse.getInstance(context);
    }

    public static EventService getEventServiceInstance(Context context) {
        return EventServiceParse.getInstance(context);
    }
}
