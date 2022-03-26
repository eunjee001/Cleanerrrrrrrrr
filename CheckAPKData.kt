package com.kyoungss.cleaner

import java.io.File

data class CheckAPKData(
    val appName: String,
    var isSelected: Boolean,
    val position: Int,
    val apkSize: Long,
    val file : File
)