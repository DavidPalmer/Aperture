package com.rewyndr.reflectbig.parse.impl;

import android.content.Context;

import com.parse.Parse;
import com.parse.ParseObject;
import com.rewyndr.reflectbig.R;
import com.rewyndr.reflectbig.parse.model.PhotoParse;

/**
 * This is an abstract class which has to be extended by all parse implementations for initializing
 * Parse
 * Created by Satish on 7/24/2014.
 */
public abstract class ParseBase {

    protected static boolean initialised = false;

    protected static void initParse(Context context) {
        if (!initialised) {
            ParseObject.registerSubclass(PhotoParse.class);
            Parse.initialize(context, context.getResources().getString(R.string.parse_app_id), context.getResources().getString(R.string.parse_client_key));
            initialised = true;
        }
    }
}
