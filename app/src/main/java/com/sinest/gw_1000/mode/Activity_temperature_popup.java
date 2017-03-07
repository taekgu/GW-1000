package com.sinest.gw_1000.mode;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.sinest.gw_1000.R;
import com.sinest.gw_1000.management.Application_manager;

/**
 * Created by Jinwook on 2017-01-03.
 */

public class Activity_temperature_popup extends Activity {

    private final static int TEMPERATURE_ABOVE = 0;
    private final static int TEMPERATURE_BELOW = 1;

    private final static int TEMP_MAX = 40;
    private final static int TEMP_MIN = 25;

    TextView textView_time;
    private int temperature;
    private int mode; // 위 온도 0, 아래 온도 1
    private Context mContext;

    private ImageView background;
    private ImageView popup_keypad_enter;
    private ImageView popup_keypad_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        layoutParams.dimAmount = 0.7f;
        getWindow().setAttributes(layoutParams);
        setContentView(R.layout.activity_waiting_time_popup);
        Application_manager.setFullScreen(this);

        mContext = this;

        Intent intent = getIntent();
        mode = intent.getIntExtra("mode", -1);
        temperature = intent.getIntExtra("temp", 0);

        popup_keypad_enter = (ImageView)findViewById(R.id.popup_keypad_enter);
        popup_keypad_back = (ImageView)findViewById(R.id.popup_keypad_back);

        popup_keypad_enter.setOnTouchListener(mTouchEvent);
        popup_keypad_back.setOnTouchListener(mTouchEvent);

        setScreen();

        // 폰트 설정
        Typeface tf = Typeface.createFromAsset(getAssets(), "fonts/digital.ttf");
        textView_time = (TextView) findViewById(R.id.working_time_popup_text);
        textView_time.setText(""+temperature);
        textView_time.setTypeface(tf);
    }

    @Override
    protected void onResume() {
        super.onResume();

        // 슬립 모드 동작 재시작
        Application_manager.setSleep_f(0,true);
    }

    private void setScreen() {

        // 배경 설정
        background = (ImageView) findViewById(R.id.working_time_standard);
        // 언어 중국어일 때
        if (Application_manager.img_flag == 1) {

            // 내부온도 창일 경우
            if (mode == TEMPERATURE_ABOVE) {

                background.setBackgroundResource(R.drawable.keypad_internal_temp_cn);
            }
            // 수온 창일 경우
            else if (mode == TEMPERATURE_BELOW) {

                background.setBackgroundResource(R.drawable.keypad_water_temp_cn);
            }

            // 엔터, 백 버튼 배경
            popup_keypad_enter.setBackgroundResource(R.drawable.keypad_enter_ch);
            popup_keypad_back.setBackgroundResource(R.drawable.keypad_back_ch);
        }
        // 나머지 언어일 때
        else {

            // 내부온도 창일 경우
            if (mode == TEMPERATURE_ABOVE) {

                background.setBackgroundResource(R.drawable.keypad_internal_temp_en);
            }
            // 수온 창일 경우
            else if (mode == TEMPERATURE_BELOW) {

                background.setBackgroundResource(R.drawable.keypad_water_temp_en);
            }

            // 엔터, 백 버튼 배경
            popup_keypad_enter.setBackgroundResource(R.drawable.keypad_enter);
            popup_keypad_back.setBackgroundResource(R.drawable.keypad_back);
        }
    }

    private View.OnTouchListener mTouchEvent = new View.OnTouchListener() {

        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {

            int action = motionEvent.getAction();
            int id = view.getId();

            if (action == MotionEvent.ACTION_UP) {

                switch (id) {

                    case R.id.popup_keypad_enter:

                        if (temperature >= TEMP_MIN && temperature <= TEMP_MAX) {

                            SharedPreferences sharedPreferences = Application_manager.getSharedPreferences();
                            SharedPreferences.Editor editor = sharedPreferences.edit();

                            if (mode == TEMPERATURE_ABOVE) {

                                editor.putInt(Application_manager.DB_TEMPERATURE_USER, temperature);
                                Application_manager.SENSOR_TEMP_USER = temperature;
                            } else {

                                editor.putInt(Application_manager.DB_TEMPERATURE_BED_USER, temperature);
                                Application_manager.SENSOR_TEMP_BED_USER = temperature;
                            }
                            editor.commit();
                            finish();
                        }
                        else {

                            Application_manager.getToastManager().popToast(3);
                            //Toast.makeText(mContext, "25~40 사이의 값을 입력해주세요", Toast.LENGTH_SHORT).show();
                        }
                        break;
                    case R.id.popup_keypad_back:

                        finish();
                        break;
                }
            }
            return true;
        }
    };

    public void onClicked(View v)
    {
        int id = v.getId();
        int resourceId;
        TextView txt;
        String t;
        int temp;

        for(int i=0; i<=9; i++)
        {
            resourceId = getResources().getIdentifier("popup_keypad_"+i,"id","com.sinest.gw_1000");
            if(resourceId == id){
                txt = (TextView)findViewById(R.id.working_time_popup_text);
                t = (String)txt.getText();
                temp = Integer.parseInt(t);

                temp = (temp * 10) + i;
                temp = (temp % 100);

                temperature = temp;
                txt.setText(Integer.toString(temp));
                break;
            }
        }
    }
}
