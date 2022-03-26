package com.kyoungss.cleaner.storage.document

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
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.kyoungss.cleaner.Actions
import com.kyoungss.cleaner.DisplayAdjustment
import com.kyoungss.cleaner.expand.Expandable
import com.kyoungss.cleaner.R
import com.kyoungss.cleaner.data.RepositoryImpl
import com.kyoungss.cleaner.storage.CheckStorageSecondData
import java.io.File
import kotlin.collections.ArrayList

class ManageDocumentSizeActivity : AppCompatActivity() {
    var context: Context = this
    private lateinit var dpAdjustment: DisplayAdjustment

    private val twoGBListCategory = ArrayList<DocData>()
    private val oneGBListCategory = ArrayList<DocData>()
    private val oneGBDownListCategory = ArrayList<DocData>()
    private val fiveMBListCategory = ArrayList<DocData>()
    private val oneMBListCategory = ArrayList<DocData>()

    private var removeDoc: Button? = null
    private val docListCategory = ArrayList<DocData>()

    private var documentAdapter = DocumentSizeAdapter(docListCategory, context)
    private var oneMBDownAdapter = DocumentSizeAdapter(oneMBListCategory, context)
    private var fiveMBDownAdapter = DocumentSizeAdapter(fiveMBListCategory, context)
    private var oneGBDownAdapter = DocumentSizeAdapter(oneGBDownListCategory, context)
    private var oneGBAdapter = DocumentSizeAdapter(oneGBListCategory, context)
    private var twoGBAdapter = DocumentSizeAdapter(twoGBListCategory, context)

    private var checkboxList = ArrayList<CheckDocData>()

    private var twoGBCheckboxList = ArrayList<CheckStorageSecondData>()
    private var oneGBCheckboxList = ArrayList<CheckStorageSecondData>()
    private var oneGBDownCheckboxList = ArrayList<CheckStorageSecondData>()
    private var fiveMBDownCheckboxList = ArrayList<CheckStorageSecondData>()
    private var oneMBDownCheckboxList = ArrayList<CheckStorageSecondData>()
    private var allInfo: TextView? = null
    private lateinit var allSize: TextView
    private lateinit var allCount: TextView
    private var allDocSize:Long =0
    private var allDocCount = 0
    var docSize: Long =0
    var popupWindow : PopupWindow ?= null

    val position = 0
    var selectBtn: CheckBox? = null

    companion object {
        const val TYPE_DOC = 0L
        const val TYPE_DOC_TIME: Long = 0x01
        const val TYPE_DOC_PACKAGE = 0x02

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_document_size)

        val menuBar = findViewById<Button>(R.id.menu)
        val view: View = layoutInflater.inflate(R.layout.popup_etc_layout, null)

        menuBar.setOnClickListener{
             popupWindow = PopupWindow(view, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true)
            popupWindow!!.showAsDropDown(menuBar, -205, -100)
            popupWindow!!.setBackgroundDrawable(ColorDrawable(Color.WHITE))

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
        documentFind()
        initViews()
    }

    override fun onDestroy() {
        super.onDestroy()
        if(popupWindow != null && popupWindow!!.isShowing){
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

        ///////////////////////twoGB////////////////////////
        val documentCustomTwoGB = findViewById<DocumentSizeCustomView>(R.id.document_twoGB)
        dpAdjustment.setMargins(documentCustomTwoGB, 0,0,0,0)
        dpAdjustment.setScale(documentCustomTwoGB.layoutParams, 360f,56f)

        val expandTwoGB = findViewById<Expandable>(R.id.document_twoGB_expand)

        val twoGBLine = findViewById<View>(R.id.line_twoGB)
        dpAdjustment.setMargins(twoGBLine, 0,0,0,0)
        dpAdjustment.setScale(twoGBLine.layoutParams, 360f,5f)

        //////////////////////////////////oneGB////////////////////////////
        val documentCustomOneGB = findViewById<DocumentSizeCustomView>(R.id.document_oneGB)
        dpAdjustment.setMargins(documentCustomOneGB, 0,0,0,0)
        dpAdjustment.setScale(documentCustomOneGB.layoutParams, 360f,56f)

        val expandOneGB = findViewById<Expandable>(R.id.document_oneGB_expand)

        val oneGBLine = findViewById<View>(R.id.line_oneGB)
        dpAdjustment.setMargins(oneGBLine, 0,0,0,0)
        dpAdjustment.setScale(oneGBLine.layoutParams, 360f,5f)

        /////////////////////////oneGBDown//////////////////////
        val documentCustomOneGBDown = findViewById<DocumentSizeCustomView>(R.id.document_oneGB_Down)
        dpAdjustment.setMargins(documentCustomOneGBDown, 0,0,0,0)
        dpAdjustment.setScale(documentCustomOneGBDown.layoutParams, 360f,56f)

        val expandOneGBDown = findViewById<Expandable>(R.id.document_oneGB_Down_expand)

        val oneGBDownLine = findViewById<View>(R.id.line_oneGB_Down)
        dpAdjustment.setMargins(oneGBDownLine, 0,0,0,0)
        dpAdjustment.setScale(oneGBDownLine.layoutParams, 360f,5f)
        ////////////////////////////500MB Down///////////////////////////
        val documentCustomFiveMBDown = findViewById<DocumentSizeCustomView>(R.id.document_500MB_Down)
        dpAdjustment.setMargins(documentCustomFiveMBDown, 0,0,0,0)
        dpAdjustment.setScale(documentCustomFiveMBDown.layoutParams, 360f,56f)

        val expandFiveMBDown = findViewById<Expandable>(R.id.document_500MB_Down_expand)

        val fiveMBDownLine = findViewById<View>(R.id.line_500MB_Down)
        dpAdjustment.setMargins(fiveMBDownLine, 0,0,0,0)
        dpAdjustment.setScale(fiveMBDownLine.layoutParams, 360f,5f)

        //////////////////////////100MB Down//////////////////////////////
        val documentCustomOneMBDown = findViewById<DocumentSizeCustomView>(R.id.document_100MB_Down)
        dpAdjustment.setMargins(documentCustomOneMBDown, 0,0,0,0)
        dpAdjustment.setScale(documentCustomOneMBDown.layoutParams, 360f,56f)

        val expandOneMBDown = findViewById<Expandable>(R.id.document_100MB_Down_expand)
        val oneMBDownLine = findViewById<View>(R.id.line_100MB_Down)
        dpAdjustment.setMargins(oneMBDownLine, 0,0,0,0)
        dpAdjustment.setScale(oneMBDownLine.layoutParams, 360f,5f)

        ////////////////////////////////////////////////////////////////////////////////

        removeDoc = findViewById(R.id.document_size_btn)
        dpAdjustment.setScale(removeDoc!!.layoutParams, 328f,48f)
        dpAdjustment.setMargins(removeDoc!!, 16,0,0,16)

        removeDoc?.setOnClickListener {
            selectDocRemove()
        }

        selectBtn = findViewById(R.id.document_check)

        var boolean = false

        val listenerTwoGB = View.OnClickListener {
            boolean = if (!boolean) {
                expandTwoGB.expand()
                documentCustomTwoGB.setDocMenu(R.drawable.icon_menu_drop_up)

                true
            } else {
                expandTwoGB.collapse()
                documentCustomTwoGB.setDocMenu(R.drawable.icon_menu_drop_down)

                false
            }
        }

        documentCustomTwoGB.setListener(listenerTwoGB)


        /////////////////////////////////////////////////////////////
        /////////////////////////////////////////////////////////////
        /////////////////////////////////////////////////////////////

        val listenerOneGB = View.OnClickListener {
            boolean = if (!boolean) {
                expandOneGB.expand()
                documentCustomOneGB.setDocMenu(R.drawable.icon_menu_drop_up)

                true
            } else {
                expandOneGB.collapse()
                documentCustomOneGB.setDocMenu(R.drawable.icon_menu_drop_down)

                false
            }
        }
        documentCustomOneGB.setListener(listenerOneGB)

        /////////////////////////////////////////////////////////////
        /////////////////////////////////////////////////////////////
        /////////////////////////////////////////////////////////////

        val listenerOneGBDown = View.OnClickListener {
            boolean = if (!boolean) {
                expandOneGBDown.expand()
                documentCustomOneGBDown.setDocMenu(R.drawable.icon_menu_drop_up)

                true
            } else {
                expandOneGBDown.collapse()
                documentCustomOneGBDown.setDocMenu(R.drawable.icon_menu_drop_down)

                false
            }
        }
        documentCustomOneGBDown.setListener(listenerOneGBDown)


        /////////////////////////////////////////////////////////////
        /////////////////////////////////////////////////////////////
        /////////////////////////////////////////////////////////////

        val listenerFiveMBDown = View.OnClickListener {
            boolean = if (!boolean) {
                expandFiveMBDown.expand()
                documentCustomFiveMBDown.setDocMenu(R.drawable.icon_menu_drop_up)

                true
            } else {
                expandFiveMBDown.collapse()
                documentCustomFiveMBDown.setDocMenu(R.drawable.icon_menu_drop_down)

                false
            }
        }
        documentCustomFiveMBDown.setListener(listenerFiveMBDown)

        /////////////////////////////////////////////////////////////
        /////////////////////////////////////////////////////////////
        /////////////////////////////////////////////////////////////

        val listenerOneMBDown = View.OnClickListener {
            boolean = if (!boolean) {
                expandOneMBDown.expand()
                documentCustomOneMBDown.setDocMenu(R.drawable.icon_menu_drop_up)

                true
            } else {
                expandOneMBDown.collapse()
                documentCustomOneMBDown.setDocMenu(R.drawable.icon_menu_drop_down)

                false
            }
        }
        documentCustomOneMBDown.setListener(listenerOneMBDown)
        var diffOneMbSize :Long =0
        var diffFiveMbSize :Long =0
        var diffOneGbDownSize :Long =0
        var diffOneGbSize :Long =0
        var diffTwoGbSize :Long =0
        for (diff in docListCategory) {
            if (0 < diff.documentSize && (diff.documentSize / Actions.MB) < 100) {
                oneMBListCategory.add(DocData(type = diff.type , documentIcon = diff.documentIcon, documentName = diff.documentName, documentTime = diff.documentTime, documentSize = diff.documentSize, documentChoose = diff.documentChoose, documentFile = diff.documentFile, diffTime = diff.diffTime ))
                diffOneMbSize += diff.documentSize
                documentCustomOneMBDown.setDocSizeAll(Formatter.formatFileSize(context, diffOneMbSize))
                documentCustomOneMBDown.setDocCount(oneMBListCategory.count().toString() + "개")
            } else if (100 <= (diff.documentSize / Actions.MB) && (diff.documentSize / Actions.MB) < 500) {
                fiveMBListCategory.add(DocData(type = diff.type , documentIcon = diff.documentIcon, documentName = diff.documentName, documentTime = diff.documentTime, documentSize = diff.documentSize, documentChoose = diff.documentChoose, documentFile = diff.documentFile, diffTime = diff.diffTime ))
                diffFiveMbSize += diff.documentSize
                documentCustomFiveMBDown.setDocSizeAll(Formatter.formatFileSize(context, diffFiveMbSize))
                documentCustomFiveMBDown.setDocCount(fiveMBListCategory.count().toString() + "개")
            } else if (500 <= (diff.documentSize / Actions.MB) && (diff.documentSize / Actions.GB) < 1) {
                oneGBDownListCategory.add(DocData(type = diff.type , documentIcon = diff.documentIcon, documentName = diff.documentName, documentTime = diff.documentTime, documentSize = diff.documentSize, documentChoose = diff.documentChoose, documentFile = diff.documentFile, diffTime = diff.diffTime ))
                diffOneGbDownSize += diff.documentSize
                documentCustomOneGBDown.setDocSizeAll(Formatter.formatFileSize(context, diffOneGbDownSize))
                documentCustomOneGBDown.setDocCount(oneGBDownListCategory.count().toString() + "개")
            } else if (1 <= (diff.documentSize / Actions.GB) && (diff.documentSize / Actions.GB) < 2) {
                oneGBListCategory.add(DocData(type = diff.type , documentIcon = diff.documentIcon, documentName = diff.documentName, documentTime = diff.documentTime, documentSize = diff.documentSize, documentChoose = diff.documentChoose, documentFile = diff.documentFile, diffTime = diff.diffTime ))
                diffOneGbSize += diff.documentSize
                documentCustomOneGB.setDocSizeAll(Formatter.formatFileSize(context, diffOneGbSize))
                documentCustomOneGB.setDocCount(oneGBListCategory.count().toString() + "개")
            } else if (2 <= (diff.documentSize / Actions.GB)) {
                twoGBListCategory.add(DocData(type = diff.type , documentIcon = diff.documentIcon, documentName = diff.documentName, documentTime = diff.documentTime, documentSize = diff.documentSize, documentChoose = diff.documentChoose, documentFile = diff.documentFile, diffTime = diff.diffTime ))
                diffTwoGbSize += diff.documentSize
                documentCustomTwoGB.setDocSizeAll(Formatter.formatFileSize(context, diffTwoGbSize))
                documentCustomTwoGB.setDocCount(twoGBListCategory.count().toString() + "개")
            }
        }
        ////////////////////////////////////////////////////////////////////////////
        val recyclerOneMBDown = findViewById<RecyclerView>(R.id.document_100MB_Down_recycler)
        recyclerOneMBDown.layoutManager = LinearLayoutManager(this)
        oneMBDownAdapter = DocumentSizeAdapter(oneMBListCategory, context)
        if (oneMBListCategory.isEmpty()) {
            documentCustomOneMBDown.visibility = View.GONE
            oneMBDownLine.visibility = View.GONE

        } else {
            recyclerOneMBDown.adapter = oneMBDownAdapter
        }

        val oneMBDownAllCheck = View.OnClickListener{
            boolean = if (!boolean) {
                oneMBDownAdapter.setCheckAll(false, position)
                RepositoryImpl.getDocList().clear()
                true
            } else {
                oneMBDownAdapter.setCheckAll(true, position)
                false
            }
        }

        documentCustomOneMBDown.setCheckListener(oneMBDownAllCheck)
        ///////////////////////////////////////////////////////////////////////////////
        val recyclerFiveMBDown = findViewById<RecyclerView>(R.id.document_500MB_Down_recycler)
        recyclerFiveMBDown.layoutManager = LinearLayoutManager(this)

        fiveMBDownAdapter = DocumentSizeAdapter(fiveMBListCategory, context)
        if (fiveMBListCategory.isEmpty()) {
            documentCustomFiveMBDown.visibility = View.GONE
            fiveMBDownLine.visibility=View.GONE

        } else {
            recyclerFiveMBDown.adapter = fiveMBDownAdapter
        }

        val fiveMBDownAllCheck = View.OnClickListener{
            boolean = if (!boolean) {
                fiveMBDownAdapter.setCheckAll(false, position)
                RepositoryImpl.getDocList().clear()
                true
            } else {
                fiveMBDownAdapter.setCheckAll(true, position)
                false
            }
        }

        documentCustomFiveMBDown.setCheckListener(fiveMBDownAllCheck)

        ////////////////////////////////////////////////////////////////////////////
        val recyclerOneGBDown = findViewById<RecyclerView>(R.id.document_oneGB_Down_recycler)
        recyclerOneGBDown.layoutManager = LinearLayoutManager(this)
        oneGBDownAdapter = DocumentSizeAdapter(oneGBDownListCategory, context)
        if (oneGBDownListCategory.isEmpty()) {
            documentCustomOneGBDown.visibility = View.GONE
            oneGBDownLine.visibility=View.GONE

        } else {
            recyclerOneGBDown.adapter = oneGBDownAdapter
        }

        val oneGBDownAllCheck = View.OnClickListener{
            boolean = if (!boolean) {
                oneGBDownAdapter.setCheckAll(false, position)
                RepositoryImpl.getDocList().clear()
                true
            } else {
                oneGBDownAdapter.setCheckAll(true, position)
                false
            }
        }

        documentCustomOneGBDown.setCheckListener(oneGBDownAllCheck)
        ///////////////////////////////////////////////////////////////////////////
        val recyclerOneGB = findViewById<RecyclerView>(R.id.document_oneGB_recycler)
        recyclerOneGB.layoutManager = LinearLayoutManager(this)
        oneGBAdapter = DocumentSizeAdapter(oneGBListCategory, context)
        if (oneGBListCategory.isEmpty()) {
            documentCustomOneGB.visibility = View.GONE
            oneGBLine.visibility=View.GONE

        } else {
            recyclerOneGB.adapter = oneGBAdapter
        }

        val oneGBAllCheck = View.OnClickListener{
            boolean = if (!boolean) {
                oneGBAdapter.setCheckAll(false, position)
                RepositoryImpl.getDocList().clear()
                true
            } else {
                oneGBAdapter.setCheckAll(true, position)
                false
            }
        }

        documentCustomOneGB.setCheckListener(oneGBAllCheck)
        ///////////////////////////////////////////////////////////////////////
        val recyclerTwoGB = findViewById<RecyclerView>(R.id.document_twoGB_recycler)
        recyclerTwoGB.layoutManager = LinearLayoutManager(this)
        twoGBAdapter = DocumentSizeAdapter(twoGBListCategory, context)
        if (twoGBListCategory.isEmpty()) {
            documentCustomTwoGB.visibility = View.GONE
            twoGBLine.visibility=View.GONE

        } else {
            recyclerTwoGB.adapter = twoGBAdapter
        }

        val twoGBAllCheck = View.OnClickListener{
            boolean = if (!boolean) {
                twoGBAdapter.setCheckAll(false, position)
                RepositoryImpl.getDocList().clear()
                true
            } else {
                twoGBAdapter.setCheckAll(true, position)
                false
            }
        }

        documentCustomTwoGB.setCheckListener(twoGBAllCheck)
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

            docSize= arrayListOf(files.length()).sum()
            val docCount = arrayListOf(files).count()

            allDocSize += docSize
            allDocCount += docCount

            docListCategory.add(DocData(type = TYPE_DOC, documentIcon = R.drawable.icon_message, documentName = files.name, documentTime = TYPE_DOC_TIME, documentSize = docSize, documentChoose = false, documentFile = files, diffTime =  0))

        }


    }
    fun oneMB(){
        oneMBDownCheckboxList = RepositoryImpl.getOneMBFile()

        if(oneMBListCategory.isNotEmpty()){
            val checkItr = oneMBDownCheckboxList.iterator()

            while (checkItr.hasNext()) {
                val checkListItr = checkItr.next()
                val checkDocItr = oneMBListCategory.iterator()
                while (checkDocItr.hasNext()) {
                    val checkDocListItr = checkDocItr.next()
                    if (checkListItr.fileName == checkDocListItr.documentName) {
                        checkDocListItr.documentFile.delete()
                        checkDocItr.remove()
                        oneMBDownAdapter.set(oneMBListCategory)
                    }
                }
                checkItr.remove()
            }

        }
    }
    fun fiveMB(){
        fiveMBDownCheckboxList = RepositoryImpl.getFiveMBFile()
        if(fiveMBListCategory.isNotEmpty()){
            val checkItr = fiveMBDownCheckboxList.iterator()

            while (checkItr.hasNext()) {
                val checkListItr = checkItr.next()
                val checkDocItr = fiveMBListCategory.iterator()
                while (checkDocItr.hasNext()) {
                    val checkDocListItr = checkDocItr.next()
                    if (checkListItr.fileName == checkDocListItr.documentName) {
                        checkDocListItr.documentFile.delete()
                        checkDocItr.remove()
                        fiveMBDownAdapter.set(fiveMBListCategory)

                    }
                }
                checkItr.remove()


            }
        }
    }
    fun oneGBDown(){
        oneGBDownCheckboxList = RepositoryImpl.getOneGBDownFile()

        if(oneGBDownListCategory.isNotEmpty()){
            val checkItr = oneGBDownCheckboxList.iterator()

            while (checkItr.hasNext()) {
                val checkListItr = checkItr.next()
                val checkDocItr = oneGBDownListCategory.iterator()
                while (checkDocItr.hasNext()) {
                    val checkDocListItr = checkDocItr.next()
                    if (checkListItr.fileName == checkDocListItr.documentName) {
                        checkDocListItr.documentFile.delete()
                        checkDocItr.remove()
                        oneGBDownAdapter.set(oneGBDownListCategory)
                    }
                }
                checkItr.remove()
            }
        }

    }

    fun oneGB(){
        oneGBCheckboxList = RepositoryImpl.getOneGBFile()
        if(oneGBListCategory.isNotEmpty()){
            val checkItr = oneGBCheckboxList.iterator()

            while (checkItr.hasNext()) {
                val checkListItr = checkItr.next()
                val checkDocItr = oneGBListCategory.iterator()
                while (checkDocItr.hasNext()) {
                    val checkDocListItr = checkDocItr.next()
                    if (checkListItr.fileName == checkDocListItr.documentName) {
                        checkDocListItr.documentFile.delete()
                        checkDocItr.remove()
                        oneGBAdapter.set(oneGBListCategory)

                    }
                }
                checkItr.remove()
            }
        }

    }
    fun twoGB() {
        twoGBCheckboxList = RepositoryImpl.getTwoGBFile()

        if (twoGBListCategory.isNotEmpty()) {
            val checkItr = twoGBCheckboxList.iterator()
            while (checkItr.hasNext()) {
                val checkListItr = checkItr.next()
                val checkDocItr = twoGBListCategory.iterator()
                while (checkDocItr.hasNext()) {
                    val checkDocListItr = checkDocItr.next()
                    if (checkListItr.fileName == checkDocListItr.documentName) {
                        checkDocListItr.documentFile.delete()
                        checkDocItr.remove()
                        twoGBAdapter.set(twoGBListCategory)

                    }
                }
                checkItr.remove()
            }
        }
    }

    private fun selectDocRemove(){
        finish()
        overridePendingTransition(0,0)
        val intent :Intent= intent
        startActivity(intent)
        overridePendingTransition(0,0)
        checkboxList = RepositoryImpl.getDocList()
        oneMB()
        fiveMB()
        oneGBDown()
        oneGB()
        twoGB()
    }


    fun back(view: View) {

        finish()
    }


}