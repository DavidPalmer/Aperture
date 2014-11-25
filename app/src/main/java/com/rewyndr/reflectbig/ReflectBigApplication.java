package com.rewyndr.reflectbig;

import android.app.Application;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.content.IntentFilter;

import com.parse.ParsePushBroadcastReceiver;
import com.parse.PushService;
import com.rewyndr.reflectbig.activity.LoginActivity;
import com.rewyndr.reflectbig.parse.impl.ParseBase;

/**
 * Created by Satish on 11/24/2014.
 */
public class ReflectBigApplication extends Application {
    public void onCreate() {
        // Initialize parse
        ParseBase.initParse(this);
    }
}