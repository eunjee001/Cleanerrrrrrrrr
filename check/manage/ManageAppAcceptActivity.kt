package com.kyoungss.cleaner.check.manage

import android.app.Activity
import android.app.AppOpsManager
import android.app.AppOpsManager.MODE_ALLOWED
import android.app.AppOpsManager.OPSTR_GET_USAGE_STATS
import android.app.usage.UsageStats
import android.app.usage.UsageStatsManager
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable

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
import com.kyoungss.cleaner.data.RepositoryImplProgress
import java.io.File


class ManageAppAcceptActivity  : Activity() {

    var context: Context = this
    private lateinit var dpAdjustment: DisplayAdjustment
    private var cacheCategory = RepositoryImplProgress.getCacheList()
    private var appIcons: ImageView? = null
    private var appNames: TextView? = null
    private var appUse: TextView? = null
    private var installDate: TextView? = null
    private var dateInfo: TextView? = null
    private var view : View ?= null
    var sum:Long = 0
    var freeSize:Long = 0L
    var totalSize:Long = 0L
    var usedSize:Long = -1L
    private var cacheFileSize: Long = 0
    var otherFileSize: Long = 0

    var allFileSize:Long =0
    var handler:Handler = Handler()

    private val manageAcceptListCategory = RepositoryImplProgress.getManageAccept()

    private var manageAcceptAdapter = ManageAcceptAdapter(manageAcceptListCategory, context)

    companion object {
        const val PERMISSION_EXTERNAL_STORAGE = 0x01
        const val AppInfo = 0x02
        const val REQUEST_UNINSTALL_PERMISSION = 0X03
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_manage_accept)
        val recyclerView = findViewById<RecyclerView>(R.id.recycler_manage)

        init()

        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)


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

        recyclerView.adapter = manageAcceptAdapter

        context = this
        cacheFind()
        getUsedMemorySize()
        initViews()

    }



    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    private fun init() {

        check()
    }

    fun onClick(view: View) {
        when (view.id) {
            R.id.all_manage -> {
                val intent = Intent(this, ManageAppActivity::class.java)
                this.startActivity(intent)
                finish()
            }
            R.id.Agree_manage -> {
                val intent = Intent(this, ManageAppAgreeActivity::class.java)
                this.startActivity(intent)
                finish()
            }
            R.id.Accept_manage -> {
                val intent = Intent(this, ManageAppAcceptActivity::class.java)
                this.startActivity(intent)
                finish()
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    fun check(){
        if (!checkForPermission()) {
            Toast.makeText(this, "Failed", Toast.LENGTH_LONG).show()
            startActivity(Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS))
        } else {
            val usageStats = getAppUsageStats()
            showAppUsageStats(usageStats)
        }
    }
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

        val manageRecyclerView: RecyclerView = findViewById(R.id.recycler_manage)
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
       // dpAdjustment.setScale(layoutPopUp.layoutParams, 101f,110f)

        val firstPopupTextView = view!!.findViewById<TextView>(R.id.popup_text_first)
        dpAdjustment.setMargins(firstPopupTextView, 15,15,15,0)
        firstPopupTextView.setOnClickListener {

            manageAcceptListCategory.sortByDescending {
                it.appUse
            }

            manageAcceptAdapter.set(manageAcceptListCategory)

            Toast.makeText(this, "용량 많은 순", Toast.LENGTH_LONG).show()
        }

        val secondsPopupTextView = view!!.findViewById<TextView>(R.id.popup_text_second)
        dpAdjustment.setMargins(secondsPopupTextView, 15,15,15,0)

        secondsPopupTextView.setOnClickListener {

            manageAcceptListCategory.sortBy {
                it.appUse
            }

            manageAcceptAdapter.set(manageAcceptListCategory)

            Toast.makeText(this, "용량 적은 순", Toast.LENGTH_LONG).show()
        }

        val thirdsPopupTextView = view!!.findViewById<TextView>(R.id.popup_text_third)
        dpAdjustment.setMargins(thirdsPopupTextView, 15,15,15,0)

        thirdsPopupTextView.setOnClickListener {

            manageAcceptListCategory.sortByDescending {
                it.appDate
            }

            manageAcceptAdapter.set(manageAcceptListCategory)
            Toast.makeText(this, "최신 실행 순", Toast.LENGTH_LONG).show()

        }

    }

    private fun checkForPermission(): Boolean {
                val appOps = getSystemService(Context.APP_OPS_SERVICE) as AppOpsManager
                val mode =
                    appOps.checkOpNoThrow(OPSTR_GET_USAGE_STATS, Process.myUid(),packageName)
                return mode == MODE_ALLOWED
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    private fun showAppUsageStats (usageStats: MutableList<UsageStats>) {
        val position: Int ?= null
        usageStats.sortWith(Comparator { right, left ->
            compareValues(
                left.lastTimeUsed,
                right.lastTimeUsed
            )
        })
        usageStats.forEach { it ->
            for (i in manageAcceptListCategory) {
                if (it.packageName.equals(i.appPackage)) {
                    i.appDate = it.lastTimeUsed
                }
            }
        }
        manageAcceptAdapter.set(manageAcceptListCategory)
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
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

   override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
       super.onActivityResult(requestCode, resultCode, data)
       when(requestCode){
         REQUEST_UNINSTALL_PERMISSION ->{
             manageAcceptAdapter.set(manageAcceptListCategory)
         }
        }
    }
    private fun cacheFind() {
        val externalStorage = Environment.getExternalStorageDirectory().absolutePath
        val path = "$externalStorage/Android/data"
        val file = File(path)
        val fileList = file.listFiles() ?: return
        // var fileSize : Long = 0
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


