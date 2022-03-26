package com.kyoungss.cleaner.check.clean.progress

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.*
import androidx.constraintlayout.widget.ConstraintLayout
import com.kyoungss.cleaner.R

class CleanCustomView(context: Context, attrs:AttributeSet) : ConstraintLayout(context, attrs) {

    private  var cacheTitle : TextView
    private  var cacheUseSize : TextView
    private  var cacheAllSize : TextView
    private  var cacheAllCount : TextView
    private  var cacheUseCount : TextView
    private  var cacheUseSlash : TextView
    private  var cacheCountSlash : TextView
    private  var cacheCheck : CheckBox
    private var menuBtn : ImageView
    var cacheAllCheck : Boolean = false
 //   private lateinit var cacheCheckList : List<CacheData>

    companion object {

    }

    init {
        val view = View.inflate(context, R.layout.custom_cache, this)

        cacheTitle = view.findViewById(R.id.cache_title)
        cacheUseSize = view.findViewById(R.id.cache_use_size)
        cacheAllSize = view.findViewById(R.id.cache_all_size)
        cacheAllCount = view.findViewById(R.id.cache_all_count)
        cacheUseCount = view.findViewById(R.id.cache_use_count)
        cacheUseSlash = view.findViewById(R.id.use_slash)
        cacheCountSlash = view.findViewById(R.id.count_slash)
        cacheCheck = view.findViewById(R.id.cache_checkBtn)

        menuBtn = view.findViewById(R.id.cache_menu)

        context.theme.obtainStyledAttributes(attrs, R.styleable.cacheSet,0,0).apply {
            try {
                val typedArray = context.obtainStyledAttributes(attrs, R.styleable.cacheSet)
                cacheTitle(typedArray.getString(R.styleable.cacheSet_cacheNameTxt))
                cacheUseSize(typedArray.getString(R.styleable.cacheSet_cacheUseTxt))
                cacheAllSize(typedArray.getString(R.styleable.cacheSet_cacheAllUseTxt))
                cacheUseCount(typedArray.getString(R.styleable.cacheSet_cacheUseCountTxt))
                cacheAllCount(typedArray.getString(R.styleable.cacheSet_cacheAllCountTxt))
                useSlash(typedArray.getString(R.styleable.cacheSet_cacheUseSlash))
                countSlash(typedArray.getString(R.styleable.cacheSet_cacheCountSlash))
                setCacheMenu(typedArray.getResourceId(R.styleable.cacheSet_cacheMenu, R.drawable.menu_button))
                cacheCheck(false)
                cacheCheckFalse(true)
                setCacheButton()

            }finally {
                recycle()
            }
        }
    }

    fun setCacheMenu(button : Int) {
        findViewById<ImageView>(R.id.cache_menu).setImageResource(button)

        onRefresh()
    }

    private fun setCacheButton() {
        if (cacheAllCheck){
            cacheCheck.isChecked = false
            cacheAllCheck = false
            onRefresh()
        }
        else{
            cacheAllCheck= true
            onRefresh()
        }
    }


    private fun cacheTitle(text:String?){
        cacheTitle.text=text
        onRefresh()
    }
    fun cacheUseSize(text:String?){
        cacheUseSize.text=text
        onRefresh()
    }
    fun cacheAllSize(text:String?){
        cacheAllSize.text=text
        onRefresh()
    }
    fun cacheUseCount(text:String?){
        cacheUseCount.text=text
        onRefresh()
    }
    fun cacheAllCount(text:String?){
        cacheAllCount.text=text
        onRefresh()
    }
    fun cacheCheck(check:Boolean){
        cacheCheck.isChecked = check
        onRefresh()
    }
    fun cacheCheckFalse(check:Boolean){

        cacheCheck.isChecked = false

        onRefresh()
    }
    private fun useSlash(text:String?){
        cacheUseSlash.text=text
        onRefresh()
    }
    private fun countSlash(text:String?){
        cacheCountSlash.text=text
        onRefresh()
    }

    fun onRefresh() {
        invalidate()
        requestLayout()
    }

    fun setListener(listener: OnClickListener) {
        menuBtn.setOnClickListener(listener)
        //menuBtn.dra
       // onRefresh()
    }
    fun setCheckListener(listener: OnClickListener) {
        cacheCheck.setOnClickListener(listener)
        onRefresh()

    }
}