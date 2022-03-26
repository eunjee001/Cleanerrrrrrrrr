package com.kyoungss.cleaner.firstSettings

import android.app.Activity
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Button
import android.widget.RelativeLayout
import android.widget.TextView
import com.kyoungss.cleaner.DisplayAdjustment
import com.kyoungss.cleaner.R


import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader

class EulaActivity : Activity(){

    private lateinit var dpAdjustment : DisplayAdjustment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_eula)

        initViews()
    }

    private fun initViews() {

        dpAdjustment = DisplayAdjustment.getInstance(this)

        val contentTextView = findViewById<TextView>(R.id.eula_mainText)
        contentTextView.isLongClickable = false
        contentTextView.isFocusable = false
        contentTextView.text = getTextContent()

        val eulaButton = findViewById<Button>(R.id.permission_button_agree)
        dpAdjustment.setScale(eulaButton.layoutParams, 328f, 48f)
        dpAdjustment.setMargins(eulaButton, 16,0,16,20)

        val eulaTitle = findViewById<TextView>(R.id.eula_title)
        dpAdjustment.setScale(eulaTitle.layoutParams, 147f, 33f )
        dpAdjustment.setMargins(eulaTitle, 0, 12,155,11)

        val eulaBackButton = findViewById<Button>(R.id.eula_back_button)
        dpAdjustment.setScale(eulaBackButton.layoutParams, 24f, 24f)
        dpAdjustment.setMargins(eulaBackButton, 16,16,18,16)

        val eulaTitleLayout = findViewById<RelativeLayout>(R.id.eula_title_relative)
        dpAdjustment.setScale(eulaTitleLayout.layoutParams, 360f, 56f )
        dpAdjustment.setMargins(eulaTitleLayout, 0,0,0,30)


    }
    private fun getTextContent(): String {
        val bufferedReader = BufferedReader(InputStreamReader(resources.openRawResource(R.raw.eula)))
        val stringBuilder = StringBuilder()
        var line : String?
        try{
            while(bufferedReader.readLine().also { line = it } != null) {
                if(TextUtils.isEmpty(line)) {
                    stringBuilder.append("\n")
                } else{
                    stringBuilder.append(line)
                    stringBuilder.append(" ")
                }
            }
            bufferedReader.close()
        }catch (e: IOException){
            e.printStackTrace()

        }
        return stringBuilder.toString()
    }

    fun agreePermission(view: View) {
        finish()
    }

    fun backButton(view: View) {

        finish()
    }

}
