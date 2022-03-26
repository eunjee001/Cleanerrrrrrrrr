package com.kyoungss.cleaner.storage.download

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.Environment
import android.text.format.Formatter
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.LinearLayoutManager
import com.kyoungss.cleaner.DisplayAdjustment
import com.kyoungss.cleaner.R
import com.kyoungss.cleaner.data.RepositoryImpl
import com.kyoungss.cleaner.storage.folder.ManageFolderActivity
import java.io.File
import java.util.*

class DownActivity : Activity() {
    var context: Context = this
    private lateinit var dpAdjustment: DisplayAdjustment

    var uriArr = arrayListOf("0")
    private var allInfo: TextView? = null
    private var removeDown: Button? = null
    private val downListCategory = ArrayList<DownData>()
    private var checkboxList = ArrayList<CheckDownData>()
    private lateinit var allSize: TextView
    private lateinit var allCount: TextView
    var downAllSize:Long = 0
    var downAllCount = 0
    private var downAdapter = DownAdapter(downListCategory, context)
    var popupWindow : PopupWindow ?= null
    private lateinit var downMenu:Button

    companion object {
        const val TYPE_DOWN = 0L
        const val TYPE_DOWN_TIME = 0x01
        const val TYPE_DOWN_PACKAGE =0x02

    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_download_all)
        val recyclerView = findViewById<RecyclerView>(R.id.recycler_download)

        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = downAdapter


        downloadFind()
        initViews()
    }
    override fun onDestroy() {
        super.onDestroy()
        if(null != popupWindow  && popupWindow!!.isShowing){
            popupWindow!!.dismiss()
        }
    }
    private fun initViews() {
        dpAdjustment = DisplayAdjustment.getInstance(this)

        val downBack = findViewById<Button>(R.id.downBack)
        dpAdjustment.setMargins(downBack,16,16,0,0 )
        dpAdjustment.setScale(downBack.layoutParams,24f,24f)

        val downTitle = findViewById<TextView>(R.id.downTitle)
        dpAdjustment.setMargins(downTitle, 12,12,0,0)

        allSize = findViewById(R.id.infoTitle)
        dpAdjustment.setMargins(allSize, 16,33,0,0)
        allSize.text = Formatter.formatFileSize(context, downAllSize)

        allCount = findViewById(R.id.infoTitleCount)
        dpAdjustment.setMargins(allCount, 14,34,0,0)
        allCount.text = downAllCount.toString() + "개"

       downMenu = findViewById<Button>(R.id.menu)
        dpAdjustment.setMargins(downMenu, 0,31,19,0)
        dpAdjustment.setScale(downMenu.layoutParams, 24f,24f)


        val downRecyclerView: RecyclerView = findViewById(R.id.recycler_download)
        downRecyclerView.layoutManager = LinearLayoutManager(this)
        dpAdjustment.setMargins(downRecyclerView, 0,0,0,0)

        removeDown = findViewById(R.id.remove_btn)
        dpAdjustment.setScale(removeDown!!.layoutParams, 328f,48f)
        dpAdjustment.setMargins(removeDown!!, 16,0,0,16)

        removeDown?.setOnClickListener{
            selectDownRemove()
        }
        val view: View = layoutInflater.inflate(R.layout.popup_etc_layout, null)

        downMenu.setOnClickListener{

            popupWindow = PopupWindow(
                view,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                true
            )
            popupWindow!!.showAsDropDown(downMenu, -(downMenu.width * 2), downMenu.y.toInt())
            popupWindow!!.setBackgroundDrawable(ColorDrawable(Color.WHITE))

        }

        val firstPopupTextView = view.findViewById<TextView>(R.id.popup_text_first)
        firstPopupTextView.setOnClickListener {
            val intent = Intent(this, ManageDownloadActivity::class.java)
            this.startActivity(intent)
            finish()
        }

        val secondsPopupTextView = view.findViewById<TextView>(R.id.popup_text_second)
        secondsPopupTextView.setOnClickListener {

            val intent = Intent(this, ManageDownloadSizeActivity::class.java)
            this.startActivity(intent)
            finish()
        }

        val thirdsPopupTextView = view.findViewById<TextView>(R.id.popup_text_third)
        thirdsPopupTextView.setOnClickListener {

            val intent = Intent(this, DownActivity::class.java)
            this.startActivity(intent)

            finish()
        }

    }

    private fun downloadFind() {
        val externalStorage = Environment.getExternalStorageDirectory().absolutePath
        val path = "$externalStorage/Download"
        val dir = File(path)
        val fileList = arrayOf(dir)
        for (files in fileList) {
            downloadList(files.absolutePath)
        }
    }

    private fun downloadList(path: String) {
        val file = File(path)
        val fileList = file.listFiles()?: return

        for (files in fileList) {

            val downSize: Long = arrayListOf(files.length()).sum()
            val downCount = arrayListOf(files).count()

            downAllCount += downCount
            downAllSize += downSize
            downListCategory.add(
                DownData(
                    type = TYPE_DOWN,
                    downloadIcon = R.drawable.icon_folder,
                    downloadName = files.name,
                    downloadTime = TYPE_DOWN_TIME,
                    downloadSize = downSize,
                    downloadChoose = false,
                    downloadFile = files,
                    diffTime = 0
                )
            )
        }

        downListCategory.sortBy{
            it.downloadName
        }

        downAdapter.set(downListCategory)
    }

    private fun selectDownRemove(){
        checkboxList = RepositoryImpl.getDownList()
        if (downListCategory.isNotEmpty()) {
            val checkItr = checkboxList.iterator()
            while (checkItr.hasNext()) {
                val checkListItr = checkItr.next()
                val checkDocItr = downListCategory.iterator()
                while (checkDocItr.hasNext()) {
                    val checkDocListItr = checkDocItr.next()
                    if (checkListItr.downName == checkDocListItr.downloadName) {
                        checkDocListItr.downloadFile.delete()
                        checkDocItr.remove()
                        downAdapter.set(downListCategory)

                    }
                }
                checkItr.remove()
            }
        }
    }


    fun back(view: View) {
        val intent = Intent(this, ManageFolderActivity::class.java)
        this.startActivity(intent)

        finish()
    }
}



