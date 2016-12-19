package com.sinest.gw_1000.management;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.util.Log;

import java.io.IOException;

/**
 * Created by Jinwook on 2016-11-25.
 */

public class SoundManager {

    private final static int MAX_STREAMS = 1; // 동시 재생 가능한 음원 수

    private MediaPlayer[] mediaPlayer_open;
    private MediaPlayer[] mediaPlayer_close;
    private MediaPlayer.OnPreparedListener preparedListener;
    private int prepare_cnt = 0;
    private final static int TOTAL_CNT = 6;

    private SoundPool soundPool;
    private Context context;

    public SoundManager(Context _context) {

        mediaPlayer_open = new MediaPlayer[3];
        mediaPlayer_close = new MediaPlayer[3] ;
        soundPool = new SoundPool(MAX_STREAMS, AudioManager.STREAM_MUSIC, 0);
        context = _context;

        loadSounds();
    }

    private void loadSounds() {

        try {

            Application_manager.ID_KOR[0] = soundPool.load(context.getAssets().openFd("sounds/korean_start.wav"), 1);
            Application_manager.ID_KOR[1] = soundPool.load(context.getAssets().openFd("sounds/korean_pause.wav"), 1);
            Application_manager.ID_KOR[2] = soundPool.load(context.getAssets().openFd("sounds/korean_stop.wav"), 1);
            Application_manager.ID_KOR[3] = soundPool.load(context.getAssets().openFd("sounds/korean_dooropen.wav"), 1);
            Application_manager.ID_KOR[4] = soundPool.load(context.getAssets().openFd("sounds/korean_doorclose.wav"), 1);

            Application_manager.ID_ENG[0] = soundPool.load(context.getAssets().openFd("sounds/english_start.wav"), 1);
            Application_manager.ID_ENG[1] = soundPool.load(context.getAssets().openFd("sounds/english_pause.wav"), 1);
            Application_manager.ID_ENG[2] = soundPool.load(context.getAssets().openFd("sounds/english_stop.wav"), 1);
            Application_manager.ID_ENG[3] = soundPool.load(context.getAssets().openFd("sounds/english_dooropen.wav"), 1);
            Application_manager.ID_ENG[4] = soundPool.load(context.getAssets().openFd("sounds/english_doorclose.wav"), 1);

            Application_manager.ID_CHI[0] = soundPool.load(context.getAssets().openFd("sounds/chinese_start.wav"), 1);
            Application_manager.ID_CHI[1] = soundPool.load(context.getAssets().openFd("sounds/chinese_pause.wav"), 1);
            Application_manager.ID_CHI[2] = soundPool.load(context.getAssets().openFd("sounds/chinese_stop.wav"), 1);
            Application_manager.ID_CHI[3] = soundPool.load(context.getAssets().openFd("sounds/chinese_dooropen.wav"), 1);
            Application_manager.ID_CHI[4] = soundPool.load(context.getAssets().openFd("sounds/chinese_doorclose.wav"), 1);

            Application_manager.ID_LANG_SOUND[0] = Application_manager.ID_KOR;
            Application_manager.ID_LANG_SOUND[1] = Application_manager.ID_ENG;
            Application_manager.ID_LANG_SOUND[2] = Application_manager.ID_CHI;

            for (int i=0; i<3; i++) {

                mediaPlayer_open[i] = new MediaPlayer();
                mediaPlayer_close[i] = new MediaPlayer();

                mediaPlayer_open[i].setAudioStreamType(AudioManager.STREAM_MUSIC);
                mediaPlayer_close[i].setAudioStreamType(AudioManager.STREAM_MUSIC);
            }

            AssetFileDescriptor afd;
            afd = context.getAssets().openFd("sounds/korean_dooropen.wav");
            mediaPlayer_open[0].setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());
            afd.close();
            afd = context.getAssets().openFd("sounds/english_dooropen.wav");
            mediaPlayer_open[1].setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());
            afd.close();
            afd = context.getAssets().openFd("sounds/chinese_dooropen.wav");
            mediaPlayer_open[2].setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());
            afd.close();

            afd = context.getAssets().openFd("sounds/korean_doorclose.wav");
            mediaPlayer_close[0].setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());
            afd.close();
            afd = context.getAssets().openFd("sounds/english_doorclose.wav");
            mediaPlayer_close[1].setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());
            afd.close();
            afd = context.getAssets().openFd("sounds/chinese_doorclose.wav");
            mediaPlayer_close[2].setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());
            afd.close();

            preparedListener = new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mediaPlayer) {

                    prepare_cnt++;
                }
            };

            for (int i=0; i<3; i++) {

                mediaPlayer_open[i].setOnPreparedListener(preparedListener);
                mediaPlayer_close[i].setOnPreparedListener(preparedListener);
                mediaPlayer_open[i].prepare();
                mediaPlayer_close[i].prepare();
            }

        } catch (IOException e) {

            Log.i("JW", "Sound file load error");
        }
    }

    public int play(int soundID) {

        return soundPool.play(soundID, 1.0f, 1.0f, 1, 0, 1.0f);
    }

    public void play_door_open(int lang) {

        if (prepare_cnt == TOTAL_CNT) {

            if(!mediaPlayer_close[lang].isPlaying()) {

                mediaPlayer_open[lang].start();
            }
        }
        else {

            Log.i("JW", "음원 파일이 아직 준비되지 않았습니다. prepared_cnt = " + prepare_cnt);
        }
    }

    public void play_door_close(int lang) {

        if (prepare_cnt == TOTAL_CNT) {

            if(!mediaPlayer_open[lang].isPlaying()) {

                mediaPlayer_close[lang].start();
            }
        }
        else {

            Log.i("JW", "음원 파일이 아직 준비되지 않았습니다. prepared_cnt = " + prepare_cnt);
        }
    }
}
