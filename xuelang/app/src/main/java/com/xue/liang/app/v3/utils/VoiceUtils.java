package com.xue.liang.app.v3.utils;

import android.content.Context;
import android.media.AudioManager;

/**
 * Created by jikun on 2016/12/15.
 */

public class VoiceUtils {


    public static boolean isSilence(Context context) {
        AudioManager mAudioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        int max = mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        int current = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        if (current <= 0) {
            return true;
        } else {
            return false;
        }
    }


    /**
     * 自动根据状态来判断禁音还是恢复音量 恢复的音量为最大音量的2/3
     *
     * @param context
     */
    public static void setVolumeSilenOrRecovery(Context context) {
        AudioManager mAudioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        int max = mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        int current = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        if (isSilence(context)) {
            int currentVoice = max * 2 / 3;
            mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, currentVoice, 0);//tempVolume:音量绝对值
        } else {
            mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, 0, 0);//tempVolume:音量绝对值

        }
        mAudioManager.adjustStreamVolume(AudioManager.STREAM_MUSIC, AudioManager.ADJUST_LOWER,
                AudioManager.FX_FOCUS_NAVIGATION_UP);

    }

    /**
     * 恢复的音量为最大音量
     *
     * @param context
     */
    public static void recoveryVoice(Context context) {
        AudioManager mAudioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        int max = mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        int currentVoice = max;
        mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, currentVoice, 0);//tempVolume:音量绝对值

    }


}
