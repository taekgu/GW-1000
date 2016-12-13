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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;

import com.sinest.gw_1000.R;
import com.sinest.gw_1000.management.Application_manager;
import com.sinest.gw_1000.mode.utils.CustomSeekBar;

public class Activity_manual_mode_pattern_popup extends Activity implements CustomSeekBar.OnRangeBarChangeListener  {
    int patternNum, ImageResourceId;
    Intent intent;
    ImageView img;
    SeekBar seekBar;
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

        Button manual_popup_save = (Button)findViewById(R.id.manual_popup_save);
        Button manual_popup_back = (Button)findViewById(R.id.manual_popup_back);

        intent = getIntent();
        int resourceId;
        img = (ImageView)findViewById(R.id.manual_popup_imageview);
        Log.i("currentPattern", Integer.toString(intent.getIntExtra("currentPattern",1)));
        resourceId = getResources().getIdentifier("manual_mode_pattern_" + intent.getIntExtra("currentPattern",1), "drawable", "com.sinest.gw_1000");
        img.setBackgroundResource(resourceId);

        for(int i=1; i<=12; i++){
            resourceId = getResources().getIdentifier("pattern_"+i,"id","com.sinest.gw_1000");
            Button btn = (Button)findViewById(resourceId);
            btn.setOnClickListener(mClickListener);
        }

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
                        SharedPreferences sharedPreferences = getSharedPreferences(Application_manager.NAME_OF_SHARED_PREF, 0);
                        SharedPreferences.Editor editor = sharedPreferences.edit();

                        b.setBackgroundResource(R.drawable.save_mode_off);
                        Log.i("modeNum", Integer.toString(intent.getIntExtra("modeNum",0)));
                        Log.i("patternNum", Integer.toString(patternNum));
                        editor.putInt(Application_manager.MANUAL_MODE_PATTERN_ + intent.getIntExtra("modeNum",0) + "_" + intent.getIntExtra("i",0), patternNum);
                        editor.commit();
                        finish();
                        break;
                    case R.id.manual_popup_back:
                        b = (Button) view;
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
        Log.d("TAG","min:"+min);
        Log.d("TAG","max:"+max);
    }
}
