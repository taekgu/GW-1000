package com.sinest.gw_1000.mode;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.sinest.gw_1000.R;
import com.sinest.gw_1000.management.Application_manager;
import com.sinest.gw_1000.mode.utils.CustomSeekBar;

public class Activity_manual_mode_pattern_popup extends Activity implements CustomSeekBar.OnRangeBarChangeListener  {
    int patternNum, ImageResourceId;
    int section_min, section_max;
    Intent intent;
    ImageView img;
    CustomSeekBar customSeekBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        WindowManager.LayoutParams  layoutParams = new WindowManager.LayoutParams();
        layoutParams.flags= WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        layoutParams.dimAmount= 0.7f;
        getWindow().setAttributes(layoutParams);
        setContentView(R.layout.activity_manual_mode_pattern_popup);
        Application_manager.setFullScreen(this);

        ImageView manual_popup_save = (ImageView) findViewById(R.id.manual_popup_save);
        ImageView manual_popup_back = (ImageView) findViewById(R.id.manual_popup_back);
        customSeekBar = (CustomSeekBar) findViewById(R.id.custom_seek_bar);

        intent = getIntent();
        section_min = intent.getIntExtra("seekbar_min", 0);
        section_max = intent.getIntExtra("seekbar_max", 14);

        Log.i("RR", "rr_section_min : " + section_min);
        Log.i("RR", "rr_section_max : " + section_max);

        customSeekBar.setSectionInit(section_min, section_max);



        int resourceId;
        img = (ImageView)findViewById(R.id.manual_popup_imageview);
        patternNum = intent.getIntExtra("currentPattern",1);
        Log.i("currentPattern", Integer.toString(intent.getIntExtra("currentPattern",1)));
        resourceId = getResources().getIdentifier("manual_mode_pattern_" + intent.getIntExtra("currentPattern",1), "drawable", "com.sinest.gw_1000");
        img.setBackgroundResource(resourceId);

        for(int i=1; i<=12; i++){
            resourceId = getResources().getIdentifier("pattern_"+i,"id","com.sinest.gw_1000");
            ImageView btn = (ImageView) findViewById(resourceId);
            btn.setOnClickListener(mClickListener);
        }

        manual_popup_save.setOnTouchListener(mTouchEvent);
        manual_popup_back.setOnTouchListener(mTouchEvent);


    }
    private View.OnTouchListener mTouchEvent = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            Application_manager.set_m_start_sleep(0);
            ImageView b;
            int action = motionEvent.getAction();
            int id = view.getId();

            if (action == MotionEvent.ACTION_DOWN) {
                switch (id) {
                    case R.id.manual_popup_save:
                        b = (ImageView) view;
                        b.setBackgroundResource(R.drawable.save_mode_on);
                        break;
                    case R.id.manual_popup_back:
                        b = (ImageView) view;
                        b.setBackgroundResource(R.drawable.button_circle_back_on);
                        break;
                }
            } else if (action == MotionEvent.ACTION_UP) {
                switch (id) {
                    case R.id.manual_popup_save:
                        b = (ImageView) view;
                        SharedPreferences sharedPreferences = getSharedPreferences(Application_manager.DB_NAME, 0);
                        SharedPreferences.Editor editor = sharedPreferences.edit();

                        b.setBackgroundResource(R.drawable.save_mode_off);
                        Log.i("modeNum", Integer.toString(intent.getIntExtra("modeNum",0)));
                        Log.i("patternNum", Integer.toString(patternNum));
                        editor.putInt(Application_manager.DB_MANUAL_MODE_PATTERN_ + intent.getIntExtra("modeNum",0) + "_" + intent.getIntExtra("i",0), patternNum);
                        editor.putInt(Application_manager.DB_MANUAL_MODE_SECTION_MIN_ + intent.getIntExtra("modeNum",0) + "_" + intent.getIntExtra("i",0), section_min);
                        editor.putInt(Application_manager.DB_MANUAL_MODE_SECTION_MAX_ + intent.getIntExtra("modeNum",0) + "_" + intent.getIntExtra("i",0), section_max);
                        editor.commit();
                        finish();
                        break;
                    case R.id.manual_popup_back:
                        b = (ImageView) view;
                        b.setBackgroundResource(R.drawable.button_circle_back_off);
                        finish();
                        break;
                }
            }
            return true;
        }
    };

    private View.OnClickListener mClickListener = new View.OnClickListener() {
        public void onClick(View v) {
            Application_manager.set_m_start_sleep(0);

            int id = v.getId();
            int resourceId;
            for(int i=1; i<=12; i++)
            {
                resourceId = getResources().getIdentifier("pattern_"+i,"id","com.sinest.gw_1000");
                if(resourceId==id)
                {
                    ImageResourceId = getResources().getIdentifier("manual_mode_pattern_"+i,"drawable","com.sinest.gw_1000");
                    patternNum = i;
                    img.setBackgroundResource(ImageResourceId);
                }
            }
        }
    };

    @Override
    public void onRangeBarChange(int min, int max) {
        section_min = min;
        section_max = max;
        Log.d("TAG","min:"+min);
        Log.d("TAG","max:"+max);
    }
}
