package com.rewyndr.reflectbig;

import android.app.Application;

import com.parse.PushService;
import com.rewyndr.reflectbig.activity.LoginActivity;
import com.rewyndr.reflectbig.parse.impl.ParseBase;

/**
 * Created by Satish on 11/24/2014.
 */
public class ReflectBigApplication extends Application{
    public void onCreate() {
        // Initialize parse
        ParseBase.initParse(this);

        // Set callback for push notification
        PushService.setDefaultPushCallback(this, LoginActivity.class);
    }
}
