package com.sinest.gw_1000.mode.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextClock;

import com.sinest.gw_1000.R;
import com.sinest.gw_1000.management.Application_manager;

/**
 * Created by Jinwook on 2016-12-14.
 */

public class CustomTextClock extends LinearLayout {

    private Context context;

    private IntentFilter intentFilter;
    private BroadcastReceiver broadcastReceiver;

    private TextClock textClock;

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

        setBroadcastReceiver();
    }

    private void setBroadcastReceiver() {

        intentFilter = new IntentFilter();
        intentFilter.addAction(Intent.ACTION_TIME_TICK);

        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                final String action = intent.getAction();

                if (action.equals(Intent.ACTION_TIME_TICK)) {

                    /*
                    SharedPreferences sharedPreferences = context.getSharedPreferences(Application_manager.NAME_OF_SHARED_PREF, 0);
                    int time_gap = sharedPreferences.getInt(Application_manager.TIME_GAP, 0);
                    //textClock.getText()
                    */
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
