package com.sinest.gw_1000.mode.utils;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
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

/**
 * Created by rohbyeongeon on 2016-12-12.
 */

public class CustomSeekBar extends RelativeLayout{

    private static final int TOTAL_DIVISION_COUNT = 100;
    //    private static final int MAX_CLICK_DURATION = 200;
    public CustomSeekBar.OnRangeBarChangeListener onRangeBarChangeListener;
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
    private Context context;
    private RelativeLayout relativeLayout;


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
        relFilterMin = (RelativeLayout) findViewById(R.id.rel_filter_min);
        relFilterMax = (RelativeLayout) findViewById(R.id.rel_filter_max);
        viewThumbMax = findViewById(R.id.oval_thumb_max);
        viewThumbMin = findViewById(R.id.oval_thumb_min);
        viewFilterMain = findViewById(R.id.filter_main_view);
        viewParent = findViewById(R.id.view_filter_parent);

        init();
        viewThumbMin.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) { // 시크 바의 위쪽 Thumb의 터치리스너
                dTopMin = relFilterMin.getY();
                dTopMax = relFilterMax.getY();
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN: //터치되는 순간의 Y좌표를 받아옴
                        startYMin = event.getRawY();
                        // startClickTime = Calendar.getInstance().getTimeInMillis();
                        break;

                    case MotionEvent.ACTION_MOVE: //터치된 상태에서의 움직인 거리를 계산하고 그에맞게 레이아웃 사이즈 변경
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
                        setProgress((int)resultMin, (int)resultMax);
                        break;
                    case MotionEvent.ACTION_UP: //손을 뗄 때 구간(칸)별로 나눠지게 설정
                        ViewGroup.LayoutParams _layoutParams = relFilterMin.getLayoutParams();
                        _layoutParams.height = (int)(resultMin * (heightParent/14) + initialHeightMin);
                        Log.i("BE", "min height : "+_layoutParams.height + ", resultMin : " + resultMin);
                        relFilterMin.setLayoutParams(_layoutParams);
                        setProgress((int)resultMin, (int)resultMax);
                        break;
                    default:
                        return false;
                }
                return true;
            }
        });

        viewThumbMax.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) { //아래쪽 Thumb이며 내용은 위와 동일
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
                        setProgress((int)resultMin, (int)resultMax);
                        getResultMax();
                        break;
                    case MotionEvent.ACTION_UP:
                        ViewGroup.LayoutParams _layoutParams = relFilterMax.getLayoutParams();
                        _layoutParams.height = (int)((14-resultMax) * (heightParent/14) + initialHeightMin) ;
                        Log.i("BE", "max height : "+_layoutParams.height + ", resultMax : " + resultMax);
                        relFilterMax.setLayoutParams(_layoutParams);
                        setProgress((int)resultMin, (int)resultMax);
                        break;
                    default:
                        return false;
                }
                return true;
            }
        });


    }

    private void init() { //thumb의 크기인 initialHeightMin을 설정하고 뷰가 변경될 떄의 값을 실시간으로 받아올수 있게 ViewTressObserver 적용
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
                heightParent = viewParent.getHeight();

            }
        });
    }

    public void getResultMin() { //레이아웃의 높이를 0~14단계로 계산하여 resultMin에 입력
        //Max
        resultMin = Math.floor(14 * (Math.abs(currentHeightMin)-initialHeightMin) / heightParent);
        onRangeBarChangeListener.onRangeBarChange((int) resultMin, (int) resultMax);
    }

    public void getResultMax() { //레이아웃의 높이를 0~14단계로 계산하여 resultMax에 입력
        resultMax = Math.floor(14 * (Math.abs(currentHeightMax)-initialHeightMin) / heightParent);
        resultMax = Math.abs(resultMax - 14);
        onRangeBarChangeListener.onRangeBarChange((int) resultMin, (int) resultMax);
    }
    public void setSectionInit(int min, int max) { //시크바의 프로그래스를 세팅함
        resultMin = min;
        resultMax = max;
        setProgress(min, max); //시크바의 칸부분
        setMinimumProgress(min); //시크바의 thumb_min
        setMaximumProgress(max); //시크바의 thumb_max
    }
    public void setProgress(int minProgress, int maxProgress) //시크바 칸부분 세팅
    {
        int i, resourceId;
        ImageView b;
        for(i=1; i<=14; i++) {
            resourceId = getResources().getIdentifier("seek_block_" + i, "id", "com.sinest.gw_1000");
            b = (ImageView)findViewById(resourceId);
            if(i>minProgress && i<=maxProgress)
                b.setVisibility(VISIBLE);
            else
                b.setVisibility(INVISIBLE);
        }
    }
    public void setMinimumProgress(final int minProgress) { //시크바 thumb_min 부분
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
            onRangeBarChangeListener.onRangeBarChange((int) resultMin, (int) resultMax);
        }
    }

    public void setMaximumProgress(final int maxProgress) { //시크바 thumb_max 부분
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
            onRangeBarChangeListener.onRangeBarChange((int) resultMin, (int) resultMax);
        }
    }

    public interface OnRangeBarChangeListener {
        void onRangeBarChange(int min, int max);
    }
}
