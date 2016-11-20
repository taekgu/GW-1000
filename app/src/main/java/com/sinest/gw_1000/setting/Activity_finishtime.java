package com.sinest.gw_1000.setting;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import com.sinest.gw_1000.R;

public class Activity_finishtime extends Activity {

    Button finish_time1; Button finish_time2; Button finish_time3;
    Button finish_time4; Button finish_time5; Button finish_time6;
    Button finish_time7; Button finish_time8; Button finish_time9;
    Button finish_time0; Button finish_time_enter; Button finish_time_back;

    boolean[] finish_flag = {true,true,true,true,true,true,true,true,true,true,true,true};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        layoutParams.dimAmount = 0.7f;
        getWindow().setAttributes(layoutParams);
        setContentView(R.layout.activity_finish_time);

        finish_time1 = (Button) findViewById(R.id.finish_time1);
        finish_time2 = (Button) findViewById(R.id.finish_time2);
        finish_time3 = (Button) findViewById(R.id.finish_time3);
        finish_time4 = (Button) findViewById(R.id.finish_time4);
        finish_time5 = (Button) findViewById(R.id.finish_time5);
        finish_time6 = (Button) findViewById(R.id.finish_time6);
        finish_time7 = (Button) findViewById(R.id.finish_time7);
        finish_time8 = (Button) findViewById(R.id.finish_time8);
        finish_time9 = (Button) findViewById(R.id.finish_time9);
        finish_time0 = (Button) findViewById(R.id.finish_time0);
        finish_time_enter = (Button) findViewById(R.id.finish_time_enter);
        finish_time_back = (Button) findViewById(R.id.finish_time_back);

        View.OnClickListener listener = new View.OnClickListener() {
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.start_time1:
                        //
                        if (finish_flag[0] == true) {
                            finish_time1.setBackgroundResource(R.drawable.keypad_1);
                            finish_flag[0] = false;
                        } else {
                            finish_time1.setBackgroundResource(R.drawable.keypad_1);
                            finish_flag[0] = true;
                        }
                        break;
                    case R.id.finish_time2:
                        //
                        if (finish_flag[1] == true) {
                            finish_time2.setBackgroundResource(R.drawable.keypad_2);
                            finish_flag[1] = false;
                        } else {
                            finish_time2.setBackgroundResource(R.drawable.keypad_2);
                            finish_flag[1] = true;
                        }
                        break;
                    case R.id.finish_time3:
                        //
                        if (finish_flag[2] == true) {
                            finish_time3.setBackgroundResource(R.drawable.keypad_3);
                            finish_flag[2] = false;
                        } else {
                            finish_time3.setBackgroundResource(R.drawable.keypad_3);
                            finish_flag[2] = true;
                        }
                        break;
                    case R.id.finish_time4:
                        //
                        if (finish_flag[3] == true) {
                            finish_time4.setBackgroundResource(R.drawable.keypad_4);
                            finish_flag[3] = false;
                        } else {
                            finish_time4.setBackgroundResource(R.drawable.keypad_4);
                            finish_flag[3] = true;
                        }
                        break;
                    case R.id.finish_time5:
                        //
                        if (finish_flag[4] == true) {
                            finish_time5.setBackgroundResource(R.drawable.keypad_5);
                            finish_flag[4] = false;
                        } else {
                            finish_time5.setBackgroundResource(R.drawable.keypad_5);
                            finish_flag[4] = true;
                        }
                        break;
                    case R.id.finish_time6:
                        //
                        if (finish_flag[5] == true) {
                            finish_time6.setBackgroundResource(R.drawable.keypad_6);
                            finish_flag[5] = false;
                        } else {
                            finish_time6.setBackgroundResource(R.drawable.keypad_6);
                            finish_flag[5] = true;
                        }
                        break;
                    case R.id.finish_time7:
                        //
                        if (finish_flag[6] == true) {
                            finish_time7.setBackgroundResource(R.drawable.keypad_7);
                            finish_flag[6] = false;
                        } else {
                            finish_time7.setBackgroundResource(R.drawable.keypad_7);
                            finish_flag[6] = true;
                        }
                        break;
                    case R.id.finish_time8:
                        //
                        if (finish_flag[7] == true) {
                            finish_time8.setBackgroundResource(R.drawable.keypad_8);
                            finish_flag[7] = false;
                        } else {
                            finish_time8.setBackgroundResource(R.drawable.keypad_8);
                            finish_flag[7] = true;
                        }
                        break;
                    case R.id.finish_time9:
                        //
                        if (finish_flag[8] == true) {
                            finish_time9.setBackgroundResource(R.drawable.keypad_9);
                            finish_flag[8] = false;
                        } else {
                            finish_time9.setBackgroundResource(R.drawable.keypad_9);
                            finish_flag[8] = true;
                        }
                        break;
                    case R.id.finish_time0:
                        //
                        if (finish_flag[9] == true) {
                            finish_time0.setBackgroundResource(R.drawable.keypad_0);
                            finish_flag[9] = false;
                        } else {
                            finish_time0.setBackgroundResource(R.drawable.keypad_0);
                            finish_flag[9] = true;
                        }
                        break;


                    case R.id.finish_time_enter:
                        //
                        if (finish_flag[10] == true) {
                            finish_time_enter.setBackgroundResource(R.drawable.keypad_enter);
                            finish_flag[10] = false;
                        } else {
                            finish_time_enter.setBackgroundResource(R.drawable.keypad_enter);
                            finish_flag[10] = true;
                        }
                        break;
                    case R.id.finish_time_back:
                        //
                        if (finish_flag[11] == true) {
                            finish_time_back.setBackgroundResource(R.drawable.keypad_back);
                            finish_flag[11] = false;
                            finish();
                        } else {
                            finish_time_back.setBackgroundResource(R.drawable.keypad_back);
                            finish_flag[11] = true;
                        }
                        break;
                }
            }
        };

        finish_time1.setOnClickListener(listener);
        finish_time2.setOnClickListener(listener);
        finish_time3.setOnClickListener(listener);
        finish_time4.setOnClickListener(listener);
        finish_time5.setOnClickListener(listener);
        finish_time6.setOnClickListener(listener);
        finish_time7.setOnClickListener(listener);
        finish_time8.setOnClickListener(listener);
        finish_time9.setOnClickListener(listener);
        finish_time0.setOnClickListener(listener);
        finish_time_enter.setOnClickListener(listener);
        finish_time_back.setOnClickListener(listener);




    }
}
