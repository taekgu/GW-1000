package com.sinest.gw_1000.mode.utils;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.os.Build;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.RelativeLayout;

import com.sinest.gw_1000.R;

public class CustomProgressBar extends RelativeLayout {
    private int inactiveColor;
    private int activeColor;
    private double heightParent;
    private View viewFilterMain;
    private RelativeLayout relFilterMin, relFilterMax;
    private int initialHeightMin;
    private float dTopMin, dTopMax;
    private int currentHeightMin, currentHeightMax;
    private double resultMin = 0.0;
    private double resultMax = 14.0;
    private View viewParent;
    private Context context;
    private RelativeLayout relativeLayout;
    private View viewInActiveTop, viewInActiveBottom;

    public CustomProgressBar(Context context) {
        super(context);
        this.context = context;
        initialize(context);
    }

    public CustomProgressBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        TypedArray a = context.obtainStyledAttributes(attrs, com.opalox.rangebarvertical.R.styleable.RangeBarVertical, 0, 0);
        System.out.println(a.getIndexCount());
        activeColor = a.getColor(com.opalox.rangebarvertical.R.styleable.RangeBarVertical_activeColor, Color.parseColor("#00FFFF"));
        inactiveColor = a.getColor(com.opalox.rangebarvertical.R.styleable.RangeBarVertical_inactiveColor, Color.parseColor("#808080"));
        a.recycle();
        initialize(context);
    }

    public CustomProgressBar(Context context, AttributeSet attrs, int defStyleAttr) {
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
        inflate(context, R.layout.layout_custom_progress_bar_vertical, this);
        relativeLayout = (RelativeLayout) findViewById(R.id.custom_progress_bar);
        viewFilterMain = findViewById(R.id.progress_main_view);
        relFilterMin = (RelativeLayout) findViewById(R.id.rel_progress_min);
        relFilterMax = (RelativeLayout) findViewById(R.id.rel_progress_max);
        viewParent = findViewById(R.id.progress_filter_parent);
        viewInActiveTop = findViewById(R.id.progress_inactive_line_top);
        viewInActiveBottom = findViewById(R.id.progress_inactive_line_bottom);

        init();
    }
    private void init() {
        //ViewCompat.setElevation(tvFilterMin, 14f);
        viewInActiveBottom.setBackgroundColor(inactiveColor);
        viewInActiveTop.setBackgroundColor(inactiveColor);
        initialHeightMin = (int) convertDpToPixel(30, context); //InitialHeightMin이 뭐지??
        final ViewTreeObserver viewTreeObserver = relativeLayout.getViewTreeObserver();
        viewTreeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            public void onGlobalLayout() {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    viewParent.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                } else {
                    viewParent.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                }
                currentHeightMin = relFilterMin.getHeight();
                System.out.println("viewParentGetHeight:" + viewParent.getHeight());
                heightParent = viewParent.getHeight() - 2 * initialHeightMin;

            }
        });
    }
    public void getResultMin() {
        //Max
        resultMin = Math.floor(14 * (Math.abs(currentHeightMin - initialHeightMin)) / heightParent);
    }

    public void getResultMax() {
        resultMax = Math.floor(14 * (Math.abs(currentHeightMax - initialHeightMin)) / heightParent);
        resultMax = Math.abs(resultMax - 14);
    }
    public int getMinimumProgress() {
        return (int) resultMin;
    }

    public void setMinimumProgress(final int minProgress) {
        if (minProgress >= 0 && minProgress < 14 && minProgress < resultMax) {
            resultMin = minProgress;
            viewParent.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                        viewParent.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    } else {
                        viewParent.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                    }
                    currentHeightMin = ((minProgress * (viewParent.getHeight() - 2 * initialHeightMin) / 14));
                    ViewGroup.LayoutParams layoutParams = relFilterMin.getLayoutParams();
                    layoutParams.height = currentHeightMin;
                    relFilterMin.setLayoutParams(layoutParams);
                }
            });
        }
    }

    public int getMaximumProgress() {
        return (int) resultMax;
    }

    public void setMaximumProgress(final int maxProgress) {
        if (maxProgress >= 0 && maxProgress <= 14 && maxProgress > resultMin) {
            resultMax = maxProgress;
            viewParent.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                        viewParent.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    } else {
                        viewParent.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                    }
                    currentHeightMax = ((Math.abs(maxProgress - 14) * (viewParent.getHeight() - 2 * initialHeightMin) / 14));
                    ViewGroup.LayoutParams layoutParams = relFilterMax.getLayoutParams();
                    layoutParams.height = currentHeightMax;
                    relFilterMax.setLayoutParams(layoutParams);
                    relFilterMax.bringToFront();
                }
            });
        }
    }
}
