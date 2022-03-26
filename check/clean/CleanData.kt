package com.kyoungss.cleaner.check.clean

import android.graphics.drawable.Drawable
import java.io.File

data class CleanData(
    val type: Int,
    val appName: String,
    var appUse: Long,
    val appIcon: Drawable?,
    var appChoose: Boolean,
    val file: File
    )
