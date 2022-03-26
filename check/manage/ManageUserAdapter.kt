package com.kyoungss.cleaner.check.manage

import android.content.Context
import android.os.Build
import android.text.format.Formatter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.kyoungss.cleaner.OnItemClickListenerAccept
import com.kyoungss.cleaner.R
import kotlin.collections.ArrayList

class ManageUserAdapter(private var manageList: List<ManageData>, var context: Context) : RecyclerView.Adapter<ManageUserAdapter.ManageViewHolder>() {

    private  val items= arrayListOf<ManageUserData>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ManageViewHolder {
        return ManageViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.recycler_user_manage, parent,false ))
    }
    private lateinit var itemClickListener: OnItemClickListenerAccept


    interface ItemClickListener {
        fun onClick(view:View, position: Int)
    }

    fun setItemClickListener(onItemClickListener: OnItemClickListenerAccept) {
        this.itemClickListener = onItemClickListener
    }


    override fun getItemCount(): Int {
        return manageList.size
    }


    @RequiresApi(Build.VERSION_CODES.M)
    override fun onBindViewHolder(holder: ManageViewHolder, position: Int) {
        val sec = 60
        val min = 60 *sec
        val hour = 24 * min
        val day = 30 * hour
        val month = 12 * day

        val today = System.currentTimeMillis()


        val time = manageList[position].appDate

        val diffTime = (today - time) / 1000

        if(0<= (diffTime/sec) && (diffTime/sec)< 60){
            holder.appDate!!.text = "방금 전"
        }else if( 0< (diffTime/min) && (diffTime/min) < 60){
            holder.appDate!!.text = "${diffTime/sec} 분 전"
        }else if(diffTime/hour in 1..23){
            holder.appDate!!.text = " ${diffTime/min} 시간 전"
        }else if(0< (diffTime/hour) && (diffTime/hour) <= 30){
            holder.appDate!!.text = "${diffTime/hour} 일 전"
        }else if (0<(diffTime/day) && (diffTime/day)< 12){
            holder.appDate!!.text = "${diffTime/day} 달 전"
        }else if (diffTime/month < 2) {
            holder.appDate!!.text = "${diffTime/month} 년 전"
        }else {
            holder.appDate!!.text = "오래됨"
        }

        holder.appIcon!!.setImageDrawable(manageList[position].appIcon)
        holder.appTitle!!.text= manageList[position].appName
        holder.appUses!!.text = Formatter.formatFileSize( context, manageList[position].appUse)
        holder.appDate!!.text = manageList[position].appDate.toString()
        holder.appDateInfo!!.text = manageList[position].appDateInfo

        holder.unInstall.setOnClickListener{
            itemClickListener.onItemLongClick(position)
        }
        }


    fun set(manageListCategory: ArrayList<ManageData>) {
        manageList = manageListCategory

        notifyDataSetChanged()


    }

    private val recyclerViewList = ArrayList<RecyclerView>()
    class ManageViewHolder(itemView: View?) :RecyclerView.ViewHolder(itemView!!) {

        var appIcon: ImageView? = null
        var appTitle: TextView? = null
        var appUses: TextView? = null
        var appDate: TextView? = null
        var appDateInfo: TextView? = null
        var recyclerView: RecyclerView? = null
        var unInstall: Button
        val manageList = ArrayList<ManageUserData>()


        init {
            appIcon = itemView!!.findViewById(R.id.cache_app_icon)
            this.appTitle = itemView.findViewById(R.id.cache_app_name)
            appUses = itemView.findViewById(R.id.cache_use)
            appDate = itemView.findViewById(R.id.cache_date)
            appDateInfo = itemView.findViewById(R.id.cache_date_info)
            unInstall = itemView.findViewById(R.id.uninstall)

        }

    }


}