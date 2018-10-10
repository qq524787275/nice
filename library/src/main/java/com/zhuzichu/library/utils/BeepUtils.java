package com.zhuzichu.library.utils;

import android.app.Activity;
import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.media.AudioManager;
import android.media.MediaPlayer;

import com.zhuzichu.library.R;

import java.io.IOException;

public class BeepUtils {
    private static final float BEEP_VOLUME = 0.50f;
    private static final int VIBRATE_DURATION = 200;
    private static boolean playBeep = false;
    private static MediaPlayer mediaPlayer;

    public static void playBeep(Activity mContext, boolean vibrate) {
        playBeep = true;
        AudioManager audioService = (AudioManager) mContext.getSystemService(Context.AUDIO_SERVICE);
        if (audioService.getRingerMode() != AudioManager.RINGER_MODE_NORMAL) {
            playBeep = false;
        }
        if (playBeep && mediaPlayer != null) {
            mediaPlayer.start();
        } else {
            mContext.setVolumeControlStream(AudioManager.STREAM_MUSIC);
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mediaPlayer.setOnCompletionListener(mediaPlayer -> mediaPlayer.seekTo(0));

            AssetFileDescriptor file = mContext.getResources().openRawResourceFd(R.raw.beep);
            try {
                mediaPlayer.setDataSource(file.getFileDescriptor(), file.getStartOffset(), file.getLength());
                file.close();
                mediaPlayer.setVolume(BEEP_VOLUME, BEEP_VOLUME);
                mediaPlayer.prepare();
            } catch (IOException e) {
                mediaPlayer = null;
            }
        }
        if (vibrate) {
            VibrateUtils.vibrateOnce(mContext, VIBRATE_DURATION);
        }
    }
}
