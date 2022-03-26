package com.kyoungss.cleaner.storage.video

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.*
import androidx.constraintlayout.widget.ConstraintLayout
import com.kyoungss.cleaner.R

class VideoCustomView(context: Context, attrs:AttributeSet) : ConstraintLayout(context, attrs) {

    private  var videoDate : TextView
    private  var videoSize : TextView
    private  var videoCount : TextView
    private  var videoCheck : CheckBox
    private var menuBtn : ImageView
    var videoAllCheck : Boolean = false

    init {
        val view = View.inflate(context, R.layout.custom_video, this)
        videoDate = view.findViewById(R.id.video_date)
        videoSize = view.findViewById(R.id.video_date_size)
        videoCount = view.findViewById(R.id.video_date_count)
        videoCheck = view.findViewById(R.id.checkBtn)
        menuBtn = view.findViewById(R.id.video_date_menu)

        context.theme.obtainStyledAttributes(attrs, R.styleable.videoSet,0,0).apply {
            try {
                val typedArray = context.obtainStyledAttributes(attrs, R.styleable.videoSet)
                setVideoDate(typedArray.getString(R.styleable.videoSet_videoDateTxt))
                setVideoSize(typedArray.getString(R.styleable.videoSet_videoSizeTxt))
                setVideoCount(typedArray.getString(R.styleable.videoSet_videoCountTxt))
                setVideoButton()
                setVideoMenu(typedArray.getResourceId(R.styleable.videoSet_videoMenu, R.drawable.menu_button))
            }finally {
                recycle()
            }
        }
    }

    fun setVideoMenu(button: Int) {
        findViewById<ImageView>(R.id.video_date_menu).setImageResource(button)
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

    fun setVideoDate(text:String?){
        videoDate.text=text
        onRefresh()
    }
    fun setVideoSize(text:String?){
        videoSize.text=text
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