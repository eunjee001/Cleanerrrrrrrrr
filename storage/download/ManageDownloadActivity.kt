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

class ManageDownloadActivity : AppCompatActivity() {
    var context: Context = this
    private lateinit var dpAdjustment: DisplayAdjustment

    private val twoYearListCategory = ArrayList<DownData>()
    private val yearListCategory = ArrayList<DownData>()
    private val yearDownListCategory = ArrayList<DownData>()
    private val sixMonthListCategory = ArrayList<DownData>()
    private val threeMonthListCategory = ArrayList<DownData>()
    private val monthListCategory = ArrayList<DownData>()
    private val yesterdayListCategory = ArrayList<DownData>()
    private val todayListCategory = ArrayList<DownData>()
    private var removeDown: Button? = null
    private val downListCategory = ArrayList<DownData>()
    private lateinit var allSize: TextView
    private lateinit var allCount: TextView

    private var downAdapter = DownAdapter(sixMonthListCategory, context)
    private var monthAdapter = DownAdapter(monthListCategory, context)
    private var todayAdapter = DownAdapter(todayListCategory, context)
    private var yesterdayAdapter = DownAdapter(yesterdayListCategory, context)
    private var threeMonthAdapter = DownAdapter(threeMonthListCategory, context)
    private var sixMonthAdapter = DownAdapter(sixMonthListCategory, context)
    private var yearDownAdapter = DownAdapter(yearDownListCategory, context)
    private var yearAdapter = DownAdapter(yearListCategory, context)
    private var twoYearAdapter = DownAdapter(twoYearListCategory, context)

    private var checkboxList =ArrayList<CheckDownData>()
    private var todayCheckboxList =ArrayList<CheckStorageSecondData>()
    private var yesterdayCheckboxList =ArrayList<CheckStorageSecondData>()
    private var monthCheckboxList =ArrayList<CheckStorageSecondData>()
    private var threeMonthCheckboxList =ArrayList<CheckStorageSecondData>()
    private var sixMonthCheckboxList =ArrayList<CheckStorageSecondData>()
    private var yearDownCheckboxList =ArrayList<CheckStorageSecondData>()
    private var yearCheckboxList =ArrayList<CheckStorageSecondData>()
    private var twoYearCheckboxList =ArrayList<CheckStorageSecondData>()
    private var allInfo: TextView? = null
    var downAllSize:Long = 0
    var downAllCount = 0
    var selectBtn:CheckBox ?= null
    private var diffTime: Long = 0
    var popupWindow : PopupWindow ?= null

    companion object {
        const val TYPE_DOWN = 0L
        const val TYPE_DOWN_TIME = 0x01
        const val TYPE_DOWN_PACKAGE =0x02

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_download)
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

        val downTitle = findViewById<TextView>(R.id.downloadTitle)
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


        //////////////twoYear//////////////
        val twoYearLayout = findViewById<RelativeLayout>(R.id.relative_layout_twoYear)

        val twoYearLine = findViewById<View>(R.id.line_twoYear)
        dpAdjustment.setMargins(twoYearLine, 0,0,0,0)
        dpAdjustment.setScale(twoYearLine.layoutParams, 360f,5f)

        val downloadCustomTwoYear = findViewById<DownloadCustomView>(R.id.download_twoYear)
        dpAdjustment.setMargins(downloadCustomTwoYear, 0,10,0,0)
        dpAdjustment.setScale(downloadCustomTwoYear.layoutParams, 360f,56f)

        val expandTwoYear = findViewById<Expandable>(R.id.download_twoYear_expand)


        //////////////year///////////////////////
        val yearLayout = findViewById<RelativeLayout>(R.id.relative_layout_year)
        val yearLine = findViewById<View>(R.id.line_Year)
        dpAdjustment.setMargins(yearLine, 0,0,0,0)
        dpAdjustment.setScale(yearLine.layoutParams, 360f,5f)

        val downloadCustomYear = findViewById<DownloadCustomView>(R.id.download_Year)
        dpAdjustment.setMargins(downloadCustomYear, 0,0,0,0)
        dpAdjustment.setScale(downloadCustomYear.layoutParams, 360f,56f)

        val expandYear = findViewById<Expandable>(R.id.download_Year_expand)

        /////////////////yearDown///////////////////////
        val yearDownLayout = findViewById<RelativeLayout>(R.id.relative_layout_yearDown)
        val yearDownLine = findViewById<View>(R.id.line_YearDown)
        dpAdjustment.setMargins(yearDownLine, 0,0,0,0)
        dpAdjustment.setScale(yearDownLine.layoutParams, 360f,5f)

        val downloadCustomYearDown = findViewById<DownloadCustomView>(R.id.download_YearDown)
        dpAdjustment.setMargins(downloadCustomYearDown, 0,0,0,0)
        dpAdjustment.setScale(downloadCustomYearDown.layoutParams, 360f,56f)

        val expandYearDown = findViewById<Expandable>(R.id.download_YearDown_expand)

        ///////////////////////////////sixMonth////////////////////
        val sixMonthLayout = findViewById<RelativeLayout>(R.id.relative_layout_sixMonth)
        val sixMonthLine = findViewById<View>(R.id.line_sixMonth)
        dpAdjustment.setMargins(sixMonthLine, 0,0,0,0)
        dpAdjustment.setScale(sixMonthLine.layoutParams, 360f,5f)

        val downloadCustomSixMonth = findViewById<DownloadCustomView>(R.id.download_sixMonth)
        dpAdjustment.setMargins(downloadCustomSixMonth, 0,0,0,0)
        dpAdjustment.setScale(downloadCustomSixMonth.layoutParams, 360f,56f)

        val expandSixMonth = findViewById<Expandable>(R.id.download_sixMonth_expand)

        ////////////////////////////threeMonth///////////////////////
        val threeMonthLayout = findViewById<RelativeLayout>(R.id.relative_layout_threeMonth)
        val threeMonthLine = findViewById<View>(R.id.line_threeMonth)
        dpAdjustment.setMargins(threeMonthLine, 0,0,0,0)
        dpAdjustment.setScale(threeMonthLine.layoutParams, 360f,5f)

        val downloadCustomThreeMonth = findViewById<DownloadCustomView>(R.id.download_threeMonth)
        dpAdjustment.setMargins(downloadCustomThreeMonth, 0,0,0,0)
        dpAdjustment.setScale(downloadCustomThreeMonth.layoutParams, 360f,56f)

        val expandThreeMonth = findViewById<Expandable>(R.id.download_threeMonth_expand)

        ///////////////////////////month //////////////////////////
        val monthLayout = findViewById<RelativeLayout>(R.id.relative_layout_Month)
        val monthLine = findViewById<View>(R.id.line_Month)
        dpAdjustment.setMargins(monthLine, 0,0,0,0)
        dpAdjustment.setScale(monthLine.layoutParams, 360f,5f)

        val downloadCustomMonth = findViewById<DownloadCustomView>(R.id.download_Month)
        dpAdjustment.setMargins(downloadCustomMonth, 0,0,0,0)
        dpAdjustment.setScale(downloadCustomMonth.layoutParams, 360f,56f)

        val expandMonth = findViewById<Expandable>(R.id.download_Month_expand)

        /////////////////////yesterday///////////////////
        val yesterdayLayout = findViewById<RelativeLayout>(R.id.relative_layout_Yesterday)
        val yesterdayLine = findViewById<View>(R.id.line_Yesterday)
        dpAdjustment.setMargins(yesterdayLine, 0,0,0,0)
        dpAdjustment.setScale(yesterdayLine.layoutParams, 360f,5f)

        val downloadCustomYesterday = findViewById<DownloadCustomView>(R.id.download_Yesterday)
        dpAdjustment.setMargins(downloadCustomYesterday, 0,0,0,0)
        dpAdjustment.setScale(downloadCustomYesterday.layoutParams, 360f,56f)

        val expandYesterday = findViewById<Expandable>(R.id.download_Yesterday_expand)

        //////////////////////today///////////////////////
        val todayLayout = findViewById<RelativeLayout>(R.id.relative_layout_Today)
        val todayLine = findViewById<View>(R.id.line_Today)
        dpAdjustment.setMargins(todayLine, 0,0,0,0)
        dpAdjustment.setScale(todayLine.layoutParams, 360f,5f)

        val downloadCustomToday = findViewById<DownloadCustomView>(R.id.download_Today)
        dpAdjustment.setMargins(downloadCustomToday, 0,0,0,0)
        dpAdjustment.setScale(downloadCustomToday.layoutParams, 360f,56f)

        val expandToday = findViewById<Expandable>(R.id.download_Today_expand)

        removeDown = findViewById(R.id.download_btn)
        dpAdjustment.setScale(removeDown!!.layoutParams, 328f,48f)
        dpAdjustment.setMargins(removeDown!!, 16,0,0,16)

        removeDown?.setOnClickListener {
            selectDownRemove()
        }
         selectBtn = findViewById(R.id.download_check)


        var position = 0
        var boolean = false

        val listenerTwoYear = View.OnClickListener {
            boolean = if (!boolean) {
                expandTwoYear.expand()
                downloadCustomTwoYear.setDownMenu(R.drawable.icon_menu_drop_up)

                true
            } else {
                expandTwoYear.collapse()
                downloadCustomTwoYear.setDownMenu(R.drawable.icon_menu_drop_down)

                false
            }
        }

        downloadCustomTwoYear.setListener(listenerTwoYear)


        /////////////////////////////////////////////////////////////
        /////////////////////////////////////////////////////////////
        /////////////////////////////////////////////////////////////

        val listenerYear = View.OnClickListener {
            boolean = if (!boolean) {
                expandYear.expand()
                downloadCustomYear.setDownMenu(R.drawable.icon_menu_drop_up)

                true
            } else {
                expandYear.collapse()
                downloadCustomYear.setDownMenu(R.drawable.icon_menu_drop_down)

                false
            }
        }
        downloadCustomYear.setListener(listenerYear)

        /////////////////////////////////////////////////////////////
        /////////////////////////////////////////////////////////////
        /////////////////////////////////////////////////////////////

        val listenerYearDown = View.OnClickListener {
            boolean = if (!boolean) {
                expandYearDown.expand()
                downloadCustomYearDown.setDownMenu(R.drawable.icon_menu_drop_up)

                true
            } else {
                expandYearDown.collapse()
                downloadCustomYearDown.setDownMenu(R.drawable.icon_menu_drop_down)

                false
            }
        }
        downloadCustomYearDown.setListener(listenerYearDown)


        /////////////////////////////////////////////////////////////
        /////////////////////////////////////////////////////////////
        /////////////////////////////////////////////////////////////

        val listenerSixMonth = View.OnClickListener {
            boolean = if (!boolean) {
                expandSixMonth.expand()
                downloadCustomSixMonth.setDownMenu(R.drawable.icon_menu_drop_up)

                true
            } else {
                expandSixMonth.collapse()
                downloadCustomSixMonth.setDownMenu(R.drawable.icon_menu_drop_down)

                false
            }
        }
        downloadCustomSixMonth.setListener(listenerSixMonth)

        /////////////////////////////////////////////////////////////
        /////////////////////////////////////////////////////////////
        /////////////////////////////////////////////////////////////

        val listenerThreeMonth = View.OnClickListener {
            boolean = if (!boolean) {
                expandThreeMonth.expand()
                downloadCustomThreeMonth.setDownMenu(R.drawable.icon_menu_drop_up)

                true
            } else {
                expandThreeMonth.collapse()
                downloadCustomThreeMonth.setDownMenu(R.drawable.icon_menu_drop_down)

                false
            }
        }
        downloadCustomThreeMonth.setListener(listenerThreeMonth)

        /////////////////////////////////////////////////////////////
        /////////////////////////////////////////////////////////////
        /////////////////////////////////////////////////////////////

        val listenerMonth = View.OnClickListener {
            boolean = if (!boolean) {
                expandMonth.expand()
                downloadCustomMonth.setDownMenu(R.drawable.icon_menu_drop_up)

                true
            } else {
                expandMonth.collapse()
                downloadCustomMonth.setDownMenu(R.drawable.icon_menu_drop_down)

                false
            }
        }
        downloadCustomMonth.setListener(listenerMonth)

        /////////////////////////////////////////////////////////////
        /////////////////////////////////////////////////////////////
        /////////////////////////////////////////////////////////////

        val listenerYesterday = View.OnClickListener {
            boolean = if (!boolean) {
                expandYesterday.expand()
                downloadCustomYesterday.setDownMenu(R.drawable.icon_menu_drop_up)

                true
            } else {
                expandYesterday.collapse()
                downloadCustomYesterday.setDownMenu(R.drawable.icon_menu_drop_down)

                false
            }
        }
        downloadCustomYesterday.setListener(listenerYesterday)

        /////////////////////////////////////////////////////////////
        /////////////////////////////////////////////////////////////
        /////////////////////////////////////////////////////////////

        val listenerToday = View.OnClickListener {
            boolean = if (!boolean) {
                expandToday.expand()
                downloadCustomToday.setDownMenu(R.drawable.icon_menu_drop_up)

                true
            } else {
                expandToday.collapse()
                downloadCustomToday.setDownMenu(R.drawable.icon_menu_drop_down)

                false
            }
        }
        downloadCustomToday.setListener(listenerToday)
        var diffTodayAllSize : Long = 0
        var diffYesterAllSize : Long = 0
        var diffMonthAllSize : Long = 0
        var diffThreeMonthAllSize : Long = 0
        var diffSixMonthAllSize : Long = 0
        var diffYearDownAllSize : Long = 0
        var diffYearAllSize : Long = 0
        var diffTwoYearAllSize : Long = 0
        for(diff in downListCategory){
        if ((0 < (diff.diffTime / Actions.sec) && 60 > (diff.diffTime / Actions.sec)) || (0 <= (diff.diffTime / Actions.min) && 24 > (diff.diffTime / Actions.min))) {
            todayListCategory.add(DownData(type = diff.type , downloadIcon = diff.downloadIcon, downloadName = diff.downloadName, downloadTime = diff.downloadTime, downloadSize = diff.downloadSize, downloadChoose = diff.downloadChoose, downloadFile = diff.downloadFile, diffTime = diff.diffTime ))

            diffTodayAllSize += diff.downloadSize
            downloadCustomToday.setDownSize(Formatter.formatFileSize(context,diffTodayAllSize))
            downloadCustomToday.setDownCount(todayListCategory.count().toString() +"개")

       }else if (0 < (diff.diffTime/Actions.hour) && 1>= (diff.diffTime/Actions.hour)){
            yesterdayListCategory.add(DownData(type = diff.type , downloadIcon = diff.downloadIcon, downloadName = diff.downloadName, downloadTime = diff.downloadTime, downloadSize = diff.downloadSize, downloadChoose = diff.downloadChoose, downloadFile = diff.downloadFile, diffTime = diff.diffTime ))

            diffYesterAllSize += diff.downloadSize
            downloadCustomYesterday.setDownSize(Formatter.formatFileSize(context,diffYesterAllSize))
            downloadCustomYesterday.setDownCount(yesterdayListCategory.count().toString() +"개")

        }else if (30> (diff.diffTime/Actions.hour) ){
            monthListCategory.add(DownData(type = diff.type , downloadIcon = diff.downloadIcon, downloadName = diff.downloadName, downloadTime = diff.downloadTime, downloadSize = diff.downloadSize, downloadChoose = diff.downloadChoose, downloadFile = diff.downloadFile, diffTime = diff.diffTime ))

            diffMonthAllSize += diff.downloadSize
            downloadCustomMonth.setDownSize(Formatter.formatFileSize(context,diffMonthAllSize))
            downloadCustomMonth.setDownCount(monthListCategory.count().toString() +"개")

        }else if(0< (diff.diffTime/Actions.day) && 3> (diff.diffTime/Actions.day)){
            threeMonthListCategory.add(DownData(type = diff.type , downloadIcon = diff.downloadIcon, downloadName = diff.downloadName, downloadTime = diff.downloadTime, downloadSize = diff.downloadSize, downloadChoose = diff.downloadChoose, downloadFile = diff.downloadFile, diffTime = diff.diffTime ))

            diffThreeMonthAllSize += diff.downloadSize
            downloadCustomThreeMonth.setDownSize(Formatter.formatFileSize(context,diffThreeMonthAllSize))
            downloadCustomThreeMonth.setDownCount(threeMonthListCategory.count().toString() +"개")

        }else if(3<= (diff.diffTime/Actions.day) && 6>  (diff.diffTime/Actions.day)){
            sixMonthListCategory.add(DownData(type = diff.type , downloadIcon = diff.downloadIcon, downloadName = diff.downloadName, downloadTime = diff.downloadTime, downloadSize = diff.downloadSize, downloadChoose = diff.downloadChoose, downloadFile = diff.downloadFile, diffTime = diff.diffTime ))

            diffSixMonthAllSize += diff.downloadSize
            downloadCustomSixMonth.setDownSize(Formatter.formatFileSize(context,diffSixMonthAllSize))
            downloadCustomSixMonth.setDownCount(sixMonthListCategory.count().toString() +"개")

        }else if(6<= (diff.diffTime/Actions.day) && 12>(diff.diffTime/Actions.day)){
            yearDownListCategory.add(DownData(type = diff.type , downloadIcon = diff.downloadIcon, downloadName = diff.downloadName, downloadTime = diff.downloadTime, downloadSize = diff.downloadSize, downloadChoose = diff.downloadChoose, downloadFile = diff.downloadFile, diffTime = diff.diffTime ))

            diffYearDownAllSize += diff.downloadSize
            downloadCustomYearDown.setDownSize(Formatter.formatFileSize(context,diffYearDownAllSize))
            downloadCustomYearDown.setDownCount(yearDownListCategory.count().toString() +"개")

        }else if(2>(diff.diffTime / Actions.month)  ){
            yearListCategory.add(DownData(type = diff.type , downloadIcon = diff.downloadIcon, downloadName = diff.downloadName, downloadTime = diff.downloadTime, downloadSize = diff.downloadSize, downloadChoose = diff.downloadChoose, downloadFile = diff.downloadFile, diffTime = diff.diffTime ))

            diffYearAllSize += diff.downloadSize
            downloadCustomYear.setDownSize(Formatter.formatFileSize(context,diffYearAllSize))
            downloadCustomYear.setDownCount(yearListCategory.count().toString() +"개")

        }else{
            twoYearListCategory.add(DownData(type = diff.type , downloadIcon = diff.downloadIcon, downloadName = diff.downloadName, downloadTime = diff.downloadTime, downloadSize = diff.downloadSize, downloadChoose = diff.downloadChoose, downloadFile = diff.downloadFile, diffTime = diff.diffTime ))
            diffTwoYearAllSize += diff.downloadSize
            downloadCustomTwoYear.setDownSize(Formatter.formatFileSize(context,diffTwoYearAllSize))
            downloadCustomTwoYear.setDownCount(twoYearListCategory.count().toString() +"개")
        }
        }
        /////////////////////////////////////////////////////////////////////////////////////
        val recyclerToday = findViewById<RecyclerView>(R.id.download_Today_recycler)

        recyclerToday.layoutManager = LinearLayoutManager(this)
        if(todayListCategory.isEmpty()){
            downloadCustomToday.visibility = View.GONE
            todayLine.visibility=View.GONE

        }else {
            recyclerToday.adapter = todayAdapter
        }

        val todayAllCheck = View.OnClickListener{
            boolean = if (!boolean) {
                todayAdapter.setCheckAll(false, position)
                RepositoryImpl.getDownList().clear()
                RepositoryImpl.getToday().clear()
                true
            } else {
                todayAdapter.setCheckAll(true, position)
                false
            }
        }

        downloadCustomToday.setCheckListener(todayAllCheck)

        ///////////////////////////////////////////////////////////////////////////////////
        val recyclerYesterday = findViewById<RecyclerView>(R.id.download_Yesterday_recycler)

        recyclerYesterday.layoutManager = LinearLayoutManager(this)
        if(yesterdayListCategory.isEmpty()){
            downloadCustomYesterday.visibility = View.GONE
            yesterdayLine.visibility=View.GONE

        }else {
            recyclerYesterday.adapter = yesterdayAdapter
        }

        val yesterdayAllCheck = View.OnClickListener{
            boolean = if (!boolean) {
                yesterdayAdapter.setCheckAll(false, position)
                RepositoryImpl.getDownList().clear()
                RepositoryImpl.getYesterday().clear()
                true
            } else {
                yesterdayAdapter.setCheckAll(true, position)
                false
            }
        }

        downloadCustomYesterday.setCheckListener(yesterdayAllCheck)

        ////////////////////////////////////////////////////////////////////////////////////////
        val recyclerMonth = findViewById<RecyclerView>(R.id.download_Month_recycler)


        recyclerMonth.layoutManager = LinearLayoutManager(this)
        if(monthListCategory.isEmpty()){
            downloadCustomMonth.visibility = View.GONE
            monthLine.visibility=View.GONE

        }else {
            recyclerMonth.adapter = monthAdapter
        }

        val monthAllCheck = View.OnClickListener{
            boolean = if (!boolean) {
                monthAdapter.setCheckAll(false, position)
                RepositoryImpl.getDownList().clear()
                RepositoryImpl.getMonth().clear()
                true
            } else {
                monthAdapter.setCheckAll(true, position)
                false
            }
        }

        downloadCustomMonth.setCheckListener(monthAllCheck)
        /////////////////////////////////////////////////////////////////////////////////////
        val recyclerThreeMonth = findViewById<RecyclerView>(R.id.download_threeMonth_recycler)

        recyclerThreeMonth.layoutManager = LinearLayoutManager(this)
        if(threeMonthListCategory.isEmpty()){
            downloadCustomThreeMonth.visibility = View.GONE
            threeMonthLine.visibility=View.GONE

        }else {
            recyclerThreeMonth.adapter = threeMonthAdapter
        }

        val threeMonthAllCheck = View.OnClickListener{
            boolean = if (!boolean) {
                threeMonthAdapter.setCheckAll(false, position)
                RepositoryImpl.getDownList().clear()
                RepositoryImpl.getThreeMonth().clear()
                true
            } else {
                threeMonthAdapter.setCheckAll(true, position)
                false
            }
        }

        downloadCustomThreeMonth.setCheckListener(threeMonthAllCheck)
        /////////////////////////////////////////////////////////////////////////////////
        val recyclerSixMonth = findViewById<RecyclerView>(R.id.download_sixMonth_recycler)

        recyclerSixMonth.layoutManager =LinearLayoutManager(this)
        if(sixMonthListCategory.isEmpty()){
            downloadCustomSixMonth.visibility = View.GONE
            sixMonthLine.visibility=View.GONE

        }else {
            recyclerSixMonth.adapter = sixMonthAdapter
        }

        val sixMonthAllCheck = View.OnClickListener{
            boolean = if (!boolean) {
                sixMonthAdapter.setCheckAll(false, position)
                RepositoryImpl.getDownList().clear()
                RepositoryImpl.getSixMonth().clear()
                true
            } else {
                sixMonthAdapter.setCheckAll(true, position)
                false
            }
        }

        downloadCustomSixMonth.setCheckListener(sixMonthAllCheck)
        /////////////////////////////////////////////////////////////////////////////////
        val recyclerYearDown = findViewById<RecyclerView>(R.id.download_YearDown_recycler)

        recyclerYearDown.layoutManager = LinearLayoutManager(this)
        if(yearDownListCategory.isEmpty()){
            downloadCustomYearDown.visibility = View.GONE
            yearDownLine.visibility=View.GONE

        }else {
            recyclerYearDown.adapter = yearDownAdapter
        }

        val yearDownAllCheck = View.OnClickListener{
            boolean = if (!boolean) {
                yearDownAdapter.setCheckAll(false, position)
                RepositoryImpl.getDownList().clear()
                RepositoryImpl.getYearDown().clear()
                true
            } else {
                yearDownAdapter.setCheckAll(true, position)
                false
            }
        }

        downloadCustomYearDown.setCheckListener(yearDownAllCheck)
        /////////////////////////////////////////////////////////////////////////////////
        val recyclerYear = findViewById<RecyclerView>(R.id.download_Year_recycler)

        recyclerYear.layoutManager =LinearLayoutManager(this)

        if(yearListCategory.isEmpty()){
            downloadCustomYear.visibility = View.GONE
            yearLine.visibility=View.GONE

        }else {
            recyclerYear.adapter = yearAdapter
        }

        val yearAllCheck = View.OnClickListener{
            boolean = if (!boolean) {
                yearAdapter.setCheckAll(false, position)
                RepositoryImpl.getDownList().clear()
                RepositoryImpl.getYear().clear()
                true
            } else {
                yearAdapter.setCheckAll(true, position)
                false
            }
        }

        downloadCustomYear.setCheckListener(yearAllCheck)

        //////////////////////////////////////////////////////////////////////////////////////
        val recyclerTwoYear = findViewById<RecyclerView>(R.id.download_twoYear_recycler)
        recyclerTwoYear.layoutManager = LinearLayoutManager(this)

        if(twoYearListCategory.isEmpty()){
            downloadCustomTwoYear.visibility = View.GONE
            twoYearLine.visibility=View.GONE

        }else{
            recyclerTwoYear.adapter = twoYearAdapter
        }

        val twoYearAllCheck = View.OnClickListener{
            boolean = if (!boolean) {
                twoYearAdapter.setCheckAll(false, position)
                RepositoryImpl.getDownList().clear()
                RepositoryImpl.getTwoYear().clear()
                true
            } else {
                twoYearAdapter.setCheckAll(true, position)
                false
            }
        }

        downloadCustomTwoYear.setCheckListener(twoYearAllCheck)
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
        val fileList = file.listFiles() ?: return

        for (files in fileList) {

            val downSize: Long = arrayListOf(files.length()).sum()
            val downCount = arrayListOf(files).count()

            downAllSize += downSize
            downAllCount += downCount

            val time = files.lastModified()
            val today = System.currentTimeMillis()
            diffTime = (today - time) / 1000

            downListCategory.add(DownData(
                type = TYPE_DOWN,
                downloadIcon = R.drawable.icon_folder,
                downloadName = files.name,
                downloadTime = TYPE_DOWN_TIME,
                downloadSize = downSize,
                downloadChoose = false,
                downloadFile = files,
                diffTime = diffTime
            ))

            }

    }
    fun today(){

        todayCheckboxList= RepositoryImpl.getFileToday()
        if(todayListCategory.isNotEmpty()){
            val checkItr = todayCheckboxList.iterator()

            while (checkItr.hasNext()) {
                val checkListItr = checkItr.next()
                val checkDocItr = todayListCategory.iterator()
                while (checkDocItr.hasNext()) {
                    val checkDocListItr = checkDocItr.next()
                    if (checkListItr.fileName == checkDocListItr.downloadName) {
                        checkDocListItr.downloadFile.delete()

                        checkDocItr.remove()
                        todayAdapter.set(todayListCategory)

                    }
                }
                checkItr.remove()
            }
        }
    }

    fun yesterday(){
        yesterdayCheckboxList = RepositoryImpl.getFileYesterday()
        if(yesterdayListCategory.isNotEmpty()){
            val checkItr = yesterdayCheckboxList.iterator()

            while (checkItr.hasNext()) {
                val checkListItr = checkItr.next()
                val checkDocItr = yesterdayListCategory.iterator()
                while (checkDocItr.hasNext()) {
                    val checkDocListItr = checkDocItr.next()
                    if (checkListItr.fileName == checkDocListItr.downloadName) {
                        checkDocListItr.downloadFile.delete()

                        checkDocItr.remove()
                        yesterdayAdapter.set(yesterdayListCategory)

                    }
                }
                checkItr.remove()
            }
        }


    }
    fun month(){
        monthCheckboxList = RepositoryImpl.getFileMonth()
        if(monthListCategory.isNotEmpty()) {
            val checkItr = monthCheckboxList.iterator()

            while (checkItr.hasNext()) {
                val checkListItr = checkItr.next()
                val checkDocItr = monthListCategory.iterator()
                while (checkDocItr.hasNext()) {
                    val checkDocListItr = checkDocItr.next()
                    if (checkListItr.fileName == checkDocListItr.downloadName) {
                        checkDocListItr.downloadFile.delete()
                        checkDocItr.remove()
                        monthAdapter.set(monthListCategory)
                    }
                }
                checkItr.remove()
            }
        }
    }
    fun threeMonth (){
        threeMonthCheckboxList = RepositoryImpl.getFileThreeMonth()
        if(threeMonthListCategory.isNotEmpty()){
            val checkItr = threeMonthCheckboxList.iterator()

            while (checkItr.hasNext()) {
                val checkListItr = checkItr.next()
                val checkDocItr = threeMonthListCategory.iterator()
                while (checkDocItr.hasNext()) {
                    val checkDocListItr = checkDocItr.next()
                    if (checkListItr.fileName == checkDocListItr.downloadName) {
                        checkDocListItr.downloadFile.delete()
                        checkDocItr.remove()
                        threeMonthAdapter.set(threeMonthListCategory)
                    }
                }
                checkItr.remove()
            }

        }

    }
    fun sixMonth(){
        sixMonthCheckboxList = RepositoryImpl.getFileSixMonth()
        if(sixMonthListCategory.isNotEmpty()){
            val checkItr = sixMonthCheckboxList.iterator()

            while (checkItr.hasNext()) {
                val checkListItr = checkItr.next()
                val checkDocItr = sixMonthListCategory.iterator()
                while (checkDocItr.hasNext()) {
                    val checkDocListItr = checkDocItr.next()
                    if (checkListItr.fileName == checkDocListItr.downloadName) {
                        checkDocListItr.downloadFile.delete()
                        checkDocItr.remove()
                        sixMonthAdapter.set(sixMonthListCategory)

                    }
                }
                checkItr.remove()
            }

        }

    }
    fun yearDown(){
        yearDownCheckboxList = RepositoryImpl.getFileYearDown()
        if(yearDownListCategory.isNotEmpty()){
            val checkItr = yearDownCheckboxList.iterator()

            while (checkItr.hasNext()) {
                val checkListItr = checkItr.next()
                val checkDocItr = yearDownListCategory.iterator()
                while (checkDocItr.hasNext()) {
                    val checkDocListItr = checkDocItr.next()
                    if (checkListItr.fileName == checkDocListItr.downloadName) {
                        checkDocListItr.downloadFile.delete()
                        checkDocItr.remove()
                        yearDownAdapter.set(yearDownListCategory)

                    }
                }
                checkItr.remove()
            }
        }

    }
    fun year(){
        yearCheckboxList = RepositoryImpl.getFileYear()
        if(yearListCategory.isNotEmpty()){
            val checkItr = yearCheckboxList.iterator()

            while (checkItr.hasNext()) {
                val checkListItr = checkItr.next()
                val checkDocItr = yearListCategory.iterator()
                while (checkDocItr.hasNext()) {
                    val checkDocListItr = checkDocItr.next()
                    if (checkListItr.fileName == checkDocListItr.downloadName) {
                        checkDocListItr.downloadFile.delete()
                        checkDocItr.remove()
                        yearAdapter.set(yearListCategory)

                    }
                }
                checkItr.remove()
            }
        }

    }
    fun twoYear (){
        twoYearCheckboxList = RepositoryImpl.getFileTwoYear()
        if(twoYearListCategory.isNotEmpty()){
            val checkItr = twoYearCheckboxList.iterator()

            while (checkItr.hasNext()) {
                val checkListItr = checkItr.next()
                val checkDocItr = twoYearListCategory.iterator()
                while (checkDocItr.hasNext()) {
                    val checkDocListItr = checkDocItr.next()
                    if (checkListItr.fileName == checkDocListItr.downloadName) {
                        checkDocListItr.downloadFile.delete()
                        checkDocItr.remove()
                        twoYearAdapter.set(twoYearListCategory)
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
        today()
        yesterday()
        month()
        threeMonth()
        sixMonth()
        yearDown()
        year()
        twoYear()

    }

    fun back(view: View) {

        finish()
    }
}