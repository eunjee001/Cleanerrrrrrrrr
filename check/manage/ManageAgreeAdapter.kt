package com.kyoungss.cleaner.check.manage

import android.annotation.SuppressLint
import android.content.Context
import android.text.format.Formatter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.kyoungss.cleaner.*
import com.kyoungss.cleaner.check.manage.ManageAdapter.Companion.ManageAppType
import com.kyoungss.cleaner.data.RepositoryImpl
import com.kyoungss.cleaner.data.RepositoryImplProgress
import kotlin.collections.ArrayList

class ManageAgreeAdapter(private var manageAgreeList: List<ManageData>, var context: Context) :
    RecyclerView.Adapter<ManageAgreeAdapter.ManageViewHolder>() {

    companion object {
        var acceptList =RepositoryImplProgress.getManageAccept()
        var agreeList = RepositoryImplProgress.getManageAgree()
        var manageList = RepositoryImpl.getFileList()
        const val ManageAppAgreeType = 0x01

    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ManageViewHolder {
        return ManageViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.recycler_manage, parent, false)
        )
    }
    private lateinit var itemClickListener: OnItemClickListenerAccept
    private lateinit var itemClickListenerTwo : OnItemClickListenerAgree

    interface ItemClickListener {
        fun onClick(view: View, position: Int)
    }

    fun setItemClickListener(onItemClickListener: OnItemClickListenerAccept) {
        this.itemClickListener = onItemClickListener
    }

    interface ItemClickListenerTwo {
        fun onClick(view: View, position: Int)
    }

    fun setItemClickListenerTwo(onItemClickListener: OnItemClickListenerAgree) {
        this.itemClickListenerTwo = onItemClickListener
    }

   override fun getItemCount(): Int {
       return manageAgreeList.size
    }

    override fun onBindViewHolder(holder: ManageViewHolder, position: Int) {


        val today = System.currentTimeMillis()

        val time = manageAgreeList[position].appDate

        val diffTime = (today - time) / 1000

        if (0 > (diffTime / Actions.sec)) {
            holder.appDate!!.text = "방금 전"
        } else if (0 < (diffTime / Actions.sec) && 60 > (diffTime / Actions.sec)) {
            holder.appDate!!.text = "${diffTime / Actions.sec} 분 전"
        } else if (60 < (diffTime / Actions.sec) && diffTime / Actions.min in 1..24) {
            holder.appDate!!.text = " ${diffTime / Actions.min} 시간 전"
        } else if (0 < (diffTime / Actions.hour) && (diffTime / Actions.hour) <= 30) {
            holder.appDate!!.text = "${diffTime / Actions.hour} 일 전"
        } else if (0 < (diffTime / Actions.day) && (diffTime / Actions.day) < 12) {
            holder.appDate!!.text = "${diffTime / Actions.day} 달 전"
        } else if (diffTime / Actions.month < 2) {
            holder.appDate!!.text = "${diffTime / Actions.month} 년 전"
        } else {
            holder.appDate!!.text = "오래됨"
        }

        holder.appIcon!!.setImageDrawable(manageAgreeList[position].appIcon)
        holder.appTitle!!.text = manageAgreeList[position].appName

        holder.appUses!!.text = Formatter.formatFileSize(context, manageAgreeList[position].appUse)
        //     holder.appDate!!.text = manageList[position].appDate.toString()
        holder.appDateInfo!!.text = manageAgreeList[position].appDateInfo
        holder.unInstall.setOnClickListener {
            itemClickListener.onItemLongClick(position)

        }
        holder.manageLock.isChecked = manageAgreeList[position].appChoose

        holder.manageLock.setOnCheckedChangeListener { buttonView, isChecked ->
            val agreeItr = agreeList.iterator()
            while (agreeItr.hasNext()) {
                val agreeListItr = agreeItr.next()
                itemClickListenerTwo.onItemLongClick(agreeListItr.position)

            }
        }
        holder.bind(manageAgreeList[position], position)


    }

    @SuppressLint("NotifyDataSetChanged")
    fun set(manageAgreeListCategory: ArrayList<ManageData>) {
        manageAgreeList = manageAgreeListCategory

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
        var manageLock: CheckBox

        init {
            appIcon = itemView!!.findViewById(R.id.cache_app_icon)
            this.appTitle = itemView.findViewById(R.id.cache_app_name)
            appUses = itemView.findViewById(R.id.cache_use)
            appDate = itemView.findViewById(R.id.cache_date)
            appDateInfo = itemView.findViewById(R.id.cache_date_info)
            unInstall = itemView.findViewById(R.id.uninstall)
            manageLock = itemView.findViewById(R.id.app_lock)
            //  appDate.text = "$hapDate"
        }

        fun bind(data: ManageData, position: Int) {

            manageLock.setOnClickListener {
                val agreeItr = agreeList.iterator()

                while (agreeItr.hasNext()){

                        val agreeListItr = agreeItr.next()

                        if(data.position == agreeListItr.position){
                                acceptList.add(ManageData(type = ManageAppType, appIcon =data.appIcon, appName = data.appName, appUse = data.appUse, appDate = data.appDate, appDateInfo = data.appDateInfo, appChoose = true, appPackage = data.appPackage, position = position))

                                manageList[agreeListItr.position].appChoose = true
                                agreeItr.remove()

                        }
                    }
                RepositoryImplProgress.setManageAccept(acceptList)
                RepositoryImplProgress.setManageAgree(agreeList)
                }



            }

        }

    }
