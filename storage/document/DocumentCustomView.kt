package com.kyoungss.cleaner.storage.document

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.*
import androidx.constraintlayout.widget.ConstraintLayout
import com.kyoungss.cleaner.R

class DocumentCustomView(context: Context, attrs:AttributeSet) : ConstraintLayout(context, attrs) {

    private  var documentDate : TextView
    private  var documentSize : TextView
    private  var documentCount : TextView
    private  var documentCheck : CheckBox
    private var menuBtn : ImageView
    var docAllCheck : Boolean = false

    init {
        val view = View.inflate(context, R.layout.custom_document, this)
        documentDate = view.findViewById(R.id.document_date)
        documentSize = view.findViewById(R.id.document_date_size)
        documentCount = view.findViewById(R.id.document_date_count)
        documentCheck = view.findViewById(R.id.checkBtn)
        menuBtn = view.findViewById(R.id.document_date_menu)

        context.theme.obtainStyledAttributes(attrs, R.styleable.documentSet,0,0).apply {
            try {
                val typedArray = context.obtainStyledAttributes(attrs, R.styleable.documentSet)
                setDocDate(typedArray.getString(R.styleable.documentSet_documentDateTxt))
                setDocSize(typedArray.getString(R.styleable.documentSet_documentSizeTxt))
                setDocCount(typedArray.getString(R.styleable.documentSet_documentCountTxt))
                setDocButton()
                setDocMenu(typedArray.getResourceId(R.styleable.audioSetSize_audioSizeMenu, R.drawable.menu_button))
            }finally {
                recycle()
            }
        }
    }

     fun setDocMenu(button : Int) {
        findViewById<ImageView>(R.id.document_date_menu).setImageResource(button)
        onRefresh()
    }

    private fun setDocButton() {
        if (docAllCheck){
            documentCheck.isChecked = false
            docAllCheck = false
        }
        else{
            docAllCheck= true
        }
    }

    fun setDocDate(text:String?){
        documentDate.text=text
        onRefresh()
    }
    fun setDocSize(text:String?){
        documentSize.text=text
        onRefresh()
    }
    fun setDocCount(text:String?){
        documentCount.text=text
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
        documentCheck.setOnClickListener(listener)
        onRefresh()

    }
}