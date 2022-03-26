package com.kyoungss.cleaner.storage.download

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
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.kyoungss.cleaner.Actions
import com.kyoungss.cleaner.DisplayAdjustment
import com.kyoungss.cleaner.expand.Expandable
import com.kyoungss.cleaner.R
import com.kyoungss.cleaner.data.RepositoryImpl
import com.kyoungss.cleaner.storage.CheckStorageSecondData
import java.io.File
import kotlin.collections.ArrayList

class ManageDownloadSizeActivity : AppCompatActivity() {

    var context: Context = this
    private lateinit var dpAdjustment: DisplayAdjustment

    private val twoGBListCategory = ArrayList<DownData>()
    private val oneGBListCategory = ArrayList<DownData>()
    private val oneGBDownListCategory = ArrayList<DownData>()
    private val fiveMBListCategory = ArrayList<DownData>()
    private val oneMBListCategory = ArrayList<DownData>()
    private val downListCategory = ArrayList<DownData>()
    private lateinit var allSize: TextView
    private lateinit var allCount: TextView
    private var removeDown: Button? = null

    private var oneMBDownAdapter = DownSizeAdapter(oneMBListCategory, context)
    private var fiveMBDownAdapter = DownSizeAdapter(fiveMBListCategory, context)
    private var oneGBDownAdapter = DownSizeAdapter(oneGBDownListCategory, context)
    private var oneGBAdapter = DownSizeAdapter(oneGBListCategory, context)
    private var twoGBAdapter = DownSizeAdapter(twoGBListCategory, context)

    private var twoGBCheckboxList = ArrayList<CheckStorageSecondData>()
    private var oneGBCheckboxList = ArrayList<CheckStorageSecondData>()
    private var oneGBDownCheckboxList = ArrayList<CheckStorageSecondData>()
    private var fiveMBDownCheckboxList = ArrayList<CheckStorageSecondData>()
    private var oneMBDownCheckboxList = ArrayList<CheckStorageSecondData>()
    private var checkboxList = ArrayList<CheckDownData>()
    private var allInfo: TextView? = null
    var downsize: Long =0
    var selectBtn: CheckBox? = null
    var downAllSize:Long = 0
    var downAllCount = 0
    var popupWindow : PopupWindow ?= null

    companion object {
        const val TYPE_DOWN = 0L
        const val TYPE_DOWN_TIME = 0x01

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_download_size)

        val menuBar = findViewById<Button>(R.id.menu)
        val view: View = layoutInflater.inflate(R.layout.popup_etc_layout, null)

        menuBar.setOnClickListener{
            popupWindow = PopupWindow(view, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true)
            popupWindow!!.showAsDropDown(menuBar, -205, -100)
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
        downloadFind()

        initViews()
    }
    override fun onDestroy() {
        super.onDestroy()
        if(popupWindow != null && popupWindow!!.isShowing){
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

        val downMenu = findViewById<Button>(R.id.menu)
        dpAdjustment.setMargins(downMenu, 0,31,19,0)
        dpAdjustment.setScale(downMenu.layoutParams, 24f,24f)
        ///////////////////////twoGB////////////////////////
        val downloadCustomTwoGB = findViewById<DownloadSizeCustomView>(R.id.download_twoGB)
        dpAdjustment.setMargins(downloadCustomTwoGB, 0,0,0,0)
        dpAdjustment.setScale(downloadCustomTwoGB.layoutParams, 360f,56f)

        val expandTwoGB = findViewById<Expandable>(R.id.download_twoGB_expand)

         val twoGBLine = findViewById<View>(R.id.line_twoGB)
        dpAdjustment.setMargins(twoGBLine, 0,0,0,0)
        dpAdjustment.setScale(twoGBLine.layoutParams, 360f,5f)

        //////////////////////////////////oneGB////////////////////////////
        val downloadCustomOneGB = findViewById<DownloadSizeCustomView>(R.id.download_oneGB)
        dpAdjustment.setMargins(downloadCustomOneGB, 0,0,0,0)
        dpAdjustment.setScale(downloadCustomOneGB.layoutParams, 360f,56f)

        val expandOneGB = findViewById<Expandable>(R.id.download_oneGB_expand)

        val oneGBLine = findViewById<View>(R.id.line_oneGB)
        dpAdjustment.setMargins(oneGBLine, 0,0,0,0)
        dpAdjustment.setScale(oneGBLine.layoutParams, 360f,5f)

        /////////////////////////oneGBDown//////////////////////
        val downloadCustomOneGBDown = findViewById<DownloadSizeCustomView>(R.id.download_oneGB_Down)
        dpAdjustment.setMargins(downloadCustomOneGBDown, 0,0,0,0)
        dpAdjustment.setScale(downloadCustomOneGBDown.layoutParams, 360f,56f)

        val expandOneGBDown = findViewById<Expandable>(R.id.download_oneGB_Down_expand)

        val oneGBDownLine = findViewById<View>(R.id.line_oneGB_Down)
        dpAdjustment.setMargins(oneGBDownLine, 0,0,0,0)
        dpAdjustment.setScale(oneGBDownLine.layoutParams, 360f,5f)

        ////////////////////////////500MB Down///////////////////////////
        val downloadCustomFiveMBDown = findViewById<DownloadSizeCustomView>(R.id.download_500MB_Down)
        dpAdjustment.setMargins(downloadCustomFiveMBDown, 0,0,0,0)
        dpAdjustment.setScale(downloadCustomFiveMBDown.layoutParams, 360f,56f)

        val expandFiveMBDown = findViewById<Expandable>(R.id.download_500MB_Down_expand)

        val fiveMBDownLine = findViewById<View>(R.id.line_500MB_Down)
        dpAdjustment.setMargins(fiveMBDownLine, 0,0,0,0)
        dpAdjustment.setScale(fiveMBDownLine.layoutParams, 360f,5f)

        //////////////////////////100MB Down//////////////////////////////
        val downloadCustomOneMBDown = findViewById<DownloadSizeCustomView>(R.id.download_100MB_Down)
        dpAdjustment.setMargins(downloadCustomOneMBDown, 0,0,0,0)
        dpAdjustment.setScale(downloadCustomOneMBDown.layoutParams, 360f,56f)

        val expandOneMBDown = findViewById<Expandable>(R.id.download_100MB_Down_expand)

        val oneMBDownLine = findViewById<View>(R.id.line_100MB_Down)
        dpAdjustment.setMargins(oneMBDownLine, 0,0,0,0)
        dpAdjustment.setScale(oneMBDownLine.layoutParams, 360f,5f)

        removeDown = findViewById(R.id.download_size_btn)
        dpAdjustment.setScale(removeDown!!.layoutParams, 328f,48f)
        dpAdjustment.setMargins(removeDown!!, 16,0,0,16)

        removeDown?.setOnClickListener {
            selectDownRemove()
        }
        selectBtn = findViewById(R.id.download_check)
        val position =0

        var boolean = false

        val listenerTwoGB = View.OnClickListener {
            boolean = if (!boolean) {
                expandTwoGB.expand()
                downloadCustomTwoGB.setDownMenu(R.drawable.icon_menu_drop_up)

                true
            } else {
                expandTwoGB.collapse()
                downloadCustomTwoGB.setDownMenu(R.drawable.icon_menu_drop_down)

                false
            }
        }

        downloadCustomTwoGB.setListener(listenerTwoGB)

        /////////////////////////////////////////////////////////////
        /////////////////////////////////////////////////////////////
        /////////////////////////////////////////////////////////////

        val listenerOneGB = View.OnClickListener {
            boolean = if (!boolean) {
                expandOneGB.expand()
                downloadCustomOneGB.setDownMenu(R.drawable.icon_menu_drop_up)

                true
            } else {
                expandOneGB.collapse()
                downloadCustomOneGB.setDownMenu(R.drawable.icon_menu_drop_down)

                false
            }
        }
        downloadCustomOneGB.setListener(listenerOneGB)

        /////////////////////////////////////////////////////////////
        /////////////////////////////////////////////////////////////
        /////////////////////////////////////////////////////////////

        val listenerOneGBDown = View.OnClickListener {
            boolean = if (!boolean) {
                expandOneGBDown.expand()
                downloadCustomOneGBDown.setDownMenu(R.drawable.icon_menu_drop_up)

                true
            } else {
                expandOneGBDown.collapse()
                downloadCustomOneGBDown.setDownMenu(R.drawable.icon_menu_drop_down)

                false
            }
        }
        downloadCustomOneGBDown.setListener(listenerOneGBDown)

        /////////////////////////////////////////////////////////////
        /////////////////////////////////////////////////////////////
        /////////////////////////////////////////////////////////////

        val listenerFiveMBDown = View.OnClickListener {
            boolean = if (!boolean) {
                expandFiveMBDown.expand()
                downloadCustomFiveMBDown.setDownMenu(R.drawable.icon_menu_drop_up)

                true
            } else {
                expandFiveMBDown.collapse()
                downloadCustomFiveMBDown.setDownMenu(R.drawable.icon_menu_drop_down)

                false
            }
        }
        downloadCustomFiveMBDown.setListener(listenerFiveMBDown)


        /////////////////////////////////////////////////////////////
        /////////////////////////////////////////////////////////////
        /////////////////////////////////////////////////////////////

        val listenerOneMBDown = View.OnClickListener {
            boolean = if (!boolean) {
                expandOneMBDown.expand()
                downloadCustomOneMBDown.setDownMenu(R.drawable.icon_menu_drop_up)

                true
            } else {
                expandOneMBDown.collapse()
                downloadCustomOneMBDown.setDownMenu(R.drawable.icon_menu_drop_down)

                false
            }
        }
        downloadCustomOneMBDown.setListener(listenerOneMBDown)
        var diffOneMbSize :Long =0
        var diffFiveMbSize :Long =0
        var diffOneGbDownSize :Long =0
        var diffOneGbSize :Long =0
        var diffTwoGbSize :Long =0

        for(diff in downListCategory) {
            if (0 < (diff.downloadSize / Actions.KB) && (diff.downloadSize / Actions.MB) < 100) {
                oneMBListCategory.add(DownData(type = diff.type , downloadIcon = diff.downloadIcon, downloadName = diff.downloadName, downloadTime = diff.downloadTime, downloadSize = diff.downloadSize, downloadChoose = diff.downloadChoose, downloadFile = diff.downloadFile, diffTime = diff.diffTime ))
                diffOneMbSize += diff.downloadSize
                downloadCustomOneMBDown.setDownSizeAll(Formatter.formatFileSize(context, diffOneMbSize))
                downloadCustomOneMBDown.setDownCount(oneMBListCategory.count().toString() + "개")
            } else if (100 <= (diff.downloadSize / Actions.MB) && (diff.downloadSize / Actions.MB) < 500) {
                fiveMBListCategory.add(DownData(type = diff.type , downloadIcon = diff.downloadIcon, downloadName = diff.downloadName, downloadTime = diff.downloadTime, downloadSize = diff.downloadSize, downloadChoose = diff.downloadChoose, downloadFile = diff.downloadFile, diffTime = diff.diffTime ))
                diffFiveMbSize += diff.downloadSize
                downloadCustomFiveMBDown.setDownSizeAll(Formatter.formatFileSize(context, diffFiveMbSize))
                downloadCustomFiveMBDown.setDownCount(fiveMBListCategory.count().toString() + "개")
            } else if (500 <= (diff.downloadSize / Actions.MB) && (diff.downloadSize / Actions.GB) < 1) {
                oneGBDownListCategory.add(DownData(type = diff.type , downloadIcon = diff.downloadIcon, downloadName = diff.downloadName, downloadTime = diff.downloadTime, downloadSize = diff.downloadSize, downloadChoose = diff.downloadChoose, downloadFile = diff.downloadFile, diffTime = diff.diffTime ))
                diffOneGbDownSize += diff.downloadSize
                downloadCustomOneGBDown.setDownSizeAll(Formatter.formatFileSize(context, diffOneGbDownSize))
                downloadCustomOneGBDown.setDownCount(oneGBDownListCategory.count().toString() + "개")
            } else if (1 <= (diff.downloadSize / Actions.GB) && (diff.downloadSize / Actions.GB) < 2) {
                oneGBListCategory.add(DownData(type = diff.type , downloadIcon = diff.downloadIcon, downloadName = diff.downloadName, downloadTime = diff.downloadTime, downloadSize = diff.downloadSize, downloadChoose = diff.downloadChoose, downloadFile = diff.downloadFile, diffTime = diff.diffTime ))
                diffOneGbSize += diff.downloadSize
                downloadCustomOneGB.setDownSizeAll(Formatter.formatFileSize(context, diffOneGbSize))
                downloadCustomOneGB.setDownCount(oneGBListCategory.count().toString() + "개")
            } else if (2 <= (diff.downloadSize / Actions.GB)) {
                twoGBListCategory.add(DownData(type = diff.type , downloadIcon = diff.downloadIcon, downloadName = diff.downloadName, downloadTime = diff.downloadTime, downloadSize = diff.downloadSize, downloadChoose = diff.downloadChoose, downloadFile = diff.downloadFile, diffTime = diff.diffTime ))
                diffTwoGbSize += diff.downloadSize
                downloadCustomTwoGB.setDownSizeAll(Formatter.formatFileSize(context, diffTwoGbSize))
                downloadCustomTwoGB.setDownCount(twoGBListCategory.count().toString() + "개")
            }
        }
        /////////////////////////////////////////////////////////////////////////////////
        val recyclerOneMBDown = findViewById<RecyclerView>(R.id.download_100MB_Down_recycler)
        recyclerOneMBDown.layoutManager = LinearLayoutManager(this)

        if (oneMBListCategory.isEmpty()) {
            downloadCustomOneMBDown.visibility = View.GONE
            oneMBDownLine.visibility = View.GONE

        } else {
            recyclerOneMBDown.adapter = oneMBDownAdapter
        }
        val oneMBDownAllCheck = View.OnClickListener{
            boolean = if (!boolean) {
                oneMBDownAdapter.setCheckAll(false, position)
                RepositoryImpl.getDownList().clear()
                RepositoryImpl.getOneMBFile().clear()
                true
            } else {
                oneMBDownAdapter.setCheckAll(true, position)
                false
            }
        }

        downloadCustomOneMBDown.setCheckListener(oneMBDownAllCheck)
        //////////////////////////////////////////////////////////////////////////
        val recyclerFiveMBDown = findViewById<RecyclerView>(R.id.download_500MB_Down_recycler)
        recyclerFiveMBDown.layoutManager = LinearLayoutManager(this)

        if (fiveMBListCategory.isEmpty()) {
            downloadCustomFiveMBDown.visibility = View.GONE
            fiveMBDownLine.visibility=View.GONE

        } else {
            recyclerFiveMBDown.adapter = fiveMBDownAdapter
        }

        val fiveMBDownAllCheck = View.OnClickListener{
            boolean = if (!boolean) {
                fiveMBDownAdapter.setCheckAll(false, position)
                RepositoryImpl.getDownList().clear()
                RepositoryImpl.getFiveMBFile().clear()
                true
            } else {
                fiveMBDownAdapter.setCheckAll(true, position)
                false
            }
        }

        downloadCustomFiveMBDown.setCheckListener(fiveMBDownAllCheck)
        ///////////////////////////////////////////////////////////////////////
        val recyclerOneGBDown = findViewById<RecyclerView>(R.id.download_oneGB_Down_recycler)
        recyclerOneGBDown.layoutManager = LinearLayoutManager(this)

        if (oneGBDownListCategory.isEmpty()) {
            downloadCustomOneGBDown.visibility = View.GONE
            oneGBDownLine.visibility=View.GONE

        } else {
            recyclerOneGBDown.adapter = oneGBDownAdapter
        }

        val oneGBDownAllCheck = View.OnClickListener{
            boolean = if (!boolean) {
                oneGBDownAdapter.setCheckAll(false, position)
                RepositoryImpl.getDownList().clear()
                RepositoryImpl.getOneGBDownFile().clear()

                true
            } else {
                oneGBDownAdapter.setCheckAll(true, position)
                false
            }
        }

        downloadCustomOneGBDown.setCheckListener(oneGBDownAllCheck)
        ////////////////////////////////////////////////////////////////////////////
        val recyclerOneGB = findViewById<RecyclerView>(R.id.download_oneGB_recycler)

        recyclerOneGB.layoutManager = LinearLayoutManager(this)
        if (oneGBListCategory.isEmpty()) {
            downloadCustomOneGB.visibility = View.GONE
            oneGBLine.visibility=View.GONE

        } else {
            recyclerOneGB.adapter = oneGBAdapter
        }

        val oneGBAllCheck = View.OnClickListener{
            boolean = if (!boolean) {
                oneGBAdapter.setCheckAll(false, position)
                RepositoryImpl.getDownList().clear()
                RepositoryImpl.getOneGBFile().clear()

                true
            } else {
                oneGBAdapter.setCheckAll(true, position)
                false
            }
        }

        downloadCustomOneGB.setCheckListener(oneGBAllCheck)
        /////////////////////////////////////////////////////////////////////////////
        val recyclerTwoGB = findViewById<RecyclerView>(R.id.download_twoGB_recycler)

        recyclerTwoGB.layoutManager = LinearLayoutManager(this)
        if (twoGBListCategory.isEmpty()) {
            downloadCustomTwoGB.visibility = View.GONE
            twoGBLine.visibility=View.GONE

        } else {
            recyclerTwoGB.adapter = twoGBAdapter
        }

        val twoGBAllCheck = View.OnClickListener{
            boolean = if (!boolean) {
                twoGBAdapter.setCheckAll(false, position)
                RepositoryImpl.getDownList().clear()
                RepositoryImpl.getTwoGBFile().clear()
                true
            } else {
                twoGBAdapter.setCheckAll(true, position)
                false
            }
        }
        downloadCustomTwoGB.setCheckListener(twoGBAllCheck)

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
            downsize = arrayListOf(files.length()).sum()
            val downCount = arrayListOf(files).count()

            downAllSize += downsize
            downAllCount += downCount

            downListCategory.add(DownData(
                type = TYPE_DOWN,
                downloadIcon = R.drawable.icon_folder,
                downloadName = files.name,
                downloadTime = TYPE_DOWN_TIME,
                downloadSize = downsize,
                downloadChoose = false,
                downloadFile = files,
                diffTime = 0
            ))
        }

    }
    fun oneMB(){
        oneMBDownCheckboxList = RepositoryImpl.getOneMBFile()

        if(oneMBListCategory.isNotEmpty()){
            val checkItr = oneMBDownCheckboxList.iterator()

            while (checkItr.hasNext()) {
                val checkListItr = checkItr.next()
                val checkDocItr = oneMBListCategory.iterator()
                while (checkDocItr.hasNext()) {
                    val checkDocListItr = checkDocItr.next()
                    if (checkListItr.fileName == checkDocListItr.downloadName) {
                        checkDocListItr.downloadFile.delete()
                        checkDocItr.remove()
                        oneMBDownAdapter.set(oneMBListCategory)
                    }
                }
                checkItr.remove()
            }

        }
    }
    fun fiveMB(){
        fiveMBDownCheckboxList = RepositoryImpl.getFiveMBFile()
        if(fiveMBListCategory.isNotEmpty()){
            val checkItr = fiveMBDownCheckboxList.iterator()

            while (checkItr.hasNext()) {
                val checkListItr = checkItr.next()
                val checkDocItr = fiveMBListCategory.iterator()
                while (checkDocItr.hasNext()) {
                    val checkDocListItr = checkDocItr.next()
                    if (checkListItr.fileName == checkDocListItr.downloadName) {
                        checkDocListItr.downloadFile.delete()
                        checkDocItr.remove()
                        fiveMBDownAdapter.set(fiveMBListCategory)

                    }
                }
                checkItr.remove()


            }
        }
    }
    fun oneGBDown(){
        oneGBDownCheckboxList = RepositoryImpl.getOneGBDownFile()

        if(oneGBDownListCategory.isNotEmpty()){
            val checkItr = oneGBDownCheckboxList.iterator()

            while (checkItr.hasNext()) {
                val checkListItr = checkItr.next()
                val checkDocItr = oneGBDownListCategory.iterator()
                while (checkDocItr.hasNext()) {
                    val checkDocListItr = checkDocItr.next()
                    if (checkListItr.fileName == checkDocListItr.downloadName) {
                        checkDocListItr.downloadFile.delete()
                        checkDocItr.remove()
                        oneGBDownAdapter.set(oneGBDownListCategory)
                    }
                }
                checkItr.remove()
            }
        }

    }

    fun oneGB(){
        oneGBCheckboxList = RepositoryImpl.getOneGBFile()
        if(oneGBListCategory.isNotEmpty()){
            val checkItr = oneGBCheckboxList.iterator()

            while (checkItr.hasNext()) {
                val checkListItr = checkItr.next()
                val checkDocItr = oneGBListCategory.iterator()
                while (checkDocItr.hasNext()) {
                    val checkDocListItr = checkDocItr.next()
                    if (checkListItr.fileName == checkDocListItr.downloadName) {
                        checkDocListItr.downloadFile.delete()
                        checkDocItr.remove()
                        oneGBAdapter.set(oneGBListCategory)

                    }
                }
                checkItr.remove()
            }
        }

    }
    fun twoGB() {
        twoGBCheckboxList = RepositoryImpl.getTwoGBFile()

        if (twoGBListCategory.isNotEmpty()) {
            val checkItr = twoGBCheckboxList.iterator()
            while (checkItr.hasNext()) {
                val checkListItr = checkItr.next()
                val checkDocItr = twoGBListCategory.iterator()
                while (checkDocItr.hasNext()) {
                    val checkDocListItr = checkDocItr.next()
                    if (checkListItr.fileName == checkDocListItr.downloadName) {
                        checkDocListItr.downloadFile.delete()
                        checkDocItr.remove()
                        twoGBAdapter.set(twoGBListCategory)

                    }
                }
                checkItr.remove()
            }
        }
    }
    private fun selectDownRemove(){
        finish()
        overridePendingTransition(0,0)
        val intent :Intent= intent
        startActivity(intent)
        overridePendingTransition(0,0)
        checkboxList = RepositoryImpl.getDownList()
        oneMB()
        fiveMB()
        oneGBDown()
        oneGB()
        twoGB()

    }
    fun back(view: View) {

        finish()
    }
}