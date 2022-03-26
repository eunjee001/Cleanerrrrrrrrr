package com.kyoungss.cleaner.storage.download

import java.io.File

data class CheckDownData(
    val downName:String,
    var isSelected:Boolean,
    val position: Int,
    val File : File,
    val diffTime : Long,
    val size : Long
)
