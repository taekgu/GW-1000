package com.sinest.gw_1000.Utils;

import android.util.Log;

/**
 * Created by Administrator on 2017-10-12.
 */

public class LOG {

    private static final String TAG = "JW";

    public static void D(String msg) {

        Log.d(TAG, msg);
    }

    public static void D(String className, String msg) {

        Log.d(TAG, className + " | " + msg);
    }

    public static void I(String msg) {

        Log.i(TAG, msg);
    }

    public static void E(String msg) {

        Log.e(TAG, msg);
    }
}
