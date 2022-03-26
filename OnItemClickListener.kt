package com.kyoungss.cleaner

import android.view.View
import com.kyoungss.cleaner.check.manage.ManageAcceptAdapter
import com.kyoungss.cleaner.check.manage.ManageData

interface OnItemClickListener : ManageAcceptAdapter.ItemClickListener {

    fun onItemClick(position: List<ManageData>)

    fun onItemLongClick( position: Int): Boolean

    fun onItemSelected(position: Int, selected: Boolean)
    override fun onClick(view: View, position: Int) {
        TODO("Not yet implemented")
    }
}