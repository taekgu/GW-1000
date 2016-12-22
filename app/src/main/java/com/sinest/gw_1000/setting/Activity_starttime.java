package com.sinest.gw_1000.setting;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Chronometer;

import com.sinest.gw_1000.R;
import com.sinest.gw_1000.management.Application_manager;

public class Activity_starttime extends Activity {

    Button start_time1; Button start_time2; Button start_time3;
    Button start_time4; Button start_time5; Button start_time6;
    Button start_time7; Button start_time8; Button start_time9;
    Button start_time0; Button start_time_enter; Button start_time_back;

    boolean[] start_flag = {true,true,true,true,true,true,true,true,true,true,true,true};

    Chronometer start_time;

    String s_buf;
    int int_buf;
    int int_c = 0;

    String buf_l;
    String buf_r;
    int int_l;
    int int_r;
    int check;

    Intent start_result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        layoutParams.dimAmount = 0.7f;
        getWindow().setAttributes(layoutParams);
        setContentView(R.layout.activity_start_time);

        Application_manager.setFullScreen(this);

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

        start_result = this.getIntent();

        start_time = (Chronometer) findViewById(R.id.start_time);

        Typeface tf = Typeface.createFromAsset(getAssets(), "fonts/digital.ttf");
        start_time.setText(start_result.getStringExtra("start"));
        start_time.setTypeface(tf);

        View.OnClickListener listener = new View.OnClickListener() {
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.start_time1:
                        //
                        time_sum(1);
                        break;
                    case R.id.start_time2:
                        //
                        time_sum(2);
                        break;
                    case R.id.start_time3:
                        //
                        time_sum(3);
                        break;
                    case R.id.start_time4:
                        //
                        time_sum(4);
                        break;
                    case R.id.start_time5:
                        //
                        time_sum(5);
                        break;
                    case R.id.start_time6:
                        //
                        time_sum(6);
                        break;
                    case R.id.start_time7:
                        //
                        time_sum(7);
                        break;
                    case R.id.start_time8:
                        //
                        time_sum(8);
                        break;
                    case R.id.start_time9:
                        //
                        time_sum(9);
                        break;
                    case R.id.start_time0:
                        //
                        time_sum(0);
                        break;

                    case R.id.start_time_enter:
                        //
                        //change
                        Application_manager.set_m_water_stime(s_buf);
                        finish();
                        break;
                    case R.id.start_time_back:
                        //
                        finish();
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

    @Override
    public boolean onTouchEvent(MotionEvent event){
        return false;
    }

    void time_sum(int k)
    {
        if(int_c >= 4)
        {
            s_buf = (String)start_time.getText();
            check = s_buf.indexOf(":");
            buf_l = s_buf.substring(0,check);
            buf_r = s_buf.substring(check+1);
            int_buf = Integer.parseInt(buf_l)*100 + Integer.parseInt(buf_r);
            // change
            int_buf = (int_buf%1000)*10 + k;

            int_l = int_buf/100;
            int_r = int_buf%100;

            if(int_l == 0)
            {
                buf_l = "00";
            }else if(int_l < 10)
            {
                buf_l = "0"+Integer.toString(int_l);
            }else{
                buf_l = Integer.toString(int_l);
            }

            if(int_r == 0)
            {
                buf_r = "00";
            }else if(int_r < 10){
                buf_r = "0"+Integer.toString(int_r);
            }else{
                buf_r = Integer.toString(int_r);
            }

            s_buf = buf_l+":"+buf_r;

            start_time.setText(s_buf);

        }else if(int_c == 0) {
            s_buf = "00:0"+(String)Integer.toString(k);
            start_time.setText(s_buf);
            int_c++;
        }
        else {
            s_buf = (String)start_time.getText();
            check = s_buf.indexOf(":");
            buf_l = s_buf.substring(0,check);
            buf_r = s_buf.substring(check+1);
            int_buf = Integer.parseInt(buf_l)*100 + Integer.parseInt(buf_r);
            //change
            int_buf = int_buf*10 + k;

            int_l = int_buf/100;
            int_r = int_buf%100;

            if(int_l == 0)
            {
                buf_l = "00";
            }else if(int_l < 10)
            {
                buf_l = "0"+Integer.toString(int_l);
            }else{
                buf_l = Integer.toString(int_l);
            }

            if(int_r == 0)
            {
                buf_r = "00";
            }else if(int_r < 10){
                buf_r = "0"+Integer.toString(int_r);
            }else{
                buf_r = Integer.toString(int_r);
            }

            s_buf = buf_l+":"+buf_r;

            start_time.setText(s_buf);
            int_c++;
        }
    }

}