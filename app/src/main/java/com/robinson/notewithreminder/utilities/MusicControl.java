package com.robinson.notewithreminder.utilities;

import android.content.Context;
import android.media.MediaPlayer;

import com.robinson.notewithreminder.R;

public class MusicControl {
    private static MusicControl sInstance;
    private final Context mContext;
    private MediaPlayer mMediaPlayer;
    public MusicControl(Context context) {
        this.mContext = context;
    }

    public static MusicControl getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new MusicControl(context);
        }
        return sInstance;
    }

    public void playMusic() {
        mMediaPlayer = MediaPlayer.create(mContext, R.raw.smoke_beep);
        mMediaPlayer.start();
    }

    public void stopMusic() {
        if(mMediaPlayer != null) {
            mMediaPlayer.stop();
            mMediaPlayer.seekTo(0);
        }
    }
}
