package com.kyoungss.cleaner.check.manage

import android.annotation.SuppressLint
import android.content.Context
import android.text.format.Formatter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.kyoungss.cleaner.Actions
import com.kyoungss.cleaner.OnItemClickListener
import com.kyoungss.cleaner.OnItemClickListenerAccept
import com.kyoungss.cleaner.R
import com.kyoungss.cleaner.data.RepositoryImpl
import com.kyoungss.cleaner.data.RepositoryImplProgress
import kotlin.collections.ArrayList

class ManageAcceptAdapter(private var manageAcceptList: List<ManageData>, var context: Context) :
    RecyclerView.Adapter<ManageAcceptAdapter.ManageViewHolder>() {

    companion object {
        var acceptList = RepositoryImplProgress.getManageAccept()
        var manageList = RepositoryImpl.getFileList()
        var agreeList = RepositoryImplProgress.getManageAgree()
        const val ManageAppAcceptType = 0x01

    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ManageViewHolder {
        return ManageViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.recycler_manage, parent, false)
        )
    }
    private lateinit var itemClickListener: OnItemClickListener
    private lateinit var itemClickListenerTwo : OnItemClickListenerAccept

   interface ItemClickListener {
        fun onClick(view: View, position: Int)
    }

    fun setItemClickListener(onItemClickListener: OnItemClickListener) {
        this.itemClickListener = onItemClickListener
    }

    interface ItemClickListenerTwo {
        fun onClick(view: View, position: Int)
    }

    fun setItemClickListenerTwo(onItemClickListener: OnItemClickListenerAccept) {
        this.itemClickListenerTwo = onItemClickListener
    }

   override fun getItemCount(): Int {
       return manageAcceptList.size
    }

    override fun onBindViewHolder(holder: ManageViewHolder, position: Int) {


        val today = System.currentTimeMillis()

        val time = manageAcceptList[position].appDate

        val diffTime = (today - time) / 1000

        if(0> (diffTime/ Actions.sec)){
            holder.appDate!!.text = "방금 전"
        }else if( 0< (diffTime/Actions.sec) && 60> (diffTime/Actions.sec) ){
            holder.appDate!!.text = "${diffTime/Actions.sec} 분 전"
        }else if(60< (diffTime/Actions.sec) && diffTime/Actions.min in 1..24){
            holder.appDate!!.text = " ${diffTime/Actions.min} 시간 전"
        }else if(0< (diffTime/Actions.hour) && (diffTime/Actions.hour) <= 30){
            holder.appDate!!.text = "${diffTime/Actions.hour} 일 전"
        }else if (0<(diffTime/Actions.day) && (diffTime/Actions.day)< 12){
            holder.appDate!!.text = "${diffTime/Actions.day} 달 전"
        }else if (diffTime/Actions.month < 2) {
            holder.appDate!!.text = "${diffTime/Actions.month} 년 전"
        }else {
            holder.appDate!!.text = "오래됨"
        }

        holder.appIcon!!.setImageDrawable(manageAcceptList[position].appIcon)
        holder.appTitle!!.text = manageAcceptList[position].appName

        holder.appUses!!.text = Formatter.formatFileSize(context, manageAcceptList[position].appUse)
        holder.appDateInfo!!.text = manageAcceptList[position].appDateInfo
        holder.unInstall.setOnClickListener {

            itemClickListener.onItemLongClick(position)

        }
        holder.manageLock.isChecked = manageAcceptList[position].appChoose

        holder.manageLock.setOnCheckedChangeListener { buttonView, isChecked ->
            val acceptItr = acceptList.iterator()
            while (acceptItr.hasNext()) {
                val acceptListItr = acceptItr.next()
                itemClickListenerTwo.onItemLongClick(acceptListItr.position)

            }
        }

        manageAcceptList.distinct()

        holder.bind(manageAcceptList[position], position)

    }

    @SuppressLint("NotifyDataSetChanged")
    fun set(manageAcceptListCategory: ArrayList<ManageData>) {
        manageAcceptList = manageAcceptListCategory

        notifyDataSetChanged()

    }


    class ManageViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView!!) {
        var appIcon: ImageView? = null
        var appTitle: TextView? = null
        var appUses: TextView? = null
        var appDate: TextView? = null
        var appDateInfo: TextView? = null
        var recyclerView: RecyclerView? = null
        var unInstall: Button
        var manageLock : CheckBox

        init {
            appIcon = itemView!!.findViewById(R.id.cache_app_icon)
            this.appTitle = itemView.findViewById(R.id.cache_app_name)
            appUses = itemView.findViewById(R.id.cache_use)
            appDate = itemView.findViewById(R.id.cache_date)
            appDateInfo = itemView.findViewById(R.id.cache_date_info)
            unInstall = itemView.findViewById(R.id.uninstall)
            manageLock = itemView.findViewById(R.id.app_lock)
        }

        fun bind(data: ManageData, position: Int) {

            manageLock.setOnClickListener{

                    val acceptItr = acceptList.iterator()

                    while (acceptItr.hasNext()) {
                        val acceptListItr = acceptItr.next()
                        println(">>> dataposiion : ${data.appName}, accept : ${acceptListItr.appName}")
                        if((data.appName == acceptListItr.appName) ){
                            println(">>> 제외")
                            agreeList.add(ManageData(type = ManageAppAcceptType, appIcon = data.appIcon, appName = data.appName, appUse = data.appUse, appDate = data.appDate, appDateInfo = data.appDateInfo, appChoose = false, appPackage = data.appPackage, position = data.position))

                            manageList[acceptListItr.position].appChoose = false

                            acceptItr.remove()

                }
            }



            }

        }
    }
}