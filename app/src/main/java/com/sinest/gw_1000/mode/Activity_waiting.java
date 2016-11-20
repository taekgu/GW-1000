package com.sinest.gw_1000.mode;

import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        communicator = Application_communicator.getCommunicator();
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
        broadcastReceiver = new Application_broadcast(handler_update_data);
        IntentFilter mIntentFilter = new IntentFilter();
        mIntentFilter.addAction("update.data");
        //registerReceiver(broadcastReceiver, mIntentFilter);

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

        TextView time_text = (TextView)findViewById(R.id.waiting_time_text);

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
    protected void onPause() {
        super.onPause();
        //unregisterReceiver(broadcastReceiver);
    }

    private void registReceiver() {

        if (broadcastReceiver == null) {


        }
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
                        txt = (TextView)findViewById(R.id.waiting_oxygen_text);
                        t = (String)txt.getText();
                        temp = Integer.parseInt(t)+5;
                        t = Integer.toString(temp);
                        txt.setText(t);
                        break;
                    case R.id.waiting_oxygen_down_button:
                        b  = (Button) view;
                        b.setBackgroundResource(R.drawable.button_down);
                        txt = (TextView)findViewById(R.id.waiting_oxygen_text);
                        t = (String)txt.getText();
                        temp = Integer.parseInt(t)-5;
                        t = Integer.toString(temp);
                        txt.setText(t);
                        break;
                    case R.id.waiting_pressure_up_button:
                        b  = (Button) view;
                        b.setBackgroundResource(R.drawable.button_up);
                        txt = (TextView)findViewById(R.id.waiting_pressure_text);
                        t = (String)txt.getText();
                        temp = Integer.parseInt(t)+5;
                        t = Integer.toString(temp);
                        txt.setText(t);
                        break;
                    case R.id.waiting_pressure_down_button:
                        b  = (Button) view;
                        b.setBackgroundResource(R.drawable.button_down);
                        txt = (TextView)findViewById(R.id.waiting_pressure_text);
                        t = (String)txt.getText();
                        temp = Integer.parseInt(t)-5;
                        t = Integer.toString(temp);
                        txt.setText(t);
                        break;
                    case R.id.waiting_time_up_button:
                        b  = (Button) view;
                        b.setBackgroundResource(R.drawable.button_up);
                        txt = (TextView)findViewById(R.id.waiting_time_text);
                        t = (String)txt.getText();
                        temp = Integer.parseInt(t)+1;
                        t = Integer.toString(temp);
                        txt.setText(t);
                        break;
                    case R.id.waiting_time_down_button:
                        b  = (Button) view;
                        b.setBackgroundResource(R.drawable.button_down);
                        txt = (TextView)findViewById(R.id.waiting_time_text);
                        t = (String)txt.getText();
                        temp = Integer.parseInt(t)-1;
                        t = Integer.toString(temp);
                        txt.setText(t);
                        break;
                    case R.id.waiting_dooropen_button:
                        b  = (Button) view;
                        background = (LinearLayout)findViewById(R.id.waiting_background);
                        b.setBackgroundResource(R.drawable.door_open_off);
                        background.setBackgroundResource(R.drawable.waiting_dooropen_backimage);

                        byte val = 0x01;
                        communicator.set_tx(11, val);
                        communicator.send(communicator.get_tx());
                        break;
                    case R.id.waiting_doorclose_button:
                        b  = (Button) view;
                        background = (LinearLayout)findViewById(R.id.waiting_background);
                        b.setBackgroundResource(R.drawable.door_close_off);
                        background.setBackgroundResource(R.drawable.waiting_doorclose_backimage);
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
