package com.sinest.gw_1000.mode.utils;

import android.content.Context;
import android.content.res.Resources;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.sinest.gw_1000.R;

/**
 * Created by Jinwook on 2016-12-19.
 */

public class CustomSeekBar_made_by_Jinwook extends RelativeLayout {

    private Context context;

    private RelativeLayout layout_top, layout_bottom;
    private ImageView thumb_top, thumb_bottom;
//    private ImageView view_inactive_top, view_inactive_bottom;
    private ImageView view_progress;

    private int init_val; // 초기 thumb 레이아웃 크기(최소 크기 = 20dp)
    private int val_top, val_bottom; // 현재 좌표값
    private int val_max = 0;

    private final static int NUM_OF_SECTION = 14; // 몇 개 구간으로 나눌지 0 ~ DIV-1
    private int divider;
    private int real_top, real_bottom; // 실제 값

    private float start, moved; // 시작 좌표, 이동 좌표

    public CustomSeekBar_made_by_Jinwook(Context context) {
        super(context);
        this.context = context;
        init();
    }

    public CustomSeekBar_made_by_Jinwook(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        init();
    }

    public CustomSeekBar_made_by_Jinwook(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        init();
    }

    private float convertDpToPixel(float dp, Context context) {
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        return dp * ((float) metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT);
    }

    private void init() {

        inflate(context, R.layout.layout_customseekbar_made_by_jinwook, this);

        layout_top = (RelativeLayout) findViewById(R.id.layout_top);
        layout_bottom = (RelativeLayout) findViewById(R.id.layout_bottom);

        thumb_top = (ImageView) findViewById(R.id.thumb_max);
        thumb_bottom = (ImageView) findViewById(R.id.thumb_min);

        view_progress = (ImageView) findViewById(R.id.view_progress);
        /*
        view_inactive_top = (ImageView) findViewById(R.id.view_inactive_top);
        view_inactive_bottom = (ImageView) findViewById(R.id.view_inactive_bottom);
*/
        init_val = (int) convertDpToPixel(20, context);
        val_top = init_val;
        val_bottom = init_val;
        Log.i("JW", "thumb 기본 크기 = " + init_val);

        view_progress.post(new Runnable() {
            @Override
            public void run() {

                val_max = view_progress.getHeight();
                Log.i("JW", "시크바 최대값 = " + val_max);
                divider = val_max / NUM_OF_SECTION;
            }
        });

        thumb_top.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {

                View layout_view = layout_top;
                ViewGroup.LayoutParams layoutParams = layout_view.getLayoutParams();

                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        start = motionEvent.getRawY();
                        break;
                    case MotionEvent.ACTION_UP:

                        /*
                        layout_view = layout_top;
                        layoutParams = layout_view.getLayoutParams();
                        layoutParams.height = (val_max - (real_top * divider)) + init_val;
                        layout_view.setLayoutParams(layoutParams);
                        */
                        break;
                    case MotionEvent.ACTION_MOVE:

                        moved = motionEvent.getRawY() - start;
                        start = motionEvent.getRawY();

                        layout_view = layout_top;
                        layoutParams = layout_view.getLayoutParams();
                        if (layoutParams.height + moved < init_val) {

                            layoutParams.height = init_val;
                        }
                        else if (layoutParams.height + moved + val_bottom > val_max) {

                            layoutParams.height = val_max - val_bottom;
                        }
                        else {

                            layoutParams.height += moved;
                        }
                        layout_view.setLayoutParams(layoutParams);
                        val_top = layout_top.getHeight();
                        //Log.i("JW", "val_top = " + val_top);
                        real_top = (val_max - (val_top - init_val)) / divider;
                        Log.i("JW", "최대 = " + real_top);
                        break;
                    default:
                        return false;
                }
                return true;
            }
        });

        thumb_bottom.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {

                View layout_view = layout_bottom;
                ViewGroup.LayoutParams layoutParams = layout_view.getLayoutParams();

                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        start = motionEvent.getRawY();
                        break;
                    case MotionEvent.ACTION_UP:

                        /*
                        layout_view = layout_bottom;
                        layoutParams = layout_view.getLayoutParams();
                        layoutParams.height = real_bottom * divider + init_val;
                        layout_view.setLayoutParams(layoutParams);
                        */
                        break;
                    case MotionEvent.ACTION_MOVE:

                        moved = start - motionEvent.getRawY();
                        start = motionEvent.getRawY();

                        layout_view = layout_bottom;
                        layoutParams = layout_view.getLayoutParams();
                        if (layoutParams.height + moved < init_val) {

                            layoutParams.height = init_val;
                        }
                        else if (layoutParams.height + moved + val_top > val_max) {

                            layoutParams.height = val_max - val_top;
                        }
                        else {

                            layoutParams.height += moved;
                        }
                        layout_view.setLayoutParams(layoutParams);
                        val_bottom = layout_bottom.getHeight();
                        //Log.i("JW", "val_bottom = " + val_bottom);
                        real_bottom = (val_bottom - init_val) / divider;
                        Log.i("JW", "최소 = " + real_bottom);
                        break;
                    default:
                        return false;
                }
                return true;
            }
        });
    }
}
