package com.kyoungss.cleaner.storage.video

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.text.format.Formatter
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.kyoungss.cleaner.Actions
import com.kyoungss.cleaner.DisplayAdjustment
import com.kyoungss.cleaner.expand.Expandable
import com.kyoungss.cleaner.R
import com.kyoungss.cleaner.data.RepositoryImpl
import com.kyoungss.cleaner.storage.CheckStorageData

import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class ManageVideoActivity : AppCompatActivity() {
    var context: Context = this
    private lateinit var dpAdjustment: DisplayAdjustment

    private val twoYearListCategory = ArrayList<VideoData>()
    private val yearListCategory = ArrayList<VideoData>()
    private val yearDownListCategory = ArrayList<VideoData>()
    private val sixMonthListCategory = ArrayList<VideoData>()
    private val threeMonthListCategory = ArrayList<VideoData>()
    private val monthListCategory = ArrayList<VideoData>()
    private val yesterdayListCategory = ArrayList<VideoData>()
    private val todayListCategory = ArrayList<VideoData>()
    private val videoListCategory = ArrayList<VideoData>()

    private var removeVideo: Button? = null

    private lateinit var  allSize : TextView
    private lateinit var  allCount : TextView

    private var videoAdapter = VideoAdapter(videoListCategory,context)
    private var monthAdapter = VideoAdapter(monthListCategory,context)
    private var todayAdapter = VideoAdapter(todayListCategory,context)
    private var yesterdayAdapter = VideoAdapter(yesterdayListCategory,context)
    private var threeMonthAdapter = VideoAdapter(threeMonthListCategory,context)
    private var sixMonthAdapter = VideoAdapter(sixMonthListCategory,context)
    private var yearDownAdapter = VideoAdapter(yearDownListCategory,context)
    private var yearAdapter = VideoAdapter(yearListCategory,context)
    private var twoYearAdapter = VideoAdapter(twoYearListCategory,context)
    
    private var checkboxList =ArrayList<CheckVideoData>()
    private var todayCheckboxList =ArrayList<CheckStorageData>()
    private var yesterdayCheckboxList =ArrayList<CheckStorageData>()
    private var monthCheckboxList =ArrayList<CheckStorageData>()
    private var threeMonthCheckboxList =ArrayList<CheckStorageData>()
    private var sixMonthCheckboxList =ArrayList<CheckStorageData>()
    private var yearDownCheckboxList =ArrayList<CheckStorageData>()
    private var yearCheckboxList =ArrayList<CheckStorageData>()
    private var twoYearCheckboxList =ArrayList<CheckStorageData>()
    var popupWindow : PopupWindow ?= null

    var selectBtn:CheckBox ?= null
    var allVideoSize:Long = 0
    var allVideoCount:Int = 0
    var uriVideo: Uri? = null
    var diffTime: Long = 0

    companion object {
        const val TYPE_VIDEO = 0L
        const val TYPE_VIDEO_PACKAGE = 0x01
        const val TYPE_VIDEO_TIME = 0x02
        const val DELETE_PERMISSION_REQUEST = 0x03


    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_video)

        val menuBar = findViewById<Button>(R.id.menu)
        val view: View = layoutInflater.inflate(R.layout.popup_gallery_layout, null)

        menuBar.setOnClickListener{
            popupWindow = PopupWindow(view, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true)
            popupWindow!!.showAsDropDown(menuBar, -205, -100)
            popupWindow!!.setBackgroundDrawable(ColorDrawable(Color.WHITE))

        }

        val firstPopupTextView = view.findViewById<TextView>(R.id.popup_text_first)
        firstPopupTextView.setOnClickListener {
            val intent = Intent(this, ManageVideoActivity::class.java)
            this.startActivity(intent)
            finish()
        }

        val secondsPopupTextView = view.findViewById<TextView>(R.id.popup_text_second)
        secondsPopupTextView.setOnClickListener {

            val intent = Intent(this, ManageVideoSizeActivity::class.java)
            this.startActivity(intent)
            finish()
        }
        getAllVideoPath()
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

        val videoBack = findViewById<Button>(R.id.videoBack)
        dpAdjustment.setMargins(videoBack,16,16,0,0 )
        dpAdjustment.setScale(videoBack.layoutParams,24f,24f)

        val videoTitle = findViewById<TextView>(R.id.videoTitle)
        dpAdjustment.setMargins(videoTitle, 12,12,0,0)

        allSize = findViewById(R.id.infoTitle)
        dpAdjustment.setMargins(allSize, 16,33,0,0)
        allSize.text = Formatter.formatFileSize(context, allVideoSize)

        allCount = findViewById(R.id.infoTitleCount)
        dpAdjustment.setMargins(allCount, 14,34,0,0)
        allCount.text = allVideoCount.toString() + "개"

        val videoMenu = findViewById<Button>(R.id.menu)
        dpAdjustment.setMargins(videoMenu, 0,31,19,0)
        dpAdjustment.setScale(videoMenu.layoutParams, 24f,24f)

        //////////////twoYear//////////////
        val twoYearLayout = findViewById<RelativeLayout>(R.id.relative_layout_twoYear)

        val twoYearLine = findViewById<View>(R.id.line_twoYear)
        dpAdjustment.setMargins(twoYearLine, 0,0,0,0)
        dpAdjustment.setScale(twoYearLine.layoutParams, 360f,5f)

        val videoCustomTwoYear = findViewById<VideoCustomView>(R.id.video_twoYear)
        dpAdjustment.setMargins(videoCustomTwoYear, 0,10,0,0)
        dpAdjustment.setScale(videoCustomTwoYear.layoutParams, 360f,56f)

        val expandTwoYear = findViewById<Expandable>(R.id.video_twoYear_expand)


        //////////////year///////////////////////
        val yearLayout = findViewById<RelativeLayout>(R.id.relative_layout_year)
        val yearLine = findViewById<View>(R.id.line_Year)
        dpAdjustment.setMargins(yearLine, 0,0,0,0)
        dpAdjustment.setScale(yearLine.layoutParams, 360f,5f)

        val videoCustomYear = findViewById<VideoCustomView>(R.id.video_Year)
        dpAdjustment.setMargins(videoCustomYear, 0,0,0,0)
        dpAdjustment.setScale(videoCustomYear.layoutParams, 360f,56f)

        val expandYear = findViewById<Expandable>(R.id.video_Year_expand)

        /////////////////yearDown///////////////////////
        val yearDownLayout = findViewById<RelativeLayout>(R.id.relative_layout_yearDown)
        val yearDownLine = findViewById<View>(R.id.line_YearDown)
        dpAdjustment.setMargins(yearDownLine, 0,0,0,0)
        dpAdjustment.setScale(yearDownLine.layoutParams, 360f,5f)

        val videoCustomYearDown = findViewById<VideoCustomView>(R.id.video_YearDown)
        dpAdjustment.setMargins(videoCustomYearDown, 0,0,0,0)
        dpAdjustment.setScale(videoCustomYearDown.layoutParams, 360f,56f)

        val expandYearDown = findViewById<Expandable>(R.id.video_YearDown_expand)

        ///////////////////////////////sixMonth////////////////////
        val sixMonthLayout = findViewById<RelativeLayout>(R.id.relative_layout_sixMonth)
        val sixMonthLine = findViewById<View>(R.id.line_sixMonth)
        dpAdjustment.setMargins(sixMonthLine, 0,0,0,0)
        dpAdjustment.setScale(sixMonthLine.layoutParams, 360f,5f)

        val videoCustomSixMonth = findViewById<VideoCustomView>(R.id.video_sixMonth)
        dpAdjustment.setMargins(videoCustomSixMonth, 0,0,0,0)
        dpAdjustment.setScale(videoCustomSixMonth.layoutParams, 360f,56f)

        val expandSixMonth = findViewById<Expandable>(R.id.video_sixMonth_expand)

        ////////////////////////////threeMonth///////////////////////
        val threeMonthLayout = findViewById<RelativeLayout>(R.id.relative_layout_threeMonth)
        val threeMonthLine = findViewById<View>(R.id.line_threeMonth)
        dpAdjustment.setMargins(threeMonthLine, 0,0,0,0)
        dpAdjustment.setScale(threeMonthLine.layoutParams, 360f,5f)

        val videoCustomThreeMonth = findViewById<VideoCustomView>(R.id.video_threeMonth)
        dpAdjustment.setMargins(videoCustomThreeMonth, 0,0,0,0)
        dpAdjustment.setScale(videoCustomThreeMonth.layoutParams, 360f,56f)

        val expandThreeMonth = findViewById<Expandable>(R.id.video_threeMonth_expand)

        ///////////////////////////month //////////////////////////
        val monthLayout = findViewById<RelativeLayout>(R.id.relative_layout_Month)
        val monthLine = findViewById<View>(R.id.line_Month)
        dpAdjustment.setMargins(monthLine, 0,0,0,0)
        dpAdjustment.setScale(monthLine.layoutParams, 360f,5f)

        val videoCustomMonth = findViewById<VideoCustomView>(R.id.video_Month)
        dpAdjustment.setMargins(videoCustomMonth, 0,0,0,0)
        dpAdjustment.setScale(videoCustomMonth.layoutParams, 360f,56f)

        val expandMonth = findViewById<Expandable>(R.id.video_Month_expand)

        /////////////////////yesterday///////////////////
        val yesterdayLayout = findViewById<RelativeLayout>(R.id.relative_layout_Yesterday)
        val yesterdayLine = findViewById<View>(R.id.line_Yesterday)
        dpAdjustment.setMargins(yesterdayLine, 0,0,0,0)
        dpAdjustment.setScale(yesterdayLine.layoutParams, 360f,5f)

        val videoCustomYesterday = findViewById<VideoCustomView>(R.id.video_Yesterday)
        dpAdjustment.setMargins(videoCustomYesterday, 0,0,0,0)
        dpAdjustment.setScale(videoCustomYesterday.layoutParams, 360f,56f)

        val expandYesterday = findViewById<Expandable>(R.id.video_Yesterday_expand)

        //////////////////////today///////////////////////
        val todayLayout = findViewById<RelativeLayout>(R.id.relative_layout_Today)
        val todayLine = findViewById<View>(R.id.line_Today)
        dpAdjustment.setMargins(todayLine, 0,0,0,0)
        dpAdjustment.setScale(todayLine.layoutParams, 360f,5f)

        val videoCustomToday = findViewById<VideoCustomView>(R.id.video_Today)
        dpAdjustment.setMargins(videoCustomToday, 0,0,0,0)
        dpAdjustment.setScale(videoCustomToday.layoutParams, 360f,56f)

        val expandToday = findViewById<Expandable>(R.id.video_Today_expand)

         removeVideo = findViewById(R.id.video_btn)


        selectBtn = findViewById(R.id.grid_selected)
        dpAdjustment.setScale(removeVideo!!.layoutParams, 328f,48f)
        dpAdjustment.setMargins(removeVideo!!, 16,0,0,16)

        var boolean = false

        val position =0
        val listenerTwoYear = View.OnClickListener {
            boolean = if (!boolean) {
                expandTwoYear.expand()
                videoCustomTwoYear.setVideoMenu(R.drawable.icon_menu_drop_up)

                true
            } else {
                expandTwoYear.collapse()
                videoCustomTwoYear.setVideoMenu(R.drawable.icon_menu_drop_down)

                false
            }
        }

        videoCustomTwoYear.setListener(listenerTwoYear)


        /////////////////////////////////////////////////////////////
        /////////////////////////////////////////////////////////////
        /////////////////////////////////////////////////////////////

        val listenerYear = View.OnClickListener {
            boolean = if (!boolean) {
                expandYear.expand()
                videoCustomYear.setVideoMenu(R.drawable.icon_menu_drop_up)

                true
            } else {
                expandYear.collapse()
                videoCustomYear.setVideoMenu(R.drawable.icon_menu_drop_down)

                false
            }
        }
        videoCustomYear.setListener(listenerYear)

        /////////////////////////////////////////////////////////////
        /////////////////////////////////////////////////////////////
        /////////////////////////////////////////////////////////////

        val listenerYearDown = View.OnClickListener {
            boolean = if (!boolean) {
                expandYearDown.expand()
                videoCustomYearDown.setVideoMenu(R.drawable.icon_menu_drop_up)

                true
            } else {
                expandYearDown.collapse()
                videoCustomYearDown.setVideoMenu(R.drawable.icon_menu_drop_down)

                false
            }
        }
        videoCustomYearDown.setListener(listenerYearDown)

        /////////////////////////////////////////////////////////////
        /////////////////////////////////////////////////////////////
        /////////////////////////////////////////////////////////////

        val listenerSixMonth = View.OnClickListener {
            boolean = if (!boolean) {
                expandSixMonth.expand()
                videoCustomSixMonth.setVideoMenu(R.drawable.icon_menu_drop_up)

                true
            } else {
                expandSixMonth.collapse()
                videoCustomSixMonth.setVideoMenu(R.drawable.icon_menu_drop_down)

                false
            }
        }
        videoCustomSixMonth.setListener(listenerSixMonth)

        /////////////////////////////////////////////////////////////
        /////////////////////////////////////////////////////////////
        /////////////////////////////////////////////////////////////
        val listenerThreeMonth = View.OnClickListener {
            boolean = if (!boolean) {
                expandThreeMonth.expand()
                videoCustomThreeMonth.setVideoMenu(R.drawable.icon_menu_drop_up)

                true
            } else {
                expandThreeMonth.collapse()
                videoCustomThreeMonth.setVideoMenu(R.drawable.icon_menu_drop_down)

                false
            }
        }
        videoCustomThreeMonth.setListener(listenerThreeMonth)


        /////////////////////////////////////////////////////////////
        /////////////////////////////////////////////////////////////
        /////////////////////////////////////////////////////////////

        val listenerMonth = View.OnClickListener {
            boolean = if (!boolean) {
                expandMonth.expand()
                videoCustomMonth.setVideoMenu(R.drawable.icon_menu_drop_up)

                true
            } else {
                expandMonth.collapse()
                videoCustomMonth.setVideoMenu(R.drawable.icon_menu_drop_down)

                false
            }
        }
        videoCustomMonth.setListener(listenerMonth)


        /////////////////////////////////////////////////////////////
        /////////////////////////////////////////////////////////////
        /////////////////////////////////////////////////////////////

        val listenerYesterday = View.OnClickListener {
            boolean = if (!boolean) {
                expandYesterday.expand()
                videoCustomYesterday.setVideoMenu(R.drawable.icon_menu_drop_up)

                true
            } else {
                expandYesterday.collapse()
                videoCustomYesterday.setVideoMenu(R.drawable.icon_menu_drop_down)

                false
            }
        }
        videoCustomYesterday.setListener(listenerYesterday)

        /////////////////////////////////////////////////////////////
        /////////////////////////////////////////////////////////////
        /////////////////////////////////////////////////////////////

        val listenerToday = View.OnClickListener {
            boolean = if (!boolean) {
                expandToday.expand()
                videoCustomToday.setVideoMenu(R.drawable.icon_menu_drop_up)

                true
            } else {
                expandToday.collapse()
                videoCustomToday.setVideoMenu(R.drawable.icon_menu_drop_down)

                false
            }
        }
        videoCustomToday.setListener(listenerToday)
        var diffTodayAllSize : Long = 0
        var diffYesterAllSize : Long = 0
        var diffMonthAllSize : Long = 0
        var diffThreeMonthAllSize : Long = 0
        var diffSixMonthAllSize : Long = 0
        var diffYearDownAllSize : Long = 0
        var diffYearAllSize : Long = 0
        var diffTwoYearAllSize : Long = 0

        for(diff in videoListCategory) {
            if ((0 < (diff.diffTime / Actions.sec) && 60 > (diff.diffTime / Actions.sec)) || (0 <= (diff.diffTime / Actions.min) && 24 > (diff.diffTime / Actions.min))) {
                todayListCategory.add(VideoData(type = diff.type , video = diff.video, videoSize = diff.videoSize, videoTime = diff.videoTime, videoPackage = diff.videoPackage, videoName = diff.videoName, videoChoose = diff.videoChoose, diffTime = diff.diffTime))
                diffTodayAllSize += diff.videoSize
                videoCustomToday.setVideoSize(Formatter.formatFileSize(context, diffTodayAllSize))
                videoCustomToday.setVideoCount(todayListCategory.count().toString() + "개")
            } else if (0 < (diff.diffTime / Actions.hour) && (diff.diffTime / Actions.hour) <= 1) {
                yesterdayListCategory.add(VideoData(type = diff.type , video = diff.video, videoSize = diff.videoSize, videoTime = diff.videoTime, videoPackage = diff.videoPackage, videoName = diff.videoName, videoChoose = diff.videoChoose, diffTime = diff.diffTime))
                diffYesterAllSize += diff.videoSize
                videoCustomYesterday.setVideoSize(Formatter.formatFileSize(context, diffYesterAllSize))
                videoCustomYesterday.setVideoCount(yesterdayListCategory.count().toString() + "개")
            } else if ((diff.diffTime / Actions.hour) < 30) {
                monthListCategory.add(VideoData(type = diff.type , video = diff.video, videoSize = diff.videoSize, videoTime = diff.videoTime, videoPackage = diff.videoPackage, videoName = diff.videoName, videoChoose = diff.videoChoose, diffTime = diff.diffTime))
                diffMonthAllSize += diff.videoSize
                videoCustomMonth.setVideoSize(Formatter.formatFileSize(context, diffMonthAllSize))
                videoCustomMonth.setVideoCount(monthListCategory.count().toString() + "개")
            } else if (0 < (diff.diffTime / Actions.day) && (diff.diffTime / Actions.day) < 3) {
                threeMonthListCategory.add(VideoData(type = diff.type , video = diff.video, videoSize = diff.videoSize, videoTime = diff.videoTime, videoPackage = diff.videoPackage, videoName = diff.videoName, videoChoose = diff.videoChoose, diffTime = diff.diffTime))
                diffThreeMonthAllSize += diff.videoSize
                videoCustomThreeMonth.setVideoSize(Formatter.formatFileSize(context, diffThreeMonthAllSize))
                videoCustomThreeMonth.setVideoCount(threeMonthListCategory.count().toString() + "개")
            } else if (3 <= (diff.diffTime / Actions.day) && (diff.diffTime / Actions.day) < 6) {
                sixMonthListCategory.add(VideoData(type = diff.type , video = diff.video, videoSize = diff.videoSize, videoTime = diff.videoTime, videoPackage = diff.videoPackage, videoName = diff.videoName, videoChoose = diff.videoChoose, diffTime = diff.diffTime))
                diffSixMonthAllSize += diff.videoSize
                videoCustomSixMonth.setVideoSize(Formatter.formatFileSize(context, diffSixMonthAllSize))
                videoCustomSixMonth.setVideoCount(sixMonthListCategory.count().toString() + "개")
            } else if (6 <= (diff.diffTime / Actions.day) && (diff.diffTime / Actions.day) < 12) {
                yearDownListCategory.add(VideoData(type = diff.type , video = diff.video, videoSize = diff.videoSize, videoTime = diff.videoTime, videoPackage = diff.videoPackage, videoName = diff.videoName, videoChoose = diff.videoChoose, diffTime = diff.diffTime))
                diffYearDownAllSize += diff.videoSize
                videoCustomYearDown.setVideoSize(Formatter.formatFileSize(context, diffYearDownAllSize))
                videoCustomYearDown.setVideoCount(yearDownListCategory.count().toString() + "개")
            } else if ((diff.diffTime / Actions.month) < 2) {
                yearListCategory.add(VideoData(type = diff.type , video = diff.video, videoSize = diff.videoSize, videoTime = diff.videoTime, videoPackage = diff.videoPackage, videoName = diff.videoName, videoChoose = diff.videoChoose, diffTime = diff.diffTime))
                diffYearAllSize += diff.videoSize
                videoCustomYear.setVideoSize(Formatter.formatFileSize(context, diffYearAllSize))
                videoCustomYear.setVideoCount(yearListCategory.count().toString() + "개")
            } else {
                twoYearListCategory.add(VideoData(type = diff.type , video = diff.video, videoSize = diff.videoSize, videoTime = diff.videoTime, videoPackage = diff.videoPackage, videoName = diff.videoName, videoChoose = diff.videoChoose, diffTime = diff.diffTime))
                diffTwoYearAllSize += diff.videoSize
                videoCustomTwoYear.setVideoSize(Formatter.formatFileSize(context, diffTwoYearAllSize))
                videoCustomTwoYear.setVideoCount(twoYearListCategory.count().toString() + "개")
            }
        }
        ////////////////////////////////////////////////////////////////////////////////////
        val recyclerToday = findViewById<RecyclerView>(R.id.video_Today_recycler)
        dpAdjustment.setMargins(recyclerToday, 16,0,0,0)
        dpAdjustment.setScale(recyclerToday.layoutParams, 365f,365f)
        recyclerToday.layoutManager = GridLayoutManager(this, 3 )
        if(todayListCategory.isEmpty()){
            videoCustomToday.visibility = View.GONE
            todayLine.visibility=View.GONE

        }else {
            recyclerToday.adapter = todayAdapter
        }

        val todayAllCheck = View.OnClickListener{
            boolean = if (!boolean) {
                todayAdapter.setCheckAll(false, position)

                RepositoryImpl.getVideoList().clear()
                RepositoryImpl.getToday().clear()


                true
            } else {
                todayAdapter.setCheckAll(true, position)
                false
            }
        }

        videoCustomToday.setCheckListener(todayAllCheck)

        ////////////////////////////////////////////////////////////////////////////
        val recyclerYesterday = findViewById<RecyclerView>(R.id.video_Yesterday_recycler)
        dpAdjustment.setMargins(recyclerYesterday, 0,0,2,0)
        dpAdjustment.setScale(recyclerYesterday.layoutParams, 365f,365f)
        recyclerYesterday.layoutManager = GridLayoutManager(this, 3)
        if(yesterdayListCategory.isEmpty()){
            videoCustomYesterday.visibility = View.GONE
            yesterdayLine.visibility=View.GONE

        }else {
            recyclerYesterday.adapter = yesterdayAdapter
        }

        val yesterdayAllCheck = View.OnClickListener{
            boolean = if (!boolean) {
                yesterdayAdapter.setCheckAll(false, position)
                RepositoryImpl.getVideoList().clear()
                RepositoryImpl.getYesterday().clear()
                true
            } else {
                yesterdayAdapter.setCheckAll(true, position)
                false
            }
        }

        videoCustomYesterday.setCheckListener(yesterdayAllCheck)
        ////////////////////////////////////////////////////////////////////////////////////////
        val recyclerMonth = findViewById<RecyclerView>(R.id.video_Month_recycler)
        dpAdjustment.setMargins(recyclerMonth, 0,0,2,0)
        dpAdjustment.setScale(recyclerMonth.layoutParams, 365f,365f)
        recyclerMonth.layoutManager = GridLayoutManager(this, 3)
        if(monthListCategory.isEmpty()){
            videoCustomMonth.visibility = View.GONE
            monthLine.visibility=View.GONE

        }else {
            recyclerMonth.adapter = monthAdapter
        }

        val monthAllCheck = View.OnClickListener{
            boolean = if (!boolean) {
                monthAdapter.setCheckAll(false, position)
                RepositoryImpl.getVideoList().clear()
                RepositoryImpl.getMonth().clear()
                true
            } else {
                monthAdapter.setCheckAll(true, position)
                false
            }
        }

        videoCustomMonth.setCheckListener(monthAllCheck)
        /////////////////////////////////////////////////////////////////////////////////
        val recyclerThreeMonth = findViewById<RecyclerView>(R.id.video_threeMonth_recycler)

        dpAdjustment.setMargins(recyclerThreeMonth, 0,0,2,0)
        dpAdjustment.setScale(recyclerThreeMonth.layoutParams, 365f,365f)
        recyclerThreeMonth.layoutManager = GridLayoutManager(this, 3)
        if(threeMonthListCategory.isEmpty()){
            videoCustomThreeMonth.visibility = View.GONE
            threeMonthLine.visibility=View.GONE

        }else {
            recyclerThreeMonth.adapter = threeMonthAdapter
        }

        val threeMonthAllCheck = View.OnClickListener{
            boolean = if (!boolean) {
                threeMonthAdapter.setCheckAll(false, position)
                RepositoryImpl.getVideoList().clear()
                RepositoryImpl.getThreeMonth().clear()
                true
            } else {
                threeMonthAdapter.setCheckAll(true, position)
                false
            }
        }

        videoCustomThreeMonth.setCheckListener(threeMonthAllCheck)
        /////////////////////////////////////////////////////////////////////////////////////
        val recyclerSixMonth = findViewById<RecyclerView>(R.id.video_sixMonth_recycler)
        dpAdjustment.setMargins(recyclerSixMonth, 0,0,2,0)
        dpAdjustment.setScale(recyclerSixMonth.layoutParams, 365f,365f)
        recyclerSixMonth.layoutManager = GridLayoutManager(this, 3)
        if(sixMonthListCategory.isEmpty()){
            videoCustomSixMonth.visibility = View.GONE
            sixMonthLine.visibility=View.GONE

        }else {
            recyclerSixMonth.adapter = sixMonthAdapter
        }


        val sixMonthAllCheck = View.OnClickListener{
            boolean = if (!boolean) {
                sixMonthAdapter.setCheckAll(false, position)
                RepositoryImpl.getVideoList().clear()
                RepositoryImpl.getSixMonth().clear()
                true
            } else {
                sixMonthAdapter.setCheckAll(true, position)
                false
            }
        }

        videoCustomSixMonth.setCheckListener(sixMonthAllCheck)
        //////////////////////////////////////////////////////////////////////////////////////
        val recyclerYearDown = findViewById<RecyclerView>(R.id.video_YearDown_recycler)
        dpAdjustment.setMargins(recyclerYearDown, 0,0,2,0)
        dpAdjustment.setScale(recyclerYearDown.layoutParams, 365f,365f)
        recyclerYearDown.layoutManager = GridLayoutManager(this, 3)
        if(yearDownListCategory.isEmpty()){
            videoCustomYearDown.visibility = View.GONE
            yearDownLine.visibility=View.GONE

        }else {
            recyclerYearDown.adapter = yearDownAdapter
        }


        val yearDownAllCheck = View.OnClickListener{
            boolean = if (!boolean) {
                yearDownAdapter.setCheckAll(false, position)
                RepositoryImpl.getVideoList().clear()
                RepositoryImpl.getYearDown().clear()
                true
            } else {
                yearDownAdapter.setCheckAll(true, position)
                false
            }
        }

        videoCustomYearDown.setCheckListener(yearDownAllCheck)
        /////////////////////////////////////////////////////////////////////////////////
        val recyclerYear = findViewById<RecyclerView>(R.id.video_Year_recycler)
        dpAdjustment.setMargins(recyclerYear, 0,0,2,0)
        dpAdjustment.setScale(recyclerYear.layoutParams, 365f,365f)
        recyclerYear.layoutManager = GridLayoutManager(this, 3)
        if(yearListCategory.isEmpty()){
            videoCustomYear.visibility = View.GONE
            yearLine.visibility=View.GONE

        }else {
            recyclerYear.adapter = yearAdapter
        }

        val yearAllCheck = View.OnClickListener{
            boolean = if (!boolean) {
                yearAdapter.setCheckAll(false, position)
                RepositoryImpl.getVideoList().clear()
                RepositoryImpl.getYear().clear()
                true
            } else {
                yearAdapter.setCheckAll(true, position)
                false
            }
        }

        videoCustomYear.setCheckListener(yearAllCheck)
        /////////////////////////////////////////////////////////////////////////////
        val recyclerTwoYear = findViewById<RecyclerView>(R.id.video_twoYear_recycler)
        dpAdjustment.setMargins(recyclerTwoYear, 0,0,2,0)
        dpAdjustment.setScale(recyclerTwoYear.layoutParams, 365f,365f)
        recyclerTwoYear.layoutManager = GridLayoutManager(this, 3)
        if(twoYearListCategory.isEmpty()){
            videoCustomTwoYear.visibility = View.GONE
            twoYearLine.visibility=View.GONE

        }else{
            recyclerTwoYear.adapter = twoYearAdapter
        }


        val twoYearAllCheck = View.OnClickListener{
            boolean = if (!boolean) {
                twoYearAdapter.setCheckAll(false, position)
                RepositoryImpl.getVideoList().clear()
                RepositoryImpl.getTwoYear().clear()
                true
            } else {
                twoYearAdapter.setCheckAll(true, position)
                false
            }
        }

        videoCustomTwoYear.setCheckListener(twoYearAllCheck)
        removeVideo?.setOnClickListener {

            selectRemoveVideo()
        }
    }
  

    private fun getAllVideoPath() {

        val externalStorage: Uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI
        val indexID: Int
        var videoID: Long
        val cursor = contentResolver.query(
            MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
            null,
            null,
            null,
            MediaStore.Video.VideoColumns.DATE_TAKEN
        )

        val time = cursor?.getColumnIndexOrThrow(MediaStore.Video.Media.DATE_TAKEN)
        val playtime = cursor?.getColumnIndexOrThrow(MediaStore.Video.VideoColumns.DURATION)

        val menus : Vector<Menu> = Vector()
        if (cursor != null) {
                indexID = cursor.getColumnIndexOrThrow(MediaStore.Video.Media._ID)
                while (cursor.moveToNext()) {
                    videoID = cursor.getLong(indexID)

                    uriVideo = Uri.withAppendedPath(externalStorage, "" + videoID)

                    val bitmap :Bitmap = MediaStore.Video.Thumbnails.getThumbnail(contentResolver, videoID, MediaStore.Video.Thumbnails.MINI_KIND, null)
                    val size = cursor.getColumnIndex(MediaStore.Video.Media.SIZE)
                    val videoSize = cursor.getLong(size)
                    var videoTime = cursor.getLong(time!!)
                    val play = cursor.getLong(playtime!!)

                    val playCalendar = Calendar.getInstance()
                    playCalendar.timeInMillis = play
                    val formatPlayDate = SimpleDateFormat("hh:mm:ss")
                    val calendar = Calendar.getInstance()
                    calendar.timeInMillis = videoTime


                    val today = System.currentTimeMillis()
                    diffTime = (today - videoTime) / 1000
                    val count = arrayListOf(videoID).count()

                    val videoSizeList = arrayListOf(videoSize).sum()
                    allVideoSize += videoSizeList
                    allVideoCount += count

                    videoListCategory.add(VideoData(
                        type = TYPE_VIDEO,
                        video = uriVideo!!,
                        videoSize = videoSize,
                        videoTime = formatPlayDate.format(play),
                        videoPackage =  TYPE_VIDEO_PACKAGE,
                        videoName = videoID.toString(),
                        videoChoose = false,
                        diffTime = diffTime
                    ))

                }

             cursor.close()
            }

    }

    fun today(){
        todayCheckboxList= RepositoryImpl.getToday()
        if(todayListCategory.isNotEmpty()){
            val checkItr = todayCheckboxList.iterator()

            while (checkItr.hasNext()) {
                val checkListItr = checkItr.next()
                val checkImageItr = todayListCategory.iterator()
                while (checkImageItr.hasNext()) {
                    val checkImageListItr = checkImageItr.next()
                    if (checkListItr.galleryName == checkImageListItr.videoName) {
                        context.contentResolver?.openFileDescriptor(
                            checkImageListItr.video,
                            "w",
                            null
                        ).use {
                            checkImageListItr.video.let {
                                contentResolver.delete(
                                    checkImageListItr.video,
                                    null,
                                    null
                                )
                                Toast.makeText(this, "파일을 삭제 했습니다.", Toast.LENGTH_LONG).show()
                            }

                        }
                        checkImageItr.remove()
                        todayAdapter.set(todayListCategory)

                    }
                }
                checkItr.remove()
            }
        }
    }

    fun yesterday(){
        yesterdayCheckboxList = RepositoryImpl.getYesterday()
        if(yesterdayListCategory.isNotEmpty()){
            val checkItr = checkboxList.iterator()

            while (checkItr.hasNext()) {
                val checkListItr = checkItr.next()
                val checkImageItr = yesterdayCheckboxList.iterator()
                while (checkImageItr.hasNext()) {
                    val checkImageListItr = checkImageItr.next()
                    if (checkListItr.videoName == checkImageListItr.galleryName) {
                        context.contentResolver?.openFileDescriptor(
                            checkImageListItr.gallery,
                            "w",
                            null
                        ).use {
                            checkImageListItr.gallery.let {
                                contentResolver.delete(
                                    checkImageListItr.gallery,
                                    null,
                                    null
                                )
                                Toast.makeText(this, "파일을 삭제 했습니다.", Toast.LENGTH_LONG).show()
                            }


                        }
                        checkImageItr.remove()
                        yesterdayAdapter.set(yesterdayListCategory)

                    }
                }
                checkItr.remove()
            }
        }


    }
    fun month(){
        monthCheckboxList = RepositoryImpl.getMonth()
        if(monthListCategory.isNotEmpty()) {
            val checkItr = checkboxList.iterator()

            while (checkItr.hasNext()) {
                val checkListItr = checkItr.next()
                val checkImageItr = monthCheckboxList.iterator()
                while (checkImageItr.hasNext()) {
                    val checkImageListItr = checkImageItr.next()
                    if (checkListItr.videoName == checkImageListItr.galleryName) {
                        context.contentResolver?.openFileDescriptor(
                            checkImageListItr.gallery,
                            "w",
                            null
                        ).use {
                            checkImageListItr.gallery.let {
                                contentResolver.delete(
                                    checkImageListItr.gallery,
                                    null,
                                    null
                                )
                                Toast.makeText(this, "파일을 삭제 했습니다.", Toast.LENGTH_LONG).show()
                            }
                        }
                        checkImageItr.remove()
                        monthAdapter.set(monthListCategory)
                    }
                }
                checkItr.remove()
            }
        }
    }
    fun threeMonth (){
        threeMonthCheckboxList = RepositoryImpl.getThreeMonth()
        if(threeMonthListCategory.isNotEmpty()){
            val checkItr = checkboxList.iterator()

            while (checkItr.hasNext()) {
                val checkListItr = checkItr.next()
                val checkImageItr = threeMonthCheckboxList.iterator()
                while (checkImageItr.hasNext()) {
                    val checkImageListItr = checkImageItr.next()
                    if (checkListItr.videoName == checkImageListItr.galleryName) { context.contentResolver?.openFileDescriptor(checkImageListItr.gallery, "w", null).use {
                        checkImageListItr.gallery.let { contentResolver.delete(checkImageListItr.gallery, null, null)
                            Toast.makeText(this, "파일을 삭제 했습니다.", Toast.LENGTH_LONG).show()
                        }

                    }
                        checkImageItr.remove()
                        threeMonthAdapter.set(threeMonthListCategory)
                    }
                }
                checkItr.remove()
            }

        }

    }
    fun sixMonth(){
        sixMonthCheckboxList = RepositoryImpl.getSixMonth()
        if(sixMonthListCategory.isNotEmpty()){
            val checkItr = checkboxList.iterator()

            while (checkItr.hasNext()) {
                val checkListItr = checkItr.next()
                val checkImageItr = sixMonthCheckboxList.iterator()
                while (checkImageItr.hasNext()) {
                    val checkImageListItr = checkImageItr.next()
                    if (checkListItr.videoName == checkImageListItr.galleryName) {
                        context.contentResolver?.openFileDescriptor(
                            checkImageListItr.gallery,
                            "w",
                            null
                        ).use {
                            checkImageListItr.gallery.let {
                                contentResolver.delete(
                                    checkImageListItr.gallery,
                                    null,
                                    null
                                )
                                Toast.makeText(this, "파일을 삭제 했습니다.", Toast.LENGTH_LONG).show()
                            }


                        }
                        checkImageItr.remove()

                    }
                }
                checkItr.remove()
                sixMonthAdapter.set(sixMonthListCategory)
            }

        }

    }
    fun yearDown(){
        yearDownCheckboxList = RepositoryImpl.getYearDown()
        if(yearDownListCategory.isNotEmpty()){
            val checkItr = checkboxList.iterator()

            while (checkItr.hasNext()) {
                val checkListItr = checkItr.next()
                val checkImageItr = yearDownCheckboxList.iterator()
                while (checkImageItr.hasNext()) {
                    val checkImageListItr = checkImageItr.next()
                    if (checkListItr.videoName == checkImageListItr.galleryName) {
                        context.contentResolver?.openFileDescriptor(
                            checkImageListItr.gallery,
                            "w",
                            null
                        ).use {
                            checkImageListItr.gallery.let {
                                contentResolver.delete(
                                    checkImageListItr.gallery,
                                    null,
                                    null
                                )
                                Toast.makeText(this, "파일을 삭제 했습니다.", Toast.LENGTH_LONG).show()
                            }


                        }
                        checkImageItr.remove()
                        yearDownAdapter.set(yearDownListCategory)

                    }
                }
                checkItr.remove()
            }
        }

    }
    fun year(){
        yearCheckboxList = RepositoryImpl.getYear()
        if(yearListCategory.isNotEmpty()){
            val checkItr = checkboxList.iterator()

            while (checkItr.hasNext()) {
                val checkListItr = checkItr.next()
                val checkImageItr = yearCheckboxList.iterator()
                while (checkImageItr.hasNext()) {
                    val checkImageListItr = checkImageItr.next()
                    if (checkListItr.videoName == checkImageListItr.galleryName) {
                        context.contentResolver?.openFileDescriptor(
                            checkImageListItr.gallery,
                            "w",
                            null
                        ).use {
                            checkImageListItr.gallery.let {
                                contentResolver.delete(
                                    checkImageListItr.gallery,
                                    null,
                                    null
                                )
                                Toast.makeText(this, "파일을 삭제 했습니다.", Toast.LENGTH_LONG).show()
                            }
                        }
                        checkImageItr.remove()
                        yearAdapter.set(yearListCategory)

                    }
                }
                checkItr.remove()
            }
        }

    }
    fun twoYear (){
        twoYearCheckboxList = RepositoryImpl.getTwoYear()
        if(twoYearListCategory.isNotEmpty()){
            val checkItr = checkboxList.iterator()

            while (checkItr.hasNext()) {
                val checkListItr = checkItr.next()
                val checkVideoItr = twoYearCheckboxList.iterator()
                while (checkVideoItr.hasNext()) {
                    val checkVideoListItr = checkVideoItr.next()
                    if (checkListItr.videoName == checkVideoListItr.galleryName) {
                        context.contentResolver?.openFileDescriptor(
                            checkVideoListItr.gallery,
                            "w",
                            null
                        ).use {
                            checkVideoListItr.gallery.let {
                                contentResolver.delete(
                                    checkVideoListItr.gallery,
                                    null,
                                    null
                                )
                                Toast.makeText(this, "파일을 삭제 했습니다.", Toast.LENGTH_LONG).show()
                            }


                        }
                        checkVideoItr.remove()
                        twoYearAdapter.set(twoYearListCategory)
                    }
                }
                checkItr.remove()
            }
        }
    }

    var appCount: Int = 0
    var sum =0
    private fun selectRemoveVideo() {
        finish()
        overridePendingTransition(0,0)
        val intent :Intent= intent
        startActivity(intent)
        overridePendingTransition(0,0)
        checkboxList = RepositoryImpl.getVideoList()
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