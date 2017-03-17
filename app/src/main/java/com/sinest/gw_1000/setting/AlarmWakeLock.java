package com.sinest.gw_1000.setting;

import android.content.Context;
import android.os.PowerManager;
import android.util.Log;

/**
 * Created by Administrator on 2016-11-30.
 */

public class AlarmWakeLock {

    private static final String TAG = "AlarmWakeLock";
    private static PowerManager.WakeLock mWakeLock;

    public static void wakeLock(Context context){
        if(mWakeLock != null){
            return;
        }
        PowerManager powerManager = (PowerManager)context.getSystemService(Context.POWER_SERVICE);
        mWakeLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, TAG);
        mWakeLock.acquire();
    }

    public static void releaseWakeLock(){
        if(mWakeLock != null){
            mWakeLock.release();
            mWakeLock = null;
        }
    }
}
