package com.sinest.gw_1000.setting;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.ImageView;
import android.widget.Toast;

import com.sinest.gw_1000.R;
import com.sinest.gw_1000.management.Application_manager;

public class Activity_time extends Activity {

    Button time1; Button time2; Button time3;
    Button time4; Button time5; Button time6;
    Button time7; Button time8; Button time9;
    Button time0; Button time_enter; Button time_back;

    boolean[] start_flag = {true,true,true,true,true,true,true,true,true,true,true,true};

    Chronometer time;

    String s_buf = "00:00";
    int int_buf;
    int int_c = 0;

    String buf_l;
    String buf_r;
    int int_l;
    int int_r;
    int check;

    ImageView time_keypad_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        layoutParams.dimAmount = 0.7f;
        getWindow().setAttributes(layoutParams);
        setContentView(R.layout.activity_time);

        Application_manager.setFullScreen(this);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        time_keypad_id = (ImageView) findViewById(R.id.time_keypad_id);

        time1 = (Button) findViewById(R.id.time1);
        time2 = (Button) findViewById(R.id.time2);
        time3 = (Button) findViewById(R.id.time3);
        time4 = (Button) findViewById(R.id.time4);
        time5 = (Button) findViewById(R.id.time5);
        time6 = (Button) findViewById(R.id.time6);
        time7 = (Button) findViewById(R.id.time7);
        time8 = (Button) findViewById(R.id.time8);
        time9 = (Button) findViewById(R.id.time9);
        time0 = (Button) findViewById(R.id.time0);
        time_enter = (Button) findViewById(R.id.time_enter);
        time_back = (Button) findViewById(R.id.time_back);
        time = (Chronometer) findViewById(R.id.time);

        Typeface tf = Typeface.createFromAsset(getAssets(), "fonts/digital.ttf");
        time.setText("00:00");
        time.setTypeface(tf);

        /*
        * 토글식 버튼
        * 한번 누를 때마다 그 버튼의 값으로
        * 시간을 만들어주는 함수 실행
        * enter버튼은 시간이 시간범위에 있는지 확인후 끝낸다
        */
        View.OnClickListener listener = new View.OnClickListener() {
            public void onClick(View v) {
                Application_manager.set_m_start_sleep(0);
                switch (v.getId()) {
                    case R.id.time1:
                        time_sum(1);
                        break;
                    case R.id.time2:
                        time_sum(2);
                        break;
                    case R.id.time3:
                        time_sum(3);
                        break;
                    case R.id.time4:
                        time_sum(4);
                        break;
                    case R.id.time5:
                        time_sum(5);
                        break;
                    case R.id.time6:
                        time_sum(6);
                        break;
                    case R.id.time7:
                        time_sum(7);
                        break;
                    case R.id.time8:
                        time_sum(8);
                        break;
                    case R.id.time9:
                        time_sum(9);
                        break;
                    case R.id.time0:
                        time_sum(0);
                        break;
                    case R.id.time_enter:
                        check = s_buf.indexOf(":");
                        buf_l = s_buf.substring(0,check);
                        buf_r = s_buf.substring(check+1);
                        int_buf = Integer.parseInt(buf_l)*100 + Integer.parseInt(buf_r);
                        int_l = int_buf/100;
                        int_r = int_buf%100;
                        if(int_l >= 24 || int_r >=60){
                            Application_manager.getToastManager().popToast(11);
                        }else{
                            Application_manager.setTime(s_buf);
                        }
                        finish();
                        break;
                    case R.id.time_back:
                        finish();
                        break;
                }
            }
        };
        time1.setOnClickListener(listener);
        time2.setOnClickListener(listener);
        time3.setOnClickListener(listener);
        time4.setOnClickListener(listener);
        time5.setOnClickListener(listener);
        time6.setOnClickListener(listener);
        time7.setOnClickListener(listener);
        time8.setOnClickListener(listener);
        time9.setOnClickListener(listener);
        time0.setOnClickListener(listener);
        time_enter.setOnClickListener(listener);
        time_back.setOnClickListener(listener);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event){
        return false;
    }

    protected void onResume() {
        super.onResume();
        // 언어에 따른 이미지 초기설정
        time_keypad_id.setBackgroundResource(Application_manager.time_keypad[Application_manager.img_flag]);
        time_enter.setBackgroundResource(Application_manager.keypad_enter[Application_manager.img_flag]);
        time_back.setBackgroundResource(Application_manager.keypad_back[Application_manager.img_flag]);

        // 슬립 모드 동작 재시작
        Application_manager.setSleep_f(0,true);
    }

    /*
    * 누른 버튼에 따라
    * 시간을 만들어주는 함수
    * 인자를 누른 버튼의 값으로 받는다
     */
    void time_sum(int k)
    {
        if(int_c >= 4)
        {
            s_buf = (String)time.getText();
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
            time.setText(s_buf);

        }else if(int_c == 0) {
            s_buf = "00:0"+(String)Integer.toString(k);
            time.setText(s_buf);
            int_c++;
        }else {
            s_buf = (String)time.getText();
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
            time.setText(s_buf);
            int_c++;
        }
    }

}
