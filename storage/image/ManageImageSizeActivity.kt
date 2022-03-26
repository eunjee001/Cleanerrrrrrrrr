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
import com.kyoungss.cleaner.Actions
import com.kyoungss.cleaner.DisplayAdjustment
import com.kyoungss.cleaner.expand.Expandable
import com.kyoungss.cleaner.R
import com.kyoungss.cleaner.data.RepositoryImpl
import com.kyoungss.cleaner.storage.CheckStorageData
import kotlin.collections.ArrayList

class ManageImageSizeActivity : AppCompatActivity() {
    var context: Context = this
    private lateinit var dpAdjustment: DisplayAdjustment

    private val twoGBListCategory = ArrayList<ImageData>()
    private val oneGBListCategory = ArrayList<ImageData>()
    private val oneGBDownListCategory = ArrayList<ImageData>()
    private val fiveMBListCategory = ArrayList<ImageData>()
    private val oneMBListCategory = ArrayList<ImageData>()
    private val imageListCategory = ArrayList<ImageData>()
    
    private var removeImage: Button? = null
    private lateinit var allSize: TextView
    private lateinit var allCount: TextView
    private var imageAdapter = ImageSizeAdapter(imageListCategory, context)
    private var oneMBDownAdapter = ImageSizeAdapter(oneMBListCategory, context)
    private var fiveMBDownAdapter = ImageSizeAdapter(fiveMBListCategory, context)
    private var oneGBDownAdapter = ImageSizeAdapter(oneGBDownListCategory, context)
    private var oneGBAdapter = ImageSizeAdapter(oneGBListCategory, context)
    private var twoGBAdapter = ImageSizeAdapter(twoGBListCategory, context)

    private var twoGBCheckboxList = ArrayList<CheckStorageData>()
    private var oneGBCheckboxList = ArrayList<CheckStorageData>()
    private var oneGBDownCheckboxList = ArrayList<CheckStorageData>()
    private var fiveMBDownCheckboxList = ArrayList<CheckStorageData>()
    private var oneMBDownCheckboxList = ArrayList<CheckStorageData>()
    private var checkboxList = ArrayList<CheckImageData>()
    var popupWindow : PopupWindow ?= null

    var imageAllSize: Long = 0
    var imageAllCount: Int = 0

    var uriImage: Uri? = null
    var diffTime: Long = 0
    var imageSize: Long = 0
    var selectBtn: CheckBox? = null

    companion object {
        const val TYPE_IMAGE = 0L
        const val TYPE_IMAGE_PACKAGE = 0x01
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image_size)

        val menuBar = findViewById<Button>(R.id.menu)
        val view: View = layoutInflater.inflate(R.layout.popup_gallery_layout, null)

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
            val intent = Intent(this, ManageImageActivity::class.java)
            this.startActivity(intent)

            finish()
        }

        val secondsPopupTextView = view.findViewById<TextView>(R.id.popup_text_second)
        secondsPopupTextView.setOnClickListener {
            val intent = Intent(this, ManageImageSizeActivity::class.java)
            this.startActivity(intent)

            finish()
        }
        getAllImagePath()

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

        val imageBack = findViewById<Button>(R.id.imageBack)
        dpAdjustment.setMargins(imageBack,16,16,0,0 )
        dpAdjustment.setScale(imageBack.layoutParams,24f,24f)

        val imageTitle = findViewById<TextView>(R.id.imageTitle)
        dpAdjustment.setMargins(imageTitle, 12,12,0,0)

        allSize = findViewById(R.id.infoTitle)
        dpAdjustment.setMargins(allSize, 16,33,0,0)
        allSize.text = Formatter.formatFileSize(context, imageAllSize)

        allCount = findViewById(R.id.infoTitleCount)
        dpAdjustment.setMargins(allCount, 14,34,0,0)
        allCount.text = imageAllCount.toString() + "개"

        val imageMenu = findViewById<Button>(R.id.menu)
        dpAdjustment.setMargins(imageMenu, 0,31,19,0)
        dpAdjustment.setScale(imageMenu.layoutParams, 24f,24f)

        ///////////////////////twoGB////////////////////////
        val imageCustomTwoGB = findViewById<ImageSizeCustomView>(R.id.image_twoGB)
        dpAdjustment.setMargins(imageCustomTwoGB, 0,0,0,0)
        dpAdjustment.setScale(imageCustomTwoGB.layoutParams, 360f,56f)

        val expandTwoGB = findViewById<Expandable>(R.id.image_twoGB_expand)
         val twoGBLine = findViewById<View>(R.id.line_twoGB)
        dpAdjustment.setMargins(twoGBLine, 0,0,0,0)
        dpAdjustment.setScale(twoGBLine.layoutParams, 360f,5f)

        //////////////////////////////////oneGB////////////////////////////
        val imageCustomOneGB = findViewById<ImageSizeCustomView>(R.id.image_oneGB)
        dpAdjustment.setMargins(imageCustomOneGB, 0,0,0,0)
        dpAdjustment.setScale(imageCustomOneGB.layoutParams, 360f,56f)

        val expandOneGB = findViewById<Expandable>(R.id.image_oneGB_expand)

        val oneGBLine = findViewById<View>(R.id.line_oneGB)
        dpAdjustment.setMargins(oneGBLine, 0,0,0,0)
        dpAdjustment.setScale(oneGBLine.layoutParams, 360f,5f)

        /////////////////////////oneGBDown//////////////////////
        val imageCustomOneGBDown = findViewById<ImageSizeCustomView>(R.id.image_oneGB_Down)
        dpAdjustment.setMargins(imageCustomOneGBDown, 0,0,0,0)
        dpAdjustment.setScale(imageCustomOneGBDown.layoutParams, 360f,56f)

        val expandOneGBDown = findViewById<Expandable>(R.id.image_oneGB_Down_expand)

        val oneGBDownLine = findViewById<View>(R.id.line_oneGB_Down)
        dpAdjustment.setMargins(oneGBDownLine, 0,0,0,0)
        dpAdjustment.setScale(oneGBDownLine.layoutParams, 360f,5f)

        ////////////////////////////500MB Down///////////////////////////
        val imageCustomFiveMBDown = findViewById<ImageSizeCustomView>(R.id.image_500MB_Down)
        dpAdjustment.setMargins(imageCustomFiveMBDown, 0,0,0,0)
        dpAdjustment.setScale(imageCustomFiveMBDown.layoutParams, 360f,56f)

        val expandFiveMBDown = findViewById<Expandable>(R.id.image_500MB_Down_expand)

        val fiveMBDownLine = findViewById<View>(R.id.line_500MB_Down)
        dpAdjustment.setMargins(fiveMBDownLine, 0,0,0,0)
        dpAdjustment.setScale(fiveMBDownLine.layoutParams, 360f,5f)

        //////////////////////////100MB Down//////////////////////////////
        val imageCustomOneMBDown = findViewById<ImageSizeCustomView>(R.id.image_100MB_Down)
        dpAdjustment.setMargins(imageCustomOneMBDown, 0,0,0,0)
        dpAdjustment.setScale(imageCustomOneMBDown.layoutParams, 360f,56f)

        val expandOneMBDown = findViewById<Expandable>(R.id.image_100MB_Down_expand)

        val oneMBDownLine = findViewById<View>(R.id.line_100MB_Down)
        dpAdjustment.setMargins(oneMBDownLine, 0,0,0,0)
        dpAdjustment.setScale(oneMBDownLine.layoutParams, 360f,5f)

        ///////////////////////////////////////////////////////
        removeImage = findViewById(R.id.image_size_btn)
        dpAdjustment.setScale(removeImage!!.layoutParams, 328f,48f)
        dpAdjustment.setMargins(removeImage!!, 16,0,0,16)

        removeImage?.setOnClickListener {
            selectRemoveAudio()
        }
        selectBtn = findViewById(R.id.grid_selected)

        var boolean = false
        var position = 0


        val listenerTwoGB = View.OnClickListener {
            boolean = if (!boolean) {
                expandTwoGB.expand()
                imageCustomTwoGB.setImageMenu(R.drawable.icon_menu_drop_up)

                true
            } else {
                expandTwoGB.collapse()
                imageCustomTwoGB.setImageMenu(R.drawable.icon_menu_drop_down)

                false
            }
        }

        imageCustomTwoGB.setListener(listenerTwoGB)

        /////////////////////////////////////////////////////////////
        /////////////////////////////////////////////////////////////
        /////////////////////////////////////////////////////////////

        val listenerOneGB = View.OnClickListener {
            boolean = if (!boolean) {
                expandOneGB.expand()
                imageCustomOneGB.setImageMenu(R.drawable.icon_menu_drop_up)

                true
            } else {
                expandOneGB.collapse()
                imageCustomOneGB.setImageMenu(R.drawable.icon_menu_drop_down)

                false
            }
        }
        imageCustomOneGB.setListener(listenerOneGB)

        /////////////////////////////////////////////////////////////
        /////////////////////////////////////////////////////////////
        /////////////////////////////////////////////////////////////

        val listenerOneGBDown = View.OnClickListener {
            boolean = if (!boolean) {
                expandOneGBDown.expand()
                imageCustomOneGBDown.setImageMenu(R.drawable.icon_menu_drop_up)

                true
            } else {
                expandOneGBDown.collapse()
                imageCustomOneGBDown.setImageMenu(R.drawable.icon_menu_drop_down)

                false
            }
        }
        imageCustomOneGBDown.setListener(listenerOneGBDown)



        /////////////////////////////////////////////////////////////
        /////////////////////////////////////////////////////////////
        /////////////////////////////////////////////////////////////

        val listenerFiveMBDown = View.OnClickListener {
            boolean = if (!boolean) {
                expandFiveMBDown.expand()
                imageCustomFiveMBDown.setImageMenu(R.drawable.icon_menu_drop_up)

                true
            } else {
                expandFiveMBDown.collapse()
                imageCustomFiveMBDown.setImageMenu(R.drawable.icon_menu_drop_down)

                false
            }
        }
        imageCustomFiveMBDown.setListener(listenerFiveMBDown)

        /////////////////////////////////////////////////////////////
        /////////////////////////////////////////////////////////////
        /////////////////////////////////////////////////////////////

        val listenerOneMBDown = View.OnClickListener {
            boolean = if (!boolean) {
                expandOneMBDown.expand()
                imageCustomOneMBDown.setImageMenu(R.drawable.icon_menu_drop_up)

                true
            } else {
                expandOneMBDown.collapse()
                imageCustomOneMBDown.setImageMenu(R.drawable.icon_menu_drop_down)

                false
            }
        }
        imageCustomOneMBDown.setListener(listenerOneMBDown)
        var diffOneMbSize :Long =0
        var diffFiveMbSize :Long =0
        var diffOneGbDownSize :Long =0
        var diffOneGbSize :Long =0
        var diffTwoGbSize :Long =0
        for(diff in imageListCategory) {
            if (0 < (diff.imageSize) && 100 > (diff.imageSize / Actions.MB)) {
                oneMBListCategory.add(ImageData(type = diff.type , image = diff.image, imageSize = diff.imageSize, imagePackage = diff.imagePackage, imageName = diff.imageName, imageChoose = diff.imageChoose, diffTime = diff.diffTime))
                diffOneMbSize += diff.imageSize
                imageCustomOneMBDown.setImageSizeAll(Formatter.formatFileSize(context, diffOneMbSize))
                imageCustomOneMBDown.setImageCount(imageListCategory.count().toString() + "개")
            } else if (100 <= (diff.imageSize / Actions.MB) && 500 > (diff.imageSize / Actions.MB)) {
                fiveMBListCategory.add(ImageData(type = diff.type , image = diff.image, imageSize = diff.imageSize, imagePackage = diff.imagePackage, imageName = diff.imageName, imageChoose = diff.imageChoose, diffTime = diff.diffTime))
                diffFiveMbSize += diff.imageSize
                imageCustomFiveMBDown.setImageSizeAll(Formatter.formatFileSize(context, diffFiveMbSize))
                imageCustomFiveMBDown.setImageCount(fiveMBListCategory.count().toString() + "개")
            } else if (500 <= (diff.imageSize / Actions.MB) && 1 > (diff.imageSize / Actions.GB)) {
                oneGBDownListCategory.add(ImageData(type = diff.type , image = diff.image, imageSize = diff.imageSize, imagePackage = diff.imagePackage, imageName = diff.imageName, imageChoose = diff.imageChoose, diffTime = diff.diffTime))
                diffOneGbDownSize += diff.imageSize
                imageCustomOneGBDown.setImageSizeAll(Formatter.formatFileSize(context, diffOneGbDownSize))
                imageCustomOneGBDown.setImageCount(oneGBDownListCategory.count().toString() + "개")
            } else if (1 <= (diff.imageSize / Actions.GB) && 2 > (diff.imageSize / Actions.GB)) {
                oneGBListCategory.add(ImageData(type = diff.type , image = diff.image, imageSize = diff.imageSize, imagePackage = diff.imagePackage, imageName = diff.imageName, imageChoose = diff.imageChoose, diffTime = diff.diffTime))
                diffOneGbSize += diff.imageSize
                imageCustomOneGB.setImageSizeAll(Formatter.formatFileSize(context, diffOneGbSize))
                imageCustomOneGB.setImageCount(oneGBListCategory.count().toString() + "개")
            } else if (2 <= (diff.imageSize / Actions.GB)) {
                twoGBListCategory.add(ImageData(type = diff.type , image = diff.image, imageSize = diff.imageSize, imagePackage = diff.imagePackage, imageName = diff.imageName, imageChoose = diff.imageChoose, diffTime = diff.diffTime))
                diffTwoGbSize += diff.imageSize
                imageCustomTwoGB.setImageSizeAll(Formatter.formatFileSize(context, diffTwoGbSize))
                imageCustomTwoGB.setImageCount(twoGBListCategory.count().toString() + "개")
            }
        }
        //////////////////////////////////////////////////////////////////////////////////
        val recyclerOneMBDown = findViewById<RecyclerView>(R.id.image_100MB_Down_recycler)
        recyclerOneMBDown.layoutManager = GridLayoutManager(this, 3)

        dpAdjustment.setScale(recyclerOneMBDown.layoutParams, 365f,365f)
        if (oneMBListCategory.isEmpty()) {
            imageCustomOneMBDown.visibility = View.GONE
            oneMBDownLine.visibility = View.GONE

        } else {
            recyclerOneMBDown.adapter = oneMBDownAdapter
        }

        val oneMBDownAllCheck = View.OnClickListener{
            boolean = if (!boolean) {
                oneMBDownAdapter.setCheckAll(false, position)
                RepositoryImpl.getImageList().clear()
                RepositoryImpl.getOneMB().clear()
                true
            } else {
                oneMBDownAdapter.setCheckAll(true, position)
                false
            }
        }

        imageCustomOneMBDown.setCheckListener(oneMBDownAllCheck)
        /////////////////////////////////////////////////////////////////////////////////
        val recyclerFiveMBDown = findViewById<RecyclerView>(R.id.image_500MB_Down_recycler)
        dpAdjustment.setScale(recyclerFiveMBDown.layoutParams, 365f,365f)
        recyclerFiveMBDown.layoutManager = GridLayoutManager(this, 3)
        if (fiveMBListCategory.isEmpty()) {
            imageCustomFiveMBDown.visibility = View.GONE
            fiveMBDownLine.visibility=View.GONE

        } else {
            recyclerFiveMBDown.adapter = fiveMBDownAdapter
        }

        val fiveMBDownAllCheck = View.OnClickListener{
            boolean = if (!boolean) {
                fiveMBDownAdapter.setCheckAll(false, position)
                RepositoryImpl.getImageList().clear()
                RepositoryImpl.getFiveMB().clear()
                true
            } else {
                fiveMBDownAdapter.setCheckAll(true, position)
                false
            }
        }

        imageCustomFiveMBDown.setCheckListener(fiveMBDownAllCheck)

        ////////////////////////////////////////////////////////////////////////////////////
        val recyclerOneGBDown = findViewById<RecyclerView>(R.id.image_oneGB_Down_recycler)
        recyclerOneGBDown.layoutManager = GridLayoutManager(this, 3)
        dpAdjustment.setScale(recyclerOneGBDown.layoutParams, 365f,365f)
        if (oneGBDownListCategory.isEmpty()) {
            imageCustomOneGBDown.visibility = View.GONE
            oneGBDownLine.visibility=View.GONE

        } else {
            recyclerOneGBDown.adapter = oneGBDownAdapter
        }

        val oneGBDownAllCheck = View.OnClickListener{
            boolean = if (!boolean) {
                oneGBDownAdapter.setCheckAll(false, position)
                RepositoryImpl.getImageList().clear()
                RepositoryImpl.getOneGBDown().clear()
                true
            } else {
                oneGBDownAdapter.setCheckAll(true, position)
                false
            }
        }

        imageCustomOneGBDown.setCheckListener(oneGBDownAllCheck)
        //////////////////////////////////////////////////////////////////////////////////////
        val recyclerOneGB = findViewById<RecyclerView>(R.id.image_oneGB_recycler)
        recyclerOneGB.layoutManager = GridLayoutManager(this, 3)
        dpAdjustment.setScale(recyclerOneGB.layoutParams, 365f,365f)
        if (oneGBListCategory.isEmpty()) {
            imageCustomOneGB.visibility = View.GONE
            oneGBLine.visibility=View.GONE

        } else {
            recyclerOneGB.adapter = oneGBAdapter
        }

        val oneGBAllCheck = View.OnClickListener{
            boolean = if (!boolean) {
                oneGBAdapter.setCheckAll(false, position)
                RepositoryImpl.getImageList().clear()
                RepositoryImpl.getOneGB().clear()
                true
            } else {
                oneGBAdapter.setCheckAll(true, position)
                false
            }
        }

        imageCustomOneGB.setCheckListener(oneGBAllCheck)
        /////////////////////////////////////////////////////////////////////////////////////
        val recyclerTwoGB = findViewById<RecyclerView>(R.id.image_twoGB_recycler)
        recyclerTwoGB.layoutManager = GridLayoutManager(this, 3)
        dpAdjustment.setScale(recyclerTwoGB.layoutParams, 365f,365f)
        if (twoGBListCategory.isEmpty()) {
            imageCustomTwoGB.visibility = View.GONE
            twoGBLine.visibility=View.GONE

        } else {
            recyclerTwoGB.adapter = twoGBAdapter
        }

        val twoGBAllCheck = View.OnClickListener{
            boolean = if (!boolean) {
                twoGBAdapter.setCheckAll(false, position)
                RepositoryImpl.getImageList().clear()
                RepositoryImpl.getTwoGB().clear()
                true
            } else {
                twoGBAdapter.setCheckAll(true, position)
                false
            }
        }

        imageCustomTwoGB.setCheckListener(twoGBAllCheck)

    }

 

    private fun getAllImagePath() {

        val externalStorage: Uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        var indexID: Int
        var imageID: Long
        val cursor = contentResolver.query(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            null,
            null,
            null,
            MediaStore.Images.ImageColumns.DATE_TAKEN
        )

        if (cursor != null) {
            while (cursor.moveToNext()) {
                indexID = cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID)
                imageID = cursor.getLong(indexID)

                uriImage = Uri.withAppendedPath(externalStorage, "" + imageID)
                val size = cursor.getColumnIndex(MediaStore.Images.Media.SIZE)
                imageSize = cursor.getLong(size)

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
                        diffTime = diffTime
                    )
                )
                RepositoryImpl.setImageAllSizeList(imageListCategory)
            }

        }
    }
    fun oneMB(){
        oneMBDownCheckboxList = RepositoryImpl.getOneMB()

        if(oneMBListCategory.isNotEmpty()){
            val checkItr = oneMBDownCheckboxList.iterator()

            while (checkItr.hasNext()) {
                val checkListItr = checkItr.next()
                val checkImageUriItr = oneMBListCategory.iterator()
                while (checkImageUriItr.hasNext()) {
                    val checkImageUriListItr = checkImageUriItr.next()
                    if (checkListItr.galleryName == checkImageUriListItr.imageName) {
                        context.contentResolver?.openFileDescriptor(
                            checkImageUriListItr.image,
                            "w",
                            null
                        ).use {
                            checkImageUriListItr.image.let {
                                contentResolver.delete(
                                    checkImageUriListItr.image,
                                    null,
                                    null
                                )
                                Toast.makeText(this, "파일을 삭제 했습니다.", Toast.LENGTH_LONG).show()
                            }
                        }
                        checkImageUriItr.remove()
                        oneMBDownAdapter.set(oneMBListCategory)
                    }
                }
                checkItr.remove()
            }
        }
    }

    fun fiveMB(){
        fiveMBDownCheckboxList = RepositoryImpl.getFiveMB()
        if(fiveMBListCategory.isNotEmpty()){
            val checkItr = fiveMBDownCheckboxList.iterator()

            while (checkItr.hasNext()) {
                val checkListItr = checkItr.next()
                val checkImageUriItr = fiveMBListCategory.iterator()
                while (checkImageUriItr.hasNext()) {
                    val checkImageUriListItr = checkImageUriItr.next()
                    println("checkListItr. ${checkListItr.galleryName}, checkImageUriListItr . ${checkImageUriListItr.image}")

                    if (checkListItr.galleryName == checkImageUriListItr.imageName) {
                        context.contentResolver?.openFileDescriptor(
                            checkImageUriListItr.image,
                            "w",
                            null
                        ).use {
                            checkImageUriListItr.image.let {
                                contentResolver.delete(
                                    checkImageUriListItr.image,
                                    null,
                                    null
                                )
                                Toast.makeText(this, "파일을 삭제 했습니다.", Toast.LENGTH_LONG).show()
                            }
                        }
                        checkImageUriItr.remove()
                        fiveMBDownAdapter.set(fiveMBListCategory)

                    }
                }
                checkItr.remove()


            }
        }
    }
    fun oneGBDown(){
        oneGBDownCheckboxList = RepositoryImpl.getOneGBDown()

        if(oneGBDownListCategory.isNotEmpty()){
            val checkItr = oneGBDownCheckboxList.iterator()

            while (checkItr.hasNext()) {
                val checkListItr = checkItr.next()
                val checkImageUriItr = oneGBDownListCategory.iterator()
                while (checkImageUriItr.hasNext()) {
                    val checkImageUriListItr = checkImageUriItr.next()
                    if (checkListItr.galleryName == checkImageUriListItr.imageName) {
                        context.contentResolver?.openFileDescriptor(
                            checkImageUriListItr.image,
                            "w",
                            null
                        ).use {
                            checkImageUriListItr.image.let {
                                contentResolver.delete(
                                    checkImageUriListItr.image,
                                    null,
                                    null
                                )
                                Toast.makeText(this, "파일을 삭제 했습니다.", Toast.LENGTH_LONG).show()
                            }
                        }
                        checkImageUriItr.remove()
                        oneGBDownAdapter.set(oneGBDownListCategory)
                    }
                }
                checkItr.remove()
            }
        }

    }

    fun oneGB(){
        oneGBCheckboxList = RepositoryImpl.getOneGB()
        if(oneGBListCategory.isNotEmpty()){
            val checkItr = oneGBCheckboxList.iterator()

            while (checkItr.hasNext()) {
                val checkListItr = checkItr.next()
                val checkImageUriItr = oneGBListCategory.iterator()
                while (checkImageUriItr.hasNext()) {
                    val checkImageUriListItr = checkImageUriItr.next()
                    if (checkListItr.galleryName == checkImageUriListItr.imageName) {
                        context.contentResolver?.openFileDescriptor(
                            checkImageUriListItr.image,
                            "w",
                            null
                        ).use {
                            checkImageUriListItr.image.let {
                                contentResolver.delete(checkImageUriListItr.image, null, null)
                                Toast.makeText(this, "파일을 삭제 했습니다.", Toast.LENGTH_LONG).show()
                            }
                        }
                        checkImageUriItr.remove()
                        oneGBAdapter.set(oneGBListCategory)

                    }
                }
                checkItr.remove()
            }
        }

    }
    fun twoGB() {
        twoGBCheckboxList = RepositoryImpl.getTwoGB()

        if (twoGBListCategory.isNotEmpty()) {
            val checkItr = twoGBCheckboxList.iterator()
            while (checkItr.hasNext()) {
                val checkListItr = checkItr.next()
                val checkImageUriItr = twoGBListCategory.iterator()
                while (checkImageUriItr.hasNext()) {
                    val checkImageUriListItr = checkImageUriItr.next()
                    if (checkListItr.galleryName == checkImageUriListItr.imageName) {
                        context.contentResolver?.openFileDescriptor(
                            checkImageUriListItr.image,
                            "w",
                            null
                        ).use {
                            checkImageUriListItr.image.let {
                                contentResolver.delete(
                                    checkImageUriListItr.image,
                                    null,
                                    null
                                )
                                Toast.makeText(this, "파일을 삭제 했습니다.", Toast.LENGTH_LONG).show()
                            }
                        }
                        checkImageUriItr.remove()
                        twoGBAdapter.set(twoGBListCategory)

                    }
                }
                checkItr.remove()
            }
        }
    }
    private fun selectRemoveAudio(){
        finish()
        overridePendingTransition(0,0)
        val intent :Intent= intent
        startActivity(intent)
        overridePendingTransition(0,0)
        checkboxList = RepositoryImpl.getImageList()
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