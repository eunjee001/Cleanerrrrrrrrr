package com.kyoungss.cleaner.check.clean

import android.content.Context
import android.text.format.Formatter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.kyoungss.cleaner.CheckAPKData
import com.kyoungss.cleaner.OnItemClickListener
import com.kyoungss.cleaner.R
import com.kyoungss.cleaner.data.RepositoryImplProgress

class ApkAdapter(private var cleanList: ArrayList<ApkData>, private var context: Context) : RecyclerView.Adapter<ApkAdapter.CacheViewHolder>() {
    companion object {
        var checkboxList = arrayListOf<CheckAPKData>()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CacheViewHolder {
        return CacheViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.recycler_child_item, parent, false))
    }

    private lateinit var itemClickListener: OnItemClickListener
    interface ItemClickListener{
        fun onClick(view: View, position: Int)
    }
    fun setItemClickListener(onItemClickListener: OnItemClickListener){
        this.itemClickListener = onItemClickListener
    }

    override fun getItemCount(): Int {
        return cleanList.size
    }

    override fun onBindViewHolder(holder: CacheViewHolder, position: Int) {
        holder.appIcons!!.setImageDrawable(cleanList[position].appIcon)
        holder.appNames!!.text = cleanList[position].appName
        holder.appUses!!.text = Formatter.formatFileSize(context, cleanList[position].appUse)
        holder.appCheck.isChecked = cleanList[position].appChoose

        holder.bind(cleanList[position], position)
        println("checkList: $checkboxList")
    }

    fun set(cleanListCategory: ArrayList<ApkData>) {
        cleanList = cleanListCategory
        notifyDataSetChanged()
    }

    class CacheViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView!!) {
        var appIcons : ImageView? =null
        var appNames : TextView? =null
        var appUses : TextView ?= null
        var appCheck : CheckBox

        init {
            appIcons = itemView!!.findViewById(R.id.cache_app_icon)
            appNames = itemView.findViewById(R.id.cache_app_name)
            appUses = itemView.findViewById(R.id.cache_use)
            appCheck = itemView.findViewById(R.id.cache_app_check)
        }


        fun bind(data: ApkData, position : Int) {
            appCheck.setOnClickListener {
                if (appCheck.isChecked){
                    var allAppSize:Long = 0
                    allAppSize += data.appUse
                    checkboxList.add(CheckAPKData(
                        appName = data.appName,
                        isSelected = appCheck.isSelected,
                        position = position,
                        apkSize = allAppSize,
                        file = data.file
                    ))
                    data.appChoose = true

                }else {
                    val itr = checkboxList.iterator()

                    while (itr.hasNext()) {
                        val itrNext = itr.next()
                        if (position == itrNext.position) {
                            itr.remove()
                            data.appChoose = false
                        }
                    }
                }
                RepositoryImplProgress.setCheckApkList(checkboxList)

            }
        }
    }


    fun setCheckAll(boolean: Boolean, position: Int){
        for(check in cleanList){
            if(check.appChoose == !boolean){
                check.appChoose = boolean
                var allAppSize:Long = 0
                allAppSize += check.appUse
                checkboxList.add(CheckAPKData(appName = check.appName, isSelected = check.appChoose, position = position, apkSize = allAppSize,file = check.file ))
                println(">>>> checkbox size1 : ${checkboxList.size}")
            }else{
                check.appChoose != boolean
               // cleanList.removeAll(cleanList)
                checkboxList.clear()
            }
            RepositoryImplProgress.setCheckApkList(checkboxList)

            notifyDataSetChanged()

        }

        }

}


