package com.kyoungss.cleaner.storage.document

import java.io.File

data class DocData(
    val type: Long,
    val documentIcon: Int,
    val documentName: String,
    val documentTime: Long,
    var documentSize: Long,
    var documentChoose: Boolean,
    var documentFile: File,
    var diffTime: Long
)