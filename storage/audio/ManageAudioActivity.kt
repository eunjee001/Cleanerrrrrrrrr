package com.kyoungss.cleaner.storage.audio

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
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
import com.kyoungss.cleaner.storage.CheckStorageData
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class ManageAudioActivity : AppCompatActivity() {
    var context: Context = this
    private lateinit var dpAdjustment: DisplayAdjustment

    private val twoYearListCategory = ArrayList<AudioData>()
    private val yearListCategory = ArrayList<AudioData>()
    private val yearDownListCategory = ArrayList<AudioData>()
    private val sixMonthListCategory = ArrayList<AudioData>()
    private val threeMonthListCategory = ArrayList<AudioData>()
    private val monthListCategory = ArrayList<AudioData>()
    private val yesterdayListCategory = ArrayList<AudioData>()
    private val todayListCategory = ArrayList<AudioData>()
    private var removeaudio: Button? = null
    private  lateinit var  allSize : TextView
    private  lateinit var  allCount : TextView
    private val audioListCategory =ArrayList<AudioData>()
    private var audioAdapter = AudioAdapter(audioListCategory, context)
    private var monthAdapter = AudioAdapter(monthListCategory, context)
    private var todayAdapter = AudioAdapter(todayListCategory, context)
    private var yesterdayAdapter = AudioAdapter(yesterdayListCategory, context)
    private var threeMonthAdapter = AudioAdapter(threeMonthListCategory, context)
    private var sixMonthAdapter = AudioAdapter(sixMonthListCategory, context)
    private var yearDownAdapter = AudioAdapter(yearDownListCategory, context)
    private var yearAdapter = AudioAdapter(yearListCategory, context)
    private var twoYearAdapter = AudioAdapter(twoYearListCategory, context)

    private var checkboxList =ArrayList<CheckAudioData>()
    private var todayCheckboxList =ArrayList<CheckStorageData>()
    private var yesterdayCheckboxList =ArrayList<CheckStorageData>()
    private var monthCheckboxList =ArrayList<CheckStorageData>()
    private var threeMonthCheckboxList =ArrayList<CheckStorageData>()
    private var sixMonthCheckboxList =ArrayList<CheckStorageData>()
    private var yearDownCheckboxList =ArrayList<CheckStorageData>()
    private var yearCheckboxList =ArrayList<CheckStorageData>()
    private var twoYearCheckboxList =ArrayList<CheckStorageData>()
    private var diffTime: Long = 0
    val position =0
    var popupWindow : PopupWindow ?= null

    var audioAllSize:Long = 0
    var audioAllCount = 0

    var selectBtn:CheckBox ?= null

    companion object {
        const val TYPE_AUDIO = 0L
        const val TYPE_AUDIO_PACKAGE = 0x01
        const val DELETE_PERMISSION_REQUEST = 0x02
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_audio)

        val menuBar = findViewById<Button>(R.id.menu)
        val view: View = layoutInflater.inflate(R.layout.popup_etc_layout, null)

        menuBar.setOnClickListener{
            popupWindow = PopupWindow(view, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true)
            popupWindow!!.showAsDropDown(menuBar, -205, -100)
            popupWindow!!.setBackgroundDrawable(ColorDrawable(Color.WHITE))

        }

        val firstPopupTextView = view.findViewById<TextView>(R.id.popup_text_first)
        firstPopupTextView.setOnClickListener {
            val intent = Intent(this, ManageAudioActivity::class.java)
            this.startActivity(intent)
            finish()
        }

        val secondsPopupTextView = view.findViewById<TextView>(R.id.popup_text_second)
        secondsPopupTextView.setOnClickListener {

            val intent = Intent(this, ManageAudioSizeActivity::class.java)
            this.startActivity(intent)
            finish()

        }

        val thirdsPopupTextView = view.findViewById<TextView>(R.id.popup_text_third)
        thirdsPopupTextView.setOnClickListener {

            val intent = Intent(this, AudioActivity::class.java)

            this.startActivity(intent)
            finish()

        }

        getAllAudioPath()
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

        val audioBack = findViewById<Button>(R.id.audioBack)
        dpAdjustment.setMargins(audioBack,16,16,0,0 )
        dpAdjustment.setScale(audioBack.layoutParams,24f,24f)

        val audioTitle = findViewById<TextView>(R.id.audioTitle)
        dpAdjustment.setMargins(audioTitle, 12,12,0,0)

        allSize = findViewById(R.id.infoTitle)
        dpAdjustment.setMargins(allSize, 16,33,0,0)
        allSize.text = Formatter.formatFileSize(context, audioAllSize)

        allCount = findViewById(R.id.infoTitleCount)
        dpAdjustment.setMargins(allCount, 14,34,0,0)
        allCount.text = audioAllCount.toString() + "개"

        val audioMenu = findViewById<Button>(R.id.menu)
        dpAdjustment.setMargins(audioMenu, 0,31,19,0)
        dpAdjustment.setScale(audioMenu.layoutParams, 24f,24f)

        //////////////twoYear//////////////
        val twoYearLayout = findViewById<RelativeLayout>(R.id.relative_layout_twoYear)

        val twoYearLine = findViewById<View>(R.id.line_twoYear)
        dpAdjustment.setMargins(twoYearLine, 0,0,0,0)
        dpAdjustment.setScale(twoYearLine.layoutParams, 360f,5f)

        val audioCustomTwoYear = findViewById<AudioCustomView>(R.id.audio_twoYear)
        dpAdjustment.setMargins(audioCustomTwoYear, 0,10,0,0)
        dpAdjustment.setScale(audioCustomTwoYear.layoutParams, 360f,56f)

        val expandTwoYear = findViewById<Expandable>(R.id.audio_twoYear_expand)


        //////////////year///////////////////////
        val yearLayout = findViewById<RelativeLayout>(R.id.relative_layout_year)
        val yearLine = findViewById<View>(R.id.line_Year)
        dpAdjustment.setMargins(yearLine, 0,0,0,0)
        dpAdjustment.setScale(yearLine.layoutParams, 360f,5f)

        val audioCustomYear = findViewById<AudioCustomView>(R.id.audio_Year)
        dpAdjustment.setMargins(audioCustomYear, 0,0,0,0)
        dpAdjustment.setScale(audioCustomYear.layoutParams, 360f,56f)

        val expandYear = findViewById<Expandable>(R.id.audio_Year_expand)

        /////////////////yearDown///////////////////////
        val yearDownLayout = findViewById<RelativeLayout>(R.id.relative_layout_yearDown)
        val yearDownLine = findViewById<View>(R.id.line_YearDown)
        dpAdjustment.setMargins(yearDownLine, 0,0,0,0)
        dpAdjustment.setScale(yearDownLine.layoutParams, 360f,5f)

        val audioCustomYearDown = findViewById<AudioCustomView>(R.id.audio_YearDown)
        dpAdjustment.setMargins(audioCustomYearDown, 0,0,0,0)
        dpAdjustment.setScale(audioCustomYearDown.layoutParams, 360f,56f)

        val expandYearDown = findViewById<Expandable>(R.id.audio_YearDown_expand)

        ///////////////////////////////sixMonth////////////////////
        val sixMonthLayout = findViewById<RelativeLayout>(R.id.relative_layout_sixMonth)
        val sixMonthLine = findViewById<View>(R.id.line_sixMonth)
        dpAdjustment.setMargins(sixMonthLine, 0,0,0,0)
        dpAdjustment.setScale(sixMonthLine.layoutParams,  360f,5f)

        val audioCustomSixMonth = findViewById<AudioCustomView>(R.id.audio_sixMonth)
        dpAdjustment.setMargins(audioCustomSixMonth, 0,0,0,0)
        dpAdjustment.setScale(audioCustomSixMonth.layoutParams, 360f,56f)

        val expandSixMonth = findViewById<Expandable>(R.id.audio_sixMonth_expand)

        ////////////////////////////threeMonth///////////////////////
        val threeMonthLayout = findViewById<RelativeLayout>(R.id.relative_layout_threeMonth)
        val threeMonthLine = findViewById<View>(R.id.line_threeMonth)
        dpAdjustment.setMargins(threeMonthLine, 0,0,0,0)
        dpAdjustment.setScale(threeMonthLine.layoutParams, 360f,5f)

        val audioCustomThreeMonth = findViewById<AudioCustomView>(R.id.audio_threeMonth)
        dpAdjustment.setMargins(audioCustomThreeMonth, 0,0,0,0)
        dpAdjustment.setScale(audioCustomThreeMonth.layoutParams, 360f,56f)

        val expandThreeMonth = findViewById<Expandable>(R.id.audio_threeMonth_expand)

        ///////////////////////////month //////////////////////////
        val monthLayout = findViewById<RelativeLayout>(R.id.relative_layout_Month)
        val monthLine = findViewById<View>(R.id.line_Month)
        dpAdjustment.setMargins(monthLine, 0,0,0,0)
        dpAdjustment.setScale(monthLine.layoutParams, 360f,5f)

        val audioCustomMonth = findViewById<AudioCustomView>(R.id.audio_Month)
        dpAdjustment.setMargins(audioCustomMonth, 0,0,0,0)
        dpAdjustment.setScale(audioCustomMonth.layoutParams, 360f,56f)

        val expandMonth = findViewById<Expandable>(R.id.audio_Month_expand)

        /////////////////////yesterday///////////////////
        val yesterdayLayout = findViewById<RelativeLayout>(R.id.relative_layout_Yesterday)
        val yesterdayLine = findViewById<View>(R.id.line_Yesterday)
        dpAdjustment.setMargins(yesterdayLine, 0,0,0,0)
        dpAdjustment.setScale(yesterdayLine.layoutParams, 360f,5f)

        val audioCustomYesterday = findViewById<AudioCustomView>(R.id.audio_Yesterday)
        dpAdjustment.setMargins(audioCustomYesterday, 0,0,0,0)
        dpAdjustment.setScale(audioCustomYesterday.layoutParams, 360f,56f)

        val expandYesterday = findViewById<Expandable>(R.id.audio_Yesterday_expand)

        //////////////////////today///////////////////////
        val todayLayout = findViewById<RelativeLayout>(R.id.relative_layout_Today)
        val todayLine = findViewById<View>(R.id.line_Today)
        dpAdjustment.setMargins(todayLine, 0,0,0,0)
        dpAdjustment.setScale(todayLine.layoutParams, 360f,5f)

        val audioCustomToday = findViewById<AudioCustomView>(R.id.audio_Today)
        dpAdjustment.setMargins(audioCustomToday, 0,0,0,0)
        dpAdjustment.setScale(audioCustomToday.layoutParams, 360f,56f)

        val expandToday = findViewById<Expandable>(R.id.audio_Today_expand)


        removeaudio = findViewById(R.id.audio_btn)
        dpAdjustment.setScale(removeaudio!!.layoutParams, 328f,48f)
        dpAdjustment.setMargins(removeaudio!!, 16,0,0,16)


        selectBtn = findViewById(R.id.grid_selected)

        var boolean = false

       
        val listenerTwoYear = View.OnClickListener {
            boolean = if (!boolean) {
                expandTwoYear.expand()
                audioCustomTwoYear.setAudioMenu(R.drawable.icon_menu_drop_up)

                true
            } else {
                expandTwoYear.collapse()
                audioCustomTwoYear.setAudioMenu(R.drawable.icon_menu_drop_down)

                false
            }
        }
        audioCustomTwoYear.setListener(listenerTwoYear)


        /////////////////////////////////////////////////////////////
        /////////////////////////////////////////////////////////////
        /////////////////////////////////////////////////////////////
       
        val listenerYear = View.OnClickListener {
            boolean = if (!boolean) {
                expandYear.expand()
                audioCustomYear.setAudioMenu(R.drawable.icon_menu_drop_up)

                true
            } else {
                expandYear.collapse()
                audioCustomYear.setAudioMenu(R.drawable.icon_menu_drop_down)

                false
            }
        }
        audioCustomYear.setListener(listenerYear)

        /////////////////////////////////////////////////////////////
        /////////////////////////////////////////////////////////////
        /////////////////////////////////////////////////////////////
        
        val listenerYearDown = View.OnClickListener {
            boolean = if (!boolean) {
                expandYearDown.expand()
                audioCustomYearDown.setAudioMenu(R.drawable.icon_menu_drop_up)

                true
            } else {
                expandYearDown.collapse()
                audioCustomYearDown.setAudioMenu(R.drawable.icon_menu_drop_down)

                false
            }
        }
        audioCustomYearDown.setListener(listenerYearDown)


        /////////////////////////////////////////////////////////////
        /////////////////////////////////////////////////////////////
        /////////////////////////////////////////////////////////////
        
        val listenerSixMonth = View.OnClickListener {
            boolean = if (!boolean) {
                expandSixMonth.expand()
                audioCustomSixMonth.setAudioMenu(R.drawable.icon_menu_drop_up)

                true
            } else {
                expandSixMonth.collapse()
                audioCustomSixMonth.setAudioMenu(R.drawable.icon_menu_drop_down)

                false
            }
        }
        audioCustomSixMonth.setListener(listenerSixMonth)


        /////////////////////////////////////////////////////////////
        /////////////////////////////////////////////////////////////
        /////////////////////////////////////////////////////////////

        val listenerThreeMonth = View.OnClickListener {
            boolean = if (!boolean) {
                expandThreeMonth.expand()
                audioCustomThreeMonth.setAudioMenu(R.drawable.icon_menu_drop_up)

                true
            } else {
                expandThreeMonth.collapse()
                audioCustomThreeMonth.setAudioMenu(R.drawable.icon_menu_drop_down)

                false
            }
        }
        audioCustomThreeMonth.setListener(listenerThreeMonth)


        /////////////////////////////////////////////////////////////
        /////////////////////////////////////////////////////////////
        /////////////////////////////////////////////////////////////

        val listenerMonth = View.OnClickListener {
            boolean = if (!boolean) {
                expandMonth.expand()
                audioCustomMonth.setAudioMenu(R.drawable.icon_menu_drop_up)

                true
            } else {
                expandMonth.collapse()
                audioCustomMonth.setAudioMenu(R.drawable.icon_menu_drop_down)

                false
            }
        }
        audioCustomMonth.setListener(listenerMonth)


        /////////////////////////////////////////////////////////////
        /////////////////////////////////////////////////////////////
        /////////////////////////////////////////////////////////////

        val listenerYesterday = View.OnClickListener {
            boolean = if (!boolean) {
                expandYesterday.expand()
                audioCustomYesterday.setAudioMenu(R.drawable.icon_menu_drop_up)

                true
            } else {
                expandYesterday.collapse()
                audioCustomYesterday.setAudioMenu(R.drawable.icon_menu_drop_down)

                false
            }
        }
        audioCustomYesterday.setListener(listenerYesterday)

        /////////////////////////////////////////////////////////////
        /////////////////////////////////////////////////////////////
        /////////////////////////////////////////////////////////////

        val listenerToday = View.OnClickListener {
            boolean = if (!boolean) {
                expandToday.expand()
                audioCustomToday.setAudioMenu(R.drawable.icon_menu_drop_up)

                true
            } else {
                expandToday.collapse()
                audioCustomToday.setAudioMenu(R.drawable.icon_menu_drop_down)

                false
            }
        }

        audioCustomToday.setListener(listenerToday)

        var diffTodayAllSize : Long = 0
        var diffYesterAllSize : Long = 0
        var diffMonthAllSize : Long = 0
        var diffThreeMonthAllSize : Long = 0
        var diffSixMonthAllSize : Long = 0
        var diffYearDownAllSize : Long = 0
        var diffYearAllSize : Long = 0
        var diffTwoYearAllSize : Long = 0

        for(diff in audioListCategory){
                if ((0 < (diff.diffTime/ Actions.sec) && 60 > (diff.diffTime / Actions.sec)) || (0 <= (diff.diffTime / Actions.min) && 24 > (diff.diffTime/ Actions.min))) {
                    todayListCategory.add(AudioData(type = diff.type , audioIcon = diff.audioIcon, audioName = diff.audioName, audioTime = diff.audioTime, audioSize = diff.audioSize, audioChoose = diff.audioChoose, audioUri = diff.audioUri, diffTime = diff.diffTime ))
                    diffTodayAllSize += diff.audioSize
                    audioCustomToday.setAudioSize(Formatter.formatFileSize(context, diffTodayAllSize))
                    audioCustomToday.setAudioCount(todayListCategory.count().toString() + "개")

                } else if (0 < (diff.diffTime / Actions.hour) && 1>=(diff.diffTime / Actions.hour)) {
                    yesterdayListCategory.add(AudioData(type = diff.type , audioIcon = diff.audioIcon, audioName = diff.audioName, audioTime = diff.audioTime, audioSize = diff.audioSize, audioChoose = diff.audioChoose, audioUri = diff.audioUri, diffTime = diff.diffTime ))
                    diffYesterAllSize += diff.audioSize
                    audioCustomYesterday.setAudioSize(Formatter.formatFileSize(context, diffYesterAllSize))
                    audioCustomYesterday.setAudioCount(yesterdayListCategory.count().toString() + "개")

                } else if (30 > (diff.diffTime / Actions.hour)) {
                    monthListCategory.add(AudioData(type = diff.type , audioIcon = diff.audioIcon, audioName = diff.audioName, audioTime = diff.audioTime, audioSize = diff.audioSize, audioChoose = diff.audioChoose, audioUri = diff.audioUri, diffTime = diff.diffTime ))
                    diffMonthAllSize += diff.audioSize
                    audioCustomMonth.setAudioSize(Formatter.formatFileSize(context, diffMonthAllSize))
                    audioCustomMonth.setAudioCount(monthListCategory.count().toString() + "개")

                } else if (0 < (diff.diffTime / Actions.day) && 3> (diff.diffTime / Actions.day) ) {
                    threeMonthListCategory.add(AudioData(type = diff.type , audioIcon = diff.audioIcon, audioName = diff.audioName, audioTime = diff.audioTime, audioSize = diff.audioSize, audioChoose = diff.audioChoose, audioUri = diff.audioUri, diffTime = diff.diffTime ))
                    diffThreeMonthAllSize += diff.audioSize
                    audioCustomThreeMonth.setAudioSize(Formatter.formatFileSize(context, diffThreeMonthAllSize))
                    audioCustomThreeMonth.setAudioCount(threeMonthListCategory.count().toString() + "개")

                } else if (3 <= (diff.diffTime / Actions.day) && (diff.diffTime / Actions.day) < 6) {
                    sixMonthListCategory.add(AudioData(type = diff.type , audioIcon = diff.audioIcon, audioName = diff.audioName, audioTime = diff.audioTime, audioSize = diff.audioSize, audioChoose = diff.audioChoose, audioUri = diff.audioUri, diffTime = diff.diffTime ))
                    diffSixMonthAllSize += diff.audioSize
                    audioCustomSixMonth.setAudioSize(Formatter.formatFileSize(context, diffSixMonthAllSize))
                    audioCustomSixMonth.setAudioCount(sixMonthListCategory.count().toString() + "개")

                } else if (6 <= (diff.diffTime / Actions.day) && (diff.diffTime / Actions.day) < 12) {
                    yearDownListCategory.add(AudioData(type = diff.type , audioIcon = diff.audioIcon, audioName = diff.audioName, audioTime = diff.audioTime, audioSize = diff.audioSize, audioChoose = diff.audioChoose, audioUri = diff.audioUri, diffTime = diff.diffTime ))
                    diffYearDownAllSize += diff.audioSize
                    audioCustomYearDown.setAudioSize(Formatter.formatFileSize(context, diffYearDownAllSize))
                    audioCustomYearDown.setAudioCount(yearDownListCategory.count().toString() + "개")

                } else if ((diff.diffTime / Actions.month) < 2) {
                    yearListCategory.add(AudioData(type = diff.type , audioIcon = diff.audioIcon, audioName = diff.audioName, audioTime = diff.audioTime, audioSize = diff.audioSize, audioChoose = diff.audioChoose, audioUri = diff.audioUri, diffTime = diff.diffTime ))
                    diffYearAllSize += diff.audioSize
                    audioCustomYear.setAudioSize(Formatter.formatFileSize(context, diffYearAllSize))
                    audioCustomYear.setAudioCount(yearListCategory.count().toString() + "개")

                } else if ((diff.diffTime/Actions.month) > 2 ){
                    twoYearListCategory.add(AudioData(type = diff.type , audioIcon = diff.audioIcon, audioName = diff.audioName, audioTime = diff.audioTime, audioSize = diff.audioSize, audioChoose = diff.audioChoose, audioUri = diff.audioUri, diffTime = diff.diffTime ))
                    diffTwoYearAllSize +=diff.audioSize
                    audioCustomTwoYear.setAudioSize(Formatter.formatFileSize(context, diffTwoYearAllSize))
                    audioCustomTwoYear.setAudioCount(twoYearListCategory.count().toString() + "개")

                }
        }
        ////////////////////////////////////////////////////////////////////////////////////

        val recyclerToday = findViewById<RecyclerView>(R.id.audio_Today_recycler)

        recyclerToday.layoutManager = LinearLayoutManager(this)
        if(todayListCategory.isEmpty()){
            audioCustomToday.visibility = View.GONE
            todayLine.visibility=View.GONE

        }else {
            recyclerToday.adapter = todayAdapter
        }

        val todayAllCheck = View.OnClickListener{
            boolean = if (boolean) {
                todayAdapter.setCheckAll(false, position)
                RepositoryImpl.getAudioList().clear()
                RepositoryImpl.getToday().clear()


                true
            } else {
                todayAdapter.setCheckAll(true, position)
                false
            }
        }

        audioCustomToday.setCheckListener(todayAllCheck)
        //////////////////////////////////////////////////////////////////////////////////////
        val recyclerYesterday = findViewById<RecyclerView>(R.id.audio_Yesterday_recycler)
        recyclerYesterday.layoutManager = LinearLayoutManager(this)
        if(yesterdayListCategory.isEmpty()){
            audioCustomYesterday.visibility = View.GONE
            yesterdayLine.visibility=View.GONE

        }else {
            recyclerYesterday.adapter = yesterdayAdapter
        }

        val yesterdayAllCheck = View.OnClickListener{
            boolean = if (boolean) {
                yesterdayAdapter.setCheckAll(false, position)
                RepositoryImpl.getAudioList().clear()
                RepositoryImpl.getYesterday().clear()

                true
            } else {
                yesterdayAdapter.setCheckAll(true, position)
                false
            }
        }

        audioCustomYesterday.setCheckListener(yesterdayAllCheck)
        ////////////////////////////////////////////////////////////////////////////////////
        val recyclerMonth = findViewById<RecyclerView>(R.id.audio_Month_recycler)
        recyclerMonth.layoutManager = LinearLayoutManager(this)
        if(monthListCategory.isEmpty()){
            audioCustomMonth.visibility = View.GONE
            monthLine.visibility=View.GONE

        }else {
            recyclerMonth.adapter = monthAdapter
        }

        val monthAllCheck = View.OnClickListener{
            boolean = if (boolean) {
                monthAdapter.setCheckAll(false, position)
                RepositoryImpl.getAudioList().clear()
                RepositoryImpl.getMonth().clear()

                true
            } else {
                monthAdapter.setCheckAll(true, position)
                false
            }
        }

        audioCustomMonth.setCheckListener(monthAllCheck)
        ///////////////////////////////////////////////////////////////////////////////////////
        val recyclerThreeMonth = findViewById<RecyclerView>(R.id.audio_threeMonth_recycler)
       recyclerThreeMonth.layoutManager = LinearLayoutManager(this)
        if(threeMonthListCategory.isEmpty()){
            audioCustomThreeMonth.visibility = View.GONE
            threeMonthLine.visibility=View.GONE

        }else {
            recyclerThreeMonth.adapter = threeMonthAdapter
        }

        val threeMonthAllCheck = View.OnClickListener{
            boolean = if (boolean) {
                threeMonthAdapter.setCheckAll(false, position)
                RepositoryImpl.getAudioList().clear()
                RepositoryImpl.getThreeMonth().clear()

                true
            } else {
                threeMonthAdapter.setCheckAll(true, position)
                false
            }
        }

        audioCustomThreeMonth.setCheckListener(threeMonthAllCheck)
        /////////////////////////////////////////////////////////////////////////////////////////
        val recyclerSixMonth = findViewById<RecyclerView>(R.id.audio_sixMonth_recycler)

        recyclerSixMonth.layoutManager =LinearLayoutManager(this)
        if(sixMonthListCategory.isEmpty()){
            audioCustomSixMonth.visibility = View.GONE
            sixMonthLine.visibility=View.GONE

        }else {
            recyclerSixMonth.adapter = sixMonthAdapter
        }

        val sixMonthAllCheck = View.OnClickListener{
            boolean = if (boolean) {
                sixMonthAdapter.setCheckAll(false, position)
                RepositoryImpl.getAudioList().clear()
                RepositoryImpl.getSixMonth().clear()

                true
            } else {
                sixMonthAdapter.setCheckAll(true, position)
                false
            }
        }

        audioCustomSixMonth.setCheckListener(sixMonthAllCheck)
        /////////////////////////////////////////////////////////////////////////////////////////
        val recyclerYearDown = findViewById<RecyclerView>(R.id.audio_YearDown_recycler)

        recyclerYearDown.layoutManager = LinearLayoutManager(this)
        if(yearDownListCategory.isEmpty()){
            audioCustomYearDown.visibility = View.GONE
            yearDownLine.visibility=View.GONE

        }else {
            recyclerYearDown.adapter = yearDownAdapter
        }

        val yearDownAllCheck = View.OnClickListener{
            boolean = if (boolean) {
                yearDownAdapter.setCheckAll(false, position)
                RepositoryImpl.getAudioList().clear()
                RepositoryImpl.getYearDown().clear()

                true
            } else {
                yearDownAdapter.setCheckAll(true, position)
                false
            }
        }

        audioCustomYearDown.setCheckListener(yearDownAllCheck)
        //////////////////////////////////////////////////////////////////////////////////////////
        val recyclerYear = findViewById<RecyclerView>(R.id.audio_Year_recycler)

        recyclerYear.layoutManager =LinearLayoutManager(this)
        if(yearListCategory.isEmpty()){
            audioCustomYear.visibility = View.GONE
            yearLine.visibility=View.GONE

        }else {
            recyclerYear.adapter = yearAdapter
        }

        val yearAllCheck = View.OnClickListener{
            boolean = if (boolean) {
                yearAdapter.setCheckAll(false, position)
                RepositoryImpl.getAudioList().clear()
                RepositoryImpl.getYear().clear()

                true
            } else {
                yearAdapter.setCheckAll(true, position)
                false
            }
        }

        audioCustomYear.setCheckListener(yearAllCheck)
        ////////////////////////////////////////////////////////////////////////////////////////

        val recyclerTwoYear = findViewById<RecyclerView>(R.id.audio_twoYear_recycler)
        recyclerTwoYear.layoutManager = LinearLayoutManager(this)
        if(twoYearListCategory.isEmpty()){
            audioCustomTwoYear.visibility = View.GONE
            twoYearLine.visibility=View.GONE

        }else{
            recyclerTwoYear.adapter = twoYearAdapter
        }

        val twoYearAllCheck = View.OnClickListener{
            boolean = if (boolean) {
                twoYearAdapter.setCheckAll(false, position)
                RepositoryImpl.getAudioList().clear()
                RepositoryImpl.getTwoYear().clear()
                true
            } else {
                twoYearAdapter.setCheckAll(true, position)
                false
            }
        }

        audioCustomTwoYear.setCheckListener(twoYearAllCheck)
        removeaudio?.setOnClickListener {
            selectRemoveAudio()
        }
    }


    private var contentUri : Uri? = null
    private fun getAllAudioPath() {
        val externalStorage: Uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI

        val indexID: Int

        var audioID: Long
        val cursor = contentResolver.query(
            MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
            null,
            null,
            null,
            MediaStore.Audio.AudioColumns.DATE_MODIFIED
        )
        var audioName: String? = null
        var play:Long =0
        val time = cursor!!.getColumnIndexOrThrow(MediaStore.Audio.AudioColumns.DATE_TAKEN)

        val playtime = cursor.getColumnIndexOrThrow(MediaStore.Audio.AudioColumns.DURATION)


        val audioPlayDate = SimpleDateFormat("hh:mm:ss")

        if (cursor != null && cursor.count> 0) {
            indexID = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media._ID)
            val nameID = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DISPLAY_NAME)
            //  val files = cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns.MEDIA_TYPE_AUDIO.toString())
            while (cursor.moveToNext()) {

                audioID = cursor.getLong(indexID)
                audioName = cursor.getString(nameID)
                play = cursor.getLong(playtime)
                val uriAudio = Uri.withAppendedPath(externalStorage, "" + audioID)

                //println(">>>>> file : $files")

                val size = cursor.getColumnIndex(MediaStore.Audio.Media.SIZE)
                val audioSize = cursor.getLong(size)
                val audioTime = cursor.getLong(time)

                val calendar = Calendar.getInstance()
                val playCalendar = Calendar.getInstance()
                println(">>>> $audioTime")

                calendar.timeInMillis = audioTime
                playCalendar.timeInMillis = play

                val count = arrayListOf(audioID).count()

                val audioSizeList = arrayListOf(audioSize).sum()
                audioAllSize += audioSizeList
                audioAllCount += count

                val today = System.currentTimeMillis()
                println(">>>> $today")
                diffTime = (today - audioTime) / 1000
                println(">>> diffTime : $diffTime")
                contentUri = Uri.withAppendedPath(
                    MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                    indexID.toString()
                )
                audioListCategory.add(
                    AudioData(
                        type = TYPE_AUDIO,
                        audioIcon = R.drawable.icon_music,
                        audioName = audioName!!,
                        audioTime = audioPlayDate.format(play),
                        audioSize = audioSize,
                        audioChoose = false,
                        audioUri = uriAudio!!,
                        diffTime = diffTime
                    )
                )

            }

        }
    }
    fun today(){
        todayCheckboxList= RepositoryImpl.getToday()
        if(todayListCategory.isNotEmpty()){
            val checkItr = todayCheckboxList.iterator()

            while (checkItr.hasNext()) {
                val checkListItr = checkItr.next()
                val checkAudioItr = todayListCategory.iterator()
                while (checkAudioItr.hasNext()) {
                    val checkAudioListItr = checkAudioItr.next()
                    if (checkListItr.galleryName == checkAudioListItr.audioName) {
                        context.contentResolver?.openFileDescriptor(
                            checkAudioListItr.audioUri,
                            "w",
                            null
                        ).use {
                            checkAudioListItr.audioUri.let {
                                contentResolver.delete(
                                    checkAudioListItr.audioUri,
                                    null,
                                    null
                                )
                                Toast.makeText(this, "파일을 삭제 했습니다.", Toast.LENGTH_LONG).show()
                            }
                        }
                        checkAudioItr.remove()
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
            val checkItr = yesterdayCheckboxList.iterator()

            while (checkItr.hasNext()) {
                val checkListItr = checkItr.next()
                val checkAudioItr = yesterdayListCategory.iterator()
                while (checkAudioItr.hasNext()) {
                    val checkAudioListItr = checkAudioItr.next()
                    if (checkListItr.galleryName == checkAudioListItr.audioName) {
                        context.contentResolver?.openFileDescriptor(
                            checkAudioListItr.audioUri,
                            "w",
                            null
                        ).use {
                            checkAudioListItr.audioUri.let {
                                contentResolver.delete(
                                    checkAudioListItr.audioUri,
                                    null,
                                    null
                                )
                                Toast.makeText(this, "파일을 삭제 했습니다.", Toast.LENGTH_LONG).show()
                            }


                        }
                        checkAudioItr.remove()
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
            val checkItr = monthCheckboxList.iterator()

            while (checkItr.hasNext()) {
                val checkListItr = checkItr.next()
                val checkAudioItr = monthListCategory.iterator()
                while (checkAudioItr.hasNext()) {
                    val checkAudioListItr = checkAudioItr.next()
                    if (checkListItr.galleryName == checkAudioListItr.audioName) {
                        context.contentResolver?.openFileDescriptor(
                            checkAudioListItr.audioUri,
                            "w",
                            null
                        ).use {
                            checkAudioListItr.audioUri.let {
                                contentResolver.delete(
                                    checkAudioListItr.audioUri,
                                    null,
                                    null
                                )
                                Toast.makeText(this, "파일을 삭제 했습니다.", Toast.LENGTH_LONG).show()
                            }
                        }
                        checkAudioItr.remove()
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
            val checkItr = threeMonthCheckboxList.iterator()

            while (checkItr.hasNext()) {
                val checkListItr = checkItr.next()
                val checkAudioItr = threeMonthListCategory.iterator()
                while (checkAudioItr.hasNext()) {
                    val checkAudioListItr = checkAudioItr.next()
                    if (checkListItr.galleryName == checkAudioListItr.audioName) { context.contentResolver?.openFileDescriptor(checkAudioListItr.audioUri, "w", null).use {
                        checkAudioListItr.audioUri.let { contentResolver.delete(checkAudioListItr.audioUri, null, null)
                            Toast.makeText(this, "파일을 삭제 했습니다.", Toast.LENGTH_LONG).show()
                        }

                    }
                        checkAudioItr.remove()
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
            val checkItr = sixMonthCheckboxList.iterator()

            while (checkItr.hasNext()) {
                val checkListItr = checkItr.next()
                val checkAudioItr = sixMonthListCategory.iterator()
                while (checkAudioItr.hasNext()) {
                    val checkAudioListItr = checkAudioItr.next()
                    if (checkListItr.galleryName == checkAudioListItr.audioName) {
                        context.contentResolver?.openFileDescriptor(
                            checkAudioListItr.audioUri,
                            "w",
                            null
                        ).use {
                            checkAudioListItr.audioUri.let {
                                contentResolver.delete(
                                    checkAudioListItr.audioUri,
                                    null,
                                    null
                                )
                                Toast.makeText(this, "파일을 삭제 했습니다.", Toast.LENGTH_LONG).show()
                            }


                        }
                        checkAudioItr.remove()
                        sixMonthAdapter.set(sixMonthListCategory)

                    }
                }
                checkItr.remove()
            }

        }

    }
    fun yearDown(){
        yearDownCheckboxList = RepositoryImpl.getYearDown()
        if(yearDownListCategory.isNotEmpty()){
            val checkItr = yearDownCheckboxList.iterator()

            while (checkItr.hasNext()) {
                val checkListItr = checkItr.next()
                val checkAudioItr = yearDownListCategory.iterator()
                while (checkAudioItr.hasNext()) {
                    val checkAudioListItr = checkAudioItr.next()
                    if (checkListItr.galleryName == checkAudioListItr.audioName) {
                        context.contentResolver?.openFileDescriptor(
                            checkAudioListItr.audioUri,
                            "w",
                            null
                        ).use {
                            checkAudioListItr.audioUri.let {
                                contentResolver.delete(
                                    checkAudioListItr.audioUri,
                                    null,
                                    null
                                )
                                Toast.makeText(this, "파일을 삭제 했습니다.", Toast.LENGTH_LONG).show()
                            }


                        }
                        checkAudioItr.remove()
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
            val checkItr = yearCheckboxList.iterator()

            while (checkItr.hasNext()) {
                val checkListItr = checkItr.next()
                val checkAudioItr = yearListCategory.iterator()
                while (checkAudioItr.hasNext()) {
                    val checkAudioListItr = checkAudioItr.next()
                    if (checkListItr.galleryName == checkAudioListItr.audioName) {
                        context.contentResolver?.openFileDescriptor(
                            checkAudioListItr.audioUri,
                            "w",
                            null
                        ).use {
                            checkAudioListItr.audioUri.let {
                                contentResolver.delete(
                                    checkAudioListItr.audioUri,
                                    null,
                                    null
                                )
                                Toast.makeText(this, "파일을 삭제 했습니다.", Toast.LENGTH_LONG).show()
                            }
                        }
                        checkAudioItr.remove()
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
            val checkItr = twoYearCheckboxList.iterator()

            while (checkItr.hasNext()) {
                val checkListItr = checkItr.next()
                val checkAudioItr = twoYearListCategory.iterator()
                while (checkAudioItr.hasNext()) {
                    val checkAudioListItr = checkAudioItr.next()
                    if (checkListItr.galleryName== checkAudioListItr.audioName) {
                        context.contentResolver?.openFileDescriptor(
                            checkAudioListItr.audioUri,
                            "w",
                            null
                        ).use {
                            checkAudioListItr.audioUri.let {
                                contentResolver.delete(
                                    checkAudioListItr.audioUri,
                                    null,
                                    null
                                )
                                Toast.makeText(this, "파일을 삭제 했습니다.", Toast.LENGTH_LONG).show()
                            }
                        }
                        checkAudioItr.remove()
                        twoYearAdapter.set(twoYearListCategory)
                    }
                }
                checkItr.remove()
            }
        }
    }

    private fun selectRemoveAudio(){
        finish()
        overridePendingTransition(0,0)
        val intent :Intent= intent
        startActivity(intent)
        overridePendingTransition(0,0)
        checkboxList = RepositoryImpl.getAudioList()

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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode == Activity.RESULT_OK && requestCode == DELETE_PERMISSION_REQUEST){
            selectRemoveAudio()
        }
    }
}