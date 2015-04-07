package io.rewyndr.reflectbig.service;

import android.content.Context;

import io.rewyndr.reflectbig.interfaces.EventService;
import io.rewyndr.reflectbig.interfaces.LoginService;
import io.rewyndr.reflectbig.interfaces.PhotoService;
import io.rewyndr.reflectbig.parse.impl.EventServiceParse;
import io.rewyndr.reflectbig.parse.impl.LoginServiceParse;
import io.rewyndr.reflectbig.parse.impl.PhotoServiceParse;

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
