package com.sinest.gw_1000.mode;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.sinest.gw_1000.R;

public class Activity_manual_mode_setting extends AppCompatActivity {

    public static final int REQUEST_CODE_MANUAL_PATTERN_01 = 1011;
    public static final int REQUEST_CODE_MANUAL_PATTERN_02 = 1012;
    public static final int REQUEST_CODE_MANUAL_PATTERN_03 = 1013;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manual_mode_setting);

        Button manual_mode_setting_save = (Button)findViewById(R.id.manual_mode_setting_save);
        Button manual_mode_setting_back = (Button)findViewById(R.id.manual_mode_setting_back);

        ImageView manual_mode_setting_1 = (ImageView)findViewById(R.id.manual_mode_setting_1);
        ImageView manual_mode_setting_2 = (ImageView)findViewById(R.id.manual_mode_setting_2);
        ImageView manual_mode_setting_3 = (ImageView)findViewById(R.id.manual_mode_setting_3);

        manual_mode_setting_save.setOnTouchListener(mTouchEvent);
        manual_mode_setting_back.setOnTouchListener(mTouchEvent);

        manual_mode_setting_1.setOnTouchListener(mTouchEvent);
        manual_mode_setting_2.setOnTouchListener(mTouchEvent);
        manual_mode_setting_3.setOnTouchListener(mTouchEvent);
    }
    private View.OnTouchListener mTouchEvent = new View.OnTouchListener() {
                        @Override
                        public boolean onTouch(View view, MotionEvent motionEvent) {
                            Button b;
                            Intent intent;
                            int action = motionEvent.getAction();
                            int id = view.getId();
                            if (action == MotionEvent.ACTION_DOWN) {
                                switch (id) {
                                    case R.id.manual_mode_setting_save:
                                        b = (Button) view;
                        b.setBackgroundResource(R.drawable.save_mode_on);
                        break;
                    case R.id.manual_mode_setting_back:
                        b = (Button) view;
                        b.setBackgroundResource(R.drawable.button_circle_back_on);
                        break;
                    case R.id.manual_mode_setting_1:
                        break;
                    case R.id.manual_mode_setting_2:
                        break;
                    case R.id.manual_mode_setting_3:
                        break;

                }
            } else if (action == MotionEvent.ACTION_UP) {
                switch (id) {
                    case R.id.manual_mode_setting_save:
                        b = (Button) view;
                        b.setBackgroundResource(R.drawable.save_mode_off);
                        break;
                    case R.id.manual_mode_setting_back:
                        b = (Button) view;
                        b.setBackgroundResource(R.drawable.button_circle_back_off);
                        finish();
                        break;
                    case R.id.manual_mode_setting_1:
                        intent = new Intent(getApplicationContext(), Activity_manual_mode_pattern_popup.class);
                        startActivityForResult(intent, REQUEST_CODE_MANUAL_PATTERN_01);
                        break;
                    case R.id.manual_mode_setting_2:
                        intent = new Intent(getApplicationContext(), Activity_manual_mode_pattern_popup.class);
                        startActivityForResult(intent, REQUEST_CODE_MANUAL_PATTERN_02);
                        break;
                    case R.id.manual_mode_setting_3:
                        intent = new Intent(getApplicationContext(), Activity_manual_mode_pattern_popup.class);
                        startActivityForResult(intent, REQUEST_CODE_MANUAL_PATTERN_03);
                        break;
                }
            }
            return true;
        }
    };
}
