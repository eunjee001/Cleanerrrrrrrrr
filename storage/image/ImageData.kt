package com.kyoungss.cleaner.storage.image

import android.net.Uri

data class ImageData(
    val type: Long,
    val image: Uri,
    val imageSize: Long,
    val imagePackage: Int,
    val imageName: String,
    var imageChoose: Boolean,
    var diffTime : Long
)