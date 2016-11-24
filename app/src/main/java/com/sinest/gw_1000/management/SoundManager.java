package com.sinest.gw_1000.management;

import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;
import android.util.Log;

import com.sinest.gw_1000.R;

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

            soundPool.load(context.getAssets().openFd("sounds/korean_start.wav"), 1);
        }
        catch (IOException e) {

            Log.i("JW", "Sound file load error");
        }
    }
}
