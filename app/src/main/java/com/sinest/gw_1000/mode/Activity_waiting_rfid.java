package com.sinest.gw_1000.mode;

import android.content.BroadcastReceiver;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextClock;
import android.widget.TextView;
import android.view.View;
import android.content.Intent;

import com.sinest.gw_1000.R;
import com.sinest.gw_1000.communication.Communicator;
import com.sinest.gw_1000.management.Application_broadcast;
import com.sinest.gw_1000.management.Application_manager;
import com.sinest.gw_1000.setting.Activity_setting;

public class Activity_waiting_rfid extends AppCompatActivity {

    Communicator communicator;
    Handler handler_update_data;
    BroadcastReceiver broadcastReceiver;

    private int val_oxygen = 0;
    private int val_pressure = 0;
    private int val_time = 0;

    TextView time_text, oxygen_text, pressure_text;

    private int mode = 0; // 0: waiting, 1: working

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_waiting_rfid);
        Application_manager.setFullScreen(this);

        communicator = Application_manager.getCommunicator();

        // 폰트 설정
        Typeface tf = Typeface.createFromAsset(getAssets(), "fonts/digital.ttf");
        TextClock clock = (TextClock) findViewById(R.id.waiting_rfid_clock);
        clock.setTypeface(tf);

        SharedPreferences sharedPreferences = getSharedPreferences(Application_manager.NAME_OF_SHARED_PREF, 0);
        val_oxygen = sharedPreferences.getInt(Application_manager.VAL_OXYGEN, 0);
        val_pressure = sharedPreferences.getInt(Application_manager.VAL_PRESSURE, 0);
        val_time = sharedPreferences.getInt(Application_manager.VAL_TIME, 10);

        time_text = (TextView)findViewById(R.id.waiting_rfid_time_text);
        time_text.setTypeface(tf);
        oxygen_text = (TextView)findViewById(R.id.waiting_rfid_oxygen_text);
        oxygen_text.setTypeface(tf);
        pressure_text = (TextView)findViewById(R.id.waiting_rfid_pressure_text);
        pressure_text.setTypeface(tf);

        oxygen_text.setText(""+val_oxygen);
        pressure_text.setText(""+val_pressure);
        time_text.setText(""+val_time);

        ImageView waiting_setting_button = (ImageView)findViewById(R.id.waiting_rfid_setting_button);
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
    }

    @Override
    protected void onResume() {
        super.onResume();
        Application_manager.setFullScreen(this);

        registReceiver();

        SharedPreferences sharedPreferences = getSharedPreferences(Application_manager.NAME_OF_SHARED_PREF, 0);

        val_time = sharedPreferences.getInt(Application_manager.WAITING_WORKING_TIME, 0);
        time_text.setText(Integer.toString(val_time));
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregistReceiver();
    }

    @Override
    protected void onStop() {
        super.onStop();
        SharedPreferences sharedPreferences = getSharedPreferences(Application_manager.NAME_OF_SHARED_PREF, 0);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(Application_manager.VAL_OXYGEN, val_oxygen);
        editor.putInt(Application_manager.VAL_PRESSURE, val_pressure);
        editor.putInt(Application_manager.VAL_TIME, val_time);
        Log.i("JW", ""+val_time);
        editor.commit();
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
                    TextView textView_oxygen = (TextView) findViewById(R.id.textView_rfid_oxygen);
                    textView_oxygen.setText(""+temp);

                    // 습도 평균
                    temp = 0;
                    for (int i=4; i<8; i++) {

                        if (onoff_flag[i] == 1) {

                            temp += communicator.get_rx_idx(i+7);
                        }
                    }
                    TextView textView_humidity = (TextView) findViewById(R.id.textView_rfid_humidity);
                    textView_humidity.setText(""+temp);

                    // 내부온도 평균
                    temp = 0;
                    for (int i=8; i<12; i++) {

                        if (onoff_flag[i] == 1) {

                            temp += communicator.get_rx_idx(i-5);
                        }
                    }
                    TextView textView_temperature = (TextView) findViewById(R.id.textView_rfid_temperature_above);
                    textView_temperature.setText(""+temp);

                    // 수온
                    temp = communicator.get_rx_idx(2);
                    TextView textView_temperature_bed = (TextView) findViewById(R.id.textView_rfid_temperature_below);
                    textView_temperature_bed.setText(""+temp);
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
                        break;
                    case R.id.waiting_rfid_oxygen_up_button:
                        view.setBackgroundResource(R.drawable.button_up);

                        if (mode == 0) {

                            val_oxygen++;
                            if (val_oxygen > 5) val_oxygen = 5;
                            oxygen_text.setText("" + val_oxygen);

                            val = (byte) val_oxygen;
                            communicator.set_tx(8, val);
                            communicator.send(communicator.get_tx());
                        }
                        break;
                    case R.id.waiting_rfid_oxygen_down_button:
                        view.setBackgroundResource(R.drawable.button_down);

                        if (mode == 0) {

                            val_oxygen--;
                            if (val_oxygen < 0) val_oxygen = 0;
                            oxygen_text.setText("" + val_oxygen);

                            val = (byte) val_oxygen;
                            communicator.set_tx(8, val);
                            communicator.send(communicator.get_tx());
                        }
                        break;
                    case R.id.waiting_rfid_pressure_up_button:
                        view.setBackgroundResource(R.drawable.button_up);

                        if (mode == 0) {

                            val_pressure += 1;
                            if (val_pressure > 6) val_pressure = 6;
                            pressure_text.setText("" + val_pressure);

                            communicator.set_tx(5, (byte) val_pressure);
                            communicator.send(communicator.get_tx());
                        }
                        break;
                    case R.id.waiting_rfid_pressure_down_button:
                        view.setBackgroundResource(R.drawable.button_down);

                        if (mode == 0) {

                            val_pressure -= 1;
                            if (val_pressure < 0) val_pressure = 0;
                            pressure_text.setText("" + val_pressure);

                            communicator.set_tx(5, (byte) val_pressure);
                            communicator.send(communicator.get_tx());
                        }
                        break;
                    case R.id.waiting_rfid_time_up_button:
                        view.setBackgroundResource(R.drawable.button_up);

                        if (mode == 0) {

                            val_time += 1;
                            if (val_time > 90) val_time = 90;
                            time_text.setText("" + val_time);
                        }
                        break;
                    case R.id.waiting_rfid_time_down_button:
                        view.setBackgroundResource(R.drawable.button_down);

                        if (mode == 0) {

                            val_time -= 1;
                            if (val_time < 1) val_time = 1;
                            time_text.setText("" + val_time);
                        }
                        break;
                    case R.id.waiting_rfid_dooropen_button:
                        background = (LinearLayout)findViewById(R.id.waiting_rfid_background);
                        view.setBackgroundResource(R.drawable.door_open_off);
                        background.setBackgroundResource(R.drawable.waiting_dooropen_backimage);

                        val = 0x01;
                        communicator.set_tx(11, val);
                        communicator.send(communicator.get_tx());

                        Application_manager.getSoundManager().play(Application_manager.ID_LANG_SOUND[Application_manager.LANGUAGE][3]);
                        break;
                    case R.id.waiting_rfid_doorclose_button:
                        background = (LinearLayout)findViewById(R.id.waiting_rfid_background);
                        view.setBackgroundResource(R.drawable.door_close_off);
                        background.setBackgroundResource(R.drawable.waiting_doorclose_backimage);

                        val = 0x02;
                        communicator.set_tx(11, val);
                        communicator.send(communicator.get_tx());

                        Application_manager.getSoundManager().play(Application_manager.ID_LANG_SOUND[Application_manager.LANGUAGE][4]);
                        break;
                    case R.id.waiting_rfid_time_text:

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

    // back키 작동 중지
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event){
        switch(keyCode){
            case KeyEvent.KEYCODE_BACK:
                return false;
        }
        return super.onKeyDown(keyCode, event);
    }
}
