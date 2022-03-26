package com.kyoungss.cleaner.storage.video

import android.net.Uri

data class CheckVideoData(
    val videoName:String,
    var isSelected:Boolean,
    val position: Int,
    val video : Uri,
    val videoSize:Long,
    val diffTime : Long
)
