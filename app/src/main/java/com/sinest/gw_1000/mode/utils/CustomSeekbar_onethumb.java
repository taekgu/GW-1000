package com.sinest.gw_1000.mode.utils;

import android.content.Context;
import android.content.res.Resources;
import android.os.Build;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.sinest.gw_1000.R;
import com.sinest.gw_1000.management.Application_manager;

/**
 * Created by rohbyeongeon on 2017-03-02.
 */

public class CustomSeekbar_onethumb extends RelativeLayout{

    private static final int TOTAL_DIVISION_COUNT = 100;
    //    private static final int MAX_CLICK_DURATION = 200;
    private double widthParent;
    private View  viewFilterMain, viewThumbMin;
    private RelativeLayout relFilterMin;
    private float startXMin;
    private float movedXMin;
    private int initialWidthMin;
    private float dLeftMin, dRightMax;
    private int currentWidthMin;
    private double current_loc = 0.0;
    private View viewParent;
    private Context context;
    private RelativeLayout relativeLayout;

    public CustomSeekbar_onethumb(Context context) {
        super(context);
        this.context = context;
        initialize(context);
    }

    public CustomSeekbar_onethumb(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        initialize(context);
    }

    public CustomSeekbar_onethumb(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        initialize(context);
    }

    public static float convertDpToPixel(float dp, Context context) {
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        return dp * ((float) metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT);
    }

    private void initialize(Context context) {
        inflate(context, R.layout.layout_custom_seekbar_onethumb, this);
        relativeLayout = (RelativeLayout) findViewById(R.id.main_seekbar);
        relFilterMin = (RelativeLayout) findViewById(R.id.main_seekbar_min);
        viewThumbMin = findViewById(R.id.main_seekbar_thumb);
        viewFilterMain = findViewById(R.id.main_seekbar_view);
        viewParent = findViewById(R.id.main_seekbar_parent);

        init();
        viewThumbMin.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                dLeftMin = relFilterMin.getX();
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        startXMin = event.getRawX();
                        break;

                    case MotionEvent.ACTION_MOVE:
                        movedXMin = event.getRawX() - startXMin;
                        startXMin = event.getRawX();
                        if (relFilterMin.getWidth() + movedXMin <= initialWidthMin || dLeftMin + relFilterMin.getWidth()+ movedXMin >= dRightMax+initialWidthMin+5) {
                            currentWidthMin = relFilterMin.getWidth();
                            getCurrentLoc();
                            break;
                        }
                        ViewGroup.LayoutParams layoutParams = relFilterMin.getLayoutParams();
                        layoutParams.width += movedXMin;
                        relFilterMin.setLayoutParams(layoutParams);
                        dLeftMin = relFilterMin.getX();
                        currentWidthMin = relFilterMin.getWidth();
                        getCurrentLoc();
                        Log.i("seekbar_debug", "current_loc : " + current_loc);
                        setProgress((int)current_loc);
                        break;
                    case MotionEvent.ACTION_UP:
                        ViewGroup.LayoutParams _layoutParams = relFilterMin.getLayoutParams();
                        _layoutParams.width = (int)(current_loc * (widthParent/10) + initialWidthMin);
                        relFilterMin.setLayoutParams(_layoutParams);
                        Log.i("seekbar_debug_up", "current_loc : " + current_loc);
                        Application_manager.set_m_volume((int)current_loc*10);
                        Application_manager.getSoundManager().setVolume_alarm((int)current_loc*10);
                        setProgress((int)current_loc);
                        break;
                    default:
                        return false;
                }
                return true;
            }
        });
}
    private void init() {
        initialWidthMin = (int) convertDpToPixel(30, context);
        final ViewTreeObserver viewTreeObserver = relativeLayout.getViewTreeObserver();
        //  if (viewTreeObserver.isAlive())
        viewTreeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            public void onGlobalLayout() {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    viewParent.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                } else {
                    viewParent.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                }
                dLeftMin = relFilterMin.getX();
                dRightMax = dLeftMin+viewParent.getWidth();
                currentWidthMin = relFilterMin.getWidth();
                widthParent = viewParent.getWidth();

                Log.i("seekbar_debug", "dLeftMin : " + dLeftMin + " dRightMax : " + dRightMax);
            }
        });
    }
    public void getCurrentLoc() {
        //Max
        current_loc = Math.floor(10 * ((currentWidthMin - initialWidthMin + 10) / widthParent));
    }
    public void setProgress(int current_loc)
    {
        int i, resourceId;
        ImageView b;
        for(i=1; i<=10; i++) {
            resourceId = getResources().getIdentifier("main_seek_block_" + i, "id", "com.sinest.gw_1000");
            b = (ImageView)findViewById(resourceId);
            if(i<=current_loc)
                b.setVisibility(VISIBLE);
            else
                b.setVisibility(INVISIBLE);
        }
    }
    public void setCurrentLoc(final int progress) {
        current_loc = progress;
        viewParent.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                        viewParent.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    } else {
                        viewParent.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                    }
                    currentWidthMin = (progress * (viewParent.getWidth()) / 10) + initialWidthMin;
                    ViewGroup.LayoutParams layoutParams = relFilterMin.getLayoutParams();
                    layoutParams.width = currentWidthMin;
                    relFilterMin.setLayoutParams(layoutParams);
                    setProgress(progress);
                }
            });
        }
}
