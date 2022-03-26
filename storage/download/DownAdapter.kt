package com.kyoungss.cleaner.storage.download

import android.annotation.SuppressLint
import android.content.Context
import android.text.format.Formatter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.kyoungss.cleaner.Actions
import com.kyoungss.cleaner.R
import com.kyoungss.cleaner.data.RepositoryImpl
import com.kyoungss.cleaner.storage.CheckStorageSecondData
import kotlin.collections.ArrayList

class DownAdapter(private var downList: ArrayList<DownData>, val context: Context) : RecyclerView.Adapter<DownAdapter.Holder>() {

    companion object {
        var checkboxList = arrayListOf<CheckDownData>()
        var todayCheckboxList =arrayListOf<CheckStorageSecondData>()
        var yesterdayCheckboxList =arrayListOf<CheckStorageSecondData>()
        var monthCheckboxList =arrayListOf<CheckStorageSecondData>()
        var threeMonthCheckboxList =arrayListOf<CheckStorageSecondData>()
        var sixMonthCheckboxList =arrayListOf<CheckStorageSecondData>()
        var yearDownCheckboxList =arrayListOf<CheckStorageSecondData>()
        var yearCheckboxList =arrayListOf<CheckStorageSecondData>()
        var twoYearCheckboxList =arrayListOf<CheckStorageSecondData>()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        return Holder(
            LayoutInflater.from(parent.context).inflate(R.layout.recycler_download, parent, false)
        )
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {


        holder.downloadIcon!!.setImageResource(downList[position].downloadIcon)
        holder.downloadSize!!.text = Formatter.formatFileSize(context, downList[position].downloadSize)
        holder.downloadName!!.text = downList[position].downloadName

        holder.downloadCheck.isChecked = downList[position].downloadChoose
        holder.bind(downList[position], position)

    }

    override fun getItemCount(): Int {
        return downList.size
    }
    fun set(sixMonthListCategory: java.util.ArrayList<DownData>) {
        downList = sixMonthListCategory
        notifyDataSetChanged()
    }

    class Holder(itemView: View?) : RecyclerView.ViewHolder(itemView!!) {
        var downloadIcon: ImageView? = itemView!!.findViewById(R.id.download_icon)
        var downloadName: TextView? = itemView!!.findViewById(R.id.download_name)
        var downloadSize: TextView? = itemView?.findViewById(R.id.download_size)

        var downloadCheck: CheckBox = itemView!!.findViewById(R.id.download_check)

        fun bind(data: DownData, position: Int) {

            downloadCheck.setOnClickListener {

                if (downloadCheck.isChecked) {
                    checkboxList.add(
                        CheckDownData(
                            downName = data.downloadName,
                            isSelected = downloadCheck.isSelected,
                            position = position,
                            File = data.downloadFile,
                            diffTime = data.diffTime,
                            size = data.downloadSize
                        )
                    )
                    data.downloadChoose = true
                    for (diff in checkboxList) {
                        if ((0 < (diff.diffTime / Actions.sec) && 60 > (diff.diffTime / Actions.sec)) || (0 <= (diff.diffTime / Actions.min) && 24 > (diff.diffTime / Actions.min))) {
                            todayCheckboxList.add(
                                CheckStorageSecondData(
                                    fileName = diff.downName,
                                    isSelected = downloadCheck.isSelected,
                                    position = position,
                                    File = diff.File,
                                    diffTime = diff.diffTime,
                                    fileSize = diff.size
                                )
                            )
                        } else if (0 < (diff.diffTime / Actions.hour) && 1 >= (diff.diffTime / Actions.hour)) {
                            yesterdayCheckboxList.add(
                                CheckStorageSecondData(
                                    fileName = diff.downName,
                                    isSelected = downloadCheck.isSelected,
                                    position = position,
                                    File = diff.File,
                                    diffTime = diff.diffTime,
                                    fileSize = diff.size
                                )
                            )
                        } else if (30 > (diff.diffTime / Actions.hour)) {
                            monthCheckboxList.add(
                                CheckStorageSecondData(
                                    fileName = diff.downName,
                                    isSelected = downloadCheck.isSelected,
                                    position = position,
                                    File = diff.File,
                                    diffTime = diff.diffTime,
                                    fileSize = diff.size
                                )
                            )
                        } else if (0 < (diff.diffTime / Actions.day) && 3 > (diff.diffTime / Actions.day)) {
                            threeMonthCheckboxList.add(
                                CheckStorageSecondData(
                                    fileName = diff.downName,
                                    isSelected = downloadCheck.isSelected,
                                    position = position,
                                    File = diff.File,
                                    diffTime = diff.diffTime,
                                    fileSize = diff.size
                                )
                            )
                        } else if (3 <= (diff.diffTime / Actions.day) && 6 > (diff.diffTime / Actions.day)) {
                            sixMonthCheckboxList.add(
                                CheckStorageSecondData(
                                    fileName = diff.downName,
                                    isSelected = downloadCheck.isSelected,
                                    position = position,
                                    File = diff.File,
                                    diffTime = diff.diffTime,
                                    fileSize = diff.size
                                )
                            )
                        } else if (6 <= (diff.diffTime / Actions.day) && 12 > (diff.diffTime / Actions.day)) {
                            yearDownCheckboxList.add(
                                CheckStorageSecondData(
                                    fileName = diff.downName,
                                    isSelected = downloadCheck.isSelected,
                                    position = position,
                                    File = diff.File,
                                    diffTime = diff.diffTime,
                                    fileSize = diff.size
                                )
                            )
                        } else if (2 > (diff.diffTime / Actions.month)) {
                            yearCheckboxList.add(
                                CheckStorageSecondData(
                                    fileName = diff.downName,
                                    isSelected = downloadCheck.isSelected,
                                    position = position,
                                    File = diff.File,
                                    diffTime = diff.diffTime,
                                    fileSize = diff.size
                                )
                            )
                        } else {
                            twoYearCheckboxList.add(
                                CheckStorageSecondData(
                                    fileName = diff.downName,
                                    isSelected = downloadCheck.isSelected,
                                    position = position,
                                    File = diff.File,
                                    diffTime = diff.diffTime,
                                    fileSize = diff.size
                                )
                            )
                        }
                    }
                } else {
                    val itr = checkboxList.iterator()

                    while (itr.hasNext()) {
                        val itrNext = itr.next()
                        if (data.downloadName == itrNext.downName) {
                            itr.remove()
                        }

                    }
                    val todayItr = todayCheckboxList.iterator()

                    while (todayItr.hasNext()){
                        val itrNext = todayItr.next()
                        if(data.downloadName == itrNext.fileName){
                            todayItr.remove()

                        }
                    }
                    val yesterdayItr = yesterdayCheckboxList.iterator()

                    while (yesterdayItr.hasNext()){
                        val itrNext = yesterdayItr.next()
                        if(data.downloadName == itrNext.fileName){
                            yesterdayItr.remove()

                        }
                    }
                    val monthItr = monthCheckboxList.iterator()

                    while (monthItr.hasNext()){
                        val itrNext = monthItr.next()
                        if(data.downloadName == itrNext.fileName){
                            monthItr.remove()

                        }
                    }
                    val threeMonthItr = threeMonthCheckboxList.iterator()

                    while (threeMonthItr.hasNext()){
                        val itrNext = threeMonthItr.next()
                        if(data.downloadName == itrNext.fileName){
                            threeMonthItr.remove()

                        }
                    }
                    val sixMonthItr = sixMonthCheckboxList.iterator()

                    while (sixMonthItr.hasNext()){
                        val itrNext = sixMonthItr.next()
                        if(data.downloadName == itrNext.fileName){
                            sixMonthItr.remove()

                        }
                    }
                    val yearDownItr = yearDownCheckboxList.iterator()

                    while (yearDownItr.hasNext()){
                        val itrNext = yearDownItr.next()
                        if(data.downloadName == itrNext.fileName){
                            yearDownItr.remove()

                        }
                    }
                    val yearItr = yearCheckboxList.iterator()

                    while (yearItr.hasNext()){
                        val itrNext = yearItr.next()
                        if(data.downloadName == itrNext.fileName){
                            yearItr.remove()

                        }
                    }
                    val twoYearItr = twoYearCheckboxList.iterator()

                    while (twoYearItr.hasNext()){
                        val itrNext = twoYearItr.next()
                        if(data.downloadName == itrNext.fileName){
                            twoYearItr.remove()

                        }
                    }


                }
                RepositoryImpl.setFileToday(todayCheckboxList)
                RepositoryImpl.setFileYesterday(yesterdayCheckboxList)
                RepositoryImpl.setFileMonth(monthCheckboxList)
                RepositoryImpl.setFileThreeMonth(threeMonthCheckboxList)
                RepositoryImpl.setFileSixMonth(sixMonthCheckboxList)
                RepositoryImpl.setFileYearDown(yearDownCheckboxList)
                RepositoryImpl.setFileYear(yearCheckboxList)
                RepositoryImpl.setFileTwoYear(twoYearCheckboxList)
                RepositoryImpl.setDownList(checkboxList)


            }
        }
    }
    @SuppressLint("NotifyDataSetChanged")
    fun setCheckAll(boolean: Boolean, position: Int){
        for(check in downList) {
            if (check.downloadChoose == !boolean) {
                check.downloadChoose = boolean
                checkboxList.add(
                    CheckDownData(
                        downName = check.downloadName,
                        isSelected = check.downloadChoose,
                        position = position,
                        File= check.downloadFile, diffTime = check.diffTime, size = check.downloadSize
                    )
                )
                for (diff in checkboxList) {
                    if ((0 < (diff.diffTime / Actions.sec) && 60 > (diff.diffTime / Actions.sec)) || (0 <= (diff.diffTime / Actions.min) && 24 > (diff.diffTime / Actions.min))) {
                        todayCheckboxList.add(
                            CheckStorageSecondData(
                                fileName = diff.downName,
                                isSelected = diff.isSelected,
                                position = position,
                                File = diff.File,
                                diffTime = diff.diffTime,
                                fileSize = diff.size
                            )
                        )
                    } else if (0 < (diff.diffTime / Actions.hour) && 1 >= (diff.diffTime / Actions.hour)) {
                        yesterdayCheckboxList.add(
                            CheckStorageSecondData(
                                fileName = diff.downName,
                                isSelected = diff.isSelected,
                                position = position,
                                File = diff.File,
                                diffTime = diff.diffTime,
                                fileSize = diff.size
                            )
                        )
                    } else if (30 > (diff.diffTime / Actions.hour)) {
                        monthCheckboxList.add(
                            CheckStorageSecondData(
                                fileName = diff.downName,
                                isSelected = diff.isSelected,
                                position = position,
                                File = diff.File,
                                diffTime = diff.diffTime,
                                fileSize = diff.size
                            )
                        )
                    } else if (0 < (diff.diffTime / Actions.day) && 3 > (diff.diffTime / Actions.day)) {
                        threeMonthCheckboxList.add(
                            CheckStorageSecondData(
                                fileName = diff.downName,
                                isSelected = diff.isSelected,
                                position = position,
                                File = diff.File,
                                diffTime = diff.diffTime,
                                fileSize = diff.size
                            )
                        )
                    } else if (3 <= (diff.diffTime / Actions.day) && 6 > (diff.diffTime / Actions.day)) {
                        sixMonthCheckboxList.add(
                            CheckStorageSecondData(
                                fileName = diff.downName,
                                isSelected = diff.isSelected,
                                position = position,
                                File = diff.File,
                                diffTime = diff.diffTime,
                                fileSize = diff.size
                            )
                        )
                    } else if (6 <= (diff.diffTime / Actions.day) && 12 > (diff.diffTime / Actions.day)) {
                        yearDownCheckboxList.add(
                            CheckStorageSecondData(
                                fileName = diff.downName,
                                isSelected = diff.isSelected,
                                position = position,
                                File = diff.File,
                                diffTime = diff.diffTime,
                                fileSize = diff.size
                            )
                        )
                    } else if (2 > (diff.diffTime / Actions.month)) {
                        yearCheckboxList.add(
                            CheckStorageSecondData(
                                fileName = diff.downName,
                                isSelected = diff.isSelected,
                                position = position,
                                File = diff.File,
                                diffTime = diff.diffTime,
                                fileSize = diff.size
                            )
                        )
                    } else {
                        twoYearCheckboxList.add(
                            CheckStorageSecondData(
                                fileName = diff.downName,
                                isSelected = diff.isSelected,
                                position = position,
                                File = diff.File,
                                diffTime = diff.diffTime,
                                fileSize = diff.size
                            )
                        )
                    }
                }
            }
            RepositoryImpl.setDownList(checkboxList)
            RepositoryImpl.setFileToday(todayCheckboxList)
            RepositoryImpl.setFileYesterday(yesterdayCheckboxList)
            RepositoryImpl.setFileMonth(monthCheckboxList)
            RepositoryImpl.setFileThreeMonth(threeMonthCheckboxList)
            RepositoryImpl.setFileSixMonth(sixMonthCheckboxList)
            RepositoryImpl.setFileYearDown(yearDownCheckboxList)
            RepositoryImpl.setFileYear(yearCheckboxList)
            RepositoryImpl.setFileTwoYear(twoYearCheckboxList)
        }

        notifyDataSetChanged()
    }

}