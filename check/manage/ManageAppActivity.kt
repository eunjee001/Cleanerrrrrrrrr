package com.kyoungss.cleaner.check.manage

import android.annotation.SuppressLint
import android.app.Activity
import android.app.AppOpsManager
import android.app.AppOpsManager.MODE_ALLOWED
import android.app.AppOpsManager.OPSTR_GET_USAGE_STATS
import android.app.usage.UsageStats
import android.app.usage.UsageStatsManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.drawable.ColorDrawable

import android.net.Uri
import android.os.*
import android.provider.Settings
import android.text.format.Formatter

import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.kyoungss.cleaner.*
import java.util.*
import com.kyoungss.cleaner.data.RepositoryImpl
import com.kyoungss.cleaner.data.RepositoryImplProgress
import java.io.File


class ManageAppActivity  : Activity() {

    var context: Context = this
    private lateinit var dpAdjustment: DisplayAdjustment
    private var cacheCategory = RepositoryImplProgress.getCacheList()

    private var appIcons: ImageView? = null
    private var appNames: TextView? = null
    private var appUse: TextView? = null
    private var installDate: TextView? = null
    private var dateInfo: TextView? = null
    private var view : View ?= null

    lateinit var allManage :Button
    lateinit var agreeManage :Button
    lateinit var acceptManage :Button

    private val manageListCategory = RepositoryImpl.getFileList()
    private var manageAgreeListCategory = RepositoryImplProgress.getManageAgree()
    private var manageAcceptListCategory = RepositoryImplProgress.getManageAccept()
    var sum:Long = 0
    var freeSize:Long = 0L
    var totalSize:Long = 0L
    var usedSize:Long = -1L
    private var cacheFileSize: Long = 0
    lateinit var manageRecyclerView: RecyclerView
    val position:Int = 0
    var otherFileSize: Long = 0
    var allFileSize:Long =0
    private var manageAdapter = ManageAdapter(manageListCategory, context)
    private var manageAgreeAdapter = ManageAgreeAdapter(manageAgreeListCategory, context)
    private var manageAcceptAdapter = ManageAcceptAdapter(manageAcceptListCategory, context)
    var uninstallIntent: Intent? = null
    companion object {
        const val AppInfo = 0x02
        const val REQUEST_UNINSTALL_PERMISSION = 0X03
        const val REQUEST_UNINSTALL_PERMISSION_TWO = 0X04
    }
    var handler: Handler = Handler()
    var list = arrayListOf<String>()

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP_MR1)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_manage)
        val recyclerView = findViewById<RecyclerView>(R.id.recycler_manage)

        init()

        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        manageAdapter.setItemClickListener(object : OnItemClickListenerAccept {
            override fun onItemClick(position: List<ManageData>) {
                TODO("Not yet implemented")
            }

            override fun onItemLongClick(position: Int): Boolean {

                uninstallIntent = Intent(
                Intent.ACTION_DELETE,
                Uri.parse("package:${manageListCategory[position].appPackage}")
                )

                    if (manageListCategory[position].appChoose ) {
                        Toast.makeText(
                            context,
                            "설치 제거 안됨",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        startActivityForResult(uninstallIntent, REQUEST_UNINSTALL_PERMISSION)

                }

                return false
            }

            override fun onItemSelected(position: Int, selected: Boolean) {
            }

        })
        manageAgreeAdapter.setItemClickListener(object : OnItemClickListenerAccept {
            override fun onItemClick(position: List<ManageData>) {
                TODO("Not yet implemented")
            }

            override fun onItemLongClick(position: Int): Boolean {

                val uninstallIntent = Intent(
                    Intent.ACTION_DELETE,
                    Uri.parse("package:${manageAgreeListCategory[position].appPackage}")
                )

                    startActivityForResult(uninstallIntent, REQUEST_UNINSTALL_PERMISSION_TWO)

                return false
            }

            override fun onItemSelected(position: Int, selected: Boolean) {
            }

        })
        manageAcceptAdapter.setItemClickListenerTwo(object : OnItemClickListenerAccept {

            override fun onItemClick(position: List<ManageData>) {
                TODO("Not yet implemented")
            }

            override fun onItemLongClick(position: Int): Boolean {

                val r = Runnable {
                    manageAcceptAdapter.set(manageAcceptListCategory)
                }
                handler.post(r)


                return false

            }

            override fun onItemSelected(position: Int, selected: Boolean) {

            }

        })
        manageAcceptAdapter.setItemClickListener(object : OnItemClickListener {
            override fun onItemClick(position: List<ManageData>) {
                TODO("Not yet implemented")
            }

            override fun onItemLongClick(position: Int): Boolean {

                Toast.makeText(
                    context,
                    "설치 제거 안됨",
                    Toast.LENGTH_SHORT
                ).show()

                return false
            }

            override fun onItemSelected(position: Int, selected: Boolean) {
            }

        })
        manageAgreeAdapter.setItemClickListenerTwo(object : OnItemClickListenerAgree {

            override fun onItemClick(position: List<ManageData>) {
                TODO("Not yet implemented")
            }

            override fun onItemLongClick(position: Int): Boolean {

                val r = Runnable {

                    manageAgreeAdapter.set(manageAgreeListCategory)

                }
                handler.post(r)

                return false

            }
            override fun onItemSelected(position: Int, selected: Boolean) {

            }

        })

        recyclerView.adapter = manageAdapter

        context = this
        cacheFind()
        getUsedMemorySize()
        initViews()

    }





    @RequiresApi(Build.VERSION_CODES.LOLLIPOP_MR1)
    private fun init() {
        check()
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP_MR1)
    fun check(){
        if (!checkForPermission()) {
            Toast.makeText(this, "Failed", Toast.LENGTH_LONG).show()
            startActivity(Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS))
        } else {
            val usageStats = getAppUsageStats()
            showAppUsageStats(usageStats)
        }
    }
    @SuppressLint("SetTextI18n")
    private fun initViews() {
        dpAdjustment = DisplayAdjustment.getInstance(this)
        val menuBar = findViewById<Button>(R.id.menu)


        val cacheCleanBox = findViewById<RelativeLayout>(R.id.cacheCleanBox)
        dpAdjustment.setMargins(cacheCleanBox,16,16,0,0)
        dpAdjustment.setScale(cacheCleanBox.layoutParams,160f,120f)

        val cacheCleanBoxText = findViewById<TextView>(R.id.cacheClean)
        dpAdjustment.setMargins(cacheCleanBoxText,10,6,0,0)

        val cacheCleanOnlyText= findViewById<TextView>(R.id.onlyCache)
        dpAdjustment.setMargins(cacheCleanOnlyText,43,10,0,0)

        val cacheKB = findViewById<TextView>(R.id.cacheKB)
        dpAdjustment.setMargins(cacheKB,0,52,0,0)

        val cacheCleanUseText = findViewById<TextView>(R.id.useCache)
        dpAdjustment.setMargins(cacheCleanUseText,36,0,0,0)

        val cacheCleanSlash = findViewById<TextView>(R.id.slash)
        dpAdjustment.setMargins(cacheCleanSlash,0,0,0,0)

        val cacheCleanAllText = findViewById<TextView>(R.id.allCache)
        dpAdjustment.setMargins(cacheCleanAllText,0,0,0,0)

        val memoryBox = findViewById<RelativeLayout>(R.id.useMem)
        dpAdjustment.setMargins(memoryBox,8,16,0,0)
        dpAdjustment.setScale(memoryBox.layoutParams,160f,120f)

        val memoryBoxText = findViewById<TextView>(R.id.useMemory)
        dpAdjustment.setMargins(memoryBoxText,10,6,0,0)

        val memoryOnlyText = findViewById<TextView>(R.id.onlyMemory)
        dpAdjustment.setMargins(memoryOnlyText,57,10,0,0)

        val memoryBoxPercent = findViewById<TextView>(R.id.memoryPercent)
        dpAdjustment.setMargins(memoryBoxPercent,0,52,0,0)

        val memoryUseText = findViewById<TextView>(R.id.useMyMemory)
        dpAdjustment.setMargins(memoryUseText,36,0,0,0)

        val memorySlash = findViewById<TextView>(R.id.use_slash)
        dpAdjustment.setMargins(memorySlash,0,0,0,0)

        val memoryAllSlash = findViewById<TextView>(R.id.allMemory)
        dpAdjustment.setMargins(memoryAllSlash,0,0,0,0)

        allManage = findViewById(R.id.all_manage)
        agreeManage = findViewById(R.id.Agree_manage)
        acceptManage = findViewById(R.id.Accept_manage)

        var allCacheUse:Long = 0
        for(list in cacheCategory) {
            allCacheUse += list.appUse
        }
        memoryUseText.text = Formatter.formatFileSize(context, usedSize)
        memoryAllSlash.text = Formatter.formatFileSize(context, totalSize)
       val memoryPercent = 100* usedSize / totalSize
        memoryOnlyText.text = memoryPercent.toString()


        var allFile:Long = 0
        for(list in cacheCategory) {
            allFile += list.appUse
        }
        allFile += allFileSize
        cacheCleanUseText.text = Formatter.formatFileSize(context, allCacheUse)
        cacheCleanAllText.text = Formatter.formatFileSize(context, allFile)
        val cleanCache = RepositoryImplProgress.getCacheRemove()

        if (0 <= (cleanCache / Actions.KB) && (cleanCache / Actions.KB) < 1000) {
            cacheKB.text = "KB"
            cacheCleanOnlyText.text = (cleanCache / Actions.KB).toString()

        } else if (0 < (cleanCache / Actions.MB) && (cleanCache / Actions.MB) < 1000) {
            cacheKB.text = "MB"
            cacheCleanOnlyText.text = (cleanCache / Actions.MB).toString()
        } else if (0 < (cleanCache / Actions.GB) && (cleanCache / Actions.GB) <1000) {
            cacheKB.text = "GB"
            cacheCleanOnlyText.text = (cleanCache / Actions.GB).toString()
        }

        appIcons = findViewById(R.id.cache_app_icon)
        appNames = findViewById(R.id.cache_app_name)
        appUse = findViewById(R.id.cache_use)
        installDate = findViewById(R.id.cache_date)
        dateInfo = findViewById(R.id.cache_date_info)


        manageRecyclerView = findViewById(R.id.recycler_manage)
        manageRecyclerView.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        view= layoutInflater.inflate(R.layout.popup_layout, null)

        menuBar.setOnClickListener {
            val popupWindow = PopupWindow(
                view,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
                , true
            )

            popupWindow.showAsDropDown(menuBar, -(menuBar.width*3),-101)

            popupWindow.setBackgroundDrawable(ColorDrawable(Color.WHITE))


        }

        val layoutPopUp = view!!.findViewById<RelativeLayout>(R.id.layoutPopUp)
       dpAdjustment.setMargins(layoutPopUp, 243,20,16,0)

        val firstPopupTextView = view!!.findViewById<TextView>(R.id.popup_text_first)
        dpAdjustment.setMargins(firstPopupTextView, 15,15,15,0)
        firstPopupTextView.setOnClickListener {

            manageListCategory.sortByDescending {
                it.appUse
            }

            manageAdapter.set(manageListCategory)

            Toast.makeText(this, "용량 많은 순", Toast.LENGTH_LONG).show()
        }

        val secondsPopupTextView = view!!.findViewById<TextView>(R.id.popup_text_second)
        dpAdjustment.setMargins(secondsPopupTextView, 15,15,15,0)

        secondsPopupTextView.setOnClickListener {

            manageListCategory.sortBy {
                it.appUse
            }

            manageAdapter.set(manageListCategory)

            Toast.makeText(this, "용량 적은 순", Toast.LENGTH_LONG).show()
        }

        val thirdsPopupTextView = view!!.findViewById<TextView>(R.id.popup_text_third)
        dpAdjustment.setMargins(thirdsPopupTextView, 15,15,15,0)

        thirdsPopupTextView.setOnClickListener {

            manageListCategory.sortByDescending {
                it.appDate
            }

            manageAdapter.set(manageListCategory)
            Toast.makeText(this, "최신 실행 순", Toast.LENGTH_LONG).show()

        }


    }
    private fun cacheFind() {
        val externalStorage = Environment.getExternalStorageDirectory().absolutePath
        val path = "$externalStorage/Android/data"
        val file = File(path)
        val fileList = file.listFiles() ?: return
        for ((count, files) in fileList.withIndex()) {
            cacheList(files.absolutePath, files.name)
            otherFileSize = files.length()
            allFileSize += otherFileSize


        }

    }



    private fun cacheList(path: String, packageName : String) {
        val file = File(path)
        val fileList = file.listFiles() ?: return

        for (files in fileList) {
            if (files.name.equals("cache")){
                cacheHaveList(files.absolutePath, packageName)
            }else{
                otherFileSize = files.length()

                cacheNoHaveList(files.absolutePath, packageName)
            }
            allFileSize += otherFileSize

        }


    }
    private fun cacheHaveList(path:String, packageName : String) {
        val file = File(path)
        val fileList = file.listFiles() ?: return
        for (files in fileList) {
            if (files.isDirectory) {
                cacheHaveList(files.absolutePath, packageName)

            }else if(files.isFile) {
                cacheFileSize = files.length()

            }
        }

    }
    private fun cacheNoHaveList(path:String, packageName : String) {

        val file = File(path)
        val fileList = file.listFiles() ?: return
        for (files in fileList) {
            if (files.isDirectory) {
                cacheNoHaveList(files.absolutePath, packageName)
            }else if(files.isFile) {
                otherFileSize = files.length()
            }
            allFileSize += otherFileSize

        }

    }
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    private fun checkForPermission(): Boolean {
                val appOps = getSystemService(Context.APP_OPS_SERVICE) as AppOpsManager
                val mode =
                    appOps.checkOpNoThrow(OPSTR_GET_USAGE_STATS, Process.myUid(),packageName)
                return mode == MODE_ALLOWED
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    private fun showAppUsageStats (usageStats: MutableList<UsageStats>) {
        usageStats.sortWith { right, left ->
            compareValues(
                left.lastTimeUsed,
                right.lastTimeUsed
            )
        }
        usageStats.forEach {
            for (i in manageListCategory) {
                if (it.packageName.equals(i.appPackage)) {
                    i.appDate = it.lastTimeUsed
                }
            }
        }
        manageAdapter.set(manageListCategory)
    }
    @SuppressLint("ResourceType")
    fun onClick(view: View) {
        when (view.id) {
            R.id.all_manage -> {
                manageRecyclerView.adapter = manageAdapter

                manageAdapter.set(manageListCategory)
                allManage.setBackgroundResource(R.drawable.filled_btn_small_ena)
                allManage.setTextColor(Color.parseColor(getString(R.color.tachyon_theme_white)))
                agreeManage.setBackgroundResource(R.drawable.line_btn_small_disabled_ena)
                agreeManage.setTextColor(Color.parseColor(getString(R.color.tachyon_theme_gray_dark_slight)))
                acceptManage.setBackgroundResource(R.drawable.line_btn_small_disabled_ena)
                acceptManage.setTextColor(Color.parseColor(getString(R.color.tachyon_theme_gray_dark_slight)))

            }
            R.id.Agree_manage -> {

                println(">>>> 허용, list : $manageAgreeListCategory")
                manageAgreeListCategory = RepositoryImplProgress.getManageAgree()

                manageRecyclerView.adapter = manageAgreeAdapter
                manageAgreeAdapter.set(manageAgreeListCategory)
                allManage.setBackgroundResource(R.drawable.line_btn_small_disabled_ena)
                allManage.setTextColor(Color.parseColor(getString(R.color.tachyon_theme_gray_dark_slight)))
                agreeManage.setBackgroundResource(R.drawable.filled_btn_small_ena)
                agreeManage.setTextColor(Color.parseColor(getString(R.color.tachyon_theme_white)))
                acceptManage.setBackgroundResource(R.drawable.line_btn_small_disabled_ena)
                acceptManage.setTextColor(Color.parseColor(getString(R.color.tachyon_theme_gray_dark_slight)))

            }
            R.id.Accept_manage -> {
                println(">>>> 제외, list : $manageAcceptListCategory")
                manageAcceptListCategory = RepositoryImplProgress.getManageAccept()

                manageRecyclerView.adapter = manageAcceptAdapter

                manageAcceptAdapter.set(manageAcceptListCategory)
                allManage.setBackgroundResource(R.drawable.line_btn_small_disabled_ena)
                allManage.setTextColor(Color.parseColor(getString(R.color.tachyon_theme_gray_dark_slight)))
                agreeManage.setBackgroundResource(R.drawable.line_btn_small_disabled_ena)
                agreeManage.setTextColor(Color.parseColor(getString(R.color.tachyon_theme_gray_dark_slight)))
                acceptManage.setBackgroundResource(R.drawable.filled_btn_small_ena)
                acceptManage.setTextColor(Color.parseColor(getString(R.color.tachyon_theme_white)))

            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP_MR1)
    private fun getAppUsageStats(): MutableList<UsageStats> {
        val cal = Calendar.getInstance()
        cal.add(Calendar.YEAR, -1)
        val usageStatsManager = getSystemService(Context.USAGE_STATS_SERVICE) as UsageStatsManager
        return usageStatsManager.queryUsageStats(
            UsageStatsManager.INTERVAL_YEARLY,
            cal.timeInMillis,
            System.currentTimeMillis()
        )
    }

    private fun isAppInstalled (packageName: String, packageManager:PackageManager) : Boolean{
        return try{
            packageManager.getPackageInfo(packageName, 0)
            println(">>> 설치됨")
            true
        }catch (ex : PackageManager.NameNotFoundException){
            false
        }
    }

   override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
       super.onActivityResult(requestCode, resultCode, data)
       when(requestCode) {
           REQUEST_UNINSTALL_PERMISSION -> {

               val itr = manageListCategory.iterator()

               println(">>> manage : $manageListCategory")

               val packageManager: PackageManager = packageManager
               while (itr.hasNext()) {
                   val itrNext = itr.next()


                   if (isAppInstalled(itrNext.appPackage, packageManager)) {
                       println(">>> ?")
                   } else {
                       val itrAgree = manageAgreeListCategory.iterator()
                        while (itrAgree.hasNext()) {
                            val itrNextAgree = itrAgree.next()
                            println(">>>> xx")
                            if (itrNext.appName == itrNextAgree.appName) {
                                itrAgree.remove()
                            }
                        }
                       itr.remove()
                       manageAdapter.set(manageListCategory)
                       manageAgreeAdapter.set(manageAgreeListCategory)

                   }

                   println(">>> pkgName : ${uninstallIntent!!.dataString!!}, itrNext :$itrNext")
                   println(">>> request: $requestCode")
               }

               println(">>> uninstallIntent : ${uninstallIntent!!.dataString}")

           }
           REQUEST_UNINSTALL_PERMISSION_TWO -> {

               val itr = manageAgreeListCategory.iterator()
               println(">>> manage : $manageListCategory")

               val packageManager: PackageManager = packageManager
               while (itr.hasNext()) {
                   val itrNext = itr.next()


                   if (isAppInstalled(itrNext.appPackage, packageManager)) {
                       println(">>> ?")
                   } else {
                       println(">>>> xx")
                       val itrManage = manageListCategory.iterator()

                       while (itrManage.hasNext()) {
                           val itrNextManage = itrManage.next()

                           println(">> ${itrNext.appPackage} , ${itrNextManage.appPackage}")
                           if (itrNext.appPackage == itrNextManage.appPackage) {
                               itrManage.remove()
                           }
                       }
                       itr.remove()
                       manageAgreeAdapter.set(manageAgreeListCategory)
                       manageAdapter.set(manageListCategory)

                   }


               }

           }       }
    }

    private fun getUsedMemorySize(): Long {
        try {
            val info: Runtime = Runtime.getRuntime()
            freeSize = info.freeMemory()
            totalSize = info.totalMemory()
            usedSize = totalSize - freeSize
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return usedSize
    }

    fun close(view: View) {
        finish()
    }
}


