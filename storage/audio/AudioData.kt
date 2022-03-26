package com.kyoungss.cleaner.storage.audio

import android.net.Uri

data class AudioData(
    val type: Long,
    val audioIcon: Int,
    val audioName: String,
    val audioTime: String,
    var audioSize: Long,
    var audioChoose: Boolean,
    val audioUri: Uri,
    val diffTime: Long
)