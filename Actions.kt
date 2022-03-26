package com.kyoungss.cleaner

object Actions {

    private const val prefix = "com.tachyon.cleaner."

    const val START_FOREGROUND = prefix + "startforeground"
    const val STOP_FOREGROUND = prefix + "stopforeground"

    const val FINISHED_WIPER_TASK = 100
    const val INIT_WIPER_TASK = -1
    const val PROGRESS_MAXIMUM = 100

    const val sec = 60
    const val min = 60 * sec
    const val hour = 24 * min
    const val day = 30 * hour
    const val month = 12 * day

    const val KB = 1024
    const val MB = 1024 * KB
    const val GB = 1024 * MB
}