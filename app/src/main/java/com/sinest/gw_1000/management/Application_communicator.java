package com.sinest.gw_1000.management;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.view.View;

import com.sinest.gw_1000.communication.Communicator;

/**
 * Created by Jinwook on 2016-11-20.
 */

public class Application_communicator extends Application {

    // DB name
    public final static String NAME_OF_SHARED_PREF = "myData";

    // 대기모드 산소농도 / 수압세기 / 사용시간 값
    public final static String VAL_OXYGEN   = "val_oxygen";
    public final static String VAL_PRESSURE = "val_pressure";
    public final static String VAL_TIME     = "val_time";

    // 라이브러리 20개중 선택된 값
    public final static String LIBRARY_LOC  = "library_location";

    // 매뉴얼 모드 man_pattern_*_*
    public final static String MANUAL_MODE_PATTERN_ = "man_pattern_";
    public final static String MANUAL_MODE_TIME_ = "man_time_";

    public final static String WAITING_WORKING_TIME = "waiting_working_time";

    private static Context context;
    private static Communicator communicator;

    // 앱 동작 시간
    private int runningTime = 0;

    public void onCreate() {

        Application_communicator.context = getApplicationContext();
        Application_communicator.communicator = new Communicator(context);

    }

    public static Communicator getCommunicator() {

        return communicator;
    }

    public static void setFullScreen(Activity activity)
    {
        activity.getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
        );
    }
}
