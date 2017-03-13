package com.sinest.gw_1000.management;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;

/**
 * Created by Administrator on 2017-03-14.
 */

public class CustomProgressDialog extends ProgressDialog {

    public CustomProgressDialog(Context context) {
        super(context);

        // 언어별 메시지 설정
        int language = Application_manager.m_language;
        String msg = "";

        // 한
        if (language == 0) {

            msg = "잠시만 기다려 주십시오";
        }
        // 영
        else if (language == 1) {

            msg = "Please wait a moment";
        }
        // 중
        else if (language == 2) {

            msg = "请稍等一会儿";
        }

        this.setCancelable(false);
        this.setIndeterminate(true);
        this.setMessage(msg);
    }

    public void showDialog(final Handler handler) {

        if (!this.isShowing()) {

            // 포커스 해제
            this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE, WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE);
            // 디스플레이
            this.show();
            // 전체화면
            this.getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY

            );
            // 포커스 설정
            this.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE);

            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {

                    while (true) {

                        try {

                            Thread.sleep(1000);

                            if (!Application_manager.getIsWaiting_init()) {

                                break;
                            }

                        } catch (Exception e) {

                            Log.i("JW", "예외 발생: progress dialog 동작");
                        }
                    }
                    dismissDialog();
                    if (handler != null) {

                        handler.sendEmptyMessage(0);
                    }
                }
            });
            thread.start();
        }
    }

    public void dismissDialog() {

        if (this.isShowing()) {

            this.dismiss();
        }
    }
}
