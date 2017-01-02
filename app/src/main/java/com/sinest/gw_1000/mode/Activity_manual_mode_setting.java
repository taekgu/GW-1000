package com.sinest.gw_1000.mode;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.sinest.gw_1000.R;
import com.sinest.gw_1000.management.Application_manager;
import com.sinest.gw_1000.mode.utils.CustomProgressBar;

public class Activity_manual_mode_setting extends Activity{

    public static final int REQUEST_CODE_MANUAL_PATTERN_01 = 1011;
    public static final int REQUEST_CODE_MANUAL_PATTERN_02 = 1012;
    public static final int REQUEST_CODE_MANUAL_PATTERN_03 = 1013;

    public static final int REQUEST_CODE_MANUAL_TEXT_01 = 1021;
    public static final int REQUEST_CODE_MANUAL_TEXT_02 = 1022;
    public static final int REQUEST_CODE_MANUAL_TEXT_03 = 1023;

    private int modeNum; // 어떤 매뉴얼 모드인지 1~5
    private int[] pattern = new int[3]; // 선택되어있던 패턴
    private int[] pattern_prev = new int[3]; // 팝업에서 변경되기 전 초기 패턴
    private int[] time = new int[3]; // 선택되어있던 시간
    private int[] time_prev = new int[3]; // 팝업에서 변경되기 전 초기 시간
    private int[][] section = new int[3][2];

    private ImageView[] manual_mode_setting = new ImageView[3];
    private TextView[] manual_mode_time = new TextView[3];
    private TextView manual_mode_total;
    private CustomProgressBar[] custom_progress_bar = new CustomProgressBar[3];

    private int num_of_enabled_pattern = 0;
    boolean isRun;
    TextView clock;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manual_mode_setting);
        Application_manager.setFullScreen(this);
        // 선택한 매뉴얼 모드 이미지 넣기
        ImageView manual_setting_selected_mode = (ImageView) findViewById(R.id.manual_setting_selected_mode);
        Intent intent = getIntent();

        Button manual_mode_setting_save = (Button)findViewById(R.id.manual_mode_setting_save);
        Button manual_mode_setting_back = (Button)findViewById(R.id.manual_mode_setting_back);


        manual_mode_total = (TextView)findViewById(R.id.manual_mode_total);
        Typeface tf = Typeface.createFromAsset(getAssets(), "fonts/digital.ttf");

        clock = (TextView) findViewById(R.id.manual_mode_setting_clock);
        clock.setTypeface(tf);

        modeNum = intent.getExtras().getInt("modeNum");
        if (modeNum != -1) {

            int resourceId = getResources().getIdentifier("mode" + (modeNum + 15) + "_on", "drawable", "com.sinest.gw_1000");
            manual_setting_selected_mode.setBackgroundResource(resourceId);
        }

        // 선택한 매뉴얼 모드 정보 로드
        SharedPreferences sharedPreferences = getSharedPreferences(Application_manager.DB_NAME, 0);
        int resourceId;
        for (int i=0; i<3; i++) {

            pattern[i] = sharedPreferences.getInt(Application_manager.DB_MANUAL_MODE_PATTERN_ + modeNum + "_" + i, 1);
            pattern_prev[i] = pattern[i];
            time[i] = sharedPreferences.getInt(Application_manager.DB_MANUAL_MODE_TIME_ + modeNum + "_" + i, 30);
            time_prev[i] = time[i];
            section[i][0] = sharedPreferences.getInt(Application_manager.DB_MANUAL_MODE_SECTION_MIN_ + modeNum + "_" + i, 1);
            section[i][1] = sharedPreferences.getInt(Application_manager.DB_MANUAL_MODE_SECTION_MAX_ + modeNum + "_" + i, 1);

            resourceId = getResources().getIdentifier("manual_mode_progress_bar_" + (i+1), "id", "com.sinest.gw_1000");
            custom_progress_bar[i] = (CustomProgressBar)findViewById(resourceId);
            custom_progress_bar[i].setMinimumProgress(section[i][0]);
            custom_progress_bar[i].setMaximumProgress(section[i][1]);

            resourceId = getResources().getIdentifier("manual_mode_setting_" + (i+1), "id", "com.sinest.gw_1000");
            manual_mode_setting[i] = (ImageView)findViewById(resourceId);

            resourceId = getResources().getIdentifier("manual_mode_time_" + (i+1), "id", "com.sinest.gw_1000");
            manual_mode_time[i] = (TextView)findViewById(resourceId);
            manual_mode_time[i].setTypeface(tf);
            manual_mode_time[i].setText(""+time[i]);
            manual_mode_time[i].setOnClickListener(onClickListener);

            resourceId = getResources().getIdentifier("manual_mode_pattern_" + pattern[i], "drawable", "com.sinest.gw_1000");
            manual_mode_setting[i].setBackgroundResource(resourceId);
        }
        manual_mode_setting_save.setOnTouchListener(mTouchEvent);
        manual_mode_setting_back.setOnTouchListener(mTouchEvent);
        manual_mode_total.setTypeface(tf);
        manual_mode_total.setText(""+(time[0] + time[1] + time[2]));
    }

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            int id = view.getId();
            Intent intent;

            if(id==R.id.manual_mode_time_1 || id==R.id.manual_mode_time_2 || id==R.id.manual_mode_time_3)
            {
                intent = new Intent(getApplicationContext(), Activity_waiting_working_time_popup.class);
                intent.putExtra("modeNum", modeNum);
                if(id==R.id.manual_mode_time_1)
                    intent.putExtra("mode", 1);
                else if(id==R.id.manual_mode_time_2)
                    intent.putExtra("mode", 2);
                else
                    intent.putExtra("mode", 3);
                startActivity(intent);;
            }
        }
    };

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

                }
            } else if (action == MotionEvent.ACTION_UP) {
                switch (id) {
                    case R.id.manual_mode_setting_save:
                        b = (Button) view;
                        b.setBackgroundResource(R.drawable.save_mode_off);

                        SharedPreferences sharedPreferences;
                        SharedPreferences.Editor editor;

                        if (num_of_enabled_pattern != 0) {
                            // 변경된 값 저장 후 액티비티 종료
                            sharedPreferences = getSharedPreferences(Application_manager.DB_NAME, 0);
                            editor = sharedPreferences.edit();

                            for (int i = 0; i < 3; i++) {
                                editor.putInt(Application_manager.DB_MANUAL_MODE_PATTERN_ + modeNum + "_" + i, pattern[i]);
                                editor.putInt(Application_manager.DB_MANUAL_MODE_TIME_ + modeNum + "_" + i, time[i]);
                            }
                            editor.commit();
                            finish();
                        }
                        else {

                            Toast.makeText(getApplicationContext(), "하나 이상의 패턴을 사용해야합니다.", Toast.LENGTH_SHORT).show();
                        }
                        break;
                    case R.id.manual_mode_setting_back:
                        b = (Button) view;
                        b.setBackgroundResource(R.drawable.button_circle_back_off);
                        for (int i=0; i<3; i++) {

                            time[i] = time_prev[i];
                            pattern[i] = pattern_prev[i];
                        }
                        // 시간 이전 시간값으로 되돌린 후 종료
                        sharedPreferences = getSharedPreferences(Application_manager.DB_NAME, 0);
                        editor = sharedPreferences.edit();

                        for (int i=0; i<3; i++) {
                            editor.putInt(Application_manager.DB_MANUAL_MODE_PATTERN_ + modeNum + "_" + i, pattern[i]);
                            editor.putInt(Application_manager.DB_MANUAL_MODE_TIME_ + modeNum + "_" + i, time[i]);
                        }
                        editor.commit();
                        finish();
                        break;
                }
            }
            return true;
        }
    };

    protected void onResume() {
        super.onResume();
        Application_manager.setFullScreen(this);
        isRun = true;

        SharedPreferences sharedPreferences = getSharedPreferences(Application_manager.DB_NAME, 0);
        int resourceId;

        Log.i("onResume", "onResume");

        for (int i=0; i<3; i++) {
            section[i][0] = sharedPreferences.getInt(Application_manager.DB_MANUAL_MODE_SECTION_MIN_ + modeNum + "_" + i, 1);
            section[i][1] = sharedPreferences.getInt(Application_manager.DB_MANUAL_MODE_SECTION_MAX_ + modeNum + "_" + i, 1);
            pattern[i] = sharedPreferences.getInt(Application_manager.DB_MANUAL_MODE_PATTERN_ + modeNum + "_" + i, 1);
            time[i] = sharedPreferences.getInt(Application_manager.DB_MANUAL_MODE_TIME_ + modeNum + "_" + i, 30);
            num_of_enabled_pattern = 0;
            if (time[i] == 0) {

                manual_mode_setting[i].setVisibility(View.INVISIBLE);
            }
            else {

                num_of_enabled_pattern++;
                manual_mode_setting[i].setVisibility(View.VISIBLE);
            }

            Log.i("pattern[i]", Integer.toString(pattern[i]));
            Log.i("modeNum", Integer.toString(modeNum));
            Log.i("section_min", Integer.toString(section[i][0]));
            Log.i("section_max", Integer.toString(section[i][1]));

            resourceId = getResources().getIdentifier("manual_mode_pattern_" + pattern[i], "drawable", "com.sinest.gw_1000");
            manual_mode_setting[i].setBackgroundResource(resourceId);

            resourceId = getResources().getIdentifier("manual_mode_progress_bar_" + (i+1), "id", "com.sinest.gw_1000");
            custom_progress_bar[i] = (CustomProgressBar)findViewById(resourceId);

            custom_progress_bar[i].setMinimumProgress(section[i][0]);
            custom_progress_bar[i].setMaximumProgress(section[i][1]);

            manual_mode_time[i].setText(""+time[i]);
        }
        manual_mode_total.setText(""+(time[0] + time[1] + time[2]));
    }

    @Override
    protected void onPause() {
        super.onPause();
        isRun = false;
    }

    @Override
    protected void onStart() {
        super.onStart();
        isRun = true;
        Thread myThread = new Thread(new Runnable() {
            public void run() {
                while (true) {
                    try {
                        handler.sendMessage(handler.obtainMessage());
                        Thread.sleep(1000);
                    } catch (Throwable t) {
                    }
                }
            }
        });
        myThread.start();
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            updateThread();
        }
    };

    private void updateThread() {
        clock.setText(Application_manager.doInit_time());
    }

    public void onClicked(View v)
    {
        Intent intent;
        int id = v.getId();
        Log.i("test", "onClicked");
        if(id==R.id.manual_mode_setting_1 || id==R.id.manual_mode_setting_2 || id==R.id.manual_mode_setting_3)
        {
            intent = new Intent(getApplicationContext(), Activity_manual_mode_pattern_popup.class);
            Log.i("test", "onClicked:imageView");
            intent.putExtra("modeNum", modeNum);

            if(id==R.id.manual_mode_setting_1) {
                intent.putExtra("i", 0);
                intent.putExtra("currentPattern", pattern[0]);
                intent.putExtra("seekbar_min", section[0][0]);
                intent.putExtra("seekbar_max", section[0][1]);
            }
            else if(id==R.id.manual_mode_setting_2) {
                intent.putExtra("i", 1);
                intent.putExtra("currentPattern", pattern[1]);
                intent.putExtra("seekbar_min", section[1][0]);
                intent.putExtra("seekbar_max", section[1][1]);
            }
            else{
                intent.putExtra("i", 2);
                intent.putExtra("currentPattern", pattern[2]);
                intent.putExtra("seekbar_min", section[2][0]);
                intent.putExtra("seekbar_max", section[2][1]);
            }
            startActivity(intent);
        }

    }
}