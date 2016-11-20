package com.sinest.gw_1000.setting;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import com.sinest.gw_1000.R;

public class Activity_emotion extends Activity {

    Button emotion_ledm_up; Button emotion_ledm_down;
    Button emotion_led_up; Button emotion_led_down;
    Button emotion_soundm_up; Button emotion_soundm_down;
    Button emotion_sound_up; Button emotion_sound_down;
    Button emotion_back;

    boolean[] emotion_ledm_flag = {true,true};
    boolean[] emotion_led_flag = {true,true};
    boolean[] emotion_soundm_flag = {true,true};
    boolean[] emotion_sound_flag = {true,true};

    boolean emotion_b_f = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        layoutParams.dimAmount = 0.7f;
        getWindow().setAttributes(layoutParams);
        setContentView(R.layout.activity_emotion);

        emotion_ledm_up = (Button)findViewById(R.id.emotion_ledm_up);
        emotion_ledm_down = (Button)findViewById(R.id.emotion_ledm_down);
        emotion_led_up = (Button)findViewById(R.id.emotion_led_up);
        emotion_led_down = (Button)findViewById(R.id.emotion_led_down);
        emotion_soundm_up = (Button)findViewById(R.id.emotion_soundm_up);
        emotion_soundm_down = (Button)findViewById(R.id.emotion_soundm_down);
        emotion_sound_up = (Button)findViewById(R.id.emotion_sound_up);
        emotion_sound_down = (Button)findViewById(R.id.emotion_sound_down);
        emotion_back = (Button)findViewById(R.id.emotion_back);

        View.OnClickListener listener = new View.OnClickListener() {
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.emotion_ledm_up:
                        //
                        if (emotion_ledm_flag[0] == true) {
                            emotion_ledm_up.setBackgroundResource(R.drawable.button_up_on);
                            emotion_ledm_flag[0] = false;
                        } else {
                            emotion_ledm_up.setBackgroundResource(R.drawable.button_up_off);
                            emotion_ledm_flag[0] = true;
                        }
                        break;
                    case R.id.emotion_ledm_down:
                        //
                        if (emotion_ledm_flag[1] == true) {
                            emotion_ledm_down.setBackgroundResource(R.drawable.button_down_on);
                            emotion_ledm_flag[1] = false;
                        } else {
                            emotion_ledm_down.setBackgroundResource(R.drawable.button_down_off);
                            emotion_ledm_flag[1] = true;
                        }
                        break;
                    case R.id.emotion_led_up:
                        //
                        if (emotion_led_flag[0] == true) {
                            emotion_led_up.setBackgroundResource(R.drawable.button_up_on);
                            emotion_led_flag[0] = false;
                        } else {
                            emotion_led_up.setBackgroundResource(R.drawable.button_up_off);
                            emotion_led_flag[0] = true;
                        }
                        break;
                    case R.id.emotion_led_down:
                        //
                        if (emotion_led_flag[1] == true) {
                            emotion_led_down.setBackgroundResource(R.drawable.button_down_on);
                            emotion_led_flag[1] = false;
                        } else {
                            emotion_led_down.setBackgroundResource(R.drawable.button_down_off);
                            emotion_led_flag[1] = true;
                        }
                        break;
                    //--------------------------------------------------
                    case R.id.emotion_soundm_up:
                        //
                        if (emotion_soundm_flag[0] == true) {
                            emotion_soundm_up.setBackgroundResource(R.drawable.button_up_on);
                            emotion_soundm_flag[0] = false;
                        } else {
                            emotion_soundm_up.setBackgroundResource(R.drawable.button_up_off);
                            emotion_soundm_flag[0] = true;
                        }
                        break;
                    case R.id.emotion_soundm_down:
                        //
                        if (emotion_soundm_flag[1] == true) {
                            emotion_soundm_down.setBackgroundResource(R.drawable.button_down_on);
                            emotion_soundm_flag[1] = false;
                        } else {
                            emotion_soundm_down.setBackgroundResource(R.drawable.button_down_off);
                            emotion_soundm_flag[1] = true;
                        }
                        break;
                    case R.id.emotion_sound_up:
                        //
                        if (emotion_sound_flag[0] == true) {
                            emotion_sound_up.setBackgroundResource(R.drawable.button_up_on);
                            emotion_sound_flag[0] = false;
                        } else {
                            emotion_sound_up.setBackgroundResource(R.drawable.button_up_off);
                            emotion_sound_flag[0] = true;
                        }
                        break;
                    case R.id.emotion_sound_down:
                        //
                        if (emotion_sound_flag[1] == true) {
                            emotion_sound_down.setBackgroundResource(R.drawable.button_down_on);
                            emotion_sound_flag[1] = false;
                        } else {
                            emotion_sound_down.setBackgroundResource(R.drawable.button_down_off);
                            emotion_sound_flag[1] = true;
                        }
                        break;
                    case R.id.emotion_back:
                        if (emotion_b_f == true) {
                            emotion_back.setBackgroundResource(R.drawable.button_elipse_back_on);
                            emotion_b_f = false;
                            finish();
                        } else {
                            emotion_back.setBackgroundResource(R.drawable.button_elipse_back_off);
                            emotion_b_f = true;
                        }
                        break;
                }
            }
        };

        emotion_ledm_up.setOnClickListener(listener);
        emotion_ledm_down.setOnClickListener(listener);
        emotion_led_up.setOnClickListener(listener);
        emotion_led_down.setOnClickListener(listener);
        emotion_soundm_up.setOnClickListener(listener);
        emotion_soundm_down.setOnClickListener(listener);
        emotion_sound_up.setOnClickListener(listener);
        emotion_sound_down.setOnClickListener(listener);
        emotion_back.setOnClickListener(listener);

    }
}
