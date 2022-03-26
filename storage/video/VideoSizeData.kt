package com.kyoungss.cleaner.storage.video

import android.net.Uri

data class VideoSizeData(
    val type: Long,
    val video: Uri,
    val videoSize: Long,
    val videoTime: String,
    val videoPackage: Int,
    val videoName: String,
    var videoChoose: Boolean
)