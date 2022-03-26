package com.kyoungss.cleaner

import android.app.usage.StorageStatsManager
import android.content.Context
import android.content.Intent
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.content.pm.ResolveInfo
import android.os.Build
import android.os.Environment
import android.os.Process
import android.os.StatFs
import android.text.format.Formatter
import androidx.annotation.RequiresApi
import java.io.FileInputStream
import java.lang.Exception
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class Utils(applicationContext: Context) {

    var context: Context = applicationContext

    /**
     * 저장 공간 남은 사이즈
     * 권한 필요 없음
     */
    fun getExternalStorageFreeSize() {
        val path = Environment.getExternalStorageDirectory()
        val stat = StatFs(path.path)

        val longSize: Long

        if (Build.VERSION_CODES.JELLY_BEAN_MR2 > Build.VERSION.SDK_INT) {
            longSize = (stat.availableBlocks * stat.blockSize).toLong()
        } else {
            longSize = stat.availableBlocksLong * stat.blockSizeLong
        }

        println(">>>>>>>>>>저장 " +  Formatter.formatFileSize(context, longSize))
    }


    /**
     * 저장 토탈 사이즈
     * 권한 필요 없음
     */
    fun getExternalStorageTotalSize() {
        val path = Environment.getExternalStorageDirectory()
        val stat = StatFs(path.path)

        val longSize: Long

        if (Build.VERSION_CODES.JELLY_BEAN_MR2 > Build.VERSION.SDK_INT) {
            longSize = (stat.blockSize * stat.blockCount).toLong()
        } else {
            longSize = stat.blockSizeLong * stat.blockCountLong
        }

        println(">>>>>>>>>> " +  Formatter.formatFileSize(context, longSize))
    }

    /**
     * 설치된 날짜
     * QUERY_ALL_PACKAGES 권한 필요
     * 요청은 필요 없음
     */
    fun getInstalledApplicationDate(){
        println(">>>>>>>>>> getInstalledApplicationDate1 ")
        val intent = Intent(Intent.ACTION_MAIN, null)
        intent.addCategory(Intent.CATEGORY_LAUNCHER)
        val packageManager = context.packageManager

        val launchedApps = packageManager.queryIntentActivities(intent, 0) as ArrayList<ResolveInfo>

        val hash = HashMap<Long, String>()



        for (resolveInfo in launchedApps) {
            val pkgName = resolveInfo.activityInfo.packageName
            try {
                val applicationInfo = packageManager.getApplicationInfo(pkgName, PackageManager.GET_META_DATA)

                if (isSystemPackage(applicationInfo)){

                    continue
                }


                val time = packageManager.getPackageInfo(pkgName, 0).firstInstallTime
                println(">>>>>>>>>> isSystemPackage $time")
                println(">>>>>>>>>> isSystemPackage $pkgName")

                hash[time] = pkgName

                val format: DateFormat = SimpleDateFormat("yyyy/MM/dd HH")
                val calendar = Calendar.getInstance()
                calendar.timeInMillis = time
//                val applicationInfo = packageManager.getApplicationInfo(pkgName, PackageManager.GET_META_DATA)
//                val appNames = packageManager.getApplicationLabel(applicationInfo)
//                val appIcons = packageManager.getApplicationIcon(applicationInfo)


                 println(">>>>>>>>>>>>>>>>>>>>>> PKG : " + pkgName + "  //" + format.format(calendar.time)
                           + " // " + FileInputStream(applicationInfo.sourceDir).channel.size()) //APK 사이즈
//                appImg.setImageDrawable(appIcons)
//               appName.text = (appNames)
            } catch (e: Exception) {
                e.printStackTrace()
            }

            val hashList = hash.keys.sortedDescending()

            print("@@@@@@@@@@@@@ $hashList" )

            print(hash[hashList[0]])

        }
    }

    /**
     * 어플리케이션 이름
     * 권한 필요 없음
     */
    fun getInstalledApplication(){
        val intent = Intent(Intent.ACTION_MAIN, null)
        intent.addCategory(Intent.CATEGORY_LAUNCHER)

        val packageManager = context.packageManager
        val launchedApps = packageManager.queryIntentActivities(intent, 0) as ArrayList<ResolveInfo>

        for (resolveInfo in launchedApps) {
            val pkgName = resolveInfo.activityInfo.packageName
            val applicationInfo = packageManager.getApplicationInfo(pkgName, PackageManager.GET_META_DATA)

            if (!isSystemPackage(applicationInfo)){
                println(">>>>> Label "+ packageManager.getApplicationLabel(applicationInfo))
            }else{
                println(">>>>> System Label "+ packageManager.getApplicationLabel(applicationInfo))
            }
        }
        println(">>>>>>>>>>>>>>>>> ${launchedApps.size}")
    }





    /**
     * 앱 캐시 사이즈  데이터 사이즈, 앱 크기
     * API 26 이상부터 가능
     * 사용 정보 접근 권한이 필요
     */
    @RequiresApi(Build.VERSION_CODES.O)
    fun getCacheSize(){
        val storageManager: StorageStatsManager = context.getSystemService(StorageStatsManager::class.java)

        val intent = Intent(Intent.ACTION_MAIN)
        intent.addCategory(Intent.CATEGORY_LAUNCHER)

        val resolveInfos: List<ResolveInfo> = context.packageManager.queryIntentActivities(intent, 0)
        for (resolveInfo in resolveInfos) {
            val uid = resolveInfo.activityInfo.applicationInfo.storageUuid
            val packageName = resolveInfo.activityInfo.packageName
            val userHandle = Process.myUserHandle()

            try {
//                val file = File(Environment.getExternalStorageDirectory().absolutePath + "/Android/data/" + packageName + "/cache")

                val storageStats = storageManager.queryStatsForPackage(uid, packageName, userHandle)
                println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>")
                println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>")
                println(">>>>>>>>>>>>>>>>>>>> PackageName : $packageName")
                println(">>>>>>>>>>>>>>>>>>>> App Size : " + storageStats.appBytes)
                println(">>>>>>>>>>>>>>>>>>>> Cache Size : " + storageStats.cacheBytes)
                println(">>>>>>>>>>>>>>>>>>>> Data Size : " + storageStats.dataBytes)
                println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>")
                println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>")
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun getInstalledAppStorage() {
        val intent = Intent(Intent.ACTION_MAIN, null)
        intent.addCategory(Intent.CATEGORY_LAUNCHER)
        val packageManager = context.packageManager

        val launcherApps = packageManager.queryIntentActivities(intent, 0) as ArrayList<ResolveInfo>

        for(resolveInfo in launcherApps) {
            val pkgName = resolveInfo.activityInfo.packageName
            try {
                val applicationInfo = packageManager.getApplicationInfo(pkgName, 0)
                print(">>>>>>>>>>>>>>>>>%% "+FileInputStream(applicationInfo.sourceDir).channel.size() * 0.0001);
                val appSize = FileInputStream(applicationInfo.sourceDir).channel.size()
                print("?????$appSize")
                // appStoresize.text = (" " + percent)


            }catch (e : Exception) {
                e.printStackTrace()
            }
        }





    }

    /**
     * 시스템 앱인지 확인
     * 권한 필요 없음
     */
    fun isSystemPackage(ai: ApplicationInfo): Boolean {
        return ai.flags and ApplicationInfo.FLAG_SYSTEM != 0
    }
}