package com.sinest.gw_1000.setting;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.sinest.gw_1000.R;

public class Activity_rfidpassword_check extends AppCompatActivity {

    String ps = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rfidpassword_check);

        Intent ps_check = getIntent();
        ps = ps_check.getStringExtra("pr_pass");
        Log.v("test","ps : "+ps);



    }
}
