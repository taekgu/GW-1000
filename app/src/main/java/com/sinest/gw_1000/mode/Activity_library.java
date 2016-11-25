package com.sinest.gw_1000.mode;

import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextClock;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.sinest.gw_1000.R;
import com.sinest.gw_1000.communication.Communicator;
import com.sinest.gw_1000.management.Application_broadcast;
import com.sinest.gw_1000.management.Application_communicator;

public class Activity_library extends AppCompatActivity {

    public static final int REQUEST_CODE_LIBRARY = 1002;
    public static final int REQUEST_CODE_MANUAL_MODE_SETTING = 1003;

    int cnt = 0;
    int manual_cnt = 0;

    int[] checked_loc = new int[Application_communicator.MAX_CHECKED];
    int[] library_map = new int[20];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_library);

        // 폰트 설정
        Typeface tf = Typeface.createFromAsset(getAssets(), "fonts/digital.ttf");
        TextClock clock = (TextClock) findViewById(R.id.library_clock);
        clock.setTypeface(tf);

        for (int i=0; i<20; i++) {

            library_map[i] = 0;
        }

        SharedPreferences sharedPreferences = getSharedPreferences(Application_communicator.NAME_OF_SHARED_PREF, 0);
        for (int i=0; i<Application_communicator.MAX_CHECKED; i++) {

            checked_loc[i] = sharedPreferences.getInt(Application_communicator.LIBRARY_LOC_ + i, i);
            library_map[checked_loc[i]] = 1;

            int tb_resourceId;
            ToggleButton tb;
            if (checked_loc[i] < 15) {

                tb_resourceId = getResources().getIdentifier("automode_" + (checked_loc[i] + 1), "id", "com.sinest.gw_1000");
                tb = (ToggleButton) findViewById(tb_resourceId);
                tb_resourceId = getResources().getIdentifier("automode_on_" + (checked_loc[i] + 1), "drawable", "com.sinest.gw_1000");
            } else {

                tb_resourceId = getResources().getIdentifier("manual_mode_" + (checked_loc[i] - 14), "id", "com.sinest.gw_1000");
                tb = (ToggleButton) findViewById(tb_resourceId);
                tb_resourceId = getResources().getIdentifier("manual_mode_on_" + (checked_loc[i] - 14), "drawable", "com.sinest.gw_1000");
                manual_cnt++;
            }
            tb.setBackgroundResource(tb_resourceId);

            cnt++;
        }

        Log.i("JW", "library loaded, cnt = " + cnt + ", manual_cnt = " + manual_cnt);

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

                        if (cnt == 4) {

                            SharedPreferences sharedPreferences = getSharedPreferences(Application_communicator.NAME_OF_SHARED_PREF, 0);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            for (int i = 0; i < Application_communicator.MAX_CHECKED; i++) {

                                editor.putInt(Application_communicator.LIBRARY_LOC_ + i, checked_loc[i]);
                                Log.i("JW", "Save checked_loc" + i + " set " + checked_loc[i]);
                            }
                            editor.commit();
                            finish();
                        }
                        break;
                    case R.id.library_set_button:
                        b.setBackgroundResource(R.drawable.library_setting_off);
                        if(manual_cnt==1) {
                            // 추가해야됨

                            int modeNum = -1;
/*
                            for (int i=15; i<=19; i++) {

                                if (checked_loc[i] == 1) {

                                    modeNum = i - 14;
                                }
                            }
*/
                            intent = new Intent(getApplicationContext(), Activity_manual_mode_setting.class);
                            intent.putExtra("modeNum", modeNum);
                            startActivity(intent);
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

                if (library_map[i-1] == 1) {

                    //tb.setChecked(false);
                    library_map[i-1] = 0;
                    Log.i("JW", "Lib " + i + " checked false");
                    tb_resourceId = getResources().getIdentifier("automode_"+i,"drawable","com.sinest.gw_1000");
                    tb.setBackgroundResource(tb_resourceId);

                    for (int j=0; j<Application_communicator.MAX_CHECKED - 1; j++) {

                        if (checked_loc[j] == i-1) {

                            for (int k=j; k<Application_communicator.MAX_CHECKED - 1; k++) {

                                checked_loc[k] = checked_loc[k+1];
                            }
                        }
                    }
                    cnt--;
                    checked_loc[cnt] = -1;
                }
                else {

                    if (cnt < 4) {

                        //tb.setChecked(true);
                        library_map[i-1] = 1;
                        Log.i("JW", "Lib " + i + " checked true");
                        tb_resourceId = getResources().getIdentifier("automode_on_" + i, "drawable", "com.sinest.gw_1000");
                        tb.setBackgroundResource(tb_resourceId);

                        checked_loc[cnt] = i - 1;
                        cnt++;
                    }
                }
            }
        }
        for(int i=1; i<=5; i++) {
            resourceId = getResources().getIdentifier("manual_mode_"+i,"id","com.sinest.gw_1000");
            if(resourceId==id)
            {
                tb = (ToggleButton)findViewById(resourceId);

                if (library_map[i+14] == 1) {

                    //tb.setChecked(false);
                    library_map[i+14] = 0;
                    Log.i("JW", "Lib " + (i+14) + " checked false");
                    tb_resourceId = getResources().getIdentifier("manual_mode_"+i,"drawable","com.sinest.gw_1000");
                    tb.setBackgroundResource(tb_resourceId);

                    for (int j=0; j<Application_communicator.MAX_CHECKED - 1; j++) {

                        if (checked_loc[j] == i-1) {

                            for (int k=j; k<Application_communicator.MAX_CHECKED - 1; k++) {

                                checked_loc[k] = checked_loc[k+1];
                            }
                        }
                    }
                    cnt--;
                    checked_loc[cnt] = -1;
                    manual_cnt--;
                }
                else {

                    if (cnt < 4) {

                        //tb.setChecked(true);
                        library_map[i+14] = 1;
                        tb_resourceId = getResources().getIdentifier("manual_mode_on_" + i, "drawable", "com.sinest.gw_1000");
                        tb.setBackgroundResource(tb_resourceId);

                        checked_loc[cnt] = i+14;
                        cnt++;
                        manual_cnt++;
                    }
                }
            }
        }
    }
}
