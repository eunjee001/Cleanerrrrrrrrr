package com.kyoungss.cleaner.storage.image

import android.net.Uri

data class ImageSizeData(
    val type: Long,
    val image: Uri,
    val imageSize: String,
    val imagePackage: String,
    val imageDate: Long,
)