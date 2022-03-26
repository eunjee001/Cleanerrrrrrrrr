package com.kyoungss.cleaner.check.clean

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.format.Formatter
import android.view.KeyEvent
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.kyoungss.cleaner.DisplayAdjustment
import com.kyoungss.cleaner.R
import com.kyoungss.cleaner.check.manage.ManageAppActivity
import com.kyoungss.cleaner.data.RepositoryImplProgress

class CheckRemoveFinishActivity: AppCompatActivity() {
    var context: Context = this
    private lateinit var dpAdjustment: DisplayAdjustment
    private var cacheList = RepositoryImplProgress.getCacheList()

    private lateinit var removeSize: TextView
    private  var cacheAdapter = CacheAdapter(cacheList, context)

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        // 뒤로가기 버튼 클릭 시
        return if (keyCode == KeyEvent.KEYCODE_BACK) {
            false
        } else super.onKeyDown(keyCode, event)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_remove_check)
        initViews()
    }
    private fun initViews() {
        dpAdjustment = DisplayAdjustment.getInstance(this)

        val checkImage = findViewById<ImageView>(R.id.check_img)
        dpAdjustment.setMargins(checkImage, 154,255,0,0)
        dpAdjustment.setScale(checkImage.layoutParams, 52f,52f)

        val removeTitle = findViewById<TextView>(R.id.remove_info_title)
        dpAdjustment.setMargins(removeTitle, 102,40,0,0)

        val removeExplain = findViewById<TextView>(R.id.remove_info_title_explain)
        dpAdjustment.setMargins(removeExplain, 63,10,0,0)

        val lineFirst = findViewById<View>(R.id.line_remove_first)
        dpAdjustment.setMargins(lineFirst, 46,20,0,0)
        dpAdjustment.setScale(lineFirst.layoutParams, 268f,1f)

        val lineSecond = findViewById<View>(R.id.line_remove_second)
        dpAdjustment.setMargins(lineSecond, 46,10,0,0)
        dpAdjustment.setScale(lineSecond.layoutParams, 268f,1f)

        removeSize=findViewById(R.id.remove_cache)
        dpAdjustment.setMargins(removeSize, 56,10,0,0)

        val removeCacheExplain = findViewById<TextView>(R.id.remove_cache_explain)
        dpAdjustment.setMargins(removeCacheExplain, 62,10,0,0)

        val removeAppInfo = findViewById<Button>(R.id.remove_app_info)
        dpAdjustment.setMargins(removeAppInfo, 16,0,0,10)
        dpAdjustment.setScale(removeAppInfo.layoutParams, 328f,48f)

        val removeOk = findViewById<Button>(R.id.remove_cache_ok)
        dpAdjustment.setMargins(removeOk, 16,0,0,16)
        dpAdjustment.setScale(removeOk.layoutParams, 328f,48f)

        val removeLongSize = RepositoryImplProgress.getCacheRemove() or RepositoryImplProgress.getAPKRemove()

        removeSize.text = Formatter.formatFileSize(context, removeLongSize )
    }

    fun checkClear(view: View) {


        val intent = Intent(this, CleanActivity::class.java)

        startActivity(intent)
        finish()
    }

    fun appInfo(view: View) {
        val intent = Intent(this, ManageAppActivity::class.java)
        this.startActivity(intent)

        finish()
    }

}
