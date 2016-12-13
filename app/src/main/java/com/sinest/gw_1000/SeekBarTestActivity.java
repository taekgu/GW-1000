package com.sinest.gw_1000;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.sinest.gw_1000.mode.utils.CustomSeekBar;

public class SeekBarTestActivity extends AppCompatActivity implements CustomSeekBar.OnRangeBarChangeListener  {

    private CustomSeekBar rangeBarVertical;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seek_bar_test);
        rangeBarVertical= (CustomSeekBar) findViewById(R.id.timer1);

//        rangeBarVertical.setMinimumProgress(40);
//        rangeBarVertical.setMaximumProgress(70);

        //   tvMin = (TextView) findViewById(R.id.tv_filter_min);
    }

    public void onRangeBarChange(int min, int max) {
    }
}
