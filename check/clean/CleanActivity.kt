package com.kyoungss.cleaner.check.clean


import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.os.*
import android.text.format.Formatter
import android.view.View
import android.widget.*
import androidx.annotation.RequiresApi

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.kyoungss.cleaner.*
import com.kyoungss.cleaner.expand.Expandable
import com.kyoungss.cleaner.check.clean.progress.CleanCustomView
import com.kyoungss.cleaner.check.clean.progress.MainRemoveCheckActivity
import com.kyoungss.cleaner.data.RepositoryImpl
import com.kyoungss.cleaner.data.RepositoryImplProgress
import java.io.File

class CleanActivity : Activity() {
    var context: Context = this
    private lateinit var dpAdjustment: DisplayAdjustment

    private var cacheCategory = RepositoryImplProgress.getCacheList()
    private val apkCategory = RepositoryImplProgress.getApkAppList()
    private lateinit var cacheAdapter : CacheAdapter
    private lateinit var apkAdapter : ApkAdapter
    private var checkboxCacheList = arrayListOf<CheckCacheData>()
    private var checkboxApkList = arrayListOf<CheckAPKData>()
    var sum:Long = 0
    var freeSize:Long = 0L
    var totalSize:Long = 0L
    var usedSize:Long = -1L
    private var cacheFileSize: Long = 0
    private var apkFileSize: Long = 0

    val position = 0
    var otherFileSize: Long = 0

    var allFileSize:Long =0
    var cacheSizeSum:Long =0

    companion object {
        const val REQUEST_CHECK_PERMISSION = 0X02


    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        RepositoryImpl.progressInt.value = 0

        setContentView(R.layout.activity_cache)
        getUsedMemorySize()
        cacheFind()
        apkFind()
        initViews()
    }


    private fun initViews() {
        dpAdjustment = DisplayAdjustment.getInstance(this)

        checkboxApkList = RepositoryImplProgress.getCheckApkList()
        checkboxCacheList = RepositoryImplProgress.getCheckList()

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

        val cacheBoostBtn = findViewById<Button>(R.id.boost_btn)
        dpAdjustment.setMargins(cacheBoostBtn,16,0,0,16)
        dpAdjustment.setScale(cacheBoostBtn.layoutParams, 328f,48f)
        var allCacheUse:Long = 0
        for(list in cacheCategory) {
            allCacheUse += list.appUse
        }

        var boolean = false
        val position =0

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
      //  cacheCleanOnlyText.text = (cleanCache / 1024).toString()   // 단위 작게 못하겠음 추후수정
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

        val customFirstMenu = findViewById<CleanCustomView>(R.id.cache_first)
        val expandFirstMenu = findViewById<Expandable>(R.id.cache_first_expand)

        val listenerFirstMenu = View.OnClickListener {
            boolean = if (!boolean) {
                expandFirstMenu.expand()
                customFirstMenu.setCacheMenu(R.drawable.icon_menu_drop_up)

                true
            } else {
                expandFirstMenu.collapse()
                customFirstMenu.setCacheMenu(R.drawable.icon_menu_drop_down)

                false
            }
        }
        customFirstMenu.setListener(listenerFirstMenu)

        val cacheAllCheck = View.OnClickListener{
            boolean = if (!boolean) {
                cacheAdapter.setCheckAll(false, position)
                RepositoryImplProgress.getCheckList().clear()
                customFirstMenu.onRefresh()

                true
            } else {
                cacheAdapter.setCheckAll(true, position)
                customFirstMenu.onRefresh()

                false
            }
        }
        cacheAdapter = CacheAdapter(cacheCategory, context)
        val recyclerFirstMenu = findViewById<RecyclerView>(R.id.cache_first_recycler)
        ScrollView.SCROLLBARS_OUTSIDE_OVERLAY
        recyclerFirstMenu.layoutManager = LinearLayoutManager(this)
        if (cacheCategory.isEmpty()) {
            customFirstMenu.visibility = View.GONE
        } else {
            recyclerFirstMenu.adapter = cacheAdapter
        }
        val cacheAllCount = RepositoryImplProgress.getCacheList().count()

        if(checkboxCacheList.isEmpty()){
            customFirstMenu.cacheUseSize("0KB")
            customFirstMenu.cacheUseCount("0")
            customFirstMenu.cacheAllSize(Formatter.formatFileSize(context,allCacheUse))
            customFirstMenu.cacheAllCount(cacheAllCount.toString())
            customFirstMenu.onRefresh()

        }else{
            val cacheUse = checkboxCacheList[position].cacheSize
            val cacheCheckCount = checkboxCacheList.count()

            customFirstMenu.cacheUseSize(cacheUse.toString())
            customFirstMenu.cacheAllSize(Formatter.formatFileSize(context, allCacheUse))
            customFirstMenu.cacheUseCount(cacheCheckCount.toString())
            customFirstMenu.cacheAllCount(cacheAllCount.toString())
            customFirstMenu.onRefresh()

        }

        customFirstMenu.setCheckListener(cacheAllCheck)

        /////////////////////////////////////////////////////////////////

        val customSecondMenu = findViewById<CleanCustomView>(R.id.cache_second)

        val expandSecondMenu = findViewById<Expandable>(R.id.cache_second_expand)
        val listenerSecondMenu = View.OnClickListener {
            boolean = if (!boolean) {
                expandSecondMenu.expand()
                customSecondMenu.setCacheMenu(R.drawable.icon_menu_drop_up)

                true
            } else {
                expandSecondMenu.collapse()
                customSecondMenu.setCacheMenu(R.drawable.icon_menu_drop_down)

                false
            }
        }
        customSecondMenu.setListener(listenerSecondMenu)

        val recyclerSecondMenu = findViewById<RecyclerView>(R.id.cache_second_recycler)
        recyclerSecondMenu.layoutManager = LinearLayoutManager(this)

        apkAdapter = ApkAdapter(apkCategory,context)
        if (apkCategory.isEmpty()) {
            customSecondMenu.visibility = View.GONE
        } else {
            recyclerSecondMenu.adapter = apkAdapter

        }


        val apkAllCheck = View.OnClickListener{
            boolean = if (!boolean) {
                apkAdapter.setCheckAll(false, position)
                RepositoryImplProgress.getCheckApkList().clear()
                customSecondMenu.onRefresh()

                true
            } else {
                apkAdapter.setCheckAll(true, position)
                customSecondMenu.onRefresh()

                false
            }
        }
        val apkAllCount = apkCategory.count()

        if(checkboxApkList.isEmpty()){
            customSecondMenu.cacheUseSize("0KB")
            customSecondMenu.cacheUseCount("0")
            customSecondMenu.cacheAllSize(Formatter.formatFileSize(context, apkFileSize))
            customSecondMenu.cacheAllCount(apkAllCount.toString())
            customSecondMenu.onRefresh()

        }else{
            val apkUse = checkboxApkList[position].apkSize
            val apkCheckCount = checkboxApkList.count()

            customSecondMenu.cacheUseSize(apkUse.toString())
            customSecondMenu.cacheAllSize(Formatter.formatFileSize(context, apkFileSize))
            customSecondMenu.cacheUseCount(apkCheckCount.toString())
            customSecondMenu.cacheAllCount(apkAllCount.toString())
            customSecondMenu.onRefresh()

        }

        customSecondMenu.setCheckListener(apkAllCheck)

        val allChooseBtn = findViewById<Button>(R.id.allChoose)
        allChooseBtn.setOnClickListener{
            println(">>>> check all")
            boolean = if (!boolean) {

                cacheAdapter.setCheckAll(true, position)
                apkAdapter.setCheckAll(true, position)
                customFirstMenu.cacheCheck(true)
                customFirstMenu.onRefresh()

                customSecondMenu.cacheCheck(true)
                customSecondMenu.onRefresh()

                RepositoryImplProgress.getCheckList().clear()
                RepositoryImplProgress.getCheckApkList().clear()

                true
            } else {
                customFirstMenu.cacheCheck(false)
                customFirstMenu.onRefresh()
                customSecondMenu.cacheCheck(false)
                customSecondMenu.onRefresh()
                cacheAdapter.setCheckAll(false, position)
                apkAdapter.setCheckAll(false, position)


                false
            }
        }
    }

    fun cacheRemove(view: View){
        checkboxApkList = RepositoryImplProgress.getCheckApkList()
        checkboxCacheList = RepositoryImplProgress.getCheckList()

        println(">> checkboxList : $checkboxCacheList")
        if(checkboxCacheList.isNotEmpty() || checkboxApkList.isNotEmpty()){
            val intent = Intent(this, MainRemoveCheckActivity::class.java)
            this.startActivity(intent)

            finish()
        }else {
            Toast.makeText(context, "선택된 파일이 없습니다.", Toast.LENGTH_LONG).show()

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
                var applicationInfo: ApplicationInfo? = null
                try {
                    applicationInfo = packageManager.getApplicationInfo(packageName, PackageManager.GET_META_DATA)
                }catch (e: PackageManager.NameNotFoundException){
                    continue
                }
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
        var direcFileSize:Long = 0
        for (files in fileList) {
            if (files.isDirectory) {
                cacheHaveList(files.absolutePath, packageName)
                direcFileSize = files.length()

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
    private fun apkFind(){
        val externalStorageStats = Environment.getExternalStorageDirectory().absolutePath
        val file = File(externalStorageStats)
        val fileList = file.listFiles() ?: return

        for((count, files) in fileList.withIndex()){
            apkFile(files.absolutePath)
        }
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    private fun apkFile(path: String) {
        val file = File(path)
        val fileList = file.listFiles() ?: return

        for (files in fileList){

            if(files.absolutePath.endsWith(".apk")){
                val apkName = files.name
                val apkSize = files.length()
                apkFileSize += apkSize
            }
        }
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when(requestCode){
            REQUEST_CHECK_PERMISSION ->{
                apkAdapter.set(apkCategory)
                cacheAdapter.set(cacheCategory)
            }
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

    fun back(view: View) {

        finish()
    }



}