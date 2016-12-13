package com.sinest.gw_1000.mode;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextClock;
import android.widget.TextView;
import android.widget.Toast;

import com.sinest.gw_1000.R;
import com.sinest.gw_1000.communication.Communicator;
import com.sinest.gw_1000.management.Application_broadcast;
import com.sinest.gw_1000.management.Application_manager;
import com.sinest.gw_1000.mode.utils.CustomTextClock;
import com.sinest.gw_1000.setting.Activity_setting;

public class Activity_waiting extends AppCompatActivity {

    public static final int REQUEST_CODE_WORKINGTIME_POPUP  = 1001;
    private final static int SET_BUTTON_INVISIBLE           = 1002;
    private final static int SET_BUTTON_VISIBLE             = 1003;

    Communicator communicator;
    Handler handler_update_data;
    BroadcastReceiver broadcastReceiver;

    private int val_oxygen = 0;
    private int val_pressure = 0;
    private int val_time = 0;

    TextView time_text, oxygen_text, pressure_text;
    int[] checked_loc = new int[Application_manager.MAX_CHECKED];
    Fragment_waiting fragment_waiting;
    Fragment_working fragment_working;

    ImageView waiting_library_button;
    ImageView waiting_setting_button;

    CustomTextClock clock;

    private int mode = 0; // 0: waiting, 1: working

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_waiting);
        Application_manager.setFullScreen(this);

        communicator = Application_manager.getCommunicator();

        // 폰트 설정
        Typeface tf = Typeface.createFromAsset(getAssets(), "fonts/digital.ttf");
        //TextClock clock = (TextClock) findViewById(R.id.waiting_clock);
        //clock.setTypeface(tf);
        clock = (CustomTextClock) findViewById(R.id.waiting_clock);

        // 산소 농도, 압력, 시간 값 불러오기
        SharedPreferences sharedPreferences = getSharedPreferences(Application_manager.NAME_OF_SHARED_PREF, 0);
        val_oxygen = sharedPreferences.getInt(Application_manager.VAL_OXYGEN, 0);
        val_pressure = sharedPreferences.getInt(Application_manager.VAL_PRESSURE, 0);
        val_time = sharedPreferences.getInt(Application_manager.VAL_TIME, 10);

        time_text = (TextView)findViewById(R.id.waiting_time_text);
        time_text.setTypeface(tf);
        oxygen_text = (TextView)findViewById(R.id.waiting_oxygen_text);
        oxygen_text.setTypeface(tf);
        pressure_text = (TextView)findViewById(R.id.waiting_pressure_text);
        pressure_text.setTypeface(tf);

        oxygen_text.setText(""+val_oxygen);
        pressure_text.setText(""+val_pressure);
        time_text.setText(""+val_time);


        fragment_waiting = new Fragment_waiting();
        for (int i = 0; i< Application_manager.MAX_CHECKED; i++) {

            checked_loc[i] = sharedPreferences.getInt(Application_manager.LIBRARY_LOC_ + i, i);
            fragment_waiting.addCheckedIdx(checked_loc[i]);
            Log.i("JW", "Selected library idx : " + checked_loc[i]);
        }

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

        ImageView waiting_door_open_button = (ImageView)findViewById(R.id.waiting_dooropen_button);
        ImageView waiting_door_close_button = (ImageView)findViewById(R.id.waiting_doorclose_button);

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
    }

    @Override
    protected void onResume() {
        super.onResume();
        Application_manager.setFullScreen(this);

        registReceiver();
        clock.registReceiver();
        clock.doInit_time();

        SharedPreferences sharedPreferences = getSharedPreferences(Application_manager.NAME_OF_SHARED_PREF, 0);

        fragment_waiting.reset();
        for (int i = 0; i< Application_manager.MAX_CHECKED; i++) {

            checked_loc[i] = sharedPreferences.getInt(Application_manager.LIBRARY_LOC_ + i, i);
            fragment_waiting.addCheckedIdx(checked_loc[i]);
            Log.i("JW", "Selected library idx : " + checked_loc[i]);
        }
        fragment_waiting.refresh();

        val_time = sharedPreferences.getInt(Application_manager.VAL_TIME, 10);
        time_text.setText(Integer.toString(val_time));
        /*
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.frameLayout_fragment, fragment_waiting);
        fragmentTransaction.commit();*/
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregistReceiver();
        clock.unregistReceiver();
    }

    @Override
    protected void onStop() {
        super.onStop();
        SharedPreferences sharedPreferences = getSharedPreferences(Application_manager.NAME_OF_SHARED_PREF, 0);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(Application_manager.VAL_OXYGEN, val_oxygen);
        editor.putInt(Application_manager.VAL_PRESSURE, val_pressure);
        editor.putInt(Application_manager.VAL_TIME, val_time);
        editor.commit();
    }

    public void changeFragment_working(int modeNum) {

        if (val_time > 0) {

            if (fragment_working == null) {

                fragment_working = new Fragment_working();
            }

            // 타이머 스레드가 동작중이지 않은 경우
            if (!fragment_working.getIsAlive()) {

                Log.i("JW", "changeFragment (waiting -> working)");
                Application_manager.getSoundManager().play(Application_manager.ID_LANG_SOUND[Application_manager.LANGUAGE][0]);
                FragmentManager fm = getFragmentManager();
                FragmentTransaction fragmentTransaction = fm.beginTransaction();

                fragment_working.init(modeNum, val_time);

                fragmentTransaction.replace(R.id.frameLayout_fragment, fragment_working);
                fragmentTransaction.commit();

                mode = 1;
                handler_update_data.sendEmptyMessage(SET_BUTTON_INVISIBLE);

                // 동작 모드로 바뀌기 이전 산소농도, 수압, 시간 값 저장
                SharedPreferences sharedPreferences = getSharedPreferences(Application_manager.NAME_OF_SHARED_PREF, 0);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putInt(Application_manager.VAL_OXYGEN, val_oxygen);
                editor.putInt(Application_manager.VAL_PRESSURE, val_pressure);
                editor.putInt(Application_manager.VAL_TIME, val_time);
                editor.commit();
            }
            // 타이머 스레드가 아직 동작중인 경우
            else {

                Log.i("JW", "changeFragment (waiting -> working) is failed");
                Toast.makeText(this, "잠시 후 다시 시도해주세요", Toast.LENGTH_SHORT).show();
            }
        }
        else {

            Toast.makeText(this, "동작 시간을 1~90분 사이로 설정해야합니다", Toast.LENGTH_SHORT).show();
        }
    }

    public void changeFragment_waiting() {


        Log.i("JW", "changeFragment (working -> waiting)");
        setTimeLeft(val_time);
        FragmentManager fm = getFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();

        fragmentTransaction.replace(R.id.frameLayout_fragment, fragment_waiting);
        fragmentTransaction.commit();

        mode = 0;
        handler_update_data.sendEmptyMessage(SET_BUTTON_VISIBLE);

        // 동작 시작 전 산소 농도, 압력, 시간 값 불러오기
        SharedPreferences sharedPreferences = getSharedPreferences(Application_manager.NAME_OF_SHARED_PREF, 0);
        val_oxygen = sharedPreferences.getInt(Application_manager.VAL_OXYGEN, 0);
        val_pressure = sharedPreferences.getInt(Application_manager.VAL_PRESSURE, 0);
        val_time = sharedPreferences.getInt(Application_manager.VAL_TIME, 10);

        runOnUiThread(new Runnable() {
            @Override
            public void run() {

                oxygen_text.setText(""+val_oxygen);
                pressure_text.setText(""+val_pressure);
                time_text.setText(""+val_time);
            }
        });
    }

    public void setTimeLeft(final int min) {

        runOnUiThread(new Runnable() {
            @Override
            public void run() {

                time_text.setText("" + min);
            }
        });
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

                    SharedPreferences sharedPreferences = getSharedPreferences(Application_manager.NAME_OF_SHARED_PREF, 0);

                    int[] onoff_flag = new int[12];
                    for (int i=1; i<=4; i++) { // 세로

                        for (int j = 1; j <= 3; j++) { // 가로

                            onoff_flag[((j-1)*4 + i - 1)] = sharedPreferences.getInt(Application_manager.RFID_ONOFF + i + "" + j, 0);
                        }
                    }

                    // 산소농도 평균
                    int temp = 0;
                    for (int i=0; i<4; i++) {

                        if (onoff_flag[i] == 1) {

                            temp += communicator.get_rx_idx(i+7);
                        }
                    }
                    TextView textView_oxygen = (TextView) findViewById(R.id.textView_oxygen);
                    textView_oxygen.setText(""+temp);

                    // 습도 평균
                    temp = 0;
                    for (int i=4; i<8; i++) {

                        if (onoff_flag[i] == 1) {

                            temp += communicator.get_rx_idx(i+7);
                        }
                    }
                    TextView textView_humidity = (TextView) findViewById(R.id.textView_humidity);
                    textView_humidity.setText(""+temp);

                    // 내부온도 평균
                    temp = 0;
                    for (int i=8; i<12; i++) {

                        if (onoff_flag[i] == 1) {

                            temp += communicator.get_rx_idx(i-5);
                        }
                    }
                    TextView textView_temperature = (TextView) findViewById(R.id.textView_temperature_above);
                    textView_temperature.setText(""+temp);

                    // 수온
                    temp = communicator.get_rx_idx(2);
                    TextView textView_temperature_bed = (TextView) findViewById(R.id.textView_temperature_below);
                    textView_temperature_bed.setText(""+temp);
                }
                else if (msg.what == SET_BUTTON_INVISIBLE) {

                    waiting_library_button.setVisibility(View.INVISIBLE);
                    waiting_setting_button.setVisibility(View.INVISIBLE);
                }
                else if (msg.what == SET_BUTTON_VISIBLE) {

                    waiting_library_button.setVisibility(View.VISIBLE);
                    waiting_setting_button.setVisibility(View.VISIBLE);
                }
            }
        };
    }

    private View.OnTouchListener mTouchEvent = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            LinearLayout background;
            Intent intent;
            Intent intent_setting;
            int action = motionEvent.getAction();
            int id = view.getId();
            if (action == MotionEvent.ACTION_DOWN) {
                switch (id) {
                    case R.id.waiting_library_button:
                        view.setBackgroundResource(R.drawable.library_on);
                        break;
                    case R.id.waiting_setting_button:
                        //iv  = (ImageView) view;
                        view.setBackgroundResource(R.drawable.setting_on);
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
                        view.setBackgroundResource(R.drawable.door_open_on);
                        break;
                    case R.id.waiting_doorclose_button:
                        view.setBackgroundResource(R.drawable.door_close_on);
                        break;
                    case R.id.waiting_time_text:
                        break;
                }
            } else if (action == MotionEvent.ACTION_UP) {

                byte val = 0x00;
                switch (id) {
                    case R.id.waiting_library_button:
                        view.setBackgroundResource(R.drawable.library);
                        intent = new Intent(getApplicationContext(), Activity_library.class);
                        startActivity(intent);
                        break;
                    case R.id.waiting_setting_button:
                        view.setBackgroundResource(R.drawable.setting);
                        //setting
                        intent_setting = new Intent(getApplicationContext(), Activity_setting.class);
                        startActivity(intent_setting);
                        break;
                    case R.id.waiting_oxygen_up_button:
                        view.setBackgroundResource(R.drawable.button_up);

                        //if (mode == 0) {

                            val_oxygen++;
                            if (val_oxygen > 5) val_oxygen = 5;
                            oxygen_text.setText("" + val_oxygen);

                            val = (byte) val_oxygen;
                            communicator.set_tx(8, val);
                            communicator.send(communicator.get_tx());
                        //}
                        break;
                    case R.id.waiting_oxygen_down_button:
                        view.setBackgroundResource(R.drawable.button_down);

                        //if (mode == 0) {

                            val_oxygen--;
                            if (val_oxygen < 0) val_oxygen = 0;
                            oxygen_text.setText("" + val_oxygen);

                            val = (byte) val_oxygen;
                            communicator.set_tx(8, val);
                            communicator.send(communicator.get_tx());
                        //}
                        break;
                    case R.id.waiting_pressure_up_button:
                        view.setBackgroundResource(R.drawable.button_up);

                        //if (mode == 0) {

                            val_pressure += 1;
                            if (val_pressure > 6) val_pressure = 6;
                            pressure_text.setText("" + val_pressure);

                            communicator.set_tx(5, (byte) val_pressure);
                            communicator.send(communicator.get_tx());
                        //}
                        break;
                    case R.id.waiting_pressure_down_button:
                        view.setBackgroundResource(R.drawable.button_down);

                        //if (mode == 0) {

                            val_pressure -= 1;
                            if (val_pressure < 0) val_pressure = 0;
                            pressure_text.setText("" + val_pressure);

                            communicator.set_tx(5, (byte) val_pressure);
                            communicator.send(communicator.get_tx());
                        //}
                        break;
                    case R.id.waiting_time_up_button:
                        view.setBackgroundResource(R.drawable.button_up);

                        val_time += 1;
                        if (val_time > 90) val_time = 90;
                        time_text.setText("" + val_time);

                        if (mode == 1) {

                            fragment_working.setTime_m_left(val_time);
                        }
                        break;
                    case R.id.waiting_time_down_button:
                        view.setBackgroundResource(R.drawable.button_down);

                        val_time -= 1;
                        if (val_time < 1) val_time = 1;
                        time_text.setText("" + val_time);

                        if (mode == 1) {

                            fragment_working.setTime_m_left(val_time);
                        }
                        break;
                    case R.id.waiting_dooropen_button:
                        background = (LinearLayout)findViewById(R.id.waiting_background);
                        view.setBackgroundResource(R.drawable.door_open_off);
                        background.setBackgroundResource(R.drawable.waiting_dooropen_backimage);

                        val = 0x01;
                        communicator.set_tx(11, val);
                        communicator.send(communicator.get_tx());

                        Application_manager.getSoundManager().play(Application_manager.ID_LANG_SOUND[Application_manager.LANGUAGE][3]);
                        break;
                    case R.id.waiting_doorclose_button:
                        background = (LinearLayout)findViewById(R.id.waiting_background);
                        view.setBackgroundResource(R.drawable.door_close_off);
                        background.setBackgroundResource(R.drawable.waiting_doorclose_backimage);

                        val = 0x02;
                        communicator.set_tx(11, val);
                        communicator.send(communicator.get_tx());

                        Application_manager.getSoundManager().play(Application_manager.ID_LANG_SOUND[Application_manager.LANGUAGE][4]);
                        break;
                    case R.id.waiting_time_text:

                        if (mode == 0) {

                            intent = new Intent(getApplicationContext(), Activity_waiting_working_time_popup.class);
                            intent.putExtra("mode", 0);
                            startActivity(intent);
                        }
                        break;
                }
            }
            return true;
        }
    };
}
