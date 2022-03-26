package com.kyoungss.cleaner.storage.image

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.text.format.Formatter
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.kyoungss.cleaner.*
import com.kyoungss.cleaner.expand.Expandable
import com.kyoungss.cleaner.check.manage.ManageData
import com.kyoungss.cleaner.data.RepositoryImpl
import com.kyoungss.cleaner.storage.CheckStorageData
import java.util.*
import kotlin.collections.ArrayList

class ManageImageActivity : AppCompatActivity() {

    var context: Context = this
    private lateinit var dpAdjustment: DisplayAdjustment

    private val twoYearListCategory = ArrayList<ImageData>()
    private val yearListCategory = ArrayList<ImageData>()
    private val yearDownListCategory = ArrayList<ImageData>()
    private val sixMonthListCategory = ArrayList<ImageData>()
    private val threeMonthListCategory = ArrayList<ImageData>()
    private val monthListCategory = ArrayList<ImageData>()
    private val yesterdayListCategory = ArrayList<ImageData>()
    private val todayListCategory = ArrayList<ImageData>()
    private val imageListCategory = ArrayList<ImageData>()
    var popupWindow : PopupWindow ?= null

    private var removeImage: Button? = null
    private lateinit var allSize: TextView
    private lateinit var allCount: TextView
    var imageAllSize: Long = 0
    var imageAllCount: Int = 0

    private var imageAdapter = ImageAdapter(imageListCategory, context)

    private var monthAdapter = ImageAdapter(monthListCategory ,context)
    private var todayAdapter = ImageAdapter(todayListCategory ,context)
    private var yesterdayAdapter = ImageAdapter(yesterdayListCategory ,context)
    private var threeMonthAdapter = ImageAdapter(threeMonthListCategory ,context)
    private var sixMonthAdapter = ImageAdapter(sixMonthListCategory ,context)
    private var yearDownAdapter = ImageAdapter(yearDownListCategory ,context)
    private var yearAdapter = ImageAdapter(yearListCategory ,context)
    private var twoYearAdapter = ImageAdapter(twoYearListCategory ,context)

    private var checkboxList = ArrayList<CheckImageData>()
    private var todayCheckboxList =ArrayList<CheckStorageData>()
    private var yesterdayCheckboxList =ArrayList<CheckStorageData>()
    private var monthCheckboxList =ArrayList<CheckStorageData>()
    private var threeMonthCheckboxList =ArrayList<CheckStorageData>()
    private var sixMonthCheckboxList =ArrayList<CheckStorageData>()
    private var yearDownCheckboxList =ArrayList<CheckStorageData>()
    private var yearCheckboxList =ArrayList<CheckStorageData>()
    private var twoYearCheckboxList =ArrayList<CheckStorageData>()

    var selectBtn: CheckBox? = null
    private var uriImage: Uri? = null
    var diffTime: Long = 0

    companion object {
        const val TYPE_IMAGE = 0L
        const val TYPE_IMAGE_PACKAGE = 0x01

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image)
        val menuBar = findViewById<Button>(R.id.menu)

        val view: View = layoutInflater.inflate(R.layout.popup_gallery_layout, null)

        imageAdapter.setItemClickListener(object : OnItemClickListener {

            override fun onItemClick(position: List<ManageData>) {
                TODO("Not yet implemented")
            }

            override fun onItemLongClick(position: Int): Boolean {


                return false

            }
            override fun onItemSelected(position: Int, selected: Boolean) {

            }

        })
        menuBar.setOnClickListener {
             popupWindow = PopupWindow(
                view,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                true
            )
            popupWindow!!.showAsDropDown(menuBar, -205, -100)
            popupWindow!!.setBackgroundDrawable(ColorDrawable(Color.WHITE))

        }

        val firstPopupTextView = view.findViewById<TextView>(R.id.popup_text_first)
        firstPopupTextView.setOnClickListener {
            //Toast.makeText(this, "날짜 순")
        }

        val secondsPopupTextView = view.findViewById<TextView>(R.id.popup_text_second)
        secondsPopupTextView.setOnClickListener {

            val intent = Intent(this, ManageImageSizeActivity::class.java)
            this.startActivity(intent)
            finish()
        }
        getAllImagePath()

        initViews()

        removeImage?.setOnClickListener {


            selectRemoveImage()

        }
    }

    override fun onBackPressed(){
        super.onBackPressed()
        // 뒤로가기 버튼 클릭 시
        /*finish()
        overridePendingTransition(0,0)
        val intent :Intent= intent
        startActivity(intent)
        overridePendingTransition(0,0)
*/
    }

    override fun onResume() {
        super.onResume()

    }

    override fun onDestroy() {
        super.onDestroy()
        if(popupWindow != null && popupWindow!!.isShowing){
            popupWindow!!.dismiss()
        }
    }


    private fun initViews() {
        dpAdjustment = DisplayAdjustment.getInstance(this)

        val imageBack = findViewById<Button>(R.id.imageBack)
        dpAdjustment.setMargins(imageBack,16,16,0,0 )
        dpAdjustment.setScale(imageBack.layoutParams,24f,24f)

        val imageTitle = findViewById<TextView>(R.id.imageTitle)
        dpAdjustment.setMargins(imageTitle, 12,12,0,0)

        allSize = findViewById(R.id.infoTitle)
        dpAdjustment.setMargins(allSize, 16,33,0,0)

        allCount = findViewById(R.id.infoTitleCount)
        dpAdjustment.setMargins(allCount, 14,34,0,0)

        val imageMenu = findViewById<Button>(R.id.menu)
        dpAdjustment.setMargins(imageMenu, 0,31,19,0)
        dpAdjustment.setScale(imageMenu.layoutParams, 24f,24f)

        val imageList = findViewById<RelativeLayout>(R.id.list)

        //////////////twoYear//////////////

        val twoYearLine = findViewById<View>(R.id.line_twoYear)
        dpAdjustment.setMargins(twoYearLine, 0,0,0,0)
        dpAdjustment.setScale(twoYearLine.layoutParams, 360f,5f)

        val imageCustomTwoYear = findViewById<ImageCustomView>(R.id.image_twoYear)
        dpAdjustment.setMargins(imageCustomTwoYear, 0,10,0,0)
        dpAdjustment.setScale(imageCustomTwoYear.layoutParams, 360f,56f)

        val expandTwoYear = findViewById<Expandable>(R.id.image_twoYear_expand)


        //////////////year///////////////////////
        val yearLine = findViewById<View>(R.id.line_Year)
        dpAdjustment.setMargins(yearLine, 0,0,0,0)
        dpAdjustment.setScale(yearLine.layoutParams, 360f,5f)

        val imageCustomYear = findViewById<ImageCustomView>(R.id.image_Year)
        dpAdjustment.setMargins(imageCustomYear, 0,0,0,0)
        dpAdjustment.setScale(imageCustomYear.layoutParams, 360f,56f)

        val expandYear = findViewById<Expandable>(R.id.image_Year_expand)

        /////////////////yearDown///////////////////////
        val yearDownLine = findViewById<View>(R.id.line_YearDown)
        dpAdjustment.setMargins(yearDownLine, 0,0,0,0)
        dpAdjustment.setScale(yearDownLine.layoutParams, 360f,5f)

        val imageCustomYearDown = findViewById<ImageCustomView>(R.id.image_YearDown)
        dpAdjustment.setMargins(imageCustomYearDown, 0,0,0,0)
        dpAdjustment.setScale(imageCustomYearDown.layoutParams, 360f,56f)

        val expandYearDown = findViewById<Expandable>(R.id.image_YearDown_expand)

        ///////////////////////////////sixMonth////////////////////
        val sixMonthLayout = findViewById<RelativeLayout>(R.id.relative_layout_sixMonth)
        val sixMonthLine = findViewById<View>(R.id.line_sixMonth)
        dpAdjustment.setMargins(sixMonthLine, 0,0,0,0)
        dpAdjustment.setScale(sixMonthLine.layoutParams, 360f,5f)

        val imageCustomSixMonth = findViewById<ImageCustomView>(R.id.image_sixMonth)
        dpAdjustment.setMargins(imageCustomSixMonth, 0,0,0,0)
        dpAdjustment.setScale(imageCustomSixMonth.layoutParams, 360f,56f)

        val expandSixMonth = findViewById<Expandable>(R.id.image_sixMonth_expand)

        ////////////////////////////threeMonth///////////////////////
        val threeMonthLine = findViewById<View>(R.id.line_threeMonth)
        dpAdjustment.setMargins(threeMonthLine, 0,0,0,0)
        dpAdjustment.setScale(threeMonthLine.layoutParams, 360f,5f)

        val imageCustomThreeMonth = findViewById<ImageCustomView>(R.id.image_threeMonth)
        dpAdjustment.setMargins(imageCustomThreeMonth, 0,0,0,0)
        dpAdjustment.setScale(imageCustomThreeMonth.layoutParams, 360f,56f)

        val expandThreeMonth = findViewById<Expandable>(R.id.image_threeMonth_expand)

        ///////////////////////////month //////////////////////////
        val monthLine = findViewById<View>(R.id.line_Month)
        dpAdjustment.setMargins(monthLine, 0,0,0,0)
        dpAdjustment.setScale(monthLine.layoutParams, 360f,5f)

        val imageCustomMonth = findViewById<ImageCustomView>(R.id.image_Month)
        dpAdjustment.setMargins(imageCustomMonth, 0,0,0,0)
        dpAdjustment.setScale(imageCustomMonth.layoutParams, 360f,56f)

        val expandMonth = findViewById<Expandable>(R.id.image_Month_expand)

        /////////////////////yesterday///////////////////
        val yesterdayLine = findViewById<View>(R.id.line_Yesterday)
        dpAdjustment.setMargins(yesterdayLine, 0,0,0,0)
        dpAdjustment.setScale(yesterdayLine.layoutParams, 360f,5f)

        val imageCustomYesterday = findViewById<ImageCustomView>(R.id.image_Yesterday)
        dpAdjustment.setMargins(imageCustomYesterday, 0,0,0,0)
        dpAdjustment.setScale(imageCustomYesterday.layoutParams, 360f,56f)

        val expandYesterday = findViewById<Expandable>(R.id.image_Yesterday_expand)

        //////////////////////today///////////////////////
        val todayLayout = findViewById<RelativeLayout>(R.id.relative_layout_Today)
        val todayLine = findViewById<View>(R.id.line_Today)
        dpAdjustment.setMargins(todayLine, 0,0,0,0)
        dpAdjustment.setScale(todayLine.layoutParams, 360f,5f)

        val imageCustomToday = findViewById<ImageCustomView>(R.id.image_Today)
        dpAdjustment.setMargins(imageCustomToday, 0,0,0,0)
        dpAdjustment.setScale(imageCustomToday.layoutParams, 360f,56f)

        val expandToday = findViewById<Expandable>(R.id.image_Today_expand)

        //////////////////////////////////////////////////////////////////////
        allSize.text = Formatter.formatFileSize(context, imageAllSize)
        allCount.text = imageAllCount.toString() + "개"
        removeImage = findViewById(R.id.image_btn)


        dpAdjustment.setScale(removeImage!!.layoutParams, 328f,48f)
        dpAdjustment.setMargins(removeImage!!, 16,0,0,16)
        selectBtn = findViewById(R.id.grid_selected)

        val position = 0

        var boolean = false

        val listenerTwoYear = View.OnClickListener {
            boolean = if (!boolean) {
                expandTwoYear.expand()
                imageCustomTwoYear.setImageMenu(R.drawable.icon_menu_drop_up)

                true
            } else {
                expandTwoYear.collapse()
                imageCustomTwoYear.setImageMenu(R.drawable.icon_menu_drop_down)

                false
            }
        }

        imageCustomTwoYear.setListener(listenerTwoYear)

        /////////////////////////////////////////////////////////////
        /////////////////////////////////////////////////////////////
        /////////////////////////////////////////////////////////////

        val listenerYear = View.OnClickListener {
            boolean = if (!boolean) {
                expandYear.expand()
                imageCustomYear.setImageMenu(R.drawable.icon_menu_drop_up)

                true
            } else {
                expandYear.collapse()
                imageCustomYear.setImageMenu(R.drawable.icon_menu_drop_down)

                false
            }
        }
        imageCustomYear.setListener(listenerYear)


        /////////////////////////////////////////////////////////////
        /////////////////////////////////////////////////////////////
        /////////////////////////////////////////////////////////////

        val listenerYearDown = View.OnClickListener {
            boolean = if (!boolean) {
                expandYearDown.expand()
                imageCustomYearDown.setImageMenu(R.drawable.icon_menu_drop_up)

                true
            } else {
                expandYearDown.collapse()
                imageCustomYearDown.setImageMenu(R.drawable.icon_menu_drop_down)

                false
            }
        }
        imageCustomYearDown.setListener(listenerYearDown)


        /////////////////////////////////////////////////////////////
        /////////////////////////////////////////////////////////////
        /////////////////////////////////////////////////////////////

        val listenerSixMonth = View.OnClickListener {
            boolean = if (!boolean) {
                expandSixMonth.expand()
                imageCustomSixMonth.setImageMenu(R.drawable.icon_menu_drop_up)

                true
            } else {
                expandSixMonth.collapse()
                imageCustomSixMonth.setImageMenu(R.drawable.icon_menu_drop_down)

                false
            }
        }

        imageCustomSixMonth.setListener(listenerSixMonth)

        /////////////////////////////////////////////////////////////
        /////////////////////////////////////////////////////////////
        /////////////////////////////////////////////////////////////

        val listenerThreeMonth = View.OnClickListener {
            boolean = if (!boolean) {
                expandThreeMonth.expand()
                imageCustomThreeMonth.setImageMenu(R.drawable.icon_menu_drop_up)

                true
            } else {
                expandThreeMonth.collapse()
                imageCustomThreeMonth.setImageMenu(R.drawable.icon_menu_drop_down)

                false
            }
        }

        imageCustomThreeMonth.setListener(listenerThreeMonth)

        /////////////////////////////////////////////////////////////
        /////////////////////////////////////////////////////////////
        /////////////////////////////////////////////////////////////

        val listenerMonth = View.OnClickListener {
            boolean = if (!boolean) {
                expandMonth.expand()
                imageCustomMonth.setImageMenu(R.drawable.icon_menu_drop_up)

                true
            } else {
                expandMonth.collapse()
                imageCustomMonth.setImageMenu(R.drawable.icon_menu_drop_down)

                false
            }
        }

        imageCustomMonth.setListener(listenerMonth)

        /////////////////////////////////////////////////////////////
        /////////////////////////////////////////////////////////////
        /////////////////////////////////////////////////////////////

        val listenerYesterday = View.OnClickListener {
            boolean = if (!boolean) {
                expandYesterday.expand()
                imageCustomYesterday.setImageMenu(R.drawable.icon_menu_drop_up)

                true
            } else {
                expandYesterday.collapse()
                imageCustomYesterday.setImageMenu(R.drawable.icon_menu_drop_down)

                false
            }
        }

        imageCustomYesterday.setListener(listenerYesterday)

        /////////////////////////////////////////////////////////////
        /////////////////////////////////////////////////////////////
        /////////////////////////////////////////////////////////////


        val listenerToday = View.OnClickListener {
            boolean = if (!boolean) {
                expandToday.expand()
                imageCustomToday.setImageMenu(R.drawable.icon_menu_drop_up)

                true
            } else {
                expandToday.collapse()
                imageCustomToday.setImageMenu(R.drawable.icon_menu_drop_down)

                false
            }
        }
        imageCustomToday.setListener(listenerToday)

        //////////////////////////////////////////////////////////////////////////////////
        var diffTodayAllSize : Long = 0
        var diffYesterAllSize : Long = 0
        var diffMonthAllSize : Long = 0
        var diffThreeMonthAllSize : Long = 0
        var diffSixMonthAllSize : Long = 0
        var diffYearDownAllSize : Long = 0
        var diffYearAllSize : Long = 0
        var diffTwoYearAllSize : Long = 0

        for(diff in imageListCategory) {
            if ((0 < (diff.diffTime / Actions.sec) && 60 > (diff.diffTime / Actions.sec)) || (0 <= (diff.diffTime / Actions.min) && 24 > (diff.diffTime / Actions.min))) {
                todayListCategory.add(ImageData(type = diff.type , image = diff.image, imageSize = diff.imageSize, imagePackage = diff.imagePackage, imageName = diff.imageName, imageChoose = diff.imageChoose, diffTime = diff.diffTime))

                diffTodayAllSize += diff.imageSize
                imageCustomToday.setImageSize(Formatter.formatFileSize(context, diffTodayAllSize))
                imageCustomToday.setImageCount(todayListCategory.count().toString() + "개")
            } else if (0 < (diff.diffTime / Actions.hour) && 1 >= (diff.diffTime / Actions.hour)) {
                yesterdayListCategory.add(ImageData(type = diff.type , image = diff.image, imageSize = diff.imageSize, imagePackage = diff.imagePackage, imageName = diff.imageName, imageChoose = diff.imageChoose, diffTime = diff.diffTime))

                diffYesterAllSize += diff.imageSize
                imageCustomYesterday.setImageSize( Formatter.formatFileSize(context, diffYesterAllSize))
                imageCustomYesterday.setImageCount(yesterdayListCategory.count().toString() + "개")
            } else if (30 > (diff.diffTime / Actions.hour)) {
                monthListCategory.add(ImageData(type = diff.type , image = diff.image, imageSize = diff.imageSize, imagePackage = diff.imagePackage, imageName = diff.imageName, imageChoose = diff.imageChoose, diffTime = diff.diffTime))

                diffMonthAllSize += diff.imageSize
                imageCustomMonth.setImageSize(Formatter.formatFileSize(context, diffMonthAllSize))
                imageCustomMonth.setImageCount(monthListCategory.count().toString() + "개")
            } else if (0 < (diff.diffTime / Actions.day) && 3 > (diff.diffTime / Actions.day)) {
                threeMonthListCategory.add(ImageData(type = diff.type , image = diff.image, imageSize = diff.imageSize, imagePackage = diff.imagePackage, imageName = diff.imageName, imageChoose = diff.imageChoose, diffTime = diff.diffTime))

                diffThreeMonthAllSize += diff.imageSize
                imageCustomThreeMonth.setImageSize(Formatter.formatFileSize(context, diffThreeMonthAllSize))
                imageCustomThreeMonth.setImageCount(threeMonthListCategory.count().toString() + "개")
            } else if (3 <= (diff.diffTime / Actions.day) && 6 > (diff.diffTime / Actions.day)) {
                sixMonthListCategory.add(ImageData(type = diff.type , image = diff.image, imageSize = diff.imageSize, imagePackage = diff.imagePackage, imageName = diff.imageName, imageChoose = diff.imageChoose, diffTime = diff.diffTime))

                diffSixMonthAllSize += diff.imageSize
                imageCustomSixMonth.setImageSize(Formatter.formatFileSize(context, diffSixMonthAllSize))
                imageCustomSixMonth.setImageCount(sixMonthListCategory.count().toString() + "개")
            } else if (6 <= (diff.diffTime / Actions.day) && 12 > (diff.diffTime / Actions.day)) {
                yearDownListCategory.add(ImageData(type = diff.type , image = diff.image, imageSize = diff.imageSize, imagePackage = diff.imagePackage, imageName = diff.imageName, imageChoose = diff.imageChoose, diffTime = diff.diffTime))

                diffYearDownAllSize += diff.imageSize
                imageCustomYearDown.setImageSize(Formatter.formatFileSize(context, diffYearDownAllSize))
                imageCustomYearDown.setImageCount(yearDownListCategory.count().toString() + "개")
            } else if (2 > (diff.diffTime / Actions.month)) {
                yearListCategory.add(ImageData(type = diff.type , image = diff.image, imageSize = diff.imageSize, imagePackage = diff.imagePackage, imageName = diff.imageName, imageChoose = diff.imageChoose, diffTime = diff.diffTime))

                diffYearAllSize += diff.imageSize
                imageCustomYear.setImageSize(Formatter.formatFileSize(context, diffYearAllSize))
                imageCustomYear.setImageCount(yearListCategory.count().toString() + "개")
            } else {
                twoYearListCategory.add(ImageData(type = diff.type , image = diff.image, imageSize = diff.imageSize, imagePackage = diff.imagePackage, imageName = diff.imageName, imageChoose = diff.imageChoose, diffTime = diff.diffTime))

                diffTwoYearAllSize += diff.imageSize
                imageCustomTwoYear.setImageSize(Formatter.formatFileSize(context, diffTwoYearAllSize))
                imageCustomTwoYear.setImageCount(twoYearListCategory.count().toString() + "개")
            }
        }
 //////////////////////////////////////////////////////////////////////////////////////////////

        val recyclerToday = findViewById<RecyclerView>(R.id.image_Today_recycler)
        dpAdjustment.setScale(recyclerToday.layoutParams, 365f,365f)

        recyclerToday.layoutManager = GridLayoutManager(this, 3)
        if (todayListCategory.isEmpty()) {
            imageCustomToday.visibility = View.GONE
            todayLine.visibility=View.GONE
        } else {
            recyclerToday.adapter = todayAdapter
        }

        val todayAllCheck = View.OnClickListener{
            boolean = if (!boolean) {
                todayAdapter.setCheckAll(false, position)
                RepositoryImpl.getImageList().clear()
                RepositoryImpl.getToday().clear()
                true
            } else {
                todayAdapter.setCheckAll(true, position)
                false
            }
        }

        imageCustomToday.setCheckListener(todayAllCheck)

   //////////////////////////////////////////////////////////////////////////////////////////

        val recyclerYesterday = findViewById<RecyclerView>(R.id.image_Yesterday_recycler)
        dpAdjustment.setScale(recyclerYesterday.layoutParams, 365f,365f)

        recyclerYesterday.layoutManager = GridLayoutManager(this, 3)
        if (yesterdayListCategory.isEmpty()) {
            imageCustomYesterday.visibility = View.GONE
            yesterdayLine.visibility=View.GONE

        } else {
            recyclerYesterday.adapter = yesterdayAdapter
        }

        val yesterdayAllCheck = View.OnClickListener{
            boolean = if (!boolean) {
                yesterdayAdapter.setCheckAll(false, position)
                RepositoryImpl.getImageList().clear()
                RepositoryImpl.getYesterday().clear()
                true
            } else {
                yesterdayAdapter.setCheckAll(true, position)
                false
            }
        }

        imageCustomYesterday.setCheckListener(yesterdayAllCheck)
        ///////////////////////////////////////////////////////////////////////////////////
        val recyclerMonth = findViewById<RecyclerView>(R.id.image_Month_recycler)
        dpAdjustment.setScale(recyclerMonth.layoutParams, 365f,365f)

        recyclerMonth.layoutManager = GridLayoutManager(this, 3)
        if (monthListCategory.isEmpty()) {
            imageCustomMonth.visibility = View.GONE
            monthLine.visibility=View.GONE

        } else {
            recyclerMonth.adapter = monthAdapter
        }

        val monthAllCheck = View.OnClickListener{
            boolean = if (!boolean) {
                monthAdapter.setCheckAll(false, position)
                RepositoryImpl.getImageList().clear()
                RepositoryImpl.getMonth().clear()
                true
            } else {
                monthAdapter.setCheckAll(true, position)
                false
            }
        }

        imageCustomMonth.setCheckListener(monthAllCheck)
        /////////////////////////////////////////////////////////////////////////////////
        val recyclerThreeMonth = findViewById<RecyclerView>(R.id.image_threeMonth_recycler)
        dpAdjustment.setScale(recyclerThreeMonth.layoutParams, 365f,365f)
        recyclerThreeMonth.layoutManager = GridLayoutManager(this, 3)
        if (threeMonthListCategory.isEmpty()) {
            imageCustomThreeMonth.visibility = View.GONE
            threeMonthLine.visibility=View.GONE

        } else {
            recyclerThreeMonth.adapter = threeMonthAdapter
        }

        val threeMonthAllCheck = View.OnClickListener{
            boolean = if (!boolean) {
                threeMonthAdapter.setCheckAll(false, position)
                RepositoryImpl.getImageList().clear()
                RepositoryImpl.getThreeMonth().clear()
                true
            } else {
                threeMonthAdapter.setCheckAll(true, position)
                false
            }
        }

        imageCustomThreeMonth.setCheckListener(threeMonthAllCheck)
        ///////////////////////////////////////////////////////////////////////////////
        val recyclerSixMonth = findViewById<RecyclerView>(R.id.image_sixMonth_recycler)
        dpAdjustment.setScale(recyclerSixMonth.layoutParams, 365f,365f)
        recyclerSixMonth.layoutManager = GridLayoutManager(this, 3)
        if (sixMonthListCategory.isEmpty()) {
            imageCustomSixMonth.visibility = View.GONE
            sixMonthLine.visibility=View.GONE

        } else {
            recyclerSixMonth.adapter = sixMonthAdapter
        }

        val sixMonthAllCheck = View.OnClickListener{
            boolean = if (!boolean) {
                sixMonthAdapter.setCheckAll(false, position)
                RepositoryImpl.getImageList().clear()
                RepositoryImpl.getSixMonth().clear()
                true
            } else {
                sixMonthAdapter.setCheckAll(true, position)
                false
            }
        }

        imageCustomSixMonth.setCheckListener(sixMonthAllCheck)
        //////////////////////////////////////////////////////////////////////////////

        val recyclerYearDown = findViewById<RecyclerView>(R.id.image_YearDown_recycler)
        dpAdjustment.setScale(recyclerYearDown.layoutParams, 365f,365f)
        recyclerYearDown.layoutManager = GridLayoutManager(this, 3)
        if (yearDownListCategory.isEmpty()) {
            imageCustomYearDown.visibility = View.GONE
            yearDownLine.visibility=View.GONE

        } else {
            recyclerYearDown.adapter = yearDownAdapter
        }

        val yearDownAllCheck = View.OnClickListener{
            boolean = if (!boolean) {
                yearDownAdapter.setCheckAll(false, position)
                RepositoryImpl.getImageList().clear()
                RepositoryImpl.getYearDown().clear()
                true
            } else {
                yearDownAdapter.setCheckAll(true, position)
                false
            }
        }

        imageCustomYearDown.setCheckListener(yearDownAllCheck)
        //////////////////////////////////////////////////////////////////////////////////
        val recyclerYear = findViewById<RecyclerView>(R.id.image_Year_recycler)
        dpAdjustment.setScale(recyclerYear.layoutParams, 365f,365f)
        recyclerYear.layoutManager = GridLayoutManager(this, 3)
        if (yearListCategory.isEmpty()) {
            imageCustomYear.visibility = View.GONE
            yearLine.visibility=View.GONE

        } else {
            recyclerYear.adapter = yearAdapter
        }

        val yearAllCheck = View.OnClickListener{
            boolean = if (!boolean) {
                yearAdapter.setCheckAll(false, position)
                RepositoryImpl.getImageList().clear()
                RepositoryImpl.getYear().clear()
                true
            } else {
                yearAdapter.setCheckAll(true, position)
                false
            }
        }

        imageCustomYear.setCheckListener(yearAllCheck)
        //////////////////////////////////////////////////////////////////////////////

        val recyclerTwoYear = findViewById<RecyclerView>(R.id.image_twoYear_recycler)
        dpAdjustment.setScale(recyclerTwoYear.layoutParams, 365f,365f)
        recyclerTwoYear.layoutManager = GridLayoutManager(this, 3)
        if (twoYearListCategory.isEmpty()) {
            imageCustomTwoYear.visibility = View.GONE
            twoYearLine.visibility=View.GONE

        } else {
            recyclerTwoYear.adapter = twoYearAdapter
        }
        val twoYearAllCheck = View.OnClickListener{
            boolean = if (!boolean) {
                twoYearAdapter.setCheckAll(false, position)
                RepositoryImpl.getImageList().clear()
                RepositoryImpl.getTwoYear().clear()

                true
            } else {
                twoYearAdapter.setCheckAll(true, position)
                false
            }
        }

        imageCustomTwoYear.setCheckListener(twoYearAllCheck)
    }





    private fun getAllImagePath() {

        val externalStorage: Uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        val indexID: Int
        var imageID: Long
        val cursor = contentResolver.query(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            null,
            null,
            null,
            MediaStore.Images.ImageColumns.DATE_TAKEN
        )

        val time = cursor?.getColumnIndexOrThrow(MediaStore.Images.Media.DATE_TAKEN)

        if (cursor != null) {
            indexID = cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID)
            while (cursor.moveToNext()) {
                imageID = cursor.getLong(indexID)

                uriImage = Uri.withAppendedPath(externalStorage, "" + imageID)

                val size = cursor.getColumnIndex(MediaStore.Images.Media.SIZE)
                val imageSize = cursor.getLong(size)
                val imageTime = cursor.getLong(time!!)

                val calendar = Calendar.getInstance()
                calendar.timeInMillis = imageTime

                val today = System.currentTimeMillis()
                diffTime = (today - imageTime) / 1000

                val count = arrayListOf(imageID).count()
                val imageSizeList = arrayListOf(imageSize).sum()
                imageAllSize += imageSizeList
                imageAllCount += count

                imageListCategory.add(
                    ImageData(
                        type = TYPE_IMAGE,
                        image = uriImage!!,
                        imageSize = imageSize,
                        imageName = imageID.toString(),
                        imagePackage = TYPE_IMAGE_PACKAGE,
                        imageChoose = false,
                        diffTime = diffTime,
                    )
                )
            }
        }

    }
    fun today(){
        todayCheckboxList= RepositoryImpl.getToday()
        if(todayListCategory.isNotEmpty()){
            val checkItr = todayCheckboxList.iterator()

            while (checkItr.hasNext()) {
                val checkListItr = checkItr.next()
                val checkImageItr = todayListCategory.iterator()
                while (checkImageItr.hasNext()) {
                    val checkImageListItr = checkImageItr.next()
                    if (checkListItr.galleryName == checkImageListItr.imageName) {
                        context.contentResolver?.openFileDescriptor(
                            checkImageListItr.image,
                            "w",
                            null
                        ).use {
                            checkImageListItr.image.let {
                                contentResolver.delete(
                                    checkImageListItr.image,
                                    null,
                                    null
                                )
                                Toast.makeText(this, "파일을 삭제 했습니다.", Toast.LENGTH_LONG).show()
                            }


                        }
                        checkImageItr.remove()
                        todayAdapter.set(todayListCategory)

                    }
                }
                checkItr.remove()
            }
        }
        }

    fun yesterday(){
        yesterdayCheckboxList = RepositoryImpl.getYesterday()
        if(yesterdayListCategory.isNotEmpty()){
            val checkItr = yesterdayCheckboxList.iterator()

            while (checkItr.hasNext()) {
                val checkListItr = checkItr.next()
                val checkImageItr = yesterdayListCategory.iterator()
                while (checkImageItr.hasNext()) {
                    val checkImageListItr = checkImageItr.next()
                    if (checkListItr.galleryName == checkImageListItr.imageName) {
                        context.contentResolver?.openFileDescriptor(
                            checkImageListItr.image,
                            "w",
                            null
                        ).use {
                            checkImageListItr.image.let {
                                contentResolver.delete(
                                    checkImageListItr.image,
                                    null,
                                    null
                                )
                                Toast.makeText(this, "파일을 삭제 했습니다.", Toast.LENGTH_LONG).show()
                            }


                        }
                        checkImageItr.remove()
                        yesterdayAdapter.set(yesterdayListCategory)

                    }
                }
                checkItr.remove()
            }
        }


    }
    fun month(){
        monthCheckboxList = RepositoryImpl.getMonth()
        if(monthListCategory.isNotEmpty()) {
            val checkItr = monthCheckboxList.iterator()

            while (checkItr.hasNext()) {
                val checkListItr = checkItr.next()
                val checkImageItr = monthListCategory.iterator()
                while (checkImageItr.hasNext()) {
                    val checkImageListItr = checkImageItr.next()
                    if (checkListItr.galleryName == checkImageListItr.imageName) {
                        context.contentResolver?.openFileDescriptor(
                            checkImageListItr.image,
                            "w",
                            null
                        ).use {
                            checkImageListItr.image.let {
                                contentResolver.delete(
                                    checkImageListItr.image,
                                    null,
                                    null
                                )
                                Toast.makeText(this, "파일을 삭제 했습니다.", Toast.LENGTH_LONG).show()
                            }
                        }
                        checkImageItr.remove()
                        monthAdapter.set(monthListCategory)
                    }
                }
                checkItr.remove()
            }
        }
    }
    fun threeMonth (){
        threeMonthCheckboxList = RepositoryImpl.getThreeMonth()
        if(threeMonthListCategory.isNotEmpty()){
            val checkItr = threeMonthCheckboxList.iterator()

            while (checkItr.hasNext()) {
                val checkListItr = checkItr.next()
                val checkImageItr = threeMonthListCategory.iterator()
                while (checkImageItr.hasNext()) {
                    val checkImageListItr = checkImageItr.next()
                    if (checkListItr.galleryName == checkImageListItr.imageName) { context.contentResolver?.openFileDescriptor(checkImageListItr.image, "w", null).use {
                        checkImageListItr.image.let { contentResolver.delete(checkImageListItr.image, null, null)
                            Toast.makeText(this, "파일을 삭제 했습니다.", Toast.LENGTH_LONG).show()
                        }

                    }
                        checkImageItr.remove()
                        threeMonthAdapter.set(threeMonthListCategory)
                    }
                }
                checkItr.remove()
            }

        }

    }
    fun sixMonth(){
        sixMonthCheckboxList = RepositoryImpl.getSixMonth()
        if(sixMonthListCategory.isNotEmpty()){
            val checkItr = sixMonthCheckboxList.iterator()

            while (checkItr.hasNext()) {
                val checkListItr = checkItr.next()
                val checkImageItr = sixMonthListCategory.iterator()
                while (checkImageItr.hasNext()) {
                    val checkImageListItr = checkImageItr.next()
                    if (checkListItr.galleryName == checkImageListItr.imageName) {
                        context.contentResolver?.openFileDescriptor(
                            checkImageListItr.image,
                            "w",
                            null
                        ).use {
                            checkImageListItr.image.let {
                                contentResolver.delete(
                                    checkImageListItr.image,
                                    null,
                                    null
                                )
                                Toast.makeText(this, "파일을 삭제 했습니다.", Toast.LENGTH_LONG).show()
                            }


                        }
                        checkImageItr.remove()

                    }
                }
                checkItr.remove()
                sixMonthAdapter.set(sixMonthListCategory)
            }

        }

    }
    fun yearDown(){
        yearDownCheckboxList = RepositoryImpl.getYearDown()
        if(yearDownListCategory.isNotEmpty()){
            val checkItr = yearDownCheckboxList.iterator()

            while (checkItr.hasNext()) {
                val checkListItr = checkItr.next()
                val checkImageItr = yearDownListCategory.iterator()
                while (checkImageItr.hasNext()) {
                    val checkImageListItr = checkImageItr.next()
                    if (checkListItr.galleryName == checkImageListItr.imageName) {
                        context.contentResolver?.openFileDescriptor(
                            checkImageListItr.image,
                            "w",
                            null
                        ).use {
                            checkImageListItr.image.let {
                                contentResolver.delete(
                                    checkImageListItr.image,
                                    null,
                                    null
                                )
                                Toast.makeText(this, "파일을 삭제 했습니다.", Toast.LENGTH_LONG).show()
                            }


                        }
                        checkImageItr.remove()
                        yearDownAdapter.set(yearDownListCategory)

                    }
                }
                checkItr.remove()
            }
        }

    }
    fun year(){
        yearCheckboxList = RepositoryImpl.getYear()
        if(yearListCategory.isNotEmpty()){
            val checkItr = yearCheckboxList.iterator()

            while (checkItr.hasNext()) {
                val checkListItr = checkItr.next()
                val checkImageItr = yearListCategory.iterator()
                while (checkImageItr.hasNext()) {
                    val checkImageListItr = checkImageItr.next()
                    if (checkListItr.galleryName == checkImageListItr.imageName) {
                        context.contentResolver?.openFileDescriptor(
                            checkImageListItr.image,
                            "w",
                            null
                        ).use {
                            checkImageListItr.image.let {
                                contentResolver.delete(
                                    checkImageListItr.image,
                                    null,
                                    null
                                )
                                Toast.makeText(this, "파일을 삭제 했습니다.", Toast.LENGTH_LONG).show()
                            }
                        }
                        checkImageItr.remove()
                        yearAdapter.set(yearListCategory)

                    }
                }
                checkItr.remove()
            }
        }

    }
    fun twoYear (){
        twoYearCheckboxList = RepositoryImpl.getTwoYear()
        if(twoYearListCategory.isNotEmpty()){
            val checkItr = twoYearCheckboxList.iterator()

            while (checkItr.hasNext()) {
                val checkListItr = checkItr.next()
                val checkImageItr = twoYearListCategory.iterator()
                while (checkImageItr.hasNext()) {
                    val checkImageListItr = checkImageItr.next()
                    if (checkListItr.galleryName == checkImageListItr.imageName) {
                        context.contentResolver?.openFileDescriptor(
                            checkImageListItr.image,
                            "w",
                            null
                        ).use {
                            checkImageListItr.image.let {
                                contentResolver.delete(
                                    checkImageListItr.image,
                                    null,
                                    null
                                )
                                Toast.makeText(this, "파일을 삭제 했습니다.", Toast.LENGTH_LONG).show()
                            }


                        }
                        checkImageItr.remove()
                        twoYearAdapter.set(twoYearListCategory)
                    }
                }
                checkItr.remove()
            }
        }
    }


    private fun selectRemoveImage() {
        finish()
        overridePendingTransition(0,0)
        val intent :Intent= intent
        startActivity(intent)
        overridePendingTransition(0,0)
        checkboxList = RepositoryImpl.getImageList()

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

