package com.sinest.gw_1000.setting;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.sinest.gw_1000.R;

public class Activity_engine extends AppCompatActivity {

    Button eng_28h; Button eng_36h; Button eng_43h; Button eng_48h;
    Button eng_54h; Button eng_60h; Button eng_1step; Button eng_2step;
    Button eng_3step; Button eng_4step; Button eng_5step;

    Button eng_b_water; Button eng_b_inter; Button eng_b_sol; Button eng_b_ven;
    Button eng_door_open; Button eng_door_close;
    Button eng_b_left; Button eng_b_right; Button eng_b_back; Button eng_r_left; Button eng_r_right;

    boolean[] eng_h_flag = {true,true,true,true,true,true};
    boolean[] eng_step_flag = {true,true,true,true,true};

    boolean[] eng_b_flag = {true,true,true,true,true,true};
    boolean[] eng_flag = {true,true,true,true,true};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_engine);

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

        View.OnClickListener listener = new View.OnClickListener() {
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.eng_28h:
                        //
                        if (eng_h_flag[0] == true) {
                            eng_28h.setBackgroundResource(R.drawable.water_28_on);
                            eng_h_flag[0] = false;
                        } else {
                            eng_28h.setBackgroundResource(R.drawable.water_28_off);
                            eng_h_flag[0] = true;
                        }
                        break;
                    case R.id.eng_36h:
                        //
                        if (eng_h_flag[1] == true) {
                            eng_36h.setBackgroundResource(R.drawable.water_36_on);
                            eng_h_flag[1] = false;
                        } else {
                            eng_36h.setBackgroundResource(R.drawable.water_36_off);
                            eng_h_flag[1] = true;
                        }
                        break;
                    case R.id.eng_43h:
                        //
                        if (eng_h_flag[2] == true) {
                            eng_43h.setBackgroundResource(R.drawable.water_43_on);
                            eng_h_flag[2] = false;
                        } else {
                            eng_43h.setBackgroundResource(R.drawable.water_43_off);
                            eng_h_flag[2] = true;
                        }
                        break;
                    case R.id.eng_48h:
                        //
                        if (eng_h_flag[3] == true) {
                            eng_48h.setBackgroundResource(R.drawable.water_49_on);
                            eng_h_flag[3] = false;
                        } else {
                            eng_48h.setBackgroundResource(R.drawable.water_49_off);
                            eng_h_flag[3] = true;
                        }
                        break;
                    case R.id.eng_54h:
                        //
                        if (eng_h_flag[4] == true) {
                            eng_54h.setBackgroundResource(R.drawable.water_54_on);
                            eng_h_flag[4] = false;
                        } else {
                            eng_54h.setBackgroundResource(R.drawable.water_54_off);
                            eng_h_flag[4] = true;
                        }
                        break;
                    case R.id.eng_60h:
                        //
                        if (eng_h_flag[5] == true) {
                            eng_60h.setBackgroundResource(R.drawable.water_60_on);
                            eng_h_flag[5] = false;
                        } else {
                            eng_60h.setBackgroundResource(R.drawable.water_60_off);
                            eng_h_flag[5] = true;
                        }
                        break;
                    case R.id.eng_1step:
                        //
                        if (eng_step_flag[0] == true) {
                            eng_1step.setBackgroundResource(R.drawable.oxygen_1step_on);
                            eng_step_flag[0] = false;
                        } else {
                            eng_1step.setBackgroundResource(R.drawable.oxygen_1step_off);
                            eng_step_flag[0] = true;
                        }
                        break;
                    case R.id.eng_2step:
                        //
                        if (eng_step_flag[1] == true) {
                            eng_2step.setBackgroundResource(R.drawable.oxygen_2step_on);
                            eng_step_flag[1] = false;
                        } else {
                            eng_2step.setBackgroundResource(R.drawable.oxygen_2step_off);
                            eng_step_flag[1] = true;
                        }
                        break;
                    case R.id.eng_3step:
                        //
                        if (eng_step_flag[2] == true) {
                            eng_3step.setBackgroundResource(R.drawable.oxygen_3step_on);
                            eng_step_flag[2] = false;
                        } else {
                            eng_3step.setBackgroundResource(R.drawable.oxygen_3step_off);
                            eng_step_flag[2] = true;
                        }
                        break;
                    case R.id.eng_4step:
                        //
                        if (eng_step_flag[3] == true) {
                            eng_4step.setBackgroundResource(R.drawable.oxygen_4step_on);
                            eng_step_flag[3] = false;
                        } else {
                            eng_4step.setBackgroundResource(R.drawable.oxygen_4step_off);
                            eng_step_flag[3] = true;
                        }
                        break;
                    case R.id.eng_5step:
                        //
                        if (eng_step_flag[4] == true) {
                            eng_5step.setBackgroundResource(R.drawable.oxygen_5step_on);
                            eng_step_flag[4] = false;
                        } else {
                            eng_5step.setBackgroundResource(R.drawable.oxygen_5step_off);
                            eng_step_flag[4] = true;
                        }
                        break;
//----------------------------------------------------------------------------------------------------------------------------

                    case R.id.eng_b_water:
                        //  그레이 / 블루 / 핑크 ??
                        if (eng_b_flag[0] == true) {
                            eng_b_water.setBackgroundResource(R.drawable.button_blue);
                            eng_b_flag[0] = false;
                        } else {
                            eng_b_water.setBackgroundResource(R.drawable.button_gry);
                            eng_b_flag[0] = true;
                        }
                        break;
                    case R.id.eng_b_inter:
                        //
                        if (eng_b_flag[1] == true) {
                            eng_b_inter.setBackgroundResource(R.drawable.button_blue);
                            eng_b_flag[1] = false;
                        } else {
                            eng_b_inter.setBackgroundResource(R.drawable.button_gry);
                            eng_b_flag[1] = true;
                        }
                        break;
                    case R.id.eng_b_sol:
                        //
                        if (eng_b_flag[2] == true) {
                            eng_b_sol.setBackgroundResource(R.drawable.button_blue);
                            eng_b_flag[2] = false;
                        } else {
                            eng_b_sol.setBackgroundResource(R.drawable.button_gry);
                            eng_b_flag[2] = true;
                        }
                        break;
                    case R.id.eng_b_ven:
                        //
                        if (eng_b_flag[3] == true) {
                            eng_b_ven.setBackgroundResource(R.drawable.button_blue);
                            eng_b_flag[3] = false;
                        } else {
                            eng_b_ven.setBackgroundResource(R.drawable.button_gry);
                            eng_b_flag[3] = true;
                        }
                        break;
                    case R.id.eng_door_open:
                        //
                        if (eng_b_flag[4] == true) {
                            eng_door_open.setBackgroundResource(R.drawable.door_open_on);
                            eng_b_flag[4] = false;
                        } else {
                            eng_door_open.setBackgroundResource(R.drawable.door_open_off);
                            eng_b_flag[4] = true;
                        }
                        break;
                    case R.id.eng_door_close:
                        //
                        if (eng_b_flag[5] == true) {
                            eng_door_close.setBackgroundResource(R.drawable.door_close_on);
                            eng_b_flag[5] = false;
                        } else {
                            eng_door_close.setBackgroundResource(R.drawable.door_close_off);
                            eng_b_flag[5] = true;
                        }
                        break;
                    case R.id.eng_b_left:
                        //
                        if (eng_flag[0] == true) {
                            eng_b_left.setBackgroundResource(R.drawable.moving_left_on);
                            eng_flag[0] = false;
                        } else {
                            eng_b_left.setBackgroundResource(R.drawable.moving_left_off);
                            eng_flag[0] = true;
                        }
                        break;
                    case R.id.eng_b_right:
                        //
                        if (eng_flag[1] == true) {
                            eng_b_right.setBackgroundResource(R.drawable.moving_right_on);
                            eng_flag[1] = false;
                        } else {
                            eng_b_right.setBackgroundResource(R.drawable.moving_right_off);
                            eng_flag[1] = true;
                        }
                        break;
                    case R.id.eng_b_back:
                        //
                        if (eng_flag[2] == true) {
                            eng_b_back.setBackgroundResource(R.drawable.button_circle_back_on);
                            eng_flag[2] = false;
                        } else {
                            eng_b_back.setBackgroundResource(R.drawable.button_circle_back_off);
                            eng_flag[2] = true;
                        }
                        break;
                    case R.id.eng_r_left:
                        //
                        if (eng_flag[3] == true) {
                            eng_r_left.setBackgroundResource(R.drawable.rotation_left_on);
                            eng_flag[3] = false;
                        } else {
                            eng_r_left.setBackgroundResource(R.drawable.rotation_left_off);
                            eng_flag[3] = true;
                        }
                        break;
                    case R.id.eng_r_right:
                        //
                        if (eng_flag[4] == true) {
                            eng_r_right.setBackgroundResource(R.drawable.rotation_right_on);
                            eng_flag[4] = false;
                        } else {
                            eng_r_right.setBackgroundResource(R.drawable.rotation_right_off);
                            eng_flag[4] = true;
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
        eng_door_open.setOnClickListener(listener);
        eng_door_close.setOnClickListener(listener);
        eng_b_left.setOnClickListener(listener);
        eng_b_right.setOnClickListener(listener);
        eng_b_back.setOnClickListener(listener);
        eng_r_left.setOnClickListener(listener);
        eng_r_right.setOnClickListener(listener);





    }
}
