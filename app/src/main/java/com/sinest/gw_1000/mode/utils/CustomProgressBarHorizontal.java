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

public class CustomProgressBarHorizontal extends RelativeLayout {
    private int inactiveColor;
    private int activeColor;
    private double widthParent;
    private View viewFilterMain;
    private RelativeLayout relFilterMin, relFilterMax;
    private float startYMin, startYMax;
    private float movedYMin, movedYMax;
    private int initialWidthMin;
    private float dTopMin, dTopMax;
    private int currentWidthMin, currentWidthMax;
    private double resultMin = 0.0;
    private double resultMax = 14.0;
    private View viewParent;
    private Context context;
    private RelativeLayout relativeLayout;
    private View viewInActiveTop, viewInActiveBottom;

    public CustomProgressBarHorizontal(Context context) {
        super(context);
        this.context = context;
        initialize(context);
    }

    public CustomProgressBarHorizontal(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        TypedArray a = context.obtainStyledAttributes(attrs, com.opalox.rangebarvertical.R.styleable.RangeBarVertical, 0, 0);
        System.out.println(a.getIndexCount());
        activeColor = a.getColor(com.opalox.rangebarvertical.R.styleable.RangeBarVertical_activeColor, Color.parseColor("#00FFFF"));
        inactiveColor = a.getColor(com.opalox.rangebarvertical.R.styleable.RangeBarVertical_inactiveColor, Color.parseColor("#808080"));
        a.recycle();
        initialize(context);
    }

    public CustomProgressBarHorizontal(Context context, AttributeSet attrs, int defStyleAttr) {
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
        inflate(context, R.layout.layout_custom_progress_bar_horizontal, this);
        relativeLayout = (RelativeLayout) findViewById(R.id.custom_progress_bar_horizontal);
        viewFilterMain = findViewById(R.id.progress_main_view_horizontal);
        relFilterMin = (RelativeLayout) findViewById(R.id.rel_progress_min_horizontal);
        relFilterMax = (RelativeLayout) findViewById(R.id.rel_progress_max_horizontal);
        viewParent = findViewById(R.id.progress_filter_parent_horizontal);
        viewInActiveTop = findViewById(R.id.progress_inactive_line_top_horizontal);
        viewInActiveBottom = findViewById(R.id.progress_inactive_line_bottom_horizontal);

        init();
    }
    private void init() {
        //ViewCompat.setElevation(tvFilterMin, 14f);
        viewInActiveBottom.setBackgroundColor(inactiveColor);
        viewInActiveTop.setBackgroundColor(inactiveColor);
        initialWidthMin = (int) convertDpToPixel(30, context); //InitialHeightMin이 뭐지??
        final ViewTreeObserver viewTreeObserver = relativeLayout.getViewTreeObserver();
        viewTreeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            public void onGlobalLayout() {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    viewParent.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                } else {
                    viewParent.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                }
                currentWidthMin = relFilterMin.getWidth();
                System.out.println("viewParentGetWidth:" + viewParent.getWidth());
                widthParent = viewParent.getWidth() - 2 * initialWidthMin;

            }
        });
    }

    public void setSectionInit(int min, int max) {
        resultMin = min;
        resultMax = max;
        setMinimumProgress(min);
        setMaximumProgress(max);
    }
    public void getResultMin() {
        //Maxr
        resultMin = Math.floor(14 * (Math.abs(currentWidthMin - initialWidthMin)) / widthParent);
    }

    public void getResultMax() {
        resultMax = Math.floor(14 * (Math.abs(currentWidthMax - initialWidthMin)) / widthParent);
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
                    currentWidthMin = ((minProgress * (viewParent.getWidth() - 2 * initialWidthMin) / 14));
                    ViewGroup.LayoutParams layoutParams = relFilterMin.getLayoutParams();
                    layoutParams.width = currentWidthMin;
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
                    currentWidthMax = ((Math.abs(maxProgress - 14) * (viewParent.getWidth() - 2 * initialWidthMin) / 14));
                    ViewGroup.LayoutParams layoutParams = relFilterMax.getLayoutParams();
                    layoutParams.width = currentWidthMax;
                    relFilterMax.setLayoutParams(layoutParams);
                    relFilterMax.bringToFront();
                }
            });
        }
    }
}
