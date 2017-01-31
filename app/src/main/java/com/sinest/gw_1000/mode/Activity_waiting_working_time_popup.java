package com.sinest.gw_1000.mode;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
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
    int mode, modeNum; //mode : 어느 액티비티에서 불러왔는지, modeNum 어떤 매뉴얼 모드인지
    int workingTime;
    Context mContext;

    private ImageView background;
    private ImageView popup_keypad_enter;
    private ImageView popup_keypad_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        // 화면 설정
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        WindowManager.LayoutParams  layoutParams = new WindowManager.LayoutParams();
        layoutParams.flags= WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        layoutParams.dimAmount= 0.7f;
        getWindow().setAttributes(layoutParams);
        setContentView(R.layout.activity_waiting_time_popup);
        Application_manager.setFullScreen(this);

        popup_keypad_enter = (ImageView)findViewById(R.id.popup_keypad_enter);
        popup_keypad_back = (ImageView)findViewById(R.id.popup_keypad_back);

        popup_keypad_enter.setOnTouchListener(mTouchEvent);
        popup_keypad_back.setOnTouchListener(mTouchEvent);

        setScreen();

        mContext = this;
        Intent intent = getIntent();
        SharedPreferences sharedPreferences = getSharedPreferences(Application_manager.DB_NAME, 0);
        mode = intent.getIntExtra("mode", -1);
        Log.i("mode", Integer.toString(mode));
        if(mode==0){ // Waiting 화면에서 넘어왔을 때
            workingTime = sharedPreferences.getInt(Application_manager.DB_VAL_TIME, 10);
            //Log.i("JW", "onCreate / val_time = " + workingTime);
            //workingTime = intent.getIntExtra("time", -1);
        }
        else
        {
            modeNum = intent.getExtras().getInt("modeNum");
            if(mode==1) //manual mode setting에서 첫번째 text
                workingTime = sharedPreferences.getInt(Application_manager.DB_MANUAL_MODE_TIME_ + modeNum + "_"+"0", 30);
            else if(mode==2) //manual mode setting에서 두번째 text
                workingTime = sharedPreferences.getInt(Application_manager.DB_MANUAL_MODE_TIME_ + modeNum + "_"+"1", 30);
            else if(mode==3) //manual mode setting에서 세번째 text
                workingTime = sharedPreferences.getInt(Application_manager.DB_MANUAL_MODE_TIME_ + modeNum + "_"+"2", 30);
        }

        // 폰트 설정
        Typeface tf = Typeface.createFromAsset(getAssets(), "fonts/digital.ttf");
        textView_workingTime = (TextView) findViewById(R.id.working_time_popup_text);
        textView_workingTime.setText(""+workingTime);
        textView_workingTime.setTypeface(tf);
    }

    private void setScreen() {

        // 배경 설정
        background = (ImageView) findViewById(R.id.working_time_standard);
        // 언어 중국어일 때
        if (Application_manager.img_flag == 1) {

            background.setBackgroundResource(R.drawable.working_time_keypad_cn);

            // 엔터, 백 버튼 배경
            popup_keypad_enter.setBackgroundResource(R.drawable.keypad_enter_ch);
            popup_keypad_back.setBackgroundResource(R.drawable.keypad_back_ch);
        }
        // 나머지 언어일 때
        else {

            background.setBackgroundResource(R.drawable.working_time_keypad);

            // 엔터, 백 버튼 배경
            popup_keypad_enter.setBackgroundResource(R.drawable.keypad_enter);
            popup_keypad_back.setBackgroundResource(R.drawable.keypad_back);
        }
    }

    private View.OnTouchListener mTouchEvent = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            Application_manager.set_m_start_sleep(0);
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

                        SharedPreferences sharedPreferences = getSharedPreferences(Application_manager.DB_NAME, 0);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        if (mode == 0) { // Waiting 화면에서 넘어왔을 때

                            if (workingTime >= 1 && workingTime <= 90) { // 0 ~ 90

                                editor.putInt(Application_manager.DB_VAL_TIME, workingTime);

                                editor.commit();
                                finish();
                            }
                            else {

                                Toast.makeText(mContext, "1~90 사이의 값을 입력해주세요", Toast.LENGTH_SHORT).show();
                            }
                        }
                        else { // 매뉴얼 모드 세팅에서 넘어왔을 때

                            if (workingTime >= 0 && workingTime <= 90) { // 0 ~ 90

                                if (mode == 1) //manual mode setting에서 첫번째 text
                                    editor.putInt(Application_manager.DB_MANUAL_MODE_TIME_ + modeNum + "_" + "0", workingTime);
                                else if (mode == 2) //manual mode setting에서 두번째 text
                                    editor.putInt(Application_manager.DB_MANUAL_MODE_TIME_ + modeNum + "_" + "1", workingTime);
                                else if (mode == 3) //manual mode setting에서 세번째 text
                                    editor.putInt(Application_manager.DB_MANUAL_MODE_TIME_ + modeNum + "_" + "2", workingTime);

                                editor.commit();
                                finish();
                            }
                            else { // 범위 초과

                                Toast.makeText(mContext, "0~90 사이의 값을 입력해주세요", Toast.LENGTH_SHORT).show();
                            }
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
        Application_manager.set_m_start_sleep(0);
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

                workingTime = temp;
                txt.setText(Integer.toString(temp));
                break;
            }
        }
    }
}
