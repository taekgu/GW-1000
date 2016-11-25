package com.sinest.gw_1000.management;

import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;
import android.util.Log;

import java.io.IOException;

/**
 * Created by Jinwook on 2016-11-25.
 */

public class SoundManager {

    private final static int NUM_OF_STREAMS = 15;

    private SoundPool soundPool;
    private Context context;

    public SoundManager(Context _context) {

        soundPool = new SoundPool(NUM_OF_STREAMS, AudioManager.STREAM_MUSIC, 0);
        context = _context;

        loadSounds();
    }

    private void loadSounds() {

        try {

            Application_communicator.ID_KOR[0] = soundPool.load(context.getAssets().openFd("sounds/korean_start.wav"), 1);
            Application_communicator.ID_KOR[1] = soundPool.load(context.getAssets().openFd("sounds/korean_pause.wav"), 1);
            Application_communicator.ID_KOR[2] = soundPool.load(context.getAssets().openFd("sounds/korean_stop.wav"), 1);
            Application_communicator.ID_KOR[3] = soundPool.load(context.getAssets().openFd("sounds/korean_dooropen.wav"), 1);
            Application_communicator.ID_KOR[4] = soundPool.load(context.getAssets().openFd("sounds/korean_doorclose.wav"), 1);

            Application_communicator.ID_ENG[0] = soundPool.load(context.getAssets().openFd("sounds/english_start.wav"), 1);
            Application_communicator.ID_ENG[1] = soundPool.load(context.getAssets().openFd("sounds/english_pause.wav"), 1);
            Application_communicator.ID_ENG[2] = soundPool.load(context.getAssets().openFd("sounds/english_stop.wav"), 1);
            Application_communicator.ID_ENG[3] = soundPool.load(context.getAssets().openFd("sounds/english_dooropen.wav"), 1);
            Application_communicator.ID_ENG[4] = soundPool.load(context.getAssets().openFd("sounds/english_doorclose.wav"), 1);

            Application_communicator.ID_CHI[0] = soundPool.load(context.getAssets().openFd("sounds/chinese_start.wav"), 1);
            Application_communicator.ID_CHI[1] = soundPool.load(context.getAssets().openFd("sounds/chinese_pause.wav"), 1);
            Application_communicator.ID_CHI[2] = soundPool.load(context.getAssets().openFd("sounds/chinese_stop.wav"), 1);
            Application_communicator.ID_CHI[3] = soundPool.load(context.getAssets().openFd("sounds/chinese_dooropen.wav"), 1);
            Application_communicator.ID_CHI[4] = soundPool.load(context.getAssets().openFd("sounds/chinese_doorclose.wav"), 1);

            Application_communicator.ID_LANG_SOUND[0] = Application_communicator.ID_KOR;
            Application_communicator.ID_LANG_SOUND[1] = Application_communicator.ID_ENG;
            Application_communicator.ID_LANG_SOUND[2] = Application_communicator.ID_CHI;

        } catch (IOException e) {

            Log.i("JW", "Sound file load error");
        }
    }

    public int play(int soundID) {

        return soundPool.play(soundID, 1.0f, 1.0f, 1, 0, 1.0f);
    }
}
