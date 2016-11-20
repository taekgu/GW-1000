package com.sinest.gw_1000.mode;

import android.content.BroadcastReceiver;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;
import android.view.View;
import android.content.Intent;

import com.sinest.gw_1000.R;
import com.sinest.gw_1000.communication.Communicator;

public class Activity_waiting_rfid extends AppCompatActivity {

    public static final int REQUEST_CODE_LIBRARY = 1002;
    public static final int REQUEST_CODE_SETTING = 1003;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_waiting_rfid);

    }
    public void onClicked(View v)
    {
        Intent intent;
        switch(v.getId()){
            case R.id.waiting_library_button:
                intent = new Intent(getApplicationContext(), Activity_library.class);
                startActivityForResult(intent, REQUEST_CODE_LIBRARY);
                break;
            case R.id.waiting_setting_button:
                break;
            case R.id.waiting_dooropen_button:
                break;
            case R.id.waiting_doorclose_button:
                break;
            case R.id.waiting_pressure_up_button:
                break;
            case R.id.waiting_pressure_down_button:
                break;
            case R.id.waiting_time_up_button:
                break;
            case R.id.waiting_time_down_button:
                break;
        }
    }
}
