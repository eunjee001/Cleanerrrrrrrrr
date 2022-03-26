package com.kyoungss.cleaner.main

import android.Manifest.permission.WRITE_EXTERNAL_STORAGE
import android.app.Activity
import android.app.AppOpsManager
import android.content.Context
import android.content.Intent
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.content.pm.ResolveInfo
import android.os.*
import android.view.View
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import com.kyoungss.cleaner.DisplayAdjustment
import com.kyoungss.cleaner.check.clean.progress.MainCheckActivity
import com.kyoungss.cleaner.storage.folder.ManageFolderActivity
import com.kyoungss.cleaner.R
import java.io.FileInputStream

import java.lang.Exception
import java.util.*
import kotlin.collections.HashMap


class MainActivity : Activity() {
    var context: Context = this

    private final var FINISH_INTERVAL_TIME :Long = 2000
    private var doubleBackToExit = false
    var mBackWait:Long =0
    private lateinit var dpAdjustment: DisplayAdjustment
    private lateinit var appInstall : TextView
    private lateinit var systemInstall : TextView
    private lateinit var totalSize : TextView
    private lateinit var appImgFirst : ImageView
    private lateinit var appNameFirst : TextView
    private lateinit var appImgSecond : ImageView
    private lateinit var appNameSecond : TextView
    private lateinit var appImgThird : ImageView
    private lateinit var appNameThird : TextView
    private lateinit var appClearButton : Button

    private lateinit var  appStoreSize : TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initViews()
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    fun start(view: View) {

        if(if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                !checkForUsageAccessPermission() || !Environment.isExternalStorageManager()
            } else {
               !checkForUsageAccessPermission() ||  ActivityCompat.checkSelfPermission(this,WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
            }
        ) {
            val intent = Intent(this, PermissionActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_SINGLE_TOP)
            this.startActivity(intent)
        }else{
            val intent = Intent(this, MainCheckActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_SINGLE_TOP)
            this.startActivity(intent)
            }
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    private fun checkForUsageAccessPermission():Boolean {
        val appOps = getSystemService(Context.APP_OPS_SERVICE) as AppOpsManager
        val mode = appOps.checkOpNoThrow(AppOpsManager.OPSTR_GET_USAGE_STATS, Process.myUid(), packageName)
        return mode == AppOpsManager.MODE_ALLOWED
    }

    override fun onResume() {
        super.onResume()
        getInstalledApplication()
        getInstalledApplicationDate()
        getAppStorage()

        getInstalledAppStorage()
    }

    override fun onBackPressed() {
        val tempTime = System.currentTimeMillis()
        val intervalTime = tempTime - mBackWait
        if (intervalTime in 0..FINISH_INTERVAL_TIME) {
            super.onBackPressed()

        } else {
            mBackWait = tempTime
            Toast.makeText(this, "뒤로가기 버튼을 한번 더 누르면 종료됩니다.", Toast.LENGTH_SHORT).show()
            return
        }

        super.onBackPressed()

        /*if(System.currentTimeMillis() - mBackWait >= 1500){
            mBackWait = System.currentTimeMillis()
            Toast.makeText(this, "뒤로가기 버튼을 한번 더 누르면 종료됩니다.", Toast.LENGTH_LONG).show()
        }else{
            ActivityCompat.finishAffinity(this)
            System.runFinalization()
            exitProcess(0)
        }*/
    }
    fun runDelayed(mills:Long, function:() -> Unit){
        Handler(Looper.getMainLooper()).postDelayed(function, mills)
    }
    private fun getInstalledApplication() {
        val intent = Intent(Intent.ACTION_MAIN, null)
        intent.addCategory(Intent.CATEGORY_LAUNCHER)

        val launcherApps = packageManager.queryIntentActivities(intent, 0) as ArrayList<ResolveInfo>

        for(resolveInfo in launcherApps){
            val pkgName = resolveInfo.activityInfo.packageName
            val applicationInfo = packageManager.getApplicationInfo(pkgName, PackageManager.GET_META_DATA)

            val appCount=pkgName.count()
            if(!isSystemPackage(applicationInfo)){
                appInstall.text = getString(R.string.app_use_files, appCount)
            }else{
                systemInstall.text = getString(R.string.app_use_files, appCount)
            }
        }

    }

    private fun getInstalledApplicationDate() {

        val intent = Intent(Intent.ACTION_MAIN, null)
        intent.addCategory(Intent.CATEGORY_LAUNCHER)

        val launchedApps = packageManager.queryIntentActivities(intent, 0) as ArrayList<ResolveInfo>

        val applicationDate = HashMap<Long, String>()

        for (resolveInfo in launchedApps) {
            val pkgName = resolveInfo.activityInfo.packageName

            if (pkgName == packageName) {
                continue
            }
            try {
                val applicationInfo =
                    packageManager.getApplicationInfo(pkgName, PackageManager.GET_META_DATA)

                if (isSystemPackage(applicationInfo)) {
                    continue
                }

                val time = packageManager.getPackageInfo(pkgName, 0).firstInstallTime

                applicationDate[time] = pkgName
                val calendar = Calendar.getInstance()
                calendar.timeInMillis = time

            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        val hashList = applicationDate.keys.sortedDescending()
        for (list in hashList) {

            if (3 == hashList.indexOf(list)) {
                break
            }

            val applicationName =
                applicationDate[list]?.let {
                    packageManager.getApplicationInfo(
                        it,
                        PackageManager.GET_META_DATA
                    )
                }
            if (0 == hashList.indexOf(list)) {
                val appNamesFirst = applicationName?.let { packageManager.getApplicationLabel(it) }
                val appIconsFirst = applicationName?.let { packageManager.getApplicationIcon(it) }
                appNameFirst.text = (appNamesFirst)
                appImgFirst.setImageDrawable(appIconsFirst)
            }

            if (1 == hashList.indexOf(list)) {
                val appNamesSecond = applicationName?.let { packageManager.getApplicationLabel(it) }
                val appIconsSecond = applicationName?.let { packageManager.getApplicationIcon(it) }
                appNameSecond.text = (appNamesSecond)
                appImgSecond.setImageDrawable(appIconsSecond)
            }

            if (2 == hashList.indexOf(list)) {
                val appNamesThird = applicationName?.let { packageManager.getApplicationLabel(it) }
                val appIconsThird = applicationName?.let { packageManager.getApplicationIcon(it) }
                appNameThird.text = (appNamesThird)
                appImgThird.setImageDrawable(appIconsThird)
            }
        }
    }

    private  fun getAppStorage(){
        val availPath = Environment.getExternalStorageDirectory()

        val availStat = StatFs(availPath.path)
        val allPath = Environment.getDataDirectory().absolutePath
        val allStat = StatFs(allPath)

        val intent = Intent(Intent.ACTION_MAIN, null)
        intent.addCategory(Intent.CATEGORY_LAUNCHER)
        var appSizeSum:Long =0
        val launcherApps = packageManager.queryIntentActivities(intent, 0)
        var appSize:Long = 0

        val availLongSize: Long = if (Build.VERSION_CODES.JELLY_BEAN_MR2 > Build.VERSION.SDK_INT) {
            (availStat.availableBlocks * availStat.blockSize).toLong()
        } else {
            availStat.availableBlocksLong * availStat.blockSizeLong
        }


        val allLongSize:Long = if (Build.VERSION_CODES.JELLY_BEAN_MR2 > Build.VERSION.SDK_INT) {
            (allStat.blockSize * allStat.blockCount).toLong()
        } else {
            allStat.blockSizeLong * allStat.blockCountLong
        }


        for(resolveInfo in launcherApps) {
            val pkgName = resolveInfo.activityInfo.packageName
            try {
                val applicationInfo = packageManager.getApplicationInfo(pkgName, 0)
                appSize = FileInputStream(applicationInfo.sourceDir).channel.size()

                appSizeSum += appSize

            }catch (e : Exception) {
                e.printStackTrace()
            }

        }
        val storage = 100 * appSizeSum / (allLongSize+availLongSize)
        appStoreSize.text = storage.toString()
        totalSize.text = String.format("%.2f",(allLongSize-availLongSize)/(1024*1024*1024).toDouble()).toString()
    }

    private fun getInstalledAppStorage() {

        val intent = Intent(Intent.ACTION_MAIN, null)
        intent.addCategory(Intent.CATEGORY_LAUNCHER)
        var sum:Long =0
        val launcherApps = packageManager.queryIntentActivities(intent, 0) //as ArrayList<ResolveInfo>


        var appSize:Long = 0

        for(resolveInfo in launcherApps) {
            val pkgName = resolveInfo.activityInfo.packageName
            try {
                val applicationInfo = packageManager.getApplicationInfo(pkgName, 0)
                appSize = FileInputStream(applicationInfo.sourceDir).channel.size()

                sum += appSize

            }catch (e : Exception) {
                e.printStackTrace()
            }
        }
    }

    fun initViews() {

        dpAdjustment = DisplayAdjustment.getInstance(this)

        val startBtnImg = findViewById<ImageView>(R.id.main_start_btn)
        dpAdjustment.setMargins(startBtnImg,43,12,0,0)

        val startTimeText = findViewById<TextView>(R.id.main_date_start_text)
        dpAdjustment.setMargins(startTimeText,30,28,0,0)

        val startTime= findViewById<TextView>(R.id.main_date_start)
        dpAdjustment.setMargins(startTime,5,28,0,0)

        val startTextView = findViewById<TextView>(R.id.main_start_text)
        dpAdjustment.setMargins(startTextView,50,0,0,0)

        appImgFirst = findViewById(R.id.AppImgFirst)
        dpAdjustment.setMargins(appImgFirst,40,14,0,7)
        dpAdjustment.setScale(appImgFirst.layoutParams, 40f,40f)

        appNameFirst = findViewById(R.id.App_name_first)
        dpAdjustment.setMargins(appNameFirst,30,0,0,0)
        dpAdjustment.setScale(appNameFirst.layoutParams, 61f,40f)

        appImgSecond = findViewById(R.id.AppImgSecond)
        dpAdjustment.setMargins(appImgSecond,67,14,0,7)
        dpAdjustment.setScale(appImgSecond.layoutParams, 40f,40f)

        appNameSecond = findViewById(R.id.App_name_second)
        dpAdjustment.setMargins(appNameSecond,45,0,0,0)
        dpAdjustment.setScale(appNameSecond.layoutParams, 61f,40f)

        appImgThird = findViewById(R.id.AppImgThird)
        dpAdjustment.setMargins(appImgThird,65,14,0,7)
        dpAdjustment.setScale(appImgThird.layoutParams, 40f,40f)

        appNameThird = findViewById(R.id.App_name_third)
        dpAdjustment.setMargins(appNameThird,45,0,0,0)
        dpAdjustment.setScale(appNameThird.layoutParams, 61f,40f)


        val mainBox = findViewById<RelativeLayout>(R.id.main_box)
        dpAdjustment.setMargins(mainBox, 0,20,0,0)

        val latestInstallTitle = findViewById<TextView>(R.id.latest_install_app)
        dpAdjustment.setMargins(latestInstallTitle, 26,16,0,10)

        val appListRelative = findViewById<RelativeLayout>(R.id.App_list)
        dpAdjustment.setMargins(appListRelative,16,0,0,0)
        dpAdjustment.setScale(appListRelative.layoutParams, 328f,100f)

        appClearButton = findViewById(R.id.main_clear_button)
        dpAdjustment.setMargins(appClearButton, 16,24,0,16)
        dpAdjustment.setScale(appClearButton.layoutParams, 328f,48f)

        val firstBox = findViewById<RelativeLayout>(R.id.first_box)
        dpAdjustment.setMargins(firstBox, 16,0,10,10)
        dpAdjustment.setScale(firstBox.layoutParams, 159f, 75f)

        appInstall = findViewById(R.id.main_app_install)
        dpAdjustment.setMargins(appInstall,0,0,10,6)

        val firstTextBox = findViewById<RelativeLayout>(R.id.first_box_text)
        dpAdjustment.setMargins(firstTextBox,10,6,0,0)

        val secondBox = findViewById<RelativeLayout>(R.id.second_box)
        dpAdjustment.setMargins(secondBox, 0,0,0,10)
        dpAdjustment.setScale(secondBox.layoutParams, 159f,75f)

        systemInstall = findViewById(R.id.main_app_system)
        dpAdjustment.setMargins(systemInstall,0,0,10,6)

        val secondTextBox = findViewById<RelativeLayout>(R.id.second_box_text)
        dpAdjustment.setMargins(secondTextBox, 10,6,0,0)

        val thirdBox = findViewById<RelativeLayout>(R.id.third_box)
        dpAdjustment.setMargins(thirdBox,16,0,10,17)
        dpAdjustment.setScale(thirdBox.layoutParams,159f,75f)

        appStoreSize = findViewById(R.id.main_app_stored)
        dpAdjustment.setMargins(appStoreSize,0,0,10,6)

        val thirdTextBox = findViewById<RelativeLayout>(R.id.third_box_text)
        dpAdjustment.setMargins(thirdTextBox, 10,6,0,0)

        val fourthBox = findViewById<RelativeLayout>(R.id.fourth_box)
        dpAdjustment.setMargins(fourthBox, 0,0,0,17)
        dpAdjustment.setScale(fourthBox.layoutParams, 159f,75f)

        totalSize = findViewById(R.id.main_app_all)
        dpAdjustment.setMargins(totalSize,0,0,10,5)

        val fourthTextBox = findViewById<RelativeLayout>(R.id.fourth_box_text)
        dpAdjustment.setMargins(fourthTextBox,10,6,0,0)

        val mainCircle = findViewById<RelativeLayout>(R.id.main_button_backGround)
        dpAdjustment.setScale(mainCircle.layoutParams, 180f ,180f )
        dpAdjustment.setMargins(mainCircle, 73, 23,0,0)
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    fun clear(view: View) {

        if(if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                !checkForUsageAccessPermission() || !Environment.isExternalStorageManager()
            } else {
                !checkForUsageAccessPermission() ||  ActivityCompat.checkSelfPermission(this,WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
            }
        ) {
            Toast.makeText(
                context,
                "권한 설정이 필요합니다. \n 검사시작 버튼 먼저 눌러 주세요.",
                Toast.LENGTH_SHORT
            ).show()
        }else{
            val intent = Intent(this, ManageFolderActivity::class.java)
            this.startActivity(intent)
        }

    }

    private fun isSystemPackage(ai: ApplicationInfo): Boolean {
        return ai.flags and ApplicationInfo.FLAG_SYSTEM != 0
    }
}


