package com.system.shailendra.core.utils

import android.content.Context
import android.media.MediaPlayer
import com.system.shailendra.R

fun ding(context: Context) {
    val mediaPlayer = MediaPlayer.create(context, R.raw.beep)
    mediaPlayer.start()
}