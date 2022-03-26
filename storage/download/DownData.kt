package com.kyoungss.cleaner.storage.download

import java.io.File

data class DownData(
    val type: Long,
    val downloadIcon: Int,
    val downloadName: String,
    val downloadTime: Int,
    var downloadSize: Long,
    var downloadChoose: Boolean,
    val downloadFile: File,
    val diffTime : Long
)