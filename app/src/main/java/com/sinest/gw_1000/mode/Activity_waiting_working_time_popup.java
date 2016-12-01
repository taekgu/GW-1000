package com.sinest.gw_1000.mode;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.sinest.gw_1000.R;
import com.sinest.gw_1000.management.Application_manager;

public class Activity_waiting_working_time_popup extends Activity {

    TextView textView_workingTime;
    int workingTime;
    Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        WindowManager.LayoutParams  layoutParams = new WindowManager.LayoutParams();
        layoutParams.flags= WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        layoutParams.dimAmount= 0.7f;
        getWindow().setAttributes(layoutParams);
        setContentView(R.layout.activity_waiting_time_popup);
        Application_manager.setFullScreen(this);

        mContext = this;

        SharedPreferences sharedPreferences = getSharedPreferences(Application_manager.NAME_OF_SHARED_PREF, 0);
        workingTime = sharedPreferences.getInt(Application_manager.WAITING_WORKING_TIME, 10);

        textView_workingTime = (TextView) findViewById(R.id.working_time_popup_text);
        textView_workingTime.setText(""+workingTime);

        ImageView popup_keypad_enter = (ImageView)findViewById(R.id.popup_keypad_enter);
        ImageView popup_keypad_back = (ImageView)findViewById(R.id.popup_keypad_back);

        popup_keypad_enter.setOnTouchListener(mTouchEvent);
        popup_keypad_back.setOnTouchListener(mTouchEvent);
    }

    private View.OnTouchListener mTouchEvent = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            Button b;
            int action = motionEvent.getAction();
            int id = view.getId();
            if (action == MotionEvent.ACTION_DOWN) {
                switch (id) {
                    case R.id.popup_keypad_enter:
                        break;
                    case R.id.popup_keypad_back:
                        break;
                }
            } else if (action == MotionEvent.ACTION_UP) {
                switch (id) {
                    case R.id.popup_keypad_enter:

                        if (workingTime != 0) {

                            SharedPreferences sharedPreferences = getSharedPreferences(Application_manager.NAME_OF_SHARED_PREF, 0);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putInt(Application_manager.WAITING_WORKING_TIME, workingTime);
                            editor.commit();
                            finish();
                        }
                        else {

                            Toast.makeText(mContext, "1~90 사이의 값을 입력해주세요", Toast.LENGTH_SHORT).show();
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
        Intent intent;
        intent = new Intent(getApplicationContext(), Activity_waiting.class);
        TextView txt;
        String t;
        int temp;

        for(int i=0; i<=9; i++)
        {
            resourceId = getResources().getIdentifier("popup_keypad_"+i,"id","com.sinest.gw_1000");
            if(resourceId==id){
                txt = (TextView)findViewById(R.id.working_time_popup_text);
                t = (String)txt.getText();
                temp = Integer.parseInt(t);
                if(temp==0)
                    temp = i;
                else
                    temp = temp*10 + i;

                if(temp>=0 && temp>90)
                    temp = 0;
               workingTime = temp;
                /*intent.putExtra("working_time", temp);*/
                txt.setText(Integer.toString(temp));
                break;
            }
        }
    }
}
