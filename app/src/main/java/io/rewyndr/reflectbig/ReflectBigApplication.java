package io.rewyndr.reflectbig;

import android.app.Application;

import io.rewyndr.reflectbig.parse.impl.ParseBase;

/**
 * Created by Satish on 11/24/2014.
 */
public class ReflectBigApplication extends Application {
    public void onCreate() {
        // Initialize parse
        ParseBase.initParse(this);
    }
}