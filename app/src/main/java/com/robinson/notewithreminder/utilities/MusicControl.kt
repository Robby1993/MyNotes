package com.robinson.notewithreminder.utilities

import android.content.Context
import android.media.MediaPlayer
import android.util.Log
import com.robinson.notewithreminder.R

class MusicControl(private val mContext: Context) {
    private var mMediaPlayer: MediaPlayer? = null
    fun playMusic() {
        mMediaPlayer = MediaPlayer.create(mContext, R.raw.smoke_beep)
        mMediaPlayer?.start()
    }

    fun stopMusic() {
        if (mMediaPlayer != null) {
            mMediaPlayer!!.stop()
            mMediaPlayer!!.seekTo(0)
        } else {
            Log.d("stopMusic", "-----------not stop")
        }
    }

    companion object {
        private var sInstance: MusicControl? = null

        @JvmStatic
        fun getInstance(context: Context): MusicControl? {
            if (sInstance == null) {
                sInstance = MusicControl(context)
            }
            return sInstance
        }
    }
}