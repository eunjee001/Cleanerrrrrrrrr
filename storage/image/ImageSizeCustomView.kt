package com.kyoungss.cleaner.storage.image

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.*
import androidx.constraintlayout.widget.ConstraintLayout
import com.kyoungss.cleaner.R

class ImageSizeCustomView(context: Context, attrs:AttributeSet) : ConstraintLayout(context, attrs) {

    private  var imageSize : TextView
    private  var imageAllSize : TextView
    private  var imageCount : TextView
    private  var imageCheck : CheckBox
    private var menuBtn : ImageView
    var imageAllCheck : Boolean = false

     init {
         val view = View.inflate(context, R.layout.custom_image_size, this)
         imageSize = view.findViewById(R.id.image_size)
         imageAllSize = view.findViewById(R.id.image_size_all)
         imageCount = view.findViewById(R.id.image_size_count)
         imageCheck = view.findViewById(R.id.checkBtn)
         menuBtn = view.findViewById(R.id.image_size_menu)

         context.theme.obtainStyledAttributes(attrs, R.styleable.imageSetSize,0,0).apply {
            try {
                val typedArray = context.obtainStyledAttributes(attrs, R.styleable.imageSetSize)
                setImageSize(typedArray.getString(R.styleable.imageSetSize_imageSizeCateTxt))
                setImageSizeAll(typedArray.getString(R.styleable.imageSetSize_imageAllSizeTxt))
                setImageCount(typedArray.getString(R.styleable.imageSetSize_imageSizeCountTxt))
                setImageButton()
                setImageMenu(typedArray.getResourceId(R.styleable.imageSetSize_imageSizeMenu, R.drawable.menu_button))
            }finally {
                recycle()
            }
        }
    }

    fun setImageMenu(button : Int) {
        findViewById<ImageView>(R.id.image_size_menu).setImageResource(button)
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

    fun setImageSize(text:String?){
        imageSize.text=text
        onRefresh()
    }
    fun setImageSizeAll(text:String?){
        imageAllSize.text=text
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