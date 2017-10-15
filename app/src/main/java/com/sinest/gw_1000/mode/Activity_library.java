package com.sinest.gw_1000.mode;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sinest.gw_1000.R;
import com.sinest.gw_1000.management.Application_manager;

/**
 * Created by Jinwook.
 *
 * 사용 모드 선택, 메뉴얼 모드 관리
 */

public class Activity_library extends AppCompatActivity{

    int cnt = 0;
    int manual_cnt = 0;

    int[] checked_loc = new int[Application_manager.MAX_CHECKED];
    int[] library_map = new int[20]; // 20개 모드 선택 유무 (0: 선택x, 1: 선택o)

    int mode_setting = 0; // 0: 일반 모드 / 1: 매뉴얼 모드 세팅 모드

    // 시간 업데이트 스레드 동작 플래그
    boolean isRun;

    TextView clock;

    LinearLayout library_back_image;
    ImageView library_back_button;
    ImageView library_save_button;
    ImageView library_set_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_library);
        Application_manager.setFullScreen(this);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        // 폰트 설정
        Typeface tf = Typeface.createFromAsset(getAssets(), "fonts/digital.ttf");
        clock = (TextView) findViewById(R.id.library_clock);
        clock.setTypeface(tf);
        clock.setText(Application_manager.doInit_time());

        library_back_image = (LinearLayout) findViewById(R.id.ribrary_back_image);

        for (int i=0; i<20; i++) {

            library_map[i] = 0;
        }

        SharedPreferences sharedPreferences = Application_manager.getSharedPreferences();

        int imageView_id, image_id;
        ImageView imageView;
        for (int i = 0; i< Application_manager.MAX_CHECKED; i++) {

            checked_loc[i] = sharedPreferences.getInt(Application_manager.DB_LIBRARY_LOC_ + i, i);
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

        library_back_button = (ImageView)findViewById(R.id.library_back_button);
        library_save_button = (ImageView)findViewById(R.id.library_save_button);
        library_set_button = (ImageView)findViewById(R.id.library_set_button);

        library_back_button.setOnTouchListener(mTouchEvent);
        library_save_button.setOnTouchListener(mTouchEvent);
        library_set_button.setOnTouchListener(mTouchEvent);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Application_manager.set_m_start_sleep(0);
        Log.v("sb1","test");
        int action = event.getAction();
        switch(action) {
            case MotionEvent.ACTION_DOWN :    //화면을 터치했을때
                break;
            case MotionEvent.ACTION_UP :    //화면을 터치했다 땠을때
                break;
            case MotionEvent.ACTION_MOVE :    //화면을 터치하고 이동할때
                break;
        }
        return super.onTouchEvent(event);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Application_manager.setFullScreen(this);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        library_back_image.setBackgroundResource(Application_manager.ribrary_back_image[Application_manager.useChineseImage]);
        library_back_button.setBackgroundResource(Application_manager.button_circle_back_off[Application_manager.useChineseImage]);
        library_save_button.setBackgroundResource(Application_manager.save_mode_off[Application_manager.useChineseImage]);
        library_set_button.setBackgroundResource(Application_manager.library_setting_off[Application_manager.useChineseImage]);

        change_mode(mode_setting);

        // 슬립 모드 동작 재시작
        Application_manager.setSleep_f(0,true);

        isRun = true;
        Thread myThread = new Thread(new Runnable() {
            public void run() {
                while (isRun) {
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

    @Override
    protected void onPause() {
        super.onPause();

    }

    @Override
    protected void onStart() {
        super.onStart();
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

    private View.OnTouchListener mTouchEvent = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            Application_manager.set_m_start_sleep(0);
            int action = motionEvent.getAction();
            int id = view.getId();

            if (action == MotionEvent.ACTION_DOWN) {
                switch (id) {
                    case R.id.library_back_button:
                        view.setBackgroundResource(Application_manager.button_circle_back_on[Application_manager.useChineseImage]);
                        break;
                    case R.id.library_save_button:
                        view.setBackgroundResource(Application_manager.save_mode_on[Application_manager.useChineseImage]);
                        break;
                    case R.id.library_set_button:
                        view.setBackgroundResource(Application_manager.library_setting_on[Application_manager.useChineseImage]);
                        break;

                }
            }
            else if (action == MotionEvent.ACTION_UP) {
                switch (id) {
                    case R.id.library_back_button:
                        view.setBackgroundResource(Application_manager.button_circle_back_off[Application_manager.useChineseImage]);
                        finish();
                        break;
                    // Save 버튼 터치 이벤트
                    case R.id.library_save_button:
                        view.setBackgroundResource(Application_manager.save_mode_off[Application_manager.useChineseImage]);

                        if (mode_setting == 0) {

                            // 4개 모드 선택됐는지 확인
                            if (cnt == 4) {

                                SharedPreferences sharedPreferences = Application_manager.getSharedPreferences();
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                for (int i = 0; i < Application_manager.MAX_CHECKED; i++) {

                                    editor.putInt(Application_manager.DB_LIBRARY_LOC_ + i, checked_loc[i]);
                                    Log.i("JW", "Save checked_loc(0-19)" + i + " set " + checked_loc[i]);
                                }
                                editor.commit();
                                finish();
                            }
                            else {

                                Application_manager.getToastManager().popToast(0);
                            }
                        }
                        break;
                    case R.id.library_set_button:
                        view.setBackgroundResource(Application_manager.library_setting_off[Application_manager.useChineseImage]);

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
        Application_manager.set_m_start_sleep(0);
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

                            Application_manager.getToastManager().popToast(1);
                            //Toast.makeText(this, "4개 초과 선택할 수 없습니다", Toast.LENGTH_SHORT).show();
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

    /**
     * 모드 선택 / 매뉴얼 모드 세팅 모드 전환
     * @param mode 0: 모드 선택 / 1: 매뉴얼 모드 세팅
     */
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
