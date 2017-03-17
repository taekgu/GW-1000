package com.sinest.gw_1000.mode.utils;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.sinest.gw_1000.R;

public class CustomProgressBar extends RelativeLayout {
    private Context context;
    public CustomProgressBar(Context context) {
        super(context);
        this.context = context;
        initialize(context);
    }
    public CustomProgressBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        initialize(context);
    }
    public CustomProgressBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        initialize(context);
    }
    private void initialize(Context context) { //레이아웃 inflate
        inflate(context, R.layout.layout_custom_progress_bar_vertical, this);
    }

    public void setProgress(final int minProgress, final int maxProgress) { //받아온 범위의 이미지를 VISIBLE로 변환, 프로그래스 설정
        int i, resourceId;
        ImageView b;
        for(i=1; i<=14; i++) {
            resourceId = getResources().getIdentifier("block_" + i, "id", "com.sinest.gw_1000");
            b = (ImageView)findViewById(resourceId);
            if(i>minProgress && i<=maxProgress)
                b.setVisibility(VISIBLE);
            else
                b.setVisibility(INVISIBLE);
        }
    }
}
