package com.sinest.gw_1000.mode.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.icu.text.DateFormatSymbols;
import android.icu.text.SimpleDateFormat;
import android.icu.util.Calendar;
import android.os.Handler;
import android.os.Message;
import android.text.format.DateFormat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextClock;

import com.sinest.gw_1000.R;
import com.sinest.gw_1000.management.Application_manager;

import static com.sinest.gw_1000.management.Application_manager.getTime_gap_mm;
import static com.sinest.gw_1000.management.Application_manager.getTime_gap_tt;

/**
 * Created by Jinwook on 2016-12-14.
 */

public class CustomTextClock extends LinearLayout {

    private Context context;

    private IntentFilter intentFilter;
    private BroadcastReceiver broadcastReceiver;

    private TextClock textClock;

    private int cnt_t;
    private int cnt_m;
    private String p_time;

    private String doTime = "00:00";


    public CustomTextClock(Context context) {
        super(context);

        init(context);
        inflate(context, R.layout.layout_customtextclock, this);
    }

    public CustomTextClock(Context context, AttributeSet attrs) {
        super(context, attrs);

        init(context);
        inflate(context, R.layout.layout_customtextclock, this);
    }

    public CustomTextClock(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        init(context);
        inflate(context, R.layout.layout_customtextclock, this);
    }


    public void init(Context _context) {

        Log.i("JW", "Initialize custom textClock");

        this.context = _context;

        inflate(context, R.layout.layout_customtextclock, this);

        // 폰트 설정
        textClock = (TextClock) findViewById(R.id.custom_textClock);
        Typeface tf = Typeface.createFromAsset(context.getAssets(), "fonts/digital.ttf");
        textClock.setTypeface(tf);
        doInit_time();
        setBroadcastReceiver();
    }

    public void doInit_time()
    {
        String p_time = Application_manager.getText();
        String g_time = Application_manager.m_gap_clock;
        boolean t_f = Application_manager.m_gap_clock_f;

        Log.v("ss","p_time : "+p_time);
        Log.v("ss","g_time : "+g_time);

        String aa = p_time.substring(0,2);
        String bb = p_time.substring(3,5);
        int p_time_t = Integer.parseInt(aa);
        int p_time_m = Integer.parseInt(bb);

        String ga = g_time.substring(0,2);
        String gb = g_time.substring(3,5);
        int g_time_t = Integer.parseInt(ga);
        int g_time_m = Integer.parseInt(gb);

        int t = 0;
        int m = 0;

        // +
        if(t_f){
            t = p_time_t + g_time_t;
            m = p_time_m + g_time_m;
        // -
        }else{
            if(p_time_t > g_time_t & p_time_m < g_time_m){
                t = (p_time_t - g_time_t) - 1;
                m = 60 + p_time_m - g_time_m;
            }
            else{
                t = p_time_t - g_time_t;
                m = p_time_m - g_time_m;
            }
        }

        if(m > 60){
            t = t + 1;
            m = m - 60;
        }
        if(t > 24 ){
            t = t - 24;
        }

        String doTime_t;
        String doTime_m;

        if(t < 10){
            doTime_t = "0"+String.valueOf(t);
        }
        else{
            doTime_t = String.valueOf(t);
        }

        if(m < 10){
            doTime_m = "0"+String.valueOf(m);
        }
        else{
            doTime_m = String.valueOf(m);
        }

        doTime = doTime_t+":"+doTime_m;

        Log.v("ss","doTime : "+ doTime);

        textClock.setText(doTime);
    }

    private void setBroadcastReceiver() {

        intentFilter = new IntentFilter();
        intentFilter.addAction(Intent.ACTION_TIME_TICK);

        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                final String action = intent.getAction();

                if (action.equals(Intent.ACTION_TIME_TICK)) {
                    doInit_time();
                }
            }
        };
    }

    public void registReceiver() {

        Log.i("JW", "Register ACTION_TIME_TICK receiver");
        context.registerReceiver(broadcastReceiver, intentFilter);
    }

    public void unregistReceiver() {

        Log.i("JW", "Unregister ACTION_TIME_TICK receiver");
        context.unregisterReceiver(broadcastReceiver);
    }

}
