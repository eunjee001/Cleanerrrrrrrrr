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

class DownSizeAdapter(private var downList: ArrayList<DownData>, val context:Context) : RecyclerView.Adapter<DownSizeAdapter.Holder>() {

    companion object {
        var checkboxList = arrayListOf<CheckDownData>()
        var twoGBCheckboxList =arrayListOf<CheckStorageSecondData>()
        var oneGBCheckboxList =arrayListOf<CheckStorageSecondData>()
        var oneGBDownCheckboxList =arrayListOf<CheckStorageSecondData>()
        var fiveMBDownCheckboxList =arrayListOf<CheckStorageSecondData>()
        var oneMBDownCheckboxList =arrayListOf<CheckStorageSecondData>()
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
        var downloadIcon : ImageView?= itemView!!.findViewById(R.id.download_icon)
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
                            diffTime = data.diffTime,
                            File = data.downloadFile,
                            size = data.downloadSize
                        )
                    )
                    data.downloadChoose = true
                    for (diff in checkboxList) {
                        if ((diff.size > 0) || 0 < (diff.size / Actions.MB) && 100 > (diff.size / Actions.MB)) {
                            oneMBDownCheckboxList.add(
                                CheckStorageSecondData(
                                    fileName = diff.downName,
                                    isSelected = downloadCheck.isSelected,
                                    position = position,
                                    File = diff.File,
                                    diffTime = diff.diffTime,
                                    fileSize = diff.size
                                )
                            )
                        } else if (100 <= (diff.size / Actions.MB) && 500 > (diff.size / Actions.MB)) {
                            fiveMBDownCheckboxList.add(
                                CheckStorageSecondData(
                                    fileName = diff.downName,
                                    isSelected = downloadCheck.isSelected,
                                    position = position,
                                    File = diff.File,
                                    diffTime = diff.diffTime,
                                    fileSize = diff.size
                                )
                            )
                        } else if (500 <= (diff.size / Actions.MB) && 1 > (diff.size / Actions.GB)) {
                            oneGBDownCheckboxList.add(
                                CheckStorageSecondData(
                                    fileName = diff.downName,
                                    isSelected = downloadCheck.isSelected,
                                    position = position,
                                    File = diff.File,
                                    diffTime = diff.diffTime,
                                    fileSize = diff.size
                                )
                            )
                        } else if (1 <= (diff.size / Actions.GB) && 2 > (diff.size / Actions.GB)) {
                            oneGBCheckboxList.add(
                                CheckStorageSecondData(
                                    fileName = diff.downName,
                                    isSelected = downloadCheck.isSelected,
                                    position = position,
                                    File = diff.File,
                                    diffTime = diff.diffTime,
                                    fileSize = diff.size
                                )
                            )
                        } else if (2 <= (diff.size / Actions.GB)) {
                            twoGBCheckboxList.add(
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

                    val oneMBItr = oneMBDownCheckboxList.iterator()

                    while (oneMBItr.hasNext()) {
                        val itrNext = oneMBItr.next()
                        if (data.downloadName == itrNext.fileName) {
                            oneMBItr.remove()

                        }
                    }
                    val fiveMBItr = fiveMBDownCheckboxList.iterator()

                    while (fiveMBItr.hasNext()) {
                        val itrNext = fiveMBItr.next()
                        if (data.downloadName == itrNext.fileName) {
                            fiveMBItr.remove()

                        }
                    }
                    val oneGBDownItr = oneGBDownCheckboxList.iterator()

                    while (oneGBDownItr.hasNext()) {
                        val itrNext = oneGBDownItr.next()
                        if (data.downloadName == itrNext.fileName) {
                            oneGBDownItr.remove()

                        }
                    }
                    val oneGBItr = oneGBCheckboxList.iterator()

                    while (oneGBItr.hasNext()) {
                        val itrNext = oneGBItr.next()
                        if (data.downloadName == itrNext.fileName) {
                            oneGBItr.remove()

                        }
                    }
                    val twoGBItr = twoGBCheckboxList.iterator()

                    while (twoGBItr.hasNext()) {
                        val itrNext = twoGBItr.next()
                        if (data.downloadName == itrNext.fileName) {
                            twoGBItr.remove()

                        }
                    }

                }

                RepositoryImpl.setOneMBFile(oneMBDownCheckboxList)
                RepositoryImpl.setFiveMBFile(fiveMBDownCheckboxList)
                RepositoryImpl.setOneGBDownFile(oneGBDownCheckboxList)
                RepositoryImpl.setOneGBFile(oneGBCheckboxList)
                RepositoryImpl.setTwoGBFile(twoGBCheckboxList)
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
                        position = position, File= check.downloadFile, diffTime = check.diffTime, size = check.downloadSize
                    )
                )

                for (diff in checkboxList) {
                    if ((diff.size > 0) || 0 < (diff.size / Actions.MB) && 100 > (diff.size / Actions.MB)) {
                        oneMBDownCheckboxList.add(
                            CheckStorageSecondData(
                                fileName = diff.downName,
                                isSelected = diff.isSelected,
                                position = position,
                                File = diff.File,
                                diffTime = diff.diffTime,
                                fileSize = diff.size
                            )
                        )
                    } else if (100 <= (diff.size / Actions.MB) && 500 > (diff.size / Actions.MB)) {
                        fiveMBDownCheckboxList.add(
                            CheckStorageSecondData(
                                fileName = diff.downName,
                                isSelected = diff.isSelected,
                                position = position,
                                File = diff.File,
                                diffTime = diff.diffTime,
                                fileSize = diff.size
                            )
                        )
                    } else if (500 <= (diff.size / Actions.MB) && 1 > (diff.size / Actions.GB)) {
                        oneGBDownCheckboxList.add(
                            CheckStorageSecondData(
                                fileName = diff.downName,
                                isSelected = diff.isSelected,
                                position = position,
                                File = diff.File,
                                diffTime = diff.diffTime,
                                fileSize = diff.size
                            )
                        )
                    } else if (1 <= (diff.size / Actions.GB) && 2 > (diff.size / Actions.GB)) {
                        oneGBCheckboxList.add(
                            CheckStorageSecondData(
                                fileName = diff.downName,
                                isSelected = diff.isSelected,
                                position = position,
                                File = diff.File,
                                diffTime = diff.diffTime,
                                fileSize = diff.size
                            )
                        )
                    } else if (2 <= (diff.size / Actions.GB)) {
                        twoGBCheckboxList.add(
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


        }
        RepositoryImpl.setOneMBFile(oneMBDownCheckboxList)
        RepositoryImpl.setFiveMBFile(fiveMBDownCheckboxList)
        RepositoryImpl.setOneGBDownFile(oneGBDownCheckboxList)
        RepositoryImpl.setOneGBFile(oneGBCheckboxList)
        RepositoryImpl.setTwoGBFile(twoGBCheckboxList)
        RepositoryImpl.setDownList(checkboxList)

        notifyDataSetChanged()
    }

}