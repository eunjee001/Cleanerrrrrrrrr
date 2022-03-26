package com.kyoungss.cleaner.storage.audio

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

class ManageAudioSizeActivity : AppCompatActivity() {
    var context: Context = this
    private lateinit var dpAdjustment: DisplayAdjustment

    private val twoGBListCategory = ArrayList<AudioSizeData>()
    private val oneGBListCategory = ArrayList<AudioSizeData>()
    private val oneGBDownListCategory = ArrayList<AudioSizeData>()
    private val fiveMBListCategory = ArrayList<AudioSizeData>()
    private val oneMBListCategory = ArrayList<AudioSizeData>()
    private val audioListCategory =ArrayList<AudioSizeData>()

    private var removeAudio: Button? = null
    private lateinit var  allSize : TextView
    private lateinit var  allCount : TextView
    private var oneMBDownAdapter = AudioSizeAdapter(oneMBListCategory, context)
    private var fiveMBDownAdapter = AudioSizeAdapter(fiveMBListCategory, context)
    private var oneGBDownAdapter = AudioSizeAdapter(oneGBDownListCategory, context)
    private var oneGBAdapter = AudioSizeAdapter(oneGBListCategory, context)
    private var twoGBAdapter = AudioSizeAdapter(twoGBListCategory, context)

    private var checkboxList = ArrayList<CheckAudioData>()
    private var twoGBCheckboxList = ArrayList<CheckStorageData>()
    private var oneGBCheckboxList = ArrayList<CheckStorageData>()
    private var oneGBDownCheckboxList = ArrayList<CheckStorageData>()
    private var fiveMBDownCheckboxList = ArrayList<CheckStorageData>()
    private var oneMBDownCheckboxList = ArrayList<CheckStorageData>()
    var audioAllSize:Long = 0
    var audioAllCount:Int = 0
    var selectBtn: CheckBox? = null
    var audioSize:Long = 0
    var popupWindow : PopupWindow ?= null

    companion object {
        const val TYPE_AUDIO = 0L
        const val TYPE_AUDIO_PACKAGE = 0x01
        const val DELETE_PERMISSION_REQUEST = 0x02
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_audio_size)

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


        ///////////////////////twoGB////////////////////////
        val audioCustomTwoGB = findViewById<AudioSizeCustomView>(R.id.audio_twoGB)
        dpAdjustment.setMargins(audioCustomTwoGB, 0,0,0,0)
        dpAdjustment.setScale(audioCustomTwoGB.layoutParams, 360f,56f)

        val expandTwoGB = findViewById<Expandable>(R.id.audio_twoGB_expand)

        val twoGBLine = findViewById<View>(R.id.line_twoGB)
        dpAdjustment.setMargins(twoGBLine, 0,0,0,0)
        dpAdjustment.setScale(twoGBLine.layoutParams, 360f,5f)


        //////////////////////////////////oneGB////////////////////////////
        val audioCustomOneGB = findViewById<AudioSizeCustomView>(R.id.audio_oneGB)
        dpAdjustment.setMargins(audioCustomOneGB, 0,0,0,0)
        dpAdjustment.setScale(audioCustomOneGB.layoutParams, 360f,56f)

        val expandOneGB = findViewById<Expandable>(R.id.audio_oneGB_expand)

        val oneGBLine = findViewById<View>(R.id.line_oneGB)
        dpAdjustment.setMargins(oneGBLine, 0,0,0,0)
        dpAdjustment.setScale(oneGBLine.layoutParams, 360f,5f)



        /////////////////////////oneGBDown//////////////////////
        val audioCustomOneGBDown = findViewById<AudioSizeCustomView>(R.id.audio_oneGB_Down)
        dpAdjustment.setMargins(audioCustomOneGBDown, 0,0,0,0)
        dpAdjustment.setScale(audioCustomOneGBDown.layoutParams, 360f,56f)

        val expandOneGBDown = findViewById<Expandable>(R.id.audio_oneGB_Down_expand)

        val oneGBDownLine = findViewById<View>(R.id.line_oneGB_Down)
        dpAdjustment.setMargins(oneGBDownLine, 0,0,0,0)
        dpAdjustment.setScale(oneGBDownLine.layoutParams, 360f,5f)

        ////////////////////////////500MB Down///////////////////////////
        val audioCustomFiveMBDown = findViewById<AudioSizeCustomView>(R.id.audio_500MB_Down)
        dpAdjustment.setMargins(audioCustomFiveMBDown, 0,0,0,0)
        dpAdjustment.setScale(audioCustomFiveMBDown.layoutParams, 360f,56f)

        val expandFiveMBDown = findViewById<Expandable>(R.id.audio_500MB_Down_expand)

        val fiveMBDownLine = findViewById<View>(R.id.line_500MB_Down)
        dpAdjustment.setMargins(fiveMBDownLine, 0,0,0,0)
        dpAdjustment.setScale(fiveMBDownLine.layoutParams, 360f,5f)

        //////////////////////////100MB Down//////////////////////////////
        val audioCustomOneMBDown = findViewById<AudioSizeCustomView>(R.id.audio_100MB_Down)
        dpAdjustment.setMargins(audioCustomOneMBDown, 0,0,0,0)
        dpAdjustment.setScale(audioCustomOneMBDown.layoutParams, 360f,56f)

        val expandOneMBDown = findViewById<Expandable>(R.id.audio_100MB_Down_expand)

        val oneMBDownLine = findViewById<View>(R.id.line_100MB_Down)
        dpAdjustment.setMargins(oneMBDownLine, 0,0,0,0)
        dpAdjustment.setScale(oneMBDownLine.layoutParams, 360f,5f)

        ////////////////////////////////////////////////////////////////////////////////
        removeAudio = findViewById(R.id.audio_size_btn)
        dpAdjustment.setScale(removeAudio!!.layoutParams, 328f,48f)
        dpAdjustment.setMargins(removeAudio!!, 16,0,0,16)

        removeAudio?.setOnClickListener {
            selectRemoveAudio()
        }
        selectBtn = findViewById(R.id.audio_check)
        val position =0

        var boolean = false


        val listenerTwoGB = View.OnClickListener {
            boolean = if (!boolean) {
                expandTwoGB.expand()
                audioCustomTwoGB.setAudioMenu(R.drawable.icon_menu_drop_up)

                true
            } else {
                expandTwoGB.collapse()
                audioCustomTwoGB.setAudioMenu(R.drawable.icon_menu_drop_down)

                false
            }
        }

        audioCustomTwoGB.setListener(listenerTwoGB)


        /////////////////////////////////////////////////////////////
        /////////////////////////////////////////////////////////////
        /////////////////////////////////////////////////////////////

        val listenerOneGB = View.OnClickListener {
            boolean = if (!boolean) {
                expandOneGB.expand()
                audioCustomOneGB.setAudioMenu(R.drawable.icon_menu_drop_up)

                true
            } else {
                expandOneGB.collapse()
                audioCustomOneGB.setAudioMenu(R.drawable.icon_menu_drop_down)

                false
            }
        }
        audioCustomOneGB.setListener(listenerOneGB)

        /////////////////////////////////////////////////////////////
        /////////////////////////////////////////////////////////////
        /////////////////////////////////////////////////////////////

        val listenerOneGBDown = View.OnClickListener {
            boolean = if (!boolean) {
                expandOneGBDown.expand()
                audioCustomOneGBDown.setAudioMenu(R.drawable.icon_menu_drop_up)

                true
            } else {
                expandOneGBDown.collapse()
                audioCustomOneGBDown.setAudioMenu(R.drawable.icon_menu_drop_down)

                false
            }
        }
        audioCustomOneGBDown.setListener(listenerOneGBDown)


        /////////////////////////////////////////////////////////////
        /////////////////////////////////////////////////////////////
        /////////////////////////////////////////////////////////////

        val listenerFiveMBDown = View.OnClickListener {
            boolean = if (!boolean) {
                expandFiveMBDown.expand()
                audioCustomFiveMBDown.setAudioMenu(R.drawable.icon_menu_drop_up)

                true
            } else {
                expandFiveMBDown.collapse()
                audioCustomFiveMBDown.setAudioMenu(R.drawable.icon_menu_drop_down)

                false
            }
        }
        audioCustomFiveMBDown.setListener(listenerFiveMBDown)

        /////////////////////////////////////////////////////////////
        /////////////////////////////////////////////////////////////
        /////////////////////////////////////////////////////////////

        val listenerOneMBDown = View.OnClickListener {
            boolean = if (!boolean) {
                expandOneMBDown.expand()
                audioCustomOneMBDown.setAudioMenu(R.drawable.icon_menu_drop_up)

                true
            } else {
                expandOneMBDown.collapse()
                audioCustomOneMBDown.setAudioMenu(R.drawable.icon_menu_drop_down)

                false
            }
        }
        audioCustomOneMBDown.setListener(listenerOneMBDown)

        /////////////////////////////////////////////////////////////
        /////////////////////////////////////////////////////////////
        /////////////////////////////////////////////////////////////
        var diffOneMbSize :Long =0
        var diffFiveMbSize :Long =0
        var diffOneGbDownSize :Long =0
        var diffOneGbSize :Long =0
        var diffTwoGbSize :Long =0

        for(diff in audioListCategory) {
            if (0 <= (diff.audioSize) || (0 < (diff.audioSize / Actions.KB) && 100 > (diff.audioSize / Actions.MB))) {
                oneMBListCategory.add(AudioSizeData(type = diff.type , audioIcon = diff.audioIcon, audioName = diff.audioName, audioTime = diff.audioTime, audioSize = diff.audioSize, audioChoose = diff.audioChoose, audioUri = diff.audioUri))
                diffOneMbSize += diff.audioSize
                audioCustomOneMBDown.setAudioSizeAll(Formatter.formatFileSize(context, diffOneMbSize))
                audioCustomOneMBDown.setAudioCount(oneMBListCategory.count().toString() + "개")

            } else if (100 <= (diff.audioSize / Actions.MB) && 500 > (diff.audioSize / Actions.MB)) {
                fiveMBListCategory.add(AudioSizeData(type = diff.type , audioIcon = diff.audioIcon, audioName = diff.audioName, audioTime = diff.audioTime, audioSize = diff.audioSize, audioChoose = diff.audioChoose, audioUri = diff.audioUri))
                diffFiveMbSize += diff.audioSize
                audioCustomFiveMBDown.setAudioSizeAll(Formatter.formatFileSize(context, diffFiveMbSize))
                audioCustomFiveMBDown.setAudioCount(fiveMBListCategory.count().toString() + "개")

            } else if (500 <= (diff.audioSize / Actions.MB) && 1 > (diff.audioSize / Actions.GB)) {
                oneGBDownListCategory.add(AudioSizeData(type = diff.type , audioIcon = diff.audioIcon, audioName = diff.audioName, audioTime = diff.audioTime, audioSize = diff.audioSize, audioChoose = diff.audioChoose, audioUri = diff.audioUri))
                diffOneGbDownSize += diff.audioSize
                audioCustomOneGBDown.setAudioSizeAll(Formatter.formatFileSize(context, diffOneGbDownSize))
                audioCustomOneGBDown.setAudioCount(oneGBDownListCategory.count().toString() + "개")

            } else if (1 <= (diff.audioSize / Actions.GB) && 2 > (diff.audioSize / Actions.GB)) {
                oneGBListCategory.add(AudioSizeData(type = diff.type , audioIcon = diff.audioIcon, audioName = diff.audioName, audioTime = diff.audioTime, audioSize = diff.audioSize, audioChoose = diff.audioChoose, audioUri = diff.audioUri))
                diffOneGbSize += diff.audioSize
                audioCustomOneGB.setAudioSizeAll(Formatter.formatFileSize(context, diffOneGbSize))
                audioCustomOneGB.setAudioCount(oneGBListCategory.count().toString() + "개")

            } else if (2 <= (diff.audioSize / Actions.GB)) {
                twoGBListCategory.add(AudioSizeData(type = diff.type , audioIcon = diff.audioIcon, audioName = diff.audioName, audioTime = diff.audioTime, audioSize = diff.audioSize, audioChoose = diff.audioChoose, audioUri = diff.audioUri))
                diffTwoGbSize += diff.audioSize
                audioCustomTwoGB.setAudioSizeAll(Formatter.formatFileSize(context, diffTwoGbSize))
                audioCustomTwoGB.setAudioCount(twoGBListCategory.count().toString() + "개")
            }
        }
        //////////////////////////////////////////////////////////////////////////////////
        val recyclerOneMBDown = findViewById<RecyclerView>(R.id.audio_100MB_Down_recycler)
        recyclerOneMBDown.layoutManager = LinearLayoutManager(this)
        if (oneMBListCategory.isEmpty()) {
            audioCustomOneMBDown.visibility = View.GONE
            oneMBDownLine.visibility = View.GONE
        } else {
            recyclerOneMBDown.adapter = oneMBDownAdapter
        }

        val oneMBDownAllCheck = View.OnClickListener{
            boolean = if (boolean) {
                oneMBDownAdapter.setCheckAll(false, position)
                RepositoryImpl.getAudioList().clear()
                RepositoryImpl.getOneMB().clear()

                true
            } else {
                oneMBDownAdapter.setCheckAll(true, position)
                false
            }
        }

        audioCustomOneMBDown.setCheckListener(oneMBDownAllCheck)

        /////////////////////////////////////////////////////////////////////////////////
        val recyclerFiveMBDown = findViewById<RecyclerView>(R.id.audio_500MB_Down_recycler)
        recyclerFiveMBDown.layoutManager = LinearLayoutManager(this)

        if (fiveMBListCategory.isEmpty()) {
            audioCustomFiveMBDown.visibility = View.GONE
            fiveMBDownLine.visibility=View.GONE

        } else {
            recyclerFiveMBDown.adapter = fiveMBDownAdapter
        }

        val fiveMBDownAllCheck = View.OnClickListener{
            boolean = if (!boolean) {
                fiveMBDownAdapter.setCheckAll(false, position)
                RepositoryImpl.getAudioList().clear()
                RepositoryImpl.getFiveMB().clear()

                true
            } else {
                fiveMBDownAdapter.setCheckAll(true, position)
                false
            }
        }

        audioCustomFiveMBDown.setCheckListener(fiveMBDownAllCheck)
        //////////////////////////////////////////////////////////////////////////////////
        val recyclerOneGBDown = findViewById<RecyclerView>(R.id.audio_oneGB_Down_recycler)
        recyclerOneGBDown.layoutManager = LinearLayoutManager(this)
        if (oneGBDownListCategory.isEmpty()) {
            audioCustomOneGBDown.visibility = View.GONE
            oneGBDownLine.visibility=View.GONE

        } else {
            recyclerOneGBDown.adapter = oneGBDownAdapter
        }

        val oneGBDownAllCheck = View.OnClickListener{
            boolean = if (!boolean) {
                oneGBDownAdapter.setCheckAll(false, position)
                RepositoryImpl.getAudioList().clear()
                RepositoryImpl.getOneGBDown().clear()

                true
            } else {
                oneGBDownAdapter.setCheckAll(true, position)
                false
            }
        }

        audioCustomOneGBDown.setCheckListener(oneGBDownAllCheck)
        ///////////////////////////////////////////////////////////////////////////////////////
        val recyclerOneGB = findViewById<RecyclerView>(R.id.audio_oneGB_recycler)
        recyclerOneGB.layoutManager = LinearLayoutManager(this)
        if (oneGBListCategory.isEmpty()) {
            audioCustomOneGB.visibility = View.GONE
            oneGBLine.visibility=View.GONE

        } else {
            recyclerOneGB.adapter = oneGBAdapter
        }

        val oneGBAllCheck = View.OnClickListener{
            boolean = if (!boolean) {
                oneGBAdapter.setCheckAll(false, position)
                RepositoryImpl.getAudioList().clear()
                RepositoryImpl.getOneGB().clear()

                true
            } else {
                oneGBAdapter.setCheckAll(true, position)
                false
            }
        }

        audioCustomOneGB.setCheckListener(oneGBAllCheck)
        /////////////////////////////////////////////////////////////////////////////////
        val recyclerTwoGB = findViewById<RecyclerView>(R.id.audio_twoGB_recycler)
        recyclerTwoGB.layoutManager = LinearLayoutManager(this)
        if (twoGBListCategory.isEmpty()) {
            audioCustomTwoGB.visibility = View.GONE
            twoGBLine.visibility=View.GONE

        } else {
            recyclerTwoGB.adapter = twoGBAdapter
        }


        val twoGBAllCheck = View.OnClickListener{
            boolean = if (!boolean) {
                twoGBAdapter.setCheckAll(false, position)
                RepositoryImpl.getAudioList().clear()
                RepositoryImpl.getTwoGB().clear()

                true
            } else {
                twoGBAdapter.setCheckAll(true, position)
                false
            }
        }

        audioCustomTwoGB.setCheckListener(twoGBAllCheck)

    }


    var contentUri : Uri? = null
    private fun getAllAudioPath() {
        val externalStorage: Uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI

        val indexID: Int
        val nameID: Int


        var audioID: Long

            val cursor = contentResolver.query(
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                null,
                null,
                null,
                MediaStore.Audio.AudioColumns.DATE_TAKEN
            )
            val time = cursor?.getColumnIndexOrThrow(MediaStore.Audio.AudioColumns.DATE_TAKEN)
            val playtime = cursor?.getColumnIndexOrThrow(MediaStore.Audio.AudioColumns.DURATION)
        if (cursor != null) {

                indexID = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media._ID)
                nameID = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DISPLAY_NAME)

                while (cursor.moveToNext()) {
                    audioID = cursor.getLong(indexID)
                    val audioName = cursor.getString(nameID)
                    val play = cursor.getLong(playtime!!)
                    val uriAudio = Uri.withAppendedPath(externalStorage, "" + audioID)

                    val size = cursor.getColumnIndex(MediaStore.Audio.Media.SIZE)
                    audioSize = cursor.getLong(size)
                    val audioTime = cursor.getLong(time!!)

                    val formatPlayDate = SimpleDateFormat("hh:mm:ss")

                    val calendar = Calendar.getInstance()
                    val playCalendar = Calendar.getInstance()

                    calendar.timeInMillis = audioTime
                    playCalendar.timeInMillis = play

                    val count = arrayListOf(audioID).count()

                    val audioSizeList = arrayListOf(audioSize).sum()
                    audioAllSize += audioSizeList
                    audioAllCount += count
                    contentUri = Uri.withAppendedPath(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, audioID.toString())

                    audioListCategory.add(AudioSizeData(type = TYPE_AUDIO, audioIcon = R.drawable.icon_music, audioName = audioName, audioTime = formatPlayDate.format(play), audioSize = audioSize, audioChoose = false,  audioUri = uriAudio))


                }

        }


    }
    fun oneMB(){
        oneMBDownCheckboxList = RepositoryImpl.getOneMB()

        if(oneMBListCategory.isNotEmpty()){
            val checkItr = oneMBDownCheckboxList.iterator()

            while (checkItr.hasNext()) {
                val checkListItr = checkItr.next()
                val checkAudioUriItr = oneMBListCategory.iterator()
                while (checkAudioUriItr.hasNext()) {
                    val checkAudioUriListItr = checkAudioUriItr.next()
                    if (checkListItr.galleryName == checkAudioUriListItr.audioName) {
                        context.contentResolver?.openFileDescriptor(
                            checkAudioUriListItr.audioUri,
                            "w",
                            null
                        ).use {
                            checkAudioUriListItr.audioUri.let {
                                contentResolver.delete(
                                    checkAudioUriListItr.audioUri,
                                    null,
                                    null
                                )
                                Toast.makeText(this, "파일을 삭제 했습니다.", Toast.LENGTH_LONG).show()
                            }
                        }
                        checkAudioUriItr.remove()
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
                val checkAudioUriItr = fiveMBListCategory.iterator()
                while (checkAudioUriItr.hasNext()) {
                    val checkAudioUriListItr = checkAudioUriItr.next()
                    println("checkListItr. ${checkListItr.galleryName}, checkaudioUriListItr . ${checkAudioUriListItr.audioName}")

                    if (checkListItr.galleryName == checkAudioUriListItr.audioName) {
                        context.contentResolver?.openFileDescriptor(
                            checkAudioUriListItr.audioUri,
                            "w",
                            null
                        ).use {
                            checkAudioUriListItr.audioUri.let {
                                contentResolver.delete(
                                    checkAudioUriListItr.audioUri,
                                    null,
                                    null
                                )
                                Toast.makeText(this, "파일을 삭제 했습니다.", Toast.LENGTH_LONG).show()
                            }
                        }
                        checkAudioUriItr.remove()
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
                val checkAudioUriItr = oneGBDownListCategory.iterator()
                while (checkAudioUriItr.hasNext()) {
                    val checkAudioUriListItr = checkAudioUriItr.next()
                    if (checkListItr.galleryName == checkAudioUriListItr.audioName) {
                        context.contentResolver?.openFileDescriptor(
                            checkAudioUriListItr.audioUri,
                            "w",
                            null
                        ).use {
                            checkAudioUriListItr.audioUri.let {
                                contentResolver.delete(
                                    checkAudioUriListItr.audioUri,
                                    null,
                                    null
                                )
                                Toast.makeText(this, "파일을 삭제 했습니다.", Toast.LENGTH_LONG).show()
                            }
                        }
                        checkAudioUriItr.remove()
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
                val checkAudioUriItr = oneGBListCategory.iterator()
                while (checkAudioUriItr.hasNext()) {
                    val checkAudioUriListItr = checkAudioUriItr.next()
                    if (checkListItr.galleryName == checkAudioUriListItr.audioName) {
                        context.contentResolver?.openFileDescriptor(
                            checkAudioUriListItr.audioUri,
                            "w",
                            null
                        ).use {
                            checkAudioUriListItr.audioUri.let {
                                contentResolver.delete(checkAudioUriListItr.audioUri, null, null)
                                Toast.makeText(this, "파일을 삭제 했습니다.", Toast.LENGTH_LONG).show()
                            }
                        }
                        checkAudioUriItr.remove()
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
                val checkAudioUriItr = twoGBListCategory.iterator()
                while (checkAudioUriItr.hasNext()) {
                    val checkAudioUriListItr = checkAudioUriItr.next()
                    if (checkListItr.galleryName == checkAudioUriListItr.audioName) {
                        context.contentResolver?.openFileDescriptor(
                            checkAudioUriListItr.audioUri,
                            "w",
                            null
                        ).use {
                            checkAudioUriListItr.audioUri.let {
                                contentResolver.delete(
                                    checkAudioUriListItr.audioUri,
                                    null,
                                    null
                                )
                                Toast.makeText(this, "파일을 삭제 했습니다.", Toast.LENGTH_LONG).show()
                            }
                        }
                        checkAudioUriItr.remove()
                        twoGBAdapter.set(twoGBListCategory)

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