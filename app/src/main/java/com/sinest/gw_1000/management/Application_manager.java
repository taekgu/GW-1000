package com.sinest.gw_1000.management;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.PowerManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;

import com.sinest.gw_1000.R;
import com.sinest.gw_1000.communication.Communicator;
import com.sinest.gw_1000.setting.Activity_setting;

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

    public static String m_password = "0000";

    public static int gap_t = 0;
    public static int gap_m = 0;

    public static boolean up = true;
    // 설정한 시간이 크면 true 작으면 false

    static SharedPreferences sharedPreferences;

    // DB name
    public final static String DB_NAME = "myData";

    // 대기모드 산소농도 / 수압세기 / 사용시간 값
    public final static String DB_VAL_OXYGEN = "val_oxygen";
    public final static String DB_VAL_PRESSURE = "val_pressure";
    public final static String DB_VAL_TIME = "val_time";

    // 시간차이
    public final static String DB_TIME_GAP = "time_gap";
    public final static String DB_TIME_GAP_F = "time_gap_f";

    //비밀번호
    public final static String DB_PASSWORD = "password";

    //EMOTION
    public final static String DB_EMOTION1 = "emotion1";
    public final static String DB_EMOTION2 = "emotion2";
    public final static String DB_EMOTION3 = "emotion3";
    public final static String DB_EMOTION4 = "emotion4";

    //WATER
    public final static String DB_WATER_F = "water_f";
    public final static String DB_WATER_FF = "water_ff";
    public final static String DB_WATER_ST = "water_st";
    public final static String DB_WATER_FT = "water_ft";

    //Extern_led
    public final static String DB_EXTERN_LED = "extern_led";

    //DB_PAUSE
    public final static String DB_PAUSE = "pause";

    //DB_LANGUEAGE
    public final static String DB_LANGUEAGE = "language";

    //DB_INVERTER
    public final static String DB_INVERTER = "inverter";

    //DB_RUNNING_TIME
    public final static String DB_RUNNING_TIME = "runnig_time";

    //DB_VOLUME
    public final static String DB_VOLUME = "volume";

    // 대기모드 동작시간
    // public final static String WAITING_WORKING_TIME = "waiting_working_time";

    // 라이브러리 20개중 선택된 값
    public final static int MAX_CHECKED = 4;
    public final static String DB_LIBRARY_LOC_ = "library_location_";

    // 매뉴얼 모드 man_pattern_*_*
    public final static String DB_MANUAL_MODE_PATTERN_ = "man_pattern_"; // man_pattern_(1-5)_(
    public final static String DB_MANUAL_MODE_TIME_ = "man_time_";
    public final static String DB_MANUAL_MODE_SECTION_MIN_ = "man_section_min_";
    public final static String DB_MANUAL_MODE_SECTION_MAX_ = "man_section_max_"; //man

    // 세팅 on/off값
    public final static String DB_SETTING_ONOFF_VAL_ = "setting_onoff_val_";

    // RFID모드 on/off값
    public final static String DB_RFID_ONOFF = "rfid_onoff";

    //sleep mode flag
    public final static String DB_SLEEP_M = "sleep_m";

    // 내부 온도 및 수온
    public final static String DB_TEMPERATURE = "temp_above";
    public final static String DB_TEMPERATURE_BED = "temp_below";

    // 사운드 id
    public final static int NUM_OF_LANG = 3;
    public final static int NUM_OF_SOUND = 5;
    public final static int[] ID_KOR = new int[5];
    public final static int[] ID_ENG = new int[5];
    public final static int[] ID_CHI = new int[5];
    public final static int[][] ID_LANG_SOUND = new int[NUM_OF_LANG][NUM_OF_SOUND];
    public final static MediaPlayer[][] mediaPlayer = new MediaPlayer[NUM_OF_LANG][NUM_OF_SOUND];
    public static MediaPlayer mediaPlayer_therapy;

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

    private static final String TAG = "AlarmWakeLock";
    public static PowerManager.WakeLock mWakeLock;

    //emotion
    public static int led_mode_num = 0;
    public static int led_bright_num = 1;
    public static int sound_mode_num = 0;
    public static int sound_volume_num = 1;

    //Volume
    public static int m_volume = 0;

    // 언어 [KOR, ENG, CHI]
    public static int m_language = 0;

    //Inverter 0-> 0 1-> 50 2-> 100
    public static int m_inverter = 0;

    //External_led
    public static int m_external_led = 0;

    //Water HeaterTimer
    public static boolean m_water_heater_time_save = false;
    public static boolean m_water_heater_f = false;
    public static String m_water_heater_time_stime = "00:00";
    public static String m_water_heater_time_ftime = "00:00";

    //Pause Rotation
    public static boolean m_pause_rotation = false;

    //Operation time
    public static int m_operation_time = 0;

    // rfid_pass_f
    public static boolean rfid_pass_f = true;
    public static boolean rfid_pass_f2 = false;

    private static int start_m = 0;
    private static int end_m = 0;
    private static boolean m_sleep_f = false;
    public static int m_sleep_ff = 3;
    public static boolean m_operation_f = false;

    //-------------------------------Img ---------------------------------------------------
    // 0-> 한국,영어 1-> 중국
    //setting
    public static int img_flag = 0;
    public static int[] button_on = {R.drawable.button_on, R.drawable.button_on};//
    public static int[] button_off = {R.drawable.button_off, R.drawable.button_off_ch};
    public static int[] on = {R.drawable.on, R.drawable.on_ch};
    public static int[] off = {R.drawable.off, R.drawable.off_ch};
    public static int[] button_play_on = {R.drawable.button_play_on, R.drawable.button_play_on};//
    public static int[] button_circle_back_on = {R.drawable.button_circle_back_on, R.drawable.button_circle_back_on_ch};
    public static int[] button_circle_back_off = {R.drawable.button_circle_back_off, R.drawable.button_circle_back_off_ch};
    public static int[] sleepmode_1min_on = {R.drawable.sleepmode_1min_on, R.drawable.sleepmode_1min_on_ch};
    public static int[] sleepmode_3min_on = {R.drawable.sleepmode_3min_on, R.drawable.sleepmode_3min_on_ch};
    public static int[] sleepmode_5min_on = {R.drawable.sleepmode_5min_on, R.drawable.sleepmode_5min_on_ch};
    public static int[] sleepmode_continue_on = {R.drawable.sleepmode_continue_on, R.drawable.sleepmode_continue_on_ch};
    public static int[] sleepmode_1min = {R.drawable.sleepmode_1min, R.drawable.sleepmode_1min_ch};
    public static int[] sleepmode_3min = {R.drawable.sleepmode_3min, R.drawable.sleepmode_3min_ch};
    public static int[] sleepmode_5min = {R.drawable.sleepmode_5min, R.drawable.sleepmode_5min_ch};
    public static int[] sleepmode_continue_off = {R.drawable.sleepmode_continue_off, R.drawable.sleepmode_continue_off_ch};
    public static int[] emotion_on = {R.drawable.emotion_on, R.drawable.emotion_on_ch};
    public static int[] emotion_off = {R.drawable.emotion_off, R.drawable.emotion_off_ch};
    public static int[] language_ch = {R.drawable.language_ch, R.drawable.language_ch_ch};
    public static int[] setting_back_image = {R.drawable.setting_back_image, R.drawable.setting_back_image_ch};

    //setting pop
    public static int[] check_on = {R.drawable.check_on, R.drawable.check_on_ch};
    public static int[] check_off = {R.drawable.check_off, R.drawable.check_off_ch};
    public static int[] save_setting_on = {R.drawable.save_setting_on, R.drawable.save_setting_on_ch};
    public static int[] save_setting_off = {R.drawable.save_setting_off, R.drawable.save_setting_off_ch};
    public static int[] button_elipse_back_on = {R.drawable.button_elipse_back_on, R.drawable.button_elipse_back_on_ch};
    public static int[] button_elipse_back_off = {R.drawable.button_elipse_back_off, R.drawable.button_elipse_back_off_ch};
    // 저장버튼 추가 요청및 추가해야함  ,  rfid 쪽 안함 ,  팝업 배경 받고 진행

    //engine
    public static int[] engineermode_back_image = {R.drawable.engineermode_back_image, R.drawable.engineermode_back_image_ch};
    public static int[] door_open_on = {R.drawable.door_open_on, R.drawable.door_open_on_ch};
    public static int[] door_open_off = {R.drawable.door_open_off, R.drawable.door_open_off_ch};
    public static int[] door_close_on = {R.drawable.door_close_on, R.drawable.door_close_on_ch};
    public static int[] door_close_off = {R.drawable.door_close_off, R.drawable.door_close_off_ch};
    public static int[] inverter_ls = {R.drawable.inverter_ls, R.drawable.inverter_ls_ch};
    public static int[] inverter_ys = {R.drawable.inverter_ys, R.drawable.inverter_ys_ch};
    public static int[] oxygen_1step_on = {R.drawable.oxygen_1step_on, R.drawable.oxygen_1step_on_ch};
    public static int[] oxygen_1step_off = {R.drawable.oxygen_1step_off, R.drawable.oxygen_1step_off_ch};
    public static int[] oxygen_2step_on = {R.drawable.oxygen_2step_on, R.drawable.oxygen_2step_on_ch};
    public static int[] oxygen_2step_off = {R.drawable.oxygen_2step_off, R.drawable.oxygen_2step_off_ch};
    public static int[] oxygen_3step_on = {R.drawable.oxygen_3step_on, R.drawable.oxygen_3step_on_ch};
    public static int[] oxygen_3step_off = {R.drawable.oxygen_3step_off, R.drawable.oxygen_3step_off_ch};
    public static int[] oxygen_4step_on = {R.drawable.oxygen_4step_on, R.drawable.oxygen_4step_on_ch};
    public static int[] oxygen_4step_off = {R.drawable.oxygen_4step_off, R.drawable.oxygen_4step_off_ch};
    public static int[] oxygen_5step_on = {R.drawable.oxygen_5step_on, R.drawable.oxygen_5step_on_ch};
    public static int[] oxygen_5step_off = {R.drawable.oxygen_5step_off, R.drawable.oxygen_5step_off_ch};
    public static int[] program_mode_on = {R.drawable.program_mode_on, R.drawable.program_mode_on_ch};
    public static int[] program_mode_off = {R.drawable.program_mode_off, R.drawable.program_mode_off_ch};


    //working

    //waiting
    public static int[] waiting_doorclose_backimage = {R.drawable.waiting_doorclose_backimage, R.drawable.waiting_doorclose_backimage_ch};
    public static int[] waiting_dooropen_backimage = {R.drawable.waiting_dooropen_backimage, R.drawable.waiting_dooropen_backimage_ch};

    //waiting rfid
    public static int[] waiting_rfid_doorclose_back = {R.drawable.waiting_rfid_doorclose_back, R.drawable.waiting_rfid_doorclose_back_ch};
    public static int[] waiting_rfid_dooropen_back = {R.drawable.waiting_rfid_dooropen_back, R.drawable.waiting_rfid_dooropen_back_ch};


    //library
    public static int[] ribrary_back_image = {R.drawable.ribrary_back_image, R.drawable.ribrary_back_image_ch};





    //-------------------------------Img ---------------------------------------------------

    public void onCreate() {

        Application_manager.context = getApplicationContext();
        Application_manager.communicator = new Communicator(context);
        Application_manager.soundManager = new SoundManager(context);

        // 기기 DPI 출력
        DisplayMetrics metrics = new DisplayMetrics();
        WindowManager mgr = (WindowManager)context.getSystemService(Context.WINDOW_SERVICE);
        mgr.getDefaultDisplay().getMetrics(metrics);
        Log.i("JW", "densityDPI = " + metrics.densityDpi);

        //시간차 저장
        SharedPreferences sharedPreferences = context.getSharedPreferences(DB_NAME, 0);
        //m_gap_clock = sharedPreferences.getString(DB_TIME_GAP,"00:00");
        m_gap_clock_f = sharedPreferences.getBoolean(DB_TIME_GAP_F,true);
        Log.v("ss","m_gap_clock : "+m_gap_clock);
        Log.v("ss","m_gap_clock_f : "+m_gap_clock_f);

        // 러닝타임 측정
        runningTime = sharedPreferences.getInt(DB_RUNNING_TIME,0);
        Log.v("sss","START : "+runningTime);
        setThread_runningTime();
        isRun = true;
        thread_runningTime.start();


        //emotion 저장
        led_mode_num = sharedPreferences.getInt(DB_EMOTION1,0);
        led_bright_num = sharedPreferences.getInt(DB_EMOTION2,1);
        sound_mode_num = sharedPreferences.getInt(DB_EMOTION3,0);
        sound_volume_num = sharedPreferences.getInt(DB_EMOTION4,1);

        //water_heater_time_save
        m_water_heater_time_save = sharedPreferences.getBoolean(DB_WATER_F,false);
        m_water_heater_f = sharedPreferences.getBoolean(DB_WATER_FF,false);
        m_water_heater_time_stime = sharedPreferences.getString(DB_WATER_ST,"00:00");
        m_water_heater_time_ftime = sharedPreferences.getString(DB_WATER_FT,"00:00");

        //External_led
        m_external_led = sharedPreferences.getInt(DB_EXTERN_LED,0);

        //Rause Rotation
        m_pause_rotation = sharedPreferences.getBoolean(DB_PAUSE,false);

        //DB_LANGUEAGE
        m_language = sharedPreferences.getInt(DB_LANGUEAGE,0);

        //DB_INVERTER
        m_inverter = sharedPreferences.getInt(DB_INVERTER,0);

        //DB_VOLUME
        m_volume = sharedPreferences.getInt(DB_VOLUME,0);

        // 어플 시작 시 이전 time gap 가져와서 변수에 넣기

        //sleep mode flag
        m_sleep_ff = sharedPreferences.getInt(DB_SLEEP_M,3);

        if(m_language == 2){
            img_flag = 1;
        }else{
            img_flag = 0;
        }
    }

    synchronized public static void set_m_start_sleep(int i){
        sharedPreferences = context.getSharedPreferences(Application_manager.DB_NAME, 0);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        start_m = i;
    }

    synchronized public static void set_m_sleep_m(int i){
        sharedPreferences = context.getSharedPreferences(Application_manager.DB_NAME, 0);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(DB_SLEEP_M, i);
        editor.commit();
        m_sleep_ff = i;
    }

    synchronized public static void set_m_volume(int i){
        sharedPreferences = context.getSharedPreferences(Application_manager.DB_NAME, 0);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(DB_VOLUME, i);
        editor.commit();
        m_volume = i;
    }

    synchronized public static void set_m_language(int i){
        sharedPreferences = context.getSharedPreferences(Application_manager.DB_NAME, 0);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(DB_LANGUEAGE, i);
        editor.commit();
        m_language = i;
        if(m_language == 2){
            img_flag = 1;
        }else{
            img_flag = 0;
        }
    }

    synchronized public static void set_m_inverter(int i){
        sharedPreferences = context.getSharedPreferences(Application_manager.DB_NAME, 0);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(DB_INVERTER, i);
        editor.commit();
        m_inverter = i;
    }

    synchronized public static void set_m_pause_rotation(boolean i){
        sharedPreferences = context.getSharedPreferences(Application_manager.DB_NAME, 0);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(DB_PAUSE, i);
        editor.commit();
        m_pause_rotation = i;
    }

    synchronized public static void set_m_external_led(int i){
        sharedPreferences = context.getSharedPreferences(Application_manager.DB_NAME, 0);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(DB_EXTERN_LED, i);
        editor.commit();
        m_external_led = i;
    }

    synchronized public static void set_m_water_ftime(String ft){
        sharedPreferences = context.getSharedPreferences(Application_manager.DB_NAME, 0);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(DB_WATER_FT, ft);
        editor.commit();
        m_water_heater_time_ftime = ft;
    }

    synchronized public static void set_m_water_stime(String st){
        sharedPreferences = context.getSharedPreferences(Application_manager.DB_NAME, 0);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(DB_WATER_ST, st);
        editor.commit();
        m_water_heater_time_stime = st;
    }

    synchronized public static void set_m_water_ff(boolean flag){
        sharedPreferences = context.getSharedPreferences(Application_manager.DB_NAME, 0);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(DB_WATER_FF, flag);
        editor.commit();
        m_water_heater_f = flag;
    }

    synchronized public static void set_m_water_f(boolean flag){
        sharedPreferences = context.getSharedPreferences(Application_manager.DB_NAME, 0);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(DB_WATER_F, flag);
        editor.commit();
        m_water_heater_time_save = flag;
    }

    synchronized public static void set_m_emotion(int e1,int e2,int e3,int e4){
        sharedPreferences = context.getSharedPreferences(Application_manager.DB_NAME, 0);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(DB_EMOTION1, e1);
        editor.putInt(DB_EMOTION2, e2);
        editor.putInt(DB_EMOTION3, e3);
        editor.putInt(DB_EMOTION4, e4);
        editor.commit();
        led_mode_num = e1;
        led_bright_num = e2;
        sound_mode_num = e3;
        sound_volume_num = e4;
    }

    synchronized public static String get_m_password(){
        SharedPreferences sharedPreferences = context.getSharedPreferences(DB_NAME, 0);
        m_password = sharedPreferences.getString(DB_PASSWORD,"0000");
        return m_password;
    }

    synchronized public static void set_m_password(String pw){
        sharedPreferences = context.getSharedPreferences(Application_manager.DB_NAME, 0);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(DB_PASSWORD, pw);
        editor.commit();
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
        //설정 시간
        String a = s_time.substring(0,2);
        String b = s_time.substring(3,5);
        int a_ = Integer.parseInt(a);
        int b_ = Integer.parseInt(b);

        //현재 시간
        String aa = ss_time.substring(0,2);
        String bb = ss_time.substring(3,5);
        int aa_ = Integer.parseInt(aa);
        int bb_ = Integer.parseInt(bb);

        String gap_buf_t;
        String gap_buf_m;

        if(a_ > aa_){
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
        }else if(a_ == aa_){
            //시간은 같다
            gap_t = a_-aa_;
            // 설정 분이 크다
            if(b_ >= bb_){
                up = true;
                gap_m = b_-bb_;
            }//설정 분이 작다
            else{
                up = false;
                gap_m = (bb_-b_);
            }

        }else{
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

        sharedPreferences = context.getSharedPreferences(Application_manager.DB_NAME, 0);
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
        editor.putString(DB_TIME_GAP, s_time_gap_t);
        editor.putBoolean(DB_TIME_GAP_F, up);
        editor.commit();
    }


    public static String doInit_time()
    {
        String p_time = Application_manager.getText();
        String g_time = Application_manager.m_gap_clock;
        boolean t_f = Application_manager.m_gap_clock_f;

        Log.v("ss","p_time : "+p_time);
        Log.v("ss","g_time : "+g_time);

        String aa = p_time.substring(0,2);
        String bb = p_time.substring(3,5);
        int p_time_t = Integer.parseInt(aa);
        int p_time_m = Integer.parseInt(bb);

        String ga = g_time.substring(0,2);
        String gb = g_time.substring(3,5);
        int g_time_t = Integer.parseInt(ga);
        int g_time_m = Integer.parseInt(gb);

        int t = 0;
        int m = 0;

        // +
        if(t_f){
            t = p_time_t + g_time_t;
            m = p_time_m + g_time_m;
            // -
        }else{
            if(p_time_t > g_time_t & p_time_m < g_time_m){
                t = (p_time_t - g_time_t) - 1;
                m = 60 + p_time_m - g_time_m;
            }
            else{
                t = p_time_t - g_time_t;
                m = p_time_m - g_time_m;
            }
        }

        if(m >= 60){
            t = t + 1;
            m = m - 60;
        }
        if(t >= 24 ){
            t = t - 24;
        }

        String doTime_t;
        String doTime_m;

        if(t < 10){
            doTime_t = "0"+String.valueOf(t);
        }
        else{
            doTime_t = String.valueOf(t);
        }

        if(m < 10){
            doTime_m = "0"+String.valueOf(m);
        }
        else{
            doTime_m = String.valueOf(m);
        }

        String doTime = doTime_t+":"+doTime_m;

        Log.v("ss","doTime : "+ doTime);

        return doTime;
        //clock.setText(doTime);
    }


    synchronized public static void wakeLock(Context context){
        if(mWakeLock != null){
            return;
        }
        PowerManager powerManager = (PowerManager)context.getSystemService(Context.POWER_SERVICE);
        mWakeLock = powerManager.newWakeLock(PowerManager.FULL_WAKE_LOCK,TAG);
        mWakeLock.acquire();
        Log.v("mm","2");
    }

    synchronized public static void releaseWakeLock() {
        if(mWakeLock != null){
            mWakeLock.release();
            mWakeLock = null;
            Log.v("mm","1");
        }
    }

    synchronized public static void save_Running_time(){
        sharedPreferences = context.getSharedPreferences(Application_manager.DB_NAME, 0);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(DB_RUNNING_TIME, runningTime);
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

                        if(m_operation_f == true){
                            runningTime++;
                            save_Running_time();
                            Log.v("sss","STOP : "+runningTime);
                        }

                        if(m_sleep_f == true){
                            start_m++;
                            if(start_m == end_m){
                                Activity_setting.devicePolicyManager.lockNow();
                            }
                            Log.v("sb_test","start_m : "+start_m+"  end_m : "+end_m);
                        }
                    }
                    catch (Exception e) {

                    }
                }
            }
        });
    }

    synchronized public static void setSleep(int start, int end, boolean f) {
        m_sleep_f = f;
        start_m = start;
        end_m = end;
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

    private void initImg(){

    }

    // 언어 이미지 변경
    synchronized public static void setLangueage() {

    }
}