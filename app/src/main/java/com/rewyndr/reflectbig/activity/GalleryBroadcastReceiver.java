package com.rewyndr.reflectbig.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by dil on 11/23/14.
 */
public class GalleryBroadcastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Intent downloader = new Intent(context, GalleryService.class);
        context.startService(downloader);
    }
}
