package com.kyoungss.cleaner.firstSettings

import android.app.Activity
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.annotation.RequiresApi
import com.kyoungss.cleaner.DisplayAdjustment
import com.kyoungss.cleaner.R

class EulaFirstActivity : Activity(){
    private lateinit var dpAdjustment: DisplayAdjustment

    private lateinit var eulaItem :RelativeLayout
    private lateinit var checkIcon: CheckBox
    private lateinit var eulaNextButton :Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_eula_start)

        initViews()
    }

    private fun initViews(){
        dpAdjustment = DisplayAdjustment.getInstance(this)

        val eulaTitle = findViewById<TextView>(R.id.eula_start_title)
        dpAdjustment.setMargins(eulaTitle, 16, 80,0,0)

        val eulaInfo = findViewById<TextView>(R.id.eula_first_info)
        dpAdjustment.setMargins(eulaInfo, 16,20,120,0)

        eulaItem =findViewById<RelativeLayout>(R.id.eula_first_agree)
        dpAdjustment.setScale(eulaItem.layoutParams, 328f, 56f )
        dpAdjustment.setMargins(eulaItem, 16, 30, 16, 0)
        checkIcon = findViewById(R.id.agreeCheck)




        eulaNextButton = findViewById(R.id. eula_button_next)
        dpAdjustment.setScale(eulaNextButton.layoutParams, 328f, 48f)
        dpAdjustment.setMargins(eulaNextButton, 16, 0, 16, 16)

    }

    fun look(view: View) {

        val intent = Intent(this, EulaActivity::class.java)

        this.startActivity(intent)
        checkIcon.isChecked = true

    }

    @RequiresApi(Build.VERSION_CODES.M)
    fun next(view: View) {
        var boolean = false

        if(!checkIcon.isChecked){
            Toast.makeText(this, "이용약관을 확인해 주세요", Toast.LENGTH_SHORT).show()


        }else{
            val intent = Intent(this, GuideActivity::class.java)
            this.startActivity(intent)

            finish()
        }
    }
}

