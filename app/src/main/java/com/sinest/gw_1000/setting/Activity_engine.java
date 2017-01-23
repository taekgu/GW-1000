package com.sinest.gw_1000.setting;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextClock;
import android.widget.TextView;

import com.sinest.gw_1000.R;
import com.sinest.gw_1000.communication.Communicator;
import com.sinest.gw_1000.management.Application_manager;
import com.sinest.gw_1000.mode.Activity_waiting;
import com.sinest.gw_1000.mode.utils.CustomTextClock;

public class Activity_engine extends AppCompatActivity {

    Button eng_28h; Button eng_36h; Button eng_43h; Button eng_48h;
    Button eng_54h; Button eng_60h; Button eng_1step; Button eng_2step;
    Button eng_3step; Button eng_4step; Button eng_5step;

    Button eng_b_water; Button eng_b_inter; Button eng_b_sol; Button eng_b_ven;
    Button eng_door_open; Button eng_door_close;
    Button eng_b_left; Button eng_b_right; Button eng_b_back; Button eng_r_left; Button eng_r_right;

    Button program_m; Button invert_choice;

    Button hidden_e_1; Button hidden_e_2; Button hidden_e_3; Button hidden_e_4;

    TextView oxygen_m; TextView operation_t;
    Intent check;
    Intent main_intent;
    String check_activity;

    boolean[] eng_h_flag = {true,true,true,true,true,true};
    boolean[] eng_step_flag = {true,true,true,true,true};

    boolean[] eng_b_flag = {true,true,true,true,true,true};
    boolean[] eng_flag = {true,true,true,true,true};

    boolean[] hidden = {false,false,false,false};

    boolean mode_f = true; boolean invert_f = true;

    int heater_f = 0;
    int w_press = 0;
    int oxy = 0;
    Byte inverter = 0x00;

    Communicator communicator;
    TextView clock;

    private boolean isRun = false;

    LinearLayout activity_engine;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_engine);
        Application_manager.setFullScreen(this);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        // 폰트 설정
        Typeface tf = Typeface.createFromAsset(getAssets(), "fonts/digital.ttf");
        clock = (TextView) findViewById(R.id.textClock_e);
        clock.setTypeface(tf);

        check = getIntent();
        check_activity = check.getStringExtra("activity");
        if(check_activity == null)
        {
            check_activity = "main";
        }

        activity_engine = (LinearLayout)findViewById(R.id.activity_engine);

        eng_28h = (Button)findViewById(R.id.eng_28h);
        eng_36h = (Button)findViewById(R.id.eng_36h);
        eng_43h = (Button)findViewById(R.id.eng_43h);
        eng_48h = (Button)findViewById(R.id.eng_48h);
        eng_54h = (Button)findViewById(R.id.eng_54h);
        eng_60h = (Button)findViewById(R.id.eng_60h);
        eng_1step = (Button)findViewById(R.id.eng_1step);
        eng_2step = (Button)findViewById(R.id.eng_2step);
        eng_3step = (Button)findViewById(R.id.eng_3step);
        eng_4step = (Button)findViewById(R.id.eng_4step);
        eng_5step = (Button)findViewById(R.id.eng_5step);

        eng_b_water = (Button)findViewById(R.id.eng_b_water);
        eng_b_inter = (Button)findViewById(R.id.eng_b_inter);
        eng_b_sol = (Button)findViewById(R.id.eng_b_sol);
        eng_b_ven = (Button)findViewById(R.id.eng_b_ven);
        eng_door_open = (Button)findViewById(R.id.eng_door_open);
        eng_door_close = (Button)findViewById(R.id.eng_door_close);
        eng_b_left = (Button)findViewById(R.id.eng_b_left);
        eng_b_right = (Button)findViewById(R.id.eng_b_right);
        eng_b_back = (Button)findViewById(R.id.eng_b_back);
        eng_r_left = (Button)findViewById(R.id.eng_r_left);
        eng_r_right = (Button)findViewById(R.id.eng_r_right);

        oxygen_m = (TextView) findViewById(R.id.oxygen_m);
        oxygen_m.setTypeface(tf);
        operation_t = (TextView) findViewById(R.id.operation_t);
        operation_t.setTypeface(tf);

        hidden_e_1 = (Button)findViewById(R.id.hidden_e_1);
        hidden_e_2 = (Button)findViewById(R.id.hidden_e_2);
        hidden_e_3 = (Button)findViewById(R.id.hidden_e_3);
        hidden_e_4 = (Button)findViewById(R.id.hidden_e_4);

        program_m = (Button)findViewById(R.id.program_m);
        invert_choice = (Button)findViewById(R.id.invert_choice);

        communicator = Application_manager.getCommunicator();
        int ox_m = (communicator.get_rx_idx(7)+communicator.get_rx_idx(8)+communicator.get_rx_idx(9)+communicator.get_rx_idx(10))/4;
        oxygen_m.setText(String.valueOf(ox_m));

        main_intent = new Intent(this, Activity_waiting.class);

        View.OnClickListener listener = new View.OnClickListener() {
            public void onClick(View v) {
                Application_manager.set_m_start_sleep(0);
                switch (v.getId()) {
                    case R.id.eng_28h:
                        //
                        if (eng_h_flag[0] == true) {
                            setZerosWaterPressure();
                            eng_28h.setBackgroundResource(R.drawable.water_28_on);
                            eng_h_flag[0] = false;
                            w_press = 1;
                        } else if (eng_h_flag[0] == false) {
                            eng_28h.setBackgroundResource(R.drawable.water_28_off);
                            eng_h_flag[0] = true;
                            w_press = 0;
                        }
                        Log.v("test",""+w_press);
                        communicator.set_engineer(2,(byte)((byte)inverter|(byte)w_press));
                        break;
                    case R.id.eng_36h:
                        //
                        if (eng_h_flag[1] == true) {
                            setZerosWaterPressure();
                            eng_36h.setBackgroundResource(R.drawable.water_36_on);
                            eng_h_flag[1] = false;
                            w_press = 2;
                        } else if (eng_h_flag[1] == false){
                            eng_36h.setBackgroundResource(R.drawable.water_36_off);
                            eng_h_flag[1] = true;
                            w_press = 0;
                        }
                        Log.v("test",""+w_press);
                        communicator.set_engineer(2,(byte)((byte)inverter|(byte)w_press));
                        break;
                    case R.id.eng_43h:
                        //
                        if (eng_h_flag[2] == true) {
                            setZerosWaterPressure();
                            eng_43h.setBackgroundResource(R.drawable.water_43_on);
                            eng_h_flag[2] = false;
                            w_press = 3;
                        } else if (eng_h_flag[2] == false){
                            eng_43h.setBackgroundResource(R.drawable.water_43_off);
                            eng_h_flag[2] = true;
                            w_press = 0;
                        }
                        communicator.set_engineer(2,(byte)((byte)inverter|(byte)w_press));
                        break;
                    case R.id.eng_48h:
                        //
                        if (eng_h_flag[3] == true) {
                            setZerosWaterPressure();
                            eng_48h.setBackgroundResource(R.drawable.water_49_on);
                            eng_h_flag[3] = false;
                            w_press = 4;
                        } else if (eng_h_flag[3] == false){
                            eng_48h.setBackgroundResource(R.drawable.water_49_off);
                            eng_h_flag[3] = true;
                            w_press = 0;
                        }
                        communicator.set_engineer(2,(byte)((byte)inverter|(byte)w_press));
                        break;
                    case R.id.eng_54h:
                        //
                        if (eng_h_flag[4] == true) {
                            setZerosWaterPressure();
                            eng_54h.setBackgroundResource(R.drawable.water_54_on);
                            eng_h_flag[4] = false;
                            w_press = 5;
                        } else if (eng_h_flag[4] == false){
                            eng_54h.setBackgroundResource(R.drawable.water_54_off);
                            eng_h_flag[4] = true;
                            w_press = 0;
                        }
                        communicator.set_engineer(2,(byte)((byte)inverter|(byte)w_press));
                        break;
                    case R.id.eng_60h:
                        //
                        if (eng_h_flag[5] == true) {
                            setZerosWaterPressure();
                            eng_60h.setBackgroundResource(R.drawable.water_60_on);
                            eng_h_flag[5] = false;
                            w_press = 6;
                        } else if (eng_h_flag[5] == false){
                            eng_60h.setBackgroundResource(R.drawable.water_60_off);
                            eng_h_flag[5] = true;
                            w_press = 0;
                        }
                        communicator.set_engineer(2,(byte)((byte)inverter|(byte)w_press));
                        break;
                    case R.id.eng_1step:
                        //
                        if (eng_step_flag[0] == true) {
                            setZerosOXY();
                            eng_1step.setBackgroundResource(Application_manager.oxygen_1step_on[Application_manager.img_flag]);
                            eng_step_flag[0] = false;
                            oxy = 1;
                        } else {
                            eng_1step.setBackgroundResource(Application_manager.oxygen_1step_off[Application_manager.img_flag]);
                            eng_step_flag[0] = true;
                            oxy = 0;
                        }
                        communicator.set_engineer(5,(byte)oxy);
                        break;
                    case R.id.eng_2step:
                        //
                        if (eng_step_flag[1] == true) {
                            setZerosOXY();
                            eng_2step.setBackgroundResource(Application_manager.oxygen_2step_on[Application_manager.img_flag]);
                            eng_step_flag[1] = false;
                            oxy = 2;
                        } else {
                            eng_2step.setBackgroundResource(Application_manager.oxygen_2step_off[Application_manager.img_flag]);
                            eng_step_flag[1] = true;
                            oxy = 0;
                        }
                        communicator.set_engineer(5,(byte)oxy);
                        break;
                    case R.id.eng_3step:
                        //
                        if (eng_step_flag[2] == true) {
                            setZerosOXY();
                            eng_3step.setBackgroundResource(Application_manager.oxygen_3step_on[Application_manager.img_flag]);
                            eng_step_flag[2] = false;
                            oxy = 3;
                        } else {
                            eng_3step.setBackgroundResource(Application_manager.oxygen_3step_off[Application_manager.img_flag]);
                            eng_step_flag[2] = true;
                            oxy = 0;
                        }
                        communicator.set_engineer(5,(byte)oxy);
                        break;
                    case R.id.eng_4step:
                        //
                        if (eng_step_flag[3] == true) {
                            setZerosOXY();
                            eng_4step.setBackgroundResource(Application_manager.oxygen_4step_on[Application_manager.img_flag]);
                            eng_step_flag[3] = false;
                            oxy = 4;
                        } else {
                            eng_4step.setBackgroundResource(Application_manager.oxygen_4step_off[Application_manager.img_flag]);
                            eng_step_flag[3] = true;
                            oxy = 0;
                        }
                        communicator.set_engineer(5,(byte)oxy);
                        break;
                    case R.id.eng_5step:
                        //
                        if (eng_step_flag[4] == true) {
                            setZerosOXY();
                            eng_5step.setBackgroundResource(Application_manager.oxygen_5step_on[Application_manager.img_flag]);
                            eng_step_flag[4] = false;
                            oxy = 5;
                        } else {
                            eng_5step.setBackgroundResource(Application_manager.oxygen_5step_off[Application_manager.img_flag]);
                            eng_step_flag[4] = true;
                            oxy = 0;
                        }
                        communicator.set_engineer(5,(byte)oxy);
                        break;
//----------------------------------------------------------------------------------------------------------------------------

                    case R.id.eng_b_water:
                        //  그레이 / 블루 / 핑크 ??
                        if (eng_b_flag[0] == true) {
                            eng_b_water.setBackgroundResource(R.drawable.button_blue);
                            eng_b_flag[0] = false;
                            communicator.set_engineer(3,(byte)0x01);
                        } else {
                            eng_b_water.setBackgroundResource(R.drawable.button_gry);
                            eng_b_flag[0] = true;
                            communicator.set_engineer(3,(byte)0x00);
                        }
                        break;
                    case R.id.eng_b_inter:
                        //
                        if (heater_f == 0) {
                            eng_b_inter.setBackgroundResource(R.drawable.button_blue);
                            heater_f = 1;
                            communicator.set_engineer(4,(byte)0x01);
                        } else if(heater_f == 1){
                            eng_b_inter.setBackgroundResource(R.drawable.button_pink);
                            heater_f = 2;
                            communicator.set_engineer(4,(byte)0x02);
                        } else if(heater_f == 2){
                            eng_b_inter.setBackgroundResource(R.drawable.button_gry);
                            heater_f = 0;
                            communicator.set_engineer(4,(byte)0x00);
                        }
                        break;
                    case R.id.eng_b_sol:
                        //
                        if (eng_b_flag[2] == true) {
                            eng_b_sol.setBackgroundResource(R.drawable.button_blue);
                            eng_b_flag[2] = false;
                            communicator.set_engineer(7,(byte)0x01);
                        } else {
                            eng_b_sol.setBackgroundResource(R.drawable.button_gry);
                            eng_b_flag[2] = true;
                            communicator.set_engineer(7,(byte)0x00);
                        }
                        break;
                    case R.id.eng_b_ven:
                        //
                        if (eng_b_flag[3] == true) {
                            eng_b_ven.setBackgroundResource(R.drawable.button_blue);
                            eng_b_flag[3] = false;
                            communicator.set_engineer(6,(byte)0x01);
                        } else {
                            eng_b_ven.setBackgroundResource(R.drawable.button_gry);
                            eng_b_flag[3] = true;
                            communicator.set_engineer(6,(byte)0x00);
                        }
                        break;
                    case R.id.program_m:
                        //
                        if (mode_f == true) {
                            program_m.setBackgroundResource(Application_manager.program_mode_on[Application_manager.img_flag]);
                            Application_manager.set_m_gw_1000(false);

                            // GW-1000L 버전 설정
                            Application_manager.setting_back_image[0] = R.drawable.setting_back_image_l;
                            Application_manager.setting_back_image[1] = R.drawable.setting_back_image_l_ch;


                            mode_f = false;
                        } else {
                            program_m.setBackgroundResource(Application_manager.program_mode_off[Application_manager.img_flag]);
                            Application_manager.set_m_gw_1000(true);
                            // GW-1000H 버전 설정
                            Application_manager.setting_back_image[0] = R.drawable.setting_back_image;
                            Application_manager.setting_back_image[1] = R.drawable.setting_back_image_ch;


                            mode_f = true;
                        }
                        break;
                    case R.id.invert_choice:
                        //
                        if (invert_f == true) {
                            invert_choice.setBackgroundResource(Application_manager.inverter_ls[Application_manager.img_flag]);
                            invert_f = false;
                            inverter = 0x10;
                        } else {
                            invert_choice.setBackgroundResource(Application_manager.inverter_ys[Application_manager.img_flag]);
                            invert_f = true;
                            inverter = 0x00;
                        }
                        communicator.set_engineer(2,(byte)((byte)inverter|(byte)w_press));
                        break;

                    case R.id.hidden_e_1:
                        //
                        if(hidden[3] == true)
                        {
                            hidden[0] = false;
                            hidden[1] = false;
                            hidden[2] = false;
                            hidden[3] = false;
                            //operating time re-set
                            operation_t.setText("0");
                            Application_manager.setRunningTime(0);
                        }else
                        {
                            hidden[0] = true;
                            Log.v("hidden","hidden1");
                        }
                        break;
                    case R.id.hidden_e_2:
                        //
                        if(hidden[0] == true)
                        {
                            hidden[1] = true;
                            Log.v("hidden","hidden2");
                        }
                        break;
                    case R.id.hidden_e_3:
                        //
                        if(hidden[1] == true)
                        {
                            hidden[2] = true;
                            Log.v("hidden","hidden3");
                        }
                        break;
                    case R.id.hidden_e_4:
                        //
                        if(hidden[2] == true)
                        {
                            hidden[3] = true;
                            Log.v("hidden","hidden4");
                        }
                        break;

                }
            }
        };

        eng_28h.setOnClickListener(listener);
        eng_36h.setOnClickListener(listener);
        eng_43h.setOnClickListener(listener);
        eng_48h.setOnClickListener(listener);
        eng_54h.setOnClickListener(listener);
        eng_60h.setOnClickListener(listener);
        eng_1step.setOnClickListener(listener);
        eng_2step.setOnClickListener(listener);
        eng_3step.setOnClickListener(listener);
        eng_4step.setOnClickListener(listener);
        eng_5step.setOnClickListener(listener);

        eng_b_water.setOnClickListener(listener);
        eng_b_inter.setOnClickListener(listener);
        eng_b_sol.setOnClickListener(listener);
        eng_b_ven.setOnClickListener(listener);
        program_m.setOnClickListener(listener);
        invert_choice.setOnClickListener(listener);

        hidden_e_1.setOnClickListener(listener);
        hidden_e_2.setOnClickListener(listener);
        hidden_e_3.setOnClickListener(listener);
        hidden_e_4.setOnClickListener(listener);

        eng_b_back.setOnTouchListener(mTouchEvent);
        eng_b_left.setOnTouchListener(mTouchEvent);
        eng_b_right.setOnTouchListener(mTouchEvent);
        eng_r_left.setOnTouchListener(mTouchEvent);
        eng_r_right.setOnTouchListener(mTouchEvent);

        eng_door_open.setOnTouchListener(mTouchEvent);
        eng_door_close.setOnTouchListener(mTouchEvent);
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

    private View.OnTouchListener mTouchEvent = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            Application_manager.set_m_start_sleep(0);
            int action = motionEvent.getAction();
            int id = view.getId();
            if (action == MotionEvent.ACTION_DOWN) {
                switch (id) {
                    case R.id.eng_b_back:
                        eng_b_back.setBackgroundResource(Application_manager.button_circle_back_on[Application_manager.img_flag]);
                        break;
                    case R.id.eng_b_left:
                        eng_b_left.setBackgroundResource(R.drawable.moving_left_on);
                        communicator.set_engineer(9,(byte)0x01);
                        break;
                    case R.id.eng_b_right:
                        eng_b_right.setBackgroundResource(R.drawable.moving_right_on);
                        communicator.set_engineer(9,(byte)0x02);
                        break;
                    case R.id.eng_r_left:
                        eng_r_left.setBackgroundResource(R.drawable.rotation_left_on);
                        communicator.set_engineer(9,(byte)0x10);
                        break;
                    case R.id.eng_r_right:
                        eng_r_right.setBackgroundResource(R.drawable.rotation_right_on);
                        communicator.set_engineer(9,(byte)0x20);
                        break;

                    case R.id.eng_door_open:
                        eng_door_open.setBackgroundResource(Application_manager.door_open_on[Application_manager.img_flag]);
                        communicator.set_engineer(9,(byte)0x01);
                        break;
                    case R.id.eng_door_close:
                        eng_door_close.setBackgroundResource(Application_manager.door_close_on[Application_manager.img_flag]);
                        communicator.set_engineer(9,(byte)0x02);
                        break;
                }
            } else if (action == MotionEvent.ACTION_UP) {
                byte val = 0x00;
                switch (id) {
                    case R.id.eng_b_back:
                        eng_b_back.setBackgroundResource(Application_manager.button_circle_back_off[Application_manager.img_flag]);
                        Log.v("test","check_activity : " + check_activity);
                        if(check_activity.equals("setting")){
                            onStop();
                            finish();
                        }
                        else{
                            onStop();
                            startActivity(main_intent);
                        }

                        break;
                    case R.id.eng_b_left:
                        eng_b_left.setBackgroundResource(R.drawable.moving_left_off);
                        communicator.set_engineer(9,(byte)0x00);
                        break;
                    case R.id.eng_b_right:
                        eng_b_right.setBackgroundResource(R.drawable.moving_right_off);
                        communicator.set_engineer(9,(byte)0x00);
                        break;
                    case R.id.eng_r_left:
                        eng_r_left.setBackgroundResource(R.drawable.rotation_left_off);
                        communicator.set_engineer(9,(byte)0x00);
                        break;
                    case R.id.eng_r_right:
                        eng_r_right.setBackgroundResource(R.drawable.rotation_right_off);
                        communicator.set_engineer(9,(byte)0x00);
                        break;

                    case R.id.eng_door_open:
                        eng_door_open.setBackgroundResource(Application_manager.door_open_off[Application_manager.img_flag]);
                        communicator.set_engineer(9,(byte)0x00);
                        break;
                    case R.id.eng_door_close:
                        eng_door_close.setBackgroundResource(Application_manager.door_close_off[Application_manager.img_flag]);
                        communicator.set_engineer(9,(byte)0x00);
                        break;
                }
            }
            return true;
        }
    };

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
        operation_t.setText(""+ Application_manager.getRunningTime()/60);
    }

    @Override
    protected void onStop() {
        super.onStop();
        isRun = false;
    }

    @Override
    protected void onResume() {
        super.onResume();
        Application_manager.setFullScreen(this);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        isRun = true;

        activity_engine.setBackgroundResource(Application_manager.engineermode_back_image[Application_manager.img_flag]);
        eng_door_open.setBackgroundResource(Application_manager.door_open_off[Application_manager.img_flag]);
        eng_door_close.setBackgroundResource(Application_manager.door_close_off[Application_manager.img_flag]);
        invert_choice.setBackgroundResource(Application_manager.inverter_ys[Application_manager.img_flag]);

        if(Application_manager.gw_1000 == true){
            program_m.setBackgroundResource(Application_manager.program_mode_off[Application_manager.img_flag]);
        }else if(Application_manager.gw_1000 == false){
            program_m.setBackgroundResource(Application_manager.program_mode_on[Application_manager.img_flag]);
        }
        eng_1step.setBackgroundResource(Application_manager.oxygen_1step_off[Application_manager.img_flag]);
        eng_2step.setBackgroundResource(Application_manager.oxygen_2step_off[Application_manager.img_flag]);
        eng_3step.setBackgroundResource(Application_manager.oxygen_3step_off[Application_manager.img_flag]);
        eng_4step.setBackgroundResource(Application_manager.oxygen_4step_off[Application_manager.img_flag]);
        eng_5step.setBackgroundResource(Application_manager.oxygen_5step_off[Application_manager.img_flag]);
        eng_b_back.setBackgroundResource(Application_manager.button_circle_back_off[Application_manager.img_flag]);

    }

    @Override
    protected void onPause() {
        super.onPause();
        isRun = false;
    }

    void setZerosWaterPressure()
    {
        eng_28h.setBackgroundResource(R.drawable.water_28_off);
        eng_h_flag[0] = true;
        eng_36h.setBackgroundResource(R.drawable.water_36_off);
        eng_h_flag[1] = true;
        eng_43h.setBackgroundResource(R.drawable.water_43_off);
        eng_h_flag[2] = true;
        eng_48h.setBackgroundResource(R.drawable.water_49_off);
        eng_h_flag[3] = true;
        eng_54h.setBackgroundResource(R.drawable.water_54_off);
        eng_h_flag[4] = true;
        eng_60h.setBackgroundResource(R.drawable.water_60_off);
        eng_h_flag[5] = true;
    }

    void setZerosOXY(){
        eng_1step.setBackgroundResource(Application_manager.oxygen_1step_off[Application_manager.img_flag]);
        eng_step_flag[0] = true;
        eng_2step.setBackgroundResource(Application_manager.oxygen_2step_off[Application_manager.img_flag]);
        eng_step_flag[1] = true;
        eng_3step.setBackgroundResource(Application_manager.oxygen_3step_off[Application_manager.img_flag]);
        eng_step_flag[2] = true;
        eng_4step.setBackgroundResource(Application_manager.oxygen_4step_off[Application_manager.img_flag]);
        eng_step_flag[3] = true;
        eng_5step.setBackgroundResource(Application_manager.oxygen_5step_off[Application_manager.img_flag]);
        eng_step_flag[4] = true;
    }
}
