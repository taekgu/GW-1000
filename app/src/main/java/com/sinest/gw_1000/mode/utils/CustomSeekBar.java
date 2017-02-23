package com.sinest.gw_1000.mode.utils;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.os.Build;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sinest.gw_1000.R;

/**
 * Created by rohbyeongeon on 2016-12-12.
 */

public class CustomSeekBar extends RelativeLayout{

    private static final int TOTAL_DIVISION_COUNT = 100;
    //    private static final int MAX_CLICK_DURATION = 200;
    public CustomSeekBar.OnRangeBarChangeListener onRangeBarChangeListener;
    private int inactiveColor;
    private int activeColor;
    private double heightParent;
    private View viewFilterMain, viewThumbMin, viewThumbMax;
    private RelativeLayout relFilterMin, relFilterMax;
    private float startYMin, startYMax;
    private float movedYMin, movedYMax;
    private int initialHeightMin;
    private float dTopMin, dTopMax;
    private int currentHeightMin, currentHeightMax;
    private double resultMin = 0.0;
    private double resultMax = 14.0;
    private View viewParent;
    private TextView tvFilterMin, tvFilterMax;
    private Context context;
    private long startClickTime;
    private RelativeLayout relativeLayout;
    private int minRange = 0, maxRange = 100;
    private View viewInActiveTop, viewInActiveBottom;

    public CustomSeekBar(Context context) {
        super(context);
        this.context = context;
        initialize(context);
    }

    public CustomSeekBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        TypedArray a = context.obtainStyledAttributes(attrs, com.opalox.rangebarvertical.R.styleable.RangeBarVertical, 0, 0);
        System.out.println(a.getIndexCount());
        activeColor = a.getColor(com.opalox.rangebarvertical.R.styleable.RangeBarVertical_activeColor, Color.parseColor("#00FFFF"));
        inactiveColor = a.getColor(com.opalox.rangebarvertical.R.styleable.RangeBarVertical_inactiveColor, Color.parseColor("#808080"));
        a.recycle();
        initialize(context);
    }

    public CustomSeekBar(Context context, AttributeSet attrs, int defStyleAttr) {
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
        inflate(context, R.layout.filter_enquiry_age_fragment_temp, this);
        onRangeBarChangeListener = (CustomSeekBar.OnRangeBarChangeListener) context;
        onRangeBarChangeListener.onRangeBarChange((int) resultMin, (int) resultMax);
        relativeLayout = (RelativeLayout) findViewById(R.id.rel_main);
        tvFilterMin = (TextView) findViewById(R.id.tv_filter_min);
        tvFilterMax = (TextView) findViewById(R.id.tv_filter_max);
        relFilterMin = (RelativeLayout) findViewById(R.id.rel_filter_min);
        relFilterMax = (RelativeLayout) findViewById(R.id.rel_filter_max);
        viewThumbMax = findViewById(R.id.oval_thumb_max);
        viewThumbMin = findViewById(R.id.oval_thumb_min);
        viewFilterMain = findViewById(R.id.filter_main_view);
        viewParent = findViewById(R.id.view_filter_parent);
        viewInActiveTop = findViewById(R.id.view_inactive_line_top);
        viewInActiveBottom = findViewById(R.id.view_inactive_line_bottom);


        init();

        viewThumbMin.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                dTopMin = relFilterMin.getY();
                dTopMax = relFilterMax.getY();
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        startYMin = event.getRawY();
                        // startClickTime = Calendar.getInstance().getTimeInMillis();
                        break;

                    case MotionEvent.ACTION_MOVE:
                        movedYMin = event.getRawY() - startYMin;
                        startYMin = event.getRawY();
                        if (relFilterMin.getHeight() + movedYMin <= initialHeightMin || dTopMin + relFilterMin.getHeight()+ movedYMin >= dTopMax - (heightParent/14*2)+10) {
                            Log.i("debug", "dTopMax : "+dTopMax);
                            currentHeightMin = relFilterMin.getHeight();
                            getResultMin();
                            break;
                        }
                        ViewGroup.LayoutParams layoutParams = relFilterMin.getLayoutParams();
                        layoutParams.height += movedYMin;
                        relFilterMin.setLayoutParams(layoutParams);
                        dTopMin = relFilterMin.getY();
                        currentHeightMin = relFilterMin.getHeight();
                        getResultMin();
                        break;
                    case MotionEvent.ACTION_UP:
                        ViewGroup.LayoutParams _layoutParams = relFilterMin.getLayoutParams();
                        _layoutParams.height = (int)(resultMin * (heightParent/14) + initialHeightMin);
                        Log.i("BE", "min height : "+_layoutParams.height + ", resultMin : " + resultMin);
                        relFilterMin.setLayoutParams(_layoutParams);
                        break;
                    default:
                        return false;
                }
                return true;
            }
        });

        viewThumbMax.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                dTopMin = relFilterMin.getY();
                dTopMax = relFilterMax.getY();
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        startYMax = event.getRawY();
                        break;
                    case MotionEvent.ACTION_MOVE:

                        movedYMax = event.getRawY() - startYMax;
                        startYMax = event.getRawY();

                        if (relFilterMax.getHeight() - movedYMax <= initialHeightMin || relFilterMax.getY() + movedYMax <= currentHeightMin + dTopMin + (heightParent/14*2)-10) {
                            currentHeightMax = relFilterMax.getHeight();
                            getResultMax();
                            break;
                        }
                        ViewGroup.LayoutParams layoutParams = relFilterMax.getLayoutParams();
                        layoutParams.height -= movedYMax;
                        relFilterMax.setLayoutParams(layoutParams);
                        dTopMax = relFilterMax.getY();
                        currentHeightMax = relFilterMax.getHeight();
                        getResultMax();
                        break;
                    case MotionEvent.ACTION_UP:
                        ViewGroup.LayoutParams _layoutParams = relFilterMax.getLayoutParams();
                        _layoutParams.height = (int)((14-resultMax) * (heightParent/14) + initialHeightMin) ;
                        Log.i("BE", "max height : "+_layoutParams.height + ", resultMax : " + resultMax);
                        relFilterMax.setLayoutParams(_layoutParams);
                        break;
                    default:
                        return false;
                }
                return true;
            }
        });


    }

    private void init() {
        ViewCompat.setElevation(tvFilterMin, 14f);
        viewInActiveBottom.setBackgroundColor(inactiveColor);
        viewInActiveTop.setBackgroundColor(inactiveColor);
        initialHeightMin = (int) convertDpToPixel(20, context);
        final ViewTreeObserver viewTreeObserver = relativeLayout.getViewTreeObserver();
        //  if (viewTreeObserver.isAlive())
        viewTreeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            public void onGlobalLayout() {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    viewParent.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                } else {
                    viewParent.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                }
                dTopMin = relFilterMin.getY();
                dTopMax = relFilterMax.getY();
                currentHeightMin = relFilterMin.getHeight();
                System.out.println("viewParentGetHeight_rr:" + viewParent.getHeight());
                System.out.println("initialHeightMin_rr:" + initialHeightMin);
                heightParent = viewParent.getHeight();

            }
        });


    }

    public void getResultMin() {
        //Max
        resultMin = Math.floor(14 * (Math.abs(currentHeightMin)-initialHeightMin) / heightParent);
        tvFilterMin.setText((int) resultMin + "");
        onRangeBarChangeListener.onRangeBarChange((int) resultMin, (int) resultMax);

    }

    public void getResultMax() {
        resultMax = Math.floor(14 * (Math.abs(currentHeightMax)-initialHeightMin) / heightParent);
        resultMax = Math.abs(resultMax - 14);
        tvFilterMax.setText(((int) resultMax + ""));
        onRangeBarChangeListener.onRangeBarChange((int) resultMin, (int) resultMax);
    }
    public void setSectionInit(int min, int max) {
        resultMin = min;
        resultMax = max;
        setMinimumProgress(min);
        setMaximumProgress(max);
    }
    public int getMinimumProgress() {
        return (int) resultMin;
    }
    public void setMinimumProgress(final int minProgress) {
        if (minProgress >= 0 && minProgress <= resultMax) {
            resultMin = minProgress;
            viewParent.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                        viewParent.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    } else {
                        viewParent.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                    }
                    currentHeightMin = (minProgress * ((viewParent.getHeight())) / 14) + initialHeightMin;
                    ViewGroup.LayoutParams layoutParams = relFilterMin.getLayoutParams();
                    layoutParams.height = currentHeightMin;
                    relFilterMin.setLayoutParams(layoutParams);
                }
            });
            tvFilterMin.setText((int) resultMin + "");
            onRangeBarChangeListener.onRangeBarChange((int) resultMin, (int) resultMax);
        }
    }

    public int getMaximumProgress() {
        return (int) resultMax;
    }

    public void setMaximumProgress(final int maxProgress) {
        if (maxProgress >= resultMin && maxProgress <= 14) {
            resultMax = maxProgress;
            viewParent.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                        viewParent.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    } else {
                        viewParent.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                    }
                    currentHeightMax = (Math.abs(maxProgress - 14) * (viewParent.getHeight())) / 14 + initialHeightMin;
                    ViewGroup.LayoutParams layoutParams = relFilterMax.getLayoutParams();
                    layoutParams.height = currentHeightMax;
                    relFilterMax.setLayoutParams(layoutParams);
                }
            });
            tvFilterMax.setText((int) resultMax + "");

            onRangeBarChangeListener.onRangeBarChange((int) resultMin, (int) resultMax);
        }
    }

    public interface OnRangeBarChangeListener {
        void onRangeBarChange(int min, int max);
    }
}
