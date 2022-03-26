package com.kyoungss.cleaner.storage

import java.io.File

data class CheckStorageSecondData(
    val fileName:String,
    var isSelected:Boolean,
    val position: Int,
    val File :File,
    val diffTime : Long,
    val fileSize: Long
)
