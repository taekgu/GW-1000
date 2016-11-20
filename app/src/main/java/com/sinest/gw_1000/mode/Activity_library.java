package com.sinest.gw_1000.mode;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ToggleButton;

import com.sinest.gw_1000.R;

public class Activity_library extends AppCompatActivity {

    public static final int REQUEST_CODE_LIBRARY = 1002;
    public static final int REQUEST_CODE_MANUAL_MODE_SETTING = 1003;

    int cnt;
    int manual_cnt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_library);

        Button library_back_button = (Button)findViewById(R.id.library_back_button);
        Button library_save_button = (Button)findViewById(R.id.library_save_button);
        Button library_set_button = (Button)findViewById(R.id.library_set_button);

        library_back_button.setOnTouchListener(mTouchEvent);
        library_save_button.setOnTouchListener(mTouchEvent);
        library_set_button.setOnTouchListener(mTouchEvent);

    }

    private View.OnTouchListener mTouchEvent = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            Intent intent;
            Button b = (Button) view;
            int action = motionEvent.getAction();
            int id = view.getId();
            if (action == MotionEvent.ACTION_DOWN) {
                switch (id) {
                    case R.id.library_back_button:
                        b.setBackgroundResource(R.drawable.button_circle_back_on);
                        break;
                    case R.id.library_save_button:
                        b.setBackgroundResource(R.drawable.save_mode_on);
                        break;
                    case R.id.library_set_button:
                        b.setBackgroundResource(R.drawable.library_setting_on);
                        break;

                }
            }
            else if (action == MotionEvent.ACTION_UP) {
                switch (id) {
                    case R.id.library_back_button:
                        b.setBackgroundResource(R.drawable.button_circle_back_off);
                        finish();
                        break;
                    case R.id.library_save_button:
                        b.setBackgroundResource(R.drawable.save_mode_off);
                        break;
                    case R.id.library_set_button:
                        b.setBackgroundResource(R.drawable.library_setting_off);
                        if(manual_cnt==1) {
                            intent = new Intent(getApplicationContext(), Activity_manual_mode_setting.class);
                            startActivityForResult(intent, REQUEST_CODE_MANUAL_MODE_SETTING);
                        }
                        break;
                }
            }
            return true;
            }
    };
    public void onClicked(View v)
    {
        ToggleButton tb;
        int id = v.getId();
        int resourceId, tb_resourceId;

        for(int i=1; i<=15; i++){
            resourceId = getResources().getIdentifier("automode_"+i,"id","com.sinest.gw_1000");
            if(resourceId==id)
            {
                tb = (ToggleButton)findViewById(resourceId);
                if(tb.isChecked()) {
                    tb_resourceId = getResources().getIdentifier("automode_on_"+i,"drawable","com.sinest.gw_1000");
                    if (cnt > 3)
                        tb.setChecked(false);
                    else
                    {
                        tb.setBackgroundResource(tb_resourceId);
                        cnt++;
                    }
                }
                else{
                    tb_resourceId = getResources().getIdentifier("automode_"+i,"drawable","com.sinest.gw_1000");
                    tb.setBackgroundResource(tb_resourceId);
                    cnt--;
                }
            }
        }
        for(int i=1; i<=5; i++) {
            resourceId = getResources().getIdentifier("manual_mode_"+i,"id","com.sinest.gw_1000");
            if(resourceId==id)
            {
                tb = (ToggleButton)findViewById(resourceId);
                if(tb.isChecked()) {
                    tb_resourceId = getResources().getIdentifier("manual_mode_on_"+i,"drawable","com.sinest.gw_1000");
                    if (cnt > 3)
                        tb.setChecked(false);
                    else
                    {
                        tb.setBackgroundResource(tb_resourceId);
                        manual_cnt++;
                        cnt++;
                    }
                }
                else{
                    tb_resourceId = getResources().getIdentifier("manual_mode_"+i,"drawable","com.sinest.gw_1000");
                    tb.setBackgroundResource(tb_resourceId);
                    manual_cnt--;
                    cnt--;
                }
            }
        }
    }
}
