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

                        break;
                    case R.id.start_time2:
                        //

                        break;
                    case R.id.start_time3:
                        //

                        break;
                    case R.id.start_time4:
                        //

                        break;
                    case R.id.start_time5:
                        //

                        break;
                    case R.id.start_time6:
                        //

                        break;
                    case R.id.start_time7:
                        //

                        break;
                    case R.id.start_time8:
                        //

                        break;
                    case R.id.start_time9:
                        //

                        break;
                    case R.id.start_time0:
                        //

                        break;


                    case R.id.start_time_enter:
                        //

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
}
