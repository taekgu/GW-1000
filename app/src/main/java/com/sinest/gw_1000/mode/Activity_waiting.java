package com.sinest.gw_1000.mode;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sinest.gw_1000.R;
import com.sinest.gw_1000.communication.Communicator;
import com.sinest.gw_1000.management.Application_broadcast;
import com.sinest.gw_1000.management.Application_manager;
import com.sinest.gw_1000.management.CustomProgressDialog;
import com.sinest.gw_1000.mode.utils.CustomProgressBarBlock;
import com.sinest.gw_1000.setting.Activity_setting;

/**
 * Created by Jinwook.
 *
 * 대기 화면
 */

public class Activity_waiting extends AppCompatActivity {

    private final static int SET_BUTTON_INVISIBLE           = 1002;
    private final static int SET_BUTTON_VISIBLE             = 1003;

    public final static int MAX_OXYGEN                     = 5;
    public final static int MAX_PRESSURE                   = 6;

    Communicator communicator;
    Handler handler_update_data;
    BroadcastReceiver broadcastReceiver;

    private int val_oxygen_injection = 0;
    private int val_pressure = 0;
    private int val_time = 0; // 동작 전 설정 시간
    private int val_time_work = 0; // 동작 시간

    TextView time_text, oxygen_text, pressure_text;
    int[] checked_loc = new int[Application_manager.MAX_CHECKED]; // 선택된 라이브러리 4개 메뉴

    Fragment_waiting fragment_waiting;
    Fragment_working fragment_working;

    ImageView waiting_library_button;
    ImageView waiting_setting_button;

    TextView clock;

    // 내부 온도 및 수온
    TextView textView_temperature;
    TextView textView_temperature_bed;

    // GW-1000H / L 버전에 따라 visible/invisible 되는 레이아웃들
    LinearLayout layout_switchable1, layout_switchable2;

    private int mode = 0; // 0: waiting, 1: working

    // 시간 업데이트 스레드 동작 플래그
    boolean isRun;

    private ImageView background;
    private ImageView background_device;
    private AnimationDrawable frameAnimation;

    // 동작 중 노즐 위치 표시 바
    CustomProgressBarBlock progressBar_nozzle_loc;

    ImageView waiting_door_open_button;
    ImageView waiting_door_close_button;

    // 동작 중 화면 꺼짐 플래그
    boolean isScreen_turned_off = false;
    boolean isWork_finished = false;

    private SharedPreferences sharedPreferences = null;

    // 라이브러리 세팅, 세팅 버튼 중 하나라도 터치된 버튼이 있는지 확인용 플래그 (동작과 동시 실행 방지)
    private boolean isTouched = false;
    synchronized public boolean getIsTouched() {

        return isTouched;
    }
    synchronized public void setIsTouched(boolean val) {

        isTouched = val;
    }

    // 동작 버튼 중 터치된 버튼이 있는지 확인용 플래그 (라이브러리 세팅, 세팅과 동시 실행 방지)
    private boolean isTouched_work = false;
    synchronized public boolean getIsTouched_work() {

        return isTouched_work;
    }
    synchronized public void setIsTouched_work(boolean val) {

        isTouched_work = val;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_waiting);
        Application_manager.setFullScreen(this);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        sharedPreferences = Application_manager.getSharedPreferences();

        communicator = Application_manager.getCommunicator();

        layout_switchable1 = (LinearLayout) findViewById(R.id.layout_switch_visible1);
        layout_switchable2 = (LinearLayout) findViewById(R.id.layout_switch_visible3);

        // 폰트 설정
        Typeface tf = Typeface.createFromAsset(getAssets(), "fonts/digital.ttf");
        clock = (TextView) findViewById(R.id.waiting_clock);
        clock.setTypeface(tf);
        Log.v("sb","back_clock : " + Application_manager.getText());
        clock.setText(Application_manager.doInit_time());

        // 산소 농도, 압력, 시간 값 불러오기
        val_oxygen_injection = sharedPreferences.getInt(Application_manager.DB_VAL_OXYGEN_INJECTION, 0);
        val_pressure = sharedPreferences.getInt(Application_manager.DB_VAL_PRESSURE, 0);
        val_time = sharedPreferences.getInt(Application_manager.DB_VAL_TIME, 10);

        // tx 메시지의 DATA2, 5에 수압, 산소투입량 입력
        communicator.set_tx(3, (byte) (Application_manager.inverterVal | (byte) (val_pressure * Application_manager.m_inverter / 2)));
        communicator.set_tx(6, (byte) val_oxygen_injection);

        time_text = (TextView)findViewById(R.id.waiting_time_text);
        time_text.setTypeface(tf);
        oxygen_text = (TextView)findViewById(R.id.waiting_oxygen_text);
        oxygen_text.setTypeface(tf);
        pressure_text = (TextView)findViewById(R.id.waiting_pressure_text);
        pressure_text.setTypeface(tf);

        oxygen_text.setText("" + val_oxygen_injection);
        pressure_text.setText(""+val_pressure);
        time_text.setText(""+val_time);

        textView_temperature = (TextView) findViewById(R.id.textView_temperature_above);
        textView_temperature_bed = (TextView) findViewById(R.id.textView_temperature_below);
        textView_temperature.setOnTouchListener(mTouchEvent);
        textView_temperature_bed.setOnTouchListener(mTouchEvent);

        fragment_waiting = new Fragment_waiting();
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.frameLayout_fragment, fragment_waiting);
        fragmentTransaction.commit();

        waiting_library_button = (ImageView)findViewById(R.id.waiting_library_button);
        waiting_setting_button = (ImageView)findViewById(R.id.waiting_setting_button);

        ImageView waiting_oxygen_up_button = (ImageView)findViewById(R.id.waiting_oxygen_up_button);
        ImageView waiting_oxygen_down_button = (ImageView)findViewById(R.id.waiting_oxygen_down_button);
        ImageView waiting_pressure_up_button = (ImageView)findViewById(R.id.waiting_pressure_up_button);
        ImageView waiting_pressure_down_button = (ImageView)findViewById(R.id.waiting_pressure_down_button);
        ImageView waiting_time_up_button = (ImageView)findViewById(R.id.waiting_time_up_button);
        ImageView waiting_time_down_button = (ImageView)findViewById(R.id.waiting_time_down_button);

        waiting_door_open_button = (ImageView)findViewById(R.id.waiting_dooropen_button);
        waiting_door_close_button = (ImageView)findViewById(R.id.waiting_doorclose_button);

        waiting_library_button.setOnTouchListener(mTouchEvent);
        waiting_setting_button.setOnTouchListener(mTouchEvent);

        waiting_oxygen_up_button.setOnTouchListener(mTouchEvent);
        waiting_oxygen_down_button.setOnTouchListener(mTouchEvent);
        waiting_pressure_up_button.setOnTouchListener(mTouchEvent);
        waiting_pressure_down_button.setOnTouchListener(mTouchEvent);
        waiting_time_up_button.setOnTouchListener(mTouchEvent);
        waiting_time_down_button.setOnTouchListener(mTouchEvent);

        waiting_door_open_button.setOnTouchListener(mTouchEvent);
        waiting_door_close_button.setOnTouchListener(mTouchEvent);

        time_text.setOnTouchListener(mTouchEvent);

        background = (ImageView) findViewById(R.id.activity_waiting_background);
        background_device = (ImageView) findViewById(R.id.imageView_device);

        progressBar_nozzle_loc = (CustomProgressBarBlock) findViewById(R.id.seekBar2);
    }

    @Override
    protected void onResume() {
        super.onResume();

        Application_manager.setFullScreen(this);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        registReceiver();

        // 언어에 따라 배경
        if (mode == 0) {

            switch(Application_manager.getProgramMode()) {
                case Application_manager.MODE_L:
                    if (Application_manager.useChineseImage == 1) { // 중국어
                        background.setBackgroundResource(R.drawable.l_workingmotion0_c);
                    } else {
                        background.setBackgroundResource(R.drawable.l_workingmotion0_e);
                    }
                    break;
                case Application_manager.MODE_H:
                    if (Application_manager.useChineseImage == 1) { // 중국어
                        background.setBackgroundResource(R.drawable.h_workingmotion0_c);
                    } else {
                        background.setBackgroundResource(R.drawable.h_workingmotion0_e);
                    }
                    break;
                case Application_manager.MODE_A:
                    if (Application_manager.useChineseImage == 1) { // 중국어
                        background.setBackgroundResource(R.drawable.a_workingmotion0_c);
                    } else {
                        background.setBackgroundResource(R.drawable.a_workingmotion0_e);
                    }
                    break;
            }
        }
        waiting_door_open_button.setBackgroundResource(Application_manager.door_open_off[Application_manager.useChineseImage]);
        waiting_door_close_button.setBackgroundResource(Application_manager.door_close_off[Application_manager.useChineseImage]);

        switch(Application_manager.getProgramMode()) {
            case Application_manager.MODE_L:
                layout_switchable1.setVisibility(View.INVISIBLE);
                layout_switchable2.setVisibility(View.INVISIBLE);
                break;
            case Application_manager.MODE_H:
                layout_switchable1.setVisibility(View.VISIBLE);
                layout_switchable2.setVisibility(View.INVISIBLE);
                break;
            case Application_manager.MODE_A:
                layout_switchable1.setVisibility(View.VISIBLE);
                layout_switchable2.setVisibility(View.VISIBLE);
                break;
        }

        // 선택 모드 변경시 프래그먼트 갱신
        if (mode == 0) {

            // 선택된 모드 갱신
            fragment_waiting.reset();
            for (int i = 0; i < Application_manager.MAX_CHECKED; i++) {

                checked_loc[i] = sharedPreferences.getInt(Application_manager.DB_LIBRARY_LOC_ + i, i);
                fragment_waiting.addCheckedIdx(checked_loc[i]);
                Log.i("JW", "Selected library idx : " + checked_loc[i]);
            }
            fragment_waiting.refresh();

            // 동작 시간 갱신 (시간 설정 팝업 -> 웨이팅 복귀 시)
            val_time = sharedPreferences.getInt(Application_manager.DB_VAL_TIME, 10);
            time_text.setText(Integer.toString(val_time));
        }

        // 화면에 보여질 때 센서값 불러오기
        runOnUiThread(new Runnable() {
            @Override
            public void run() {

                // 수온 불러오기
                textView_temperature.setText(""+Application_manager.SENSOR_TEMP);
                textView_temperature_bed.setText(""+Application_manager.SENSOR_TEMP_BED);
            }
        });

        // 동작중이 아닐 경우에만 슬립 모드 동작 재시작
        if (mode == 0) {

            Application_manager.setSleep_f(0,true);
        }

        updateDoorState();

        // 동작 중에 액티비티 resume 시
        if (mode == 1) {

            // 애니메이션 재시작
            if(Application_manager.useChineseImage == 0){
                start_animation();
            }
            else if(Application_manager.useChineseImage == 1){
                start_animation_ch();
            }

            // 화면 껐다가 켜진 경우
            if (isScreen_turned_off) {

                isScreen_turned_off = false;
                Log.i("JW", "isScreen_turned_off = false");

                // 동작이 종료되었을 경우 프래그먼트 변경
                if (isWork_finished) {

                    changeFragment_waiting();
                    isWork_finished = false;
                    Log.i("JW", "isWork_finished = false");
                }
            }
        }

        isRun = true;
        Thread myThread = new Thread(new Runnable() {
            public void run() {
                while (isRun) {
                    try {
                        handler.sendMessage(handler.obtainMessage());
                        Thread.sleep(1000);
                    } catch (Throwable t) {
                    }
                }
                Log.i("JW_LIFECYCLE", "Activity_waiting - time update thread 종료");
            }
        });
        myThread.start();
    }

    @Override
    protected void onPause() {
        super.onPause();

        unregistReceiver();
        isRun = false;

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(Application_manager.DB_VAL_OXYGEN_INJECTION, val_oxygen_injection);
        editor.putInt(Application_manager.DB_VAL_PRESSURE, val_pressure);
        editor.putInt(Application_manager.DB_VAL_TIME, val_time);
        editor.commit();

        // 동작 중에 액티비티 pause 시
        if (mode == 1) {

            // 애니메이션 정지
            stop_animation();

            // 동작 시간 종료되어도 프래그먼트 전환되지 않게
            isScreen_turned_off = true;
            Log.i("JW", "isScreen_turned_off = true");
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    /**
     * 동작 시작 시 동작 프래그먼트로 변경
     * @param patternNum 선택 모드 번호
     */
    public void changeFragment_working(int patternNum) {

        if (val_time > 0) {

            if (fragment_working == null) {

                fragment_working = new Fragment_working();
            }

            // 타이머 스레드가 동작중이지 않은 경우
            if (!fragment_working.getIsAlive()) {

                if (Application_manager.getSoundManager().play(Application_manager.m_language, 0) == 0) {

                    Log.i("JW", "changeFragment (waiting -> working)");

                    communicator.set_tx(3, (byte) (Application_manager.inverterVal | (byte) val_pressure));

                    // 동작 시 라이브러리, 설정 버튼 안보이게
                    handler_update_data.sendEmptyMessage(SET_BUTTON_INVISIBLE);

                    val_time_work = val_time;

                    FragmentManager fm = getFragmentManager();
                    FragmentTransaction fragmentTransaction = fm.beginTransaction();

                    fragment_working.init(patternNum, val_time_work, 0);
                    // tx 메시지의 DATA1에 패턴 입력
                    communicator.set_tx(2, (byte) patternNum);

                    fragmentTransaction.replace(R.id.frameLayout_fragment, fragment_working);
                    fragmentTransaction.commit();

                    mode = 1;
                    Application_manager.m_operation_f = true;

                    // 시작 명령
                    communicator.set_tx(1, (byte)0x01);

                    // 동작 모드로 바뀌기 이전 산소농도, 수압, 시간 값 저장
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putInt(Application_manager.DB_VAL_OXYGEN_INJECTION, val_oxygen_injection);
                    editor.putInt(Application_manager.DB_VAL_PRESSURE, val_pressure);
                    editor.putInt(Application_manager.DB_VAL_TIME, val_time);
                    editor.commit();

                    // 애니메이션 시작
                    if(Application_manager.useChineseImage == 0){
                        start_animation();
                    }else if(Application_manager.useChineseImage == 1){
                        start_animation_ch();
                    }

                    // 동작 구간 표시
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            progressBar_nozzle_loc.setVisibility(View.VISIBLE);
                            //seekBar_water.setVisibility(View.VISIBLE);
                        }
                    });

                    // 치료 음악 재생
                    if (Application_manager.sound_mode_num != 0) {

                        Thread thread = new Thread(new Runnable() {
                            @Override
                            public void run() {

                                while (true) {

                                    if (!Application_manager.getSoundManager().getIsPlaying()) {

                                        Log.i("JW", "치료 음악 재생");
                                        Application_manager.getSoundManager().play_therapy(Application_manager.sound_mode_num, true);
                                        break;
                                    }
                                }
                            }
                        });
                        thread.start();
                    }

                    // 슬립 모드 중지
                    Application_manager.setSleep_f(0, false);
                }
            }
            // 타이머 스레드가 아직 동작중인 경우
            else {

                Log.i("JW", "changeFragment (waiting -> working) is failed");
                Application_manager.getToastManager().popToast(4);
                //Toast.makeText(this, "잠시 후 다시 시도해주세요", Toast.LENGTH_SHORT).show();
            }
        }
        else {

            Application_manager.getToastManager().popToast(5);
            //Toast.makeText(this, "동작 시간을 1~90분 사이로 설정해야합니다", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 동작 종료/정지 시 대기 프래그먼트로 변경
     */
    public void changeFragment_waiting() {

        // 중지 명령
        communicator.set_tx(1, (byte) 0x00);

        // 동작 시작 전 산소 농도, 압력, 시간 값 불러오기
        val_oxygen_injection = sharedPreferences.getInt(Application_manager.DB_VAL_OXYGEN_INJECTION, 0);
        val_pressure = sharedPreferences.getInt(Application_manager.DB_VAL_PRESSURE, 0);
        val_time = sharedPreferences.getInt(Application_manager.DB_VAL_TIME, 10);

        // 동작 시작 전 값으로 tx 값 복원
        byte val = (byte) val_oxygen_injection;
        communicator.set_tx(6, val);
        communicator.set_tx(3, (byte) (Application_manager.inverterVal | (byte) (val_pressure * Application_manager.m_inverter / 2)));

        // 치료 음악 재생 종료
        if (Application_manager.sound_mode_num != 0) {

            Log.i("JW", "치료 음악 중지");
            Application_manager.getSoundManager().play_therapy(Application_manager.sound_mode_num, false);
        }

        // tx 메시지의 DATA1에 패턴 초기화
        communicator.set_tx(2, (byte) 0x00);

        // UI 관련 작업의 경우 화면이 켜져있을 때 실행
        if (isScreen_turned_off) {

            isWork_finished = true;
            Log.i("JW", "isWork_finished = true");
        }
        else {

            Log.i("JW", "changeFragment (working -> waiting)");
            setTimeLeft(val_time);
            FragmentManager fm = getFragmentManager();
            FragmentTransaction fragmentTransaction = fm.beginTransaction();

            fragmentTransaction.replace(R.id.frameLayout_fragment, fragment_waiting);
            fragmentTransaction.commit();

            mode = 0;
            Application_manager.m_operation_f = false;

            // 동작 중지 시 라이브러리, 설정 버튼 보이게
            handler_update_data.sendEmptyMessage(SET_BUTTON_VISIBLE);

            runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    oxygen_text.setText("" + val_oxygen_injection);
                    pressure_text.setText("" + val_pressure);
                    time_text.setText("" + val_time);
                }
            });

            // 애니메이션 정지
            stop_animation();

            // 동작 구간 숨김
            runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    progressBar_nozzle_loc.setVisibility(View.INVISIBLE);
                }
            });

            // 슬립 모드 재시작
            Application_manager.setSleep_f(0, true);
        }
    }

    /**
     * progressDialog 출력 후, 모터 원점 복귀 신호 대기
     */
    public void wait_motor_back() {

        Application_manager.setIsWaiting_init(true);

        changeFragment_waiting();

        CustomProgressDialog progressDialog = new CustomProgressDialog(this);
        progressDialog.showDialog(null);
    }

    /**
     * 동작 애니메이션 시작 - 영문
     */
    private void start_animation() {

        switch(Application_manager.getProgramMode()) {
            case Application_manager.MODE_L:
                background.setBackgroundResource(R.drawable.animation_working_l);
                break;
            case Application_manager.MODE_H:
                background.setBackgroundResource(R.drawable.animation_working_h);
                break;
            case Application_manager.MODE_A:
                background.setBackgroundResource(R.drawable.animation_working_a);
                break;
        }
        frameAnimation = (AnimationDrawable) background.getBackground();
        frameAnimation.start();
    }

    /**
     * 동작 애니메이션 시작 - 중문
     */
    private void start_animation_ch() {

        switch(Application_manager.getProgramMode()) {
            case Application_manager.MODE_L:
                background.setBackgroundResource(R.drawable.animation_working_l_ch);
                break;
            case Application_manager.MODE_H:
                background.setBackgroundResource(R.drawable.animation_working_h_ch);
                break;
            case Application_manager.MODE_A:
                background.setBackgroundResource(R.drawable.animation_working_a_ch);
                break;
        }
        frameAnimation = (AnimationDrawable) background.getBackground();
        frameAnimation.start();
    }

    /**
     * 동작 애니메이션 정지
     */
    private void stop_animation() {

        if (frameAnimation != null) {

            runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    frameAnimation.stop();
                    // 리소스아이디 0을 넘김으로써 빈 drawable을 선택여 기존에 가지고 있던 리소스 자동 해제
                    frameAnimation.selectDrawable(0);

                    switch(Application_manager.getProgramMode()) {
                        case Application_manager.MODE_L:
                            if (Application_manager.useChineseImage == 1) { // 중국어
                                background.setBackgroundResource(R.drawable.l_workingmotion0_c);
                            } else {
                                background.setBackgroundResource(R.drawable.l_workingmotion0_e);
                            }
                            break;
                        case Application_manager.MODE_H:
                            if (Application_manager.useChineseImage == 1) { // 중국어
                                background.setBackgroundResource(R.drawable.h_workingmotion0_c);
                            } else {
                                background.setBackgroundResource(R.drawable.h_workingmotion0_e);
                            }
                            break;
                        case Application_manager.MODE_A:
                            if (Application_manager.useChineseImage == 1) { // 중국어
                                background.setBackgroundResource(R.drawable.a_workingmotion0_c);
                            } else {
                                background.setBackgroundResource(R.drawable.a_workingmotion0_e);
                            }
                            break;
                    }
                }
            });
        }
    }

    /**
     * 동작 중 남은 시간 변경
     * @param min 변경값
     */
    public void setTimeLeft(final int min) {

        runOnUiThread(new Runnable() {
            @Override
            public void run() {

                time_text.setText("" + min);
                val_time_work = min;
            }
        });
    }

    /**
     * 디바이스 메시지 수신 확인을 위한 리시버
     */
    private void registReceiver() {

        if (broadcastReceiver != null) {

            return;
        }

        final IntentFilter mIntentFilter = new IntentFilter();
        mIntentFilter.addAction("update.data");

        setHandler_update_data();
        broadcastReceiver = new Application_broadcast(handler_update_data);
        this.registerReceiver(broadcastReceiver, mIntentFilter);
        Log.i("JW", "registerReceiver");
    }

    /**
     * 수신 확인 리시버 등록 해제
     */
    private void unregistReceiver() {

        if (broadcastReceiver != null) {

            this.unregisterReceiver(broadcastReceiver);
            broadcastReceiver = null;
            Log.i("JW", "unregisterReceiver");
        }
    }

    /**
     * 디바이스 메시지 수신 시 값(내부온도, 수온, 산소농도, 습도) 업데이트 처리를 위한 핸들러
     */
    private void setHandler_update_data() {

        handler_update_data = new Handler() {

            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);

                if (msg.what == 1) {

//                    int[] onoff_flag = new int[12];
//                    for (int i=1; i<=4; i++) { // 세로
//
//                        for (int j = 1; j <= 3; j++) { // 가로
//
//                            onoff_flag[((j-1)*4 + i - 1)] = sharedPreferences.getInt(Application_manager.DB_SETTING_ONOFF_VAL_ + i + "" + j, 0);
//                        }
//                    }

                    // 내부온도 (단일값)
                    int temp = communicator.get_rx_idx(3);
                    textView_temperature = (TextView) findViewById(R.id.textView_temperature_above);
                    textView_temperature.setText(""+temp);
                    Application_manager.SENSOR_TEMP = temp;

                    // 수온
                    temp = communicator.get_rx_idx(2);
                    textView_temperature_bed = (TextView) findViewById(R.id.textView_temperature_below);
                    textView_temperature_bed.setText(""+temp);
                    Application_manager.SENSOR_TEMP_BED = temp;

                    // 노즐 위치 0~14
                    //Log.i("JW", "노즐 위치: "+Application_manager.getCommunicator().get_rx_idx(15));
                    progressBar_nozzle_loc.setProgress(14-communicator.get_rx_idx(15));
                }
                else if (msg.what == SET_BUTTON_INVISIBLE) {

                    waiting_library_button.setVisibility(View.INVISIBLE);
                    waiting_setting_button.setVisibility(View.INVISIBLE);
                }
                else if (msg.what == SET_BUTTON_VISIBLE) {

                    waiting_library_button.setVisibility(View.VISIBLE);
                    waiting_setting_button.setVisibility(View.VISIBLE);
                }

                // 내부온도, 수온 설정 값과 디바이스 실제 값을 비교하여 물히터와 히터 작동 여부 판별
                // 물히터
                // Water heater timer 에서 설정한 시간 내이고 GW-1000H 버전인 경우 - 현재 온도에 따라 동작여부 결정

                if (Application_manager.water_time_flag) {
                    // 온도 높을 때 - 냉
                    if (Application_manager.SENSOR_TEMP_BED_USER + 1 < Application_manager.SENSOR_TEMP_BED) {

                        communicator.set_tx(4, (byte) 0x01);
                    }
                    // 온도 낮을 때 - 온
                    else if (Application_manager.SENSOR_TEMP_BED_USER - 1 > Application_manager.SENSOR_TEMP_BED) {

                        communicator.set_tx(4, (byte) 0x02);
                    }
                    // 설정 범위 +-1 이내일 때 - 끄기
                    else {

                        communicator.set_tx(4, (byte) 0x00);
                    }
                }
                // Water heater timer 에서 설정한 시간 외거나 GW-1000L 버전인 경우 - 끄기
                else {

                    communicator.set_tx(4, (byte) 0x00);
                }

                // 히터
                // GW-1000H 버전인 경우
//                if (Application_manager.gw_1000) {
//
//                    // 온도 높을 때 - 냉
//                    if (Application_manager.SENSOR_TEMP_USER + 1 < Application_manager.SENSOR_TEMP) {
//
//                        communicator.set_tx(5, (byte) 0x01);
//                    }
//                    // 온도 낮을 때 - 온
//                    else if (Application_manager.SENSOR_TEMP_USER - 1 > Application_manager.SENSOR_TEMP) {
//
//                        communicator.set_tx(5, (byte) 0x02);
//                    }
//                    // 설정 범위 +-1 이내일 때 - 끄기
//                    else {
//
//                        communicator.set_tx(5, (byte) 0x00);
//                    }
//                }
//                // GW-1000L 버전인 경우
//                else {
//
//                    communicator.set_tx(5, (byte) 0x00);
//                }
                switch(Application_manager.getProgramMode()) {
                    case Application_manager.MODE_L:
                        communicator.set_tx(5, (byte) 0x00);
                        break;
                    case Application_manager.MODE_H:
                        // 추가개발 - 도어 상태 업데이트 (1000H)
                        updateDoorState();
                    case Application_manager.MODE_A:
                        // 온도 높을 때 - 냉
                        if (Application_manager.SENSOR_TEMP_USER + 1 < Application_manager.SENSOR_TEMP) {

                            communicator.set_tx(5, (byte) 0x01);
                        }
                        // 온도 낮을 때 - 온
                        else if (Application_manager.SENSOR_TEMP_USER - 1 > Application_manager.SENSOR_TEMP) {

                            communicator.set_tx(5, (byte) 0x02);
                        }
                        // 설정 범위 +-1 이내일 때 - 끄기
                        else {

                            communicator.set_tx(5, (byte) 0x00);
                        }
                        break;
                }
            }
        };
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            updateThread();
        }
    };

    private void updateThread() {
        clock.setText(Application_manager.doInit_time());
    }

    private View.OnTouchListener mTouchEvent = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            Application_manager.set_m_start_sleep(0);
            Intent intent;
            Intent intent_setting;
            int action = motionEvent.getAction();
            int id = view.getId();

            if (action == MotionEvent.ACTION_DOWN) {
                switch (id) {
                    case R.id.waiting_library_button:
                        view.setBackgroundResource(R.drawable.library_on);
                        setIsTouched(true);
                        break;
                    case R.id.waiting_setting_button:
                        view.setBackgroundResource(R.drawable.setting_on);
                        setIsTouched(true);
                        break;
                    case R.id.waiting_oxygen_up_button:
                        view.setBackgroundResource(R.drawable.button_up_on);
                        break;
                    case R.id.waiting_oxygen_down_button:
                        view.setBackgroundResource(R.drawable.button_down_on);
                        break;
                    case R.id.waiting_pressure_up_button:
                        view.setBackgroundResource(R.drawable.button_up_on);
                        break;
                    case R.id.waiting_pressure_down_button:
                        view.setBackgroundResource(R.drawable.button_down_on);
                        break;
                    case R.id.waiting_time_up_button:
                        view.setBackgroundResource(R.drawable.button_up_on);
                        break;
                    case R.id.waiting_time_down_button:
                        view.setBackgroundResource(R.drawable.button_down_on);
                        break;
                    case R.id.waiting_dooropen_button:
                        view.setBackgroundResource(Application_manager.door_open_on[Application_manager.useChineseImage]);
                        break;
                    case R.id.waiting_doorclose_button:
                        view.setBackgroundResource(Application_manager.door_close_on[Application_manager.useChineseImage]);
                        break;
                    case R.id.waiting_time_text:
                        break;
                }
            } else if (action == MotionEvent.ACTION_UP) {

                byte val = 0x00;
                switch (id) {
                    case R.id.waiting_library_button:
                        view.setBackgroundResource(R.drawable.library);
                        setIsTouched(false);

                        if (!getIsTouched_work()) {

                            intent = new Intent(getApplicationContext(), Activity_library.class);
                            startActivity(intent);
                        }
                        break;
                    case R.id.waiting_setting_button:
                        view.setBackgroundResource(R.drawable.setting);
                        setIsTouched(false);

                        if (!getIsTouched_work()) {

                            //setting
                            intent_setting = new Intent(getApplicationContext(), Activity_setting.class);
                            startActivity(intent_setting);
                            finish();
                        }
                        break;
                    case R.id.waiting_oxygen_up_button:
                        view.setBackgroundResource(R.drawable.button_up);

                        val_oxygen_injection++;
                        if (val_oxygen_injection > MAX_OXYGEN) val_oxygen_injection = MAX_OXYGEN;
                        oxygen_text.setText("" + val_oxygen_injection);
                        val = (byte) val_oxygen_injection;

                        communicator.set_tx(6, val);
                        break;
                    case R.id.waiting_oxygen_down_button:
                        view.setBackgroundResource(R.drawable.button_down);

                        val_oxygen_injection--;
                        if (val_oxygen_injection < 0) val_oxygen_injection = 0;
                        oxygen_text.setText("" + val_oxygen_injection);
                        val = (byte) val_oxygen_injection;

                        communicator.set_tx(6, val);
                        break;
                    case R.id.waiting_pressure_up_button:
                        view.setBackgroundResource(R.drawable.button_up);

                        val_pressure += 1;
                        if (val_pressure > MAX_PRESSURE) val_pressure = MAX_PRESSURE;
                        pressure_text.setText("" + val_pressure);

                        if (mode == 1) {
                            communicator.set_tx(3, (byte) (Application_manager.inverterVal | (byte) val_pressure));
                        }
                        break;
                    case R.id.waiting_pressure_down_button:
                        view.setBackgroundResource(R.drawable.button_down);

                        val_pressure -= 1;
                        if (val_pressure < 0) val_pressure = 0;
                        pressure_text.setText("" + val_pressure);

                        if (mode == 1) {
                            communicator.set_tx(3, (byte) (Application_manager.inverterVal | (byte) val_pressure));
                        }
                        break;
                    case R.id.waiting_time_up_button:
                        view.setBackgroundResource(R.drawable.button_up);

                        if (mode == 0) { // 대기 모드일 때

                            val_time += 1;
                            if (val_time > 90) val_time = 90;
                            time_text.setText("" + val_time);
                        } else if (mode == 1) { // 동작 모드일 때

                            val_time_work += 1;
                            if (val_time_work > 90) val_time_work = 90;
                            time_text.setText("" + val_time_work);

                            fragment_working.setTime_m_left(val_time_work);
                        }
                        break;
                    case R.id.waiting_time_down_button:
                        view.setBackgroundResource(R.drawable.button_down);

                        if (mode == 0) { // 대기 모드일 때

                            val_time -= 1;
                            if (val_time < 1) val_time = 1;
                            time_text.setText("" + val_time);
                        } else if (mode == 1) { // 동작 모드일 때

                            val_time_work -= 1;
                            if (val_time_work < 1) val_time_work = 1;
                            time_text.setText("" + val_time_work);

                            fragment_working.setTime_m_left(val_time_work);
                        }
                        break;
                    case R.id.waiting_dooropen_button:

                        view.setBackgroundResource(Application_manager.door_open_off[Application_manager.useChineseImage]);

                        if (Application_manager.getSoundManager().play(Application_manager.m_language, 3) == 0) {

                            background_device.setBackgroundResource(R.drawable.open);
                            Application_manager.set_door_state(true);

                            val = 0x01;
                            communicator.set_tx(8, val);
                        }
                        break;
                    case R.id.waiting_doorclose_button:

                        view.setBackgroundResource(Application_manager.door_close_off[Application_manager.useChineseImage]);

                        if (Application_manager.getSoundManager().play(Application_manager.m_language, 4) == 0) {

                            background_device.setBackgroundResource(R.drawable.close);
                            Application_manager.set_door_state(false);

                            val = 0x02;
                            communicator.set_tx(8, val);
                        }
                        break;
                    case R.id.waiting_time_text:

                        if (mode == 0) {

                            intent = new Intent(getApplicationContext(), Activity_waiting_working_time_popup.class);
                            intent.putExtra("mode", 0);
                            startActivity(intent);
                        }
                        break;
                    case R.id.textView_temperature_above:

                        intent = new Intent(getApplicationContext(), Activity_temperature_popup.class);
                        intent.putExtra("mode", 0);
                        intent.putExtra("temp", Application_manager.SENSOR_TEMP_USER);
                        startActivity(intent);
                        break;
                    case R.id.textView_temperature_below:

                        intent = new Intent(getApplicationContext(), Activity_temperature_popup.class);
                        intent.putExtra("mode", 1);
                        intent.putExtra("temp", Application_manager.SENSOR_TEMP_BED_USER);
                        startActivity(intent);
                        break;
                }
            }
            return true;
        }
    };

    /**
     * Back 버튼 터치 이벤트
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event){
        switch(keyCode){
            case KeyEvent.KEYCODE_BACK:

                return false;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Application_manager.set_m_start_sleep(0);
        Log.v("sb1","test");
        int action = event.getAction();
        switch(action) {
            case MotionEvent.ACTION_DOWN :    //화면을 터치했을때
                break;
            case MotionEvent.ACTION_UP :    //화면을 터치했다 땠을때
                break;
            case MotionEvent.ACTION_MOVE :    //화면을 터치하고 이동할때
                break;
        }
        return super.onTouchEvent(event);
    }

    private void updateDoorState() {

        switch(Application_manager.getProgramMode()) {
            case Application_manager.MODE_L:
                background_device.setBackgroundResource(R.drawable.none);
                break;
            case Application_manager.MODE_H:
                if (Application_manager.getCommunicator().get_rx_idx(16) == 0x00) {
                    background_device.setBackgroundResource(R.drawable.close);
                }
                else {
                    background_device.setBackgroundResource(R.drawable.open);
                }
                break;
            case Application_manager.MODE_A:
                if (Application_manager.isDoorOpened) {
                    background_device.setBackgroundResource(R.drawable.open);
                }
                else {
                    background_device.setBackgroundResource(R.drawable.close);
                }
                break;
        }
    }
}
