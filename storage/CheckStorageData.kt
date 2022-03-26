package com.kyoungss.cleaner.storage

import android.net.Uri

data class CheckStorageData(
    val galleryName:String,
    var isSelected:Boolean,
    val position: Int,
    val gallery :Uri,
    val diffTime : Long,
    val gallerySize: Long
)
