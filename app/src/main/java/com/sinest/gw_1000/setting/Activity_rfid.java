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
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sinest.gw_1000.R;
import com.sinest.gw_1000.management.Application_manager;

public class Activity_rfid extends Activity {

    Button rfid_w_up; Button rfid_w_down;
    Button rfid_t_up; Button rfid_t_down;
    Button rfid_save; Button rfid_check; Button rfid_off;
    Button rfid_back;

    LinearLayout activity_rfid;

    boolean[] rfid_flag = {true,true,true};

    TextView rfid_t_c; TextView rfid_w_c;

    String s_buf;
    int int_buf;
    int treatment_num;

    String[] w_mode = {"A1","A2","A3","A4","A5","A6","A7","A8","A9","A10","A11","A12","A13","A14","A15",
            "M1","M2","M3","M4","M5","NM"};
    int w_mode_idx = 0;
    Intent check;
    boolean save_f = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        layoutParams.dimAmount = 0.7f;
        getWindow().setAttributes(layoutParams);
        setContentView(R.layout.activity_rfid);

        Application_manager.setFullScreen(this);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        activity_rfid = (LinearLayout) findViewById(R.id.activity_rfid);
        rfid_w_up = (Button) findViewById(R.id.rfid_w_up);
        rfid_w_down = (Button) findViewById(R.id.rfid_w_down);
        rfid_t_up = (Button) findViewById(R.id.rfid_t_up);
        rfid_t_down = (Button) findViewById(R.id.rfid_t_down);
        rfid_save = (Button) findViewById(R.id.rfid_save);
        rfid_check = (Button) findViewById(R.id.rfid_check);
        rfid_off = (Button) findViewById(R.id.rfid_off);
        rfid_back = (Button) findViewById(R.id.rfid_back);
        rfid_t_c = (TextView) findViewById(R.id.rfid_t_c);
        rfid_w_c = (TextView) findViewById(R.id.rfid_w_c);

        treatment_num = 1;

        Typeface tf = Typeface.createFromAsset(getAssets(), "fonts/digital.ttf");

        rfid_t_c.setText(w_mode[w_mode_idx]);
        rfid_w_c.setText(Integer.toString(treatment_num));
        rfid_t_c.setTypeface(tf);
        rfid_w_c.setTypeface(tf);

        check = getIntent();

        /*
        * 토글식 버튼
        * on 될시 데이터 전송
        * flag가 true이면 on false이면 off
        */
        View.OnClickListener listener = new View.OnClickListener() {
            public void onClick(View v) {
                Application_manager.set_m_start_sleep(0);
                switch (v.getId()) {
                    case R.id.rfid_off:
                        if (rfid_flag[2] == true) {
                            rfid_off.setBackgroundResource(Application_manager.save_setting_on[Application_manager.useChineseImage]);
                            rfid_flag[2] = false;
                            Application_manager.set_rfid_on_f(1);
                        } else {
                            rfid_off.setBackgroundResource(Application_manager.save_setting_off[Application_manager.useChineseImage]);
                            rfid_flag[2] = true;
                            Application_manager.set_rfid_on_f(0);
                        }
                        break;
                }
            }
        };

        rfid_off.setOnClickListener(listener);
        rfid_w_up.setOnTouchListener(mTouchEvent);
        rfid_w_down.setOnTouchListener(mTouchEvent);
        rfid_t_up.setOnTouchListener(mTouchEvent);
        rfid_t_down.setOnTouchListener(mTouchEvent);
        rfid_save.setOnTouchListener(mTouchEvent);
        rfid_check.setOnTouchListener(mTouchEvent);
        rfid_back.setOnTouchListener(mTouchEvent);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event){
        return false;
    }

    /*
    * 버튼을 누를시 -> ACTION_DOWN
    * 버튼을 눌렀다 땔시 -> ACTION_UP
    * 버튼의 이미지 변화 및 땔시 데이터 변환
    */
    private View.OnTouchListener mTouchEvent = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            Application_manager.set_m_start_sleep(0);
            int action = motionEvent.getAction();
            int id = view.getId();
            if (action == MotionEvent.ACTION_DOWN) {
                switch (id) {
                    case R.id.rfid_w_up:
                        rfid_w_up.setBackgroundResource(R.drawable.button_up_on);
                        break;
                    case R.id.rfid_w_down:
                        rfid_w_down.setBackgroundResource(R.drawable.button_down_on);
                        break;
                    case R.id.rfid_t_up:
                        rfid_t_up.setBackgroundResource(R.drawable.button_up_on);
                        break;
                    case R.id.rfid_t_down:
                        rfid_t_down.setBackgroundResource(R.drawable.button_down_on);
                        break;
                    case R.id.rfid_save:
                        rfid_save.setBackgroundResource(Application_manager.save_on[Application_manager.useChineseImage]);
                        break;
                    case R.id.rfid_check:
                        rfid_check.setBackgroundResource(Application_manager.check_on[Application_manager.useChineseImage]);
                        break;
                    case R.id.rfid_back:
                        rfid_back.setBackgroundResource(Application_manager.button_elipse_back_on[Application_manager.useChineseImage]);
                        break;
                }
            } else if (action == MotionEvent.ACTION_UP) {
                switch (id) {
                    case R.id.rfid_w_up:
                        rfid_w_up.setBackgroundResource(R.drawable.button_up_off);
                        if(w_mode_idx == 20)
                        {
                            w_mode_idx = 0;
                        }else{
                            w_mode_idx++;
                        }
                        rfid_t_c.setText(w_mode[w_mode_idx]);
                        break;
                    case R.id.rfid_w_down:
                        rfid_w_down.setBackgroundResource(R.drawable.button_down_off);
                        if(w_mode_idx == 0)
                        {
                            w_mode_idx = 20;
                        }else{
                            w_mode_idx--;
                        }
                        rfid_t_c.setText(w_mode[w_mode_idx]);
                        break;
                    case R.id.rfid_t_up:
                        rfid_t_up.setBackgroundResource(R.drawable.button_up_off);
                        s_buf = (String)rfid_w_c.getText();
                        if(Integer.parseInt(s_buf) < 10 && 1 <= Integer.parseInt(s_buf))
                        {
                            int_buf = Integer.parseInt(s_buf) + 1;
                            treatment_num++;
                            s_buf = Integer.toString(int_buf);
                            rfid_w_c.setText(s_buf);
                        }
                        break;
                    case R.id.rfid_t_down:
                        rfid_t_down.setBackgroundResource(R.drawable.button_down_off);
                        s_buf = (String)rfid_w_c.getText();
                        if(Integer.parseInt(s_buf) <= 10 && 1 < Integer.parseInt(s_buf))
                        {
                            int_buf = Integer.parseInt(s_buf) - 1;
                            treatment_num--;
                            s_buf = Integer.toString(int_buf);
                            rfid_w_c.setText(s_buf);
                        }
                        break;
                    case R.id.rfid_save:
                        rfid_save.setBackgroundResource(Application_manager.save_off[Application_manager.useChineseImage]);
                        //save -> working_mode_num, treatment_num
                        if (rfid_flag[2] == false) {
                            save_f = true;
                        }
                        break;
                    case R.id.rfid_check:
                        rfid_check.setBackgroundResource(Application_manager.check_off[Application_manager.useChineseImage]);
                        break;
                    case R.id.rfid_back:
                        rfid_back.setBackgroundResource(Application_manager.button_elipse_back_off[Application_manager.useChineseImage]);
                        Application_manager.rfid_pass_f2 = false;
                        if(save_f == true)
                        {
                            Application_manager.rfid_pass_f = false;
                            finish();
                        }
                        else if(save_f == false)
                        {
                            Application_manager.rfid_pass_f = true;
                            finish();
                        }
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

        // 언어에 따른 이미지 초기설정
        rfid_save.setBackgroundResource(Application_manager.save_off[Application_manager.useChineseImage]);
        rfid_check.setBackgroundResource(Application_manager.check_off[Application_manager.useChineseImage]);
        rfid_off.setBackgroundResource(Application_manager.save_setting_off[Application_manager.useChineseImage]);
        activity_rfid.setBackgroundResource(Application_manager.rfid_working_popup[Application_manager.useChineseImage]);
        rfid_back.setBackgroundResource(Application_manager.button_elipse_back_off[Application_manager.useChineseImage]);

        // 슬립 모드 동작 재시작
        Application_manager.setSleep_f(0,true);
    }
}
