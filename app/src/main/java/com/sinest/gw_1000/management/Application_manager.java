package com.sinest.gw_1000.management;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.Application;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
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
import com.sinest.gw_1000.splash.Activity_booting;

import java.lang.Thread.UncaughtExceptionHandler;

/**
 * Created by Jinwook on 2016-11-20.
 *
 * 액티비티에서 공용으로 사용되는 자원 관리
 */

public class Application_manager extends Application {

    // 동작 중인지 확인 flag
    public static boolean working_flag = false;

    // 설정된 water time에 포함되어 있는지 확인 flag
    public static boolean water_time_flag = false;

    // 도어 상태
    public static boolean isDoorOpened = false;
    public static final String DB_DOOR_STATE = "door_state";

    // 인버터 타입 false: 야스카와 / true: LS
    public static boolean inverterType = false;
    public static byte inverterVal = 0x00; // LS: 0x10, 야스카와: 0x00
    public static final String DB_INVERT_TYPE = "invert_type";

    // 활성 액티비티 대기모드인지 엔지니어모드인지 확인
    private static boolean isEngineerMode = false;
    synchronized public static boolean getIsEngineerMode() {

        return isEngineerMode;
    }
    synchronized public static void setIsEngineerMode(boolean val) {

        isEngineerMode = val;
    }

    // WIFI 연결 유/무
    private static boolean isConnected_wifi = false;
    synchronized public static boolean getIsConnected_wifi() {

        return isConnected_wifi;
    }
    synchronized public static void setIsConnected_wifi(boolean val) {

        isConnected_wifi = val;
    }

    // RFID - 일반 대기 모드 전환시 센서값 저장
    public static int SENSOR_HUMIDITY   = 0;
    public static int SENSOR_OXYGEN     = 0;
    public static int SENSOR_TEMP       = 0;
    public static int SENSOR_TEMP_BED   = 0;

    // 사용자 설정 온도 값
    public static int SENSOR_TEMP_USER      = 0;
    public static int SENSOR_TEMP_BED_USER  = 0;

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

    // 내부 DB
    private static SharedPreferences sharedPreferences = null;
    public static SharedPreferences getSharedPreferences() {

        return sharedPreferences;
    }
    public final static String DB_NAME = "myData";

    // 대기모드 산소농도 / 수압세기 / 사용시간 값
    public final static String DB_VAL_OXYGEN = "val_oxygen";
    public final static String DB_VAL_OXYGEN_SPRAY = "val_oxygen_spray";
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

    // 라이브러리 20개중 선택된 값
    public final static int MAX_CHECKED = 4;
    public final static String DB_LIBRARY_LOC_ = "library_location_";

    // 매뉴얼 모드 man_pattern_*_*
    public final static String DB_MANUAL_MODE_PATTERN_ = "man_pattern_"; // man_pattern_(1-5)_(0-3)
    public final static String DB_MANUAL_MODE_TIME_ = "man_time_";
    public final static String DB_MANUAL_MODE_SECTION_MIN_ = "man_section_min_";
    public final static String DB_MANUAL_MODE_SECTION_MAX_ = "man_section_max_"; //man

    // 세팅 on/off값
    public final static String DB_SETTING_ONOFF_VAL_ = "setting_onoff_val_";

    // RFID모드 on/off값
    public final static String DB_RFID_ONOFF = "rfid_onoff";
    public final static String RFID_ON_F = "rfid_on_f";

    //sleep mode flag
    public final static String DB_SLEEP_M = "sleep_m";

    // 내부 온도 및 수온
    public final static String DB_TEMPERATURE_USER = "temp_above_user";
    public final static String DB_TEMPERATURE_BED_USER = "temp_below_user";

    // GW-1000 모드 확인 Key
    public final static String GW_1000 = "GW_1000";

    // 사운드 id
    public final static int NUM_OF_LANG = 3;
    public final static int NUM_OF_SOUND = 5;
    public final static MediaPlayer[][] mediaPlayer = new MediaPlayer[NUM_OF_LANG][NUM_OF_SOUND];

    // App context
    private static Context context;
    public static Context getContext() { return context; }

    // Communicator
    private static Communicator communicator;

    // SoundManager
    private static SoundManager soundManager;

    // ToastManager
    private static ToastManager toastManager;
    public static ToastManager getToastManager() { return toastManager; }

    // 앱 동작 시간
    private boolean isRun = false;
    private static int runningTime = 0;
    private Thread thread_runningTime;

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

    // sleep 모드
    private static int start_m = 0;
    private static int end_m = 0;
    private static boolean m_sleep_f = false;
    public static int m_sleep_ff = 3;
    public static boolean m_operation_f = false;
    public final static String DB_SLEEP_MIN = "db_sleep_min";

    //GW-1000H / GW-1000L    true -> H    false -> L
    public static boolean gw_1000 = true;

    //-------------------------------Img ---------------------------------------------------
    // 0-> 한국,영어 1-> 중국
    //setting
    public static int img_flag = 0;
    public static int[] button_on = {R.drawable.button_on, R.drawable.button_on};//
    public static int[] button_off = {R.drawable.button_off, R.drawable.button_off_ch};
    public static int[] on = {R.drawable.on, R.drawable.on_ch};
    public static int[] off = {R.drawable.off, R.drawable.off_ch};
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
    public static int[] inverter_0 = {R.drawable.inverter_0, R.drawable.inverter_0_ch};
    public static int[] inverter_50 = {R.drawable.inverter_50, R.drawable.inverter_50_ch};
    public static int[] inverter_100 = {R.drawable.inverter_100, R.drawable.inverter_100_ch};

    //setting pop
    public static int[] check_on = {R.drawable.check_on, R.drawable.check_on_ch};
    public static int[] check_off = {R.drawable.check_off, R.drawable.check_off_ch};
    public static int[] save_on = {R.drawable.save_on, R.drawable.save_on_ch};
    public static int[] save_off = {R.drawable.save_off, R.drawable.save_off_ch};
    public static int[] save_setting_on = {R.drawable.save_setting_on, R.drawable.save_setting_on_ch};
    public static int[] save_setting_off = {R.drawable.save_setting_off, R.drawable.save_setting_off_ch};
    public static int[] button_elipse_back_on = {R.drawable.button_elipse_back_on, R.drawable.button_elipse_back_on_ch};
    public static int[] button_elipse_back_off = {R.drawable.button_elipse_back_off, R.drawable.button_elipse_back_off_ch};
    public static int[] emotionpopup = {R.drawable.emotionpopup, R.drawable.emotionpopup_ch};
    public static int[] rfid_working_popup = {R.drawable.rfid_working_popup, R.drawable.rfid_working_popup_ch};
    public static int[] water_heater_timer_popup = {R.drawable.water_heater_timer_popup, R.drawable.water_heater_timer_popup_ch};
    public static int[] rfid_password_popup = {R.drawable.rfid_password_popup, R.drawable.rfid_password_popup_ch};
    public static int[] keypad_enter = {R.drawable.keypad_enter, R.drawable.keypad_enter_ch};
    public static int[] keypad_back = {R.drawable.keypad_back, R.drawable.keypad_back_ch};
    public static int[] keypad_change = {R.drawable.keypad_change, R.drawable.keypad_change_ch};
    public static int[] keypad_delete = {R.drawable.keypad_delete, R.drawable.keypad_delete_ch};
    public static int[] time_keypad = {R.drawable.time_keypad, R.drawable.time_keypad_ch};
    public static int[] water_heater_finishtimer_keyped = {R.drawable.water_heater_finishtimer_keyped, R.drawable.water_heater_finishtimer_keyped_ch};
    public static int[] water_heater_start_timer_keyped = {R.drawable.water_heater_start_timer_keyped, R.drawable.water_heater_start_timer_keyped_ch};

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

    //waiting
    public static int[] waiting_backimage = {R.drawable.workingmotion0, R.drawable.workingmotion0_ch};

    //library
    public static int[] ribrary_back_image = {R.drawable.ribrary_back_image, R.drawable.ribrary_back_image_ch};
    public static int[] library_setting_off = {R.drawable.library_setting_off, R.drawable.library_setting_off_ch};
    public static int[] library_setting_on = {R.drawable.library_setting_on, R.drawable.library_setting_on_ch};
    public static int[] save_mode_off = {R.drawable.save_mode_off, R.drawable.save_mode_off_ch};
    public static int[] save_mode_on = {R.drawable.save_mode_on, R.drawable.save_mode_on_ch};
    public static int[] manual_mode_setting_backimage = {R.drawable.manual_mode_setting_backimage, R.drawable.manual_mode_setting_backimage_ch};

    public static String s_time_buf = "00:00";
    public static String f_time_buf = "00:00";
    public static int time_buf_f = 1;
    public static int rfid_on_f = 0;

    // 비정상 종료 플래그 및 비정상 종료 처리 핸들러
    private static boolean isTerminated_by_uncaughtException = false;
    public final static String DB_IS_TERMINATED = "is_terminated";
    private UncaughtExceptionHandler mUncaughtExceptionHandler;

    public void onCreate() {

        sharedPreferences = getSharedPreferences(DB_NAME, 0);

        mUncaughtExceptionHandler = Thread.getDefaultUncaughtExceptionHandler();
        Thread.setDefaultUncaughtExceptionHandler(new UncaughtExceptionHandler() {
            @Override
            public void uncaughtException(Thread thread, Throwable throwable) {

                //예외상황이 발행 되는 경우 작업
                isTerminated_by_uncaughtException = true;
                sharedPreferences = getSharedPreferences(DB_NAME, 0);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putBoolean(DB_IS_TERMINATED, isTerminated_by_uncaughtException);
                editor.commit();

                // 재시작 알람 등록
                Intent intent = new Intent(getApplicationContext(), Activity_booting.class);
                PendingIntent i = PendingIntent.getActivity(getApplicationContext(), 0, intent, 0);

                AlarmManager am = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                am.set(AlarmManager.RTC, System.currentTimeMillis() + 5000, i);

                // 프로세스 종료
                //System.exit(2)
                android.os.Process.killProcess(android.os.Process.myPid());

                //예외처리를 하지 않고 DefaultUncaughtException으로 넘긴다.
                //mUncaughtExceptionHandler.uncaughtException(thread, throwable);
            }
        });

        Application_manager.context = getApplicationContext();
        Application_manager.communicator = new Communicator(context);
        Application_manager.soundManager = new SoundManager(context);
        Application_manager.toastManager = new ToastManager();

        // 기기 DPI 출력
        DisplayMetrics metrics = new DisplayMetrics();
        WindowManager mgr = (WindowManager)context.getSystemService(Context.WINDOW_SERVICE);
        mgr.getDefaultDisplay().getMetrics(metrics);

        //시간차 저장
        sharedPreferences = context.getSharedPreferences(DB_NAME, 0);
        m_gap_clock_f = sharedPreferences.getBoolean(DB_TIME_GAP_F,true);

        //rfid
        rfid_on_f = sharedPreferences.getInt(RFID_ON_F,0);

        // 러닝타임 측정
        runningTime = sharedPreferences.getInt(DB_RUNNING_TIME,0);
        setThread_runningTime();
        isRun = true;
        thread_runningTime.start();

        load_data();

        if (gw_1000 == false) {
            // GW-1000L 버전 설정
            setting_back_image[0] = R.drawable.setting_back_image_l;
            setting_back_image[1] = R.drawable.setting_back_image_l_ch;

            waiting_backimage[0] = R.drawable.waiting_backimage_l;
            waiting_backimage[1] = R.drawable.waiting_backimage_l_ch;

        } else {
            // GW-1000H 버전 설정
            setting_back_image[0] = R.drawable.setting_back_image;
            setting_back_image[1] = R.drawable.setting_back_image_ch;

            waiting_backimage[0] = R.drawable.workingmotion0;
            waiting_backimage[1] = R.drawable.workingmotion0_ch;

        }
    }

    /**
     * DB에서 초기값 로드
     */
    private void load_data(){

        sharedPreferences = context.getSharedPreferences(Application_manager.DB_NAME, 0);

        // 비정상 종료인지 확인
        isTerminated_by_uncaughtException = sharedPreferences.getBoolean(DB_IS_TERMINATED, false);
        if (isTerminated_by_uncaughtException) {

            // 중지 명령 -> 모터 원점 복귀
            communicator.set_tx(1, (byte)0x00);

            isTerminated_by_uncaughtException = false;
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean(DB_IS_TERMINATED, isTerminated_by_uncaughtException);
            editor.commit();
        }

        // 감성 LED 모드 및 밝기 불러오기
        led_mode_num = sharedPreferences.getInt(DB_EMOTION1,0);
        led_bright_num = sharedPreferences.getInt(DB_EMOTION2,1);
        getCommunicator().set_setting(3, (byte)(led_mode_num*16 | led_bright_num));

        // 감성 음원 모드 및 음량 불러오기
        sound_mode_num = sharedPreferences.getInt(DB_EMOTION3,0);
        sound_volume_num = sharedPreferences.getInt(DB_EMOTION4,1);
        soundManager.setVolume_therapy(sound_volume_num);

        // 인버터 타입 불러오기
        inverterType = sharedPreferences.getBoolean(DB_INVERT_TYPE, false);
        set_inverter(inverterType);

        //water_heater_time_save
        m_water_heater_time_save = sharedPreferences.getBoolean(DB_WATER_F,false);
        m_water_heater_f = sharedPreferences.getBoolean(DB_WATER_FF,false);
        m_water_heater_time_stime = sharedPreferences.getString(DB_WATER_ST,"00:00");
        m_water_heater_time_ftime = sharedPreferences.getString(DB_WATER_FT,"00:00");

        //External_led
        m_external_led = sharedPreferences.getInt(DB_EXTERN_LED,0);
        getCommunicator().set_setting(2, (byte)m_external_led);

        //Rause Rotation
        m_pause_rotation = sharedPreferences.getBoolean(DB_PAUSE,false);
        if (m_pause_rotation) {
            getCommunicator().set_setting(4, (byte)0x01);
        }
        else {
            getCommunicator().set_setting(4, (byte)0x00);
        }

        //GW_1000
        gw_1000 = sharedPreferences.getBoolean(GW_1000,true);

        //DB_LANGUEAGE
        m_language = sharedPreferences.getInt(DB_LANGUEAGE,0);

        //DB_INVERTER
        m_inverter = sharedPreferences.getInt(DB_INVERTER,0);

        //DB_VOLUME
        m_volume = sharedPreferences.getInt(DB_VOLUME,0);
        soundManager.setVolume_alarm(m_volume);

        // 어플 시작 시 이전 time gap 가져와서 변수에 넣기

        //sleep mode flag
        m_sleep_ff = sharedPreferences.getInt(DB_SLEEP_M,3);
        if(m_sleep_ff == 0){
            end_m = 60;
        }else if(m_sleep_ff == 1) {
            end_m = 180;
        }else if(m_sleep_ff == 2) {
            end_m = 300;
        }else if(m_sleep_ff == 3) {
            end_m = 0;
        }

        if(m_language == 2){
            img_flag = 1;
        }else{
            img_flag = 0;
        }

        // DB에서 필요 정보 불러와서 변수에 저장
        SENSOR_TEMP = 0;
        SENSOR_TEMP_BED = 0;
        SENSOR_TEMP_USER = sharedPreferences.getInt(DB_TEMPERATURE_USER, 25);
        SENSOR_TEMP_BED_USER = sharedPreferences.getInt(DB_TEMPERATURE_BED_USER, 25);

        // 도어 상태
        isDoorOpened = sharedPreferences.getBoolean(DB_DOOR_STATE, false);
    }

    public static void set_inverter(boolean _inverterType) {

        // LS
        if (_inverterType) {
            communicator.set_engineer(2, (byte)0x10);
            inverterVal = 0x10;
        }
        // 야스카와
        else {
            communicator.set_engineer(2, (byte)0x00);
            inverterVal = 0x00;
        }
    }

    /**
     * Door open/close 상태 저장
     * @param state true: open / false: close
     */
    public static void set_door_state(boolean state) {

        sharedPreferences = context.getSharedPreferences(Application_manager.DB_NAME, 0);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(DB_DOOR_STATE, state);
        editor.commit();
        isDoorOpened = state;
    }

    synchronized public static void set_m_gw_1000(boolean i){
        sharedPreferences = context.getSharedPreferences(Application_manager.DB_NAME, 0);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(GW_1000, i);
        editor.commit();
        gw_1000 = i;

        // GW-1000L 버전일 경우 히터 끄기
        if (!i) {

            communicator.set_tx(5, (byte) 0x00);
        }
    }

    synchronized public static void set_rfid_on_f(int i){
        sharedPreferences = context.getSharedPreferences(Application_manager.DB_NAME, 0);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(RFID_ON_F, i);
        editor.commit();
        rfid_on_f = i;

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
        soundManager.setVolume_therapy(e4);
    }

    synchronized public static String get_m_password(){
        sharedPreferences = context.getSharedPreferences(DB_NAME, 0);
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

    /*
    * 현제 시간을 받아오는 함수
    */
    synchronized public static String getText(){

        String doTime_tt;
        String doTime_mm;

        long r_time = System.currentTimeMillis();
        int r_t = (int)((r_time/1000/60/60)%24)+9;
        int r_m = (int)((r_time/1000/60)%60);
        if(r_t >= 24)
        {
            r_t = r_t-24;
        }

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
        doCalculation_gap();
    }

    // 시간 차이 계산 및 DB 저장
    public static void doCalculation_gap()
    {
        String ss_time = getText();
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

    /*
    * 시간차와 현재 시간을 가져와
    * 그 차이를 가지고 계산하여 시간을 설정
    */
    public static String doInit_time()
    {
        String p_time = Application_manager.getText();
        String g_time = Application_manager.m_gap_clock;
        g_time = sharedPreferences.getString(DB_TIME_GAP,"00:00");
        boolean t_f = Application_manager.m_gap_clock_f;

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

        check_water_time(doTime_t, doTime_m);

        return doTime;
    }

    // water_time 확인
    private static void check_water_time(String h, String m) {

        // water heater timer가 ON 일 경우
        if (m_water_heater_f == false) {

            String sw_h = m_water_heater_time_stime.substring(0, 2);
            String sw_m = m_water_heater_time_stime.substring(3, 5);
            int sw_h_t = Integer.parseInt(sw_h);
            int sw_m_t = Integer.parseInt(sw_m);

            String fw_h = m_water_heater_time_ftime.substring(0, 2);
            String fw_m = m_water_heater_time_ftime.substring(3, 5);
            int fw_h_t = Integer.parseInt(fw_h);
            int fw_m_t = Integer.parseInt(fw_m);

            int now_h = Integer.parseInt(h);
            int now_m = Integer.parseInt(m);

            int s_t = (sw_h_t * 60) + sw_m_t;
            int f_t = (fw_h_t * 60) + fw_m_t;
            int n_t = (now_h * 60) + now_m;

            if (s_t < f_t) {
                if (s_t <= n_t && n_t <= f_t) {
                    water_time_flag = true; // 설정시간 o
                } else {
                    water_time_flag = false;
                }
            } else if (s_t > f_t) {
                if (f_t < n_t && n_t < s_t) {
                    water_time_flag = false; // 설정시간 x
                } else {
                    water_time_flag = true;
                }
            } else {
                water_time_flag = false;
            }
        }
        else {

            water_time_flag = true;
        }
    }

    // 슬립모드 설정
    synchronized public static void wakeLock(Context context){
        if(mWakeLock != null){
            return;
        }
        PowerManager powerManager = (PowerManager)context.getSystemService(Context.POWER_SERVICE);
        mWakeLock = powerManager.newWakeLock(PowerManager.FULL_WAKE_LOCK,TAG);
        mWakeLock.acquire();
    }
    synchronized public static void releaseWakeLock() {
        if(mWakeLock != null){
            mWakeLock.release();
            mWakeLock = null;
        }
    }

    /*
    * 기기 구동시간 저장
     */
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

    /*
    * 기기 구동시간과 슬립모드 시간을 계산
    * m_operation_f 이 true이면 구동시간을 check
    * m_sleep_f 이 true이면 슬립모드 시간을 check
     */
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
                        }

                        if(m_sleep_f == true){
                            start_m++;
                            if(start_m == end_m){
                                Activity_setting.devicePolicyManager.lockNow();
                            }
                        }
                    }
                    catch (Exception e) {

                    }
                }
            }
        });
    }

    // 1,3,5에 따른 슬립 모드 설정
    synchronized public static void setSleep(int start, int end, boolean f) {
        m_sleep_f = f;
        start_m = start;
        end_m = end;
    }

    // 움직일때 마다 슬립모드 flag 다시 셋팅
    synchronized public static void setSleep_f(int start, boolean f) {
        m_sleep_f = f;
        start_m = start;
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

    // 원점 복귀 대기 ProgressDialog 종료 시점 확인 위한 플래그(디바이스 상태가 0x20 일 때 true)
    private static boolean isWaiting_init = false;
    public synchronized static boolean getIsWaiting_init() {
        return isWaiting_init;
    }
    public synchronized static void setIsWaiting_init(boolean val) {
        isWaiting_init = val;
    }
}