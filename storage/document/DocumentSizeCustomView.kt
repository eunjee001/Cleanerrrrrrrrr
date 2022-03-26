package com.kyoungss.cleaner.storage.document

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.*
import androidx.constraintlayout.widget.ConstraintLayout
import com.kyoungss.cleaner.R

class DocumentSizeCustomView(context: Context, attrs:AttributeSet) : ConstraintLayout(context, attrs) {

    private  var documentSize : TextView
    private  var documentAllSize : TextView
    private  var documentCount : TextView
    private  var documentCheck : CheckBox
    private var menuBtn : ImageView
    var docAllCheck : Boolean = false

     init {
         val view = View.inflate(context, R.layout.custom_document_size, this)
         documentSize = view.findViewById(R.id.document_size)
         documentAllSize = view.findViewById(R.id.document_size_all)
         documentCount = view.findViewById(R.id.document_size_count)
         documentCheck = view.findViewById(R.id.checkBtn)
         menuBtn = view.findViewById(R.id.document_size_menu)

         context.theme.obtainStyledAttributes(attrs, R.styleable.documentSetSize,0,0).apply {
            try {
                val typedArray = context.obtainStyledAttributes(attrs, R.styleable.documentSetSize)
                setDocSize(typedArray.getString(R.styleable.documentSetSize_documentSizeDateTxt))
                setDocSizeAll(typedArray.getString(R.styleable.documentSetSize_documentAllSizeTxt))
                setDocCount(typedArray.getString(R.styleable.documentSetSize_documentSizeCountTxt))
                setDocButton()
                setDocMenu(typedArray.getResourceId(R.styleable.audioSetSize_audioSizeMenu, R.drawable.menu_button))
            }finally {
                recycle()
            }
        }
    }

    fun setDocMenu(button : Int) {
        findViewById<ImageView>(R.id.document_size_menu).setImageResource(button)
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

    fun setDocSize(text:String?){
        documentSize.text=text
        onRefresh()
    }
    fun setDocSizeAll(text:String?){
        documentAllSize.text=text
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