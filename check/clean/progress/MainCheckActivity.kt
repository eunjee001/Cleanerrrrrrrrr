package com.kyoungss.cleaner.check.clean.progress

import android.content.Context
import android.content.Intent
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.content.pm.ResolveInfo
import android.graphics.drawable.Drawable
import android.os.*
import android.view.KeyEvent
import android.view.View
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.kyoungss.cleaner.DisplayAdjustment
import com.kyoungss.cleaner.R
import com.kyoungss.cleaner.check.clean.ApkData
import com.kyoungss.cleaner.check.clean.CheckFinishActivity
import com.kyoungss.cleaner.check.clean.CleanData
import com.kyoungss.cleaner.check.manage.ManageAppActivity
import com.kyoungss.cleaner.check.manage.ManageData

import com.kyoungss.cleaner.data.RepositoryImpl
import com.kyoungss.cleaner.data.RepositoryImplProgress
import java.io.File
import java.io.FileInputStream
import java.lang.Exception
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class MainCheckActivity : AppCompatActivity() {
   var context: Context = this
    private lateinit var dpAdjustment: DisplayAdjustment

    private lateinit var appImage: ImageView
    private lateinit var appNames: TextView
    private lateinit var appPercent: TextView
    private lateinit var firstCheck: TextView
    private lateinit var secondCheck: TextView
    private lateinit var thirdCheck: TextView
    private lateinit var fourthCheck: TextView
    private lateinit var progressBar: CustomProgressbar
    private var everyAppName : CharSequence? = null
    private var everyAppIcon : Drawable? = null
    private val manageAgreeListCategory = RepositoryImplProgress.getManageAgree()
    private val manageAcceptListCategory = RepositoryImplProgress.getManageAccept()

    private val cacheListCategory = ArrayList<CleanData>()
    private val apkListCategory = ArrayList<ApkData>()
    private val manageListCategory = ArrayList<ManageData>()
    var applicationSize: Long = 0

    lateinit var page: RelativeLayout


    companion object {
        const val CACHE_DATA_TYPE = 0X01
        const val APK_DATA_TYPE = 0x02
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_check)
        initViews()
        RepositoryImpl.progressLive.observe(this) {

            if ((it in 0..100)) {
                progressBar.setProgress(it)
            }
        }

        RepositoryImpl.progressInt.observe(this) {
            if (it in 0..100) {
                appPercent.text = it.toString()
                if (manageListCategory.isNotEmpty()) {
                    appNames.text = manageListCategory[it % manageListCategory.count()].appName
                    appImage.setImageDrawable(manageListCategory[it % manageListCategory.count()].appIcon)   // []안에 어떤것을 명확히 넣을지 모르겠어서 앱 개수 넣음
                }

                if (100 == it) {
                    //다음 화면
                    val intent = Intent(this, CheckFinishActivity::class.java)
                    this.startActivity(intent)
                    finish()

                }
            }
        }
        startProgressService()
    }



    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        apkFind()
        getInstalledApplication()

        if (Build.VERSION_CODES.S> Build.VERSION.SDK_INT) {
            cacheFind()
        }
    }

    private fun initViews() {
        dpAdjustment = DisplayAdjustment.getInstance(this)

        appPercent = findViewById(R.id.main_check_percent_text)
        dpAdjustment.setMargins(appPercent, 147,70,0,0)

        appImage = findViewById(R.id.main_check_AppImg)
        dpAdjustment.setMargins(appImage, 0,25,0,0)
        dpAdjustment.setScale(appImage.layoutParams, 38f,38f)

        appNames = findViewById(R.id.main_check_appName)
        firstCheck = findViewById(R.id.main_check_firstCheck)
        secondCheck = findViewById(R.id.main_check_secondCheck)
        thirdCheck = findViewById(R.id.main_check_thirdCheck)
        fourthCheck = findViewById(R.id.main_check_fourthCheck)

        progressBar = findViewById(R.id.progressBar)
        dpAdjustment.setMargins(progressBar, 65,30,0,0)
        dpAdjustment.setScale(progressBar.layoutParams, 400f,250f)

        page = findViewById(R.id.progressMainPage)

        val progressLayout = findViewById<RelativeLayout>(R.id.main_check_loading)
        dpAdjustment.setMargins(progressLayout, 0,65,0,0)

        val stop = findViewById<Button>(R.id.main_check_clear_button)
        dpAdjustment.setScale(stop.layoutParams, 184f,48f)
        dpAdjustment.setMargins(stop, 88,25,0,0)

    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        // 뒤로가기 버튼 클릭 시
        return if (keyCode == KeyEvent.KEYCODE_BACK) {
            false
        } else super.onKeyDown(keyCode, event)
    }

    private fun getInstalledApplication() {
        val intent = Intent(Intent.ACTION_MAIN, null)
        intent.addCategory(Intent.CATEGORY_LAUNCHER)

        val hash = HashMap<Long, String>()
        val launcherApps = packageManager.queryIntentActivities(intent, 0) as ArrayList<ResolveInfo>
        for (resolveInfo in launcherApps) {
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
                launcherApps.distinct()
                val appSize = (FileInputStream(applicationInfo.sourceDir).channel.size())

                val time = packageManager.getPackageInfo(pkgName, 0).firstInstallTime

                val format: DateFormat = SimpleDateFormat("yyyy.MM.dd")

                val calendar = Calendar.getInstance()
                calendar.timeInMillis = time

                everyAppName = packageManager.getApplicationLabel(applicationInfo)
                everyAppIcon = packageManager.getApplicationIcon(applicationInfo)

                hash[appSize] = pkgName

                manageListCategory.add(
                    ManageData(
                        type = ManageAppActivity.AppInfo,
                        appIcon = everyAppIcon!!,
                        appName = "$everyAppName",
                        appUse = appSize,
                        appDate =0  ,
                        appDateInfo = format.format(calendar.time),
                        appChoose= false,
                        appPackage = pkgName,
                        position = manageListCategory.count()
                    )
                )
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        RepositoryImpl.setFileList(manageListCategory)
        if(manageAcceptListCategory.isEmpty()) {
            manageAgreeListCategory.addAll(manageListCategory)

        }
    }


    private fun cacheFind() {
        val externalStorage = Environment.getExternalStorageDirectory().absolutePath
        val path = "$externalStorage/Android/data"
        val file = File(path)
        val fileList = file.listFiles() ?: return

        val fileSize = fileList.size

        for ((count, files) in fileList.withIndex()) {
            cacheList(files.absolutePath, files.name)
            val avg = ((count+1) * 100/fileSize)
            RepositoryImpl.setProgressLive(avg)
        }
    }


    var sum: Long =0

    private fun cacheList(path: String, packageName : String) {
        val file = File(path)
        val fileList = file.listFiles() ?: return


        for (files in fileList) {
            var applicationInfo: ApplicationInfo? = null

            if (files.name.equals("cache")){

                try {
                    applicationInfo = packageManager.getApplicationInfo(packageName, PackageManager.GET_META_DATA)
                    /*if (isSystemPackage(applicationInfo)) {
                        continue
                    }*/
                }catch (e: PackageManager.NameNotFoundException){
                    continue
                }
                val applicationName = packageManager.getApplicationLabel(applicationInfo)
                val applicationIcon = packageManager.getApplicationIcon(applicationInfo)

                applicationSize = files.length()
                sum += applicationSize

                cacheHaveList(files.absolutePath, packageName)

                cacheListCategory.add(
                    CleanData(
                        type = CACHE_DATA_TYPE,
                        appIcon = applicationIcon,
                        appName = "$applicationName",
                        appUse = sum,
                        appChoose = false,
                        file = files
                    )
                )
            }
        }

        RepositoryImplProgress.setCacheAppList(cacheListCategory)
    }
    private fun cacheHaveList(path:String, packageName : String) {
        val file = File(path)
        val fileList = file.listFiles() ?: return
        for (files in fileList) {
            if (files.isDirectory) {
                cacheHaveList(files.absolutePath, packageName)
            } else if(files.isFile) {
                applicationSize = files.length()
            }
            sum += applicationSize
        }
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    private fun apkFind(){
        println(">>>> apkFind")
        val externalStorageStats = Environment.getExternalStorageDirectory().absolutePath
        val file = File(externalStorageStats)
        val fileList = file.listFiles() ?: return
        val fileSize = fileList.size

        for((count, files) in fileList.withIndex()){
            apkFile(files.absolutePath)
            val avg = ((count+1) * 100/fileSize)
            RepositoryImpl.setProgressLive(avg)
        }
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    private fun apkFile(path: String) {
        val file = File(path)
        val fileList = file.listFiles() ?: return
        println(">>>> apkFile")

        for (files in fileList){

            if(files.absolutePath.endsWith(".apk")){
                println(">>>> apk")
                val apkName = files.name
                val apkSize = files.length()
                println(">>>> apk : $apkName")
                apkListCategory.add(ApkData(type = APK_DATA_TYPE, appIcon = getDrawable(R.drawable.circular), appName =  apkName, appUse =apkSize, appChoose = false, file = files ))
                println(">>> apkList : $apkName")
            }
        }
        RepositoryImplProgress.setApkAppList(apkListCategory)
    }

    private fun setProgressRunningViews(running: Boolean) {
        if (running) {
            appPercent.visibility = View.VISIBLE
            if (RepositoryImpl.progressLive.value in 0..100) {
                appPercent.text = "${RepositoryImpl.progressLive.value}"
            } else {
               appPercent.text = "0"
            }
            progressBar.visibility = View.VISIBLE

        } else {
            appPercent.visibility = View.GONE
        }
    }

    private fun startProgressService() {
        setProgressRunningViews(true)
    }

    fun checkStop(view: View) {
        finish()
    }

     private fun isSystemPackage(ai: ApplicationInfo): Boolean {
        return ai.flags and ApplicationInfo.FLAG_SYSTEM != 0
    }

}
