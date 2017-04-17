package com.sinest.gw_1000.setting;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sinest.gw_1000.R;
import com.sinest.gw_1000.communication.Communicator;
import com.sinest.gw_1000.management.Application_manager;

public class Activity_emotion extends Activity {

    Communicator communicator;

    LinearLayout emotion_popup;

    ImageView emotion_ledm_up; ImageView emotion_ledm_down;
    ImageView emotion_led_up; ImageView emotion_led_down;
    ImageView emotion_soundm_up; ImageView emotion_soundm_down;
    ImageView emotion_sound_up; ImageView emotion_sound_down;
    Button emotion_back;

    TextView led_mode; TextView sound_mode; TextView led_bright; TextView sound_volume;

    int led_mode_num;
    int sound_mode_num;
    int led_bright_num;
    int sound_volume_num;

    Byte up;
    Byte down;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        layoutParams.dimAmount = 0.7f;
        getWindow().setAttributes(layoutParams);
        setContentView(R.layout.activity_emotion);

        Application_manager.setFullScreen(this);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        emotion_popup = (LinearLayout)findViewById(R.id.emotion_popup);

        emotion_ledm_up = (ImageView)findViewById(R.id.emotion_ledm_up);
        emotion_ledm_down = (ImageView)findViewById(R.id.emotion_ledm_down);
        emotion_led_up = (ImageView)findViewById(R.id.emotion_led_up);
        emotion_led_down = (ImageView)findViewById(R.id.emotion_led_down);
        emotion_soundm_up = (ImageView)findViewById(R.id.emotion_soundm_up);
        emotion_soundm_down = (ImageView)findViewById(R.id.emotion_soundm_down);
        emotion_sound_up = (ImageView)findViewById(R.id.emotion_sound_up);
        emotion_sound_down = (ImageView)findViewById(R.id.emotion_sound_down);
        emotion_back = (Button)findViewById(R.id.emotion_back);

        led_mode = (TextView)findViewById(R.id.led_mode);
        sound_mode = (TextView)findViewById(R.id.sound_mode);
        led_bright = (TextView)findViewById(R.id.led_bright);
        sound_volume = (TextView)findViewById(R.id.sound_volume);

        communicator = Application_manager.getCommunicator();

        emotion_ledm_up.setOnTouchListener(mTouchEvent);
        emotion_ledm_down.setOnTouchListener(mTouchEvent);
        emotion_led_up.setOnTouchListener(mTouchEvent);
        emotion_led_down.setOnTouchListener(mTouchEvent);
        emotion_soundm_up.setOnTouchListener(mTouchEvent);
        emotion_soundm_down.setOnTouchListener(mTouchEvent);
        emotion_sound_up.setOnTouchListener(mTouchEvent);
        emotion_sound_down.setOnTouchListener(mTouchEvent);
        emotion_back.setOnTouchListener(mTouchEvent);

        //change
        led_mode_num = Application_manager.led_mode_num;
        led_bright_num = Application_manager.led_bright_num;
        sound_mode_num = Application_manager.sound_mode_num;
        sound_volume_num = Application_manager.sound_volume_num;

        // 프로토콜 계산 위한 값 입력
        up = (byte)led_bright_num;
        down = (byte)(led_mode_num * 16);

        Typeface tf = Typeface.createFromAsset(getAssets(), "fonts/digital.ttf");

        led_mode .setText(Integer.toString(led_mode_num));
        sound_mode .setText(Integer.toString(sound_mode_num));
        led_bright  .setText(Integer.toString(led_bright_num));
        sound_volume  .setText(Integer.toString(sound_volume_num));

        led_mode.setTypeface(tf);
        sound_mode.setTypeface(tf);
        led_bright.setTypeface(tf);
        sound_volume.setTypeface(tf);

    }

    @Override
    protected void onResume() {
        super.onResume();

        // 언어에 따른 버튼 설정
        emotion_popup.setBackgroundResource(Application_manager.emotionpopup[Application_manager.img_flag]);
        emotion_back.setBackgroundResource(Application_manager.button_elipse_back_off[Application_manager.img_flag]);

        // 슬립 모드 동작 재시작
        Application_manager.setSleep_f(0,true);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event){
        return false;
    }

    /*
    * 버튼을 누를시 -> ACTION_DOWN
    * 버튼을 눌렀다 땔시 -> ACTION_UP
    * 버튼의 이미지 변화 및 땔시 데이터 전송
     */
    private View.OnTouchListener mTouchEvent = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            Application_manager.set_m_start_sleep(0);
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
                        emotion_back.setBackgroundResource(Application_manager.button_elipse_back_on[Application_manager.img_flag]);
                        break;
                }
            } else if (action == MotionEvent.ACTION_UP) {
                switch (id) {
                    case R.id.emotion_ledm_up:
                        emotion_ledm_up.setBackgroundResource(R.drawable.button_up_off);
                        if(1 <= led_bright_num && 5 > led_bright_num)
                        {
                            led_bright_num++;
                            up = (byte)led_bright_num;
                            led_bright.setText(String.valueOf(led_bright_num));
                            communicator.set_setting(3,(byte)((byte)up|(byte)down));
                        }
                        communicator.getSocketManager().send_setting();
                        break;
                    case R.id.emotion_ledm_down:
                        emotion_ledm_down.setBackgroundResource(R.drawable.button_down_off);
                        if(1 < led_bright_num && 5 >= led_bright_num)
                        {
                            led_bright_num--;
                            up = (byte)led_bright_num;
                            led_bright.setText(String.valueOf(led_bright_num));
                            communicator.set_setting(3,(byte)((byte)up|(byte)down));
                        }
                        communicator.getSocketManager().send_setting();
                        break;
                    case R.id.emotion_led_up:
                        emotion_led_up.setBackgroundResource(R.drawable.button_up_off);
                        if(0 <= led_mode_num && 4 > led_mode_num)
                        {
                            led_mode_num++;
                            down = (byte)(led_mode_num * 16);
                            led_mode.setText(String.valueOf(led_mode_num));
                            communicator.set_setting(3,(byte)((byte)up|(byte)down));
                        }
                        communicator.getSocketManager().send_setting();
                        break;
                    case R.id.emotion_led_down:
                        emotion_led_down.setBackgroundResource(R.drawable.button_down_off);
                        if(0 < led_mode_num && 4 >= led_mode_num)
                        {
                            led_mode_num--;
                            down = (byte)(led_mode_num * 16);
                            led_mode.setText(String.valueOf(led_mode_num));
                            communicator.set_setting(3,(byte)((byte)up|(byte)down));
                        }
                        communicator.getSocketManager().send_setting();
                        break;
                    case R.id.emotion_soundm_up:
                        emotion_soundm_up.setBackgroundResource(R.drawable.button_up_off);
                        if(1 <= sound_volume_num && 5 > sound_volume_num)
                        {
                            sound_volume_num++;
                            sound_volume.setText(String.valueOf(sound_volume_num));
                        }
                        break;
                    case R.id.emotion_soundm_down:
                        emotion_soundm_down.setBackgroundResource(R.drawable.button_down_off);
                        if(1 < sound_volume_num && 5 >= sound_volume_num)
                        {
                            sound_volume_num--;
                            sound_volume.setText(String.valueOf(sound_volume_num));
                        }
                        break;
                    case R.id.emotion_sound_up:
                        emotion_sound_up.setBackgroundResource(R.drawable.button_up_off);
                        if(0 <= sound_mode_num && 5 > sound_mode_num)
                        {
                            sound_mode_num++;
                            sound_mode.setText(String.valueOf(sound_mode_num));
                        }
                        break;
                    case R.id.emotion_sound_down:
                        emotion_sound_down.setBackgroundResource(R.drawable.button_down_off);
                        if(0 < sound_mode_num && 5 >= sound_mode_num)
                        {
                            sound_mode_num--;
                            sound_mode.setText(String.valueOf(sound_mode_num));
                        }
                        break;
                    case R.id.emotion_back:
                        emotion_back.setBackgroundResource(Application_manager.button_elipse_back_off[Application_manager.img_flag]);
                        //change
                        Application_manager.set_m_emotion(led_mode_num,led_bright_num,sound_mode_num,sound_volume_num);
                        finish();
                        break;
                }
            }
            return true;
        }
    };
}