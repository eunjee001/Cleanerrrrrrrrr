package com.kyoungss.cleaner.storage.image

import android.net.Uri

data class CheckImageData(
    val imageName:String,
    var isSelected:Boolean,
    val position: Int,
    val image :Uri,
    val diffTime : Long,
    val size : Long
)
