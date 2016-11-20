package com.sinest.gw_1000.management;

import android.app.Application;
import android.content.Context;

import com.sinest.gw_1000.communication.Communicator;

/**
 * Created by Jinwook on 2016-11-20.
 */

public class Application_communicator extends Application {

    private static Context context;
    private static Communicator communicator;

    public void onCreate() {

        Application_communicator.context = getApplicationContext();
        Application_communicator.communicator = new Communicator(context);
    }

    public static Communicator getCommunicator() {

        return communicator;
    }
}
