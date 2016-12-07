package com.sinest.gw_1000.management;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;

import com.sinest.gw_1000.communication.Communicator;

/**
 * Created by Jinwook on 2016-11-20.
 */

public class Application_manager extends Application {

    // DB name
    public final static String NAME_OF_SHARED_PREF = "myData";

    // 대기모드 산소농도 / 수압세기 / 사용시간 값
    public final static String VAL_OXYGEN   = "val_oxygen";
    public final static String VAL_PRESSURE = "val_pressure";
    public final static String VAL_TIME     = "val_time";

    // 대기모드 동작시간
    public final static String WAITING_WORKING_TIME = "waiting_working_time";

    // 라이브러리 20개중 선택된 값
    public final static int MAX_CHECKED = 4;
    public final static String LIBRARY_LOC_  = "library_location_";

    // 매뉴얼 모드 man_pattern_*_*
    public final static String MANUAL_MODE_PATTERN_ = "man_pattern_";
    public final static String MANUAL_MODE_TIME_ = "man_time_";

    // 언어 [KOR, ENG, CHI]
    public static int LANGUAGE = 0;

    // 세팅 on/off값
    public final static String SETTING_ONOFF_VAL_ = "setting_onoff_val_";

    // 사운드 id
    public final static int NUM_OF_LANG = 3;
    public final static int NUM_OF_SOUND = 5;
    public final static int[] ID_KOR = new int[5];
    public final static int[] ID_ENG = new int[5];
    public final static int[] ID_CHI = new int[5];
    public final static int[][] ID_LANG_SOUND = new int[NUM_OF_LANG][NUM_OF_SOUND];

    // App context
    private static Context context;

    // Communicator
    private static Communicator communicator;

    // SoundManager
    private static SoundManager soundManager;

    // 앱 동작 시간
    private boolean isRun = false;
    private static int runningTime = 0;
    private Thread thread_runningTime;

    public void onCreate() {

        Application_manager.context = getApplicationContext();
        Application_manager.communicator = new Communicator(context);
        Application_manager.soundManager = new SoundManager(context);

        // 러닝타임 측정
        setThread_runningTime();
        isRun = true;
        thread_runningTime.start();

        // 기기 DPI 출력
        DisplayMetrics metrics = new DisplayMetrics();
        WindowManager mgr = (WindowManager)context.getSystemService(Context.WINDOW_SERVICE);
        mgr.getDefaultDisplay().getMetrics(metrics);
        Log.i("JW", "densityDPI = " + metrics.densityDpi);
    }

    synchronized public static int getRunningTime() {

        return runningTime;
    }

    synchronized public static void setRunningTime(int sec) {

        runningTime = sec;
    }

    synchronized private void setThread_runningTime() {

        thread_runningTime = new Thread(new Runnable() {
            @Override
            public void run() {

                while (isRun) {

                    try {

                        Thread.sleep(1000);
                        runningTime++;
                    }
                    catch (Exception e) {

                    }
                }
            }
        });
    }

    public static Communicator getCommunicator() {

        return communicator;
    }

    public static SoundManager getSoundManager() {

        return soundManager;
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
