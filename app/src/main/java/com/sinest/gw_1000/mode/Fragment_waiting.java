package com.sinest.gw_1000.mode;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.sinest.gw_1000.R;

/**
 * Created by Jinwook on 2016-11-21.
 */

public class Fragment_waiting extends Fragment {

    Button mode1, mode2, mode3, mode4;

    public Fragment_waiting() {

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_waiting, container, false);

     //   Log.i("WIFI", "onCreateView - Fragment-waiting");

        mode1 = (Button) view.findViewById(R.id.button_mode_1);
        mode2 = (Button) view.findViewById(R.id.button_mode_2);
        mode3 = (Button) view.findViewById(R.id.button_mode_3);
        mode4 = (Button) view.findViewById(R.id.button_mode_4);

        mode1.setOnTouchListener(mTouchEvent);
        mode2.setOnTouchListener(mTouchEvent);
        mode3.setOnTouchListener(mTouchEvent);
        mode4.setOnTouchListener(mTouchEvent);

        return view;
    }

    private View.OnTouchListener mTouchEvent = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {

            Activity_waiting activity = (Activity_waiting) getActivity();
            Button button_clicked = (Button) view;

            if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {

                if (button_clicked == mode1) {

                    // sp로부터 모드 선택된거받아와서적용
                    //button_clicked.setBackgroundResource(R.drawable.button_play_on);
                }
                else if (button_clicked == mode2) {

                }
                else if (button_clicked == mode3) {

                }
                else if (button_clicked == mode4) {

                }
            }
            else if (motionEvent.getAction() == MotionEvent.ACTION_UP) {

                // 추가해야됨 off
                if (button_clicked == mode1) {

                    activity.changeFragment(1);
                }
                else if (button_clicked == mode2) {

                    activity.changeFragment(2);
                }
                else if (button_clicked == mode3) {

                    activity.changeFragment(3);
                }
                else if (button_clicked == mode4) {

                    activity.changeFragment(4);
                }
            }

            return true;
        }
    };
}
