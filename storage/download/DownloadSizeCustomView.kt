package com.kyoungss.cleaner.storage.download

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.*
import androidx.constraintlayout.widget.ConstraintLayout
import com.kyoungss.cleaner.R

class DownloadSizeCustomView(context: Context, attrs:AttributeSet) : ConstraintLayout(context, attrs) {

    private  var downloadSize : TextView
    private  var downloadAllSize : TextView
    private  var downloadCount : TextView
    private  var downloadCheck : CheckBox
    private var menuBtn : ImageView
    var downAllCheck : Boolean = false
     init {
         val view = View.inflate(context, R.layout.custom_download_size, this)
         downloadSize = view.findViewById(R.id.download_size)
         downloadAllSize = view.findViewById(R.id.download_size_all)
         downloadCount = view.findViewById(R.id.download_size_count)
         downloadCheck = view.findViewById(R.id.checkBtn)
         menuBtn = view.findViewById(R.id.download_size_menu)

         context.theme.obtainStyledAttributes(attrs, R.styleable.downloadSetSize,0,0).apply {
            try {
                val typedArray = context.obtainStyledAttributes(attrs, R.styleable.downloadSetSize)
                setDownSize(typedArray.getString(R.styleable.downloadSetSize_downloadSizeDateTxt))
                setDownSizeAll(typedArray.getString(R.styleable.downloadSetSize_downloadAllSizeTxt))
                setDownCount(typedArray.getString(R.styleable.downloadSetSize_downloadSizeCountTxt))
                setDownButton()
                setDownMenu(typedArray.getResourceId(R.styleable.audioSetSize_audioSizeMenu, R.drawable.menu_button))
            }finally {
                recycle()
            }
        }
    }

     fun setDownMenu(button : Int) {
        findViewById<ImageView>(R.id.download_size_menu).setImageResource(button)
        onRefresh()
    }

    private fun setDownButton() {
        if (downAllCheck){
            downloadCheck.isChecked = false
            downAllCheck = false
        }
        else{
            downAllCheck= true
        }
    }

    fun setDownSize(text:String?){
        downloadSize.text=text
        onRefresh()
    }
    fun setDownSizeAll(text:String?){
        downloadAllSize.text=text
        onRefresh()
    }
    fun setDownCount(text:String?){
        downloadCount.text=text
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
        downloadCheck.setOnClickListener(listener)
        onRefresh()

    }
}