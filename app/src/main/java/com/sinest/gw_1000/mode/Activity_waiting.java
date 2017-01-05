package com.sinest.gw_1000.mode;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.sinest.gw_1000.R;
import com.sinest.gw_1000.communication.Communicator;
import com.sinest.gw_1000.management.Application_broadcast;
import com.sinest.gw_1000.management.Application_manager;
import com.sinest.gw_1000.mode.utils.CustomProgressBar;
import com.sinest.gw_1000.mode.utils.CustomProgressBarHorizontal;
import com.sinest.gw_1000.setting.Activity_setting;

public class Activity_waiting extends AppCompatActivity {

    public static final int REQUEST_CODE_WORKINGTIME_POPUP  = 1001;
    private final static int SET_BUTTON_INVISIBLE           = 1002;
    private final static int SET_BUTTON_VISIBLE             = 1003;

    Communicator communicator;
    Handler handler_update_data;
    BroadcastReceiver broadcastReceiver;

    private int val_oxygen = 0;
    private int val_pressure = 0;
    private int val_time = 0;

    TextView time_text, oxygen_text, pressure_text;
    int[] checked_loc = new int[Application_manager.MAX_CHECKED];
    Fragment_waiting fragment_waiting;
    Fragment_working fragment_working;

    ImageView waiting_library_button;
    ImageView waiting_setting_button;

    TextView clock;

    TextView textView_temperature;
    TextView textView_temperature_bed;

    private int mode = 0; // 0: waiting, 1: working

    boolean flag = false;
    boolean isRun;

    private ImageView background;
    private AnimationDrawable frameAnimation;

    CustomProgressBarHorizontal seekBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_waiting);
        Application_manager.setFullScreen(this);

        communicator = Application_manager.getCommunicator();

        // 폰트 설정
        Typeface tf = Typeface.createFromAsset(getAssets(), "fonts/digital.ttf");
        clock = (TextView) findViewById(R.id.waiting_clock);
        clock.setTypeface(tf);
        Log.v("sb","back_clock : " + Application_manager.getText());
        clock.setText(Application_manager.doInit_time());

        // 산소 농도, 압력, 시간 값 불러오기
        SharedPreferences sharedPreferences = getSharedPreferences(Application_manager.DB_NAME, 0);
        val_oxygen = sharedPreferences.getInt(Application_manager.DB_VAL_OXYGEN, 0);
        val_pressure = sharedPreferences.getInt(Application_manager.DB_VAL_PRESSURE, 0);
        val_time = sharedPreferences.getInt(Application_manager.DB_VAL_TIME, 10);

        time_text = (TextView)findViewById(R.id.waiting_time_text);
        time_text.setTypeface(tf);
        oxygen_text = (TextView)findViewById(R.id.waiting_oxygen_text);
        oxygen_text.setTypeface(tf);
        pressure_text = (TextView)findViewById(R.id.waiting_pressure_text);
        pressure_text.setTypeface(tf);

        oxygen_text.setText(""+val_oxygen);
        pressure_text.setText(""+val_pressure);
        time_text.setText(""+val_time);

        Application_manager.SENSOR_TEMP = sharedPreferences.getInt(Application_manager.DB_TEMPERATURE, 0);
        Application_manager.SENSOR_TEMP_BED = sharedPreferences.getInt(Application_manager.DB_TEMPERATURE_BED, 0);
        textView_temperature = (TextView) findViewById(R.id.textView_temperature_above);
        textView_temperature_bed = (TextView) findViewById(R.id.textView_temperature_below);
        textView_temperature.setOnTouchListener(mTouchEvent);
        textView_temperature_bed.setOnTouchListener(mTouchEvent);


        fragment_waiting = new Fragment_waiting();
        for (int i = 0; i< Application_manager.MAX_CHECKED; i++) {

            checked_loc[i] = sharedPreferences.getInt(Application_manager.DB_LIBRARY_LOC_ + i, i);
            fragment_waiting.addCheckedIdx(checked_loc[i]);
            Log.i("JW", "Selected library idx : " + checked_loc[i]);
        }

        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.frameLayout_fragment, fragment_waiting);
        fragmentTransaction.commit();

        waiting_library_button = (ImageView)findViewById(R.id.waiting_library_button);
        waiting_setting_button = (ImageView)findViewById(R.id.waiting_setting_button);

        ImageView waiting_oxygen_up_button = (ImageView)findViewById(R.id.waiting_oxygen_up_button);
        ImageView waiting_oxygen_down_button = (ImageView)findViewById(R.id.waiting_oxygen_down_button);
        ImageView waiting_pressure_up_button = (ImageView)findViewById(R.id.waiting_pressure_up_button);
        ImageView waiting_pressure_down_button = (ImageView)findViewById(R.id.waiting_pressure_down_button);
        ImageView waiting_time_up_button = (ImageView)findViewById(R.id.waiting_time_up_button);
        ImageView waiting_time_down_button = (ImageView)findViewById(R.id.waiting_time_down_button);

        ImageView waiting_door_open_button = (ImageView)findViewById(R.id.waiting_dooropen_button);
        ImageView waiting_door_close_button = (ImageView)findViewById(R.id.waiting_doorclose_button);

        waiting_library_button.setOnTouchListener(mTouchEvent);
        waiting_setting_button.setOnTouchListener(mTouchEvent);

        waiting_oxygen_up_button.setOnTouchListener(mTouchEvent);
        waiting_oxygen_down_button.setOnTouchListener(mTouchEvent);
        waiting_pressure_up_button.setOnTouchListener(mTouchEvent);
        waiting_pressure_down_button.setOnTouchListener(mTouchEvent);
        waiting_time_up_button.setOnTouchListener(mTouchEvent);
        waiting_time_down_button.setOnTouchListener(mTouchEvent);

        waiting_door_open_button.setOnTouchListener(mTouchEvent);
        waiting_door_close_button.setOnTouchListener(mTouchEvent);

        time_text.setOnTouchListener(mTouchEvent);

        background = (ImageView) findViewById(R.id.activity_waiting_background);

        seekBar = (CustomProgressBarHorizontal) findViewById(R.id.seekBar2);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Application_manager.setFullScreen(this);

        registReceiver();
        isRun = true;

        background.setBackgroundResource(Application_manager.waiting_dooropen_backimage[Application_manager.img_flag]);
        waiting_door_open_button.setBackgroundResource(Application_manager.door_open_off[Application_manager.img_flag]);
        waiting_door_close_button.setBackgroundResource(Application_manager.door_close_off[Application_manager.img_flag]);

        SharedPreferences sharedPreferences = getSharedPreferences(Application_manager.DB_NAME, 0);

        if (mode == 0) {

            fragment_waiting.reset();
            for (int i = 0; i < Application_manager.MAX_CHECKED; i++) {

                checked_loc[i] = sharedPreferences.getInt(Application_manager.DB_LIBRARY_LOC_ + i, i);
                fragment_waiting.addCheckedIdx(checked_loc[i]);
                Log.i("JW", "Selected library idx : " + checked_loc[i]);
            }
            fragment_waiting.refresh();
        }

        val_time = sharedPreferences.getInt(Application_manager.DB_VAL_TIME, 10);
        time_text.setText(Integer.toString(val_time));

        // 수온 불러오기
        textView_temperature.setText(""+Application_manager.SENSOR_TEMP);
        textView_temperature_bed.setText(""+Application_manager.SENSOR_TEMP_BED);

        // 화면에 보여질 때 센서값 불러오기
        runOnUiThread(new Runnable() {
            @Override
            public void run() {

                TextView textView_oxygen = (TextView) findViewById(R.id.textView_oxygen);
                textView_oxygen.setText(""+Application_manager.SENSOR_OXYGEN);

                TextView textView_humidity = (TextView) findViewById(R.id.textView_humidity);
                textView_humidity.setText(""+Application_manager.SENSOR_HUMIDITY);

                TextView textView_temperature = (TextView) findViewById(R.id.textView_temperature_above);
                textView_temperature.setText(""+Application_manager.SENSOR_TEMP);

                TextView textView_temperature_bed = (TextView) findViewById(R.id.textView_temperature_below);
                textView_temperature_bed.setText(""+Application_manager.SENSOR_TEMP_BED);
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregistReceiver();
        isRun = false;
        //clock.unregistReceiver();

        SharedPreferences sharedPreferences = getSharedPreferences(Application_manager.DB_NAME, 0);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(Application_manager.DB_VAL_OXYGEN, val_oxygen);
        editor.putInt(Application_manager.DB_VAL_PRESSURE, val_pressure);
        editor.putInt(Application_manager.DB_VAL_TIME, val_time);
        editor.putInt(Application_manager.DB_TEMPERATURE, Application_manager.SENSOR_TEMP);
        editor.putInt(Application_manager.DB_TEMPERATURE_BED, Application_manager.SENSOR_TEMP_BED);
        editor.commit();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
    }

    public void changeFragment_working(int modeNum) {

        if (val_time > 0) {

            if (fragment_working == null) {

                fragment_working = new Fragment_working();
            }

            // 타이머 스레드가 동작중이지 않은 경우
            if (!fragment_working.getIsAlive()) {

                if (Application_manager.getSoundManager().play(Application_manager.m_language, 0) == 0) {

                    Log.i("JW", "changeFragment (waiting -> working)");

                    FragmentManager fm = getFragmentManager();
                    FragmentTransaction fragmentTransaction = fm.beginTransaction();

                    fragment_working.init(modeNum, val_time, 0);

                    fragmentTransaction.replace(R.id.frameLayout_fragment, fragment_working);
                    fragmentTransaction.commit();

                    mode = 1;
                    Application_manager.m_operation_f = true;
                    handler_update_data.sendEmptyMessage(SET_BUTTON_INVISIBLE);

                    // 동작 모드로 바뀌기 이전 산소농도, 수압, 시간 값 저장
                    SharedPreferences sharedPreferences = getSharedPreferences(Application_manager.DB_NAME, 0);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putInt(Application_manager.DB_VAL_OXYGEN, val_oxygen);
                    editor.putInt(Application_manager.DB_VAL_PRESSURE, val_pressure);
                    editor.putInt(Application_manager.DB_VAL_TIME, val_time);
                    editor.commit();

                    // 애니메이션 시작
                    start_animation();

                    // 동작 구간 표시
                    //CustomProgressBarHorizontal progressBar = (CustomProgressBarHorizontal) findViewById(R.id.custom_progress_bar_horizontal);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            seekBar.setVisibility(View.VISIBLE);
                        }
                    });

                    // 치료 음악 재생
                    if (Application_manager.sound_mode_num != 0) {

                        Application_manager.getSoundManager().play_therapy(Application_manager.sound_mode_num, 1);
                    }
                }
            }
            // 타이머 스레드가 아직 동작중인 경우
            else {

                Log.i("JW", "changeFragment (waiting -> working) is failed");
                Toast.makeText(this, "잠시 후 다시 시도해주세요", Toast.LENGTH_SHORT).show();
            }
        }
        else {

            Toast.makeText(this, "동작 시간을 1~90분 사이로 설정해야합니다", Toast.LENGTH_SHORT).show();
        }
    }

    public void changeFragment_waiting() {

        Log.i("JW", "changeFragment (working -> waiting)");
        setTimeLeft(val_time);
        FragmentManager fm = getFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();

        fragmentTransaction.replace(R.id.frameLayout_fragment, fragment_waiting);
        fragmentTransaction.commit();

        mode = 0;
        Application_manager.m_operation_f = false;
        handler_update_data.sendEmptyMessage(SET_BUTTON_VISIBLE);

        // 동작 시작 전 산소 농도, 압력, 시간 값 불러오기
        SharedPreferences sharedPreferences = getSharedPreferences(Application_manager.DB_NAME, 0);
        val_oxygen = sharedPreferences.getInt(Application_manager.DB_VAL_OXYGEN, 0);
        val_pressure = sharedPreferences.getInt(Application_manager.DB_VAL_PRESSURE, 0);
        val_time = sharedPreferences.getInt(Application_manager.DB_VAL_TIME, 10);

        runOnUiThread(new Runnable() {
            @Override
            public void run() {

                oxygen_text.setText(""+val_oxygen);
                pressure_text.setText(""+val_pressure);
                time_text.setText(""+val_time);
            }
        });

        // 애니메이션 정지
        stop_animation();

        // 동작 구간 숨김
        /*SeekBar seekBar = (SeekBar) findViewById(R.id.seekBar2);
        seekBar.setVisibility(View.INVISIBLE);*/
        runOnUiThread(new Runnable() {
            @Override
            public void run() {

                seekBar.setVisibility(View.INVISIBLE);
            }
        });

        // 치료 음악 재생 종료
        if (Application_manager.sound_mode_num != 0) {

            Application_manager.getSoundManager().play_therapy(Application_manager.sound_mode_num, 0);
        }
    }

    private void start_animation() {

        background.setBackgroundResource(R.drawable.animation_working);
        frameAnimation = (AnimationDrawable) background.getBackground();
        frameAnimation.start();
    }

    private void stop_animation() {

        if (frameAnimation != null) {

            runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    frameAnimation.stop();
                    // 도어 열림
                    if (Application_manager.getCommunicator().get_tx_idx(11) == 0x01) {

                        background.setBackgroundResource(Application_manager.waiting_dooropen_backimage[Application_manager.img_flag]);
                    }
                    // 도어 닫힘
                    else {

                        background.setBackgroundResource(R.drawable.waiting_doorclose_backimage);
                    }
                }
            });
        }
    }

    public void setTimeLeft(final int min) {

        runOnUiThread(new Runnable() {
            @Override
            public void run() {

                time_text.setText("" + min);
            }
        });
    }

    private void registReceiver() {

        if (broadcastReceiver != null) {

            return;
        }

        final IntentFilter mIntentFilter = new IntentFilter();
        mIntentFilter.addAction("update.data");

        setHandler_update_data();
        broadcastReceiver = new Application_broadcast(handler_update_data);
        this.registerReceiver(broadcastReceiver, mIntentFilter);
        Log.i("JW", "registerReceiver");
    }

    private void unregistReceiver() {

        if (broadcastReceiver != null) {

            this.unregisterReceiver(broadcastReceiver);
            broadcastReceiver = null;
            Log.i("JW", "unregisterReceiver");
        }
    }

    private void setHandler_update_data() {

        handler_update_data = new Handler() {

            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);

                if (msg.what == 1) {

                    SharedPreferences sharedPreferences = getSharedPreferences(Application_manager.DB_NAME, 0);

                    int[] onoff_flag = new int[12];
                    for (int i=1; i<=4; i++) { // 세로

                        for (int j = 1; j <= 3; j++) { // 가로

                            onoff_flag[((j-1)*4 + i - 1)] = sharedPreferences.getInt(Application_manager.DB_RFID_ONOFF + i + "" + j, 0);
                        }
                    }

                    // 산소농도 평균
                    int temp = 0;
                    for (int i=0; i<4; i++) {

                        if (onoff_flag[i] == 1) {

                            temp += communicator.get_rx_idx(i+7);
                        }
                    }
                    TextView textView_oxygen = (TextView) findViewById(R.id.textView_oxygen);
                    textView_oxygen.setText(""+temp);
                    Application_manager.SENSOR_OXYGEN = temp;

                    // 습도 평균
                    temp = 0;
                    for (int i=4; i<8; i++) {

                        if (onoff_flag[i] == 1) {

                            temp += communicator.get_rx_idx(i+7);
                        }
                    }
                    TextView textView_humidity = (TextView) findViewById(R.id.textView_humidity);
                    textView_humidity.setText(""+temp);
                    Application_manager.SENSOR_HUMIDITY = temp;

                    // 내부온도 평균
                    temp = 0;
                    for (int i=8; i<12; i++) {

                        if (onoff_flag[i] == 1) {

                            temp += communicator.get_rx_idx(i-5);
                        }
                    }
                    textView_temperature = (TextView) findViewById(R.id.textView_temperature_above);
                    textView_temperature.setText(""+temp);
                    Application_manager.SENSOR_TEMP = temp;

                    // 수온
                    temp = communicator.get_rx_idx(2);
                    textView_temperature_bed = (TextView) findViewById(R.id.textView_temperature_below);
                    textView_temperature_bed.setText(""+temp);
                    Application_manager.SENSOR_TEMP_BED = temp;

                    // 노즐 위치
                    //seekBar.setMinimumProgress();
                    //seekBar.setMaximumProgress();
                }
                else if (msg.what == SET_BUTTON_INVISIBLE) {

                    waiting_library_button.setVisibility(View.INVISIBLE);
                    waiting_setting_button.setVisibility(View.INVISIBLE);
                }
                else if (msg.what == SET_BUTTON_VISIBLE) {

                    waiting_library_button.setVisibility(View.VISIBLE);
                    waiting_setting_button.setVisibility(View.VISIBLE);
                }
            }
        };
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

    private View.OnTouchListener mTouchEvent = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            Application_manager.set_m_start_sleep(0);
            ImageView background;
            Intent intent;
            Intent intent_setting;
            int action = motionEvent.getAction();
            int id = view.getId();
            if (action == MotionEvent.ACTION_DOWN) {
                switch (id) {
                    case R.id.waiting_library_button:
                        view.setBackgroundResource(R.drawable.library_on);
                        break;
                    case R.id.waiting_setting_button:
                        //iv  = (ImageView) view;
                        view.setBackgroundResource(R.drawable.setting_on);
                        break;
                    case R.id.waiting_oxygen_up_button:
                        view.setBackgroundResource(R.drawable.button_up_on);
                        break;
                    case R.id.waiting_oxygen_down_button:
                        view.setBackgroundResource(R.drawable.button_down_on);
                        break;
                    case R.id.waiting_pressure_up_button:
                        view.setBackgroundResource(R.drawable.button_up_on);
                        break;
                    case R.id.waiting_pressure_down_button:
                        view.setBackgroundResource(R.drawable.button_down_on);
                        break;
                    case R.id.waiting_time_up_button:
                        view.setBackgroundResource(R.drawable.button_up_on);
                        break;
                    case R.id.waiting_time_down_button:
                        view.setBackgroundResource(R.drawable.button_down_on);
                        break;
                    case R.id.waiting_dooropen_button:
                        view.setBackgroundResource(Application_manager.door_open_off[Application_manager.img_flag]);
                        break;
                    case R.id.waiting_doorclose_button:
                        view.setBackgroundResource(Application_manager.door_close_off[Application_manager.img_flag]);
                        break;
                    case R.id.waiting_time_text:
                        break;
                }
            } else if (action == MotionEvent.ACTION_UP) {

                byte val = 0x00;
                switch (id) {
                    case R.id.waiting_library_button:
                        view.setBackgroundResource(R.drawable.library);
                        intent = new Intent(getApplicationContext(), Activity_library.class);
                        startActivity(intent);
                        break;
                    case R.id.waiting_setting_button:
                        view.setBackgroundResource(R.drawable.setting);
                        //setting
                        intent_setting = new Intent(getApplicationContext(), Activity_setting.class);
                        startActivity(intent_setting);
                        finish();
                        break;
                    case R.id.waiting_oxygen_up_button:
                        view.setBackgroundResource(R.drawable.button_up);

                        //if (mode == 0) {

                            val_oxygen++;
                            if (val_oxygen > 5) val_oxygen = 5;
                            oxygen_text.setText("" + val_oxygen);

                            val = (byte) val_oxygen;
                            communicator.set_tx(8, val);
                            communicator.send(communicator.get_tx());
                        //}
                        break;
                    case R.id.waiting_oxygen_down_button:
                        view.setBackgroundResource(R.drawable.button_down);

                        //if (mode == 0) {

                            val_oxygen--;
                            if (val_oxygen < 0) val_oxygen = 0;
                            oxygen_text.setText("" + val_oxygen);

                            val = (byte) val_oxygen;
                            communicator.set_tx(8, val);
                            communicator.send(communicator.get_tx());
                        //}
                        break;
                    case R.id.waiting_pressure_up_button:
                        view.setBackgroundResource(R.drawable.button_up);

                        //if (mode == 0) {

                            val_pressure += 1;
                            if (val_pressure > 6) val_pressure = 6;
                            pressure_text.setText("" + val_pressure);

                            communicator.set_tx(5, (byte) val_pressure);
                            communicator.send(communicator.get_tx());
                        //}
                        break;
                    case R.id.waiting_pressure_down_button:
                        view.setBackgroundResource(R.drawable.button_down);

                        //if (mode == 0) {

                            val_pressure -= 1;
                            if (val_pressure < 0) val_pressure = 0;
                            pressure_text.setText("" + val_pressure);

                            communicator.set_tx(5, (byte) val_pressure);
                            communicator.send(communicator.get_tx());
                        //}
                        break;
                    case R.id.waiting_time_up_button:
                        view.setBackgroundResource(R.drawable.button_up);

                        val_time += 1;
                        if (val_time > 90) val_time = 90;
                        time_text.setText("" + val_time);

                        if (mode == 1) {

                            fragment_working.setTime_m_left(val_time);
                        }
                        break;
                    case R.id.waiting_time_down_button:
                        view.setBackgroundResource(R.drawable.button_down);

                        val_time -= 1;
                        if (val_time < 1) val_time = 1;
                        time_text.setText("" + val_time);

                        if (mode == 1) {

                            fragment_working.setTime_m_left(val_time);
                        }
                        break;
                    case R.id.waiting_dooropen_button:

                        view.setBackgroundResource(Application_manager.door_open_off[Application_manager.img_flag]);

                        if (Application_manager.getSoundManager().play(Application_manager.m_language, 3) == 0) {

                            background = (ImageView) findViewById(R.id.activity_waiting_background);
                            background.setBackgroundResource(Application_manager.waiting_dooropen_backimage[Application_manager.img_flag]);

                            val = 0x01;
                            communicator.set_tx(11, val);
                            communicator.send(communicator.get_tx());
                        }
                        break;
                    case R.id.waiting_doorclose_button:

                        view.setBackgroundResource(Application_manager.door_close_off[Application_manager.img_flag]);

                        if (Application_manager.getSoundManager().play(Application_manager.m_language, 4) == 0) {

                            background = (ImageView) findViewById(R.id.activity_waiting_background);
                            background.setBackgroundResource(R.drawable.waiting_doorclose_backimage);

                            val = 0x02;
                            communicator.set_tx(11, val);
                            communicator.send(communicator.get_tx());
                        }
                        break;
                    case R.id.waiting_time_text:

                        if (mode == 0) {

                            intent = new Intent(getApplicationContext(), Activity_waiting_working_time_popup.class);
                            intent.putExtra("mode", 0);
                            startActivity(intent);
                        }
                        break;
                    case R.id.textView_temperature_above:

                        intent = new Intent(getApplicationContext(), Activity_temperature_popup.class);
                        intent.putExtra("mode", 0);
                        intent.putExtra("temp", Application_manager.SENSOR_TEMP);
                        startActivity(intent);
                        break;
                    case R.id.textView_temperature_below:

                        intent = new Intent(getApplicationContext(), Activity_temperature_popup.class);
                        intent.putExtra("mode", 1);
                        intent.putExtra("temp", Application_manager.SENSOR_TEMP_BED);
                        startActivity(intent);
                        break;
                }
            }
            return true;
        }
    };

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
}
