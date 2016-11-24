package com.sinest.gw_1000.splash;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import com.sinest.gw_1000.R;
import com.sinest.gw_1000.communication.Communicator;
import com.sinest.gw_1000.mode.Activity_waiting;
import com.sinest.gw_1000.setting.Activity_engine;

public class Activity_booting extends AppCompatActivity {

    public static final int REQUEST_CODE_ANOTHER = 1001;
    Communicator communicator;

    AnimationDrawable frameAnimation;
    boolean isRun = true;
    int time=0;
    int hidden_pattern[] = new int[6];
    Thread thread;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.intro_animation);

        ImageView iv = (ImageView)findViewById(R.id.boot_animation);
        iv.setBackgroundResource(R.drawable.intro_images);

        frameAnimation = (AnimationDrawable) iv.getBackground();
        frameAnimation.start();


        thread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (isRun) {
                    try {
                        Thread.sleep(1000);
                        time++;
                        //Log.i("TEST", "thread is alive");
                        if (time == 7) {
                            time = 0;
                            isRun = false;
                            frameAnimation.stop();
                            Intent intent = new Intent(getApplicationContext(), Activity_waiting.class);
                            startActivityForResult(intent, REQUEST_CODE_ANOTHER);
                            finish();
                        }
                    } catch (InterruptedException e) {}
                }
            }
        });
        thread.start();
    }
    public void onClicked(View v)
    {
        int resourceId;
        if(hidden_pattern[0]<5) {
            for (int i = 1; i <= 5; i++) {
                resourceId = getResources().getIdentifier("hidden_button_" + i, "id", "com.sinest.gw_1000");
                if (resourceId == v.getId()) {
                    hidden_pattern[++hidden_pattern[0]] = i;
                }
            }
        }
        else{
            if(hidden_pattern[1]==1 && hidden_pattern[2]==2 && hidden_pattern[3]==3 && hidden_pattern[4]==4 &&hidden_pattern[5]==1){
                isRun = false;
                Intent intent = new Intent(getApplicationContext(), Activity_engine.class);
                startActivityForResult(intent, REQUEST_CODE_ANOTHER);
                finish();
            }
        }
    }
}
