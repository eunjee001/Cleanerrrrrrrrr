package com.kyoungss.cleaner.main

import android.app.AppOpsManager
import android.app.AppOpsManager.MODE_ALLOWED
import android.content.Context
import android.content.Intent
import android.provider.Settings
import android.view.View
import androidx.core.content.ContextCompat

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.net.Uri
import android.os.*
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.view.isGone
import androidx.core.view.isVisible
import com.kyoungss.cleaner.DisplayAdjustment
import com.kyoungss.cleaner.R
import com.kyoungss.cleaner.check.clean.progress.MainCheckActivity


class PermissionActivity:  Activity() {
    private lateinit var dpAdjustment: DisplayAdjustment

    private lateinit var numberOne: ImageView
    private lateinit var numberTwo: ImageView
    private lateinit var permissionImage : ImageView
    private lateinit var permissionTitle : TextView
    private lateinit var permissionExplain : TextView

    private lateinit var accessBtn : Button

    private lateinit var externalStorageBtn : Button
    private lateinit var next: Button
    private lateinit var permissionNext : Button

    companion object{
        const val REQUEST_USAGE_ACCESS = 0x00;
        const val REQUEST_STORAGE_ACCESS = 0x124
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_perm)
        if(if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                checkForUsageAccessPermission() && Environment.isExternalStorageManager()
            } else {
                checkForUsageAccessPermission() &&  ActivityCompat.checkSelfPermission(this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                ) == PackageManager.PERMISSION_GRANTED
            }

        ){
            finish()
        }


            initViews()


    }
    @RequiresApi(Build.VERSION_CODES.M)
    private fun initViews(){
        dpAdjustment = DisplayAdjustment.getInstance(this)

        val permissionClose = findViewById<Button>(R.id.permission_close)
        dpAdjustment.setMargins(permissionClose, 16,17,0,0)

        permissionTitle = findViewById(R.id.permission_title)
        dpAdjustment.setMargins(permissionTitle, 36, 86,0,0)

        permissionExplain = findViewById(R.id.permission_explain)
        dpAdjustment.setMargins(permissionExplain, 36, 0,0,0)

        permissionImage = findViewById(R.id.permission_first_image)
        dpAdjustment.setMargins(permissionImage, 66, 55,0,0)
        dpAdjustment.setScale(permissionImage.layoutParams,229f, 191f )

        val permissionRelative = findViewById<RelativeLayout>(R.id.permission_first)
        dpAdjustment.setMargins(permissionRelative, 0, 84,0,0)

        val accessAgreeText = findViewById<TextView>(R.id.permission_use_access_text)
        numberOne=findViewById(R.id.permission_num_one)

        numberTwo=findViewById(R.id.permission_num_two)

        accessBtn = findViewById(R.id.permission_use_access_btn)
        externalStorageBtn = findViewById(R.id.permission_external_storage_btn)

        permissionNext = findViewById(R.id.permission_button_agree)
        dpAdjustment.setMargins(permissionNext, 16,0,0,16)
        dpAdjustment.setScale(permissionNext.layoutParams, 328f,48f)
        if(checkForUsageAccessPermission()) {
            numberOne.setImageResource(R.drawable.img_complete_check)
            accessBtn.isGone =true
            permissionImage.setImageResource(R.drawable.img_guide_second)
            permissionTitle.text = "마지막 단계입니다."
        }
    }

    fun close(view: View) {

        finish()
    }


    @RequiresApi(Build.VERSION_CODES.M)
    fun onClick(view: View) {
        when (view.id) {
            R.id.permission_use_access_btn -> if(!checkForUsageAccessPermission()) {
                    val intent = Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS)
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                        intent.data = Uri.parse("package:$packageName")
                    }
                    startActivityForResult(intent, REQUEST_USAGE_ACCESS)

            }

            R.id.permission_external_storage_btn -> if (Build.VERSION_CODES.S<=Build.VERSION.SDK_INT){
                if (!Environment.isExternalStorageManager()){
                    requestPermission()
                }
            }else if (checkExternalStoragePermission()){
                storagePermission()
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.R)
    private fun requestPermission(){
        val intent =Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION)
        intent.data = Uri.parse("package:$packageName")
        startActivityForResult(intent, 300)

        if(Environment.isExternalStorageManager()){
            numberTwo.setImageResource(R.drawable.img_complete_check)
        }
    }

    private fun checkExternalStoragePermission():Boolean{
        return ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun checkForUsageAccessPermission(): Boolean {

            val appOps = getSystemService(Context.APP_OPS_SERVICE) as AppOpsManager
            val mode = appOps.checkOpNoThrow("android:get_usage_stats", Process.myUid(),
                packageName
            )
        if(mode != MODE_ALLOWED){
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.PACKAGE_USAGE_STATS),
                REQUEST_USAGE_ACCESS )
        }
            return mode == MODE_ALLOWED

    }



    private fun storagePermission() {
        val permission = ContextCompat.checkSelfPermission(this,Manifest.permission.READ_EXTERNAL_STORAGE)
        if(permission!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                REQUEST_STORAGE_ACCESS )
        }
    }



   @RequiresApi(Build.VERSION_CODES.M)
   override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when(requestCode){
            REQUEST_USAGE_ACCESS -> if (!checkForUsageAccessPermission()){
                Toast.makeText(this, "권한 거부됨", Toast.LENGTH_SHORT).show()
                finish()
            }else{
                Toast.makeText(this, "권한 허용", Toast.LENGTH_SHORT).show()
                numberOne.setImageResource(R.drawable.img_complete_check)
                accessBtn.isGone =true
                permissionImage.setImageResource(R.drawable.img_guide_second)
                permissionTitle.text = "마지막 단계입니다."
            }

        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permission: Array<String>, grantResults:IntArray) {
        super.onRequestPermissionsResult(requestCode, permission, grantResults)
        when(requestCode){
            REQUEST_STORAGE_ACCESS -> {
                if (grantResults.isEmpty() || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "권한 거부됨", Toast.LENGTH_SHORT).show()
                finish()
                } else {
                    Toast.makeText(this, "권한 허용", Toast.LENGTH_SHORT).show()
                    numberTwo.setImageResource(R.drawable.img_complete_check)
                    externalStorageBtn.isGone = true
                    permissionImage.setImageResource(R.drawable.img_guide_third)
                    permissionTitle.text = "검사 준비를 마쳤어요."
                    permissionExplain.text = "지금 바로 시작 가능합니다."
                }
                permissionNext.isVisible = true

            }


        }

    }

    fun agreePermission(view: View) {

        val intent = Intent(this, MainCheckActivity::class.java)
        this.startActivity(intent)

        finish()
    }
}