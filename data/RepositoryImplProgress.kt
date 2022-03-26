package com.kyoungss.cleaner.data
import androidx.lifecycle.MutableLiveData
import com.kyoungss.cleaner.*
import com.kyoungss.cleaner.check.clean.CleanData
import com.kyoungss.cleaner.check.clean.progress.CleanProgressData
import com.kyoungss.cleaner.CheckCacheData
import com.kyoungss.cleaner.check.clean.ApkData
import com.kyoungss.cleaner.check.manage.*

// singleton
object RepositoryImplProgress : Repository {

    private var currentPath: String = ""
    private var filePathList: MutableList<String> = mutableListOf()

    private var cleanAppList : ArrayList<CleanData>
    private  var cacheAppAllList :ArrayList<CleanProgressData>
    private  var apkAppAllList :ArrayList<CleanProgressData>
    private  var checkCacheList :ArrayList<CheckCacheData>
    private  var checkApkList :ArrayList<CheckAPKData>
    private  var acceptManageList :ArrayList<ManageData>
    private  var agreeManageList :ArrayList<ManageData>
    private var checkManage : ArrayList<CheckManage>
    private  var apkAppList : ArrayList<ApkData>

    var progressLive = MutableLiveData<Int>()


    private var progress: Int
    private var removeData : Long
    private var removeCacheData : Long

    private var isRunningWiperTask: Boolean

    init {



        progressLive.value = Actions.INIT_WIPER_TASK

        cleanAppList = arrayListOf()
        cacheAppAllList = arrayListOf()
        apkAppList = arrayListOf()
        checkCacheList = arrayListOf()
        checkApkList = arrayListOf()
        agreeManageList = arrayListOf()
        acceptManageList = arrayListOf()
        apkAppAllList = arrayListOf()
        checkManage = arrayListOf()
        removeData = 0
        removeCacheData =0
        progress = 100
        isRunningWiperTask = false
    }

    override suspend fun getCurrentPath(): String {
        return currentPath
    }

    override suspend fun getFilePathList(): MutableList<String> {
        return filePathList
    }

    fun setCacheAllAppList(list: ArrayList<CleanProgressData>){

        cacheAppAllList = list
    }

    fun getCacheAllList(): ArrayList<CleanProgressData> {

        return cacheAppAllList

    }
    fun setAPKAllAppList(list: ArrayList<CleanProgressData>){

        apkAppAllList = list
    }

    fun getAPKAllList(): ArrayList<CleanProgressData> {

        return apkAppAllList

    }
    //o
    fun setCacheAppList(list: ArrayList<CleanData>){
        cleanAppList = list
    }

    fun getCacheList():ArrayList<CleanData>{

        return cleanAppList
    }


    fun setApkAppList(list : ArrayList<ApkData>){
        apkAppList = list
        println(">>> list : $list")
    }

    fun removeApkAppList(cleanData: ApkData){
        apkAppList.remove(cleanData)
    }

    fun removeCacheAppList(cleanData: CleanData){
        cleanAppList.remove(cleanData)
    }

    fun getApkAppList():ArrayList<ApkData>{

        println(">>> apklist : $apkAppList")
        return apkAppList
    }

    fun setCheckList(list : ArrayList<CheckCacheData>){
        checkCacheList = list

        println(">>>>> check : $list")

    }
    fun getCheckList():ArrayList<CheckCacheData>{
        println(">>>>> getCheck : $checkCacheList")
        return checkCacheList
    }

    fun setCheckApkList(list : ArrayList<CheckAPKData>){
        checkApkList = list
        println(">>>>> checkapk : $list")

    }
    fun getCheckApkList():ArrayList<CheckAPKData>{
        return checkApkList
    }

    fun setAPKRemove (data :Long){
        removeData = data
    }

    fun getAPKRemove():Long{
        return this.removeData
    }
    fun setCacheRemove (data :Long){
        removeCacheData = data
    }

    fun getCacheRemove():Long{
        return this.removeCacheData
    }
    @Suppress("UNUSED")
    fun getProgress(): Int {
        return this.progress
    }

    fun setManageAccept(list:ArrayList<ManageData>){
        acceptManageList = list

    }
    fun getManageAccept():ArrayList<ManageData>{
        return acceptManageList
    }
    fun setManageAgree(list:ArrayList<ManageData>){
        agreeManageList = list
    }
    fun getManageAgree():ArrayList<ManageData>{

        return agreeManageList
    }
    fun setManageCheckList(list : ArrayList<CheckManage>){
        checkManage = list
    }
    fun getManageCheckList():ArrayList<CheckManage>{
        return checkManage
    }
}