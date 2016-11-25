package com.sinest.gw_1000.mode;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.sinest.gw_1000.R;
import com.sinest.gw_1000.management.Application_communicator;

import static com.sinest.gw_1000.mode.Activity_waiting.REQUEST_CODE_WORKINGTIME_POPUP;

public class Activity_manual_mode_setting extends AppCompatActivity {

    public static final int REQUEST_CODE_MANUAL_PATTERN_01 = 1011;
    public static final int REQUEST_CODE_MANUAL_PATTERN_02 = 1012;
    public static final int REQUEST_CODE_MANUAL_PATTERN_03 = 1013;

    private int modeNum;
    private int[] pattern = new int[3];
    private int[] time = new int[3];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manual_mode_setting);

        // 선택한 매뉴얼 모드 이미지 넣기
        Button manual_setting_selected_mode = (Button) findViewById(R.id.manual_setting_selected_mode);
        Intent intent = getIntent();

        Button manual_mode_setting_save = (Button)findViewById(R.id.manual_mode_setting_save);
        Button manual_mode_setting_back = (Button)findViewById(R.id.manual_mode_setting_back);

        ImageView[] manual_mode_setting = new ImageView[3];
        TextView[] manual_mode_time = new TextView[3];

        modeNum = intent.getExtras().getInt("modeNum");
        if (modeNum != -1) {

            int resourceId = getResources().getIdentifier("manual_mode_on_" + modeNum, "drawable", "com.sinest.gw_1000");
            manual_setting_selected_mode.setBackgroundResource(resourceId);
        }

        // 선택한 매뉴얼 모드 정보 로드
        SharedPreferences sharedPreferences = getSharedPreferences(Application_communicator.NAME_OF_SHARED_PREF, 0);
        int resourceId;
        for (int i=0; i<3; i++) {

            pattern[i] = sharedPreferences.getInt(Application_communicator.MANUAL_MODE_PATTERN_ + modeNum + "_" + i, 1);
            time[i] = sharedPreferences.getInt(Application_communicator.MANUAL_MODE_TIME_ + modeNum + "_" + i, 30);

            resourceId = getResources().getIdentifier("manual_mode_setting_" + (i+1), "id", "com.sinest.gw_1000");
            manual_mode_setting[i] = (ImageView)findViewById(resourceId);
            manual_mode_setting[i].setOnTouchListener(mTouchEvent);

            resourceId = getResources().getIdentifier("manual_mode_time_" + (i+1), "id", "com.sinest.gw_1000");
            manual_mode_time[i] = (TextView)findViewById(resourceId);

            resourceId = getResources().getIdentifier("manual_mode_pattern_" + pattern[i], "drawable", "com.sinest.gw_1000");
            manual_mode_setting[i].setBackgroundResource(resourceId);
            manual_mode_time[i].setText(""+time[i]);
        }

        manual_mode_setting_save.setOnTouchListener(mTouchEvent);
        manual_mode_setting_back.setOnTouchListener(mTouchEvent);
    }
    private View.OnTouchListener mTouchEvent = new View.OnTouchListener() {
                        @Override
                        public boolean onTouch(View view, MotionEvent motionEvent) {
                            Button b;
                            Intent intent;
                            int action = motionEvent.getAction();
                            int id = view.getId();
                            if (action == MotionEvent.ACTION_DOWN) {
                                switch (id) {
                                    case R.id.manual_mode_setting_save:
                                        b = (Button) view;
                        b.setBackgroundResource(R.drawable.save_mode_on);
                        break;
                    case R.id.manual_mode_setting_back:
                        b = (Button) view;
                        b.setBackgroundResource(R.drawable.button_circle_back_on);
                        break;
                    case R.id.manual_mode_setting_1:
                        break;
                    case R.id.manual_mode_setting_2:
                        break;
                    case R.id.manual_mode_setting_3:
                        break;

                }
            } else if (action == MotionEvent.ACTION_UP) {
                switch (id) {
                    case R.id.manual_mode_setting_save:
                        b = (Button) view;
                        b.setBackgroundResource(R.drawable.save_mode_off);

                        // 변경된 값 저장 후 액티비티 종료
                        SharedPreferences sharedPreferences = getSharedPreferences(Application_communicator.NAME_OF_SHARED_PREF, 0);
                        SharedPreferences.Editor editor = sharedPreferences.edit();

                        for (int i=0; i<3; i++) {

                            editor.putInt(Application_communicator.MANUAL_MODE_PATTERN_ + modeNum + "_" + i, pattern[i]);
                            editor.putInt(Application_communicator.MANUAL_MODE_TIME_ + modeNum + "_" + i, time[i]);
                        }
                        editor.commit();
                        finish();
                        break;
                    case R.id.manual_mode_setting_back:
                        b = (Button) view;
                        b.setBackgroundResource(R.drawable.button_circle_back_off);
                        finish();
                        break;
                    case R.id.manual_mode_setting_1:
                        intent = new Intent(getApplicationContext(), Activity_manual_mode_pattern_popup.class);
                        startActivityForResult(intent, REQUEST_CODE_MANUAL_PATTERN_01);
                        break;
                    case R.id.manual_mode_setting_2:
                        intent = new Intent(getApplicationContext(), Activity_manual_mode_pattern_popup.class);
                        startActivityForResult(intent, REQUEST_CODE_MANUAL_PATTERN_02);
                        break;
                    case R.id.manual_mode_setting_3:
                        intent = new Intent(getApplicationContext(), Activity_manual_mode_pattern_popup.class);
                        startActivityForResult(intent, REQUEST_CODE_MANUAL_PATTERN_03);
                        break;
                }
            }
            return true;
        }
    };
    public void onClicked(View v)
    {
        Intent intent;
        int id = v.getId();
        int resourceId;

        for(int i=1; i<=4; i++)
        {
            resourceId = getResources().getIdentifier("manual_setting_text_"+i,"id","com.sinest.gw_1000");
            if(resourceId==id){
                intent = new Intent(getApplicationContext(), Activity_waiting_working_time_popup.class);
                startActivityForResult(intent, REQUEST_CODE_WORKINGTIME_POPUP);
                break;
            }
        }
    }
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        ImageView iv;
        int img_resource = intent.getIntExtra("pattern_num", 0);
        if(requestCode==RESULT_OK) {
            switch (requestCode) {
                case REQUEST_CODE_MANUAL_PATTERN_01:
                    iv = (ImageView) findViewById(R.id.manual_mode_setting_1);
                    iv.setImageResource(img_resource);
                    break;
                case REQUEST_CODE_MANUAL_PATTERN_02:
                    iv = (ImageView) findViewById(R.id.manual_mode_setting_2);
                    iv.setImageResource(img_resource);
                    break;
                case REQUEST_CODE_MANUAL_PATTERN_03:
                    iv = (ImageView) findViewById(R.id.manual_mode_setting_3);
                    iv.setImageResource(img_resource);
                    break;
            }
        }
    }
}
