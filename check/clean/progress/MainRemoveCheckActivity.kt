package com.kyoungss.cleaner.check.clean.progress

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.kyoungss.cleaner.*
import com.kyoungss.cleaner.check.clean.CacheAdapter
import com.kyoungss.cleaner.check.clean.CheckRemoveFinishActivity

import com.kyoungss.cleaner.data.RepositoryImpl
import com.kyoungss.cleaner.data.RepositoryImplProgress

class MainRemoveCheckActivity : AppCompatActivity() {
    private lateinit var dpAdjustment: DisplayAdjustment

    var context: Context = this
   // var position = 0

    private lateinit var loadinag: TextView
    private lateinit var appPercent: TextView
    private lateinit var firstCheck: TextView
    private lateinit var secondCheck: TextView
    private lateinit var thirdCheck: TextView
    private lateinit var progressBar: CustomProgressbar
    private var cacheList = RepositoryImplProgress.getCacheList()
    private var apkAppList=  RepositoryImplProgress.getApkAppList()

    private var checkboxList =  ArrayList<CheckCacheData>()
    private var checkboxApkList =  ArrayList<CheckAPKData>()
    private  var cacheAdapter = CacheAdapter(cacheList, context)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_remove_check)
        initViews()

        RepositoryImpl.progressInt.value =  Actions.INIT_WIPER_TASK

            RepositoryImpl.progressLive.observe(this) {
                if (it in 0..100) {
                    progressBar.setProgress(it)
                }
            }

        RepositoryImpl.progressInt.observe(this) {
                if (it in 0..100) {
                    appPercent.text = it.toString()

                    if (100 == it) {
                        val intent = Intent(this, CheckRemoveFinishActivity::class.java)

                        startActivity(intent)
                        finish()


                    }
                }
            }

        startProgressService()
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        return if (keyCode == KeyEvent.KEYCODE_BACK) {
            false
        } else super.onKeyDown(keyCode, event)
    }

    private fun initViews() {
        dpAdjustment = DisplayAdjustment.getInstance(this)

        appPercent = findViewById(R.id.main_check_percent_text)
        dpAdjustment.setMargins(appPercent, 147,95,0,0)

        loadinag = findViewById(R.id.main_check_appName)
        dpAdjustment.setMargins(loadinag, 0,19,0,0)

        firstCheck = findViewById(R.id.main_check_firstCheck)
        secondCheck = findViewById(R.id.main_check_secondCheck)
        thirdCheck = findViewById(R.id.main_check_thirdCheck)
        val progressLayout = findViewById<RelativeLayout>(R.id.main_check_loading)
        dpAdjustment.setMargins(progressLayout, 0,65,0,0)
        progressBar = findViewById(R.id.progressBar)
        dpAdjustment.setMargins(progressBar, 65,30,0,0)
        dpAdjustment.setScale(progressBar.layoutParams, 400f,250f)

    }

    var appCount: Int = 0
    var sum =0

    private fun selectRemoveCache(){
        checkboxList = RepositoryImplProgress.getCheckList()

        var removeCacheSize: Long = 0
        var removeCacheSum :Long = 0

        val list :ArrayList<CheckCacheData> = RepositoryImplProgress.getCheckList()
        val itr = list.iterator()

        while (itr.hasNext()){

            val listItr = itr.next()
            listItr.file.deleteRecursively()
            println("fileremove : ${listItr.file}")
            val cacheItr = cacheList.iterator()
            removeCacheSize = listItr.cacheSize
            removeCacheSum += removeCacheSize

            println(">>> removeCacheSize $removeCacheSize")
            println(">>> removeCacheSum $removeCacheSum")

            while (cacheItr.hasNext()){
                val cacheListItr = cacheItr.next()


                if(listItr.appName == cacheListItr.appName) {
                    println(">>> 같이")
                    println(">>> listItr.appNAme : ${listItr.appName}")
                    cacheItr.remove()
                }
            }

        }
        RepositoryImplProgress.setCacheRemove(removeCacheSum)

        checkboxList.clear()
    }

    private fun selectRemoveApk(){
        checkboxApkList = RepositoryImplProgress.getCheckApkList()

        val apkCheckList : ArrayList<CheckAPKData> = RepositoryImplProgress.getCheckApkList()
        val apkItr = apkCheckList.iterator()

        var removeApkSize: Long = 0
        var removeApkSum : Long = 0


        while (apkItr.hasNext()){
            val listItr = apkItr.next()
            listItr.file.delete()
            val apkItrList= apkAppList.iterator()
            removeApkSize = listItr.apkSize
            removeApkSum += removeApkSize

            while (apkItrList.hasNext()){
                val apkListItr = apkItrList.next()

                if(listItr.appName == apkListItr.appName){
                    apkItrList.remove()
                }
            }
        }
        RepositoryImplProgress.setAPKRemove(removeApkSum)
        checkboxApkList.clear()

    }


    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        selectRemoveCache()
        selectRemoveApk()
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


}
