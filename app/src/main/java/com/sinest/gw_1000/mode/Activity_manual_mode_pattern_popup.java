package com.sinest.gw_1000.mode;

import android.app.Activity;
import android.content.Intent;
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

public class Activity_manual_mode_pattern_popup extends Activity {
    int volume, ImageResourceId;
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

        Button manual_popup_save = (Button)findViewById(R.id.manual_popup_save);
        Button manual_popup_back = (Button)findViewById(R.id.manual_popup_back);

        int resourceId;
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
            Intent resultIntent= getIntent();
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
                        ImageView img = (ImageView)findViewById(R.id.manual_popup_imageview);
                        b.setBackgroundResource(R.drawable.save_mode_off);
                        resultIntent.putExtra("pattern_num", ImageResourceId);
                        Log.i("image resource id :", Integer.toString(ImageResourceId));
                        setResult(RESULT_OK, resultIntent);
                        finish();
                        break;
                    case R.id.manual_popup_back:
                        b = (Button) view;
                        b.setBackgroundResource(R.drawable.button_circle_back_off);
                        setResult(RESULT_CANCELED, resultIntent);
                        finish();
                        break;
                }
            }
            return true;
        }
    };

    private View.OnClickListener mClickListener = new View.OnClickListener() {
        public void onClick(View v) {
            ImageView img = (ImageView)findViewById(R.id.manual_popup_imageview);
            int id = v.getId();
            int resourceId;
            for(int i=1; i<=12; i++)
            {
                resourceId = getResources().getIdentifier("pattern_"+i,"id","com.sinest.gw_1000");
                if(resourceId==id)
                {
                    ImageResourceId = getResources().getIdentifier("manual_mode_pattern_"+i,"drawable","com.sinest.gw_1000");
                    img.setImageResource(ImageResourceId);
                }
            }
        }
    };
}
