package com.sinest.gw_1000.mode;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.graphics.drawable.AnimationDrawable;
import android.nfc.NfcAdapter;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.view.View;
import android.content.Intent;

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
 * 대기 화면 - RFID 모드
 */

public class Activity_waiting_rfid extends AppCompatActivity {

    private final static int SET_BUTTON_INVISIBLE           = 1002;
    private final static int SET_BUTTON_VISIBLE             = 1003;

    private final static int MAX_OXYGEN                     = Activity_waiting.MAX_OXYGEN;
    private final static int MAX_PRESSURE                   = Activity_waiting.MAX_PRESSURE;

    Communicator communicator;
    Handler handler_update_data;
    BroadcastReceiver broadcastReceiver;

    private int val_oxygen_injection = 0;
    private int val_pressure = 0;
    private int val_time = 0;// 동작 전 설정 시간
    private int val_time_work = 0; // 동작 시간

    TextView time_text, oxygen_text, pressure_text;

    Fragment_working fragment_working;

    ImageView waiting_setting_button;

    TextView clock;

    // 내부 온도 및 수온
    TextView textView_temperature;
    TextView textView_temperature_bed;

    // GW-1000H / L 버전에 따라 visible/invisible 되는 레이아웃들
    LinearLayout layout_switchable1, layout_switchable2;

    private int mode = 0; // 0: waiting, 1: working

    // Variables for NFC tag
    private NfcAdapter mAdapter;
    private PendingIntent mPendingIntent;

    // 시간 업데이트 스레드 동작 플래그
    boolean isRun = false;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_waiting_rfid);
        Application_manager.setFullScreen(this);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        sharedPreferences = Application_manager.getSharedPreferences();

        communicator = Application_manager.getCommunicator();

        layout_switchable1 = (LinearLayout) findViewById(R.id.layout_switch_visible1_rfid);
        layout_switchable2 = (LinearLayout) findViewById(R.id.layout_switch_visible3_rfid);

        // 폰트 설정
        Typeface tf = Typeface.createFromAsset(getAssets(), "fonts/digital.ttf");
        clock = (TextView) findViewById(R.id.waiting_rfid_clock);
        clock.setTypeface(tf);
        clock.setText(Application_manager.doInit_time());

        // 산소 농도, 압력, 시간 값 불러오기
        val_oxygen_injection = sharedPreferences.getInt(Application_manager.DB_VAL_OXYGEN_INJECTION, 0);
        val_pressure = sharedPreferences.getInt(Application_manager.DB_VAL_PRESSURE, 0);
        val_time = sharedPreferences.getInt(Application_manager.DB_VAL_TIME, 10);

        // tx 메시지의 DATA2, 5에 수압, 산소투입량 입력
        communicator.set_tx(3, (byte) (Application_manager.inverterVal | (byte) (Application_manager.m_inverter * 3)));
        communicator.set_tx(6, (byte) val_oxygen_injection);

        time_text = (TextView)findViewById(R.id.waiting_rfid_time_text);
        time_text.setTypeface(tf);
        oxygen_text = (TextView)findViewById(R.id.waiting_rfid_oxygen_text);
        oxygen_text.setTypeface(tf);
        pressure_text = (TextView)findViewById(R.id.waiting_rfid_pressure_text);
        pressure_text.setTypeface(tf);

        oxygen_text.setText("" + val_oxygen_injection);
        pressure_text.setText(""+val_pressure);
        time_text.setText(""+val_time);

        textView_temperature = (TextView) findViewById(R.id.textView_rfid_temperature_above);
        textView_temperature_bed = (TextView) findViewById(R.id.textView_rfid_temperature_below);
        textView_temperature.setOnTouchListener(mTouchEvent);
        textView_temperature_bed.setOnTouchListener(mTouchEvent);

        waiting_setting_button = (ImageView)findViewById(R.id.waiting_rfid_setting_button);

        ImageView waiting_oxygen_up_button = (ImageView)findViewById(R.id.waiting_rfid_oxygen_up_button);
        ImageView waiting_oxygen_down_button = (ImageView)findViewById(R.id.waiting_rfid_oxygen_down_button);
        ImageView waiting_pressure_up_button = (ImageView)findViewById(R.id.waiting_rfid_pressure_up_button);
        ImageView waiting_pressure_down_button = (ImageView)findViewById(R.id.waiting_rfid_pressure_down_button);
        ImageView waiting_time_up_button = (ImageView)findViewById(R.id.waiting_rfid_time_up_button);
        ImageView waiting_time_down_button = (ImageView)findViewById(R.id.waiting_rfid_time_down_button);

        waiting_door_open_button = (ImageView)findViewById(R.id.waiting_rfid_dooropen_button);
        waiting_door_close_button = (ImageView)findViewById(R.id.waiting_rfid_doorclose_button);

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

        background = (ImageView) findViewById(R.id.activity_waiting_rfid_background);
        background_device = (ImageView) findViewById(R.id.imageView_device_rfid);

        progressBar_nozzle_loc = (CustomProgressBarBlock) findViewById(R.id.seekBar2_rfid);

        resolveIntent(getIntent());
        mAdapter = NfcAdapter.getDefaultAdapter(this);
        mPendingIntent = PendingIntent.getActivity(this, 0,
                new Intent(this, getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);
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

        ImageView imageView_device;
        switch(Application_manager.getProgramMode()) {
            case Application_manager.MODE_L:
                layout_switchable1.setVisibility(View.INVISIBLE);
                layout_switchable2.setVisibility(View.INVISIBLE);
                imageView_device = (ImageView) findViewById(R.id.imageView_device_rfid);
                imageView_device.setVisibility(View.INVISIBLE);
                break;
            case Application_manager.MODE_H:
                layout_switchable1.setVisibility(View.VISIBLE);
                layout_switchable2.setVisibility(View.INVISIBLE);
                imageView_device = (ImageView) findViewById(R.id.imageView_device_rfid);
                imageView_device.setVisibility(View.VISIBLE);
                break;
            case Application_manager.MODE_A:
                layout_switchable1.setVisibility(View.VISIBLE);
                layout_switchable2.setVisibility(View.VISIBLE);
                imageView_device = (ImageView) findViewById(R.id.imageView_device_rfid);
                imageView_device.setVisibility(View.VISIBLE);
                break;
        }

        if (mode == 0) {

            // 동작 시간 갱신 (시간 설정 팝업 -> 웨이팅 복귀 시)
            val_time = sharedPreferences.getInt(Application_manager.DB_VAL_TIME, 10);
            time_text.setText(Integer.toString(val_time));
        }


        // 앱이 실행될때 NFC 어댑터를 활성화 한다
        if (mAdapter != null) {

            mAdapter.enableForegroundDispatch(this, mPendingIntent, null, null);
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

        // 도어 상태
        if (Application_manager.isDoorOpened) {

            background_device.setBackgroundResource(R.drawable.open);
        }
        else {

            background_device.setBackgroundResource(R.drawable.close);
        }

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
                Log.i("JW_LIFECYCLE", "Activity_waiting_rfid - time update thread 종료");
            }
        });
        myThread.start();
    }

    @Override
    protected void onPause() {
        super.onPause();

        unregistReceiver();
        isRun = false;

        // 앱이 종료될때 NFC 어댑터를 비활성화 한다
        if (mAdapter != null) {
            mAdapter.disableForegroundDispatch(this);
        }

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

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            updateThread();
        }
    };

    private void updateThread() {
        clock.setText(Application_manager.doInit_time());
    }

    /**
     * NFC 태그 정보 수신 함수. 인텐트에 포함된 정보를 분석해서 화면에 표시
     */
    @Override
    public void onNewIntent(Intent intent) {
        setIntent(intent);
        resolveIntent(intent);
    }

    /**
     * RFID 태그 처리 이벤트
     */
    private void resolveIntent(Intent intent) {
        String action = intent.getAction();
        if (NfcAdapter.ACTION_TAG_DISCOVERED.equals(action)
                || NfcAdapter.ACTION_TECH_DISCOVERED.equals(action)
                || NfcAdapter.ACTION_NDEF_DISCOVERED.equals(action)) {

            byte[] id = intent.getByteArrayExtra(NfcAdapter.EXTRA_ID);

            Log.i("JW", "NFC tag is detected");
            Log.i("JW", "ID: " + getHex(id));

            // nfc 태그 감지되면 동작 모드로 전환

            // modeNum 설정 방법 정해야됨
            // mode == 0 (대기 상태) 일 때만 동작
            if (mode == 0
                    && !Application_manager.getIsWaiting_init()) {

                changeFragment_working(0);
            }
            /*
            Parcelable[] rawMsgs = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
            NdefMessage[] msgs;
            if (rawMsgs != null) {
                msgs = new NdefMessage[rawMsgs.length];
                for (int i = 0; i < rawMsgs.length; i++) {
                    msgs[i] = (NdefMessage) rawMsgs[i];
                }
            } else {
                // Unknown tag type
                byte[] empty = new byte[0];
                byte[] id = intent.getByteArrayExtra(NfcAdapter.EXTRA_ID);
                Parcelable tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
                byte[] payload = dumpTagData(tag).getBytes();
                NdefRecord record = new NdefRecord(NdefRecord.TNF_UNKNOWN, empty, id, payload);
                NdefMessage msg = new NdefMessage(new NdefRecord[] { record });
                msgs = new NdefMessage[] { msg };
            }
            */
        }
    }

    public void changeFragment_working(int patternNum) {

        if (val_time > 0) {

            if (fragment_working == null) {

                fragment_working = new Fragment_working();
            }

            // 타이머 스레드가 동작중이지 않은 경우
            if (!fragment_working.getIsAlive()) {

                if (Application_manager.getSoundManager().play(Application_manager.m_language, 0) == 0) {

                    Log.i("JW", "changeFragment (waiting_rfid -> working)");

                    communicator.set_tx(3, (byte) (Application_manager.inverterVal | (byte) val_pressure));

                    // 동작 시 설정 버튼 안보이게
                    handler_update_data.sendEmptyMessage(SET_BUTTON_INVISIBLE);

                    val_time_work = val_time;

                    FragmentManager fm = getFragmentManager();
                    FragmentTransaction fragmentTransaction = fm.beginTransaction();

                    fragment_working.init(patternNum, val_time_work, 1);
                    // tx 메시지의 DATA1에 패턴 입력
                    communicator.set_tx(2, (byte) patternNum);

                    fragmentTransaction.replace(R.id.frameLayout_rfid_fragment, fragment_working);
//                    fragmentTransaction.show(fragment_working);
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

                    // 동작 구간 표시 & RFID 카드 이미지 숨김
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            progressBar_nozzle_loc.setVisibility(View.VISIBLE);
                            ImageView imageView_rfid_card = (ImageView) findViewById(R.id.rfid_card);
                            imageView_rfid_card.setVisibility(View.INVISIBLE);
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
            }
        }
        else {

            Application_manager.getToastManager().popToast(5);
        }
    }

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
        communicator.set_tx(3, (byte) (Application_manager.inverterVal | (byte) (Application_manager.m_inverter * 3)));

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
        }
        else {

            Log.i("JW", "changeFragment (working -> waiting_rfid)");
            setTimeLeft(val_time);
            FragmentManager fm = getFragmentManager();
            FragmentTransaction fragmentTransaction = fm.beginTransaction();

            fragmentTransaction.hide(fragment_working);
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

            // 동작 구간 숨김 && RFID 카드 이미지 표시
            runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    progressBar_nozzle_loc.setVisibility(View.INVISIBLE);
                    ImageView imageView_rfid_card = (ImageView) findViewById(R.id.rfid_card);
                    imageView_rfid_card.setVisibility(View.VISIBLE);
                }
            });

            // 슬립 모드 재시작
            Application_manager.setSleep_f(0, true);
        }
    }

    public void wait_motor_back() {

        Application_manager.setIsWaiting_init(true);

        changeFragment_waiting();

        CustomProgressDialog progressDialog = new CustomProgressDialog(this);
        progressDialog.showDialog(null);
    }

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
     * 버퍼 데이터를 디코딩해서 String 으로 변환
     */
    private String getHex(byte[] bytes) {

        StringBuilder sb = new StringBuilder();
        for (int i = bytes.length - 1; i >= 0; --i) {

            int b = bytes[i] & 0xff;
            if (b < 0x10)

                sb.append('0');
            sb.append(Integer.toHexString(b));
            if (i > 0) {
                sb.append(" ");
            }
        }
        return sb.toString();
    }

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

    private void unregistReceiver() {

        if (broadcastReceiver != null) {

            this.unregisterReceiver(broadcastReceiver);
            broadcastReceiver = null;
            Log.i("JW", "unregisterReceiver");
        }
    }

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
                    textView_temperature = (TextView) findViewById(R.id.textView_rfid_temperature_above);
                    textView_temperature.setText(""+temp);
                    Application_manager.SENSOR_TEMP = temp;

                    // 수온
                    temp = communicator.get_rx_idx(2);
                    textView_temperature_bed = (TextView) findViewById(R.id.textView_rfid_temperature_below);
                    textView_temperature_bed.setText(""+temp);
                    Application_manager.SENSOR_TEMP_BED = temp;

                    // 노즐 위치
                    progressBar_nozzle_loc.setProgress(14-communicator.get_rx_idx(15));
                }
                else if (msg.what == SET_BUTTON_INVISIBLE) {

                    waiting_setting_button.setVisibility(View.INVISIBLE);
                }
                else if (msg.what == SET_BUTTON_VISIBLE) {

                    waiting_setting_button.setVisibility(View.VISIBLE);
                }

                // 내부온도, 수온 설정 값과 디바이스 실제 값을 비교하여 물히터와 히터 작동 여부 판별
                // 물히터
                // Water heater timer 에서 설정한 시간 내일 경우 - 현재 온도에 따라 동작여부 결정
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
                // Water heater timer 에서 설정한 시간 외일 경우 - 끄기
                else {

                    communicator.set_tx(4, (byte) 0x00);
                }

                // 히터
                switch(Application_manager.getProgramMode()) {
                    case Application_manager.MODE_L:
                        communicator.set_tx(5, (byte) 0x00);
                        break;
                    case Application_manager.MODE_H:
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
                    case R.id.waiting_rfid_setting_button:
                        view.setBackgroundResource(R.drawable.setting_on);
                        break;
                    case R.id.waiting_rfid_oxygen_up_button:
                        view.setBackgroundResource(R.drawable.button_up_on);
                        break;
                    case R.id.waiting_rfid_oxygen_down_button:
                        view.setBackgroundResource(R.drawable.button_down_on);
                        break;
                    case R.id.waiting_rfid_pressure_up_button:
                        view.setBackgroundResource(R.drawable.button_up_on);
                        break;
                    case R.id.waiting_rfid_pressure_down_button:
                        view.setBackgroundResource(R.drawable.button_down_on);
                        break;
                    case R.id.waiting_rfid_time_up_button:
                        view.setBackgroundResource(R.drawable.button_up_on);
                        break;
                    case R.id.waiting_rfid_time_down_button:
                        view.setBackgroundResource(R.drawable.button_down_on);
                        break;
                    case R.id.waiting_rfid_dooropen_button:
                        view.setBackgroundResource(Application_manager.door_open_on[Application_manager.useChineseImage]);
                        break;
                    case R.id.waiting_rfid_doorclose_button:
                        view.setBackgroundResource(Application_manager.door_close_on[Application_manager.useChineseImage]);
                        break;
                    case R.id.waiting_rfid_time_text:
                        break;
                }
            } else if (action == MotionEvent.ACTION_UP) {

                byte val = 0x00;
                switch (id) {
                    case R.id.waiting_rfid_setting_button:
                        view.setBackgroundResource(R.drawable.setting);
                        //setting
                        intent_setting = new Intent(getApplicationContext(), Activity_setting.class);
                        startActivity(intent_setting);
                        finish();
                        break;
                    case R.id.waiting_rfid_oxygen_up_button:
                        view.setBackgroundResource(R.drawable.button_up);

                        val_oxygen_injection++;
                        if (val_oxygen_injection > MAX_OXYGEN) val_oxygen_injection = MAX_OXYGEN;
                        oxygen_text.setText("" + val_oxygen_injection);
                        val = (byte) val_oxygen_injection;

                        communicator.set_tx(6, val);
                        break;
                    case R.id.waiting_rfid_oxygen_down_button:
                        view.setBackgroundResource(R.drawable.button_down);

                        val_oxygen_injection--;
                        if (val_oxygen_injection < 0) val_oxygen_injection = 0;
                        oxygen_text.setText("" + val_oxygen_injection);
                        val = (byte) val_oxygen_injection;

                        communicator.set_tx(6, val);
                        break;
                    case R.id.waiting_rfid_pressure_up_button:
                        view.setBackgroundResource(R.drawable.button_up);

                        val_pressure += 1;
                        if (val_pressure > MAX_PRESSURE) val_pressure = MAX_PRESSURE;
                            pressure_text.setText("" + val_pressure);

                            if (mode == 1) {
                                communicator.set_tx(3, (byte) (Application_manager.inverterVal | (byte) val_pressure));
                            }
                        break;
                    case R.id.waiting_rfid_pressure_down_button:
                        view.setBackgroundResource(R.drawable.button_down);

                            val_pressure -= 1;
                            if (val_pressure < 0) val_pressure = 0;
                            pressure_text.setText("" + val_pressure);

                            if (mode == 1) {
                                communicator.set_tx(3, (byte) (Application_manager.inverterVal | (byte) val_pressure));
                            }
                        break;
                    case R.id.waiting_rfid_time_up_button:
                        view.setBackgroundResource(R.drawable.button_up);

                        if (mode == 0) { // 대기 모드일 때

                            val_time += 1;
                            if (val_time > 90) val_time = 90;
                            time_text.setText("" + val_time);
                        }
                        else if (mode == 1) { // 동작 모드일 때

                            val_time_work += 1;
                            if (val_time_work > 90) val_time_work = 90;
                            time_text.setText("" + val_time_work);

                            fragment_working.setTime_m_left(val_time_work);
                        }
                        break;
                    case R.id.waiting_rfid_time_down_button:
                        view.setBackgroundResource(R.drawable.button_down);

                        if (mode == 0) { // 대기 모드일 때

                            val_time -= 1;
                            if (val_time < 1) val_time = 1;
                            time_text.setText("" + val_time);
                        }
                        else if (mode == 1) { // 동작 모드일 때

                            val_time_work -= 1;
                            if (val_time_work < 1) val_time_work = 1;
                            time_text.setText("" + val_time_work);

                            fragment_working.setTime_m_left(val_time_work);
                        }
                        break;
                    case R.id.waiting_rfid_dooropen_button:

                        view.setBackgroundResource(Application_manager.door_open_off[Application_manager.useChineseImage]);

                        if (Application_manager.getSoundManager().play(Application_manager.m_language, 3) == 0) {

                            background_device.setBackgroundResource(R.drawable.open);
                            Application_manager.set_door_state(true);

                            val = 0x01;
                            communicator.set_tx(8, val);
                        }
                        break;
                    case R.id.waiting_rfid_doorclose_button:

                        view.setBackgroundResource(Application_manager.door_close_off[Application_manager.useChineseImage]);

                        if (Application_manager.getSoundManager().play(Application_manager.m_language, 4) == 0) {

                            background_device.setBackgroundResource(R.drawable.close);
                            Application_manager.set_door_state(false);

                            val = 0x02;
                            communicator.set_tx(8, val);
                        }
                        break;
                    case R.id.waiting_rfid_time_text:

                        if (mode == 0) {

                            intent = new Intent(getApplicationContext(), Activity_waiting_working_time_popup.class);
                            intent.putExtra("mode", 0);
                            startActivity(intent);
                        }
                        break;
                    case R.id.textView_rfid_temperature_above:

                        intent = new Intent(getApplicationContext(), Activity_temperature_popup.class);
                        intent.putExtra("mode", 0);
                        intent.putExtra("temp", Application_manager.SENSOR_TEMP_USER);
                        startActivity(intent);
                        break;
                    case R.id.textView_rfid_temperature_below:

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
}
