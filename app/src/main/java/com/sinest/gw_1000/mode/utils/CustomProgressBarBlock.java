package com.sinest.gw_1000.mode.utils;

import android.content.Context;
import android.content.res.Resources;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.sinest.gw_1000.R;

public class CustomProgressBarBlock extends RelativeLayout {
    private RelativeLayout relFilter;
    private int currentWidth;
    private View viewParent;
    private Context context;

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
        viewParent = findViewById(R.id.progress_filter_parent_block);
        relFilter = (RelativeLayout) findViewById(R.id.rel_progress_block);
        currentWidth = relFilter.getWidth();
    }
    public void setProgress(final int loc) {
        currentWidth = ((loc * (viewParent.getWidth()) / 14));
        ViewGroup.LayoutParams layoutParams = relFilter.getLayoutParams();
        layoutParams.width = currentWidth;
        relFilter.setLayoutParams(layoutParams);
    }
}

