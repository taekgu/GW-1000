package com.sinest.gw_1000.management;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;

/**
 * Created by Jinwook on 2016-11-25.
 */

public class SoundManager {

    private final static int MAX_STREAMS = 1; // 동시 재생 가능한 음원 수

    private MediaPlayer latest_player;
    private MediaPlayer.OnPreparedListener preparedListener;
    private int prepare_cnt = 0;
    private final static int TOTAL_CNT = Application_manager.NUM_OF_LANG * Application_manager.NUM_OF_SOUND;



    private MediaPlayer mediaPlayer_therapy;
    private  boolean isPrepared_therapy = false;

    //private SoundPool soundPool;
    private Context context;

    public SoundManager(Context _context) {

//        soundPool = new SoundPool(MAX_STREAMS, AudioManager.STREAM_MUSIC, 0);
        context = _context;

        loadSounds2();
    }

    private void loadSounds2() {

        try {

            mediaPlayer_therapy = new MediaPlayer();
            mediaPlayer_therapy.setLooping(true);
            mediaPlayer_therapy.setAudioStreamType(AudioManager.STREAM_MUSIC);

            for (int lang=0; lang<Application_manager.NUM_OF_LANG; lang++) {

                for (int sound=0; sound<Application_manager.NUM_OF_SOUND; sound++) {

                    Application_manager.mediaPlayer[lang][sound] = new MediaPlayer();
                    Application_manager.mediaPlayer[lang][sound].setAudioStreamType(AudioManager.STREAM_NOTIFICATION);
                }
            }

            AssetFileDescriptor afd;
            afd = context.getAssets().openFd("sounds/korean_start.wav");
            Application_manager.mediaPlayer[0][0].setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());
            afd.close();
            afd = context.getAssets().openFd("sounds/korean_pause.wav");
            Application_manager.mediaPlayer[0][1].setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());
            afd.close();
            afd = context.getAssets().openFd("sounds/korean_stop.wav");
            Application_manager.mediaPlayer[0][2].setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());
            afd.close();
            afd = context.getAssets().openFd("sounds/korean_dooropen.wav");
            Application_manager.mediaPlayer[0][3].setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());
            afd.close();
            afd = context.getAssets().openFd("sounds/korean_doorclose.wav");
            Application_manager.mediaPlayer[0][4].setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());
            afd.close();

            afd = context.getAssets().openFd("sounds/english_start.wav");
            Application_manager.mediaPlayer[1][0].setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());
            afd.close();
            afd = context.getAssets().openFd("sounds/english_pause.wav");
            Application_manager.mediaPlayer[1][1].setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());
            afd.close();
            afd = context.getAssets().openFd("sounds/english_stop.wav");
            Application_manager.mediaPlayer[1][2].setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());
            afd.close();
            afd = context.getAssets().openFd("sounds/english_dooropen.wav");
            Application_manager.mediaPlayer[1][3].setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());
            afd.close();
            afd = context.getAssets().openFd("sounds/english_doorclose.wav");
            Application_manager.mediaPlayer[1][4].setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());
            afd.close();

            afd = context.getAssets().openFd("sounds/chinese_start.wav");
            Application_manager.mediaPlayer[2][0].setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());
            afd.close();
            afd = context.getAssets().openFd("sounds/chinese_pause.wav");
            Application_manager.mediaPlayer[2][1].setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());
            afd.close();
            afd = context.getAssets().openFd("sounds/chinese_stop.wav");
            Application_manager.mediaPlayer[2][2].setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());
            afd.close();
            afd = context.getAssets().openFd("sounds/chinese_dooropen.wav");
            Application_manager.mediaPlayer[2][3].setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());
            afd.close();
            afd = context.getAssets().openFd("sounds/chinese_doorclose.wav");
            Application_manager.mediaPlayer[2][4].setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());
            afd.close();

            afd = context.getAssets().openFd("sounds/therapy1.mp3");
            mediaPlayer_therapy.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());
            afd.close();

            preparedListener = new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mediaPlayer) {

                    if (mediaPlayer == mediaPlayer_therapy) {

                        isPrepared_therapy = true;
                    }
                    else {

                        prepare_cnt++;
                    }
                }
            };

            for (int lang=0; lang<Application_manager.NUM_OF_LANG; lang++) {

                for (int sound=0; sound<Application_manager.NUM_OF_SOUND; sound++) {

                    Application_manager.mediaPlayer[lang][sound].setOnPreparedListener(preparedListener);
                    Application_manager.mediaPlayer[lang][sound].prepare();
                }
            }
            mediaPlayer_therapy.setOnPreparedListener(preparedListener);
            mediaPlayer_therapy.prepare();

        } catch (IOException e) {

            Log.i("JW", "Sound file load error");
        }
    }
/*
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
*/
    public int play(int lang, int sound) {

        if (prepare_cnt == TOTAL_CNT) {

            if (latest_player == null) {

                Application_manager.mediaPlayer[lang][sound].start();
                latest_player = Application_manager.mediaPlayer[lang][sound];
                return 0;
            }
            else {

                if (!latest_player.isPlaying()) {

                    Application_manager.mediaPlayer[lang][sound].start();
                    latest_player = Application_manager.mediaPlayer[lang][sound];
                    return 0;
                }
                return -1;
            }
        }
        else {

            Log.i("JW", "음원 파일이 아직 준비되지 않았습니다. prepared_cnt = " + prepare_cnt);
            return -1;
        }
    }

    public void play_therapy(int sound, boolean isPlay) {

        // 재생
        if (isPlay) {

            if (isPrepared_therapy) {

                mediaPlayer_therapy.seekTo(0);
                mediaPlayer_therapy.start();
            }
        }
        // 중지
        else {

            if (mediaPlayer_therapy.isPlaying()) {

                mediaPlayer_therapy.pause();
            }
        }
    }
}
