package com.sinest.gw_1000.setting;

import android.app.Activity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.sinest.gw_1000.R;

import static android.R.attr.action;
import static android.R.attr.id;

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

    TextView led_mode; TextView sound_mode; TextView led_bright; TextView sound_volume;

    String s_buf;
    int int_buf;

    int led_mode_num;
    int sound_mode_num;
    int led_bright_num;
    int sound_volume_num;

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

        led_mode = (TextView)findViewById(R.id.led_mode);
        sound_mode = (TextView)findViewById(R.id.sound_mode);
        led_bright = (TextView)findViewById(R.id.led_bright);
        sound_volume = (TextView)findViewById(R.id.sound_volume);


/*
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
*/

        emotion_ledm_up.setOnTouchListener(mTouchEvent);
        emotion_ledm_down.setOnTouchListener(mTouchEvent);
        emotion_led_up.setOnTouchListener(mTouchEvent);
        emotion_led_down.setOnTouchListener(mTouchEvent);
        emotion_soundm_up.setOnTouchListener(mTouchEvent);
        emotion_soundm_down.setOnTouchListener(mTouchEvent);
        emotion_sound_up.setOnTouchListener(mTouchEvent);
        emotion_sound_down.setOnTouchListener(mTouchEvent);
        emotion_back.setOnTouchListener(mTouchEvent);

        led_mode_num = 0;
        sound_mode_num = 0;
        led_bright_num = 0;
        sound_volume_num = 0;
        led_mode .setText(Integer.toString(led_mode_num));
        sound_mode .setText(Integer.toString(sound_mode_num));
        led_bright  .setText(Integer.toString(led_bright_num));
        sound_volume  .setText(Integer.toString(sound_volume_num));

    }

    private View.OnTouchListener mTouchEvent = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            int action = motionEvent.getAction();
            int id = view.getId();
            if (action == MotionEvent.ACTION_DOWN) {
                switch (id) {
                    case R.id.emotion_ledm_up:
                        emotion_ledm_up.setBackgroundResource(R.drawable.button_up_on);
                        break;
                    case R.id.emotion_ledm_down:
                        emotion_ledm_down.setBackgroundResource(R.drawable.button_down_on);
                        break;
                    case R.id.emotion_led_up:
                        emotion_led_up.setBackgroundResource(R.drawable.button_up_on);
                        break;
                    case R.id.emotion_led_down:
                        emotion_led_down.setBackgroundResource(R.drawable.button_down_on);
                        break;
                    case R.id.emotion_soundm_up:
                        emotion_soundm_up.setBackgroundResource(R.drawable.button_up_on);
                        break;
                    case R.id.emotion_soundm_down:
                        emotion_soundm_down.setBackgroundResource(R.drawable.button_down_on);
                        break;
                    case R.id.emotion_sound_up:
                        emotion_sound_up.setBackgroundResource(R.drawable.button_up_on);
                        break;
                    case R.id.emotion_sound_down:
                        emotion_sound_down.setBackgroundResource(R.drawable.button_down_on);
                        break;
                    case R.id.emotion_back:
                        emotion_back.setBackgroundResource(R.drawable.button_elipse_back_on);
                        break;
                }
            } else if (action == MotionEvent.ACTION_UP) {
                switch (id) {
                    case R.id.emotion_ledm_up:
                        emotion_ledm_up.setBackgroundResource(R.drawable.button_up_off);
                        s_buf = (String)led_bright.getText();
                        int_buf = Integer.parseInt(s_buf) + 1;
                        led_bright_num++;
                        s_buf = Integer.toString(int_buf);
                        led_bright.setText(s_buf);
                        break;
                    case R.id.emotion_ledm_down:
                        emotion_ledm_down.setBackgroundResource(R.drawable.button_down_off);
                        s_buf = (String)led_bright.getText();
                        int_buf = Integer.parseInt(s_buf) - 1;
                        led_bright_num--;
                        s_buf = Integer.toString(int_buf);
                        led_bright.setText(s_buf);
                        break;
                    case R.id.emotion_led_up:
                        emotion_led_up.setBackgroundResource(R.drawable.button_up_off);
                        s_buf = (String)led_mode.getText();
                        int_buf = Integer.parseInt(s_buf) + 1;
                        led_mode_num++;
                        s_buf = Integer.toString(int_buf);
                        led_mode.setText(s_buf);
                        break;
                    case R.id.emotion_led_down:
                        emotion_led_down.setBackgroundResource(R.drawable.button_down_off);
                        s_buf = (String)led_mode.getText();
                        int_buf = Integer.parseInt(s_buf) - 1;
                        led_mode_num--;
                        s_buf = Integer.toString(int_buf);
                        led_mode.setText(s_buf);
                        break;
                    case R.id.emotion_soundm_up:
                        emotion_soundm_up.setBackgroundResource(R.drawable.button_up_off);
                        s_buf = (String)sound_volume.getText();
                        int_buf = Integer.parseInt(s_buf) + 1;
                        sound_volume_num++;
                        s_buf = Integer.toString(int_buf);
                        sound_volume.setText(s_buf);
                        break;
                    case R.id.emotion_soundm_down:
                        emotion_soundm_down.setBackgroundResource(R.drawable.button_down_off);
                        s_buf = (String)sound_volume.getText();
                        int_buf = Integer.parseInt(s_buf) - 1;
                        sound_volume_num--;
                        s_buf = Integer.toString(int_buf);
                        sound_volume.setText(s_buf);
                        break;
                    case R.id.emotion_sound_up:
                        emotion_sound_up.setBackgroundResource(R.drawable.button_up_off);
                        s_buf = (String)sound_mode.getText();
                        int_buf = Integer.parseInt(s_buf) + 1;
                        sound_mode_num++;
                        s_buf = Integer.toString(int_buf);
                        sound_mode.setText(s_buf);
                        break;
                    case R.id.emotion_sound_down:
                        emotion_sound_down.setBackgroundResource(R.drawable.button_down_off);
                        s_buf = (String)sound_mode.getText();
                        int_buf = Integer.parseInt(s_buf) - 1;
                        sound_mode_num--;
                        s_buf = Integer.toString(int_buf);
                        sound_mode.setText(s_buf);
                        break;
                    case R.id.emotion_back:
                        emotion_back.setBackgroundResource(R.drawable.button_elipse_back_off);
                        finish();
                        break;
                }
            }
            return true;
        }
    };
}
