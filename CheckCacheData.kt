package com.kyoungss.cleaner

import java.io.File

class CheckCacheData(
    val appName: String,
    var isSelected: Boolean,
    var position: Int,
    val cacheSize : Long,
    val file : File
)