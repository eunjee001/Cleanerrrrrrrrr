package com.kyoungss.cleaner.data
import androidx.lifecycle.MutableLiveData
import com.kyoungss.cleaner.*
import com.kyoungss.cleaner.check.manage.ManageData
import com.kyoungss.cleaner.storage.CheckStorageData
import com.kyoungss.cleaner.storage.CheckStorageSecondData
import com.kyoungss.cleaner.storage.audio.AudioData
import com.kyoungss.cleaner.storage.audio.AudioSizeData
import com.kyoungss.cleaner.storage.audio.CheckAudioData
import com.kyoungss.cleaner.storage.document.CheckDocData
import com.kyoungss.cleaner.storage.document.DocData
import com.kyoungss.cleaner.storage.document.DocDiffData
import com.kyoungss.cleaner.storage.download.CheckDownData
import com.kyoungss.cleaner.storage.download.DownData
import com.kyoungss.cleaner.storage.image.CheckImageData
import com.kyoungss.cleaner.storage.image.ImageData
import com.kyoungss.cleaner.storage.video.CheckVideoData
import com.kyoungss.cleaner.storage.video.VideoData
import com.kyoungss.cleaner.storage.video.VideoSizeData

// singleton
object RepositoryImpl : Repository {

        // for Explorer
        private var currentPath: String = ""
        private var filePathList: ArrayList<ManageData>
        private var fileList: MutableList<String>

        var progressLive = MutableLiveData<Int>()
        private  var audioCheckList :ArrayList<CheckAudioData>
        private  var docCheckList :ArrayList<CheckDocData>
        private  var downCheckList :ArrayList<CheckDownData>
        private  var imageCheckList :ArrayList<CheckImageData>
        private  var videoCheckList :ArrayList<CheckVideoData>
        private  var audioList :ArrayList<AudioData>
        private  var audioSizeList :ArrayList<AudioSizeData>
        private  var docDiff :ArrayList<DocDiffData>
        private  var docList :ArrayList<DocData>
        private  var downList :ArrayList<DownData>
        private  var imageList :ArrayList<ImageData>
        private  var imageSizeList :ArrayList<ImageData>
    private var todayCheckboxList :ArrayList<CheckStorageData>
    private var yesterdayCheckboxList :ArrayList<CheckStorageData>
    private var monthCheckboxList :ArrayList<CheckStorageData>
    private var threeMonthCheckboxList :ArrayList<CheckStorageData>
    private var sixMonthCheckboxList :ArrayList<CheckStorageData>
    private var yearDownCheckboxList :ArrayList<CheckStorageData>
    private var yearCheckboxList :ArrayList<CheckStorageData>
    private var twoYearCheckboxList :ArrayList<CheckStorageData>

    private var todayFileCheckboxList :ArrayList<CheckStorageSecondData>
    private var yesterdayFileCheckboxList :ArrayList<CheckStorageSecondData>
    private var monthFileCheckboxList :ArrayList<CheckStorageSecondData>
    private var threeMonthFileCheckboxList :ArrayList<CheckStorageSecondData>
    private var sixMonthFileCheckboxList :ArrayList<CheckStorageSecondData>
    private var yearDownFileCheckboxList :ArrayList<CheckStorageSecondData>
    private var yearFileCheckboxList :ArrayList<CheckStorageSecondData>
    private var twoYearFileCheckboxList :ArrayList<CheckStorageSecondData>
        private  var videoList :ArrayList<VideoData>
        private  var videoSizeList :ArrayList<VideoSizeData>

    private var oneMBDownCheckboxList :ArrayList<CheckStorageData>
    private var fiveMBDownCheckboxList :ArrayList<CheckStorageData>
    private var oneGBDownCheckboxList :ArrayList<CheckStorageData>
    private var oneGBCheckboxList :ArrayList<CheckStorageData>
    private var twoGBCheckboxList :ArrayList<CheckStorageData>

    private var oneMBDownFileCheckboxList :ArrayList<CheckStorageSecondData>
    private var fiveMBDownFileCheckboxList :ArrayList<CheckStorageSecondData>
    private var oneGBDownFileCheckboxList :ArrayList<CheckStorageSecondData>
    private var oneGBFileCheckboxList :ArrayList<CheckStorageSecondData>
    private var twoGBFileCheckboxList :ArrayList<CheckStorageSecondData>
        private var removeAudioData : Long
        private var removeDocData : Long
        private var removeDownData : Long
        private var removeImageData : Long
        private var removeVideoData : Long

        private var docDiffTime : Long
        // for Wiper setting
        var progressInt = MutableLiveData<Int>()
        // for Wiper Task
        private var progress: Int
        private var isRunningWiperTask: Boolean

        init {
            filePathList = arrayListOf()
            fileList = mutableListOf()

            docDiff = arrayListOf()
            audioCheckList = arrayListOf()
            docCheckList = arrayListOf()
            downCheckList = arrayListOf()
            imageCheckList = arrayListOf()
            videoCheckList = arrayListOf()

            audioList = arrayListOf()
            audioSizeList = arrayListOf()

            docList = arrayListOf()
            downList = arrayListOf()
            imageList = arrayListOf()
            imageSizeList = arrayListOf()

            videoList = arrayListOf()
            videoSizeList = arrayListOf()
            todayCheckboxList= arrayListOf()
            yesterdayCheckboxList= arrayListOf()
            monthCheckboxList= arrayListOf()
            threeMonthCheckboxList= arrayListOf()
            sixMonthCheckboxList= arrayListOf()
            yearDownCheckboxList= arrayListOf()
            yearCheckboxList= arrayListOf()
            twoYearCheckboxList= arrayListOf()
            removeAudioData =0
            removeDocData =0
            removeDownData =0
            removeImageData =0
            removeVideoData =0
            oneMBDownCheckboxList = arrayListOf()
            fiveMBDownCheckboxList = arrayListOf()
            oneGBDownCheckboxList = arrayListOf()
            oneGBCheckboxList = arrayListOf()
            twoGBCheckboxList = arrayListOf()

            oneMBDownFileCheckboxList = arrayListOf()
            fiveMBDownFileCheckboxList = arrayListOf()
            oneGBDownFileCheckboxList = arrayListOf()
            oneGBFileCheckboxList = arrayListOf()
            twoGBFileCheckboxList = arrayListOf()

            docDiffTime = 0
            progressLive.value = Actions.INIT_WIPER_TASK
            progressInt.value = Actions.INIT_WIPER_TASK
            progress = 100
            isRunningWiperTask = false
            todayFileCheckboxList= arrayListOf()
            yesterdayFileCheckboxList= arrayListOf()
            monthFileCheckboxList= arrayListOf()
            threeMonthFileCheckboxList= arrayListOf()
            sixMonthFileCheckboxList= arrayListOf()
            yearDownFileCheckboxList= arrayListOf()
            yearFileCheckboxList= arrayListOf()
            twoYearFileCheckboxList= arrayListOf()
        }

        override suspend fun getCurrentPath(): String {
            return currentPath
        }


        override suspend fun getFilePathList(): MutableList<String> {
            return fileList
        }

        fun removeFilePath(manageData: ManageData) {
            filePathList.remove(manageData)
        }

        fun setFileList(list : ArrayList<ManageData>){
            filePathList = list
            println(">>> agree get : $list")
        }

        fun getFileList(): ArrayList<ManageData> {
        println(">>> get : $filePathList")
        return filePathList

    }



    @Suppress("UNUSED")
    fun getProgress(): Int {
        return this.progress
    }

    fun setProgressLive(progress: Int) {
        this.progressLive.postValue(progress)
    }

    fun setProgressTextLive(progress: Int) {
        this.progressInt.postValue(progress)
    }

    fun setAudioList(list : ArrayList<CheckAudioData>){
        audioCheckList = list

        println(">>>>> setAudioList : $list")

    }
    fun getAudioList():ArrayList<CheckAudioData>{
        println(">>>>> getAudioList : $audioCheckList")

        return audioCheckList
    }
    fun setDocList(list : ArrayList<CheckDocData>){
        docCheckList = list
        println(">>>>> setDocList : $list")


    }
    fun getDocList():ArrayList<CheckDocData>{
        println(">>>>> getDocList : $docCheckList")

        return docCheckList
    }

    fun setDocAllList(list : ArrayList<DocData>){
        docList = list
        println(">>>>> setDocAllList : $list")


    }
    fun getDocAllList():ArrayList<DocData>{
        println(">>>>> getDocAllList : $docList")

        return docList
    }

    fun setImageAllSizeList(list : ArrayList<ImageData>){
        imageSizeList = list
        println(">>>>> setImageAllList : $list")


    }
    fun getImageAllSizeList():ArrayList<ImageData>{
        println(">>>>> getImageAllList : $docList")

        return imageSizeList
    }
    fun setDownList(list : ArrayList<CheckDownData>){
        downCheckList = list
        println(">>>>> setDownList : $list")


    }
    fun getDownList():ArrayList<CheckDownData>{
        println(">>>>> getDownList : $downCheckList")


        return downCheckList
    }

    fun setImageList(list : ArrayList<CheckImageData>){
        imageCheckList = list

        println(">>>>> setImageList : $list")

    }
    fun getImageList():ArrayList<CheckImageData>{
        println(">>>>> getImageList : $imageCheckList")

        return imageCheckList
    }

    fun setVideoList(list : ArrayList<CheckVideoData>){
        videoCheckList = list
        println(">>>>> setVideoList : $list")


    }
    fun getVideoList():ArrayList<CheckVideoData>{
        println(">>>>> getVideoList : $videoCheckList")

        return videoCheckList
    }

    fun removeAudioData(cleanData: AudioData){
        audioList.remove(cleanData)
    }
    fun removeDocData(cleanData: DocData){
        docList.remove(cleanData)
    }
    fun removeSizeAudioData(cleanData: AudioSizeData){
        audioSizeList.remove(cleanData)
    }
    fun setAudioData(list : ArrayList<AudioData>){
        audioList = list
    }
    fun getAudioData():ArrayList<AudioData>{
        return audioList
    }
    fun setToday(list : ArrayList<CheckStorageData>){
        todayCheckboxList = list
        println(">>todayCheck : $todayCheckboxList")


    }
    fun getToday():ArrayList<CheckStorageData>{
        println(">>todayCheck2 : $todayCheckboxList")
        return todayCheckboxList
    }
    fun setYesterday(list : ArrayList<CheckStorageData>){
        yesterdayCheckboxList = list


    }
    fun getYesterday():ArrayList<CheckStorageData>{

        return yesterdayCheckboxList
    }
    fun setMonth(list : ArrayList<CheckStorageData>){
        monthCheckboxList = list
        println(">>setMonth : $monthCheckboxList")


    }
    fun getMonth():ArrayList<CheckStorageData>{
        println(">>setMonth2 : $monthCheckboxList")

        return monthCheckboxList
    }
    fun setThreeMonth(list : ArrayList<CheckStorageData>){
        threeMonthCheckboxList = list


    }
    fun getThreeMonth():ArrayList<CheckStorageData>{

        return threeMonthCheckboxList
    }
    fun setSixMonth(list : ArrayList<CheckStorageData>){
        sixMonthCheckboxList = list


    }
    fun getSixMonth():ArrayList<CheckStorageData>{

        return sixMonthCheckboxList
    }
    fun setYearDown(list : ArrayList<CheckStorageData>){
        yearDownCheckboxList = list


    }
    fun getYearDown():ArrayList<CheckStorageData>{

        return yearDownCheckboxList
    }
    fun setYear(list : ArrayList<CheckStorageData>){
        yearCheckboxList = list


    }
    fun getYear():ArrayList<CheckStorageData>{

        return yearCheckboxList
    }
    fun setTwoYear(list : ArrayList<CheckStorageData>){
        twoYearCheckboxList = list


    }
    fun getTwoYear():ArrayList<CheckStorageData>{

        return twoYearCheckboxList
    }
    fun setFileToday(list : ArrayList<CheckStorageSecondData>){
        todayFileCheckboxList = list
        println(">>todayCheck : $todayCheckboxList")


    }
    fun getFileToday():ArrayList<CheckStorageSecondData>{
        println(">>todayCheck2 : $todayFileCheckboxList")
        return todayFileCheckboxList
    }
    fun setFileYesterday(list : ArrayList<CheckStorageSecondData>){
        yesterdayFileCheckboxList = list


    }
    fun getFileYesterday():ArrayList<CheckStorageSecondData>{

        return yesterdayFileCheckboxList
    }
    fun setFileMonth(list : ArrayList<CheckStorageSecondData>){
        monthFileCheckboxList = list
        println(">>setMonth : $monthFileCheckboxList")


    }
    fun getFileMonth():ArrayList<CheckStorageSecondData>{
        println(">>setMonth2 : $monthFileCheckboxList")

        return monthFileCheckboxList
    }
    fun setFileThreeMonth(list : ArrayList<CheckStorageSecondData>){
        threeMonthFileCheckboxList = list


    }
    fun getFileThreeMonth():ArrayList<CheckStorageSecondData>{

        return threeMonthFileCheckboxList
    }
    fun setFileSixMonth(list : ArrayList<CheckStorageSecondData>){
        sixMonthFileCheckboxList = list


    }
    fun getFileSixMonth():ArrayList<CheckStorageSecondData>{

        return sixMonthFileCheckboxList
    }
    fun setFileYearDown(list : ArrayList<CheckStorageSecondData>){
        yearDownFileCheckboxList = list


    }
    fun getFileYearDown():ArrayList<CheckStorageSecondData>{

        return yearDownFileCheckboxList
    }
    fun setFileYear(list : ArrayList<CheckStorageSecondData>){
        yearFileCheckboxList = list


    }
    fun getFileYear():ArrayList<CheckStorageSecondData>{

        return yearFileCheckboxList
    }
    fun setFileTwoYear(list : ArrayList<CheckStorageSecondData>){
        twoYearFileCheckboxList = list


    }
    fun getFileTwoYear():ArrayList<CheckStorageSecondData>{

        return twoYearFileCheckboxList
    }
    fun setOneMB(list : ArrayList<CheckStorageData>){
        oneMBDownCheckboxList = list


    }
    fun getOneMB():ArrayList<CheckStorageData>{

        return oneMBDownCheckboxList
    }
    fun setFiveMB(list : ArrayList<CheckStorageData>){
        fiveMBDownCheckboxList = list


    }
    fun getFiveMB():ArrayList<CheckStorageData>{

        return fiveMBDownCheckboxList
    }
    fun setOneGBDown(list : ArrayList<CheckStorageData>){
        oneGBDownCheckboxList = list


    }
    fun getOneGBDown():ArrayList<CheckStorageData>{

        return oneGBDownCheckboxList
    }
    fun setOneGB(list : ArrayList<CheckStorageData>){
        oneGBCheckboxList = list


    }
    fun getOneGB():ArrayList<CheckStorageData>{

        return oneGBCheckboxList
    }
    fun setTwoGB(list : ArrayList<CheckStorageData>){
        twoGBCheckboxList = list


    }
    fun getTwoGB():ArrayList<CheckStorageData>{

        return twoGBCheckboxList
    }

    fun setOneMBFile(list : ArrayList<CheckStorageSecondData>){
        oneMBDownFileCheckboxList = list

    }

    fun getOneMBFile():ArrayList<CheckStorageSecondData>{

        return oneMBDownFileCheckboxList
    }
    fun setFiveMBFile(list : ArrayList<CheckStorageSecondData>){
        fiveMBDownFileCheckboxList = list


    }
    fun getFiveMBFile():ArrayList<CheckStorageSecondData>{

        return fiveMBDownFileCheckboxList
    }
    fun setOneGBDownFile(list : ArrayList<CheckStorageSecondData>){
        oneGBDownFileCheckboxList = list


    }
    fun getOneGBDownFile():ArrayList<CheckStorageSecondData>{

        return oneGBDownFileCheckboxList
    }
    fun setOneGBFile(list : ArrayList<CheckStorageSecondData>){
        oneGBFileCheckboxList = list


    }
    fun getOneGBFile():ArrayList<CheckStorageSecondData>{

        return oneGBFileCheckboxList
    }
    fun setTwoGBFile(list : ArrayList<CheckStorageSecondData>){
        twoGBFileCheckboxList = list


    }
    fun getTwoGBFile():ArrayList<CheckStorageSecondData>{

        return twoGBFileCheckboxList
    }


    fun getAudioRemove():Long{
        return this.removeAudioData
    }

    fun setDocRemove (data :Long){
        removeDocData = data
    }

    fun getDocRemove():Long{
        return this.removeDocData
    }
    fun setDownRemove (data :Long){
        removeDownData = data
    }

    fun getDownRemove():Long{
        return this.removeDownData
    }
    fun setImageRemove (data :Long){
        removeImageData = data
    }

    fun getImageRemove():Long{
        return this.removeImageData
    }
    fun setVideoRemove (data :Long){
        removeVideoData = data
    }

    fun getVideoRemove():Long{
        return this.removeVideoData
    }

    fun setDocDiff (list :ArrayList<DocDiffData>){
        docDiff =list
    }

    fun getDocDiff():ArrayList<DocDiffData>{
        return docDiff
    }
    // fun getProgressLive(): MutableLiveData {
  //      return this.progressLive
 //   }

}