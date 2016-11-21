package com.sinest.gw_1000.mode;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sinest.gw_1000.R;
import com.sinest.gw_1000.communication.Communicator;
import com.sinest.gw_1000.management.Application_broadcast;
import com.sinest.gw_1000.management.Application_communicator;
import com.sinest.gw_1000.setting.Activity_setting;

public class Activity_waiting extends AppCompatActivity {

    public static final int REQUEST_CODE_WORKINGTIME_POPUP = 1001;
    public static final int REQUEST_CODE_LIBRARY = 1002;
    public static final int REQUEST_CODE_SETTING = 1003;

    Communicator communicator;
    Handler handler_update_data;
    BroadcastReceiver broadcastReceiver;

    private int val_oxygen = 0;
    private int val_pressure = 0;
    private int val_time = 0;

    TextView time_text, oxygen_text, pressure_text;
    int[] checked_loc = new int[20];
    Fragment_waiting fragment_waiting;
    Fragment_working fragment_working;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences sharedPreferences = getSharedPreferences(Application_communicator.NAME_OF_SHARED_PREF, 0);
        val_oxygen = sharedPreferences.getInt(Application_communicator.VAL_OXYGEN, 20);
        val_pressure = sharedPreferences.getInt(Application_communicator.VAL_PRESSURE, 1);
        val_time = sharedPreferences.getInt(Application_communicator.VAL_TIME, 10);

        communicator = Application_communicator.getCommunicator();

        fragment_waiting = new Fragment_waiting();
        for (int i=0; i<20; i++) {

            checked_loc[i] = sharedPreferences.getInt(Application_communicator.LIBRARY_LOC + i, 0);
            if (checked_loc[i] == 1) {

                fragment_waiting.addCheckedIdx(i);
                Log.i("WIFI", "addCheckedIdx " + i);
            }
        }

        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.frameLayout_fragment, fragment_waiting);
        fragmentTransaction.commit();

        setContentView(R.layout.activity_waiting);

        Button waiting_library_button = (Button)findViewById(R.id.waiting_library_button);
        Button waiting_setting_button = (Button)findViewById(R.id.waiting_setting_button);

        Button waiting_oxygen_up_button = (Button)findViewById(R.id.waiting_oxygen_up_button);
        Button waiting_oxygen_down_button = (Button)findViewById(R.id.waiting_oxygen_down_button);
        Button waiting_pressure_up_button = (Button)findViewById(R.id.waiting_pressure_up_button);
        Button waiting_pressure_down_button = (Button)findViewById(R.id.waiting_pressure_down_button);
        Button waiting_time_up_button = (Button)findViewById(R.id.waiting_time_up_button);
        Button waiting_time_down_button = (Button)findViewById(R.id.waiting_time_down_button);

        Button waiting_door_open_button = (Button)findViewById(R.id.waiting_dooropen_button);
        Button waiting_door_close_button = (Button)findViewById(R.id.waiting_doorclose_button);

        time_text = (TextView)findViewById(R.id.waiting_time_text);
        oxygen_text = (TextView)findViewById(R.id.waiting_oxygen_text);
        pressure_text = (TextView)findViewById(R.id.waiting_pressure_text);
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

        SharedPreferences sharedPreferences = getSharedPreferences(Application_communicator.NAME_OF_SHARED_PREF, 0);

        fragment_waiting.reset();
        for (int i=0; i<20; i++) {

            checked_loc[i] = 0;
            checked_loc[i] = sharedPreferences.getInt(Application_communicator.LIBRARY_LOC + i, 0);
            if (checked_loc[i] == 1) {

                fragment_waiting.addCheckedIdx(i);
            //    Log.i("WIFI", "addCheckedIdx " + i);
            }
        }

        fragment_waiting.refresh();

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
    }

    @Override
    protected void onStop() {
        super.onStop();
        SharedPreferences sharedPreferences = getSharedPreferences(Application_communicator.NAME_OF_SHARED_PREF, 0);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(Application_communicator.VAL_OXYGEN, val_oxygen);
        editor.putInt(Application_communicator.VAL_PRESSURE, val_pressure);
        editor.putInt(Application_communicator.VAL_TIME, val_time);
        editor.commit();
    }

    public void changeFragment_working(int modeNum) {

        Log.i("WIFI", "changeFragment");
        FragmentManager fm = getFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();

        if (fragment_working == null) {

            fragment_working = new Fragment_working();
        }
        fragment_working.setModeNum(modeNum);

        fragmentTransaction.replace(R.id.frameLayout_fragment, fragment_working);
        fragmentTransaction.commit();
    }

    public void changeFragment_waiting() {

        Log.i("WIFI", "changeFragment");
        FragmentManager fm = getFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();

        fragmentTransaction.replace(R.id.frameLayout_fragment, fragment_waiting);
        fragmentTransaction.commit();
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
        Log.i("WIFI", "registerReceiver");
    }

    private void unregistReceiver() {

        if (broadcastReceiver != null) {

            this.unregisterReceiver(broadcastReceiver);
            broadcastReceiver = null;
            Log.i("WIFI", "unregisterReceiver");
        }
    }

    private void setHandler_update_data() {

        handler_update_data = new Handler() {

            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);

                if (msg.what == 1) {

                    String temp = ""+communicator.get_rx_idx(7);
                    TextView textView_oxygen = (TextView) findViewById(R.id.textView_oxygen);
                    textView_oxygen.setText(temp);

                    temp = ""+communicator.get_rx_idx(11);
                    TextView textView_humidity = (TextView) findViewById(R.id.textView_humidity);
                    textView_humidity.setText(temp);

                    temp = ""+communicator.get_rx_idx(3);
                    TextView textView_temperature = (TextView) findViewById(R.id.textView_temperature);
                    textView_temperature.setText(temp);

                    temp = ""+communicator.get_rx_idx(2);
                    TextView textView_temperature_bed = (TextView) findViewById(R.id.textView_temperature_bed);
                    textView_temperature_bed.setText(temp);
                }
            }
        };
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
                    case R.id.waiting_library_button:
                        b  = (Button) view;
                        b.setBackgroundResource(R.drawable.library_on);
                        break;
                    case R.id.waiting_setting_button:
                        b  = (Button) view;
                        b.setBackgroundResource(R.drawable.setting_on);
                        break;
                    case R.id.waiting_oxygen_up_button:
                        b  = (Button) view;
                        b.setBackgroundResource(R.drawable.button_up_on);
                        break;
                    case R.id.waiting_oxygen_down_button:
                        b  = (Button) view;
                        b.setBackgroundResource(R.drawable.button_down_on);
                        break;
                    case R.id.waiting_pressure_up_button:
                        b  = (Button) view;
                        b.setBackgroundResource(R.drawable.button_up_on);
                        break;
                    case R.id.waiting_pressure_down_button:
                        b  = (Button) view;
                        b.setBackgroundResource(R.drawable.button_down_on);
                        break;
                    case R.id.waiting_time_up_button:
                        b  = (Button) view;
                        b.setBackgroundResource(R.drawable.button_up_on);
                        break;
                    case R.id.waiting_time_down_button:
                        b  = (Button) view;
                        b.setBackgroundResource(R.drawable.button_down_on);
                        break;
                    case R.id.waiting_dooropen_button:
                        b  = (Button) view;
                        b.setBackgroundResource(R.drawable.door_open_on);
                        break;
                    case R.id.waiting_doorclose_button:
                        b  = (Button) view;
                        b.setBackgroundResource(R.drawable.door_close_on);
                        break;
                    case R.id.waiting_time_text:
                        break;
                }
            } else if (action == MotionEvent.ACTION_UP) {

                byte val = 0x00;
                switch (id) {
                    case R.id.waiting_library_button:
                        b  = (Button) view;
                        b.setBackgroundResource(R.drawable.library);
                        intent = new Intent(getApplicationContext(), Activity_library.class);
                        startActivityForResult(intent, REQUEST_CODE_LIBRARY);
                        break;
                    case R.id.waiting_setting_button:
                        b  = (Button) view;
                        b.setBackgroundResource(R.drawable.setting);
                        //setting
                        intent_setting = new Intent(getApplicationContext(), Activity_setting.class);
                        startActivityForResult(intent_setting, REQUEST_CODE_LIBRARY);
                        break;
                    case R.id.waiting_oxygen_up_button:
                        b  = (Button) view;
                        b.setBackgroundResource(R.drawable.button_up);

                        val_oxygen += 5;
                        if (val_oxygen > 40) val_oxygen = 40;
                        oxygen_text.setText(""+val_oxygen);

                        // 20:5:40 -> 1 ~ 5 값으로 변환해서 전송
                        val = (byte) ((val_oxygen / 5) - 3);
                        communicator.set_tx(8, val);
                        communicator.send(communicator.get_tx());
                        break;
                    case R.id.waiting_oxygen_down_button:
                        b  = (Button) view;
                        b.setBackgroundResource(R.drawable.button_down);

                        val_oxygen -= 5;
                        if (val_oxygen < 20) val_oxygen = 20;
                        oxygen_text.setText(""+val_oxygen);

                        // 20:5:40 -> 1 ~ 5 값으로 변환해서 전송
                        val = (byte) ((val_oxygen / 5) - 3);
                        communicator.set_tx(8, val);
                        communicator.send(communicator.get_tx());
                        break;
                    case R.id.waiting_pressure_up_button:
                        b  = (Button) view;
                        b.setBackgroundResource(R.drawable.button_up);

                        val_pressure += 1;
                        if (val_pressure > 6) val_pressure = 6;
                        pressure_text.setText(""+val_pressure);

                        communicator.set_tx(5, (byte)val_pressure);
                        communicator.send(communicator.get_tx());
                        break;
                    case R.id.waiting_pressure_down_button:
                        b  = (Button) view;
                        b.setBackgroundResource(R.drawable.button_down);

                        val_pressure -= 1;
                        if (val_pressure < 1) val_pressure = 1;
                        pressure_text.setText(""+val_pressure);

                        communicator.set_tx(5, (byte)val_pressure);
                        communicator.send(communicator.get_tx());
                        break;
                    case R.id.waiting_time_up_button:
                        b  = (Button) view;
                        b.setBackgroundResource(R.drawable.button_up);

                        val_time += 1;
                        if (val_time > 90) val_time = 90;
                        time_text.setText(""+val_time);
                        break;
                    case R.id.waiting_time_down_button:
                        b  = (Button) view;
                        b.setBackgroundResource(R.drawable.button_down);

                        val_time -= 1;
                        if (val_time < 1) val_time = 1;
                        time_text.setText(""+val_time);
                        break;
                    case R.id.waiting_dooropen_button:
                        b  = (Button) view;
                        background = (LinearLayout)findViewById(R.id.waiting_background);
                        b.setBackgroundResource(R.drawable.door_open_off);
                        background.setBackgroundResource(R.drawable.waiting_dooropen_backimage);

                        val = 0x01;
                        communicator.set_tx(11, val);
                        communicator.send(communicator.get_tx());
                        break;
                    case R.id.waiting_doorclose_button:
                        b  = (Button) view;
                        background = (LinearLayout)findViewById(R.id.waiting_background);
                        b.setBackgroundResource(R.drawable.door_close_off);
                        background.setBackgroundResource(R.drawable.waiting_doorclose_backimage);

                        val = 0x02;
                        communicator.set_tx(11, val);
                        communicator.send(communicator.get_tx());
                        break;
                    case R.id.waiting_time_text:
                        intent = new Intent(getApplicationContext(), Activity_waiting_working_time_popup.class);
                        startActivityForResult(intent, REQUEST_CODE_WORKINGTIME_POPUP);
                        break;
                }
            }
            return true;
        }
    };
    private View.OnClickListener mClickListener = new View.OnClickListener() {
        public void onClick(View v) {
        }
    };
}
