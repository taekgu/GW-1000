package com.sinest.gw_1000.mode.utils;

import android.content.Context;
import android.content.res.Resources;
import android.os.Build;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.RelativeLayout;

import com.sinest.gw_1000.R;

public class CustomProgressBarBlock extends RelativeLayout {
    private RelativeLayout relFilter;
    private int currentWidth;
    private View viewParent;
    private View mainView;
    private Context context;
    private RelativeLayout relativeLayout;

    public CustomProgressBarBlock(Context context) {
        super(context);
        this.context = context;
        initialize(context);
    }

    public CustomProgressBarBlock(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        initialize(context);
    }

    public static float convertDpToPixel(float dp, Context context) {
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        return dp * ((float) metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT);
    }
    private void initialize(Context context) {
        inflate(context, R.layout.layout_custom_progress_bar_block, this);
        relativeLayout = (RelativeLayout) findViewById(R.id.custom_progress_bar_block);
        viewParent = findViewById(R.id.progress_filter_parent_block);
        relFilter = (RelativeLayout) findViewById(R.id.rel_progress_block);
        mainView = findViewById(R.id.progress_main_view_block);
    }
    private void init() {
        //ViewCompat.setElevation(tvFilterMin, 14f);
        final ViewTreeObserver viewTreeObserver = relativeLayout.getViewTreeObserver();
        viewTreeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            public void onGlobalLayout() {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    viewParent.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                } else {
                    viewParent.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                }
                currentWidth = relFilter.getWidth();
            }
        });
    }
    public void setProgress(final int loc) {
        viewParent.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    viewParent.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                } else {
                    viewParent.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                }
                currentWidth = ((loc * (viewParent.getWidth()) / 14));
                if(loc==1)
                    mainView.setBackgroundResource(R.drawable.bar_min);
                else if(loc==14)
                    mainView.setBackgroundResource(R.drawable.bar_max);
                else
                    mainView.setBackgroundResource(R.drawable.bar_base_horizontal);
                ViewGroup.LayoutParams layoutParams = relFilter.getLayoutParams();
                layoutParams.width = currentWidth;
                relFilter.setLayoutParams(layoutParams);
            }
        });
    }
}

