package com.kyoungss.cleaner.check.clean

import android.annotation.SuppressLint
import android.content.Context
import android.text.format.Formatter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.kyoungss.cleaner.CheckCacheData
import com.kyoungss.cleaner.OnItemClickListener
import com.kyoungss.cleaner.R
import com.kyoungss.cleaner.data.RepositoryImplProgress
import kotlin.collections.ArrayList

class CacheAdapter(private var cleanList: List<CleanData>, private var context: Context) : RecyclerView.Adapter<CacheAdapter.CacheViewHolder>() {


    companion object {
        var checkboxList = arrayListOf<CheckCacheData>()
        var cacheList = RepositoryImplProgress.getCacheList()

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

    fun set(cleanListCategory: ArrayList<CleanData>) {
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

        fun bind(data: CleanData, position : Int) {

            appCheck.setOnClickListener {
      //
                        if (appCheck.isChecked) {
                            var allCacheSize: Long = 0
                            allCacheSize += data.appUse
                            data.appChoose = true

                            checkboxList.add(CheckCacheData(appName = data.appName, isSelected = appCheck.isSelected, position = position, cacheSize = allCacheSize, file = data.file))

                        } else {
                            val itr = checkboxList.iterator()

                            while (itr.hasNext()) {
                                val itrNext = itr.next()
                                if (position == itrNext.position) {
                                    itr.remove()
                                    data.appChoose = false

                                }
                            }
                        }


//                    }catch (e:IndexOutOfBoundsException){
  //                      e.printStackTrace()
    //                }



                RepositoryImplProgress.setCheckList(checkboxList)

            }

        }
}

    @SuppressLint("NotifyDataSetChanged")
    fun setCheckAll(boolean: Boolean, position: Int){
        for(check in cleanList) {
            if (check.appChoose == !boolean) {
                check.appChoose = boolean
                var allCacheSize:Long = 0
                allCacheSize += check.appUse
                checkboxList.add(
                    CheckCacheData(
                        appName = check.appName,
                        isSelected = check.appChoose,
                        position = position,
                        cacheSize = allCacheSize,
                        file = check.file
                    )
                )
            }


            }


        RepositoryImplProgress.setCheckList(checkboxList)

        notifyDataSetChanged()
        }

}


