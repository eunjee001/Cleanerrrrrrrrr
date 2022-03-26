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
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.LinearLayoutManager
import com.kyoungss.cleaner.DisplayAdjustment
import com.kyoungss.cleaner.R
import com.kyoungss.cleaner.data.RepositoryImpl
import com.kyoungss.cleaner.storage.folder.ManageFolderActivity
import java.text.SimpleDateFormat
import java.util.*

class AudioActivity : Activity() {
    var context: Context = this
    private lateinit var dpAdjustment: DisplayAdjustment

    private var removeaudio: Button? = null
    private val audioListCategory = ArrayList<AudioData>()
    private lateinit var  allSize : TextView
    private lateinit var  allCount : TextView
    private lateinit var audioMenu:Button
    private var checkboxList = ArrayList<CheckAudioData>()
    private val audioAdapter = AudioAdapter(audioListCategory, context)
    var popupWindow : PopupWindow ?= null
    var audioAllSize:Long =0
    var audioAllCount =0
    companion object {
        const val TYPE_AUDIO_NAME = 0L

    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_audio_all)


        val recyclerView = findViewById<RecyclerView>(R.id.recycler_audio)

        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = LinearLayoutManager(this)

        recyclerView.adapter = audioAdapter

        initViews()

        getAllAudioPath()
    }

    override fun onDestroy() {
        super.onDestroy()
        if(popupWindow != null && popupWindow!!.isShowing){
            popupWindow!!.dismiss()
        }
    }

var contentUri : Uri? = null
    private fun getAllAudioPath() {
        val externalStorage: Uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI

        val indexID: Int
        val nameID: Int
        var audioID: Long
        var count = 0

        val cursor = contentResolver.query(
            MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
            null,
            null,
            null,
            MediaStore.Audio.AudioColumns.DATE_MODIFIED
        )

        val time = cursor?.getColumnIndexOrThrow(MediaStore.Audio.AudioColumns.DATE_MODIFIED)
        val playtime = cursor?.getColumnIndexOrThrow(MediaStore.Audio.AudioColumns.DURATION)

        if (cursor != null) {
                indexID = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media._ID)
                nameID = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DISPLAY_NAME)

                while (cursor.moveToNext()) {
                    audioID = cursor.getLong(indexID)
                    val audioName = cursor.getString(nameID)
                    val play = cursor.getLong(playtime!!)

                    val size = cursor.getColumnIndex(MediaStore.Audio.Media.SIZE)
                    val audioSize = cursor.getLong(size)
                    val audioTime = cursor.getLong(time!!)
                    val uriAudio = Uri.withAppendedPath(externalStorage, "" + audioID)

                    val playDateFormat = SimpleDateFormat("hh:mm:ss")

                    val calendar = Calendar.getInstance()
                    val playCalendar = Calendar.getInstance()

                    calendar.timeInMillis = audioTime
                    playCalendar.timeInMillis = play

                    count = arrayListOf(audioID).count()
                    val audioSizeList = arrayListOf(audioSize).sum()
                    audioAllSize += audioSizeList
                    audioAllCount += count
                    contentUri = Uri.withAppendedPath(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, audioID.toString())

                    audioListCategory.add(
                        AudioData(
                            type =TYPE_AUDIO_NAME,
                            audioIcon = R.drawable.icon_music,
                            audioName = audioName,
                            audioTime = playDateFormat.format(play),
                            audioSize = audioSize,
                            audioChoose = false,
                            audioUri = uriAudio,
                            diffTime = 0 //수정
                        )
                    )
                    allSize.text = Formatter.formatFileSize(context, audioAllSize)
                    allCount.text = audioAllCount.toString() + "개"

                }


        }
        audioListCategory.sortBy{
            it.audioName
        }
        audioAdapter.set(audioListCategory)

    }


    private fun selectRemoveAudio(){
        checkboxList = RepositoryImpl.getAudioList()


    if (audioListCategory.isNotEmpty()){
        val checkItr = checkboxList.iterator()

        while (checkItr.hasNext()) {
            val checkListItr = checkItr.next()
            val checkAudioUriItr = audioListCategory.iterator()
            while (checkAudioUriItr.hasNext()) {
                val checkAudioUriListItr = checkAudioUriItr.next()
                if (checkListItr.audioName == checkAudioUriListItr.audioName) {
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
                    audioAdapter.set(audioListCategory)

                }
            }
            checkItr.remove()
        }
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

        audioMenu = findViewById<Button>(R.id.menu)
        dpAdjustment.setMargins(audioMenu, 0,31,19,0)
        dpAdjustment.setScale(audioMenu.layoutParams, 24f,24f)

        val audioRecyclerView: RecyclerView = findViewById(R.id.recycler_audio)
        audioRecyclerView.layoutManager = LinearLayoutManager(this)
        dpAdjustment.setMargins(audioRecyclerView, 0,0,0,0)

        removeaudio = findViewById(R.id.remove_btn)
        dpAdjustment.setScale(removeaudio!!.layoutParams, 328f,48f)
        dpAdjustment.setMargins(removeaudio!!, 16,0,0,16)

        removeaudio?.setOnClickListener{
            selectRemoveAudio()
        }

        val view: View = layoutInflater.inflate(R.layout.popup_etc_layout, null)

        audioMenu.setOnClickListener {
            popupWindow = PopupWindow(
                view,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                true
            )
            popupWindow!!.setBackgroundDrawable(ColorDrawable(Color.WHITE))
            popupWindow!!.showAsDropDown(audioMenu, -205, -100)

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
    }

    fun back(view: View) {
        val intent = Intent(this, ManageFolderActivity::class.java)
        this.startActivity(intent)

        finish()
    }

}



