package com.sinest.gw_1000.mode;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.sinest.gw_1000.R;
import com.sinest.gw_1000.management.Application_manager;

/**
 * Created by Jinwook on 2016-11-21.
 */

public class Fragment_waiting extends Fragment {

    Button[] mode = new Button[4];
    int idx = 0;
    int[] checked_idx = new int[4];

    private View view;

    public Fragment_waiting() {

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_waiting, container, false);

        refresh();

        return view;
    }

    public void reset() {

        idx = 0;
        for (int i = 0; i< Application_manager.MAX_CHECKED; i++) {

            checked_idx[i] = -1;
        }
    }

    public void refresh() {

        for (int i = 0; i< Application_manager.MAX_CHECKED; i++) {

            if (checked_idx[i] != -1) {

                int btn_resourceId = getResources().getIdentifier("button_mode_" + (i + 1), "id", "com.sinest.gw_1000");
                mode[i] = (Button) view.findViewById(btn_resourceId);
                mode[i].setOnTouchListener(mTouchEvent);

                int resourceId = -1;
                if (checked_idx[i] < 15) {

                    resourceId = getResources().getIdentifier("automode_" + (checked_idx[i] + 1), "drawable", "com.sinest.gw_1000");
                    //   Log.i("JW", "automode_" + (checked_idx[i]+1));
                } else {

                    resourceId = getResources().getIdentifier("manual_mode_" + (checked_idx[i] - 14), "drawable", "com.sinest.gw_1000");
                    //    Log.i("JW", "manual_mode_" + (checked_idx[i]+1));
                }
                mode[i].setBackgroundResource(resourceId);
            }
        }
    }

    public void addCheckedIdx(int _idx) {

        checked_idx[idx] = _idx;
        idx++;
    }

    private View.OnTouchListener mTouchEvent = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {

            Activity_waiting activity = (Activity_waiting) getActivity();
            Button button_clicked = (Button) view;

            if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {

                for (int i=0; i<4; i++) {

                    if (button_clicked == mode[i]) {

                        int resourceId = -1;
                        if (checked_idx[i] < 15) {

                            resourceId = getResources().getIdentifier("automode_on_" + (checked_idx[i]+1), "drawable", "com.sinest.gw_1000");
                        }
                        else {

                            resourceId = getResources().getIdentifier("manual_mode_on_" + (checked_idx[i]+1), "drawable", "com.sinest.gw_1000");
                        }
                        mode[i].setBackgroundResource(resourceId);
                    }
                }
            }
            else if (motionEvent.getAction() == MotionEvent.ACTION_UP) {

                for (int i=0; i<4; i++) {

                    if (button_clicked == mode[i]) {

                        int resourceId = -1;
                        if (checked_idx[i] < 15) {

                            resourceId = getResources().getIdentifier("automode_" + (checked_idx[i]+1), "drawable", "com.sinest.gw_1000");
                        }
                        else {

                            resourceId = getResources().getIdentifier("manual_mode_" + (checked_idx[i]+1), "drawable", "com.sinest.gw_1000");
                        }
                        mode[i].setBackgroundResource(resourceId);
                        activity.changeFragment_working(i+1);
                        Application_manager.getSoundManager().play(Application_manager.ID_LANG_SOUND[Application_manager.LANGUAGE][0]);
                    }
                }
            }

            return true;
        }
    };
}
