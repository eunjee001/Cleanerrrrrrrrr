package com.kyoungss.cleaner.check.clean

import android.content.Intent
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.kyoungss.cleaner.DisplayAdjustment
import com.kyoungss.cleaner.R

class CheckFinishActivity: AppCompatActivity() {
    private lateinit var dpAdjustment: DisplayAdjustment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_check)
        initViews()
    }

    fun checkClear(view: View) {
        val intent = Intent(this, CleanActivity::class.java)
        this.startActivity(intent)

        finish()
    }
    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        // 뒤로가기 버튼 클릭 시
        return if (keyCode == KeyEvent.KEYCODE_BACK) {
            false
        } else super.onKeyDown(keyCode, event)
    }

    fun initViews() {
        dpAdjustment = DisplayAdjustment.getInstance(this)

        val checkImage = findViewById<ImageView>(R.id.check_img)
        dpAdjustment.setMargins(checkImage, 154,255,0,0)
        dpAdjustment.setScale(checkImage.layoutParams, 52f,52f)

        val checkTitle = findViewById<TextView>(R.id.check_title)
        dpAdjustment.setMargins(checkTitle, 152,42,0,0)

        val checkTitleExplain = findViewById<TextView>(R.id.check_title_explain)
        dpAdjustment.setMargins(checkTitleExplain, 85,11,0,0)

        val checkOk = findViewById<Button>(R.id.check_ok)
        dpAdjustment.setMargins(checkOk, 16,0,0,20)
        dpAdjustment.setScale(checkOk.layoutParams, 328f,48f)

    }

}
