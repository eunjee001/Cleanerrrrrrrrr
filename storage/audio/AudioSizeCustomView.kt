package com.kyoungss.cleaner.storage.audio

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.*
import androidx.constraintlayout.widget.ConstraintLayout
import com.kyoungss.cleaner.R

class AudioSizeCustomView(context: Context, attrs:AttributeSet) : ConstraintLayout(context, attrs) {

    private  var audioSize : TextView
    private  var audioAllSize : TextView
    private  var audioCount : TextView
    private  var audioCheck : CheckBox
    private var menuBtn : ImageView
    var audioAllCheck : Boolean = false
     init {
         val view = View.inflate(context, R.layout.custom_audio_size, this)
         audioSize = view.findViewById(R.id.audio_size)
         audioAllSize = view.findViewById(R.id.audio_size_all)
         audioCount = view.findViewById(R.id.audio_size_count)
         audioCheck = view.findViewById(R.id.checkBtn)
         menuBtn = view.findViewById(R.id.audio_size_menu)

         context.theme.obtainStyledAttributes(attrs, R.styleable.audioSetSize,0,0).apply {
            try {
                val typedArray = context.obtainStyledAttributes(attrs, R.styleable.audioSetSize)
                setAudioSize(typedArray.getString(R.styleable.audioSetSize_audioSizeCateTxt))
                setAudioSizeAll(typedArray.getString(R.styleable.audioSetSize_audioAllSizeTxt))
                setAudioCount(typedArray.getString(R.styleable.audioSetSize_audioSizeCountTxt))
                setAudioButton()
                setAudioMenu(typedArray.getResourceId(R.styleable.audioSetSize_audioSizeMenu, R.drawable.menu_button))
            }finally {
                recycle()
            }
        }
    }

    fun setAudioMenu(button : Int) {
        findViewById<ImageView>(R.id.audio_size_menu).setImageResource(button)
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

    fun setAudioSize(text:String?){
        audioSize.text=text
        onRefresh()
    }
    fun setAudioSizeAll(text:String?){
        audioAllSize.text=text
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