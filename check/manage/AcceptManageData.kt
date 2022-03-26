package com.kyoungss.cleaner.check.manage

import android.graphics.drawable.Drawable

data class AcceptManageData(
    val type: Int,
    val appIcon: Drawable,
    val appName: String,
    val appUse: Long,
    var appDate: Long,
    val appDateInfo: String,
    val appChoose : Boolean,
    val appPackage: String,
    val position: Int
)
