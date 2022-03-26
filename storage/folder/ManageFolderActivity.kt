package com.kyoungss.cleaner.storage.folder

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.os.Environment
import android.os.StatFs
import android.provider.MediaStore
import android.text.format.Formatter
import android.view.View
import android.widget.*

import androidx.appcompat.app.AppCompatActivity
import com.kyoungss.cleaner.DisplayAdjustment
import com.kyoungss.cleaner.R
import com.kyoungss.cleaner.storage.audio.ManageAudioActivity
import com.kyoungss.cleaner.storage.document.ManageDocumentActivity
import com.kyoungss.cleaner.storage.download.ManageDownloadActivity
import com.kyoungss.cleaner.storage.image.ManageImageActivity
import com.kyoungss.cleaner.storage.video.ManageVideoActivity

import java.io.File
import java.util.*

class ManageFolderActivity : AppCompatActivity() {
    var context: Context = this
    private lateinit var dpAdjustment: DisplayAdjustment

    private var folderGalleryIcons: ImageView? = null
    private var folderAudioIcons: ImageView? = null
    private var folderVideoIcons: ImageView? = null
    private var folderDocIcons: ImageView? = null
    private var folderDownIcons: ImageView? = null

    private lateinit var folderNamesImage: TextView
    private var folderUsesImage: TextView? = null
    private lateinit var folderButtonImage: Button
    private lateinit var folderButtonAudio: Button
    private lateinit var folderButtonVideo: Button
    private lateinit var folderButtonDocument: Button
    private lateinit var folderButtonDownload: Button

    private var folderCountsImage: TextView? = null
    private var folderNamesAudio: TextView? = null
    private var folderUsesAudio: TextView? = null
    private var folderCountsAudio: TextView? = null
    private var folderNamesVideo: TextView? = null
    private var folderUsesVideo: TextView? = null
    private var folderCountsVideo: TextView? = null

    private var folderNamesDocument: TextView? = null
    private var folderUsesDocument: TextView? = null
    private var folderCountsDocument: TextView? = null

    private var folderNamesDownload: TextView? = null
    private var folderUsesDownload: TextView? = null
    private var folderCountsDownload: TextView? = null

    var imageValue:Long = 0
    var audioValue:Long = 0
    var videoValue:Long = 0
    var downloadValue:Long = 0
    var documentValue:Long = 0

    var imageValueLong: Long = 0
    var imageAllCount: Int = 0
    var audioValueLong:Long = 0
    var audioAllCount:Int = 0
    var videoValueLong:Long =0
    var videoAllCount = 0
    var documentAllCount = 0
    var documentValueLong:Long = 0
    var downloadAllCount = 0
    var downloadValueLong:Long = 0

    var restValueLong:Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_folder_manage)

        folderAudio()
        folderImage()
        folderVideo()
        folderDocument()
        folderDownload()
        folderRest()
        initViews()
    }

    @SuppressLint("ResourceType")
    private fun initViews() {
        dpAdjustment = DisplayAdjustment.getInstance(this)

        val backBtn = findViewById<Button>(R.id.back)
        dpAdjustment.setMargins(backBtn, 16,16,0,0)

        val folderTitle = findViewById<TextView>(R.id.title)
        dpAdjustment.setMargins(folderTitle,18,12,0,0)

        val littleTitle = findViewById<TextView>(R.id.little_title)
        dpAdjustment.setMargins(littleTitle,16,36,0,0)

        val useSize = findViewById<TextView>(R.id.folder_use_size)
        dpAdjustment.setMargins(useSize,0,36,0,0)

        val useSlash = findViewById<TextView>(R.id.folder_slash)
        dpAdjustment.setMargins(useSlash,0,36,0,0)

        val allSize = findViewById<TextView>(R.id.folder_all_size)
        dpAdjustment.setMargins(allSize,0,36,0,0)

        val useExplain = findViewById<TextView>(R.id.folder_use_explain)
        dpAdjustment.setMargins(useExplain,0,36,16,0)

        val customInfo = findViewById<RelativeLayout>(R.id.custom_info)
        dpAdjustment.setScale(customInfo.layoutParams, 360f,19f)
        dpAdjustment.setMargins(customInfo,0,10,0,0 )


////////////////////////////////////////////////////////////////////////////////////

        val line = findViewById<View>(R.id.line)
        dpAdjustment.setMargins(line,0,20,0,0)
        dpAdjustment.setScale(line.layoutParams,360f,5f)

        val allInfoTitle = findViewById<TextView>(R.id.infoTitle)
        dpAdjustment.setMargins(allInfoTitle,16,20,0,0)

        //////////////// 이미지 //////////////
        val imageLayout = findViewById<RelativeLayout>(R.id.imageLine)
        dpAdjustment.setMargins(imageLayout,16,10,0,0)
        dpAdjustment.setScale(imageLayout.layoutParams,328f,64f)

        folderGalleryIcons = findViewById(R.id.folder_app_icon_first)
        folderGalleryIcons?.setImageResource(R.drawable.icon_gallery)
        dpAdjustment.setMargins(folderGalleryIcons!!,2,14,0,0)
        dpAdjustment.setScale(folderGalleryIcons!!.layoutParams,36f,36f)

        folderNamesImage = findViewById(R.id.folderName_first)
        dpAdjustment.setMargins(folderNamesImage,10,12,0,0)

        folderUsesImage = findViewById(R.id.folderUse_first)
        dpAdjustment.setMargins(folderUsesImage!!,50,2,0,0)

        folderCountsImage = findViewById(R.id.folderCount_first)
        dpAdjustment.setMargins(folderCountsImage!!,10,2,0,0)

        folderButtonImage = findViewById(R.id.manageBtn_first)
        dpAdjustment.setMargins(folderButtonImage,0,20,0,0)
        dpAdjustment.setScale(folderButtonImage.layoutParams,68f,24f)

        folderButtonImage.setOnClickListener {
            val intent = Intent(this, ManageImageActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)

            this.startActivity(intent)

        }

        ///////////////////오디오 //////////////////////////////
        val audioLayout = findViewById<RelativeLayout>(R.id.audioLine)
        dpAdjustment.setMargins(audioLayout,16,0,0,0)
        dpAdjustment.setScale(audioLayout.layoutParams,328f,64f)

        folderAudioIcons = findViewById(R.id.folder_app_icon_second)
        folderAudioIcons?.setImageResource(R.drawable.icon_music)
        dpAdjustment.setMargins(folderAudioIcons!!,2,14,0,0)
        dpAdjustment.setScale(folderAudioIcons!!.layoutParams,36f,36f)

        folderNamesAudio = findViewById(R.id.folderName_second)
        dpAdjustment.setMargins(folderNamesAudio!!,10,12,0,0)

        folderUsesAudio = findViewById(R.id.folderUse_second)
         dpAdjustment.setMargins(folderUsesAudio!!,50,2,0,0)

        folderCountsAudio = findViewById(R.id.folderCount_second)
        dpAdjustment.setMargins(folderCountsAudio!!,10,2,0,0)

        folderButtonAudio = findViewById(R.id.manageBtn_second)
        dpAdjustment.setMargins(folderButtonAudio,0,20,0,0)
        dpAdjustment.setScale(folderButtonAudio.layoutParams,68f,24f)

        folderButtonAudio.setOnClickListener {
            val intent = Intent(this, ManageAudioActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)

            this.startActivity(intent)

        }
        ///////////////////// 비디오 ////////////////////////////
        val videoLayout =findViewById<RelativeLayout>(R.id.videoLine)
        dpAdjustment.setMargins(videoLayout,16,0,0,0)
        dpAdjustment.setScale(videoLayout.layoutParams,328f,64f)

        folderVideoIcons = findViewById(R.id.folder_app_icon_third)
        folderVideoIcons?.setImageResource(R.drawable.icon_camera)
        dpAdjustment.setMargins(folderVideoIcons!!,2,14,0,0)
        dpAdjustment.setScale(folderVideoIcons!!.layoutParams,36f,36f)

        folderNamesVideo = findViewById(R.id.folderName_third)
        dpAdjustment.setMargins(folderNamesVideo!!,10,12,0,0)

        folderUsesVideo = findViewById(R.id.folderUse_third)
        dpAdjustment.setMargins(folderUsesVideo!!,50,2,0,0)

        folderCountsVideo = findViewById(R.id.folderCount_third)
        dpAdjustment.setMargins(folderCountsVideo!!,10,2,0,0)

        folderButtonVideo = findViewById(R.id.manageBtn_third)
        dpAdjustment.setMargins(folderButtonVideo,0,20,0,0)
        dpAdjustment.setScale(folderButtonVideo.layoutParams,68f,24f)

        folderButtonVideo.setOnClickListener {
            val intent = Intent(this, ManageVideoActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)

            this.startActivity(intent)

        }
        ////////////////////////////// 문서 //////////////////////////////////
        val docLayout = findViewById<RelativeLayout>(R.id.DocLine)
        dpAdjustment.setMargins(docLayout,16,0,0,0)
        dpAdjustment.setScale(docLayout.layoutParams,328f,64f)

        folderDocIcons = findViewById(R.id.folder_app_icon_fourth)
        folderDocIcons?.setImageResource(R.drawable.icon_message)
        dpAdjustment.setMargins(folderDocIcons!!,2,14,0,0)
        dpAdjustment.setScale(folderDocIcons!!.layoutParams,36f,36f)

        folderNamesDocument = findViewById(R.id.folderName_fourth)
        dpAdjustment.setMargins(folderNamesDocument!!,10,12,0,0)

        folderUsesDocument = findViewById(R.id.folderUse_fourth)
        dpAdjustment.setMargins(folderUsesDocument!!,50,2,0,0)

        folderCountsDocument = findViewById(R.id.folderCount_fourth)
        dpAdjustment.setMargins(folderCountsDocument!!,10,2,0,0)

        folderButtonDocument = findViewById(R.id.manageBtn_fourth)
        dpAdjustment.setMargins(folderButtonDocument,0,20,0,0)
        dpAdjustment.setScale(folderButtonDocument.layoutParams,68f,24f)

        folderButtonDocument.setOnClickListener {
            val intent = Intent(this, ManageDocumentActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)

            this.startActivity(intent)

        }
        ///////////////////////////////////////다운 //////////////////////////////
        val downLayout = findViewById<RelativeLayout>(R.id.downLine)
        dpAdjustment.setMargins(downLayout,16,0,0,0)
        dpAdjustment.setScale(downLayout.layoutParams,328f,64f)

        folderDownIcons = findViewById(R.id.folder_app_icon_fifth)
        folderDownIcons?.setImageResource(R.drawable.icon_folder)
        dpAdjustment.setMargins(folderDownIcons!!,2,14,0,0)
        dpAdjustment.setScale(folderDownIcons!!.layoutParams,36f,36f)

        folderNamesDownload = findViewById(R.id.folderName_fifth)
        dpAdjustment.setMargins(folderNamesDownload!!,10,12,0,0)

        folderUsesDownload = findViewById(R.id.folderUse_fifth)
        dpAdjustment.setMargins(folderUsesDownload!!,50,2,0,0)

        folderCountsDownload = findViewById(R.id.folderCount_fifth)
        dpAdjustment.setMargins(folderCountsDownload!!,10,2,0,0)

        folderButtonDownload = findViewById(R.id.manageBtn_fifth)
        dpAdjustment.setMargins(folderButtonDownload,0,20,0,0)
        dpAdjustment.setScale(folderButtonDownload.layoutParams,68f,24f)

        folderButtonDownload.setOnClickListener {
            val intent = Intent(this, ManageDownloadActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
            this.startActivity(intent)

        }

        folderUsesImage?.text = Formatter.formatFileSize(context, imageValueLong.toLong())
        folderUsesAudio?.text = Formatter.formatFileSize(context, audioValueLong)
        folderUsesVideo?.text =  Formatter.formatFileSize(context, videoValueLong)
        folderUsesDocument?.text =  Formatter.formatFileSize(context, documentValueLong)
        folderUsesDownload?.text = Formatter.formatFileSize(context, downloadValueLong)

        folderCountsImage?.text = imageAllCount.toString() + "개"
        folderCountsAudio?.text = audioAllCount.toString() + "개"
        folderCountsVideo?.text = videoAllCount.toString() + "개"
        folderCountsDocument?.text = documentAllCount.toString() + "개"
        folderCountsDownload?.text = downloadAllCount.toString() + "개"


        val totalValue = imageValueLong + audioValueLong + videoValueLong + documentValueLong + downloadValueLong +restValueLong

        var restValue = totalValue - restValueLong

        useSize.text = Formatter.formatFileSize(context, restValue)
        allSize.text = Formatter.formatFileSize(context, totalValue)

        /////////////////////////////////커스텀 뷰 //////////////

        val customBar: CustomBar = findViewById(R.id.custom_bar)

        if (0 < imageValueLong) {
            imageValue = imageValueLong * 10000 / totalValue
            imageValue.let { customBar.setDataType(CustomBar.TYPE_IMAGE, it.toInt(), Color.parseColor(getString(R.color.tachyon_theme_blue_little_little_dark))) }
        }

        if(0<audioValueLong){
            audioValue =
                audioValueLong* 1000 / totalValue
            audioValue.let { customBar.setDataType(CustomBar.TYPE_AUDIO, it.toInt(),Color.parseColor(getString(R.color.tachyon_theme_blue_blur_little)) ) }
        }

        if (0<videoValueLong){
            videoValue =
                videoValueLong* 1000  / totalValue
            videoValue.let { customBar.setDataType(CustomBar.TYPE_VIDEO, it.toInt(), Color.parseColor(getString(R.color.tachyon_theme_purple_little_dark))) }

        }


        if (0<documentValueLong){
            documentValue =
                documentValueLong* 1000 / totalValue
            documentValue.let { customBar.setDataType(CustomBar.TYPE_DOCUMENT, it.toInt(), Color.parseColor(getString(R.color.tachyon_theme_green_little_dark))) }

        }

        if (0< downloadValueLong){
            downloadValue =
                downloadValueLong* 10000 / totalValue
            downloadValue.let { customBar.setDataType(CustomBar.TYPE_DOWNLOAD, it.toInt(), Color.parseColor(getString(R.color.tachyon_theme_blue_dark_green_little))) }

        }


        customBar.createImanage()
        dpAdjustment.setMargins(customBar, 16,20,0,0)
        dpAdjustment.setScale(customBar.layoutParams,328f,18f)


    }


    private fun folderImage() {
        val size = MediaStore.Images.Media.SIZE

        val cursor = contentResolver.query(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            null,
            null,
            null,
            MediaStore.Images.ImageColumns.DATE_MODIFIED
        )

        val time = cursor?.getColumnIndexOrThrow(MediaStore.Images.ImageColumns.DATE_MODIFIED)
        var imageSize : Long ?=null

        if (cursor != null) {

                val indexID = cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID)
                while (cursor.moveToNext()) {
                    val imageID = cursor.getLong(indexID)
                    val size = cursor.getColumnIndex(MediaStore.Images.Media.SIZE)

                    imageSize = cursor.getLong(size)


                    val imageTime = cursor.getLong(time!!)
                    val imageCount = arrayListOf(imageID).count()

                    val imageSizeList = arrayListOf(imageSize).sum()
                    imageValueLong += imageSizeList
                    imageAllCount += imageCount

                    val calendar = Calendar.getInstance()
                    calendar.timeInMillis = imageTime
                }
            }
    }



    private fun folderAudio() {

        val cursor = contentResolver.query(
            MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
            null,
            null,
            null,
            MediaStore.Audio.AudioColumns.DATE_MODIFIED
        )

        val time = cursor?.getColumnIndexOrThrow(MediaStore.Audio.AudioColumns.DATE_MODIFIED)

        if (cursor != null) {

                val indexID = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media._ID)

                while (cursor.moveToNext()) {
                    val audioID = cursor.getLong(indexID)

                    val size = cursor.getColumnIndex(MediaStore.Audio.Media.SIZE)
                    val audioSize = cursor.getLong(size)
                    val audioTime = cursor.getLong(time!!)

                    val audioSizeList = arrayListOf(audioSize).sum()
                    audioValueLong += audioSizeList

                    val audioCount = arrayListOf(audioID).count()

                    audioAllCount += audioCount

                    val calendar = Calendar.getInstance()
                    calendar.timeInMillis = audioTime

            }
        }
    }
    private fun folderVideo() {

        val cursor = contentResolver.query(
            MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
            null,
            null,
            null,
            MediaStore.Video.VideoColumns.DATE_MODIFIED
        )

        val time = cursor?.getColumnIndexOrThrow(MediaStore.Video.VideoColumns.DATE_MODIFIED)

        if (cursor != null) {

            val indexID = cursor.getColumnIndexOrThrow(MediaStore.Video.Media._ID)

            while (cursor.moveToNext()) {
                val videoID = cursor.getLong(indexID)

                val size = cursor.getColumnIndex(MediaStore.Video.Media.SIZE)
                val videoSize = cursor.getLong(size)
                val videoTime = cursor.getLong(time!!)

                val videoSizeList = arrayListOf(videoSize).sum()
                videoValueLong += videoSizeList

                val videoCount = arrayListOf(videoID).count()
                videoAllCount += videoCount

                val calendar = Calendar.getInstance()
                calendar.timeInMillis = videoTime

            }
        }
    }


    private fun folderDocument() {
        val externalStorage = Environment.getExternalStorageDirectory().absolutePath
        val path = "$externalStorage/Documents"
        val dir = File(path)
        val fileList = arrayOf(dir)

        for (files in fileList) {
            folderDocumentFind(files.absolutePath)
        }
    }

    private fun folderDocumentFind(path: String) {

        val file = File(path)
        val fileList = file.listFiles() ?: return

        for (files in fileList) {
            if(!files.isFile) {
                folderDocumentFind(files.absolutePath)
            }
            val documentCount = arrayListOf(files).count()
            documentAllCount += documentCount
            val documentSize = arrayListOf(files.length()).sum()
            documentValueLong += documentSize


        }
    }


    private fun folderDownload() {
        val externalStorage = Environment.getExternalStorageDirectory().absolutePath
        val path = "$externalStorage/Download"
        val dir = File(path)
        val fileList = arrayOf(dir)
        for (files in fileList) {
            folderDownloadFind(files.absolutePath)
        }
    }

    private fun folderDownloadFind(path: String) {
        val file = File(path)
        val fileList = file.listFiles() ?: return

        for (files in fileList) {
            if(!files.isFile) {
                folderDownloadFind(files.absolutePath)
            }

            val downloadCount = arrayListOf(files).count()
            downloadAllCount += downloadCount
            val downloadSize = arrayListOf(files.length()).sum()
            downloadValueLong += downloadSize
            println(">>>>> downAllCount : $downloadAllCount, >>>>> downValue : $downloadValueLong")
        }

    }
    private fun folderRest() {

        val path : File = Environment.getDataDirectory()
        val stat  = StatFs(path.path)
        val blockSize : Long = stat.blockSizeLong


        val availableBlocks: Long = stat.availableBlocksLong
        restValueLong = blockSize*availableBlocks
    }


    fun back(view: View) {

        finish()
    }


}