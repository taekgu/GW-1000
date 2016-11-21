package com.sinest.gw_1000.setting;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextClock;
import android.widget.TextView;

import com.sinest.gw_1000.R;
import com.sinest.gw_1000.communication.Communicator;
import com.sinest.gw_1000.management.Application_communicator;
import com.sinest.gw_1000.mode.Activity_library;

import static com.sinest.gw_1000.R.id.textClock;

public class Activity_setting extends AppCompatActivity {

    Communicator communicator;

    Button b_11; Button b_21; Button b_31; Button b_41;
    Button b_12; Button b_22; Button b_32; Button b_42;
    Button b_13; Button b_23; Button b_33; Button b_43;

    Button b_rf; Button b_ex; Button b_wa; Button b_pa;
    Button b_1m; Button b_3m; Button b_5m; Button b_coutinue;
    Button b_back; Button b_emotion;
    Button b_language; Button b_inverter;
    boolean[] button_flag = {true,true,true,true,true,true,true,true,true,true,true,true};
    boolean[] button2_flag = {true,true,true,true};
    boolean[] button3_flag = {true,true,true,true};
    boolean b_back_f = true;
    boolean b_emotion_f = true;
    int b_language_f = 0;
    int b_inverter_f = 0;

    Intent intent_emotion;
    Intent intent_rfid;
    Intent intent_wa;

    String check;
    String check2;

    SeekBar seekbar;
    int volume;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        communicator = Application_communicator.getCommunicator();

        //communicator.send(communicator.get_tx)

        TextClock textClock = (TextClock) findViewById(R.id.textClock);
        //textClock.setFormat24Hour();

        b_11 = (Button)findViewById(R.id.button11);
        b_21 = (Button)findViewById(R.id.button21);
        b_31 = (Button)findViewById(R.id.button31);
        b_41 = (Button)findViewById(R.id.button41);
        b_12 = (Button)findViewById(R.id.button12);
        b_22 = (Button)findViewById(R.id.button22);
        b_32 = (Button)findViewById(R.id.button32);
        b_42 = (Button)findViewById(R.id.button42);
        b_13 = (Button)findViewById(R.id.button13);
        b_23 = (Button)findViewById(R.id.button23);
        b_33 = (Button)findViewById(R.id.button33);
        b_43 = (Button)findViewById(R.id.button43);

        b_rf = (Button)findViewById(R.id.b_rf);
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

        seekbar = (SeekBar)findViewById(R.id.seekBar);
        seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // 메소드 이름대로 사용자가 SeekBar를 터치했을때 실행됩니다
                // TODO Auto-generated method stub
                Log.v("test1","v : "+volume);
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // 메소드 이름대로 사용자가 SeekBar를 손에서 땠을때 실행됩니다
                // TODO Auto-generated method stub
                Log.v("test2","v : " + volume);
            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                // 메소드 이름대로 사용자가 SeekBar를 움직일때 실행됩니다
                // 주로 사용되는 메소드 입니다
                // TODO Auto-generated method stub
                volume = progress;
            }
        });

        intent_emotion = new Intent(this, Activity_emotion.class);
        //intent_emotion.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);

        intent_rfid = new Intent(this, Activity_rfidcardpassord.class);
        //intent_rfid.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);

        intent_wa = new Intent(this, Activity_water.class);
        //intent_wa.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);

/*
        findViewById(R.id.button11).setOnClickListener(
                new Button.OnClickListener(){
                    public void onClick(View v){

                    }
                }
        );
*/
        View.OnClickListener listener = new View.OnClickListener(){
            public void onClick(View v){
                switch(v.getId()){
                    case R.id.button11:
                        //
                        if(button_flag[0] == true){
                            b_11.setBackgroundResource(R.drawable.button_on);
                            button_flag[0] = false;
                        }else{
                            b_11.setBackgroundResource(R.drawable.button_off);
                            button_flag[0] = true;
                        }
                        break;
                    case R.id.button21:
                        //
                        if(button_flag[1] == true){
                            b_21.setBackgroundResource(R.drawable.button_on);
                            button_flag[1] = false;
                        }else{
                            b_21.setBackgroundResource(R.drawable.button_off);
                            button_flag[1] = true;
                        }
                        break;
                    case R.id.button31:
                        //
                        if(button_flag[2] == true){
                            b_31.setBackgroundResource(R.drawable.button_on);
                            button_flag[2] = false;
                        }else{
                            b_31.setBackgroundResource(R.drawable.button_off);
                            button_flag[2] = true;
                        }
                        break;
                    case R.id.button41:
                        //
                        if(button_flag[3] == true){
                            b_41.setBackgroundResource(R.drawable.button_on);
                            button_flag[3] = false;
                        }else{
                            b_41.setBackgroundResource(R.drawable.button_off);
                            button_flag[3] = true;
                        }
                        break;
                    case R.id.button12:
                        //
                        if(button_flag[4] == true){
                            b_12.setBackgroundResource(R.drawable.button_on);
                            button_flag[4] = false;
                        }else{
                            b_12.setBackgroundResource(R.drawable.button_off);
                            button_flag[4] = true;
                        }
                        break;
                    case R.id.button22:
                        //
                        if(button_flag[5] == true){
                            b_22.setBackgroundResource(R.drawable.button_on);
                            button_flag[5] = false;
                        }else{
                            b_22.setBackgroundResource(R.drawable.button_off);
                            button_flag[5] = true;
                        }
                        break;
                    case R.id.button32:
                        //
                        if(button_flag[6] == true){
                            b_32.setBackgroundResource(R.drawable.button_on);
                            button_flag[6] = false;
                        }else{
                            b_32.setBackgroundResource(R.drawable.button_off);
                            button_flag[6] = true;
                        }
                        break;
                    case R.id.button42:
                        //
                        if(button_flag[7] == true){
                            b_42.setBackgroundResource(R.drawable.button_on);
                            button_flag[7] = false;
                        }else{
                            b_42.setBackgroundResource(R.drawable.button_off);
                            button_flag[7] = true;
                        }
                        break;
                    case R.id.button13:
                        //
                        if(button_flag[8] == true){
                            b_13.setBackgroundResource(R.drawable.button_on);
                            button_flag[8] = false;
                        }else{
                            b_13.setBackgroundResource(R.drawable.button_off);
                            button_flag[8] = true;
                        }
                        break;
                    case R.id.button23:
                        //
                        if(button_flag[9] == true){
                            b_23.setBackgroundResource(R.drawable.button_on);
                            button_flag[9] = false;
                        }else{
                            b_23.setBackgroundResource(R.drawable.button_off);
                            button_flag[9] = true;
                        }
                        break;
                    case R.id.button33:
                        //
                        if(button_flag[10] == true){
                            b_33.setBackgroundResource(R.drawable.button_on);
                            button_flag[10] = false;
                        }else{
                            b_33.setBackgroundResource(R.drawable.button_off);
                            button_flag[10] = true;
                        }
                        break;
                    case R.id.button43:
                        //
                        if(button_flag[11] == true){
                            b_43.setBackgroundResource(R.drawable.button_on);
                            button_flag[11] = false;
                        }else{
                            b_43.setBackgroundResource(R.drawable.button_off);
                            button_flag[11] = true;
                        }
                        break;
//----------------------------------------------------------------------------------------
                    case R.id.b_rf:
                        //
                        if(button2_flag[0] == true){
                            b_rf.setBackgroundResource(R.drawable.on);
                            button2_flag[0] = false;
                            intent_rfid.putExtra("check","ok");
                            startActivityForResult(intent_rfid,1);
                            //startActivity(intent_rfid);
                        }else{
                            b_rf.setBackgroundResource(R.drawable.off);
                            button2_flag[0] = true;
                        }
                        break;
                    case R.id.b_ex:
                        //
                        if(button2_flag[1] == true){
                            b_ex.setBackgroundResource(R.drawable.on);
                            button2_flag[1] = false;
                        }else{
                            b_ex.setBackgroundResource(R.drawable.off);
                            button2_flag[1] = true;
                        }
                        break;
                    case R.id.b_wa:
                        //
                        if(button2_flag[2] == true){
                            b_wa.setBackgroundResource(R.drawable.on);
                            button2_flag[2] = false;
                            intent_wa.putExtra("check","ok");
                            startActivityForResult(intent_wa,2);
                            //startActivity(intent_wa);
                        }else{
                            b_wa.setBackgroundResource(R.drawable.off);
                            button2_flag[2] = true;
                        }
                        break;
                    case R.id.b_pa:
                        //
                        if(button2_flag[3] == true){
                            b_pa.setBackgroundResource(R.drawable.on);
                            button2_flag[3] = false;
                        }else{
                            b_pa.setBackgroundResource(R.drawable.off);
                            button2_flag[3] = true;
                        }
                        break;

                    //----------------------------------------------------------------------------------------
                    case R.id.b_1m:
                        //
                        if(button3_flag[0] == true){
                            b_1m.setBackgroundResource(R.drawable.sleepmode_1min_on);
                            button3_flag[0] = false;
                        }else{
                            b_1m.setBackgroundResource(R.drawable.sleepmode_1min);
                            button3_flag[0] = true;
                        }
                        break;
                    case R.id.b_3m:
                        //
                        if(button3_flag[1] == true){
                            b_3m.setBackgroundResource(R.drawable.sleepmode_3min_on);
                            button3_flag[1] = false;
                        }else{
                            b_3m.setBackgroundResource(R.drawable.sleepmode_3min);
                            button3_flag[1] = true;
                        }
                        break;
                    case R.id.b_5m:
                        //
                        if(button3_flag[2] == true){
                            b_5m.setBackgroundResource(R.drawable.sleepmode_5min_on);
                            button3_flag[2] = false;
                        }else{
                            b_5m.setBackgroundResource(R.drawable.sleepmode_5min);
                            button3_flag[2] = true;
                        }
                        break;
                    case R.id.b_coutinue:
                        //
                        if(button3_flag[3] == true){
                            b_coutinue.setBackgroundResource(R.drawable.sleepmode_continue_on);
                            button3_flag[3] = false;
                        }else{
                            b_coutinue.setBackgroundResource(R.drawable.sleepmode_continue_off);
                            button3_flag[3] = true;
                        }
                        break;
//------------------------------------------------------------------------------------
                    case R.id.b_back:
                        //
                        if(b_back_f == true){
                            b_back.setBackgroundResource(R.drawable.button_circle_back_on);
                            b_back_f = false;
                            finish();
                        }else{
                            b_back.setBackgroundResource(R.drawable.button_circle_back_off);
                            b_back_f = true;
                        }
                        break;
                    case R.id.b_emotion:
                        //
                        if(b_emotion_f == true){
                            b_emotion.setBackgroundResource(R.drawable.emotion_on);
                            b_emotion_f = false;
                        }else{
                            b_emotion.setBackgroundResource(R.drawable.emotion_off);
                            b_emotion_f = true;
                        }
                        break;

                    case R.id.b_language:
                        //
                        if(b_language_f == 0){
                            b_language.setBackgroundResource(R.drawable.language_en);
                            b_language_f = 1;
                        }else if(b_language_f == 1){
                            b_language.setBackgroundResource(R.drawable.language_ch);
                            b_language_f = 2;
                        }else if(b_language_f == 2){
                            b_language.setBackgroundResource(R.drawable.language_ko);
                            b_language_f = 0;
                        }
                        break;
                    case R.id.b_inverter:
                        //
                        if(b_inverter_f == 0){
                            b_inverter.setBackgroundResource(R.drawable.inverter_50);
                            b_inverter_f = 1;
                        }else if(b_inverter_f == 1){
                            b_inverter.setBackgroundResource(R.drawable.inverter_100);
                            b_inverter_f = 2;
                        }else if(b_inverter_f == 2){
                            b_inverter.setBackgroundResource(R.drawable.inverter_0);
                            b_inverter_f = 0;
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
        b_back.setOnClickListener(listener);
        //b_emotion.setOnClickListener(listener);
        b_language.setOnClickListener(listener);
        b_inverter.setOnClickListener(listener);

        b_emotion.setOnTouchListener(mTouchEvent);

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
                }
            } else if (action == MotionEvent.ACTION_UP) {
                byte val = 0x00;
                switch (id) {
                    case R.id.b_emotion:
                        b_emotion.setBackgroundResource(R.drawable.emotion_off);
                        startActivity(intent_emotion);
                        break;
                }
            }
            return true;
        }
    };


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode,resultCode,data);
        if(resultCode == RESULT_OK){
            switch (requestCode) {
                case 1:
                    check = data.getStringExtra("check");
                    Log.v("test",check);
                    if(check.equals("No")){
                        b_rf.setBackgroundResource(R.drawable.off);
                        button2_flag[0] = true;
                        check = "Yes";
                    }
                    break;
                case 2:
                    check2 = data.getStringExtra("check");
                    if(check2.equals("No")){
                        b_wa.setBackgroundResource(R.drawable.off);
                        button2_flag[2] = true;
                        check2 = "Yes";
                    }
                    break;
                default:
                    break;
            }
        }
    }
}
