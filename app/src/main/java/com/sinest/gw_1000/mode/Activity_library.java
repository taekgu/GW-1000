package com.sinest.gw_1000.mode;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ToggleButton;

import com.sinest.gw_1000.R;

public class Activity_library extends AppCompatActivity {

    public static final int REQUEST_CODE_LIBRARY = 1002;
    public static final int REQUEST_CODE_MANUAL_MODE_SETTING = 1003;

    int cnt;
    int manual_cnt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_library);

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
                        break;
                    case R.id.library_set_button:
                        b.setBackgroundResource(R.drawable.library_setting_off);
                        if(manual_cnt==1) {
                            intent = new Intent(getApplicationContext(), Activity_manual_mode_setting.class);
                            startActivityForResult(intent, REQUEST_CODE_MANUAL_MODE_SETTING);
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
        switch(v.getId()){
            case R.id.automode_01:
                tb = (ToggleButton)this.findViewById(R.id.automode_01);
                if(tb.isChecked()){
                    if(cnt>3)
                    {
                        tb.setChecked(false);
                    }
                    else {
                        tb.setBackgroundResource(R.drawable.automode_on_01);
                        cnt++;
                    }
                }
                else {
                    tb.setBackgroundDrawable(getResources().getDrawable(R.drawable.automode_01));
                    cnt--;
                }
                break;
            case R.id.automode_02:
                tb = (ToggleButton)this.findViewById(R.id.automode_02);
                if(tb.isChecked()){
                    if(cnt>3)
                    {
                        tb.setChecked(false);
                    }
                    else {
                        tb.setBackgroundResource(R.drawable.automode_on_02);
                        cnt++;
                    }
                }
                else {
                    tb.setBackgroundDrawable(getResources().getDrawable(R.drawable.automode_02));
                    cnt--;
                }
                break;
            case R.id.automode_03:
                tb = (ToggleButton)this.findViewById(R.id.automode_03);
                if(tb.isChecked()){
                    if(cnt>3)
                    {
                        tb.setChecked(false);
                    }
                    else {
                        tb.setBackgroundResource(R.drawable.automode_on_03);
                        cnt++;
                    }
                }
                else {
                    tb.setBackgroundDrawable(getResources().getDrawable(R.drawable.automode_03));
                    cnt--;
                }
                break;
            case R.id.automode_04:
                tb = (ToggleButton)this.findViewById(R.id.automode_04);
                if(tb.isChecked()){
                    if(cnt>3)
                    {
                        tb.setChecked(false);
                    }
                    else {
                        tb.setBackgroundResource(R.drawable.automode_on_04);
                        cnt++;
                    }
                }
                else {
                    tb.setBackgroundDrawable(getResources().getDrawable(R.drawable.automode_04));
                    cnt--;
                }
                break;
            case R.id.automode_05:
                tb = (ToggleButton)this.findViewById(R.id.automode_05);
                if(tb.isChecked()){
                    if(cnt>3)
                    {
                        tb.setChecked(false);
                    }
                    else {
                        tb.setBackgroundResource(R.drawable.automode_on_05);
                        cnt++;
                    }
                }
                else {
                    tb.setBackgroundDrawable(getResources().getDrawable(R.drawable.automode_05));
                    cnt--;
                }
                break;
            case R.id.automode_06:
                tb = (ToggleButton)this.findViewById(R.id.automode_06);
                if(tb.isChecked()){
                    if(cnt>3)
                    {
                        tb.setChecked(false);
                    }
                    else {
                        tb.setBackgroundResource(R.drawable.automode_on_06);
                        cnt++;
                    }
                }
                else {
                    tb.setBackgroundDrawable(getResources().getDrawable(R.drawable.automode_06));
                    cnt--;
                }
                break;
            case R.id.automode_07:
                tb = (ToggleButton)this.findViewById(R.id.automode_07);
                if(tb.isChecked()){
                    if(cnt>3)
                    {
                        tb.setChecked(false);
                    }
                    else {
                        tb.setBackgroundResource(R.drawable.automode_on_07);
                        cnt++;
                    }
                }
                else {
                    tb.setBackgroundDrawable(getResources().getDrawable(R.drawable.automode_07));
                    cnt--;
                }
                break;
            case R.id.automode_08:
                tb = (ToggleButton)this.findViewById(R.id.automode_08);
                if(tb.isChecked()){
                    if(cnt>3)
                    {
                        tb.setChecked(false);
                    }
                    else {
                        tb.setBackgroundResource(R.drawable.automode_on_08);
                        cnt++;
                    }
                }
                else {
                    tb.setBackgroundDrawable(getResources().getDrawable(R.drawable.automode_08));
                    cnt--;
                }
                break;
            case R.id.automode_09:
                tb = (ToggleButton)this.findViewById(R.id.automode_09);
                if(tb.isChecked()){
                    if(cnt>3)
                    {
                        tb.setChecked(false);
                    }
                    else {
                        tb.setBackgroundResource(R.drawable.automode_on_09);
                        cnt++;
                    }
                }
                else {
                    tb.setBackgroundDrawable(getResources().getDrawable(R.drawable.automode_09));
                    cnt--;
                }
                break;
            case R.id.automode_10:
                tb = (ToggleButton)this.findViewById(R.id.automode_10);
                if(tb.isChecked()){
                    if(cnt>3)
                    {
                        tb.setChecked(false);
                    }
                    else {
                        tb.setBackgroundResource(R.drawable.automode_on_10);
                        cnt++;
                    }
                }
                else {
                    tb.setBackgroundDrawable(getResources().getDrawable(R.drawable.automode_10));
                    cnt--;
                }
                break;
            case R.id.automode_11:
                tb = (ToggleButton)this.findViewById(R.id.automode_11);
                if(tb.isChecked()){
                    if(cnt>3)
                    {
                        tb.setChecked(false);
                    }
                    else {
                        tb.setBackgroundResource(R.drawable.automode_on_11);
                        cnt++;
                    }
                }
                else {
                    tb.setBackgroundDrawable(getResources().getDrawable(R.drawable.automode_11));
                    cnt--;
                }
                break;
            case R.id.automode_12:
                tb = (ToggleButton)this.findViewById(R.id.automode_12);
                if(tb.isChecked()){
                    if(cnt>3)
                    {
                        tb.setChecked(false);
                    }
                    else {
                        tb.setBackgroundResource(R.drawable.automode_on_12);
                        cnt++;
                    }
                }
                else {
                    tb.setBackgroundDrawable(getResources().getDrawable(R.drawable.automode_12));
                    cnt--;
                }
                break;
            case R.id.automode_13:
                tb = (ToggleButton)this.findViewById(R.id.automode_13);
                if(tb.isChecked()){
                    if(cnt>3)
                    {
                        tb.setChecked(false);
                    }
                    else {
                        tb.setBackgroundResource(R.drawable.automode_on_13);
                        cnt++;
                    }
                }
                else {
                    tb.setBackgroundDrawable(getResources().getDrawable(R.drawable.automode_13));
                    cnt--;
                }
                break;
            case R.id.automode_14:
                tb = (ToggleButton)this.findViewById(R.id.automode_14);
                if(tb.isChecked()){
                    if(cnt>3)
                    {
                        tb.setChecked(false);
                    }
                    else {
                        tb.setBackgroundResource(R.drawable.automode_on_14);
                        cnt++;
                    }
                }
                else {
                    tb.setBackgroundDrawable(getResources().getDrawable(R.drawable.automode_14));
                    cnt--;
                }
                break;
            case R.id.automode_15:
                tb = (ToggleButton)this.findViewById(R.id.automode_15);
                if(tb.isChecked()){
                    if(cnt>3)
                    {
                        tb.setChecked(false);
                    }
                    else {
                        tb.setBackgroundResource(R.drawable.automode_on_15);
                        cnt++;
                    }
                }
                else {
                    tb.setBackgroundDrawable(getResources().getDrawable(R.drawable.automode_15));
                    cnt--;
                }
                break;

            case R.id.manual_mode_01:
                tb = (ToggleButton)this.findViewById(R.id.manual_mode_01);
                if(tb.isChecked()) {
                    if (cnt > 3) {
                        tb.setChecked(false);
                    } else {
                        tb.setBackgroundDrawable(getResources().getDrawable(R.drawable.manual_mode_on_01));
                        manual_cnt++;
                        cnt++;
                    }
                }
                else{
                    tb.setBackgroundDrawable(getResources().getDrawable(R.drawable.manual_mode_01));
                    manual_cnt--;
                    cnt--;
                }
                break;
            case R.id.manual_mode_02:
                tb = (ToggleButton)this.findViewById(R.id.manual_mode_02);
                if(tb.isChecked()) {
                    if (cnt > 3) {
                        tb.setChecked(false);
                    } else {
                        tb.setBackgroundDrawable(getResources().getDrawable(R.drawable.manual_mode_on_02));
                        manual_cnt++;
                        cnt++;
                    }
                }
                else{
                    tb.setBackgroundDrawable(getResources().getDrawable(R.drawable.manual_mode_02));
                    manual_cnt--;
                    cnt--;
                }
                break;
            case R.id.manual_mode_03:
                tb = (ToggleButton)this.findViewById(R.id.manual_mode_03);
                if(tb.isChecked()) {
                    if (cnt > 3) {
                        tb.setChecked(false);
                    } else {
                        tb.setBackgroundDrawable(getResources().getDrawable(R.drawable.manual_mode_on_03));
                        manual_cnt++;
                        cnt++;
                    }
                }
                else{
                    tb.setBackgroundDrawable(getResources().getDrawable(R.drawable.manual_mode_03));
                    manual_cnt--;
                    cnt--;
                }
                break;
            case R.id.manual_mode_04:
                tb = (ToggleButton)this.findViewById(R.id.manual_mode_04);
                if(tb.isChecked()) {
                    if (cnt > 3) {
                        tb.setChecked(false);
                    } else {
                        tb.setBackgroundDrawable(getResources().getDrawable(R.drawable.manual_mode_on_04));
                        manual_cnt++;
                        cnt++;
                    }
                }
                else{
                    tb.setBackgroundDrawable(getResources().getDrawable(R.drawable.manual_mode_04));
                    manual_cnt--;
                    cnt--;
                }
                break;
            case R.id.manual_mode_05:
                tb = (ToggleButton)this.findViewById(R.id.manual_mode_05);
                if(tb.isChecked()) {
                    if (cnt > 3) {
                        tb.setChecked(false);
                    } else {
                        tb.setBackgroundDrawable(getResources().getDrawable(R.drawable.manual_mode_on_05));
                        manual_cnt++;
                        cnt++;
                    }
                }
                else{
                    tb.setBackgroundDrawable(getResources().getDrawable(R.drawable.manual_mode_05));
                    manual_cnt--;
                    cnt--;
                }
                break;
        }
    }
}
