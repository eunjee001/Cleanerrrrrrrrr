package com.kyoungss.cleaner.storage.document

import java.io.File

data class CheckDocData(
    val docName:String,
    var isSelected:Boolean,
    val position: Int,
    val File : File,
    val diffTime : Long,
    val size : Long
)
