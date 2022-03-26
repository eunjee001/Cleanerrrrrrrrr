package com.kyoungss.cleaner.check.manage

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.kyoungss.cleaner.OnItemClickListenerAccept
import com.kyoungss.cleaner.R
import com.kyoungss.cleaner.data.RepositoryImpl

class ManageAppUserActivity  : Activity() {

    var context: Context = this

    private lateinit var a: String
    private var appIcons: ImageView? = null
    private var appNames: TextView? = null
    private var appUse: TextView? = null
    private var installDate: TextView? = null
    private var dateInfo: TextView? = null
    private var textView: TextView? = null

    private val manageListCategory = RepositoryImpl.getFileList()

    //  lateinit var adapter: ManageAdapter
    private var manageUserAdapter = ManageUserAdapter(manageListCategory, context)
    var manageRecyclerList = arrayOf(PERMISSION_EXTERNAL_STORAGE)

    companion object {
        const val PERMISSION_EXTERNAL_STORAGE = 0x01
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_manage)
        val menuBar = findViewById<Button>(R.id.menu)
        val recyclerView = findViewById<RecyclerView>(R.id.recycler_manage)

        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        val view: View = layoutInflater.inflate(R.layout.popup_user_layout, null)

        menuBar.setOnClickListener {
            val popupWindow = PopupWindow(view, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true)
            popupWindow.showAsDropDown(menuBar, -(menuBar.width*2),menuBar.y.toInt())
        }

        val firstPopupTextView = view.findViewById<TextView>(R.id.popup_text_first)
        firstPopupTextView.setOnClickListener {

            manageListCategory.sortByDescending {
                it.appDate
            }

            manageUserAdapter.set(manageListCategory)


            Toast.makeText(this, "실행 일자", Toast.LENGTH_LONG).show()
        }

        val secondsPopupTextView = view.findViewById<TextView>(R.id.popup_text_second)
        secondsPopupTextView.setOnClickListener {

            manageListCategory.sortBy {
                it.appName
            }

            manageUserAdapter.set(manageListCategory)

            Toast.makeText(this, "이름", Toast.LENGTH_LONG).show()
        }

        manageUserAdapter.setItemClickListener(object : OnItemClickListenerAccept {
            override fun onItemClick(position: List<ManageData>) {
                TODO("Not yet implemented")
            }

            override fun onItemLongClick(position: Int): Boolean {

                val uninstallIntent = Intent(
                    Intent.ACTION_DELETE,
                    Uri.parse("package:${manageListCategory[position].appPackage}")
                )

                startActivity(uninstallIntent)

                manageUserAdapter.set(manageListCategory)
                manageListCategory.removeAt(position)

                return false
            }

            override fun onItemSelected(position: Int, selected: Boolean) {
            }

        })

        recyclerView.adapter = manageUserAdapter

        context = this
        initViews()
    }



    private fun initViews() {

        appIcons = findViewById(R.id.cache_app_icon)
        appNames = findViewById(R.id.cache_app_name)
        appUse = findViewById(R.id.cache_use)
        installDate = findViewById(R.id.cache_date)
        dateInfo = findViewById(R.id.cache_date_info)
        val manageRecyclerView: RecyclerView = findViewById(R.id.recycler_manage)
        manageRecyclerView.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
    }

    }


