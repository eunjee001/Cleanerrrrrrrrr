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

class ManageDocumentActivity : AppCompatActivity() {
    var context: Context = this
    private lateinit var dpAdjustment: DisplayAdjustment

    private val twoYearListCategory = ArrayList<DocData>()
    private val yearListCategory = ArrayList<DocData>()
    private val yearDownListCategory = ArrayList<DocData>()
    private val sixMonthListCategory = ArrayList<DocData>()
    private val threeMonthListCategory = ArrayList<DocData>()
    private val monthListCategory = ArrayList<DocData>()
    private val yesterdayListCategory = ArrayList<DocData>()
    private var todayListCategory = ArrayList<DocData>()
    private lateinit var  removeDoc:Button
    private val docListCategory = ArrayList<DocData>()

    private var documentAdapter = DocumentAdapter(docListCategory, context)
    private var monthAdapter = DocumentAdapter(monthListCategory, context)
    private var todayAdapter = DocumentAdapter(todayListCategory, context)
    private var yesterdayAdapter = DocumentAdapter(yesterdayListCategory, context)
    private var threeMonthAdapter = DocumentAdapter(threeMonthListCategory, context)
    private var sixMonthAdapter = DocumentAdapter(sixMonthListCategory, context)
    private var yearDownAdapter = DocumentAdapter(yearDownListCategory, context)
    private var yearAdapter = DocumentAdapter(yearListCategory, context)
    private var twoYearAdapter = DocumentAdapter(twoYearListCategory, context)

    private var checkboxList =ArrayList<CheckDocData>()
    private var todayCheckboxList =ArrayList<CheckStorageSecondData>()
    private var yesterdayCheckboxList =ArrayList<CheckStorageSecondData>()
    private var monthCheckboxList =ArrayList<CheckStorageSecondData>()
    private var threeMonthCheckboxList =ArrayList<CheckStorageSecondData>()
    private var sixMonthCheckboxList =ArrayList<CheckStorageSecondData>()
    private var yearDownCheckboxList =ArrayList<CheckStorageSecondData>()
    private var yearCheckboxList =ArrayList<CheckStorageSecondData>()
    private var twoYearCheckboxList =ArrayList<CheckStorageSecondData>()

    // private val data : ArrayList<DocData>()
    var docAllSize:Long = 0
    var docAllCount = 0
    var diffTime: Long = 0

    var position = 0
    private lateinit var allSize: TextView
    private lateinit var allCount: TextView
    var selectBtn:CheckBox ?= null
    var popupWindow : PopupWindow ?= null

    companion object {
        const val TYPE_DOCUMENT = 0L
        const val TYPE_DOCUMENT_PACKAGE = 0x01
        const val TYPE_DOCUMENT_TIME = 0x02

    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_document)


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
        allSize.text = Formatter.formatFileSize(context, docAllSize)


        allCount = findViewById(R.id.infoTitleCount)
        dpAdjustment.setMargins(allCount, 14,34,0,0)
        allCount.text = docAllCount.toString() + "개"


        val docMenu = findViewById<Button>(R.id.menu)
        dpAdjustment.setMargins(docMenu, 0,31,19,0)
        dpAdjustment.setScale(docMenu.layoutParams, 24f,24f)

        //////////////twoYear//////////////
        val twoYearLayout = findViewById<RelativeLayout>(R.id.relative_layout_twoYear)

        val twoYearLine = findViewById<View>(R.id.line_twoYear)
        dpAdjustment.setMargins(twoYearLine, 0,0,0,0)
        dpAdjustment.setScale(twoYearLine.layoutParams, 360f,5f)

        val documentCustomTwoYear = findViewById<DocumentCustomView>(R.id.document_twoYear)
        dpAdjustment.setMargins(documentCustomTwoYear, 0,10,0,0)
        dpAdjustment.setScale(documentCustomTwoYear.layoutParams, 360f,56f)

        val expandTwoYear = findViewById<Expandable>(R.id.document_twoYear_expand)


        //////////////year///////////////////////
        val yearLayout = findViewById<RelativeLayout>(R.id.relative_layout_year)
        val yearLine = findViewById<View>(R.id.line_Year)
        dpAdjustment.setMargins(yearLine, 0,0,0,0)
        dpAdjustment.setScale(yearLine.layoutParams, 360f,5f)

        val documentCustomYear = findViewById<DocumentCustomView>(R.id.document_Year)
        dpAdjustment.setMargins(documentCustomYear, 0,0,0,0)
        dpAdjustment.setScale(documentCustomYear.layoutParams, 360f,56f)

        val expandYear = findViewById<Expandable>(R.id.document_Year_expand)

        /////////////////yearDown///////////////////////
        val yearDownLayout = findViewById<RelativeLayout>(R.id.relative_layout_yearDown)
        val yearDownLine = findViewById<View>(R.id.line_YearDown)
        dpAdjustment.setMargins(yearDownLine, 0,0,0,0)
        dpAdjustment.setScale(yearDownLine.layoutParams, 360f,5f)

        val documentCustomYearDown = findViewById<DocumentCustomView>(R.id.document_YearDown)
        dpAdjustment.setMargins(documentCustomYearDown, 0,0,0,0)
        dpAdjustment.setScale(documentCustomYearDown.layoutParams, 360f,56f)

        val expandYearDown = findViewById<Expandable>(R.id.document_YearDown_expand)

        ///////////////////////////////sixMonth////////////////////
        val sixMonthLayout = findViewById<RelativeLayout>(R.id.relative_layout_sixMonth)
        val sixMonthLine = findViewById<View>(R.id.line_sixMonth)
        dpAdjustment.setMargins(sixMonthLine, 0,0,0,0)
        dpAdjustment.setScale(sixMonthLine.layoutParams, 360f,5f)

        val documentCustomSixMonth = findViewById<DocumentCustomView>(R.id.document_sixMonth)
        dpAdjustment.setMargins(documentCustomSixMonth, 0,0,0,0)
        dpAdjustment.setScale(documentCustomSixMonth.layoutParams, 360f,56f)

        val expandSixMonth = findViewById<Expandable>(R.id.document_sixMonth_expand)

        ////////////////////////////threeMonth///////////////////////
        val threeMonthLayout = findViewById<RelativeLayout>(R.id.relative_layout_threeMonth)
        val threeMonthLine = findViewById<View>(R.id.line_threeMonth)
        dpAdjustment.setMargins(threeMonthLine, 0,0,0,0)
        dpAdjustment.setScale(threeMonthLine.layoutParams, 360f,5f)

        val documentCustomThreeMonth = findViewById<DocumentCustomView>(R.id.document_threeMonth)
        dpAdjustment.setMargins(documentCustomThreeMonth, 0,0,0,0)
        dpAdjustment.setScale(documentCustomThreeMonth.layoutParams, 360f,56f)

        val expandThreeMonth = findViewById<Expandable>(R.id.document_threeMonth_expand)

        ///////////////////////////month //////////////////////////
        val monthLayout = findViewById<RelativeLayout>(R.id.relative_layout_Month)
        val monthLine = findViewById<View>(R.id.line_Month)
        dpAdjustment.setMargins(monthLine, 0,0,0,0)
        dpAdjustment.setScale(monthLine.layoutParams, 360f,5f)

        val documentCustomMonth = findViewById<DocumentCustomView>(R.id.document_Month)
        dpAdjustment.setMargins(documentCustomMonth, 0,0,0,0)
        dpAdjustment.setScale(documentCustomMonth.layoutParams, 360f,56f)

        val expandMonth = findViewById<Expandable>(R.id.document_Month_expand)

        /////////////////////yesterday///////////////////
        val yesterdayLayout = findViewById<RelativeLayout>(R.id.relative_layout_Yesterday)
        val yesterdayLine = findViewById<View>(R.id.line_Yesterday)
        dpAdjustment.setMargins(yesterdayLine, 0,0,0,0)
        dpAdjustment.setScale(yesterdayLine.layoutParams,  360f,5f)

        val documentCustomYesterday = findViewById<DocumentCustomView>(R.id.document_Yesterday)
        dpAdjustment.setMargins(documentCustomYesterday, 0,0,0,0)
        dpAdjustment.setScale(documentCustomYesterday.layoutParams, 360f,56f)

        val expandYesterday = findViewById<Expandable>(R.id.document_Yesterday_expand)

        //////////////////////today///////////////////////
        val todayLayout = findViewById<RelativeLayout>(R.id.relative_layout_Today)
        val todayLine = findViewById<View>(R.id.line_Today)
        dpAdjustment.setMargins(todayLine, 0,0,0,0)
        dpAdjustment.setScale(todayLine.layoutParams, 360f,5f)

        val documentCustomToday = findViewById<DocumentCustomView>(R.id.document_Today)
        dpAdjustment.setMargins(documentCustomToday, 0,0,0,0)
        dpAdjustment.setScale(documentCustomToday.layoutParams, 360f,56f)

        val expandToday = findViewById<Expandable>(R.id.document_Today_expand)

        removeDoc =findViewById(R.id.document_btn)
        dpAdjustment.setScale(removeDoc.layoutParams, 328f,48f)
        dpAdjustment.setMargins(removeDoc, 16,0,0,16)


        selectBtn = findViewById(R.id.document_check)

        var boolean = false

        removeDoc.setOnClickListener {
            selectDocRemove()
        }


        val listenerTwoYear = View.OnClickListener {
            boolean = if (!boolean) {
                expandTwoYear.expand()
                documentCustomTwoYear.setDocMenu(R.drawable.icon_menu_drop_up)

                true
            } else {
                expandTwoYear.collapse()
                documentCustomTwoYear.setDocMenu(R.drawable.icon_menu_drop_down)

                false
            }
        }

        documentCustomTwoYear.setListener(listenerTwoYear)

        /////////////////////////////////////////////////////////////
        /////////////////////////////////////////////////////////////
        /////////////////////////////////////////////////////////////

        val listenerYear = View.OnClickListener {
            boolean = if (!boolean) {
                expandYear.expand()
                documentCustomYear.setDocMenu(R.drawable.icon_menu_drop_up)

                true
            } else {
                expandYear.collapse()
                documentCustomYear.setDocMenu(R.drawable.icon_menu_drop_down)

                false
            }
        }
        documentCustomYear.setListener(listenerYear)

        /////////////////////////////////////////////////////////////
        /////////////////////////////////////////////////////////////
        /////////////////////////////////////////////////////////////

        val listenerYearDown = View.OnClickListener {
            boolean = if (!boolean) {
                expandYearDown.expand()
                documentCustomYearDown.setDocMenu(R.drawable.icon_menu_drop_up)

                true
            } else {
                expandYearDown.collapse()
                documentCustomYearDown.setDocMenu(R.drawable.icon_menu_drop_down)

                false
            }
        }
        documentCustomYearDown.setListener(listenerYearDown)

        /////////////////////////////////////////////////////////////
        /////////////////////////////////////////////////////////////
        /////////////////////////////////////////////////////////////

        val listenerSixMonth = View.OnClickListener {
            boolean = if (!boolean) {
                expandSixMonth.expand()
                documentCustomSixMonth.setDocMenu(R.drawable.icon_menu_drop_up)

                true
            } else {
                expandSixMonth.collapse()
                documentCustomSixMonth.setDocMenu(R.drawable.icon_menu_drop_down)

                false
            }
        }
        documentCustomSixMonth.setListener(listenerSixMonth)

        /////////////////////////////////////////////////////////////
        /////////////////////////////////////////////////////////////
        /////////////////////////////////////////////////////////////

        val listenerThreeMonth = View.OnClickListener {
            boolean = if (!boolean) {
                expandThreeMonth.expand()
                documentCustomThreeMonth.setDocMenu(R.drawable.icon_menu_drop_up)

                true
            } else {
                expandThreeMonth.collapse()
                documentCustomThreeMonth.setDocMenu(R.drawable.icon_menu_drop_down)

                false
            }
        }
        documentCustomThreeMonth.setListener(listenerThreeMonth)

        /////////////////////////////////////////////////////////////
        /////////////////////////////////////////////////////////////
        /////////////////////////////////////////////////////////////

        val listenerMonth = View.OnClickListener {
            boolean = if (!boolean) {
                expandMonth.expand()
                documentCustomMonth.setDocMenu(R.drawable.icon_menu_drop_up)

                true
            } else {
                expandMonth.collapse()
                documentCustomMonth.setDocMenu(R.drawable.icon_menu_drop_down)

                false
            }
        }
        documentCustomMonth.setListener(listenerMonth)

        /////////////////////////////////////////////////////////////
        /////////////////////////////////////////////////////////////
        /////////////////////////////////////////////////////////////

        val listenerYesterday = View.OnClickListener {
            boolean = if (!boolean) {
                expandYesterday.expand()
                documentCustomYesterday.setDocMenu(R.drawable.icon_menu_drop_up)

                true
            } else {
                expandYesterday.collapse()
                documentCustomYesterday.setDocMenu(R.drawable.icon_menu_drop_down)

                false
            }
        }
        documentCustomYesterday.setListener(listenerYesterday)

        /////////////////////////////////////////////////////////////
        /////////////////////////////////////////////////////////////
        /////////////////////////////////////////////////////////////

        val listenerToday = View.OnClickListener {
            boolean = if (!boolean) {
                expandToday.expand()
                documentCustomToday.setDocMenu(R.drawable.icon_menu_drop_up)

                true
            } else {
                expandToday.collapse()
                documentCustomToday.setDocMenu(R.drawable.icon_menu_drop_down)

                false
            }
        }
        documentCustomToday.setListener(listenerToday)

        var diffTodayAllSize : Long = 0
        var diffYesterAllSize : Long = 0
        var diffMonthAllSize : Long = 0
        var diffThreeMonthAllSize : Long = 0
        var diffSixMonthAllSize : Long = 0
        var diffYearDownAllSize : Long = 0
        var diffYearAllSize : Long = 0
        var diffTwoYearAllSize : Long = 0
        for (diff in docListCategory) {

                if ((0 < (diff.diffTime / Actions.sec) && 60 > (diff.diffTime / Actions.sec)) || (0 <= (diff.diffTime / Actions.min) && 24 > (diff.diffTime / Actions.min))) {
                        println(">>> diffTime : $docListCategory")
                        todayListCategory.add(DocData(type = diff.type , documentIcon = diff.documentIcon, documentName = diff.documentName, documentTime = diff.documentTime, documentSize = diff.documentSize, documentChoose = diff.documentChoose, documentFile = diff.documentFile, diffTime = diff.diffTime ))
                    diffTodayAllSize += diff.documentSize
                        documentCustomToday.setDocSize(Formatter.formatFileSize(context, diffTodayAllSize))
                        documentCustomToday.setDocCount(todayListCategory.count().toString() + "개")

                } else if (0 < (diff.diffTime / Actions.hour) && 1 >= (diff.diffTime / Actions.hour)) {
                    yesterdayListCategory.add(DocData(type = diff.type , documentIcon = diff.documentIcon, documentName = diff.documentName, documentTime = diff.documentTime, documentSize = diff.documentSize, documentChoose = diff.documentChoose, documentFile = diff.documentFile, diffTime = diff.diffTime ))
                    diffYesterAllSize += diff.documentSize
                    documentCustomYesterday.setDocSize(Formatter.formatFileSize(context, diffYesterAllSize))
                    documentCustomYesterday.setDocCount(yesterdayListCategory.count().toString() + "개")
                } else if (30 > (diff.diffTime / Actions.hour) && 1 < (diff.diffTime / Actions.hour)) {
                    monthListCategory.add(DocData(type = diff.type , documentIcon = diff.documentIcon, documentName = diff.documentName, documentTime = diff.documentTime, documentSize = diff.documentSize, documentChoose = diff.documentChoose, documentFile = diff.documentFile, diffTime = diff.diffTime ))
                    diffMonthAllSize += diff.documentSize
                    documentCustomMonth.setDocSize(Formatter.formatFileSize(context, diffMonthAllSize))
                    documentCustomMonth.setDocCount(monthListCategory.count().toString() + "개")
                } else if (0 < (diff.diffTime / Actions.day) && 3 > (diff.diffTime / Actions.day)) {
                    threeMonthListCategory.add(DocData(type = diff.type , documentIcon = diff.documentIcon, documentName = diff.documentName, documentTime = diff.documentTime, documentSize = diff.documentSize, documentChoose = diff.documentChoose, documentFile = diff.documentFile, diffTime = diff.diffTime ))
                    diffThreeMonthAllSize += diff.documentSize
                    documentCustomThreeMonth.setDocSize(Formatter.formatFileSize(context, diffThreeMonthAllSize))
                    documentCustomThreeMonth.setDocCount(threeMonthListCategory.count().toString() + "개")
                } else if (3 <= (diff.diffTime / Actions.day) && 6 > (diff.diffTime / Actions.day)) {
                    sixMonthListCategory.add(DocData(type = diff.type , documentIcon = diff.documentIcon, documentName = diff.documentName, documentTime = diff.documentTime, documentSize = diff.documentSize, documentChoose = diff.documentChoose, documentFile = diff.documentFile, diffTime = diff.diffTime ))
                    diffSixMonthAllSize += diff.documentSize
                    documentCustomSixMonth.setDocSize(Formatter.formatFileSize(context, diffSixMonthAllSize))
                    documentCustomSixMonth.setDocCount(sixMonthListCategory.count().toString() + "개")
                } else if (6 <= (diff.diffTime / Actions.day) && 12 > (diff.diffTime / Actions.day)) {
                    yearDownListCategory.add(DocData(type = diff.type , documentIcon = diff.documentIcon, documentName = diff.documentName, documentTime = diff.documentTime, documentSize = diff.documentSize, documentChoose = diff.documentChoose, documentFile = diff.documentFile, diffTime = diff.diffTime ))
                    diffYearDownAllSize += diff.documentSize
                    documentCustomYearDown.setDocSize(Formatter.formatFileSize(context, diffYearDownAllSize))
                    documentCustomYearDown.setDocCount(yearDownListCategory.count().toString() + "개")
                } else if (2 > (diff.diffTime / Actions.month)) {
                    yearListCategory.add(DocData(type = diff.type , documentIcon = diff.documentIcon, documentName = diff.documentName, documentTime = diff.documentTime, documentSize = diff.documentSize, documentChoose = diff.documentChoose, documentFile = diff.documentFile, diffTime = diff.diffTime ))
                    diffYearAllSize += diff.documentSize
                    documentCustomYear.setDocSize(Formatter.formatFileSize(context, diffYearAllSize))
                    documentCustomYear.setDocCount(yearListCategory.count().toString() + "개")
                } else {
                    twoYearListCategory.add(DocData(type = diff.type , documentIcon = diff.documentIcon, documentName = diff.documentName, documentTime = diff.documentTime, documentSize = diff.documentSize, documentChoose = diff.documentChoose, documentFile = diff.documentFile, diffTime = diff.diffTime ))
                    diffTwoYearAllSize += diff.documentSize
                    documentCustomTwoYear.setDocSize(Formatter.formatFileSize(context, diffTwoYearAllSize))
                    documentCustomTwoYear.setDocCount(twoYearListCategory.count().toString() + "개")

            }

        }

        ///////////////////////////////////////////////////////////////////////////////////
        val recyclerToday = findViewById<RecyclerView>(R.id.document_Today_recycler)
        recyclerToday.layoutManager = LinearLayoutManager(this)

        if(todayListCategory.isEmpty()){
            documentCustomToday.visibility = View.GONE
            todayLine.visibility= View.GONE
        }else {
            recyclerToday.adapter = todayAdapter
        }

        val todayAllCheck = View.OnClickListener{
            boolean = if (!boolean) {
                todayAdapter.setCheckAll(false, position)
                RepositoryImpl.getDocList().clear()
                RepositoryImpl.getFileToday().clear()
                true
            } else {
                todayAdapter.setCheckAll(true, position)
                false
            }
        }

        documentCustomToday.setCheckListener(todayAllCheck)
        //////////////////////////////////////////////////////////////////////////////////////
        val recyclerYesterday = findViewById<RecyclerView>(R.id.document_Yesterday_recycler)
        recyclerYesterday.layoutManager = LinearLayoutManager(this)

        yesterdayAdapter = DocumentAdapter(yesterdayListCategory, context)
        if(yesterdayListCategory.isEmpty()){
            documentCustomYesterday.visibility = View.GONE
            yesterdayLine.visibility= View.GONE

        }else {
            recyclerYesterday.adapter = yesterdayAdapter
        }

        val yesterdayAllCheck = View.OnClickListener{
            boolean = if (!boolean) {
                yesterdayAdapter.setCheckAll(false, position)
                RepositoryImpl.getDocList().clear()
                RepositoryImpl.getFileYesterday().clear()

                true
            } else {
                yesterdayAdapter.setCheckAll(true, position)
                false
            }
        }

        documentCustomYesterday.setCheckListener(yesterdayAllCheck)
        //////////////////////////////////////////////////////////////////////
        val recyclerMonth = findViewById<RecyclerView>(R.id.document_Month_recycler)

        recyclerMonth.layoutManager = LinearLayoutManager(this)
        if(monthListCategory.isEmpty()){
            documentCustomMonth.visibility = View.GONE
            monthLine.visibility= View.GONE

        }else {
            recyclerMonth.adapter = monthAdapter
        }

        val monthAllCheck = View.OnClickListener{
            boolean = if (!boolean) {
                monthAdapter.setCheckAll(false, position)
                RepositoryImpl.getDocList().clear()
                RepositoryImpl.getFileMonth().clear()

                true
            } else {
                monthAdapter.setCheckAll(true, position)
                false
            }
        }

        documentCustomMonth.setCheckListener(monthAllCheck)
        //////////////////////////////////////////////////////////////////////
        val recyclerThreeMonth = findViewById<RecyclerView>(R.id.document_threeMonth_recycler)

        recyclerThreeMonth.layoutManager = LinearLayoutManager(this)
        if(threeMonthListCategory.isEmpty()){
            documentCustomThreeMonth.visibility = View.GONE
            threeMonthLine.visibility= View.GONE

        }else {
            recyclerThreeMonth.adapter = threeMonthAdapter
        }

        val threeMonthAllCheck = View.OnClickListener{
            boolean = if (!boolean) {
                threeMonthAdapter.setCheckAll(false, position)
                RepositoryImpl.getDocList().clear()
                RepositoryImpl.getFileThreeMonth().clear()

                true
            } else {
                threeMonthAdapter.setCheckAll(true, position)
                false
            }
        }

        documentCustomThreeMonth.setCheckListener(threeMonthAllCheck)
        /////////////////////////////////////////////////////////////////////////////////
        val recyclerSixMonth = findViewById<RecyclerView>(R.id.document_sixMonth_recycler)

        recyclerSixMonth.layoutManager =LinearLayoutManager(this)
        if(sixMonthListCategory.isEmpty()){
            documentCustomSixMonth.visibility = View.GONE
            sixMonthLine.visibility= View.GONE

        }else {
            recyclerSixMonth.adapter = sixMonthAdapter
        }

        val sixMonthAllCheck = View.OnClickListener{
            boolean = if (!boolean) {
                sixMonthAdapter.setCheckAll(false, position)
                RepositoryImpl.getDocList().clear()
                RepositoryImpl.getFileSixMonth().clear()


                true
            } else {
                sixMonthAdapter.setCheckAll(true, position)
                false
            }
        }

        documentCustomSixMonth.setCheckListener(sixMonthAllCheck)
        //////////////////////////////////////////////////////////////////////////////////
        val recyclerYearDown = findViewById<RecyclerView>(R.id.document_YearDown_recycler)

        recyclerYearDown.layoutManager = LinearLayoutManager(this)
        if(yearDownListCategory.isEmpty()){
            documentCustomYearDown.visibility = View.GONE
            yearDownLine.visibility= View.GONE

        }else {
            recyclerYearDown.adapter = yearDownAdapter
        }

        val yearDownAllCheck = View.OnClickListener{
            boolean = if (!boolean) {
                yearDownAdapter.setCheckAll(false, position)
                RepositoryImpl.getDocList().clear()
                RepositoryImpl.getFileYearDown().clear()

                true
            } else {
                yearDownAdapter.setCheckAll(true, position)
                false
            }
        }

        documentCustomYearDown.setCheckListener(yearDownAllCheck)
        //////////////////////////////////////////////////////////////////////////
        val recyclerYear = findViewById<RecyclerView>(R.id.document_Year_recycler)

        recyclerYear.layoutManager =LinearLayoutManager(this)
        if(yearListCategory.isEmpty()){
            documentCustomYear.visibility = View.GONE
            yearLine.visibility= View.GONE

        }else {
            recyclerYear.adapter = yearAdapter
        }

        val yearAllCheck = View.OnClickListener{
            boolean = if (!boolean) {
                yearAdapter.setCheckAll(false, position)
                RepositoryImpl.getDocList().clear()
                RepositoryImpl.getFileYear().clear()

                true
            } else {
                yearAdapter.setCheckAll(true, position)
                false
            }
        }

        documentCustomYear.setCheckListener(yearAllCheck)
        ////////////////////////////////////////////////////////////////////////
        val recyclerTwoYear = findViewById<RecyclerView>(R.id.document_twoYear_recycler)

        recyclerTwoYear.layoutManager = LinearLayoutManager(this)
        if(twoYearListCategory.isEmpty()){
            documentCustomTwoYear.visibility = View.GONE
            twoYearLine.visibility= View.GONE

        }else{
            recyclerTwoYear.adapter = twoYearAdapter
        }

        val twoYearAllCheck = View.OnClickListener{
            boolean = if (!boolean) {
                twoYearAdapter.setCheckAll(false, position)
                RepositoryImpl.getDocList().clear()
                RepositoryImpl.getFileTwoYear().clear()
                true
            } else {
                twoYearAdapter.setCheckAll(true, position)
                false
            }
        }

        documentCustomTwoYear.setCheckListener(twoYearAllCheck)

        ///////////////////////////////////////////////////////////////////////////////////

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
        val docDiffList = arrayListOf<DocDiffData>()

        for (files in fileList) {
            val docSize: Long = arrayListOf(files.length()).sum()
            val docCount = arrayListOf(files).count()

            docAllSize += docSize
            docAllCount += docCount

            val time = files.lastModified()
            val today = System.currentTimeMillis()
            diffTime = (today - time) / 1000

            docListCategory.add(DocData(
                type = TYPE_DOCUMENT, documentIcon = R.drawable.icon_message, documentName = files.name, documentTime = diffTime , documentSize = docSize, documentChoose = false, documentFile = files,
                diffTime = diffTime))

            docDiffList.add(DocDiffData(diffArray = diffTime))
            RepositoryImpl.setDocDiff(docDiffList)

        }

    }

    fun today(){
        todayCheckboxList= RepositoryImpl.getFileToday()
        if(todayListCategory.isNotEmpty()){
            val checkItr = todayCheckboxList.iterator()

            while (checkItr.hasNext()) {
                val checkListItr = checkItr.next()
                val checkDocItr = todayListCategory.iterator()
                while (checkDocItr.hasNext()) {
                    val checkDocListItr = checkDocItr.next()
                    if (checkListItr.fileName == checkDocListItr.documentName) {
                        checkDocListItr.documentFile.delete()

                        checkDocItr.remove()
                        todayAdapter.set(todayListCategory)

                    }
                }
                checkItr.remove()
            }
        }
    }

    fun yesterday(){
        yesterdayCheckboxList = RepositoryImpl.getFileYesterday()
        if(yesterdayListCategory.isNotEmpty()){
            val checkItr = yesterdayCheckboxList.iterator()

            while (checkItr.hasNext()) {
                val checkListItr = checkItr.next()
                val checkDocItr = yesterdayListCategory.iterator()
                while (checkDocItr.hasNext()) {
                    val checkDocListItr = checkDocItr.next()
                    if (checkListItr.fileName == checkDocListItr.documentName) {
                        checkDocListItr.documentFile.delete()

                        checkDocItr.remove()
                        yesterdayAdapter.set(yesterdayListCategory)

                    }
                }
                checkItr.remove()
            }
        }


    }
    fun month(){
        monthCheckboxList = RepositoryImpl.getFileMonth()
        if(monthListCategory.isNotEmpty()) {
            val checkItr = monthCheckboxList.iterator()

            while (checkItr.hasNext()) {
                val checkListItr = checkItr.next()
                val checkDocItr = monthListCategory.iterator()
                while (checkDocItr.hasNext()) {
                    val checkDocListItr = checkDocItr.next()
                    if (checkListItr.fileName == checkDocListItr.documentName) {
                        checkDocListItr.documentFile.delete()
                        checkDocItr.remove()
                        monthAdapter.set(monthListCategory)
                    }
                }
                checkItr.remove()
            }
        }
    }
    fun threeMonth (){
        threeMonthCheckboxList = RepositoryImpl.getFileThreeMonth()
        if(threeMonthListCategory.isNotEmpty()){
            val checkItr = threeMonthCheckboxList.iterator()

            while (checkItr.hasNext()) {
                val checkListItr = checkItr.next()
                val checkDocItr = threeMonthListCategory.iterator()
                while (checkDocItr.hasNext()) {
                    val checkDocListItr = checkDocItr.next()
                    if (checkListItr.fileName == checkDocListItr.documentName) {
                        checkDocListItr.documentFile.delete()
                        checkDocItr.remove()
                        threeMonthAdapter.set(threeMonthListCategory)
                    }
                }
                checkItr.remove()
            }

        }

    }
    fun sixMonth(){
        sixMonthCheckboxList = RepositoryImpl.getFileSixMonth()
        if(sixMonthListCategory.isNotEmpty()){
            val checkItr = sixMonthCheckboxList.iterator()

            while (checkItr.hasNext()) {
                val checkListItr = checkItr.next()
                val checkDocItr = sixMonthListCategory.iterator()
                while (checkDocItr.hasNext()) {
                    val checkDocListItr = checkDocItr.next()
                    if (checkListItr.fileName == checkDocListItr.documentName) {
                        checkDocListItr.documentFile.delete()
                        checkDocItr.remove()
                        sixMonthAdapter.set(sixMonthListCategory)

                    }
                }
                checkItr.remove()
            }

        }

    }
    fun yearDown(){
        yearDownCheckboxList = RepositoryImpl.getFileYearDown()
        if(yearDownListCategory.isNotEmpty()){
            val checkItr = yearDownCheckboxList.iterator()

            while (checkItr.hasNext()) {
                val checkListItr = checkItr.next()
                val checkDocItr = yearDownListCategory.iterator()
                while (checkDocItr.hasNext()) {
                    val checkDocListItr = checkDocItr.next()
                    if (checkListItr.fileName == checkDocListItr.documentName) {
                        checkDocListItr.documentFile.delete()
                        checkDocItr.remove()
                        yearDownAdapter.set(yearDownListCategory)

                    }
                }
                checkItr.remove()
            }
        }

    }
    fun year(){
        yearCheckboxList = RepositoryImpl.getFileYear()
        if(yearListCategory.isNotEmpty()){
            val checkItr = yearCheckboxList.iterator()

            while (checkItr.hasNext()) {
                val checkListItr = checkItr.next()
                val checkDocItr = yearListCategory.iterator()
                while (checkDocItr.hasNext()) {
                    val checkDocListItr = checkDocItr.next()
                    if (checkListItr.fileName == checkDocListItr.documentName) {
                        checkDocListItr.documentFile.delete()
                        checkDocItr.remove()
                        yearAdapter.set(yearListCategory)

                    }
                }
                checkItr.remove()
            }
        }

    }
    fun twoYear (){
        twoYearCheckboxList = RepositoryImpl.getFileTwoYear()
        if(twoYearListCategory.isNotEmpty()){
            val checkItr = twoYearCheckboxList.iterator()

            while (checkItr.hasNext()) {
                val checkListItr = checkItr.next()
                val checkDocItr = twoYearListCategory.iterator()
                while (checkDocItr.hasNext()) {
                    val checkDocListItr = checkDocItr.next()
                    if (checkListItr.fileName == checkDocListItr.documentName) {
                        checkDocListItr.documentFile.delete()
                        checkDocItr.remove()
                        twoYearAdapter.set(twoYearListCategory)
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
        today()
        yesterday()
        month()
        threeMonth()
        sixMonth()
        yearDown()
        year()
        twoYear()
    }

    fun back(view: View) {
        finish()
    }


}