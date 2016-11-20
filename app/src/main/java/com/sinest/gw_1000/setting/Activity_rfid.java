package com.sinest.gw_1000.setting;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import com.sinest.gw_1000.R;

public class Activity_rfid extends Activity {

    Button rfid_w_up; Button rfid_w_down;
    Button rfid_t_up; Button rfid_t_down;
    Button rfid_save; Button rfid_check; Button rfid_off;
    Button rfid_back;

    boolean[] rfid_w_flag = {true,true};
    boolean[] rfid_t_flag = {true,true};
    boolean[] rfid_flag = {true,true,true};
    boolean rfid_b_f = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        layoutParams.dimAmount = 0.7f;
        getWindow().setAttributes(layoutParams);
        setContentView(R.layout.activity_rfid);

        rfid_w_up = (Button) findViewById(R.id.rfid_w_up);
        rfid_w_down = (Button) findViewById(R.id.rfid_w_down);
        rfid_t_up = (Button) findViewById(R.id.rfid_t_up);
        rfid_t_down = (Button) findViewById(R.id.rfid_t_down);
        rfid_save = (Button) findViewById(R.id.rfid_save);
        rfid_check = (Button) findViewById(R.id.rfid_check);
        rfid_off = (Button) findViewById(R.id.rfid_off);
        rfid_back = (Button) findViewById(R.id.rfid_back);

        View.OnClickListener listener = new View.OnClickListener() {
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.rfid_w_up:
                        //
                        if (rfid_w_flag[0] == true) {
                            rfid_w_up.setBackgroundResource(R.drawable.button_up_on);
                            rfid_w_flag[0] = false;
                        } else {
                            rfid_w_up.setBackgroundResource(R.drawable.button_up_off);
                            rfid_w_flag[0] = true;
                        }
                        break;
                    case R.id.rfid_w_down:
                        //
                        if (rfid_w_flag[1] == true) {
                            rfid_w_down.setBackgroundResource(R.drawable.button_down_on);
                            rfid_w_flag[1] = false;
                        } else {
                            rfid_w_down.setBackgroundResource(R.drawable.button_down_off);
                            rfid_w_flag[1] = true;
                        }
                        break;
                    case R.id.rfid_t_up:
                        //
                        if (rfid_t_flag[0] == true) {
                            rfid_t_up.setBackgroundResource(R.drawable.button_up_on);
                            rfid_w_flag[0] = false;
                        } else {
                            rfid_t_up.setBackgroundResource(R.drawable.button_up_off);
                            rfid_w_flag[0] = true;
                        }
                        break;
                    case R.id.rfid_t_down:
                        //
                        if (rfid_t_flag[1] == true) {
                            rfid_t_down.setBackgroundResource(R.drawable.button_down_on);
                            rfid_w_flag[1] = false;
                        } else {
                            rfid_t_down.setBackgroundResource(R.drawable.button_down_off);
                            rfid_w_flag[1] = true;
                        }
                        break;
                    //--------------------------------------------------
                    case R.id.rfid_save:
                        //
                        if (rfid_flag[0] == true) {
                            rfid_save.setBackgroundResource(R.drawable.save_setting_on);
                            rfid_flag[0] = false;
                        } else {
                            rfid_save.setBackgroundResource(R.drawable.save_setting_off);
                            rfid_flag[0] = true;
                        }
                        break;
                    case R.id.rfid_check:
                        //
                        if (rfid_flag[1] == true) {
                            rfid_check.setBackgroundResource(R.drawable.check_on);
                            rfid_flag[1] = false;
                        } else {
                            rfid_check.setBackgroundResource(R.drawable.check_off);
                            rfid_flag[1] = true;
                        }
                        break;
                    case R.id.rfid_off:
                        //
                        if (rfid_flag[2] == true) {
                            rfid_off.setBackgroundResource(R.drawable.on);
                            rfid_flag[2] = false;
                        } else {
                            rfid_off.setBackgroundResource(R.drawable.off);
                            rfid_flag[2] = true;
                        }
                        break;
                    case R.id.rfid_back:
                        //
                        if (rfid_b_f == true) {
                            rfid_back.setBackgroundResource(R.drawable.button_elipse_back_on);
                            rfid_b_f = false;
                        } else {
                            rfid_back.setBackgroundResource(R.drawable.button_elipse_back_off);
                            rfid_b_f = true;
                        }
                        break;
                }
            }
        };

        rfid_w_up.setOnClickListener(listener);
        rfid_w_down.setOnClickListener(listener);
        rfid_t_up.setOnClickListener(listener);
        rfid_t_down.setOnClickListener(listener);
        rfid_save.setOnClickListener(listener);
        rfid_check.setOnClickListener(listener);
        rfid_off.setOnClickListener(listener);
        rfid_back.setOnClickListener(listener);

    }
}
