package com.sinest.gw_1000.setting;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import com.sinest.gw_1000.R;

public class Activity_rfidcardpassord extends Activity {

    Button rfid_password_1; Button rfid_password_2; Button rfid_password_3;
    Button rfid_password_4; Button rfid_password_5; Button rfid_password_6;
    Button rfid_password_7; Button rfid_password_8; Button rfid_password_9; Button rfid_password_0;
    Button rfid_password_d; Button rfid_password_c; Button rfid_password_e; Button rfid_password_b;

    boolean[] r_password_kflag = {true,true,true,true,true,true,true,true,true,true};
    boolean[] r_password_flag = {true,true,true,true};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        layoutParams.dimAmount = 0.7f;
        getWindow().setAttributes(layoutParams);
        setContentView(R.layout.activity_rfid_card_passord);

        rfid_password_1 = (Button) findViewById(R.id.rfid_password_1);
        rfid_password_2 = (Button) findViewById(R.id.rfid_password_2);
        rfid_password_3 = (Button) findViewById(R.id.rfid_password_3);
        rfid_password_4 = (Button) findViewById(R.id.rfid_password_4);
        rfid_password_5 = (Button) findViewById(R.id.rfid_password_5);
        rfid_password_6 = (Button) findViewById(R.id.rfid_password_6);
        rfid_password_7 = (Button) findViewById(R.id.rfid_password_7);
        rfid_password_8 = (Button) findViewById(R.id.rfid_password_8);
        rfid_password_9 = (Button) findViewById(R.id.rfid_password_9);
        rfid_password_0 = (Button) findViewById(R.id.rfid_password_0);
        rfid_password_d = (Button) findViewById(R.id.rfid_password_d);
        rfid_password_c = (Button) findViewById(R.id.rfid_password_c);
        rfid_password_e = (Button) findViewById(R.id.rfid_password_e);
        rfid_password_b = (Button) findViewById(R.id.rfid_password_b);

        View.OnClickListener listener = new View.OnClickListener() {
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.rfid_password_1:
                        //
                        if (r_password_kflag[0] == true) {
                            rfid_password_1.setBackgroundResource(R.drawable.keypad_1);
                            r_password_kflag[0] = false;
                        } else {
                            rfid_password_1.setBackgroundResource(R.drawable.keypad_1);
                            r_password_kflag[0] = true;
                        }
                        break;
                    case R.id.rfid_password_2:
                        //
                        if (r_password_kflag[1] == true) {
                            rfid_password_2.setBackgroundResource(R.drawable.keypad_2);
                            r_password_kflag[1] = false;
                        } else {
                            rfid_password_2.setBackgroundResource(R.drawable.keypad_2);
                            r_password_kflag[1] = true;
                        }
                        break;
                    case R.id.rfid_password_3:
                        //
                        if (r_password_kflag[2] == true) {
                            rfid_password_3.setBackgroundResource(R.drawable.keypad_3);
                            r_password_kflag[2] = false;
                        } else {
                            rfid_password_3.setBackgroundResource(R.drawable.keypad_3);
                            r_password_kflag[2] = true;
                        }
                        break;
                    case R.id.rfid_password_4:
                        //
                        if (r_password_kflag[3] == true) {
                            rfid_password_4.setBackgroundResource(R.drawable.keypad_4);
                            r_password_kflag[3] = false;
                        } else {
                            rfid_password_4.setBackgroundResource(R.drawable.keypad_4);
                            r_password_kflag[3] = true;
                        }
                        break;
                    case R.id.rfid_password_5:
                        //
                        if (r_password_kflag[4] == true) {
                            rfid_password_5.setBackgroundResource(R.drawable.keypad_5);
                            r_password_kflag[4] = false;
                        } else {
                            rfid_password_5.setBackgroundResource(R.drawable.keypad_5);
                            r_password_kflag[4] = true;
                        }
                        break;
                    case R.id.rfid_password_6:
                        //
                        if (r_password_kflag[5] == true) {
                            rfid_password_6.setBackgroundResource(R.drawable.keypad_6);
                            r_password_kflag[5] = false;
                        } else {
                            rfid_password_6.setBackgroundResource(R.drawable.keypad_6);
                            r_password_kflag[5] = true;
                        }
                        break;
                    case R.id.rfid_password_7:
                        //
                        if (r_password_kflag[6] == true) {
                            rfid_password_7.setBackgroundResource(R.drawable.keypad_7);
                            r_password_kflag[6] = false;
                        } else {
                            rfid_password_7.setBackgroundResource(R.drawable.keypad_7);
                            r_password_kflag[6] = true;
                        }
                        break;
                    case R.id.rfid_password_8:
                        //
                        if (r_password_kflag[7] == true) {
                            rfid_password_8.setBackgroundResource(R.drawable.keypad_8);
                            r_password_kflag[7] = false;
                        } else {
                            rfid_password_8.setBackgroundResource(R.drawable.keypad_8);
                            r_password_kflag[7] = true;
                        }
                        break;
                    case R.id.rfid_password_9:
                        //
                        if (r_password_kflag[8] == true) {
                            rfid_password_9.setBackgroundResource(R.drawable.keypad_9);
                            r_password_kflag[8] = false;
                        } else {
                            rfid_password_9.setBackgroundResource(R.drawable.keypad_9);
                            r_password_kflag[8] = true;
                        }
                        break;
                    case R.id.rfid_password_0:
                        //
                        if (r_password_kflag[9] == true) {
                            rfid_password_0.setBackgroundResource(R.drawable.keypad_0);
                            r_password_kflag[9] = false;
                        } else {
                            rfid_password_0.setBackgroundResource(R.drawable.keypad_0);
                            r_password_kflag[9] = true;
                        }
                        break;
                    //-----------------------------------------------
                    case R.id.rfid_password_d:
                        //
                        if (r_password_flag[0] == true) {
                            rfid_password_d.setBackgroundResource(R.drawable.keypad_delete);
                            r_password_flag[0] = false;
                        } else {
                            rfid_password_d.setBackgroundResource(R.drawable.keypad_delete);
                            r_password_flag[0] = true;
                        }
                        break;
                    case R.id.rfid_password_c:
                        //
                        if (r_password_flag[1] == true) {
                            rfid_password_c.setBackgroundResource(R.drawable.keypad_change);
                            r_password_flag[1] = false;
                        } else {
                            rfid_password_c.setBackgroundResource(R.drawable.keypad_change);
                            r_password_flag[1] = true;
                        }
                        break;
                    case R.id.rfid_password_b:
                        //
                        if (r_password_flag[2] == true) {
                            rfid_password_b.setBackgroundResource(R.drawable.keypad_back);
                            r_password_flag[2] = false;
                            finish();
                        } else {
                            rfid_password_b.setBackgroundResource(R.drawable.keypad_back);
                            r_password_flag[2] = true;
                        }
                        break;
                    case R.id.rfid_password_e:
                        //
                        if (r_password_flag[3] == true) {
                            rfid_password_e.setBackgroundResource(R.drawable.keypad_enter);
                            r_password_flag[3] = false;

                        } else {
                            rfid_password_e.setBackgroundResource(R.drawable.keypad_enter);
                            r_password_flag[3] = true;
                        }
                        break;
                }
            }
        };

        rfid_password_1.setOnClickListener(listener);
        rfid_password_2.setOnClickListener(listener);
        rfid_password_3.setOnClickListener(listener);
        rfid_password_4.setOnClickListener(listener);
        rfid_password_5.setOnClickListener(listener);
        rfid_password_6.setOnClickListener(listener);
        rfid_password_7.setOnClickListener(listener);
        rfid_password_8.setOnClickListener(listener);
        rfid_password_9.setOnClickListener(listener);
        rfid_password_0.setOnClickListener(listener);
        rfid_password_d.setOnClickListener(listener);
        rfid_password_c.setOnClickListener(listener);
        rfid_password_e.setOnClickListener(listener);
        rfid_password_b.setOnClickListener(listener);

    }
}
