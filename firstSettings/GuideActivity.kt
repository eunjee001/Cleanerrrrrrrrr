package com.kyoungss.cleaner.firstSettings

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import com.kyoungss.cleaner.DisplayAdjustment
import com.kyoungss.cleaner.main.MainActivity
import com.kyoungss.cleaner.R

class GuideActivity : Activity(){

    private lateinit var dpAdjustment: DisplayAdjustment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_guide)

        initViews()
    }

    private fun initViews(){
        dpAdjustment = DisplayAdjustment.getInstance(this)

        val guideTitle = findViewById<TextView>(R.id.guide_setting)
        dpAdjustment.setMargins(guideTitle, 16, 80,0,0)

        val guideInfo = findViewById<TextView>(R.id.guide_use_info)
        dpAdjustment.setMargins(guideInfo, 16,20,0,0)

        val guideAccessTitle =findViewById<TextView>(R.id.guide_access_list)
        dpAdjustment.setMargins(guideAccessTitle, 16, 25, 0, 0)


        val guideUserLayout = findViewById<RelativeLayout>(R.id.explain_guide_user)
        dpAdjustment.setMargins(guideUserLayout, 16,30,0,0)
        dpAdjustment.setScale(guideUserLayout.layoutParams, 328f, 64f)

        val userInfoImage =findViewById<ImageView>(R.id.guide_circle)
        dpAdjustment.setMargins(userInfoImage, 4, 12, 12, 12)
        dpAdjustment.setScale(userInfoImage.layoutParams, 40f,40f)

        val userInfoTitle =findViewById<TextView>(R.id.guide_record)
        dpAdjustment.setMargins(userInfoTitle, 0, 4, 0, 0)

        val userInfoExplain =findViewById<TextView>(R.id.guide_record_explain)
        dpAdjustment.setMargins(userInfoExplain, 0, 0, 0, 0)

        val guideStorageLayout = findViewById<RelativeLayout>(R.id.explain_guide_storage)
        dpAdjustment.setMargins(guideStorageLayout, 16,20,0,0)
        dpAdjustment.setScale(guideStorageLayout.layoutParams, 328f, 64f)

        val guideInfoImage =findViewById<ImageView>(R.id.guide_stored_circle)
        dpAdjustment.setMargins(guideInfoImage, 4, 12, 12, 12)
        dpAdjustment.setScale(guideInfoImage.layoutParams, 40f,40f)

        val guideInfoTitle =findViewById<TextView>(R.id.guide_stored_title)
        dpAdjustment.setMargins(guideInfoTitle, 0, 4, 0, 0)

        val guideInfoExplain =findViewById<TextView>(R.id.guide_stored_explain)
        dpAdjustment.setMargins(guideInfoExplain, 0, 0, 0, 0)

        val guideOkButton = findViewById<Button>(R.id. eula_button_next)
        dpAdjustment.setScale(guideOkButton.layoutParams, 328f, 48f)
        dpAdjustment.setMargins(guideOkButton, 16, 0, 16, 16)
    }

    fun guideOk(view: View) {
        val intent = Intent(this, MainActivity::class.java)
        this.startActivity(intent)

        finish()
    }
}
