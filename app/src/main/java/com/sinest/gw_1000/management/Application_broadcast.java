package com.sinest.gw_1000.management;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;

/**
 * Created by Jinwook on 2016-11-20.
 */

public class Application_broadcast extends BroadcastReceiver {

    private Handler mHandler;

    public Application_broadcast(Handler handler) {

        this.mHandler = handler;
    }

    @Override
    public void onReceive(Context context, Intent intent) {

        String name = intent.getAction();

        if (name.equals("update.data")) {

            mHandler.sendEmptyMessage(0);
        }
    }
}
