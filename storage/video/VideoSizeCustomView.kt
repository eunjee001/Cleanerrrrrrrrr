package com.kyoungss.cleaner.storage.video

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.*
import androidx.constraintlayout.widget.ConstraintLayout
import com.kyoungss.cleaner.R

class VideoSizeCustomView(context: Context, attrs:AttributeSet) : ConstraintLayout(context, attrs) {

    private  var videoSize : TextView
    private  var videoAllSize : TextView
    private  var videoCount : TextView
    private  var videoCheck : CheckBox
    private var menuBtn : ImageView
    var videoAllCheck : Boolean = false

     init {
         val view = View.inflate(context, R.layout.custom_video_size, this)
         videoSize = view.findViewById(R.id.video_size)
         videoAllSize = view.findViewById(R.id.video_size_all)
         videoCount = view.findViewById(R.id.video_size_count)
         videoCheck = view.findViewById(R.id.checkBtn)
         menuBtn = view.findViewById(R.id.video_size_menu)

         context.theme.obtainStyledAttributes(attrs, R.styleable.videoSetSize,0,0).apply {
            try {
                val typedArray = context.obtainStyledAttributes(attrs, R.styleable.videoSetSize)
                setVideoSize(typedArray.getString(R.styleable.videoSetSize_videoSizeCateTxt))
                setVideoSizeAll(typedArray.getString(R.styleable.videoSetSize_videoAllSizeTxt))
                setVideoCount(typedArray.getString(R.styleable.videoSetSize_videoSizeCountTxt))
                setVideoButton()
                setVideoMenu(typedArray.getResourceId(R.styleable.audioSetSize_audioSizeMenu, R.drawable.menu_button))
            }finally {
                recycle()
            }
        }
    }

    fun setVideoMenu(button:Int) {
        findViewById<ImageView>(R.id.video_size_menu).setImageResource(button)
        onRefresh()
    }

    private fun setVideoButton() {
        if (videoAllCheck){
            videoCheck.isChecked = false
            videoAllCheck = false
        }
        else{
            videoAllCheck= true
        }
    }

    fun setVideoSize(text:String?){
        videoSize.text=text
        onRefresh()
    }
    fun setVideoSizeAll(text:String?){
        videoAllSize.text=text
        onRefresh()
    }
    fun setVideoCount(text:String?){
        videoCount.text=text
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
        videoCheck.setOnClickListener(listener)
        onRefresh()

    }
}