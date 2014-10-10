package com.rewyndr.reflectbig.service;

import android.content.Context;

import com.rewyndr.reflectbig.interfaces.EventService;
import com.rewyndr.reflectbig.interfaces.LoginService;
import com.rewyndr.reflectbig.interfaces.PhotoService;
import com.rewyndr.reflectbig.parse.impl.EventServiceParse;
import com.rewyndr.reflectbig.parse.impl.LoginServiceParse;
import com.rewyndr.reflectbig.parse.impl.PhotoServiceParse;

/**
 * This factory provides methods to create appropriate service objects to communicate to the backend
 * Created by Satish on 9/1/2014.
 */
public class ServiceFactory {

    public static PhotoService getPhotoServiceInstance(Context context) {
        return PhotoServiceParse.getInstance(context);
    }

    public static EventService getEventServiceInstance(Context context) {
        return EventServiceParse.getInstance(context);
    }

    public static LoginService getLoginServiceInstance(Context context) {
        return LoginServiceParse.getInstance(context);
    }
}
