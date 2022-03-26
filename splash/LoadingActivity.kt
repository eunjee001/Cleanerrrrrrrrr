package com.kyoungss.cleaner.splash

import android.Manifest
import android.app.Activity
import android.app.AppOpsManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.os.Process
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import com.kyoungss.cleaner.R
import com.kyoungss.cleaner.firstSettings.EulaFirstActivity
import com.kyoungss.cleaner.main.MainActivity
import java.util.*

class LoadingActivity : Activity() {

    var context:Context = this


    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_loading)
        if (if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                !checkForUsageAccessPermission() || !Environment.isExternalStorageManager()
            } else {
                !checkForUsageAccessPermission() || ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.READ_EXTERNAL_STORAGE
                ) != PackageManager.PERMISSION_GRANTED
            }
        ) {
            val timerTask = object : TimerTask() {
                @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
                override fun run() {
                    val intent = Intent(this@LoadingActivity, EulaFirstActivity::class.java)
                    startActivity(intent)

                    finish()
                }
            }
            Timer().schedule(timerTask, 3000)
        } else {
            val timerTask = object : TimerTask() {
                @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
                override fun run() {
                    val intent = Intent(this@LoadingActivity, MainActivity::class.java)
                    startActivity(intent)

                    finish()
                }
            }
            Timer().schedule(timerTask, 3000)

        }
    }
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    private fun checkForUsageAccessPermission():Boolean {
        val appOps = getSystemService(Context.APP_OPS_SERVICE) as AppOpsManager
        val mode = appOps.checkOpNoThrow(AppOpsManager.OPSTR_GET_USAGE_STATS, Process.myUid(), packageName)
        return mode == AppOpsManager.MODE_ALLOWED
    }

}

