package com.sinest.gw_1000.mode;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
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

    TextView textView_time;
    private int temperature;
    private int mode; // 위 온도 0, 아래 온도 1
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        mContext = this;
        setScreen();


        Intent intent = getIntent();
        mode = intent.getIntExtra("mode", -1);
        temperature = intent.getIntExtra("temp", 0);

        // 폰트 설정
        Typeface tf = Typeface.createFromAsset(getAssets(), "fonts/digital.ttf");
        textView_time = (TextView) findViewById(R.id.working_time_popup_text);
        textView_time.setText(""+temperature);
        textView_time.setTypeface(tf);

        ImageView popup_keypad_enter = (ImageView)findViewById(R.id.popup_keypad_enter);
        ImageView popup_keypad_back = (ImageView)findViewById(R.id.popup_keypad_back);

        popup_keypad_enter.setOnTouchListener(mTouchEvent);
        popup_keypad_back.setOnTouchListener(mTouchEvent);
    }

    private void setScreen() {

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        layoutParams.dimAmount = 0.7f;
        getWindow().setAttributes(layoutParams);
        setContentView(R.layout.activity_waiting_time_popup);
        Application_manager.setFullScreen(this);
    }

    private View.OnTouchListener mTouchEvent = new View.OnTouchListener() {

        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {

            int action = motionEvent.getAction();
            int id = view.getId();

            if (action == MotionEvent.ACTION_UP) {

                switch (id) {

                    case R.id.popup_keypad_enter:

                        if (temperature >= 1 && temperature <= 100) {

                            SharedPreferences sharedPreferences = getSharedPreferences(Application_manager.DB_NAME, 0);
                            SharedPreferences.Editor editor = sharedPreferences.edit();

                            if (mode == TEMPERATURE_ABOVE) {

                                editor.putInt(Application_manager.DB_TEMPERATURE, temperature);
                                Application_manager.SENSOR_TEMP = temperature;
                            } else {

                                editor.putInt(Application_manager.DB_TEMPERATURE_BED, temperature);
                                Application_manager.SENSOR_TEMP_BED = temperature;
                            }
                            editor.commit();
                            finish();
                        }
                        else {

                            Toast.makeText(mContext, "1~100 사이의 값을 입력해주세요", Toast.LENGTH_SHORT).show();
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
                if(temp==0)
                    temp = i;
                else
                    temp = temp*10 + i;

                if(temp<1 || temp>100)
                    temp = 0;
                temperature = temp;
                txt.setText(Integer.toString(temp));
                break;
            }
        }
    }
}
