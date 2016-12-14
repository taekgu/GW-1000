package com.sinest.gw_1000.setting;

import android.app.admin.DevicePolicyManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.os.PowerManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextClock;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;
import com.sinest.gw_1000.R;
import com.sinest.gw_1000.communication.Communicator;
import com.sinest.gw_1000.management.Application_manager;
import com.sinest.gw_1000.mode.utils.CustomTextClock;

public class Activity_setting extends AppCompatActivity {
    public static final String TAG = "SCREEN";

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

    CustomTextClock clock;

    boolean[] button_flag = new boolean[12];
    boolean[] button2_flag = {true, true, true, true};
    boolean[] button3_flag = {true, true, true, true};

    boolean[] hidden = {false, false, false, false};

    private boolean isRun = false;

    int ex_f = 0;

    boolean b_back_f = true;
    boolean b_emotion_f = true;
    int b_language_f = 0;
    int b_inverter_f = 0;

    Intent intent_emotion;
    Intent intent_rfid;
    Intent intent_wa;
    Intent intent_hidden;
    Intent intent_rfid2;
    Intent time;

    String check;
    String check2;

    int led_mode_num = 0;
    int sound_mode_num = 0;
    int led_bright_num = 1;
    int sound_volume_num = 1;

    String t_buf;

    SeekBar seekbar;
    int volume;
    Typeface tf;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Application_manager.setFullScreen(this);

        // 폰트 설정
        tf = Typeface.createFromAsset(getAssets(), "fonts/digital.ttf");
        clock = (CustomTextClock) findViewById(R.id.textClock_s);

        communicator = Application_manager.getCommunicator();

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

        SharedPreferences sharedPreferences = getSharedPreferences(Application_manager.NAME_OF_SHARED_PREF, 0);
        int resourceID;
        int rx_idx = -1;
        Log.i("JW", "onCreate");
        for (int i=1; i<=4; i++) { // 세로

            for (int j=1; j<=3; j++) { // 가로

                resourceID = getResources().getIdentifier("button" + i + "" + j, "id", "com.sinest.gw_1000");
                TextView resource = (TextView) findViewById(resourceID);

                // buttonij가 OFF 상태일 때
                if (sharedPreferences.getInt((Application_manager.SETTING_ONOFF_VAL_ + i + "" + j), 0) == 0) {

                    button_flag[((j-1)*4 + i - 1)] = true;
                    resource.setBackgroundResource(R.drawable.button_off);
                    resource.setText("");
                //    Log.i("JW", "button_flag[" + ((j-1)*4 + i - 1) + "] / button" + i + "" + j + " = OFF");
                }
                // buttonij가 ON 상태일 때
                else {

                    button_flag[((j-1)*4 + i - 1)] = false;
                    resource.setBackgroundResource(R.drawable.button_on);
                //    Log.i("JW", "button_flag[" + ((j-1)*4 + i - 1) + "] / button" + i + "" + j + " = ON");
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
        rfid_state = sharedPreferences.getBoolean(Application_manager.RFID_ONOFF, false);
        button2_flag[0] = !rfid_state;
        if (rfid_state) {

            b_rf.setBackgroundResource(R.drawable.on);
        }
        else {

            b_rf.setBackgroundResource(R.drawable.off);
        }
        b_ex = (Button)findViewById(R.id.b_ex);
        b_wa = (Button)findViewById(R.id.b_wa);
        b_pa = (Button)findViewById(R.id.b_pa);
        b_1m = (Button)findViewById(R.id.b_1m);
        b_3m = (Button)findViewById(R.id.b_3m);
        b_5m = (Button)findViewById(R.id.b_5m);
        b_coutinue = (Button)findViewById(R.id.b_coutinue);

        b_back = (Button)findViewById(R.id.b_back);
        b_emotion = (Button)findViewById(R.id.b_emotion);
        b_language = (Button)findViewById(R.id.b_language);
        b_inverter = (Button)findViewById(R.id.b_inverter);

        hidden_s_1 = (Button)findViewById(R.id.hidden_s_1);
        hidden_s_2 = (Button)findViewById(R.id.hidden_s_2);
        hidden_s_3 = (Button)findViewById(R.id.hidden_s_3);
        hidden_s_4 = (Button)findViewById(R.id.hidden_s_4);

        seekbar = (SeekBar)findViewById(R.id.seekBar);
/*
        scrOnReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Log.v("test", "on");
                clock.setText(""+ Application_manager.getTime());
            }
        };

        scrOnFilter = new IntentFilter(Intent.ACTION_SCREEN_ON);

        scrOffReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Log.v("test", "off");
            }
        };
        scrOffFilter = new IntentFilter(Intent.ACTION_SCREEN_OFF);

        registerReceiver(scrOnReceiver, scrOnFilter);
        registerReceiver(scrOffReceiver, scrOffFilter);
*/
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

        seekbar = (SeekBar) findViewById(R.id.seekBar);
        seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // 메소드 이름대로 사용자가 SeekBar를 터치했을때 실행됩니다
                // TODO Auto-generated method stub
                Log.v("test1", "v : " + volume);
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // 메소드 이름대로 사용자가 SeekBar를 손에서 땠을때 실행됩니다
                // TODO Auto-generated method stub
                Log.v("test2", "v : " + volume);
            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                // 메소드 이름대로 사용자가 SeekBar를 움직일때 실행됩니다
                // 주로 사용되는 메소드 입니다
                // TODO Auto-generated method stub
                volume = progress;
            }
        });

        //Application_manager.getRunningTime();

        intent_emotion = new Intent(this, Activity_emotion.class);
        //intent_emotion.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);

        intent_rfid = new Intent(this, Activity_rfidcardpassord.class);
        //intent_rfid.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);

        intent_wa = new Intent(this, Activity_water.class);
        //intent_wa.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);

        intent_hidden = new Intent(this, Activity_engine.class);

        intent_rfid2 = new Intent(this, Activity_rfid.class);
        //intent_rfid2.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);

        time = new Intent(this, Activity_time.class);

        //lp.screenBrightness = 0;

        View.OnClickListener listener = new View.OnClickListener() {
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.button11:
                        //
                        if (button_flag[0] == true) {
                            b_11.setBackgroundResource(R.drawable.button_on);
                            b_11.setText("" + communicator.get_rx_idx(11));
                            b_11.setTypeface(tf);
                            button_flag[0] = false;
                        } else {
                            b_11.setBackgroundResource(R.drawable.button_off);
                            b_11.setText("");
                            button_flag[0] = true;
                        }
                        break;
                    case R.id.button21:
                        //
                        if (button_flag[1] == true) {
                            b_21.setBackgroundResource(R.drawable.button_on);
                            b_21.setText("" + communicator.get_rx_idx(12));
                            b_21.setTypeface(tf);
                            button_flag[1] = false;
                        } else {
                            b_21.setBackgroundResource(R.drawable.button_off);
                            b_21.setText("");
                            button_flag[1] = true;
                        }
                        break;
                    case R.id.button31:
                        //
                        if (button_flag[2] == true) {
                            b_31.setBackgroundResource(R.drawable.button_on);
                            b_31.setText("" + communicator.get_rx_idx(13));
                            b_31.setTypeface(tf);
                            button_flag[2] = false;
                        } else {
                            b_31.setBackgroundResource(R.drawable.button_off);
                            button_flag[2] = true;
                            b_31.setText("");
                        }
                        break;
                    case R.id.button41:
                        //
                        if (button_flag[3] == true) {
                            b_41.setBackgroundResource(R.drawable.button_on);
                            b_41.setText("" + communicator.get_rx_idx(14));
                            b_41.setTypeface(tf);
                            button_flag[3] = false;
                        } else {
                            b_41.setBackgroundResource(R.drawable.button_off);
                            button_flag[3] = true;
                            b_41.setText("");
                        }
                        break;
                    case R.id.button12:
                        //
                        if (button_flag[4] == true) {
                            b_12.setBackgroundResource(R.drawable.button_on);
                            b_12.setText("" + communicator.get_rx_idx(3));
                            b_12.setTypeface(tf);
                            button_flag[4] = false;
                        } else {
                            b_12.setBackgroundResource(R.drawable.button_off);
                            button_flag[4] = true;
                            b_12.setText("");
                        }
                        break;
                    case R.id.button22:
                        //
                        if (button_flag[5] == true) {
                            b_22.setBackgroundResource(R.drawable.button_on);
                            b_22.setText("" + communicator.get_rx_idx(4));
                            b_22.setTypeface(tf);
                            button_flag[5] = false;
                        } else {
                            b_22.setBackgroundResource(R.drawable.button_off);
                            button_flag[5] = true;
                            b_22.setText("");
                        }
                        break;
                    case R.id.button32:
                        //
                        if (button_flag[6] == true) {
                            b_32.setBackgroundResource(R.drawable.button_on);
                            b_32.setText("" + communicator.get_rx_idx(5));
                            b_32.setTypeface(tf);
                            button_flag[6] = false;
                        } else {
                            b_32.setBackgroundResource(R.drawable.button_off);
                            button_flag[6] = true;
                            b_32.setText("");
                        }
                        break;
                    case R.id.button42:
                        //
                        if (button_flag[7] == true) {
                            b_42.setBackgroundResource(R.drawable.button_on);
                            b_42.setText("" + communicator.get_rx_idx(6));
                            b_42.setTypeface(tf);
                            button_flag[7] = false;
                        } else {
                            b_42.setBackgroundResource(R.drawable.button_off);
                            button_flag[7] = true;
                            b_42.setText("");
                        }
                        break;
                    case R.id.button13:
                        //
                        if (button_flag[8] == true) {
                            b_13.setBackgroundResource(R.drawable.button_on);
                            b_13.setText("" + communicator.get_rx_idx(7));
                            b_13.setTypeface(tf);
                            button_flag[8] = false;
                        } else {
                            b_13.setBackgroundResource(R.drawable.button_off);
                            button_flag[8] = true;
                            b_13.setText("");
                        }
                        break;
                    case R.id.button23:
                        //
                        if (button_flag[9] == true) {
                            b_23.setBackgroundResource(R.drawable.button_on);
                            b_23.setText("" + communicator.get_rx_idx(8));
                            b_23.setTypeface(tf);
                            button_flag[9] = false;
                        } else {
                            b_23.setBackgroundResource(R.drawable.button_off);
                            button_flag[9] = true;
                            b_23.setText("");
                        }
                        break;
                    case R.id.button33:
                        //
                        if (button_flag[10] == true) {
                            b_33.setBackgroundResource(R.drawable.button_on);
                            b_33.setText("" + communicator.get_rx_idx(9));
                            b_33.setTypeface(tf);
                            button_flag[10] = false;
                        } else {
                            b_33.setBackgroundResource(R.drawable.button_off);
                            button_flag[10] = true;
                            b_33.setText("");
                        }
                        break;
                    case R.id.button43:
                        //
                        if (button_flag[11] == true) {
                            b_43.setBackgroundResource(R.drawable.button_on);
                            b_43.setText("" + communicator.get_rx_idx(10));
                            b_43.setTypeface(tf);
                            button_flag[11] = false;
                        } else {
                            b_43.setBackgroundResource(R.drawable.button_off);
                            button_flag[11] = true;
                            b_43.setText("");
                        }
                        break;
//----------------------------------------------------------------------------------------
                    case R.id.b_rf:
                        //
                        if (button2_flag[0] == true) {
                            b_rf.setBackgroundResource(R.drawable.on);
                            button2_flag[0] = false;
                            intent_rfid.putExtra("check", "ok");
                            startActivityForResult(intent_rfid, 1);
                            //startActivity(intent_rfid);
                        } else {
                            b_rf.setBackgroundResource(R.drawable.off);
                            button2_flag[0] = true;
                        }
                        break;
                    case R.id.b_ex:
                        //
                        if (ex_f == 0) {
                            b_ex.setBackgroundResource(R.drawable.on);
                            ex_f = 1;
                            communicator.set_setting(2, (byte) 0x01);
                        } else if (ex_f == 1) {
                            b_ex.setBackgroundResource(R.drawable.button_play_on);
                            ex_f = 2;
                            communicator.set_setting(2, (byte) 0x02);
                        } else if (ex_f == 2) {
                            b_ex.setBackgroundResource(R.drawable.off);
                            ex_f = 0;
                            communicator.set_setting(2, (byte) 0x00);
                        }
                        break;
                    case R.id.b_wa:
                        //
                        if (button2_flag[2] == true) {
                            b_wa.setBackgroundResource(R.drawable.on);
                            button2_flag[2] = false;
                            intent_wa.putExtra("check", "ok");
                            startActivityForResult(intent_wa, 2);
                            //startActivity(intent_wa);
                        } else {
                            b_wa.setBackgroundResource(R.drawable.off);
                            button2_flag[2] = true;
                        }
                        break;
                    case R.id.b_pa:
                        //
                        if (button2_flag[3] == true) {
                            b_pa.setBackgroundResource(R.drawable.on);
                            button2_flag[3] = false;
                            communicator.set_setting(4, (byte) 0x01);
                        } else {
                            b_pa.setBackgroundResource(R.drawable.off);
                            button2_flag[3] = true;
                            communicator.set_setting(4, (byte) 0x00);
                        }
                        break;

                    //----------------------------------------------------------------------------------------
                    case R.id.b_1m:
                        //
                        if (button3_flag[0] == true) {
                            b_1m.setBackgroundResource(R.drawable.sleepmode_1min_on);
                            button3_flag[0] = false;

                            //devicePolicyManager.lockNow();

                            Log.v("test", "test");
                        } else {
                            b_1m.setBackgroundResource(R.drawable.sleepmode_1min);
                            button3_flag[0] = true;
                        }
                        break;
                    case R.id.b_3m:
                        //
                        if (button3_flag[1] == true) {
                            b_3m.setBackgroundResource(R.drawable.sleepmode_3min_on);
                            button3_flag[1] = false;
                        } else {
                            b_3m.setBackgroundResource(R.drawable.sleepmode_3min);
                            button3_flag[1] = true;
                        }
                        break;
                    case R.id.b_5m:
                        //
                        if (button3_flag[2] == true) {
                            b_5m.setBackgroundResource(R.drawable.sleepmode_5min_on);
                            button3_flag[2] = false;
                        } else {
                            b_5m.setBackgroundResource(R.drawable.sleepmode_5min);
                            button3_flag[2] = true;
                        }
                        break;
                    case R.id.b_coutinue:
                        //
                        if (button3_flag[3] == true) {
                            b_coutinue.setBackgroundResource(R.drawable.sleepmode_continue_on);
                            button3_flag[3] = false;
                        } else {
                            b_coutinue.setBackgroundResource(R.drawable.sleepmode_continue_off);
                            button3_flag[3] = true;
                        }
                        break;
//------------------------------------------------------------------------------------
                    case R.id.b_language:
                        //
                        if (b_language_f == 0) {
                            b_language.setBackgroundResource(R.drawable.language_en);
                            b_language_f = 1;
                        } else if (b_language_f == 1) {
                            b_language.setBackgroundResource(R.drawable.language_ch);
                            b_language_f = 2;
                        } else if (b_language_f == 2) {
                            b_language.setBackgroundResource(R.drawable.language_ko);
                            b_language_f = 0;
                        }
                        break;
                    case R.id.b_inverter:
                        //
                        if (b_inverter_f == 0) {
                            b_inverter.setBackgroundResource(R.drawable.inverter_50);
                            b_inverter_f = 1;
                        } else if (b_inverter_f == 1) {
                            b_inverter.setBackgroundResource(R.drawable.inverter_100);
                            b_inverter_f = 2;
                        } else if (b_inverter_f == 2) {
                            b_inverter.setBackgroundResource(R.drawable.inverter_0);
                            b_inverter_f = 0;
                        }
                        break;

                    case R.id.hidden_s_1:
                        //
                        if (hidden[3] == true) {
                            hidden[0] = false;
                            hidden[1] = false;
                            hidden[2] = false;
                            hidden[3] = false;
                            intent_hidden.putExtra("activity", "setting");
                            startActivityForResult(intent_hidden, 22);
                        } else {
                            hidden[0] = true;
                            Log.v("hidden", "hidden1");
                        }
                        break;
                    case R.id.hidden_s_2:
                        //
                        if (hidden[0] == true) {
                            hidden[1] = true;
                            Log.v("hidden", "hidden2");
                        }
                        break;
                    case R.id.hidden_s_3:
                        //
                        if (hidden[1] == true) {
                            hidden[2] = true;
                            Log.v("hidden", "hidden3");
                        }
                        break;
                    case R.id.hidden_s_4:
                        //
                        if (hidden[2] == true) {
                            hidden[3] = true;
                            Log.v("hidden", "hidden4");
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
        //b_back.setOnClickListener(listener);
        //b_emotion.setOnClickListener(listener);
        b_language.setOnClickListener(listener);
        b_inverter.setOnClickListener(listener);

        hidden_s_1.setOnClickListener(listener);
        hidden_s_2.setOnClickListener(listener);
        hidden_s_3.setOnClickListener(listener);
        hidden_s_4.setOnClickListener(listener);

        b_emotion.setOnTouchListener(mTouchEvent);
        b_back.setOnTouchListener(mTouchEvent);
        clock.setOnTouchListener(mTouchEvent);
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
    }

    @Override
    protected void onStop() {
        super.onStop();
        SharedPreferences sharedPreferences = getSharedPreferences(Application_manager.NAME_OF_SHARED_PREF, 0);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        isRun = false;

        rfid_state = !button2_flag[0];
        editor.putBoolean(Application_manager.RFID_ONOFF, rfid_state);
        //editor.putBoolean(Application_manager.RFID_ONOFF, false);
        Log.i("JW", "RFID mode = " + rfid_state);

        for (int i=1; i<=4; i++) { // 세로

            for (int j = 1; j <= 3; j++) { // 가로

                // 버튼 플래그가 true(OFF)일 때
                if (button_flag[((j-1)*4 + i - 1)]) {

                    editor.putInt(Application_manager.SETTING_ONOFF_VAL_ + i + "" + j, 0);
                //    Log.i("JW", "button_flag[" + ((j-1)*4 + i - 1) + "] / button" + i + "" + j + " = OFF");
                }
                else {

                    editor.putInt(Application_manager.SETTING_ONOFF_VAL_ + i + "" + j, 1);
                 //   Log.i("JW", "button_flag[" + ((j-1)*4 + i - 1) + "] / button" + i + "" + j + " = ON");
                }
            }
        }

        editor.commit();
    }

    private View.OnTouchListener mTouchEvent = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            int action = motionEvent.getAction();
            int id = view.getId();
            if (action == MotionEvent.ACTION_DOWN) {
                switch (id) {
                    case R.id.b_emotion:
                        b_emotion.setBackgroundResource(R.drawable.emotion_on);

                        break;
                    case R.id.b_back:
                        b_back.setBackgroundResource(R.drawable.button_circle_back_on);

                        break;
                    case R.id.textClock_s:
                        Application_manager.setTime_gap_n((String)clock.getText());
                        startActivity(time);
                }
            } else if (action == MotionEvent.ACTION_UP) {
                byte val = 0x00;
                switch (id) {
                    case R.id.b_emotion:
                        b_emotion.setBackgroundResource(R.drawable.emotion_off);
                        //startActivity(intent_emotion);
                        intent_emotion.putExtra("LED_M", led_mode_num);
                        intent_emotion.putExtra("LED", led_bright_num);
                        intent_emotion.putExtra("SOUND_M", sound_mode_num);
                        intent_emotion.putExtra("SOUND", sound_volume_num);

                        startActivityForResult(intent_emotion, 4);
                        break;
                    case R.id.b_back:
                        b_back.setBackgroundResource(R.drawable.button_circle_back_off);
                        isRun = false;
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

        clock.registReceiver();
        clock.doInit_time();
    }

    @Override
    protected void onPause() {
        super.onPause();
        clock.unregistReceiver();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case 1:
                    check = data.getStringExtra("check");
                    Log.v("test", check);
                    if (check.equals("No")) {
                        b_rf.setBackgroundResource(R.drawable.off);
                        button2_flag[0] = true;
                        check = "Yes";
                    } else if (check.equals("do")) {
                        startActivityForResult(intent_rfid2, 3);
                    }
                    break;
                case 2:
                    check2 = data.getStringExtra("check");
                    if (check2.equals("No")) {
                        b_wa.setBackgroundResource(R.drawable.off);
                        button2_flag[2] = true;
                        check2 = "Yes";
                    }
                    break;
                case 3:
                    check = data.getStringExtra("check");
                    if (check.equals("No")) {
                        b_rf.setBackgroundResource(R.drawable.off);
                        button2_flag[0] = true;
                        check = "Yes";
                        Log.i("JW", "RFID OFF");
                        Toast.makeText(this, "RFID OFF", Toast.LENGTH_SHORT).show();
                    }
                    else {

                        Log.i("JW", "RFID ON");
                        Toast.makeText(this, "RFID ON", Toast.LENGTH_SHORT).show();
                    }
                    break;
                case 4:
                    check = data.getStringExtra("emotion");
                    if (check.equals("OK")) {
                        led_mode_num = (int) data.getSerializableExtra("LED_M");
                        led_bright_num = (int) data.getSerializableExtra("LED");
                        sound_mode_num = (int) data.getSerializableExtra("SOUND_M");
                        sound_volume_num = (int) data.getSerializableExtra("SOUND");
                        Log.v("test", "" + led_mode_num);
                    }
                    break;
                default:
                    break;
            }
        }
    }
}