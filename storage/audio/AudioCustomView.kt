package com.kyoungss.cleaner.storage.audio

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.*
import androidx.constraintlayout.widget.ConstraintLayout
import com.kyoungss.cleaner.R

class AudioCustomView(context: Context, attrs:AttributeSet) : ConstraintLayout(context, attrs) {

    private  var audioDate : TextView
    private  var audioSize : TextView
    private  var audioCount : TextView
    private  var audioCheck : CheckBox
    private var menuBtn : ImageView
    var audioAllCheck : Boolean = false

    init {
        val view = View.inflate(context, R.layout.custom_audio, this)
        audioDate = view.findViewById(R.id.audio_date)
        audioSize = view.findViewById(R.id.audio_date_size)
        audioCount = view.findViewById(R.id.audio_date_count)
        audioCheck = view.findViewById(R.id.checkBtn)
        menuBtn = view.findViewById(R.id.audio_date_menu)

        context.theme.obtainStyledAttributes(attrs, R.styleable.audioSet,0,0).apply {
            try {
                val typedArray = context.obtainStyledAttributes(attrs, R.styleable.audioSet)
                setAudioDate(typedArray.getString(R.styleable.audioSet_audioDateTxt))
                setAudioSize(typedArray.getString(R.styleable.audioSet_audioSizeTxt))
                setAudioCount(typedArray.getString(R.styleable.audioSet_audioCountTxt))
                setAudioButton()
                setAudioMenu(typedArray.getResourceId(R.styleable.audioSet_audioMenu, R.drawable.menu_button))
            }finally {
                recycle()
            }
        }
    }

    fun setAudioMenu(button : Int) {
        findViewById<ImageView>(R.id.audio_date_menu).setImageResource(button)
        onRefresh()
    }

    private fun setAudioButton() {
        if (audioAllCheck){
            audioCheck.isChecked = false
            audioAllCheck = false
        }
        else{
            audioAllCheck= true
        }
    }

    fun setAudioDate(text:String?){
        audioDate.text=text
        onRefresh()
    }
    fun setAudioSize(text:String?){
        audioSize.text=text
        onRefresh()
    }
    fun setAudioCount(text:String?){
        audioCount.text=text
        onRefresh()
    }



    private fun onRefresh() {
        invalidate()
        requestLayout()
    }

    fun setListener(listener: OnClickListener) {
        menuBtn.setOnClickListener(listener)
    }
    fun setCheckListener(listener: OnClickListener) {
        audioCheck.setOnClickListener(listener)
        onRefresh()

    }
}