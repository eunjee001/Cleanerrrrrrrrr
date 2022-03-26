package com.kyoungss.cleaner.storage.video

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

class ManageVideoSizeActivity : AppCompatActivity() {
    var context: Context = this
    private lateinit var dpAdjustment: DisplayAdjustment

    private val twoGBListCategory = ArrayList<VideoSizeData>()
    private val oneGBListCategory = ArrayList<VideoSizeData>()
    private val oneGBDownListCategory = ArrayList<VideoSizeData>()
    private val fiveMBListCategory = ArrayList<VideoSizeData>()
    private val oneMBListCategory = ArrayList<VideoSizeData>()
    private val videoListCategory = ArrayList<VideoSizeData>()

    private var removeVideo: Button? = null
    var popupWindow : PopupWindow ?= null

    private var VideoSizeAdapter = VideoSizeAdapter(oneGBDownListCategory,context)
    private var oneMBDownAdapter = VideoSizeAdapter(oneMBListCategory,context)
    private var fiveMBDownAdapter = VideoSizeAdapter(fiveMBListCategory,context)
    private var oneGBDownAdapter = VideoSizeAdapter(oneGBDownListCategory,context)
    private var oneGBAdapter = VideoSizeAdapter(oneGBListCategory,context)
    private var twoGBAdapter = VideoSizeAdapter(twoGBListCategory,context)
    
    private var checkboxList = ArrayList<CheckVideoData>()
    private var twoGBCheckboxList = ArrayList<CheckStorageData>()
    private var oneGBCheckboxList = ArrayList<CheckStorageData>()
    private var oneGBDownCheckboxList = ArrayList<CheckStorageData>()
    private var fiveMBDownCheckboxList = ArrayList<CheckStorageData>()
    private var oneMBDownCheckboxList = ArrayList<CheckStorageData>()

    private var allInfo: TextView? = null
    private lateinit var allSize: TextView
    private lateinit var allCount: TextView
    var selectBtn: CheckBox? = null
    var videoSize: Long = 0
    var videoAllSize: Long = 0
    var videoAllCount: Int = 0
    var count = 0
    var uriVideo: Uri? = null

    companion object {
        const val TYPE_VIDEO = 0L
        const val TYPE_VIDEO_PACKAGE = 0x01
        const val TYPE_VIDEO_TIME = 0x02

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_video_size)

        val menuBar = findViewById<Button>(R.id.menu)
        val view: View = layoutInflater.inflate(R.layout.popup_gallery_layout, null)

        menuBar.setOnClickListener {
            popupWindow = PopupWindow(
                view,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                true
            )
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
        allSize.text = Formatter.formatFileSize(context, videoAllSize)

        allCount = findViewById(R.id.infoTitleCount)
        dpAdjustment.setMargins(allCount, 14,34,0,0)
        allCount.text = videoAllCount.toString() + "개"

        val videoMenu = findViewById<Button>(R.id.menu)
        dpAdjustment.setMargins(videoMenu, 0,31,19,0)
        dpAdjustment.setScale(videoMenu.layoutParams, 24f,24f)

        ///////////////////////twoGB////////////////////////
        val videoCustomTwoGB = findViewById<VideoSizeCustomView>(R.id.video_twoGB)
        dpAdjustment.setMargins(videoCustomTwoGB, 0,0,0,0)
        dpAdjustment.setScale(videoCustomTwoGB.layoutParams, 360f,56f)

        val expandTwoGB = findViewById<Expandable>(R.id.video_twoGB_expand)

        val twoGBLine = findViewById<View>(R.id.line_twoGB)
        dpAdjustment.setMargins(twoGBLine, 0,0,0,0)
        dpAdjustment.setScale(twoGBLine.layoutParams, 360f,5f)

        //////////////////////////////////oneGB////////////////////////////
        val videoCustomOneGB = findViewById<VideoSizeCustomView>(R.id.video_oneGB)
        dpAdjustment.setMargins(videoCustomOneGB, 0,0,0,0)
        dpAdjustment.setScale(videoCustomOneGB.layoutParams, 360f,56f)

        val expandOneGB = findViewById<Expandable>(R.id.video_oneGB_expand)

        val oneGBLine = findViewById<View>(R.id.line_oneGB)
        dpAdjustment.setMargins(oneGBLine, 0,0,0,0)
        dpAdjustment.setScale(oneGBLine.layoutParams, 360f,5f)

        /////////////////////////oneGBDown//////////////////////
        val videoCustomOneGBDown = findViewById<VideoSizeCustomView>(R.id.video_oneGB_Down)
        dpAdjustment.setMargins(videoCustomOneGBDown, 0,0,0,0)
        dpAdjustment.setScale(videoCustomOneGBDown.layoutParams, 360f,56f)

        val expandOneGBDown = findViewById<Expandable>(R.id.video_oneGB_Down_expand)

        val oneGBDownLine = findViewById<View>(R.id.line_oneGB_Down)
        dpAdjustment.setMargins(oneGBDownLine, 0,0,0,0)
        dpAdjustment.setScale(oneGBDownLine.layoutParams, 360f,5f)

        ////////////////////////////500MB Down///////////////////////////
        val videoCustomFiveMBDown = findViewById<VideoSizeCustomView>(R.id.video_500MB_Down)
        dpAdjustment.setMargins(videoCustomFiveMBDown, 0,0,0,0)
        dpAdjustment.setScale(videoCustomFiveMBDown.layoutParams, 360f,56f)

        val expandFiveMBDown = findViewById<Expandable>(R.id.video_500MB_Down_expand)

        val fiveMBDownLine = findViewById<View>(R.id.line_500MB_Down)
        dpAdjustment.setMargins(fiveMBDownLine, 0,0,0,0)
        dpAdjustment.setScale(fiveMBDownLine.layoutParams, 360f,5f)

        //////////////////////////100MB Down//////////////////////////////
        val videoCustomOneMBDown = findViewById<VideoSizeCustomView>(R.id.video_100MB_Down)
        dpAdjustment.setMargins(videoCustomOneMBDown, 0,0,0,0)
        dpAdjustment.setScale(videoCustomOneMBDown.layoutParams, 360f,56f)
        val expandOneMBDown = findViewById<Expandable>(R.id.video_100MB_Down_expand)

        val oneMBDownLine = findViewById<View>(R.id.line_100MB_Down)
        dpAdjustment.setMargins(oneMBDownLine, 0,0,0,0)
        dpAdjustment.setScale(oneMBDownLine.layoutParams, 360f,5f)

        ///////////////////////////////////////////////////////

        removeVideo = findViewById(R.id.video_size_btn)
        dpAdjustment.setScale(removeVideo!!.layoutParams, 328f,48f)
        dpAdjustment.setMargins(removeVideo!!, 16,0,0,16)


        selectBtn = findViewById(R.id.grid_selected)


        var boolean = false

        val position =0

        val listenerTwoGB = View.OnClickListener {
            boolean = if (!boolean) {
                expandTwoGB.expand()
                videoCustomTwoGB.setVideoMenu(R.drawable.icon_menu_drop_up)

                true
            } else {
                expandTwoGB.collapse()
                videoCustomTwoGB.setVideoMenu(R.drawable.icon_menu_drop_down)

                false
            }
        }

        videoCustomTwoGB.setListener(listenerTwoGB)


        /////////////////////////////////////////////////////////////
        /////////////////////////////////////////////////////////////
        /////////////////////////////////////////////////////////////

        val listenerOneGB = View.OnClickListener {
            boolean = if (!boolean) {
                expandOneGB.expand()
                videoCustomOneGB.setVideoMenu(R.drawable.icon_menu_drop_up)

                true
            } else {
                expandOneGB.collapse()
                videoCustomOneGB.setVideoMenu(R.drawable.icon_menu_drop_down)

                false
            }
        }
        videoCustomOneGB.setListener(listenerOneGB)

        /////////////////////////////////////////////////////////////
        /////////////////////////////////////////////////////////////
        /////////////////////////////////////////////////////////////

        val listenerOneGBDown = View.OnClickListener {
            boolean = if (!boolean) {
                expandOneGBDown.expand()
                videoCustomOneGBDown.setVideoMenu(R.drawable.icon_menu_drop_up)

                true
            } else {
                expandOneGBDown.collapse()
                videoCustomOneGBDown.setVideoMenu(R.drawable.icon_menu_drop_down)

                false
            }
        }
        videoCustomOneGBDown.setListener(listenerOneGBDown)


        /////////////////////////////////////////////////////////////
        /////////////////////////////////////////////////////////////
        /////////////////////////////////////////////////////////////

        val listenerFiveMBDown = View.OnClickListener {
            boolean = if (!boolean) {
                expandFiveMBDown.expand()
                videoCustomFiveMBDown.setVideoMenu(R.drawable.icon_menu_drop_up)

                true
            } else {
                expandFiveMBDown.collapse()
                videoCustomFiveMBDown.setVideoMenu(R.drawable.icon_menu_drop_down)

                false
            }
        }
        videoCustomFiveMBDown.setListener(listenerFiveMBDown)


        /////////////////////////////////////////////////////////////
        /////////////////////////////////////////////////////////////
        /////////////////////////////////////////////////////////////

        val listenerOneMBDown = View.OnClickListener {
            boolean = if (!boolean) {
                expandOneMBDown.expand()
                videoCustomOneMBDown.setVideoMenu(R.drawable.icon_menu_drop_up)

                true
            } else {
                expandOneMBDown.collapse()
                videoCustomOneMBDown.setVideoMenu(R.drawable.icon_menu_drop_down)

                false
            }
        }
        videoCustomOneMBDown.setListener(listenerOneMBDown)


        /////////////////////////////////////////////////////////////
        /////////////////////////////////////////////////////////////
        /////////////////////////////////////////////////////////////
        var diffOneMbSize :Long =0
        var diffFiveMbSize :Long =0
        var diffOneGbDownSize :Long =0
        var diffOneGbSize :Long =0
        var diffTwoGbSize :Long =0

        for (diff in videoListCategory) {
            if (0 <= (diff.videoSize) || (0 < (diff.videoSize / Actions.MB) && 100 > (diff.videoSize  / Actions.MB))) {
                oneMBListCategory.add(VideoSizeData(type = diff.type , video = diff.video, videoSize = diff.videoSize, videoTime = diff.videoTime, videoPackage = diff.videoPackage, videoName = diff.videoName, videoChoose = diff.videoChoose))
                diffOneMbSize += diff.videoSize
                videoCustomOneMBDown.setVideoSizeAll(Formatter.formatFileSize(context, diffOneMbSize))
                videoCustomOneMBDown.setVideoCount(oneMBListCategory.count().toString() + "개")
            } else if (100 <= (diff.videoSize  / Actions.MB) && 500 > (diff.videoSize  / Actions.MB)) {
                fiveMBListCategory.add(VideoSizeData(type = diff.type , video = diff.video, videoSize = diff.videoSize, videoTime = diff.videoTime, videoPackage = diff.videoPackage, videoName = diff.videoName, videoChoose = diff.videoChoose))
                diffFiveMbSize += diff.videoSize
                videoCustomFiveMBDown.setVideoSizeAll(Formatter.formatFileSize(context, diffFiveMbSize))
                videoCustomFiveMBDown.setVideoCount(fiveMBListCategory.count().toString() + "개")
            } else if (500 <= (diff.videoSize  / Actions.MB) && 1 > (diff.videoSize  / Actions.GB)) {
                oneGBDownListCategory.add(VideoSizeData(type = diff.type , video = diff.video, videoSize = diff.videoSize, videoTime = diff.videoTime, videoPackage = diff.videoPackage, videoName = diff.videoName, videoChoose = diff.videoChoose))
                diffOneGbDownSize += diff.videoSize
                videoCustomOneGBDown.setVideoSizeAll(Formatter.formatFileSize(context, diffOneGbDownSize))
                videoCustomOneGBDown.setVideoCount(oneGBDownListCategory.count().toString() + "개")
            } else if (1 <= (diff.videoSize  / Actions.GB) && 2 > (diff.videoSize  / Actions.GB)) {
                oneGBListCategory.add(VideoSizeData(type = diff.type , video = diff.video, videoSize = diff.videoSize, videoTime = diff.videoTime, videoPackage = diff.videoPackage, videoName = diff.videoName, videoChoose = diff.videoChoose))
                diffOneGbSize += diff.videoSize
                videoCustomOneGB.setVideoSizeAll(Formatter.formatFileSize(context, diffOneGbSize))
                videoCustomOneGB.setVideoCount(oneGBListCategory.count().toString() + "개")
            } else if (2 <= (diff.videoSize  / Actions.GB)) {
                twoGBListCategory.add(VideoSizeData(type = diff.type , video = diff.video, videoSize = diff.videoSize, videoTime = diff.videoTime, videoPackage = diff.videoPackage, videoName = diff.videoName, videoChoose = diff.videoChoose))
                diffTwoGbSize += diff.videoSize
                videoCustomTwoGB.setVideoSizeAll(Formatter.formatFileSize(context, diffTwoGbSize))
                videoCustomTwoGB.setVideoCount(twoGBListCategory.count().toString() + "개")
            }
        }
        //////////////////////////////////////////////////////////////////////////////////
        val recyclerOneMBDown = findViewById<RecyclerView>(R.id.video_100MB_Down_recycler)
        recyclerOneMBDown.layoutManager = GridLayoutManager(this, 3)
        dpAdjustment.setMargins(recyclerOneMBDown, 16,0,0,0)
        dpAdjustment.setScale(recyclerOneMBDown.layoutParams, 365f,365f)

        if (oneMBListCategory.isEmpty()) {
            videoCustomOneMBDown.visibility = View.GONE
            oneMBDownLine.visibility = View.GONE

        } else {
            recyclerOneMBDown.adapter = oneMBDownAdapter
        }

        val oneMBDownAllCheck = View.OnClickListener{
            boolean = if (!boolean) {
                oneMBDownAdapter.setCheckAll(false, position)
                RepositoryImpl.getVideoList().clear()
                RepositoryImpl.getOneMB().clear()
                true
            } else {
                oneMBDownAdapter.setCheckAll(true, position)
                false
            }
        }

        videoCustomOneMBDown.setCheckListener(oneMBDownAllCheck)
        ////////////////////////////////////////////////////////////////////////////////////
        val recyclerFiveMBDown = findViewById<RecyclerView>(R.id.video_500MB_Down_recycler)
        dpAdjustment.setMargins(recyclerFiveMBDown, 16,0,0,0)
        dpAdjustment.setScale(recyclerFiveMBDown.layoutParams, 365f,365f)
        recyclerFiveMBDown.layoutManager = GridLayoutManager(this, 3)
        if (fiveMBListCategory.isEmpty()) {
            videoCustomFiveMBDown.visibility = View.GONE
            fiveMBDownLine.visibility=View.GONE

        } else {
            recyclerFiveMBDown.adapter = fiveMBDownAdapter
        }

        val fiveMBDownAllCheck = View.OnClickListener{
            boolean = if (!boolean) {
                fiveMBDownAdapter.setCheckAll(false, position)
                RepositoryImpl.getVideoList().clear()
                RepositoryImpl.getFiveMB().clear()
                true
            } else {
                fiveMBDownAdapter.setCheckAll(true, position)
                false
            }
        }

        videoCustomFiveMBDown.setCheckListener(fiveMBDownAllCheck)
        //////////////////////////////////////////////////////////////////////////////////////
        val recyclerOneGBDown = findViewById<RecyclerView>(R.id.video_oneGB_Down_recycler)
        recyclerOneGBDown.layoutManager = GridLayoutManager(this, 3)
        dpAdjustment.setMargins(recyclerOneGBDown, 16,0,0,0)
        dpAdjustment.setScale(recyclerOneGBDown.layoutParams, 365f,365f)
        if (oneGBDownListCategory.isEmpty()) {
            videoCustomOneGBDown.visibility = View.GONE
            oneGBDownLine.visibility=View.GONE

        } else {
            recyclerOneGBDown.adapter = oneGBDownAdapter
        }

        val oneGBDownAllCheck = View.OnClickListener{
            boolean = if (!boolean) {
                oneGBDownAdapter.setCheckAll(false, position)
                RepositoryImpl.getVideoList().clear()
                RepositoryImpl.getOneGBDown().clear()
                true
            } else {
                oneGBDownAdapter.setCheckAll(true, position)
                false
            }
        }

        videoCustomOneGBDown.setCheckListener(oneGBDownAllCheck)
        ////////////////////////////////////////////////////////////////////////////////////////
        val recyclerOneGB = findViewById<RecyclerView>(R.id.video_oneGB_recycler)
        recyclerOneGB.layoutManager = GridLayoutManager(this, 3)
        dpAdjustment.setMargins(recyclerOneGB, 0,0,2,0)
        dpAdjustment.setScale(recyclerOneGB.layoutParams, 365f,365f)
        if (oneGBListCategory.isEmpty()) {
            videoCustomOneGB.visibility = View.GONE
            oneGBLine.visibility=View.GONE

        } else {
            recyclerOneGB.adapter = oneGBAdapter
        }

        val oneGBAllCheck = View.OnClickListener{
            boolean = if (!boolean) {
                oneGBAdapter.setCheckAll(false, position)
                RepositoryImpl.getVideoList().clear()
                RepositoryImpl.getOneGB().clear()
                true
            } else {
                oneGBAdapter.setCheckAll(true, position)
                false
            }
        }

        videoCustomOneGB.setCheckListener(oneGBAllCheck)
        /////////////////////////////////////////////////////////////////////////////////////////

        val recyclerTwoGB = findViewById<RecyclerView>(R.id.video_twoGB_recycler)
        recyclerTwoGB.layoutManager = GridLayoutManager(this, 3)
        dpAdjustment.setMargins(recyclerTwoGB, 0,0,2,0)
        dpAdjustment.setScale(recyclerTwoGB.layoutParams, 365f,365f)
        if (twoGBListCategory.isEmpty()) {
            videoCustomTwoGB.visibility = View.GONE
            twoGBLine.visibility=View.GONE

        } else {
            recyclerTwoGB.adapter = twoGBAdapter
        }

        val twoGBAllCheck = View.OnClickListener{
            boolean = if (!boolean) {
                twoGBAdapter.setCheckAll(false, position)
                RepositoryImpl.getVideoList().clear()
                RepositoryImpl.getTwoGB().clear()
                true
            } else {
                twoGBAdapter.setCheckAll(true, position)
                false
            }
        }

        videoCustomTwoGB.setCheckListener(twoGBAllCheck)
        removeVideo?.setOnClickListener {
            selectRemoveVideo()
        }
        //////////////////////////////////////////////////////////////////////////////
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

        val playtime = cursor?.getColumnIndexOrThrow(MediaStore.Video.VideoColumns.DURATION)

        if (cursor != null) {

            indexID = cursor.getColumnIndexOrThrow(MediaStore.Video.Media._ID)
            while (cursor.moveToNext()) {
                videoID = cursor.getLong(indexID)

                uriVideo = Uri.withAppendedPath(externalStorage, "" + videoID)
                val size = cursor.getColumnIndex(MediaStore.Video.Media.SIZE)
                videoSize = cursor.getLong(size)

                val play = cursor.getLong(playtime!!)
                val playCalendar = Calendar.getInstance()
                playCalendar.timeInMillis = play
                count = arrayListOf(videoID).count()

                val videoSizeList = arrayListOf(videoSize).sum()
                videoAllSize += videoSizeList
                videoAllCount += count
                val formatPlayDate = SimpleDateFormat("hh:mm:ss")

                videoListCategory.add(
                    VideoSizeData(
                        type = TYPE_VIDEO,
                        video = uriVideo!!,
                        videoSize = videoSize,
                        videoTime = formatPlayDate.format(play),
                        videoPackage = TYPE_VIDEO_PACKAGE,
                        videoName = videoID.toString(),
                        videoChoose = false
                    )
                )

            }
        }
        cursor!!.close()
    }

    fun oneMB(){
        oneMBDownCheckboxList = RepositoryImpl.getOneMB()

        if(oneMBListCategory.isNotEmpty()){
            val checkItr = oneMBDownCheckboxList.iterator()

            while (checkItr.hasNext()) {
                val checkListItr = checkItr.next()
                val checkVideoItr = oneMBListCategory.iterator()
                while (checkVideoItr.hasNext()) {
                    val checkVideoListItr = checkVideoItr.next()
                    if (checkListItr.galleryName == checkVideoListItr.videoName) {
                        context.contentResolver?.openFileDescriptor(
                            checkVideoListItr.video,
                            "w",
                            null
                        ).use {
                            checkVideoListItr.video.let {
                                contentResolver.delete(
                                    checkVideoListItr.video,
                                    null,
                                    null
                                )
                                Toast.makeText(this, "파일을 삭제 했습니다.", Toast.LENGTH_LONG).show()
                            }
                        }
                        checkVideoItr.remove()
                        oneMBDownAdapter.set(oneMBListCategory)

                    }
                }
                checkItr.remove()
            }

        }
    }
    fun fiveMB(){
        fiveMBDownCheckboxList = RepositoryImpl.getFiveMB()
        if(fiveMBListCategory.isNotEmpty()){
            val checkItr = fiveMBDownCheckboxList.iterator()

            while (checkItr.hasNext()) {
                val checkListItr = checkItr.next()
                val checkVideoItr = fiveMBListCategory.iterator()
                while (checkVideoItr.hasNext()) {
                    val checkVideoListItr = checkVideoItr.next()
                    println("checkListItr. ${checkListItr.galleryName}, checkVideoListItr . ${checkVideoListItr.videoName}")

                    if (checkListItr.galleryName == checkVideoListItr.videoName) {
                        context.contentResolver?.openFileDescriptor(
                            checkVideoListItr.video,
                            "w",
                            null
                        ).use {
                            checkVideoListItr.video.let {
                                contentResolver.delete(
                                    checkVideoListItr.video,
                                    null,
                                    null
                                )
                                Toast.makeText(this, "파일을 삭제 했습니다.", Toast.LENGTH_LONG).show()
                            }
                        }
                        checkVideoItr.remove()
                        fiveMBDownAdapter.set(fiveMBListCategory)

                    }
                }
                checkItr.remove()


            }
        }
    }
    fun oneGBDown(){
        oneGBDownCheckboxList = RepositoryImpl.getOneGBDown()

        if(oneGBDownListCategory.isNotEmpty()){
            val checkItr = oneGBDownCheckboxList.iterator()

            while (checkItr.hasNext()) {
                val checkListItr = checkItr.next()
                val checkVideoItr = oneGBDownListCategory.iterator()
                while (checkVideoItr.hasNext()) {
                    val checkVideoListItr = checkVideoItr.next()
                    if (checkListItr.galleryName == checkVideoListItr.videoName) {
                        context.contentResolver?.openFileDescriptor(
                            checkVideoListItr.video,
                            "w",
                            null
                        ).use {
                            checkVideoListItr.video.let {
                                contentResolver.delete(
                                    checkVideoListItr.video,
                                    null,
                                    null
                                )
                                Toast.makeText(this, "파일을 삭제 했습니다.", Toast.LENGTH_LONG).show()
                            }
                        }
                        checkVideoItr.remove()
                        oneGBDownAdapter.set(oneGBDownListCategory)
                    }
                }
                checkItr.remove()
            }
        }

    }

    fun oneGB(){
        oneGBCheckboxList = RepositoryImpl.getOneGB()
        if(oneGBListCategory.isNotEmpty()){
            val checkItr = oneGBCheckboxList.iterator()

            while (checkItr.hasNext()) {
                val checkListItr = checkItr.next()
                val checkVideoItr = oneGBListCategory.iterator()
                while (checkVideoItr.hasNext()) {
                    val checkVideoListItr = checkVideoItr.next()
                    if (checkListItr.galleryName == checkVideoListItr.videoName) {
                        context.contentResolver?.openFileDescriptor(
                            checkVideoListItr.video,
                            "w",
                            null
                        ).use {
                            checkVideoListItr.video.let {
                                contentResolver.delete(checkVideoListItr.video, null, null)
                                Toast.makeText(this, "파일을 삭제 했습니다.", Toast.LENGTH_LONG).show()
                            }
                        }
                        checkVideoItr.remove()
                        oneGBAdapter.set(oneGBListCategory)

                    }
                }
                checkItr.remove()
            }
        }

    }
    fun twoGB() {
        twoGBCheckboxList = RepositoryImpl.getTwoGB()

        if (twoGBListCategory.isNotEmpty()) {
            val checkItr = twoGBCheckboxList.iterator()
            while (checkItr.hasNext()) {
                val checkListItr = checkItr.next()
                val checkVideoItr = twoGBListCategory.iterator()
                while (checkVideoItr.hasNext()) {
                    val checkVideoListItr = checkVideoItr.next()
                    if (checkListItr.galleryName == checkVideoListItr.videoName) {
                        context.contentResolver?.openFileDescriptor(
                            checkVideoListItr.video,
                            "w",
                            null
                        ).use {
                            checkVideoListItr.video.let {
                                contentResolver.delete(
                                    checkVideoListItr.video,
                                    null,
                                    null
                                )
                                Toast.makeText(this, "파일을 삭제 했습니다.", Toast.LENGTH_LONG).show()
                            }
                        }
                        checkVideoItr.remove()
                        twoGBAdapter.set(twoGBListCategory)

                    }
                }
                checkItr.remove()
            }
        }
    }


    private fun selectRemoveVideo(){
        checkboxList = RepositoryImpl.getVideoList()
        finish()
        overridePendingTransition(0,0)
        val intent :Intent= intent
        startActivity(intent)
        overridePendingTransition(0,0)
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