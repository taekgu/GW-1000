package com.sinest.gw_1000.mode;

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
import com.sinest.gw_1000.communication.Communicator;
import com.sinest.gw_1000.management.Application_broadcast;
import com.sinest.gw_1000.management.Application_communicator;

/**
 * Created by Jinwook on 2016-11-21.
 */

public class Fragment_working extends Fragment {

    private int modeNum = -1;
    Button play, pause, stop;

    public Fragment_working() {

    }

    public void setModeNum (int _modeNum) {

        this.modeNum = _modeNum;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_working, container, false);

     //   Log.i("WIFI", "onCreateView - Fragment_working");

        play = (Button) view.findViewById(R.id.button_play);
        pause = (Button) view.findViewById(R.id.button_pause);
        stop = (Button) view.findViewById(R.id.button_stop);

        play.setOnTouchListener(mTouchEvent);
        pause.setOnTouchListener(mTouchEvent);
        stop.setOnTouchListener(mTouchEvent);

        return view;
    }

    View.OnTouchListener mTouchEvent = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {

            Activity_waiting activity_waiting = (Activity_waiting) getActivity();
            Button button_clicked = (Button) view;
            int id = button_clicked.getId();

            if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {

                switch (id) {
                    case R.id.button_play:

                        button_clicked.setBackgroundResource(R.drawable.button_play_on);
                        break;
                    case R.id.button_pause:

                        button_clicked.setBackgroundResource(R.drawable.button_pause_on);
                        break;
                    case R.id.button_stop:

                        button_clicked.setBackgroundResource(R.drawable.button_stop_on);
                        break;
                }
            }
            else if (motionEvent.getAction() == MotionEvent.ACTION_UP) {

                Communicator communicator = Application_communicator.getCommunicator();
                switch (id) {
                    case R.id.button_play:

                        button_clicked.setBackgroundResource(R.drawable.button_play_off);
                        communicator.set_tx(1, (byte)0x01);
                        break;
                    case R.id.button_pause:

                        button_clicked.setBackgroundResource(R.drawable.button_pause_off);
                        communicator.set_tx(1, (byte)0x02);
                        break;
                    case R.id.button_stop:

                        button_clicked.setBackgroundResource(R.drawable.button_stop_off);
                        communicator.set_tx(1, (byte)0x00);
                        activity_waiting.changeFragment_waiting();
                        break;
                }
                communicator.send(communicator.get_tx());
            }
            return true;
        }
    };
}
