package com.sinest.gw_1000.mode;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextClock;
import android.widget.Toast;

import com.sinest.gw_1000.R;
import com.sinest.gw_1000.management.Application_manager;
import com.sinest.gw_1000.mode.utils.CustomSeekBar;
import com.sinest.gw_1000.mode.utils.CustomTextClock;

public class Activity_library extends AppCompatActivity{

    public static final int REQUEST_CODE_LIBRARY = 1002;
    public static final int REQUEST_CODE_MANUAL_MODE_SETTING = 1003;

    int cnt = 0;
    int manual_cnt = 0;

    int[] checked_loc = new int[Application_manager.MAX_CHECKED];
    int[] library_map = new int[20];

    int mode_setting = 0;

    CustomTextClock clock;
    private CustomSeekBar customSeekBar = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_library);
        Application_manager.setFullScreen(this);

        // 폰트 설정
        Typeface tf = Typeface.createFromAsset(getAssets(), "fonts/digital.ttf");
        clock = (CustomTextClock) findViewById(R.id.library_clock);

        for (int i=0; i<20; i++) {

            library_map[i] = 0;
        }

        SharedPreferences sharedPreferences = getSharedPreferences(Application_manager.NAME_OF_SHARED_PREF, 0);

        int imageView_id, image_id;
        ImageView imageView;
        for (int i = 0; i< Application_manager.MAX_CHECKED; i++) {

            checked_loc[i] = sharedPreferences.getInt(Application_manager.LIBRARY_LOC_ + i, i);
            library_map[checked_loc[i]] = 1;

            imageView_id = getResources().getIdentifier("library_mode" + (checked_loc[i]+1), "id", "com.sinest.gw_1000");
            imageView = (ImageView) findViewById(imageView_id);
            image_id = getResources().getIdentifier("mode" + (checked_loc[i]+1) + "_on", "drawable", "com.sinest.gw_1000");
            imageView.setBackgroundResource(image_id);

            if (checked_loc[i] >= 15) {

                manual_cnt++;
            }

            cnt++;
        }

        for (int m=0; m<Application_manager.MAX_CHECKED; m++) {

            Log.i("JW", "checked_loc[" + m + "] = " + checked_loc[m]);
        }
        Log.i("JW", "library loaded, cnt = " + cnt + ", manual_cnt = " + manual_cnt);

        ImageView library_back_button = (ImageView)findViewById(R.id.library_back_button);
        ImageView library_save_button = (ImageView)findViewById(R.id.library_save_button);
        ImageView library_set_button = (ImageView)findViewById(R.id.library_set_button);

        customSeekBar = (CustomSeekBar)findViewById(R.id.customSeekBar);

        library_back_button.setOnTouchListener(mTouchEvent);
        library_save_button.setOnTouchListener(mTouchEvent);
        library_set_button.setOnTouchListener(mTouchEvent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Application_manager.setFullScreen(this);

        clock.registReceiver();
        if(Application_manager.t_flag[1] == 1)
        {
            clock.doInit_time();
            Application_manager.t_flag[1] = 0;
        }
        change_mode(mode_setting);
    }

    @Override
    protected void onPause() {
        super.onPause();
        clock.unregistReceiver();
    }

    private View.OnTouchListener mTouchEvent = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            Intent intent;
            int action = motionEvent.getAction();
            int id = view.getId();
            if (action == MotionEvent.ACTION_DOWN) {
                switch (id) {
                    case R.id.library_back_button:
                        view.setBackgroundResource(R.drawable.button_circle_back_on);
                        break;
                    case R.id.library_save_button:
                        view.setBackgroundResource(R.drawable.save_mode_on);
                        break;
                    case R.id.library_set_button:
                        view.setBackgroundResource(R.drawable.library_setting_on);
                        break;

                }
            }
            else if (action == MotionEvent.ACTION_UP) {
                switch (id) {
                    case R.id.library_back_button:
                        view.setBackgroundResource(R.drawable.button_circle_back_off);
                        finish();
                        break;
                    case R.id.library_save_button:
                        view.setBackgroundResource(R.drawable.save_mode_off);

                        if (mode_setting == 0) {

                            if (cnt == 4) {

                                SharedPreferences sharedPreferences = getSharedPreferences(Application_manager.NAME_OF_SHARED_PREF, 0);
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                for (int i = 0; i < Application_manager.MAX_CHECKED; i++) {

                                    editor.putInt(Application_manager.LIBRARY_LOC_ + i, checked_loc[i]);
                                    Log.i("JW", "Save checked_loc(0-19)" + i + " set " + checked_loc[i]);
                                }
                                editor.commit();
                                finish();
                            }
                        }
                        break;
                    case R.id.library_set_button:
                        view.setBackgroundResource(R.drawable.library_setting_off);

                        if (mode_setting == 0) {

                            mode_setting = 1;

                            change_mode(mode_setting);
                        }
                        else {

                            mode_setting = 0;

                            change_mode(mode_setting);
                        }
                        break;
                }
            }
            return true;
            }
    };
    public void onClicked(View v)
    {
        ImageView imageView;
        int id = v.getId();

        int imageView_id, image_id;

        // 일반 선택모드인 경우
        if (mode_setting == 0) {

            for (int i = 0; i < 20; i++) {

                imageView_id = getResources().getIdentifier("library_mode" + (i + 1), "id", "com.sinest.gw_1000");

                if (imageView_id == id) {

                    imageView = (ImageView) findViewById(imageView_id);

                    if (library_map[i] == 1) {

                        library_map[i] = 0;
                        Log.i("JW", "library_mode " + (i + 1) + " checked false");
                        image_id = getResources().getIdentifier("mode" + (i + 1), "drawable", "com.sinest.gw_1000");
                        imageView.setBackgroundResource(image_id);

                        for (int j = 0; j < Application_manager.MAX_CHECKED - 1; j++) {

                            if (checked_loc[j] == i) {

                                for (int k = j; k < Application_manager.MAX_CHECKED - 1; k++) {

                                    checked_loc[k] = checked_loc[k + 1];
                                    Log.i("JW", "checked_loc[" + k + "] = checked_loc[" + (k + 1) + "]");
                                }
                            }
                        }
                        cnt--;
                        checked_loc[cnt] = -1;
                        for (int m = 0; m < Application_manager.MAX_CHECKED; m++) {

                            Log.i("JW", "checked_loc[" + m + "] = " + checked_loc[m]);
                        }
                        if (i >= 15) {

                            manual_cnt--;
                        }
                    } else {

                        if (cnt < 4) {

                            library_map[i] = 1;
                            Log.i("JW", "library_mode " + (i + 1) + " checked true");
                            image_id = getResources().getIdentifier("mode" + (i + 1) + "_on", "drawable", "com.sinest.gw_1000");
                            imageView.setBackgroundResource(image_id);

                            checked_loc[cnt] = i;
                            cnt++;
                            if (i >= 15) {

                                manual_cnt++;
                            }
                        }
                        // 라이브러리 4개 이상 선택 시
                        else {

                            Toast.makeText(this, "4개 초과 선택할 수 없습니다", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }
        }
        // 매뉴얼 모드 세팅일 때
        else {

            for (int i = 15; i < 20; i++) {

                imageView_id = getResources().getIdentifier("library_mode" + (i + 1), "id", "com.sinest.gw_1000");

                if (imageView_id == id) {

                    mode_setting = 0;
                    int modeNum = i - 14;
                    Intent intent = new Intent(getApplicationContext(), Activity_manual_mode_setting.class);
                    intent.putExtra("modeNum", modeNum);
                    startActivity(intent);
                }
            }
        }
    }


    private void change_mode(int mode) {

        if (mode == 1) {

            // 체크 모두 해제
            int imageView_id, image_id;
            ImageView imageView;
            for (int i = 0; i < 15; i++) {

                imageView_id = getResources().getIdentifier("library_mode" + (i + 1), "id", "com.sinest.gw_1000");
                imageView = (ImageView) findViewById(imageView_id);
                image_id = getResources().getIdentifier("mode" + (i + 1), "drawable", "com.sinest.gw_1000");
                imageView.setBackgroundResource(image_id);
                imageView.setEnabled(false);
            }
            for (int i = 15; i < 20; i++) {

                imageView_id = getResources().getIdentifier("library_mode" + (i + 1), "id", "com.sinest.gw_1000");
                imageView = (ImageView) findViewById(imageView_id);
                image_id = getResources().getIdentifier("mode" + (i + 1) + "_on", "drawable", "com.sinest.gw_1000");
                imageView.setBackgroundResource(image_id);
            }
        }
        else {

            // 다시 원래대로
            int imageView_id, image_id;
            ImageView imageView;
            for (int i = 0; i < 20; i++) {

                imageView_id = getResources().getIdentifier("library_mode" + (i + 1), "id", "com.sinest.gw_1000");
                imageView = (ImageView) findViewById(imageView_id);
                if (library_map[i] == 1) {

                    image_id = getResources().getIdentifier("mode" + (i + 1) + "_on", "drawable", "com.sinest.gw_1000");
                }
                else {

                    image_id = getResources().getIdentifier("mode" + (i + 1), "drawable", "com.sinest.gw_1000");
                }
                imageView.setBackgroundResource(image_id);
                imageView.setEnabled(true);
            }
        }


    }



}
