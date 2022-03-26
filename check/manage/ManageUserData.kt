package com.kyoungss.cleaner.check.manage

import android.graphics.drawable.Drawable

data class ManageUserData(
    val type: Int,
    val appIcon: Drawable,
    val appName: String,
    val appUse: Long,
    var appDate: Long,
    val appDateInfo: String,

    val appPackage: String
)