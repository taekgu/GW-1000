package com.sinest.gw_1000.setting;

import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
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
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sinest.gw_1000.R;
import com.sinest.gw_1000.communication.Communicator;
import com.sinest.gw_1000.management.Application_manager;
import com.sinest.gw_1000.mode.Activity_waiting;
import com.sinest.gw_1000.mode.Activity_waiting_rfid;
import com.sinest.gw_1000.mode.utils.CustomSeekbar_onethumb;

public class Activity_setting extends AppCompatActivity {

    private boolean rfid_state = false;

    Communicator communicator;

    TextView b_11;
    TextView b_21;
    TextView b_31;
    TextView b_41;
    TextView b_12;
    TextView b_22;
    TextView b_32;
    TextView b_42;
    TextView b_13;
    TextView b_23;
    TextView b_33;
    TextView b_43;

    Button b_rf;
    Button b_ex;
    Button b_wa;
    Button b_pa;
    Button b_1m;
    Button b_3m;
    Button b_5m;
    Button b_coutinue;
    Button b_back;
    Button b_emotion;
    Button b_language;
    Button b_inverter;

    Button hidden_s_1;
    Button hidden_s_2;
    Button hidden_s_3;
    Button hidden_s_4;

    TextView s_clock;

    boolean[] button_flag = new boolean[12];
    boolean[] button2_flag = {true, true, true, true};
    boolean[] button3_flag = {true, true, true, true};

    boolean[] hidden = {false, false, false, false};

    // 시간 업데이트 스레드 동작 플래그
    private boolean isRun = false;

    int ex_f = 0;

    boolean flag = false;

    int b_language_f = 0;
    int b_inverter_f = 0;

    Intent intent_emotion;
    Intent intent_rfid;
    Intent intent_wa;
    Intent intent_hidden;
    Intent intent_rfid2;
    Intent time;

    boolean sleep_f = false;
    int sleep_cnt = 0;
    int sleep_cnt_end = 1000;

    CustomSeekbar_onethumb custom_seekbar_horizontal;
    Typeface tf;

    public static DevicePolicyManager devicePolicyManager;
    ComponentName componentName;

    LinearLayout activity_setting;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Application_manager.setFullScreen(this);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        devicePolicyManager = (DevicePolicyManager) getApplicationContext().getSystemService(Context.DEVICE_POLICY_SERVICE);
        componentName = new ComponentName(getApplicationContext(), ShutdownAdminReceiver.class);

        if(!devicePolicyManager.isAdminActive(componentName)) {
            Intent intent = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
            intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, componentName);
            startActivityForResult(intent, 0);
        }

        // 폰트 설정
        tf = Typeface.createFromAsset(getAssets(), "fonts/digital.ttf");
        s_clock = (TextView) findViewById(R.id.textClock_s);
        s_clock.setTypeface(tf);
        s_clock.setText(Application_manager.doInit_time());

        flag = true;
        communicator = Application_manager.getCommunicator();
        activity_setting = (LinearLayout)findViewById(R.id.activity_main);

        b_11 = (TextView)findViewById(R.id.button11);
        b_21 = (TextView)findViewById(R.id.button21);
        b_31 = (TextView)findViewById(R.id.button31);
        b_41 = (TextView)findViewById(R.id.button41);
        b_12 = (TextView)findViewById(R.id.button12);
        b_22 = (TextView)findViewById(R.id.button22);
        b_32 = (TextView)findViewById(R.id.button32);
        b_42 = (TextView)findViewById(R.id.button42);
        b_13 = (TextView)findViewById(R.id.button13);
        b_23 = (TextView)findViewById(R.id.button23);
        b_33 = (TextView)findViewById(R.id.button33);
        b_43 = (TextView)findViewById(R.id.button43);

        sleep_f = false;
        sleep_cnt = 0;
        sleep_cnt_end = 1000;

        SharedPreferences sharedPreferences = getSharedPreferences(Application_manager.DB_NAME, 0);
        int resourceID;
        int rx_idx = -1;
        for (int i=1; i<=4; i++) { // 세로
            for (int j=1; j<=3; j++) { // 가로
                resourceID = getResources().getIdentifier("button" + i + "" + j, "id", "com.sinest.gw_1000");
                TextView resource = (TextView) findViewById(resourceID);
                // buttonij가 OFF 상태일 때
                if (sharedPreferences.getInt((Application_manager.DB_SETTING_ONOFF_VAL_ + i + "" + j), 0) == 0) {

                    button_flag[((j-1)*4 + i - 1)] = true;
                    resource.setBackgroundResource(Application_manager.button_off[Application_manager.img_flag]);
                    resource.setText("");
                }
                // buttonij가 ON 상태일 때
                else {
                    button_flag[((j-1)*4 + i - 1)] = false;
                    resource.setBackgroundResource(Application_manager.button_on[Application_manager.img_flag]);
                    if (j == 1) {
                        rx_idx = 10 + i;
                    }
                    else if (j == 2) {
                        rx_idx = 2 + i;
                    }
                    else if (j == 3) {
                        rx_idx = 6 + i;
                    }
                    resource.setText(""+communicator.get_rx_idx(rx_idx));
                    resource.setTypeface(tf);
                }
            }
        }

        b_rf = (Button)findViewById(R.id.b_rf);
        rfid_state = sharedPreferences.getBoolean(Application_manager.DB_RFID_ONOFF, false);
        button2_flag[0] = !rfid_state;
        if (rfid_state) {
            b_rf.setBackgroundResource(Application_manager.on[Application_manager.img_flag]);
        }
        else {
            b_rf.setBackgroundResource(Application_manager.off[Application_manager.img_flag]);
        }
        b_ex = (Button)findViewById(R.id.b_ex);
        b_wa = (Button)findViewById(R.id.b_wa);
        b_pa = (Button)findViewById(R.id.b_pa);
        b_1m = (Button)findViewById(R.id.b_1m);
        b_3m = (Button)findViewById(R.id.b_3m);
        b_5m = (Button)findViewById(R.id.b_5m);
        b_coutinue = (Button)findViewById(R.id.b_coutinue);

        custom_seekbar_horizontal = (CustomSeekbar_onethumb)findViewById(R.id.custom_seek_bar_horizontal);

        b_11 = (TextView) findViewById(R.id.button11);
        b_21 = (TextView) findViewById(R.id.button21);
        b_31 = (TextView) findViewById(R.id.button31);
        b_41 = (TextView) findViewById(R.id.button41);
        b_12 = (TextView) findViewById(R.id.button12);
        b_22 = (TextView) findViewById(R.id.button22);
        b_32 = (TextView) findViewById(R.id.button32);
        b_42 = (TextView) findViewById(R.id.button42);
        b_13 = (TextView) findViewById(R.id.button13);
        b_23 = (TextView) findViewById(R.id.button23);
        b_33 = (TextView) findViewById(R.id.button33);
        b_43 = (TextView) findViewById(R.id.button43);

        b_rf = (Button) findViewById(R.id.b_rf);
        b_ex = (Button) findViewById(R.id.b_ex);
        b_wa = (Button) findViewById(R.id.b_wa);
        b_pa = (Button) findViewById(R.id.b_pa);
        b_1m = (Button) findViewById(R.id.b_1m);
        b_3m = (Button) findViewById(R.id.b_3m);
        b_5m = (Button) findViewById(R.id.b_5m);
        b_coutinue = (Button) findViewById(R.id.b_coutinue);

        b_back = (Button) findViewById(R.id.b_back);
        b_emotion = (Button) findViewById(R.id.b_emotion);
        b_language = (Button) findViewById(R.id.b_language);
        b_inverter = (Button) findViewById(R.id.b_inverter);

        hidden_s_1 = (Button) findViewById(R.id.hidden_s_1);
        hidden_s_2 = (Button) findViewById(R.id.hidden_s_2);
        hidden_s_3 = (Button) findViewById(R.id.hidden_s_3);
        hidden_s_4 = (Button) findViewById(R.id.hidden_s_4);
        custom_seekbar_horizontal.setCurrentLoc(Application_manager.m_volume/10);

        intent_emotion = new Intent(this, Activity_emotion.class);
        intent_rfid = new Intent(this, Activity_rfidcardpassord.class);
        intent_wa = new Intent(this, Activity_water.class);
        intent_hidden = new Intent(this, Activity_engine.class);
        intent_rfid2 = new Intent(this, Activity_rfid.class);
        time = new Intent(this, Activity_time.class);

        /*
        * 토글식 버튼
        * flag가 true이면 on false이면 off
        */
        View.OnClickListener listener = new View.OnClickListener() {
            public void onClick(View v) {
                Application_manager.set_m_start_sleep(0);
                switch (v.getId()) {
                    case R.id.button11:
                        if (button_flag[0] == true) {
                            b_11.setBackgroundResource(Application_manager.button_on[Application_manager.img_flag]);
                            b_11.setText("" + communicator.get_rx_idx(11) + "%");
                            b_11.setTypeface(tf);
                            button_flag[0] = false;
                            Application_manager.wakeLock(getApplicationContext());
                        } else {
                            b_11.setBackgroundResource(Application_manager.button_off[Application_manager.img_flag]);
                            b_11.setText("");
                            button_flag[0] = true;
                            Application_manager.releaseWakeLock();
                        }
                        break;
                    case R.id.button21:
                        if (button_flag[1] == true) {
                            b_21.setBackgroundResource(Application_manager.button_on[Application_manager.img_flag]);
                            b_21.setText("" + communicator.get_rx_idx(12) + "%");
                            b_21.setTypeface(tf);
                            button_flag[1] = false;
                        } else {
                            b_21.setBackgroundResource(Application_manager.button_off[Application_manager.img_flag]);
                            b_21.setText("");
                            button_flag[1] = true;
                        }
                        break;
                    case R.id.button31:
                        if (button_flag[2] == true) {
                            b_31.setBackgroundResource(Application_manager.button_on[Application_manager.img_flag]);
                            b_31.setText("" + communicator.get_rx_idx(13) + "%");
                            b_31.setTypeface(tf);
                            button_flag[2] = false;
                        } else {
                            b_31.setBackgroundResource(Application_manager.button_off[Application_manager.img_flag]);
                            button_flag[2] = true;
                            b_31.setText("");
                        }
                        break;
                    case R.id.button41:
                        if (button_flag[3] == true) {
                            b_41.setBackgroundResource(Application_manager.button_on[Application_manager.img_flag]);
                            b_41.setText("" + communicator.get_rx_idx(14) + "%");
                            b_41.setTypeface(tf);
                            button_flag[3] = false;
                        } else {
                            b_41.setBackgroundResource(Application_manager.button_off[Application_manager.img_flag]);
                            button_flag[3] = true;
                            b_41.setText("");
                        }
                        break;
                    case R.id.button12:
                        if (button_flag[4] == true) {
                            b_12.setBackgroundResource(Application_manager.button_on[Application_manager.img_flag]);
                            b_12.setText("" + communicator.get_rx_idx(3) + "℃");
                            b_12.setTypeface(tf);
                            button_flag[4] = false;
                        } else {
                            b_12.setBackgroundResource(Application_manager.button_off[Application_manager.img_flag]);
                            button_flag[4] = true;
                            b_12.setText("");
                        }
                        break;
                    case R.id.button22:
                        if (button_flag[5] == true) {
                            b_22.setBackgroundResource(Application_manager.button_on[Application_manager.img_flag]);
                            b_22.setText("" + communicator.get_rx_idx(4) + "℃");
                            b_22.setTypeface(tf);
                            button_flag[5] = false;
                        } else {
                            b_22.setBackgroundResource(Application_manager.button_off[Application_manager.img_flag]);
                            button_flag[5] = true;
                            b_22.setText("");
                        }
                        break;
                    case R.id.button32:
                        if (button_flag[6] == true) {
                            b_32.setBackgroundResource(Application_manager.button_on[Application_manager.img_flag]);
                            b_32.setText("" + communicator.get_rx_idx(5) + "℃");
                            b_32.setTypeface(tf);
                            button_flag[6] = false;
                        } else {
                            b_32.setBackgroundResource(Application_manager.button_off[Application_manager.img_flag]);
                            button_flag[6] = true;
                            b_32.setText("");
                        }
                        break;
                    case R.id.button42:
                        if (button_flag[7] == true) {
                            b_42.setBackgroundResource(Application_manager.button_on[Application_manager.img_flag]);
                            b_42.setText("" + communicator.get_rx_idx(6) + "℃");
                            b_42.setTypeface(tf);
                            button_flag[7] = false;
                        } else {
                            b_42.setBackgroundResource(Application_manager.button_off[Application_manager.img_flag]);
                            button_flag[7] = true;
                            b_42.setText("");
                        }
                        break;
                    case R.id.button13:
                        if (button_flag[8] == true) {
                            b_13.setBackgroundResource(Application_manager.button_on[Application_manager.img_flag]);
                            b_13.setText("" + communicator.get_rx_idx(7) + "%");
                            b_13.setTypeface(tf);
                            button_flag[8] = false;
                        } else {
                            b_13.setBackgroundResource(Application_manager.button_off[Application_manager.img_flag]);
                            button_flag[8] = true;
                            b_13.setText("");
                        }
                        break;
                    case R.id.button23:
                        if (button_flag[9] == true) {
                            b_23.setBackgroundResource(Application_manager.button_on[Application_manager.img_flag]);
                            b_23.setText("" + communicator.get_rx_idx(8) + "%");
                            b_23.setTypeface(tf);
                            button_flag[9] = false;
                        } else {
                            b_23.setBackgroundResource(Application_manager.button_off[Application_manager.img_flag]);
                            button_flag[9] = true;
                            b_23.setText("");
                        }
                        break;
                    case R.id.button33:
                        if (button_flag[10] == true) {
                            b_33.setBackgroundResource(Application_manager.button_on[Application_manager.img_flag]);
                            b_33.setText("" + communicator.get_rx_idx(9) + "%");
                            b_33.setTypeface(tf);
                            button_flag[10] = false;
                        } else {
                            b_33.setBackgroundResource(Application_manager.button_off[Application_manager.img_flag]);
                            button_flag[10] = true;
                            b_33.setText("");
                        }
                        break;
                    case R.id.button43:
                        if (button_flag[11] == true) {
                            b_43.setBackgroundResource(Application_manager.button_on[Application_manager.img_flag]);
                            b_43.setText("" + communicator.get_rx_idx(10) + "%");
                            b_43.setTypeface(tf);
                            button_flag[11] = false;
                        } else {
                            b_43.setBackgroundResource(Application_manager.button_off[Application_manager.img_flag]);
                            button_flag[11] = true;
                            b_43.setText("");
                        }
                        break;
//----------------------------------------------------------------------------------------
                    case R.id.b_rf:
                        if (button2_flag[0] == true) {
                            b_rf.setBackgroundResource(Application_manager.on[Application_manager.img_flag]);
                            button2_flag[0] = false;
                            startActivity(intent_rfid);
                        } else {
                            b_rf.setBackgroundResource(Application_manager.off[Application_manager.img_flag]);
                            Application_manager.rfid_pass_f = true;
                            button2_flag[0] = true;
                        }
                        break;
                    case R.id.b_ex:
                        if (ex_f == 0) {
                            b_ex.setBackgroundResource(Application_manager.on[Application_manager.img_flag]);
                            ex_f = 1;
                            communicator.set_setting(2, (byte) 0x01);
                        } else if (ex_f == 1) {
                            b_ex.setBackgroundResource(Application_manager.off[Application_manager.img_flag]);
                            ex_f = 0;
                            communicator.set_setting(2, (byte) 0x00);
                        }
                        communicator.getSocketManager().send_setting();
                        break;
                    case R.id.b_wa:
                        if (button2_flag[2] == true) {
                            startActivity(intent_wa);
                        } else {
                            b_wa.setBackgroundResource(Application_manager.off[Application_manager.img_flag]);
                            Application_manager.set_m_water_f(false);
                            button2_flag[2] = true;
                        }
                        break;
                    case R.id.b_pa:
                        if (button2_flag[3] == true) {
                            b_pa.setBackgroundResource(Application_manager.on[Application_manager.img_flag]);
                            button2_flag[3] = false;
                            communicator.set_setting(4, (byte) 0x00);
                            Application_manager.set_m_pause_rotation(button2_flag[3]);
                        } else {
                            b_pa.setBackgroundResource(Application_manager.off[Application_manager.img_flag]);
                            button2_flag[3] = true;
                            communicator.set_setting(4, (byte) 0x01);
                        }
                        communicator.getSocketManager().send_setting();
                        break;
                    //----------------------------------------------------------------------------------------
                    case R.id.b_1m:
                        if (button3_flag[0] == true) {
                            setZerosSleep();
                            b_1m.setBackgroundResource(Application_manager.sleepmode_1min_on[Application_manager.img_flag]);
                            button3_flag[0] = false;
                            //----screen off----
                            sleep_f = true;
                            sleep_cnt = 0;
                            sleep_cnt_end = 60;
                            Application_manager.setSleep(sleep_cnt,sleep_cnt_end,sleep_f);
                            Application_manager.set_m_sleep_m(0);
                        } else {
                            b_1m.setBackgroundResource(Application_manager.sleepmode_1min[Application_manager.img_flag]);
                            sleep_f = false;
                            sleep_cnt = 0;
                            sleep_cnt_end = 0;
                            Application_manager.setSleep(sleep_cnt,sleep_cnt_end,sleep_f);
                            button3_flag[0] = true;
                        }
                        break;
                    case R.id.b_3m:
                        if (button3_flag[1] == true) {
                            setZerosSleep();
                            b_3m.setBackgroundResource(Application_manager.sleepmode_3min_on[Application_manager.img_flag]);
                            sleep_f = true;
                            sleep_cnt = 0;
                            sleep_cnt_end = 3*60;
                            Application_manager.setSleep(sleep_cnt,sleep_cnt_end,sleep_f);
                            Application_manager.set_m_sleep_m(1);
                            button3_flag[1] = false;
                        } else {
                            b_3m.setBackgroundResource(Application_manager.sleepmode_3min[Application_manager.img_flag]);
                            sleep_f = false;
                            sleep_cnt = 0;
                            sleep_cnt_end = 0;
                            Application_manager.setSleep(sleep_cnt,sleep_cnt_end,sleep_f);
                            button3_flag[1] = true;
                        }
                        break;
                    case R.id.b_5m:
                        if (button3_flag[2] == true) {
                            setZerosSleep();
                            b_5m.setBackgroundResource(Application_manager.sleepmode_5min_on[Application_manager.img_flag]);
                            sleep_f = true;
                            sleep_cnt = 0;
                            sleep_cnt_end = 5*60;
                            Application_manager.setSleep(sleep_cnt,sleep_cnt_end,sleep_f);
                            Application_manager.set_m_sleep_m(2);
                            button3_flag[2] = false;
                        } else {
                            b_5m.setBackgroundResource(Application_manager.sleepmode_5min[Application_manager.img_flag]);
                            sleep_f = false;
                            sleep_cnt = 0;
                            sleep_cnt_end = 0;
                            Application_manager.setSleep(sleep_cnt,sleep_cnt_end,sleep_f);
                            button3_flag[2] = true;
                        }
                        break;
                    case R.id.b_coutinue:
                        if (button3_flag[3] == true) {
                            setZerosSleep();
                            b_coutinue.setBackgroundResource(Application_manager.sleepmode_continue_on[Application_manager.img_flag]);
                            sleep_f = false;
                            sleep_cnt = 0;
                            sleep_cnt_end = 0;
                            Application_manager.setSleep(sleep_cnt,sleep_cnt_end,sleep_f);
                            Application_manager.set_m_sleep_m(3);
                            button3_flag[3] = false;
                        } else {
                            b_coutinue.setBackgroundResource(Application_manager.sleepmode_continue_off[Application_manager.img_flag]);
                            sleep_f = false;
                            sleep_cnt = 0;
                            sleep_cnt_end = 0;
                            Application_manager.setSleep(sleep_cnt,sleep_cnt_end,sleep_f);
                            button3_flag[3] = true;
                        }
                        break;
//------------------------------------------------------------------------------------
                    case R.id.b_language:
                        if (b_language_f == 0) {
                            b_language.setBackgroundResource(R.drawable.language_en);
                            b_language_f = 1;
                        } else if (b_language_f == 1) {
                            b_language.setBackgroundResource(Application_manager.language_ch[Application_manager.img_flag]);
                            b_language_f = 2;
                        } else if (b_language_f == 2) {
                            b_language.setBackgroundResource(R.drawable.language_ko);
                            b_language_f = 0;
                        }
                        Intent intent = getIntent();
                        finish();
                        startActivity(intent);
                        break;
                    case R.id.b_inverter:
                        if (b_inverter_f == 0) {
                            b_inverter.setBackgroundResource(Application_manager.inverter_50[Application_manager.img_flag]);
                            b_inverter_f = 1;
                        } else if (b_inverter_f == 1) {
                            b_inverter.setBackgroundResource(Application_manager.inverter_100[Application_manager.img_flag]);
                            b_inverter_f = 2;
                        } else if (b_inverter_f == 2) {
                            b_inverter.setBackgroundResource(Application_manager.inverter_0[Application_manager.img_flag]);
                            b_inverter_f = 0;
                        }
                        break;
                    case R.id.hidden_s_1:
                        if (hidden[3] == true) {
                            hidden[0] = false;
                            hidden[1] = false;
                            hidden[2] = false;
                            hidden[3] = false;
                            intent_hidden.putExtra("activity", "setting");
                            startActivityForResult(intent_hidden, 22);
                        } else {
                            hidden[0] = true;
                        }
                        break;
                    case R.id.hidden_s_2:
                        //
                        if (hidden[0] == true) {
                            hidden[1] = true;
                        }
                        break;
                    case R.id.hidden_s_3:
                        //
                        if (hidden[1] == true) {
                            hidden[2] = true;
                        }
                        break;
                    case R.id.hidden_s_4:
                        //
                        if (hidden[2] == true) {
                            hidden[3] = true;
                        }
                        break;
                }
            }
        };
        b_11.setOnClickListener(listener);
        b_21.setOnClickListener(listener);
        b_31.setOnClickListener(listener);
        b_41.setOnClickListener(listener);
        b_12.setOnClickListener(listener);
        b_22.setOnClickListener(listener);
        b_32.setOnClickListener(listener);
        b_42.setOnClickListener(listener);
        b_13.setOnClickListener(listener);
        b_23.setOnClickListener(listener);
        b_33.setOnClickListener(listener);
        b_43.setOnClickListener(listener);

        b_rf.setOnClickListener(listener);
        b_ex.setOnClickListener(listener);
        b_wa.setOnClickListener(listener);
        b_pa.setOnClickListener(listener);
        b_1m.setOnClickListener(listener);
        b_3m.setOnClickListener(listener);
        b_5m.setOnClickListener(listener);
        b_coutinue.setOnClickListener(listener);
        b_language.setOnClickListener(listener);
        b_inverter.setOnClickListener(listener);

        hidden_s_1.setOnClickListener(listener);
        hidden_s_2.setOnClickListener(listener);
        hidden_s_3.setOnClickListener(listener);
        hidden_s_4.setOnClickListener(listener);
        b_emotion.setOnTouchListener(mTouchEvent);
        b_back.setOnTouchListener(mTouchEvent);
        s_clock.setOnTouchListener(mTouchEvent);
    }

    // 슬립 버튼을 모두 off시키는 함수
    void setZerosSleep()
    {
        b_1m.setBackgroundResource(Application_manager.sleepmode_1min[Application_manager.img_flag]);
        button3_flag[0] = true;
        b_3m.setBackgroundResource(Application_manager.sleepmode_3min[Application_manager.img_flag]);
        button3_flag[1] = true;
        b_5m.setBackgroundResource(Application_manager.sleepmode_5min[Application_manager.img_flag]);
        button3_flag[2] = true;
        b_coutinue.setBackgroundResource(Application_manager.sleepmode_continue_off[Application_manager.img_flag]);
        button3_flag[3] = true;

    }

    @Override
    protected void onStop() {
        super.onStop();
        SharedPreferences sharedPreferences = getSharedPreferences(Application_manager.DB_NAME, 0);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        isRun = false;
        rfid_state = !button2_flag[0];
        editor.putBoolean(Application_manager.DB_RFID_ONOFF, rfid_state);

        for (int i=1; i<=4; i++) { // 세로
            for (int j = 1; j <= 3; j++) { // 가로
                // 버튼 플래그가 true(OFF)일 때
                if (button_flag[((j-1)*4 + i - 1)]) {
                    editor.putInt(Application_manager.DB_SETTING_ONOFF_VAL_ + i + "" + j, 0);
                }
                else {
                    editor.putInt(Application_manager.DB_SETTING_ONOFF_VAL_ + i + "" + j, 1);
                }
            }
        }
        editor.commit();
    }

    // 슬립모드 지난 시간 초기화
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Application_manager.set_m_start_sleep(0);
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

    // 눌렀다 뗏을때 이벤트 발생
    // 시간설정은 눌렀을시 이벤트 발생
    private View.OnTouchListener mTouchEvent = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            Application_manager.set_m_start_sleep(0);
            int action = motionEvent.getAction();
            int id = view.getId();
            if (action == MotionEvent.ACTION_DOWN) {
                switch (id) {
                    case R.id.b_emotion:
                        b_emotion.setBackgroundResource(Application_manager.emotion_on[Application_manager.img_flag]);
                        break;
                    case R.id.b_back:
                        b_back.setBackgroundResource(Application_manager.button_circle_back_on[Application_manager.img_flag]);
                        break;
                    case R.id.textClock_s:
                        startActivity(time);
                }
            } else if (action == MotionEvent.ACTION_UP) {
                byte val = 0x00;
                switch (id) {
                    case R.id.b_emotion:
                        b_emotion.setBackgroundResource(Application_manager.emotion_off[Application_manager.img_flag]);
                        startActivity(intent_emotion);
                        break;
                    case R.id.b_back:
                        b_back.setBackgroundResource(Application_manager.button_circle_back_off[Application_manager.img_flag]);
                        isRun = false;
                        // Modified by Jinwook
                        Intent intent = null;
                        rfid_state = !button2_flag[0];
                        if (rfid_state) {
                            intent = new Intent(getApplicationContext(), Activity_waiting_rfid.class);
                        }
                        else {
                            intent = new Intent(getApplicationContext(), Activity_waiting.class);
                        }
                        startActivity(intent);
                        finish();
                        break;
                }
            }
            return true;
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        Application_manager.setFullScreen(this);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        isRun = true;
        //image change
        button_init();

        button2_flag[0] = Application_manager.rfid_pass_f;
        if(button2_flag[0] == false && Application_manager.rfid_on_f == 1){
            b_rf.setBackgroundResource(Application_manager.on[Application_manager.img_flag]);
        }else{
            b_rf.setBackgroundResource(Application_manager.off[Application_manager.img_flag]);
            if(Application_manager.rfid_pass_f2 == true)
            {
                startActivity(intent_rfid2);
            }
        }

        // 슬립 모드 동작 재시작
        Application_manager.setSleep_f(0,true);

        Thread myThread = new Thread(new Runnable() {
            public void run() {
                while (isRun) {
                    try {
                        handler.sendMessage(handler.obtainMessage());
                        Thread.sleep(1000);
                        Log.v("SB_test",""+Application_manager.water_time_flag);
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
        isRun = false;

        Application_manager.m_water_heater_time_save = button2_flag[2];
        Application_manager.set_m_water_ff(button2_flag[2]);

        Application_manager.m_external_led = ex_f;
        Application_manager.set_m_external_led(ex_f);

        Application_manager.m_pause_rotation = button2_flag[3];
        Application_manager.set_m_pause_rotation(button2_flag[3]);

        Application_manager.m_language = b_language_f;
        Application_manager.set_m_language(b_language_f);

        Application_manager.m_inverter = b_inverter_f;
        Application_manager.set_m_inverter(b_inverter_f);

        //----------------------------------------언어 변경시 버튼 상태 저장 -----------------

        SharedPreferences sharedPreferences = getSharedPreferences(Application_manager.DB_NAME, 0);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        rfid_state = !button2_flag[0];
        editor.putBoolean(Application_manager.DB_RFID_ONOFF, rfid_state);

        for (int i=1; i<=4; i++) { // 세로
            for (int j = 1; j <= 3; j++) { // 가로
                // 버튼 플래그가 true(OFF)일 때
                if (button_flag[((j-1)*4 + i - 1)]) {
                    editor.putInt(Application_manager.DB_SETTING_ONOFF_VAL_ + i + "" + j, 0);
                }
                else {
                    editor.putInt(Application_manager.DB_SETTING_ONOFF_VAL_ + i + "" + j, 1);
                }
            }
        }
        editor.commit();
    }

    // 언어에 따른 버튼 이미지 변경
    private void button_init(){
        int sleep_ff = Application_manager.m_sleep_ff;
        if (sleep_ff == 0) {
            setZerosSleep();
            b_1m.setBackgroundResource(Application_manager.sleepmode_1min_on[Application_manager.img_flag]);
        }else if (sleep_ff == 1) {
            setZerosSleep();
            b_3m.setBackgroundResource(Application_manager.sleepmode_3min_on[Application_manager.img_flag]);
        }else if (sleep_ff == 2) {
            setZerosSleep();
            b_5m.setBackgroundResource(Application_manager.sleepmode_5min_on[Application_manager.img_flag]);
        }else if (sleep_ff == 3) {
            setZerosSleep();
            b_coutinue.setBackgroundResource(Application_manager.sleepmode_continue_on[Application_manager.img_flag]);
        }

        ex_f = Application_manager.m_external_led;
        if (ex_f == 0) {
            b_ex.setBackgroundResource(Application_manager.off[Application_manager.img_flag]);
            //communicator.set_engineer(6,(byte)0x00);
        } else if (ex_f == 1) {
            b_ex.setBackgroundResource(Application_manager.on[Application_manager.img_flag]);
            //communicator.set_engineer(6,(byte)0x01);
        }

        button2_flag[2] = Application_manager.m_water_heater_f;
        if (button2_flag[2] == true) {
            b_wa.setBackgroundResource(Application_manager.off[Application_manager.img_flag]);
        } else {
            b_wa.setBackgroundResource(Application_manager.on[Application_manager.img_flag]);
        }

        button2_flag[3] = Application_manager.m_pause_rotation;
        if (button2_flag[3] == true) {
            b_pa.setBackgroundResource(Application_manager.off[Application_manager.img_flag]);
            communicator.set_setting(4, (byte) 0x00);
        } else {
            b_pa.setBackgroundResource(Application_manager.on[Application_manager.img_flag]);
            communicator.set_setting(4, (byte) 0x01);
        }

        b_language_f = Application_manager.m_language;
        if (b_language_f == 0) {
            b_language.setBackgroundResource(R.drawable.language_ko);
        } else if (b_language_f == 1) {
            b_language.setBackgroundResource(R.drawable.language_en);
        } else if (b_language_f == 2) {
            b_language.setBackgroundResource(Application_manager.language_ch[Application_manager.img_flag]);
        }

        b_inverter_f = Application_manager.m_inverter;
        if (b_inverter_f == 0) {
            b_inverter.setBackgroundResource(Application_manager.inverter_0[Application_manager.img_flag]);
        } else if (b_inverter_f == 1) {
            b_inverter.setBackgroundResource(Application_manager.inverter_50[Application_manager.img_flag]);
        } else if (b_inverter_f == 2) {
            b_inverter.setBackgroundResource(Application_manager.inverter_100[Application_manager.img_flag]);
        }

        activity_setting.setBackgroundResource(Application_manager.setting_back_image[Application_manager.img_flag]);
        b_emotion.setBackgroundResource(Application_manager.emotion_off[Application_manager.img_flag]);
        b_back.setBackgroundResource(Application_manager.button_circle_back_off[Application_manager.img_flag]);

        if(Application_manager.gw_1000 == false)
        {
            b_11.setVisibility(View.INVISIBLE);b_12.setVisibility(View.INVISIBLE);b_13.setVisibility(View.INVISIBLE);
            b_21.setVisibility(View.INVISIBLE);b_22.setVisibility(View.INVISIBLE);b_23.setVisibility(View.INVISIBLE);
            b_31.setVisibility(View.INVISIBLE);b_32.setVisibility(View.INVISIBLE);b_33.setVisibility(View.INVISIBLE);
            b_41.setVisibility(View.INVISIBLE);b_42.setVisibility(View.INVISIBLE);b_43.setVisibility(View.INVISIBLE);
        }else{
            b_11.setVisibility(View.VISIBLE);b_12.setVisibility(View.VISIBLE);b_13.setVisibility(View.VISIBLE);
            b_21.setVisibility(View.VISIBLE);b_22.setVisibility(View.VISIBLE);b_23.setVisibility(View.VISIBLE);
            b_31.setVisibility(View.VISIBLE);b_32.setVisibility(View.VISIBLE);b_33.setVisibility(View.VISIBLE);
            b_41.setVisibility(View.VISIBLE);b_42.setVisibility(View.VISIBLE);b_43.setVisibility(View.VISIBLE);
            init_nume();
        }
    }

    // 기호 표시
    void init_nume(){
        if (button_flag[0] == false) {
            b_11.setText("" + communicator.get_rx_idx(11) + "%");
            b_11.setTypeface(tf);
        }
        if (button_flag[1] == false) {
            b_21.setText("" + communicator.get_rx_idx(12) + "%");
            b_21.setTypeface(tf);
        }
        if (button_flag[2] == false) {
            b_31.setText("" + communicator.get_rx_idx(13) + "%");
            b_31.setTypeface(tf);
        }
        if (button_flag[3] == false) {
            b_41.setText("" + communicator.get_rx_idx(14) + "%");
            b_41.setTypeface(tf);
        }
        if (button_flag[4] == false) {
            b_12.setText("" + communicator.get_rx_idx(3) + "℃");
            b_12.setTypeface(tf);
        }
        if (button_flag[5] == false) {
            b_22.setText("" + communicator.get_rx_idx(4) + "℃");
            b_22.setTypeface(tf);
        }
        if (button_flag[6] == false) {
            b_32.setText("" + communicator.get_rx_idx(5) + "℃");
            b_32.setTypeface(tf);
        }
        if (button_flag[7] == false) {
            b_42.setText("" + communicator.get_rx_idx(6) + "℃");
            b_42.setTypeface(tf);
        }
        if (button_flag[8] == false) {
            b_13.setText("" + communicator.get_rx_idx(7) + "%");
            b_13.setTypeface(tf);
        }
        if (button_flag[9] == false) {
            b_23.setText("" + communicator.get_rx_idx(8) + "%");
            b_23.setTypeface(tf);
        }
        if (button_flag[10] == false) {
            b_33.setText("" + communicator.get_rx_idx(9) + "%");
            b_33.setTypeface(tf);
        }
        if (button_flag[11] == false) {
            b_43.setText("" + communicator.get_rx_idx(10) + "%");
            b_43.setTypeface(tf);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            updateThread();
        }
    };
    private void updateThread() {
        s_clock.setText(Application_manager.doInit_time());
    }

}