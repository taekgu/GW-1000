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
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.view.View;
import android.content.Intent;
import android.widget.Toast;

import com.sinest.gw_1000.R;
import com.sinest.gw_1000.communication.Communicator;
import com.sinest.gw_1000.management.Application_broadcast;
import com.sinest.gw_1000.management.Application_manager;
import com.sinest.gw_1000.mode.utils.CustomProgressBarHorizontal;
import com.sinest.gw_1000.setting.Activity_setting;

public class Activity_waiting_rfid extends AppCompatActivity {

    private final static int SET_BUTTON_INVISIBLE           = 1002;
    private final static int SET_BUTTON_VISIBLE             = 1003;

    Communicator communicator;
    Handler handler_update_data;
    BroadcastReceiver broadcastReceiver;

    private int val_oxygen = 0;
    private int val_pressure = 0;
    private int val_time = 0;

    TextView time_text, oxygen_text, pressure_text;
    ImageView waiting_setting_button;

    Fragment_working fragment_working;

    private int mode = 0; // 0: waiting, 1: working
    private boolean isDoorOpened = true;

    // Variables for NFC tag
    private NfcAdapter mAdapter;
    private PendingIntent mPendingIntent;
    boolean isRun = false;
    TextView clock;

    TextView textView_temperature;
    TextView textView_temperature_bed;

    private ImageView background;
    private AnimationDrawable frameAnimation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_waiting_rfid);
        Application_manager.setFullScreen(this);

        communicator = Application_manager.getCommunicator();

        // 폰트 설정
        Typeface tf = Typeface.createFromAsset(getAssets(), "fonts/digital.ttf");
        clock = (TextView) findViewById(R.id.waiting_rfid_clock);
        clock.setTypeface(tf);

        // 산소 농도, 압력, 시간 값 불러오기
        SharedPreferences sharedPreferences = getSharedPreferences(Application_manager.DB_NAME, 0);
        val_oxygen = sharedPreferences.getInt(Application_manager.DB_VAL_OXYGEN, 0);
        val_pressure = sharedPreferences.getInt(Application_manager.DB_VAL_PRESSURE, 0);
        val_time = sharedPreferences.getInt(Application_manager.DB_VAL_TIME, 10);

        time_text = (TextView)findViewById(R.id.waiting_rfid_time_text);
        time_text.setTypeface(tf);
        oxygen_text = (TextView)findViewById(R.id.waiting_rfid_oxygen_text);
        oxygen_text.setTypeface(tf);
        pressure_text = (TextView)findViewById(R.id.waiting_rfid_pressure_text);
        pressure_text.setTypeface(tf);

        oxygen_text.setText(""+val_oxygen);
        pressure_text.setText(""+val_pressure);
        time_text.setText(""+val_time);

        Application_manager.SENSOR_TEMP = sharedPreferences.getInt(Application_manager.DB_TEMPERATURE, 0);
        Application_manager.SENSOR_TEMP_BED = sharedPreferences.getInt(Application_manager.DB_TEMPERATURE_BED, 0);
        textView_temperature = (TextView) findViewById(R.id.textView_rfid_temperature_above);
        textView_temperature_bed = (TextView) findViewById(R.id.textView_rfid_temperature_below);
        textView_temperature.setOnTouchListener(mTouchEvent);
        textView_temperature_bed.setOnTouchListener(mTouchEvent);

        waiting_setting_button = (ImageView)findViewById(R.id.waiting_rfid_setting_button);
        waiting_setting_button.setOnTouchListener(mTouchEvent);

        ImageView waiting_oxygen_up_button = (ImageView)findViewById(R.id.waiting_rfid_oxygen_up_button);
        ImageView waiting_oxygen_down_button = (ImageView)findViewById(R.id.waiting_rfid_oxygen_down_button);
        ImageView waiting_pressure_up_button = (ImageView)findViewById(R.id.waiting_rfid_pressure_up_button);
        ImageView waiting_pressure_down_button = (ImageView)findViewById(R.id.waiting_rfid_pressure_down_button);
        ImageView waiting_time_up_button = (ImageView)findViewById(R.id.waiting_rfid_time_up_button);
        ImageView waiting_time_down_button = (ImageView)findViewById(R.id.waiting_rfid_time_down_button);

        ImageView waiting_door_open_button = (ImageView)findViewById(R.id.waiting_rfid_dooropen_button);
        ImageView waiting_door_close_button = (ImageView)findViewById(R.id.waiting_rfid_doorclose_button);


        waiting_oxygen_up_button.setOnTouchListener(mTouchEvent);
        waiting_oxygen_down_button.setOnTouchListener(mTouchEvent);
        waiting_pressure_up_button.setOnTouchListener(mTouchEvent);
        waiting_pressure_down_button.setOnTouchListener(mTouchEvent);
        waiting_time_up_button.setOnTouchListener(mTouchEvent);
        waiting_time_down_button.setOnTouchListener(mTouchEvent);

        waiting_door_open_button.setOnTouchListener(mTouchEvent);
        waiting_door_close_button.setOnTouchListener(mTouchEvent);

        time_text.setOnTouchListener(mTouchEvent);


        resolveIntent(getIntent());
        mAdapter = NfcAdapter.getDefaultAdapter(this);
        mPendingIntent = PendingIntent.getActivity(this, 0,
                new Intent(this, getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);

        background = (ImageView) findViewById(R.id.activity_waiting_rfid_background);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Application_manager.setFullScreen(this);

        registReceiver();

        isRun = true;

        SharedPreferences sharedPreferences = getSharedPreferences(Application_manager.DB_NAME, 0);

        val_time = sharedPreferences.getInt(Application_manager.DB_VAL_TIME, 10);
        time_text.setText(Integer.toString(val_time));
        Log.i("JW", "onResume time = " + val_time);

        // 수온 불러오기
        textView_temperature.setText(""+Application_manager.SENSOR_TEMP);
        textView_temperature_bed.setText(""+Application_manager.SENSOR_TEMP_BED);

        // 앱이 실행될때 NFC 어댑터를 활성화 한다
        if (mAdapter != null) {

            mAdapter.enableForegroundDispatch(this, mPendingIntent, null, null);
        }

        // 화면에 보여질 때 센서값 불러오기
        runOnUiThread(new Runnable() {
            @Override
            public void run() {

                TextView textView_oxygen = (TextView) findViewById(R.id.textView_rfid_oxygen);
                textView_oxygen.setText(""+Application_manager.SENSOR_OXYGEN);

                TextView textView_humidity = (TextView) findViewById(R.id.textView_rfid_humidity);
                textView_humidity.setText(""+Application_manager.SENSOR_HUMIDITY);

                TextView textView_temperature = (TextView) findViewById(R.id.textView_rfid_temperature_above);
                textView_temperature.setText(""+Application_manager.SENSOR_TEMP);

                TextView textView_temperature_bed = (TextView) findViewById(R.id.textView_rfid_temperature_below);
                textView_temperature_bed.setText(""+Application_manager.SENSOR_TEMP_BED);
            }
        });
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

        SharedPreferences sharedPreferences = getSharedPreferences(Application_manager.DB_NAME, 0);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(Application_manager.DB_VAL_OXYGEN, val_oxygen);
        editor.putInt(Application_manager.DB_VAL_PRESSURE, val_pressure);
        editor.putInt(Application_manager.DB_VAL_TIME, val_time);
        editor.putInt(Application_manager.DB_TEMPERATURE, Application_manager.SENSOR_TEMP);
        editor.putInt(Application_manager.DB_TEMPERATURE_BED, Application_manager.SENSOR_TEMP_BED);
        editor.commit();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onStart() {
        super.onStart();
        isRun = true;
        Thread myThread = new Thread(new Runnable() {
            public void run() {
                while (true) {
                    try {
                        handler.sendMessage(handler.obtainMessage());
                        Thread.sleep(1000);
                    } catch (Throwable t) {
                    }
                }
            }
        });
        myThread.start();
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
            if (mode == 0) {

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

    public void changeFragment_working(int modeNum) {

        if (val_time > 0) {

            if (fragment_working == null) {

                fragment_working = new Fragment_working();
            }

            // 타이머 스레드가 동작중이지 않은 경우
            if (!fragment_working.getIsAlive()) {

                if (Application_manager.getSoundManager().play(Application_manager.m_language, 0) == 0) {

                    Log.i("JW", "changeFragment (waiting_rfid -> working)");
                    FragmentManager fm = getFragmentManager();
                    FragmentTransaction fragmentTransaction = fm.beginTransaction();

                    fragment_working.init(modeNum, val_time, 1);

                    fragmentTransaction.replace(R.id.frameLayout_rfid_fragment, fragment_working);
                    fragmentTransaction.show(fragment_working);
                    fragmentTransaction.commit();

                    mode = 1;
                    handler_update_data.sendEmptyMessage(SET_BUTTON_INVISIBLE);

                    // 동작 모드로 바뀌기 이전 산소농도, 수압, 시간 값 저장
                    SharedPreferences sharedPreferences = getSharedPreferences(Application_manager.DB_NAME, 0);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putInt(Application_manager.DB_VAL_OXYGEN, val_oxygen);
                    editor.putInt(Application_manager.DB_VAL_PRESSURE, val_pressure);
                    editor.putInt(Application_manager.DB_VAL_TIME, val_time);
                    editor.commit();

                    // 애니메이션 시작
                    start_animation();

                    // 동작 구간 표시
                    CustomProgressBarHorizontal seekBar = (CustomProgressBarHorizontal) findViewById(R.id.seekBar2_rfid);
                    seekBar.setVisibility(View.VISIBLE);
                }
            }
            // 타이머 스레드가 아직 동작중인 경우
            else {

                Log.i("JW", "changeFragment (waiting_rfid -> working) is failed");
                Toast.makeText(this, "잠시 후 다시 시도해주세요", Toast.LENGTH_SHORT).show();
            }
        }
        else {

            Toast.makeText(this, "동작 시간을 1~90분 사이로 설정해야합니다", Toast.LENGTH_SHORT).show();
        }
    }

    public void changeFragment_waiting() {

        Log.i("JW", "changeFragment (working -> waiting_rfid)");
        setTimeLeft(val_time);
        FragmentManager fm = getFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();

        fragmentTransaction.hide(fragment_working);
        fragmentTransaction.commit();

        mode = 0;
        handler_update_data.sendEmptyMessage(SET_BUTTON_VISIBLE);

        // 동작 시작 전 산소 농도, 압력, 시간 값 불러오기
        SharedPreferences sharedPreferences = getSharedPreferences(Application_manager.DB_NAME, 0);
        val_oxygen = sharedPreferences.getInt(Application_manager.DB_VAL_OXYGEN, 0);
        val_pressure = sharedPreferences.getInt(Application_manager.DB_VAL_PRESSURE, 0);
        val_time = sharedPreferences.getInt(Application_manager.DB_VAL_TIME, 10);

        runOnUiThread(new Runnable() {
            @Override
            public void run() {

                oxygen_text.setText(""+val_oxygen);
                pressure_text.setText(""+val_pressure);
                time_text.setText(""+val_time);
            }
        });

        // 애니메이션 정지
        stop_animation();

        // 동작 구간 숨김
        CustomProgressBarHorizontal seekBar = (CustomProgressBarHorizontal) findViewById(R.id.seekBar2_rfid);
        seekBar.setVisibility(View.INVISIBLE);
    }

    private void start_animation() {

        background.setBackgroundResource(R.drawable.animation_working);
        frameAnimation = (AnimationDrawable) background.getBackground();
        frameAnimation.start();
    }

    private void stop_animation() {

        if (frameAnimation != null) {

            frameAnimation.stop();
            // 도어 열림
            if (Application_manager.getCommunicator().get_tx_idx(11) == 0x01) {

                background.setBackgroundResource(R.drawable.waiting_rfid_dooropen_back);
            }
            // 도어 닫힘
            else {

                background.setBackgroundResource(R.drawable.waiting_rfid_doorclose_back);
            }
        }
    }

    public void setTimeLeft(final int min) {

        runOnUiThread(new Runnable() {
            @Override
            public void run() {

                time_text.setText("" + min);
            }
        });
    }

    // 버퍼 데이터를 디코딩해서 String 으로 변환
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

    // NFC 태그 정보 수신 함수. 인텐트에 포함된 정보를 분석해서 화면에 표시
    @Override
    public void onNewIntent(Intent intent) {
        setIntent(intent);
        resolveIntent(intent);
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

                    SharedPreferences sharedPreferences = getSharedPreferences(Application_manager.DB_NAME, 0);

                    int[] onoff_flag = new int[12];
                    for (int i=1; i<=4; i++) { // 세로

                        for (int j = 1; j <= 3; j++) { // 가로

                            onoff_flag[((j-1)*4 + i - 1)] = sharedPreferences.getInt(Application_manager.DB_RFID_ONOFF + i + "" + j, 0);
                        }
                    }

                    // 산소농도 평균
                    int temp = 0;
                    for (int i=0; i<4; i++) {

                        if (onoff_flag[i] == 1) {

                            temp += communicator.get_rx_idx(i+7);
                        }
                    }
                    TextView textView_oxygen = (TextView) findViewById(R.id.textView_rfid_oxygen);
                    textView_oxygen.setText(""+temp);
                    Application_manager.SENSOR_OXYGEN = temp;

                    // 습도 평균
                    temp = 0;
                    for (int i=4; i<8; i++) {

                        if (onoff_flag[i] == 1) {

                            temp += communicator.get_rx_idx(i+7);
                        }
                    }
                    TextView textView_humidity = (TextView) findViewById(R.id.textView_rfid_humidity);
                    textView_humidity.setText(""+temp);
                    Application_manager.SENSOR_HUMIDITY = temp;

                    // 내부온도 평균
                    temp = 0;
                    for (int i=8; i<12; i++) {

                        if (onoff_flag[i] == 1) {

                            temp += communicator.get_rx_idx(i-5);
                        }
                    }
                    textView_temperature = (TextView) findViewById(R.id.textView_rfid_temperature_above);
                    textView_temperature.setText(""+temp);
                    Application_manager.SENSOR_TEMP = temp;

                    // 수온
                    temp = communicator.get_rx_idx(2);
                    textView_temperature_bed = (TextView) findViewById(R.id.textView_rfid_temperature_below);
                    textView_temperature_bed.setText(""+temp);
                    Application_manager.SENSOR_TEMP_BED = temp;

                    // 노즐 위치
                    //seekBar.setMinimumProgress();
                    //seekBar.setMaximumProgress();
                }
                else if (msg.what == SET_BUTTON_INVISIBLE) {

                    waiting_setting_button.setVisibility(View.INVISIBLE);
                }
                else if (msg.what == SET_BUTTON_VISIBLE) {

                    waiting_setting_button.setVisibility(View.VISIBLE);
                }
            }
        };
    }

    private View.OnTouchListener mTouchEvent = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            ImageView background;
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
                        view.setBackgroundResource(R.drawable.door_open_on);
                        break;
                    case R.id.waiting_rfid_doorclose_button:
                        view.setBackgroundResource(R.drawable.door_close_on);
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

                            val_oxygen++;
                            if (val_oxygen > 5) val_oxygen = 5;
                            oxygen_text.setText("" + val_oxygen);

                            val = (byte) val_oxygen;
                            communicator.set_tx(8, val);
                            communicator.send(communicator.get_tx());
                        break;
                    case R.id.waiting_rfid_oxygen_down_button:
                        view.setBackgroundResource(R.drawable.button_down);

                            val_oxygen--;
                            if (val_oxygen < 0) val_oxygen = 0;
                            oxygen_text.setText("" + val_oxygen);

                            val = (byte) val_oxygen;
                            communicator.set_tx(8, val);
                            communicator.send(communicator.get_tx());
                        break;
                    case R.id.waiting_rfid_pressure_up_button:
                        view.setBackgroundResource(R.drawable.button_up);

                            val_pressure += 1;
                            if (val_pressure > 6) val_pressure = 6;
                            pressure_text.setText("" + val_pressure);

                            communicator.set_tx(5, (byte) val_pressure);
                            communicator.send(communicator.get_tx());
                        break;
                    case R.id.waiting_rfid_pressure_down_button:
                        view.setBackgroundResource(R.drawable.button_down);

                            val_pressure -= 1;
                            if (val_pressure < 0) val_pressure = 0;
                            pressure_text.setText("" + val_pressure);

                            communicator.set_tx(5, (byte) val_pressure);
                            communicator.send(communicator.get_tx());
                        break;
                    case R.id.waiting_rfid_time_up_button:
                        view.setBackgroundResource(R.drawable.button_up);

                            val_time += 1;
                            if (val_time > 90) val_time = 90;
                            time_text.setText("" + val_time);
                        break;
                    case R.id.waiting_rfid_time_down_button:
                        view.setBackgroundResource(R.drawable.button_down);

                            val_time -= 1;
                            if (val_time < 1) val_time = 1;
                            time_text.setText("" + val_time);
                        break;
                    case R.id.waiting_rfid_dooropen_button:

                        view.setBackgroundResource(R.drawable.door_open_off);
                        if (Application_manager.getSoundManager().play(Application_manager.m_language, 3) == 0) {

                            background = (ImageView) findViewById(R.id.activity_waiting_rfid_background);
                            background.setBackgroundResource(R.drawable.waiting_rfid_dooropen_back);
                            isDoorOpened = true;

                            val = 0x01;
                            communicator.set_tx(11, val);
                            communicator.send(communicator.get_tx());
                        }
                        break;
                    case R.id.waiting_rfid_doorclose_button:

                        view.setBackgroundResource(R.drawable.door_close_off);
                        if (Application_manager.getSoundManager().play(Application_manager.m_language, 4) == 0) {
                            background = (ImageView) findViewById(R.id.activity_waiting_rfid_background);
                            background.setBackgroundResource(R.drawable.waiting_rfid_doorclose_back);
                            isDoorOpened = false;

                            val = 0x02;
                            communicator.set_tx(11, val);
                            communicator.send(communicator.get_tx());
                        }
                        break;
                    case R.id.waiting_rfid_time_text:

                        if (mode == 0) {
                            intent = new Intent(getApplicationContext(), Activity_waiting_working_time_popup.class);
                            intent.putExtra("mode", 0);
                            startActivity(intent);
                            Log.i("JW", "click up time = " + val_time);
                        }
                        break;
                    case R.id.textView_rfid_temperature_above:

                        intent = new Intent(getApplicationContext(), Activity_temperature_popup.class);
                        intent.putExtra("mode", 0);
                        intent.putExtra("temp", Application_manager.SENSOR_TEMP);
                        startActivity(intent);
                        break;
                    case R.id.textView_rfid_temperature_below:

                        intent = new Intent(getApplicationContext(), Activity_temperature_popup.class);
                        intent.putExtra("mode", 1);
                        intent.putExtra("temp", Application_manager.SENSOR_TEMP_BED);
                        startActivity(intent);
                        break;
                }
            }
            return true;
        }
    };

    // back키 작동 중지
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
