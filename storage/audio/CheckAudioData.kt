package com.kyoungss.cleaner.storage.audio

import android.net.Uri

data class CheckAudioData(
    val audioName: String,
    var isSelected:Boolean,
    val position: Int,
    val diffTime : Long,
    val audioUri : Uri,
    val audioSize : Long
)
