package com.rewyndr.reflectbig.observer;

import android.content.Context;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;

/**
 * Created by dil on 11/20/14.
 */
public class GalleryObserver extends ContentObserver {
    public GalleryObserver(Handler handler) {
        super(handler);
    }

    @Override
    public void onChange(boolean selfChange) {
        this.onChange(selfChange, null);
    }

    @Override
    public void onChange(boolean selfChange, Uri uri) {
//        doPhotoUpload();
    }

    private void doPhotoUpload(Context context, Uri uri) {
        Cursor cursor = context.getContentResolver().query(uri, null, null,
                null, "date_added DESC");
    }
}
