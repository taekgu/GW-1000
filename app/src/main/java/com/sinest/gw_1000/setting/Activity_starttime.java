package com.sinest.gw_1000.setting;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import com.sinest.gw_1000.R;

public class Activity_starttime extends Activity {

    Button start_time1; Button start_time2; Button start_time3;
    Button start_time4; Button start_time5; Button start_time6;
    Button start_time7; Button start_time8; Button start_time9;
    Button start_time0; Button start_time_enter; Button start_time_back;

    boolean[] start_flag = {true,true,true,true,true,true,true,true,true,true,true,true};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        layoutParams.dimAmount = 0.7f;
        getWindow().setAttributes(layoutParams);
        setContentView(R.layout.activity_start_time);

        start_time1 = (Button) findViewById(R.id.start_time1);
        start_time2 = (Button) findViewById(R.id.start_time2);
        start_time3 = (Button) findViewById(R.id.start_time3);
        start_time4 = (Button) findViewById(R.id.start_time4);
        start_time5 = (Button) findViewById(R.id.start_time5);
        start_time6 = (Button) findViewById(R.id.start_time6);
        start_time7 = (Button) findViewById(R.id.start_time7);
        start_time8 = (Button) findViewById(R.id.start_time8);
        start_time9 = (Button) findViewById(R.id.start_time9);
        start_time0 = (Button) findViewById(R.id.start_time0);
        start_time_enter = (Button) findViewById(R.id.start_time_enter);
        start_time_back = (Button) findViewById(R.id.start_time_back);

        View.OnClickListener listener = new View.OnClickListener() {
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.start_time1:
                        //
                        if (start_flag[0] == true) {
                            start_time1.setBackgroundResource(R.drawable.keypad_1);
                            start_flag[0] = false;
                        } else {
                            start_time1.setBackgroundResource(R.drawable.keypad_1);
                            start_flag[0] = true;
                        }
                        break;
                    case R.id.start_time2:
                        //
                        if (start_flag[1] == true) {
                            start_time2.setBackgroundResource(R.drawable.keypad_2);
                            start_flag[1] = false;
                        } else {
                            start_time2.setBackgroundResource(R.drawable.keypad_2);
                            start_flag[1] = true;
                        }
                        break;
                    case R.id.start_time3:
                        //
                        if (start_flag[2] == true) {
                            start_time3.setBackgroundResource(R.drawable.keypad_3);
                            start_flag[2] = false;
                        } else {
                            start_time3.setBackgroundResource(R.drawable.keypad_3);
                            start_flag[2] = true;
                        }
                        break;
                    case R.id.start_time4:
                        //
                        if (start_flag[3] == true) {
                            start_time4.setBackgroundResource(R.drawable.keypad_4);
                            start_flag[3] = false;
                        } else {
                            start_time4.setBackgroundResource(R.drawable.keypad_4);
                            start_flag[3] = true;
                        }
                        break;
                    case R.id.start_time5:
                        //
                        if (start_flag[4] == true) {
                            start_time5.setBackgroundResource(R.drawable.keypad_5);
                            start_flag[4] = false;
                        } else {
                            start_time5.setBackgroundResource(R.drawable.keypad_5);
                            start_flag[4] = true;
                        }
                        break;
                    case R.id.start_time6:
                        //
                        if (start_flag[5] == true) {
                            start_time6.setBackgroundResource(R.drawable.keypad_6);
                            start_flag[5] = false;
                        } else {
                            start_time6.setBackgroundResource(R.drawable.keypad_6);
                            start_flag[5] = true;
                        }
                        break;
                    case R.id.start_time7:
                        //
                        if (start_flag[6] == true) {
                            start_time7.setBackgroundResource(R.drawable.keypad_7);
                            start_flag[6] = false;
                        } else {
                            start_time7.setBackgroundResource(R.drawable.keypad_7);
                            start_flag[6] = true;
                        }
                        break;
                    case R.id.start_time8:
                        //
                        if (start_flag[7] == true) {
                            start_time8.setBackgroundResource(R.drawable.keypad_8);
                            start_flag[7] = false;
                        } else {
                            start_time8.setBackgroundResource(R.drawable.keypad_8);
                            start_flag[7] = true;
                        }
                        break;
                    case R.id.start_time9:
                        //
                        if (start_flag[8] == true) {
                            start_time9.setBackgroundResource(R.drawable.keypad_9);
                            start_flag[8] = false;
                        } else {
                            start_time9.setBackgroundResource(R.drawable.keypad_9);
                            start_flag[8] = true;
                        }
                        break;
                    case R.id.start_time0:
                        //
                        if (start_flag[9] == true) {
                            start_time0.setBackgroundResource(R.drawable.keypad_0);
                            start_flag[9] = false;
                        } else {
                            start_time0.setBackgroundResource(R.drawable.keypad_0);
                            start_flag[9] = true;
                        }
                        break;


                    case R.id.start_time_enter:
                        //
                        if (start_flag[10] == true) {
                            start_time_enter.setBackgroundResource(R.drawable.keypad_enter);
                            start_flag[10] = false;
                        } else {
                            start_time_enter.setBackgroundResource(R.drawable.keypad_enter);
                            start_flag[10] = true;
                        }
                        break;
                    case R.id.start_time_back:
                        //
                        if (start_flag[11] == true) {
                            start_time_back.setBackgroundResource(R.drawable.keypad_back);
                            start_flag[11] = false;
                            finish();
                        } else {
                            start_time_back.setBackgroundResource(R.drawable.keypad_back);
                            start_flag[11] = true;
                        }
                        break;
                }
            }
        };

        start_time1.setOnClickListener(listener);
        start_time2.setOnClickListener(listener);
        start_time3.setOnClickListener(listener);
        start_time4.setOnClickListener(listener);
        start_time5.setOnClickListener(listener);
        start_time6.setOnClickListener(listener);
        start_time7.setOnClickListener(listener);
        start_time8.setOnClickListener(listener);
        start_time9.setOnClickListener(listener);
        start_time0.setOnClickListener(listener);
        start_time_enter.setOnClickListener(listener);
        start_time_back.setOnClickListener(listener);

    }
}
