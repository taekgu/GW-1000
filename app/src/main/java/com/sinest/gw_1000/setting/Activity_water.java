package com.sinest.gw_1000.setting;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.LinearLayout;

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
    LinearLayout activity_water;

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
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        activity_water = (LinearLayout) findViewById(R.id.activity_water);
        water_save = (Button) findViewById(R.id.water_save);
        water_off = (Button) findViewById(R.id.water_off);
        water_back = (Button) findViewById(R.id.water_back);

        Typeface tf = Typeface.createFromAsset(getAssets(), "fonts/digital.ttf");
        water_s_c = (Chronometer) findViewById(R.id.water_s_c);
        water_s_c.setTypeface(tf);
        water_f_c = (Chronometer) findViewById(R.id.water_f_c);
        water_f_c.setTypeface(tf);

        intent_start = new Intent(this, Activity_starttime.class);
        intent_finish = new Intent(this, Activity_finishtime.class);

        View.OnClickListener listener = new View.OnClickListener() {
            public void onClick(View v) {
                Application_manager.set_m_start_sleep(0);
                switch (v.getId()) {
                    case R.id.water_off:
                        if (water_flag[1] == true) {
                            water_off.setBackgroundResource(Application_manager.save_setting_on[Application_manager.useChineseImage]);
                            water_flag[1] = false;
                        } else {
                            water_off.setBackgroundResource(Application_manager.save_setting_off[Application_manager.useChineseImage]);
                            water_flag[1] = true;
                        }
                        break;
                    case R.id.water_s_c:
                        intent_start.putExtra("start", water_s_c.getText());
                        startActivity(intent_start);
                        break;
                    case R.id.water_f_c:
                        intent_start.putExtra("finish", water_f_c.getText());
                        startActivity(intent_finish);
                        break;
                }
            }
        };
        water_off.setOnClickListener(listener);
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
            Application_manager.set_m_start_sleep(0);
            int action = motionEvent.getAction();
            int id = view.getId();
            if (action == MotionEvent.ACTION_DOWN) {
                switch (id) {
                    case R.id.water_save:
                        water_save.setBackgroundResource(Application_manager.save_on[Application_manager.useChineseImage]);
                        break;
                    case R.id.water_back:
                        water_back.setBackgroundResource(Application_manager.button_elipse_back_on[Application_manager.useChineseImage]);
                        break;
                }
            } else if (action == MotionEvent.ACTION_UP) {
                switch (id) {
                    case R.id.water_save:
                        water_save.setBackgroundResource(Application_manager.save_off[Application_manager.useChineseImage]);
                        //change
                        Application_manager.set_m_water_stime((String)water_s_c.getText());
                        Application_manager.set_m_water_ftime((String)water_f_c.getText());
                        Application_manager.set_m_water_f(false);
                        Application_manager.set_m_water_ff(water_flag[1]);
                        Application_manager.time_buf_f = 1;
                        finish();
                        break;
                    case R.id.water_back:
                        water_back.setBackgroundResource(Application_manager.button_elipse_back_off[Application_manager.useChineseImage]);
                        Application_manager.set_m_water_f(true);
                        Application_manager.s_time_buf = Application_manager.m_water_heater_time_stime;
                        Application_manager.f_time_buf = Application_manager.m_water_heater_time_ftime;
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

        // 언어에 따른 이미지 변경
        water_off.setBackgroundResource(Application_manager.save_setting_off[Application_manager.useChineseImage]);
        water_back.setBackgroundResource(Application_manager.button_elipse_back_off[Application_manager.useChineseImage]);
        water_save.setBackgroundResource(Application_manager.save_off[Application_manager.useChineseImage]);
        activity_water.setBackgroundResource(Application_manager.water_heater_timer_popup[Application_manager.useChineseImage]);

        //change
        do_init_time();

        // 슬립 모드 동작 재시작
        Application_manager.setSleep_f(0,true);
    }

    /*
    * setting에서 들어오면 DB에 저장된 값을 출력
    * 시작,끝 시간선택 창에서 들어오면 고른 시간을 출력
    */
    private void do_init_time(){
        if(Application_manager.time_buf_f == 1){
            start_time = Application_manager.m_water_heater_time_stime;
            water_s_c.setText(start_time);
            finish_time = Application_manager.m_water_heater_time_ftime;
            water_f_c.setText(finish_time);
        }else if(Application_manager.time_buf_f == 0){
            water_s_c.setText(Application_manager.s_time_buf);
            water_f_c.setText(Application_manager.f_time_buf);
        }
        water_flag[1] = Application_manager.m_water_heater_f;
        Application_manager.time_buf_f = 0;
    }

    @Override
    protected void onStop() {
        super.onStop();
        Application_manager.time_buf_f = 1;
    }
}
