package com.sinest.gw_1000.splash;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;
import android.view.View;
import android.content.Intent;

import com.sinest.gw_1000.communication.Communicator;
import com.sinest.gw_1000.mode.Activity_waiting;

public class Activity_booting extends AppCompatActivity {

    public static final int REQUEST_CODE_ANOTHER = 1001;
    Communicator communicator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Intent intent = new Intent(getApplicationContext(), Activity_waiting.class);
        startActivityForResult(intent, REQUEST_CODE_ANOTHER);
        finish();
    }
}
