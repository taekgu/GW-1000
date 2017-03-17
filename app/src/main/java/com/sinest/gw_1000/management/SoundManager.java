package com.sinest.gw_1000.management;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.util.Log;

import java.io.IOException;

import static android.content.Context.AUDIO_SERVICE;

/**
 * Created by Jinwook on 2016-11-25.
 *
 * 어플리케이션에서 사용되는 음원 관리
 */

public class SoundManager {

    // 마지막에 재생된 음악
    private MediaPlayer latest_player;

    public boolean isPlaying = false;
    private boolean isRun = false;
    private Thread thread_isPlaying;

    private MediaPlayer.OnPreparedListener preparedListener;
    private MediaPlayer.OnPreparedListener preparedListener_for_therapy;
    private int prepare_cnt = 0;
    private final static int TOTAL_CNT = Application_manager.NUM_OF_LANG * Application_manager.NUM_OF_SOUND;

    private final static int NUM_OF_THERAPY = 5;
    private MediaPlayer[] mediaPlayer_therapy = new MediaPlayer[NUM_OF_THERAPY];
    private int prepare_cnt_therapy = 0;

    private Context context;

    public SoundManager(Context _context) {

        context = _context;

        thread_isPlaying = new Thread(new Runnable() {
            @Override
            public void run() {

                while (isRun) {

                    if (latest_player != null) {

                        setIsPlaying(latest_player.isPlaying());
                    }
                }
            }
        });
        isRun = true;
        thread_isPlaying.start();

        loadSounds();
    }

    /**
     * 치료음악 볼륨 설정
     * @param step Emotion therapy setting 팝업창에서 Emotion sound volume 값 1~5
     */
    public void setVolume_therapy(int step) {

        final AudioManager audioManager = (AudioManager) context.getSystemService(AUDIO_SERVICE);

        int max = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        double level = (double)max / 5;

        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, (int)(level * step), 0);

        Log.i("JW_VOL", "max = " + max + " / level = " + level + " / volume = " + step);
    }

    /**
     * 알림음악 볼륨 설정
     * @param volume 세팅 액티비티에서 시크바로 설정한 볼륨 값 (0~10)
     */
    public void setVolume_alarm(int volume) {

        volume /= 10;
        final AudioManager audioManager = (AudioManager) context.getSystemService(AUDIO_SERVICE);

        int max = audioManager.getStreamMaxVolume(AudioManager.STREAM_RING);
        double level = (double)max / 10;

        audioManager.setStreamVolume(AudioManager.STREAM_RING, (int)(level * volume), 0);

        Log.i("JW_VOL", "max = " + max + " / level = " + level + " / volume = " + volume);
    }

    synchronized private void setIsPlaying(boolean val) {

        isPlaying = val;
    }

    synchronized public boolean getIsPlaying() {

        return isPlaying;
    }

    /**
     * 음원 파일 로드
     */
    private void loadSounds() {

        try {

            preparedListener = new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mediaPlayer) {

                    prepare_cnt++;
                }
            };

            preparedListener_for_therapy = new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mediaPlayer) {

                    prepare_cnt_therapy++;
                }
            };

            for (int i=0; i<NUM_OF_THERAPY; i++) {

                mediaPlayer_therapy[i] = new MediaPlayer();
                mediaPlayer_therapy[i].setLooping(true);
                mediaPlayer_therapy[i].setAudioStreamType(AudioManager.STREAM_MUSIC);
            }

            for (int lang=0; lang<Application_manager.NUM_OF_LANG; lang++) {

                for (int sound=0; sound<Application_manager.NUM_OF_SOUND; sound++) {

                    Application_manager.mediaPlayer[lang][sound] = new MediaPlayer();
                    Application_manager.mediaPlayer[lang][sound].setAudioStreamType(AudioManager.STREAM_RING);
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

            for (int i=0; i<NUM_OF_THERAPY; i++) {

                String fileName = "sounds/therapy" + (i+1) + ".mp3";
                afd = context.getAssets().openFd(fileName);
                mediaPlayer_therapy[i].setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());
                afd.close();

                mediaPlayer_therapy[i].setOnPreparedListener(preparedListener_for_therapy);
                mediaPlayer_therapy[i].prepare();
            }

            for (int lang=0; lang<Application_manager.NUM_OF_LANG; lang++) {

                for (int sound=0; sound<Application_manager.NUM_OF_SOUND; sound++) {

                    Application_manager.mediaPlayer[lang][sound].setOnPreparedListener(preparedListener);
                    Application_manager.mediaPlayer[lang][sound].prepare();
                }
            }

        } catch (IOException e) {

            Log.i("JW", "Sound file load error");
        }
    }

    /**
     * 선택 알림음원 재생
     * @param lang 언어
     * @param sound 재생 음원 번호 (0: 치료 시작, 1: 치료 일시정지, 2: 치료 정지, 3: 도어 오픈, 4: 도어 클로즈)
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

    /**
     * 선택 치료음원 재생/정지
     * @param sound 재생 음원 번호
     * @param isPlay true: 재생 / false: 정지
     */
    public void play_therapy(int sound, boolean isPlay) {

        int idx = sound - 1;
        // 재생
        if (isPlay) {

            if (prepare_cnt_therapy == NUM_OF_THERAPY) {

                mediaPlayer_therapy[idx].seekTo(0);
                mediaPlayer_therapy[idx].start();
            }
        }
        // 중지
        else {

            if (mediaPlayer_therapy[idx].isPlaying()) {

                mediaPlayer_therapy[idx].pause();
            }
        }
    }
}
