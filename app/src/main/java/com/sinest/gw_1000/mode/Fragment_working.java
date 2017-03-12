package com.sinest.gw_1000.mode;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;

import com.sinest.gw_1000.R;
import com.sinest.gw_1000.communication.Communicator;
import com.sinest.gw_1000.management.Application_manager;

/**
 * Created by Jinwook on 2016-11-21.
 */

public class Fragment_working extends Fragment {

    private int modeNum = -1;
    Button play, stop;
    private int state = 0; // 0:pause, 1:play
    private int parent = -1; // 0:waiting, 1:waiting_rfid

    private int time_m_left;
    private int time_s;

    private Thread thread_timer;
    private boolean isRun = false;
    private boolean isPause = false;
    private boolean isAlive = false;

    // 대기 화면으로 돌아가기 전 모터원점복귀 대기
    private Handler handler = null;

    public Fragment_working() {

    }

    public void init (int _modeNum, final int _time, final int _parent) {

        this.modeNum = _modeNum;
        this.time_m_left = _time;
        this.time_s = 0;
        this.parent = _parent;

        handler = new Handler() {

            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);

                if (parent == 0) {

                    Activity_waiting activity_waiting = (Activity_waiting) getActivity();
                    //activity_waiting.changeFragment_waiting();
                    activity_waiting.wait_motor_back();
                }
                else {

                    Activity_waiting_rfid activity_waiting_rfid = (Activity_waiting_rfid) getActivity();
                    activity_waiting_rfid.wait_motor_back();
                }
            }
        };

        thread_timer = new Thread(new Runnable() {
            @Override
            public void run() {

                isAlive = true;

                while (isRun) {

                    while(isPause);

                    try {

                        Thread.sleep(1000);
                        time_s++;
                        Log.i("JW", "time - " + time_s);

                        if (time_s == 60) {

                            time_s = 0;
                            setTime_m_left(time_m_left-1);
                            //time_m_left--;

                            if (parent == 0) {

                                Activity_waiting activity_waiting = (Activity_waiting) getActivity();
                                activity_waiting.setTimeLeft(time_m_left);
                            }
                            else {

                                Activity_waiting_rfid activity_waiting_rfid = (Activity_waiting_rfid) getActivity();
                                activity_waiting_rfid.setTimeLeft(time_m_left);
                            }

                            if (time_m_left == 0) {

                                isRun = false;
                                break;
                            }
                        }
                    }
                    catch (Exception e) {

                        Log.i("JW", "Exception was caught on timer thread");
                    }
                }
                if (time_m_left == 0) {

                    Log.i("JW", "치료 종료");
                    Application_manager.getSoundManager().play(Application_manager.m_language, 2);
                    handler.sendEmptyMessage(0);
                }
                isAlive = false;
            }
        });
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_working, container, false);

        Log.i("JW_LIFECYCLE", "Fragment_working - onCreateView");
        state = 1;

        play = (Button) view.findViewById(R.id.button_play);
        stop = (Button) view.findViewById(R.id.button_stop);

        play.setOnTouchListener(mTouchEvent);
        stop.setOnTouchListener(mTouchEvent);

        isRun = true;
        if (thread_timer != null) {

            thread_timer.start();
        }

        return view;
    }

    public boolean getIsAlive() {

        return isAlive;
    }

    synchronized public void setTime_m_left(int val) {

        time_m_left = val;
        Log.i("JW", "남은 동작 시간: " + time_m_left + "분");
    }

    View.OnTouchListener mTouchEvent = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            Application_manager.set_m_start_sleep(0);

            Button button_clicked = (Button) view;
            int id = button_clicked.getId();

            if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {

                switch (id) {
                    case R.id.button_play:

                        // play 중이면
                        if (state == 1) {

                            button_clicked.setBackgroundResource(R.drawable.button_pause_on);
                        }
                        else {

                            button_clicked.setBackgroundResource(R.drawable.button_play_on);
                        }
                        break;
                    case R.id.button_stop:

                        button_clicked.setBackgroundResource(R.drawable.button_stop_on);
                        break;
                }
            }
            else if (motionEvent.getAction() == MotionEvent.ACTION_UP) {

                Communicator communicator = Application_manager.getCommunicator();
                switch (id) {
                    case R.id.button_play:

                        // play 중이면
                        if (state == 1) {

                            state = 0;
                            //isPause = true;
                            button_clicked.setBackgroundResource(R.drawable.button_play_off);
                            communicator.set_tx(1, (byte)0x02);
                        //    Application_manager.getSoundManager().play(Application_manager.ID_LANG_SOUND[Application_manager.m_language][1]);
                        }
                        // pause 중이면
                        else {

                            state = 1;
                            isPause = false;
                            button_clicked.setBackgroundResource(R.drawable.button_pause_off);
                            communicator.set_tx(1, (byte)0x01);
                        //    Application_manager.getSoundManager().play(Application_manager.ID_LANG_SOUND[Application_manager.m_language][0]);
                        }
                        break;
                    case R.id.button_stop:

                        button_clicked.setBackgroundResource(R.drawable.button_stop_off);

                        if (Application_manager.getSoundManager().play(Application_manager.m_language, 1) == 0) {
                            isRun = false;
                            isPause = false;
                            state = 1;
                            communicator.set_tx(1, (byte) 0x00);

                            handler.sendEmptyMessage(0);
                        }
                        break;
                }
            }
            return true;
        }
    };

    @Override
    public void onResume() {
        super.onResume();

        Log.i("JW_LIFECYCLE", "Fragment_working - onResume");
        // 슬립 모드 동작 재시작
        Application_manager.setSleep_f(0, false);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        Log.i("JW_LIFECYCLE", "Fragment_working - onDestroy");
    }

    @Override
    public void onDetach() {
        super.onDetach();

        Log.i("JW_LIFECYCLE", "Fragment_working - onDetach");
    }
}
