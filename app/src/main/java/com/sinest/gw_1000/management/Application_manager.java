package com.sinest.gw_1000.management;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;

import com.sinest.gw_1000.communication.Communicator;

/**
 * Created by Jinwook on 2016-11-20.
 */

public class Application_manager extends Application {

    // RFID - 일반 대기 모드 전환시 센서값 저장
    public static int SENSOR_HUMIDITY   = 0;
    public static int SENSOR_OXYGEN     = 0;
    public static int SENSOR_TEMP       = 0;
    public static int SENSOR_TEMP_BED   = 0;

    // time
    public static String s_time = "00:00";
    public static String s_time_gap_t = "00:00";

    public static String s_time_gap_n = "00:00";
    public static String m_back_clock = "00:00";
    public static String m_gap_clock = "00:00";
    public static boolean m_gap_clock_f = true;

    public static int gap_t = 0;
    public static int gap_m = 0;

    public static boolean up = true;
    // 설정한 시간이 크면 true 작으면 false

    static SharedPreferences sharedPreferences;

    // DB name
    public final static String NAME_OF_SHARED_PREF = "myData";

    // 대기모드 산소농도 / 수압세기 / 사용시간 값
    public final static String VAL_OXYGEN   = "val_oxygen";
    public final static String VAL_PRESSURE = "val_pressure";
    public final static String VAL_TIME     = "val_time";

    // 시간차이
    public final static String TIME_GAP = "time_gap";
    public final static String TIME_GAP_F = "time_gap_f";

    // 대기모드 동작시간
   // public final static String WAITING_WORKING_TIME = "waiting_working_time";

    // 라이브러리 20개중 선택된 값
    public final static int MAX_CHECKED = 4;
    public final static String LIBRARY_LOC_  = "library_location_";

    // 매뉴얼 모드 man_pattern_*_*
    public final static String MANUAL_MODE_PATTERN_ = "man_pattern_"; // man_pattern_(1-5)_(
    public final static String MANUAL_MODE_TIME_ = "man_time_";
    public final static String MANUAL_MODE_SECTION_MIN_ = "man_section_min_";
    public final static String MANUAL_MODE_SECTION_MAX_ = "man_section_max_"; //man
    // 언어 [KOR, ENG, CHI]
    public static int LANGUAGE = 0;

    // 세팅 on/off값
    public final static String SETTING_ONOFF_VAL_ = "setting_onoff_val_";

    // RFID모드 on/off값
    public final static String RFID_ONOFF = "rfid_onoff";

    // 사운드 id
    public final static int NUM_OF_LANG = 3;
    public final static int NUM_OF_SOUND = 5;
    public final static int[] ID_KOR = new int[5];
    public final static int[] ID_ENG = new int[5];
    public final static int[] ID_CHI = new int[5];
    public final static int[][] ID_LANG_SOUND = new int[NUM_OF_LANG][NUM_OF_SOUND];

    public static int[] t_flag = {0,0,0,0};

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

    public static String m_time = "start";

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

        //시간차 저장
        SharedPreferences sharedPreferences = context.getSharedPreferences(NAME_OF_SHARED_PREF, 0);
        m_gap_clock = sharedPreferences.getString(TIME_GAP,"00:00");
        m_gap_clock_f = sharedPreferences.getBoolean(TIME_GAP_F,true);
        Log.v("ss","m_gap_clock : "+m_gap_clock);
        Log.v("ss","m_gap_clock_f : "+m_gap_clock_f);


        // 어플 시작 시 이전 time gap 가져와서 변수에 넣기
    }

    synchronized public static String getTime_gap_t(){
        return s_time_gap_t;
    }

    synchronized public static int getTime_gap_tt(){
        return gap_t;
    }
    synchronized public static int getTime_gap_mm(){
        return gap_m;
    }

    // 설정한 시간이 크면 true 작으면 false
    synchronized public static boolean getTime_gap_up(){
        return up;
    }

    // 이전 시간
    synchronized public static String getTime_gap_n(){
        return s_time_gap_n;
    }

    synchronized public static String getText(){

        String doTime_tt;
        String doTime_mm;

        long r_time = System.currentTimeMillis();
        //DateFormat df = new SimpleDateFormat("HH:mm:ss");
        int r_t = (int)((r_time/1000/60/60)%24)+9;
        int r_m = (int)((r_time/1000/60)%60);
        if(r_t >= 24)
        {
            r_t = r_t-24;
        }

        Log.v("sb","r_t : "+r_t);
        Log.v("sb","r_m : "+r_m);

        if(r_t < 10){
            doTime_tt = "0"+String.valueOf(r_t);
        }
        else{
            doTime_tt = String.valueOf(r_t);
        }

        if(r_m < 10){
            doTime_mm = "0"+String.valueOf(r_m);
        }
        else{
            doTime_mm = String.valueOf(r_m);
        }

        m_back_clock = doTime_tt+":"+doTime_mm;

        return m_back_clock;
    }

    // 설정 시간
    synchronized public static String getTime(){
        return s_time;
    }

    synchronized public static void setTime(String n_time){
        s_time = n_time;
        Log.v("sb","n_time : "+n_time);
        Log.v("sb","s_time : "+s_time);
        doCalculation_gap();
    }
    // 시간 차이 계산 및 DB 저장
    public static void doCalculation_gap()
    {
        String ss_time = getText();
        // 시간차 구하기
        Log.v("ss","s_time : "+s_time);
        Log.v("ss","ss_time : "+ss_time);
        String a = s_time.substring(0,2);
        String b = s_time.substring(3,5);
        int a_ = Integer.parseInt(a);
        int b_ = Integer.parseInt(b);

        String aa = ss_time.substring(0,2);
        String bb = ss_time.substring(3,5);
        int aa_ = Integer.parseInt(aa);
        int bb_ = Integer.parseInt(bb);

        String gap_buf_t;
        String gap_buf_m;

        if(a_ >= aa_){
            //설정한 시간이 더 크다
            up = true;
            gap_t = a_-aa_;
            if(b_ > bb_){
                gap_m = b_-bb_;
            }
            else{
                gap_t = gap_t - 1;
                gap_m = 60 - (bb_-b_);
            }
        }
        else{
            // 설정한 시간이 더 작다
            up = false;
            gap_t = aa_-a_;
            if(b_ > bb_){
                gap_t = gap_t - 1;
                gap_m = 60 - (b_-bb_);
            }
            else{
                gap_m = bb_-b_;
            }
        }

        if(gap_t < 10){
            gap_buf_t = "0"+String.valueOf(gap_t);
        }
        else{
            gap_buf_t = String.valueOf(gap_t);
        }

        if(gap_m < 10){
            gap_buf_m = "0"+String.valueOf(gap_m);
        }
        else{
            gap_buf_m = String.valueOf(gap_m);
        }

        s_time_gap_t = gap_buf_t+":"+gap_buf_m;
        Log.v("ss","gap_buf : "+s_time_gap_t + up);

        int g_buf;

        sharedPreferences = context.getSharedPreferences(Application_manager.NAME_OF_SHARED_PREF, 0);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        // 설정한 시간이 더 크다
        if(up == true)
        {
            g_buf = (gap_t*60)+gap_m;
        }
        else
        {
            g_buf = -(gap_t*60)+gap_m;
        }

        m_gap_clock = s_time_gap_t;
        m_gap_clock_f = up;
        editor.putString(TIME_GAP, s_time_gap_t);
        editor.putBoolean(TIME_GAP_F, up);
        editor.commit();
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
