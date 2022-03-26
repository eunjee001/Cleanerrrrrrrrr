package com.kyoungss.cleaner.storage.image

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.*
import androidx.constraintlayout.widget.ConstraintLayout
import com.kyoungss.cleaner.R

class ImageCustomView(context: Context, attrs:AttributeSet) : ConstraintLayout(context, attrs) {

    private  var imageDate : TextView
    private  var imageSize : TextView
    private  var imageCount : TextView
    private  var imageCheck : CheckBox
    private var menuBtn : ImageView
    var imageAllCheck : Boolean = false

     init {
         val view = View.inflate(context, R.layout.custom_image, this)
         imageDate = view.findViewById(R.id.image_date)
         imageSize = view.findViewById(R.id.image_date_size)
         imageCount = view.findViewById(R.id.image_date_count)
         imageCheck = view.findViewById(R.id.checkBtn)
         menuBtn = view.findViewById(R.id.image_date_menu)

         context.theme.obtainStyledAttributes(attrs, R.styleable.imageSet,0,0).apply {
            try {
                val typedArray = context.obtainStyledAttributes(attrs, R.styleable.imageSet)
                setImageDate(typedArray.getString(R.styleable.imageSet_imageDateTxt))
                setImageSize(typedArray.getString(R.styleable.imageSet_imageSizeTxt))
                setImageCount(typedArray.getString(R.styleable.imageSet_imageCountTxt))
                setImageButton()
                setImageMenu(typedArray.getResourceId(R.styleable.imageSetSize_imageSizeMenu, R.drawable.menu_button))
            }finally {
                recycle()
            }
        }
    }

     fun setImageMenu(button:Int) {
        findViewById<ImageView>(R.id.image_date_menu).setImageResource(button)
        onRefresh()
    }

    private fun setImageButton() {
        if (imageAllCheck){
            imageCheck.isChecked = false
            imageAllCheck = false
        }
        else{
            imageAllCheck= true
        }
    }

    fun setImageDate(text:String?){
        imageDate.text=text
        onRefresh()
    }
    fun setImageSize(text:String?){
        imageSize.text=text
        onRefresh()
    }
    fun setImageCount(text:String?){
        imageCount.text=text
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
        imageCheck.setOnClickListener(listener)
        onRefresh()

    }
}