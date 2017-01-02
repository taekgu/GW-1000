package com.sinest.gw_1000.setting;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Chronometer;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;
import com.sinest.gw_1000.R;
import com.sinest.gw_1000.management.Application_manager;

public class Activity_water extends Activity {

    Button water_save;
    Button water_off;
    Button water_back;

    boolean[] water_flag = {true, true};
    boolean water_b_f = true;

    Chronometer water_s_c;
    Chronometer water_f_c;

    Intent intent_start;
    Intent intent_finish;

    String start_time = "00:00";
    String finish_time = "00:00";

    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        layoutParams.dimAmount = 0.7f;
        getWindow().setAttributes(layoutParams);
        setContentView(R.layout.activity_water);

        Application_manager.setFullScreen(this);

        water_save = (Button) findViewById(R.id.water_save);
        water_off = (Button) findViewById(R.id.water_off);
        water_back = (Button) findViewById(R.id.water_back);

        Typeface tf = Typeface.createFromAsset(getAssets(), "fonts/digital.ttf");
        water_s_c = (Chronometer) findViewById(R.id.water_s_c);
        water_s_c.setTypeface(tf);
        water_f_c = (Chronometer) findViewById(R.id.water_f_c);
        water_f_c.setTypeface(tf);

        intent_start = new Intent(this, Activity_starttime.class);
        //intent_start.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);

        intent_finish = new Intent(this, Activity_finishtime.class);
        //intent_finish.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);

        //change
        do_init_time();
        //water_s_c.setText(start_time);
        //water_f_c.setText(finish_time);


        View.OnClickListener listener = new View.OnClickListener() {
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.water_off:
                        //
                        if (water_flag[1] == true) {
                            water_off.setBackgroundResource(R.drawable.on_109);
                            water_flag[1] = false;
                            Application_manager.set_m_water_ff(water_flag[1]);
                        } else {
                            water_off.setBackgroundResource(R.drawable.off_109);
                            water_flag[1] = true;
                            Application_manager.set_m_water_ff(water_flag[1]);
                        }
                        break;
                    case R.id.water_s_c:
                        startActivity(intent_start);
                        break;
                    case R.id.water_f_c:
                        startActivity(intent_finish);
                        break;
                }
            }
        };

        //water_save.setOnClickListener(listener);
        water_off.setOnClickListener(listener);
        //water_back.setOnClickListener(listener);
        water_s_c.setOnClickListener(listener);
        water_f_c.setOnClickListener(listener);

        water_save.setOnTouchListener(mTouchEvent);
        water_back.setOnTouchListener(mTouchEvent);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event){
        return false;
    }

    private View.OnTouchListener mTouchEvent = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            int action = motionEvent.getAction();
            int id = view.getId();
            if (action == MotionEvent.ACTION_DOWN) {
                switch (id) {
                    case R.id.water_save:
                        water_save.setBackgroundResource(R.drawable.save_setting_on);
                        break;
                    case R.id.water_back:
                        water_back.setBackgroundResource(R.drawable.button_elipse_back_on);
                        break;
                }
            } else if (action == MotionEvent.ACTION_UP) {
                switch (id) {
                    case R.id.water_save:
                        water_save.setBackgroundResource(R.drawable.save_setting_off);
                        //change
                        Application_manager.set_m_water_f(false);
                        finish();
                        break;
                    case R.id.water_back:
                        water_back.setBackgroundResource(R.drawable.button_elipse_back_off);
                        Application_manager.set_m_water_f(true);
                        finish();
                        break;
                }
            }
            return true;
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        Application_manager.setFullScreen(this);
        //change
        do_init_time();
    }

    private void do_init_time(){
        start_time = Application_manager.m_water_heater_time_stime;
        water_s_c.setText(start_time);
        finish_time = Application_manager.m_water_heater_time_ftime;
        water_f_c.setText(finish_time);

        water_flag[1] = Application_manager.m_water_heater_f;
    }
}
