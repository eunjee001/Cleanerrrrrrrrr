package com.kyoungss.cleaner.storage.document

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.Environment
import android.text.format.Formatter
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.LinearLayoutManager
import com.kyoungss.cleaner.DisplayAdjustment
import com.kyoungss.cleaner.R
import com.kyoungss.cleaner.data.RepositoryImpl
import com.kyoungss.cleaner.storage.folder.ManageFolderActivity
import java.io.File
import java.util.*

class DocumentActivity : Activity() {
    var context: Context = this
    private lateinit var dpAdjustment: DisplayAdjustment

    private var removeDoc: Button? = null
    private val docListCategory = ArrayList<DocData>()
    private var checkboxList = ArrayList<CheckDocData>()
    private lateinit var allSize: TextView
    private lateinit var allCount: TextView
    private val documentAdapter = DocumentAdapter(docListCategory, context)

    var allDocSize:Long = 0
    var allDocCount = 0
    var popupWindow : PopupWindow ?= null
   private lateinit var docMenu:Button
    var position = 0
    companion object {
        const val TYPE_DOC = 0L
        const val TYPE_DOC_PACKAGE = 0x01
        const val TYPE_DOC_TIME:Long = 0X02
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_document_all)
        val recyclerView = findViewById<RecyclerView>(R.id.recycler_document)

        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = documentAdapter


        documentFind()
        initViews()

    }
    override fun onDestroy() {
        super.onDestroy()
        if(null != popupWindow  && popupWindow!!.isShowing){
            popupWindow!!.dismiss()
        }
    }

    private fun initViews() {
        dpAdjustment = DisplayAdjustment.getInstance(this)

        val docBack = findViewById<Button>(R.id.docBack)
        dpAdjustment.setMargins(docBack,16,16,0,0 )
        dpAdjustment.setScale(docBack.layoutParams,24f,24f)

        val docTitle = findViewById<TextView>(R.id.docTitle)
        dpAdjustment.setMargins(docTitle, 12,12,0,0)

        allSize = findViewById(R.id.infoTitle)
        dpAdjustment.setMargins(allSize, 16,33,0,0)
        allSize.text = Formatter.formatFileSize(context, allDocSize)


        allCount = findViewById(R.id.infoTitleCount)
        dpAdjustment.setMargins(allCount, 14,34,0,0)
        allCount.text = allDocCount.toString() + "개"

        val docMenu = findViewById<Button>(R.id.menu)
        dpAdjustment.setMargins(docMenu, 0,31,19,0)
        dpAdjustment.setScale(docMenu.layoutParams, 24f,24f)

        val docRecyclerView: RecyclerView = findViewById(R.id.recycler_document)
        docRecyclerView.layoutManager = LinearLayoutManager(this)
        dpAdjustment.setMargins(docRecyclerView, 0,0,0,0)

        removeDoc = findViewById(R.id.remove_btn)
        dpAdjustment.setScale(removeDoc!!.layoutParams, 328f,48f)
        dpAdjustment.setMargins(removeDoc!!, 16,0,0,16)

        removeDoc?.setOnClickListener {
            selectDocRemove()
        }

        val view: View = layoutInflater.inflate(R.layout.popup_etc_layout, null)

        docMenu.setOnClickListener {
            popupWindow = PopupWindow(
                view,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                true
            )
            popupWindow!!.setBackgroundDrawable(ColorDrawable(Color.WHITE))

            popupWindow!!.showAsDropDown(docMenu, -205, -100)
        }

        val firstPopupTextView = view.findViewById<TextView>(R.id.popup_text_first)
        firstPopupTextView.setOnClickListener {
            val intent = Intent(this, ManageDocumentActivity::class.java)
            this.startActivity(intent)

            finish()
        }

        val secondsPopupTextView = view.findViewById<TextView>(R.id.popup_text_second)
        secondsPopupTextView.setOnClickListener {

            val intent = Intent(this, ManageDocumentSizeActivity::class.java)
            this.startActivity(intent)

            finish()
        }

        val thirdsPopupTextView = view.findViewById<TextView>(R.id.popup_text_third)
        thirdsPopupTextView.setOnClickListener {

            val intent = Intent(this, DocumentActivity::class.java)

            this.startActivity(intent)

            finish()
        }
    }


    private fun documentFind() {
        val externalStorage = Environment.getExternalStorageDirectory().absolutePath
        val path = "$externalStorage/Documents"
        val dir = File(path)
        val fileList = arrayOf(dir)

        for (files in fileList) {
            documentList(files.absolutePath)
        }
    }

    private fun documentList(path: String) {
        val file = File(path)
        val fileList = file.listFiles()?: return

        for (files in fileList) {

            val docSize: Long = arrayListOf(files.length()).sum()
            val docCount = arrayListOf(files).count()

            allDocSize += docSize
            allDocCount += docCount

            docListCategory.add(
                DocData(
                    type = TYPE_DOC,
                    documentIcon = R.drawable.icon_message,
                    documentName = files.name,
                    documentTime = TYPE_DOC_TIME,
                    documentSize = docSize,
                    documentChoose = false,
                    documentFile = files,
                    diffTime = 0

                )
            )
        }
        docListCategory.sortBy {
            it.documentName
        }

        documentAdapter.set(docListCategory)


    }

    private fun selectDocRemove() {

        checkboxList = RepositoryImpl.getDocList()
        if(docListCategory.isNotEmpty()){
            val checkItr = checkboxList.iterator()

            while (checkItr.hasNext()) {
                val checkListItr = checkItr.next()
                val checkDocItr = docListCategory.iterator()
                while (checkDocItr.hasNext()) {
                    val checkDocListItr = checkDocItr.next()
                    if (checkListItr.docName == checkDocListItr.documentName) {
                        checkDocListItr.documentFile.delete()
                        checkDocItr.remove()
                        documentAdapter.set(docListCategory)
                    }
                }
                checkItr.remove()
            }
        }

    }

    fun back(view: View) {
        val intent = Intent(this, ManageFolderActivity::class.java)
        this.startActivity(intent)

        finish()
    }
}



