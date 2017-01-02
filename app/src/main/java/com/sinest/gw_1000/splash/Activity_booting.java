package com.sinest.gw_1000.splash;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.sinest.gw_1000.R;
import com.sinest.gw_1000.communication.Communicator;
import com.sinest.gw_1000.management.Application_manager;
import com.sinest.gw_1000.mode.Activity_waiting;
import com.sinest.gw_1000.mode.Activity_waiting_rfid;
import com.sinest.gw_1000.setting.Activity_engine;

public class Activity_booting extends AppCompatActivity {

    private final static int MY_PERMISSIONS_ACCESS_COARSE_LOCATION = 1000;
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
        Application_manager.setFullScreen(this);

        ImageView iv = (ImageView)findViewById(R.id.boot_animation);
        iv.setBackgroundResource(R.drawable.intro_images);

        frameAnimation = (AnimationDrawable) iv.getBackground();
        frameAnimation.start();

        permissionCheck();

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
                            SharedPreferences sharedPreferences = getSharedPreferences(Application_manager.DB_NAME, 0);
                            Intent intent;
                            // RFID 모드 ON
                            if (!sharedPreferences.getBoolean(Application_manager.DB_RFID_ONOFF, false)) {

                                Application_manager.rfid_pass_f = true;
                                intent = new Intent(getApplicationContext(), Activity_waiting.class);
                                Log.i("JW", "Start activity_waiting");
                            }
                            // RFID 모드 OFF
                            else {

                                Application_manager.rfid_pass_f = false;
                                intent = new Intent(getApplicationContext(), Activity_waiting_rfid.class);
                                Log.i("JW", "Start activity_waiting_rfid");
                            }
                            startActivityForResult(intent, REQUEST_CODE_ANOTHER);
                            finish();
                        }
                    } catch (InterruptedException e) {}
                }
            }
        });
        thread.start();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Application_manager.setFullScreen(this);
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
            if(hidden_pattern[1]==1 && hidden_pattern[2]==2 && hidden_pattern[3]==3 && hidden_pattern[4]==4 &&hidden_pattern[5]==1){
                isRun = false;
                Intent intent = new Intent(getApplicationContext(), Activity_engine.class);
                startActivityForResult(intent, REQUEST_CODE_ANOTHER);
                finish();
            }
        }
    }

    public void permissionCheck() {

        // Assume thisActivity is the current activity
        int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION);

        // 권한 없음
        if (permissionCheck == PackageManager.PERMISSION_DENIED) {

            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, MY_PERMISSIONS_ACCESS_COARSE_LOCATION);
        }
        // 권한 있음
        else {

            Application_manager.getCommunicator().getWifiConnector().permission = 1;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_ACCESS_COARSE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    Application_manager.getCommunicator().getWifiConnector().permission = 1;

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Application_manager.getCommunicator().getWifiConnector().permission = 0;
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }
}
