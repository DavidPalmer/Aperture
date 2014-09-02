package com.rewyndr.reflectbig.service;

import android.content.Context;

import com.rewyndr.reflectbig.interfaces.UploadPhoto;
import com.rewyndr.reflectbig.interfaces.ViewPhoto;
import com.rewyndr.reflectbig.parse.impl.UploadPhotoParse;
import com.rewyndr.reflectbig.parse.impl.ViewPhotoParse;

/**
 * Created by Satish on 9/1/2014.
 */
public class ServiceFactory {

    public static ViewPhoto getViewPhotoInstance(Context context) {
        return ViewPhotoParse.getInstance(context);
    }

    public static UploadPhoto getUploadPhotoInstance(Context context) {
        return UploadPhotoParse.getInstance(context);
    }
}
