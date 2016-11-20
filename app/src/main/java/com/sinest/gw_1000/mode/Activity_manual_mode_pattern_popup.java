package com.sinest.gw_1000.mode;

import android.app.Activity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import com.sinest.gw_1000.R;

public class Activity_manual_mode_pattern_popup extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        WindowManager.LayoutParams  layoutParams = new WindowManager.LayoutParams();
        layoutParams.flags= WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        layoutParams.dimAmount= 0.7f;
        getWindow().setAttributes(layoutParams);
        setContentView(R.layout.activity_manual_mode_pattern_popup);

        Button manual_popup_save = (Button)findViewById(R.id.manual_popup_save);
        Button manual_popup_back = (Button)findViewById(R.id.manual_popup_back);

        manual_popup_save.setOnTouchListener(mTouchEvent);
        manual_popup_back.setOnTouchListener(mTouchEvent);
    }

    private View.OnTouchListener mTouchEvent = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            Button b;
            int action = motionEvent.getAction();
            int id = view.getId();
            if (action == MotionEvent.ACTION_DOWN) {
                switch (id) {
                    case R.id.manual_popup_save:
                        b = (Button) view;
                        b.setBackgroundResource(R.drawable.save_mode_on);
                        break;
                    case R.id.manual_popup_back:
                        b = (Button) view;
                        b.setBackgroundResource(R.drawable.button_circle_back_on);
                        break;
                }
            } else if (action == MotionEvent.ACTION_UP) {
                switch (id) {
                    case R.id.manual_popup_save:
                        b = (Button) view;
                        b.setBackgroundResource(R.drawable.save_mode_on);
                        break;
                    case R.id.manual_popup_back:
                        b = (Button) view;
                        b.setBackgroundResource(R.drawable.button_circle_back_on);
                        finish();
                        break;
                }
            }
            return true;
        }
    };
}
