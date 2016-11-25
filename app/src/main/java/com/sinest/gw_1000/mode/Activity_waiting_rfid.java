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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_waiting_rfid);

        // 폰트 설정
        Typeface tf = Typeface.createFromAsset(getAssets(), "fonts/digital.ttf");
        TextClock clock = (TextClock) findViewById(R.id.waiting_rfid_clock);
        clock.setTypeface(tf);

        getDataFromDB();
        communicator = Application_manager.getCommunicator();

        Button waiting_library_button = (Button)findViewById(R.id.waiting_rfid_library_button);
        Button waiting_setting_button = (Button)findViewById(R.id.waiting_rfid_setting_button);

        Button waiting_oxygen_up_button = (Button)findViewById(R.id.waiting_rfid_oxygen_up_button);
        Button waiting_oxygen_down_button = (Button)findViewById(R.id.waiting_rfid_oxygen_down_button);
        Button waiting_pressure_up_button = (Button)findViewById(R.id.waiting_rfid_pressure_up_button);
        Button waiting_pressure_down_button = (Button)findViewById(R.id.waiting_rfid_pressure_down_button);
        Button waiting_time_up_button = (Button)findViewById(R.id.waiting_rfid_time_up_button);
        Button waiting_time_down_button = (Button)findViewById(R.id.waiting_rfid_time_down_button);

        Button waiting_door_open_button = (Button)findViewById(R.id.waiting_rfid_dooropen_button);
        Button waiting_door_close_button = (Button)findViewById(R.id.waiting_rfid_doorclose_button);

        time_text = (TextView)findViewById(R.id.waiting_rfid_time_text);
        oxygen_text = (TextView)findViewById(R.id.waiting_rfid_oxygen_text);
        pressure_text = (TextView)findViewById(R.id.waiting_rfid_pressure_text);
        oxygen_text.setText(""+val_oxygen);
        pressure_text.setText(""+val_pressure);
        time_text.setText(""+val_time);

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

                    int val = (communicator.get_rx_idx(7) + communicator.get_rx_idx(8) + communicator.get_rx_idx(9) + communicator.get_rx_idx(10)) / 4;
                    String temp = ""+ val;
                    TextView textView_oxygen = (TextView) findViewById(R.id.textView_rfid_oxygen);
                    textView_oxygen.setText(temp);

                    val = (communicator.get_rx_idx(11) + communicator.get_rx_idx(12) + communicator.get_rx_idx(13) + communicator.get_rx_idx(14)) / 4;
                    temp = ""+val;
                    TextView textView_humidity = (TextView) findViewById(R.id.textView_rfid_humidity);
                    textView_humidity.setText(temp);

                    val = (communicator.get_rx_idx(3) + communicator.get_rx_idx(4) + communicator.get_rx_idx(5) + communicator.get_rx_idx(6)) / 4;
                    temp = ""+val;
                    TextView textView_temperature = (TextView) findViewById(R.id.textView_rfid_temperature_above);
                    textView_temperature.setText(temp);

                    temp = ""+communicator.get_rx_idx(2);
                    TextView textView_temperature_bed = (TextView) findViewById(R.id.textView_rfid_temperature_below);
                    textView_temperature_bed.setText(temp);
                }
            }
        };
    }

    private void getDataFromDB() {

        SharedPreferences sharedPreferences = getSharedPreferences(Application_manager.NAME_OF_SHARED_PREF, 0);
        val_oxygen = sharedPreferences.getInt(Application_manager.VAL_OXYGEN, 20);
        val_pressure = sharedPreferences.getInt(Application_manager.VAL_PRESSURE, 1);
        val_time = sharedPreferences.getInt(Application_manager.VAL_TIME, 10);
    }

    private View.OnTouchListener mTouchEvent = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            Button b;
            TextView txt;
            LinearLayout background;
            Intent intent;
            Intent intent_setting;
            String t;
            int temp;
            int action = motionEvent.getAction();
            int id = view.getId();
            if (action == MotionEvent.ACTION_DOWN) {
                switch (id) {
                    case R.id.waiting_rfid_library_button:
                        b  = (Button) view;
                        b.setBackgroundResource(R.drawable.library_on);
                        break;
                    case R.id.waiting_rfid_setting_button:
                        b  = (Button) view;
                        b.setBackgroundResource(R.drawable.setting_on);
                        break;
                    case R.id.waiting_rfid_oxygen_up_button:
                        b  = (Button) view;
                        b.setBackgroundResource(R.drawable.button_up_on);
                        break;
                    case R.id.waiting_rfid_oxygen_down_button:
                        b  = (Button) view;
                        b.setBackgroundResource(R.drawable.button_down_on);
                        break;
                    case R.id.waiting_rfid_pressure_up_button:
                        b  = (Button) view;
                        b.setBackgroundResource(R.drawable.button_up_on);
                        break;
                    case R.id.waiting_rfid_pressure_down_button:
                        b  = (Button) view;
                        b.setBackgroundResource(R.drawable.button_down_on);
                        break;
                    case R.id.waiting_rfid_time_up_button:
                        b  = (Button) view;
                        b.setBackgroundResource(R.drawable.button_up_on);
                        break;
                    case R.id.waiting_rfid_time_down_button:
                        b  = (Button) view;
                        b.setBackgroundResource(R.drawable.button_down_on);
                        break;
                    case R.id.waiting_rfid_dooropen_button:
                        b  = (Button) view;
                        b.setBackgroundResource(R.drawable.door_open_on);
                        break;
                    case R.id.waiting_rfid_doorclose_button:
                        b  = (Button) view;
                        b.setBackgroundResource(R.drawable.door_close_on);
                        break;
                    case R.id.waiting_rfid_time_text:
                        break;
                }
            } else if (action == MotionEvent.ACTION_UP) {

                byte val = 0x00;
                switch (id) {
                    case R.id.waiting_rfid_library_button:
                        b  = (Button) view;
                        b.setBackgroundResource(R.drawable.library);
                        intent = new Intent(getApplicationContext(), Activity_library.class);
                        startActivity(intent);
                        break;
                    case R.id.waiting_rfid_setting_button:
                        b  = (Button) view;
                        b.setBackgroundResource(R.drawable.setting);
                        //setting
                        intent_setting = new Intent(getApplicationContext(), Activity_setting.class);
                        startActivity(intent_setting);
                        break;
                    case R.id.waiting_rfid_oxygen_up_button:
                        b  = (Button) view;
                        b.setBackgroundResource(R.drawable.button_up);
/*
                        val_oxygen += 5;
                        if (val_oxygen > 40) val_oxygen = 40;
*/
                        val_oxygen++;
                        if (val_oxygen > 5) val_oxygen = 5;
                        oxygen_text.setText(""+val_oxygen);

                        // 20:5:40 -> 1 ~ 5 값으로 변환해서 전송
//                        val = (byte) ((val_oxygen / 5) - 3);
                        val = (byte) val_oxygen;
                        communicator.set_tx(8, val);
                        communicator.send(communicator.get_tx());
                        break;
                    case R.id.waiting_rfid_oxygen_down_button:
                        b  = (Button) view;
                        b.setBackgroundResource(R.drawable.button_down);
/*
                        val_oxygen -= 5;
                        if (val_oxygen < 20) val_oxygen = 20;
*/
                        val_oxygen--;
                        if (val_oxygen < 0) val_oxygen = 0;
                        oxygen_text.setText(""+val_oxygen);

                        // 20:5:40 -> 1 ~ 5 값으로 변환해서 전송
//                        val = (byte) ((val_oxygen / 5) - 3);
                        val = (byte) val_oxygen;
                        communicator.set_tx(8, val);
                        communicator.send(communicator.get_tx());
                        break;
                    case R.id.waiting_rfid_pressure_up_button:
                        b  = (Button) view;
                        b.setBackgroundResource(R.drawable.button_up);

                        val_pressure += 1;
                        if (val_pressure > 6) val_pressure = 6;
                        pressure_text.setText(""+val_pressure);

                        communicator.set_tx(5, (byte)val_pressure);
                        communicator.send(communicator.get_tx());
                        break;
                    case R.id.waiting_rfid_pressure_down_button:
                        b  = (Button) view;
                        b.setBackgroundResource(R.drawable.button_down);

                        val_pressure -= 1;
                        if (val_pressure < 0) val_pressure = 0;
                        pressure_text.setText(""+val_pressure);

                        communicator.set_tx(5, (byte)val_pressure);
                        communicator.send(communicator.get_tx());
                        break;
                    case R.id.waiting_rfid_time_up_button:
                        b  = (Button) view;
                        b.setBackgroundResource(R.drawable.button_up);

                        val_time += 1;
                        if (val_time > 90) val_time = 90;
                        time_text.setText(""+val_time);
                        break;
                    case R.id.waiting_rfid_time_down_button:
                        b  = (Button) view;
                        b.setBackgroundResource(R.drawable.button_down);

                        val_time -= 1;
                        if (val_time < 1) val_time = 1;
                        time_text.setText(""+val_time);
                        break;
                    case R.id.waiting_rfid_dooropen_button:
                        b  = (Button) view;
                        background = (LinearLayout)findViewById(R.id.waiting_background);
                        b.setBackgroundResource(R.drawable.door_open_off);
                        background.setBackgroundResource(R.drawable.waiting_dooropen_backimage);

                        val = 0x01;
                        communicator.set_tx(11, val);
                        communicator.send(communicator.get_tx());

                        Application_manager.getSoundManager().play(Application_manager.ID_LANG_SOUND[Application_manager.LANGUAGE][3]);
                        break;
                    case R.id.waiting_rfid_doorclose_button:
                        b  = (Button) view;
                        background = (LinearLayout)findViewById(R.id.waiting_background);
                        b.setBackgroundResource(R.drawable.door_close_off);
                        background.setBackgroundResource(R.drawable.waiting_doorclose_backimage);

                        val = 0x02;
                        communicator.set_tx(11, val);
                        communicator.send(communicator.get_tx());

                        Application_manager.getSoundManager().play(Application_manager.ID_LANG_SOUND[Application_manager.LANGUAGE][4]);
                        break;
                    case R.id.waiting_rfid_time_text:
                        intent = new Intent(getApplicationContext(), Activity_waiting_working_time_popup.class);
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
}
