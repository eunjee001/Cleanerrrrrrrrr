package com.kyoungss.cleaner.storage.document

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

class DocumentAdapter(private var docList: ArrayList<DocData>, var context: Context) : RecyclerView.Adapter<DocumentAdapter.Holder>() {

    companion object {
        var checkboxList = arrayListOf<CheckDocData>()
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
            LayoutInflater.from(parent.context).inflate(R.layout.recycler_document, parent, false)
        )
    }


    override fun getItemCount(): Int {
        return docList.size
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {

        holder.documentIcon!!.setImageResource(docList[position].documentIcon)
        holder.documentSize!!.text = Formatter.formatFileSize(context, docList[position].documentSize)
        holder.documentName!!.text = docList[position].documentName

        holder.documentCheck.isChecked = docList[position].documentChoose
        holder.bind(docList[position], position)

    }
    fun set(docListCategory: ArrayList<DocData>) {

        docList = docListCategory
        notifyDataSetChanged()

    }


    class Holder(itemView: View?) : RecyclerView.ViewHolder(itemView!!) {
        var documentIcon : ImageView?= itemView!!.findViewById(R.id.document_icon)
        var documentName: TextView? = itemView!!.findViewById(R.id.document_name)
        var documentSize: TextView? = itemView?.findViewById(R.id.document_size)

        var documentCheck: CheckBox = itemView!!.findViewById(R.id.document_check)

        fun bind(data: DocData, position: Int) {


            documentCheck.setOnClickListener {


                    if(documentCheck.isChecked)  {
                        checkboxList.add(CheckDocData(docName =  data.documentName, isSelected = documentCheck.isSelected, position = position, File = data.documentFile, diffTime = data.diffTime, size = data.documentSize))
                        data.documentChoose = true
                        for(diff in checkboxList) {
                            if ((0 < (diff.diffTime / Actions.sec) && 60 > (diff.diffTime / Actions.sec)) || (0 <= (diff.diffTime / Actions.min) && 24 > (diff.diffTime / Actions.min))) {
                                todayCheckboxList.add(CheckStorageSecondData(fileName = diff.docName, isSelected = documentCheck.isSelected, position = position, File= diff.File, diffTime = diff.diffTime, fileSize = diff.size))
                            } else if (0 < (diff.diffTime / Actions.hour) && 1 >= (diff.diffTime / Actions.hour)) {
                                yesterdayCheckboxList.add(CheckStorageSecondData(fileName = diff.docName, isSelected = documentCheck.isSelected, position = position, File= diff.File, diffTime = diff.diffTime, fileSize = diff.size))
                            } else if (30 > (diff.diffTime / Actions.hour)) {
                                monthCheckboxList.add(CheckStorageSecondData(fileName = diff.docName, isSelected = documentCheck.isSelected, position = position, File= diff.File, diffTime = diff.diffTime, fileSize = diff.size))
                            } else if (0 < (diff.diffTime / Actions.day) && 3 > (diff.diffTime / Actions.day)) {
                                threeMonthCheckboxList.add(CheckStorageSecondData(fileName = diff.docName, isSelected = documentCheck.isSelected, position = position, File= diff.File, diffTime = diff.diffTime, fileSize = diff.size))
                            } else if (3 <= (diff.diffTime / Actions.day) && 6 > (diff.diffTime / Actions.day)) {
                                sixMonthCheckboxList.add(CheckStorageSecondData(fileName = diff.docName, isSelected = documentCheck.isSelected, position = position, File= diff.File, diffTime = diff.diffTime, fileSize = diff.size))
                            } else if (6 <= (diff.diffTime / Actions.day) && 12 > (diff.diffTime / Actions.day)) {
                                yearDownCheckboxList.add(CheckStorageSecondData(fileName = diff.docName, isSelected = documentCheck.isSelected, position = position, File= diff.File, diffTime = diff.diffTime, fileSize = diff.size))
                            } else if (2 > (diff.diffTime / Actions.month)) {
                                yearCheckboxList.add(CheckStorageSecondData(fileName = diff.docName, isSelected = documentCheck.isSelected, position = position, File= diff.File, diffTime = diff.diffTime, fileSize = diff.size))
                            } else {
                                twoYearCheckboxList.add(CheckStorageSecondData(fileName = diff.docName, isSelected = documentCheck.isSelected, position = position, File= diff.File, diffTime = diff.diffTime, fileSize = diff.size))
                            }
                        }
                    }else{
                        val itr = checkboxList.iterator()

                        while (itr.hasNext()) {
                            val itrNext = itr.next()
                            if (data.documentName == itrNext.docName) {
                                itr.remove()

                            }
                        }
                                val todayItr = todayCheckboxList.iterator()

                                while (todayItr.hasNext()){
                                    val itrNext = todayItr.next()
                                    if(data.documentName == itrNext.fileName){
                                        todayItr.remove()

                                    }
                                }
                                val yesterdayItr = yesterdayCheckboxList.iterator()

                                while (yesterdayItr.hasNext()){
                                    val itrNext = yesterdayItr.next()
                                    if(data.documentName == itrNext.fileName){
                                        yesterdayItr.remove()

                                    }
                                }
                                val monthItr = monthCheckboxList.iterator()

                                while (monthItr.hasNext()){
                                    val itrNext = monthItr.next()
                                    if(data.documentName == itrNext.fileName){
                                        monthItr.remove()

                                    }
                                }
                                val threeMonthItr = threeMonthCheckboxList.iterator()

                                while (threeMonthItr.hasNext()){
                                    val itrNext = threeMonthItr.next()
                                    if(data.documentName == itrNext.fileName){
                                        threeMonthItr.remove()

                                    }
                                }
                                val sixMonthItr = sixMonthCheckboxList.iterator()

                                while (sixMonthItr.hasNext()){
                                    val itrNext = sixMonthItr.next()
                                    if(data.documentName == itrNext.fileName){
                                        sixMonthItr.remove()

                                    }
                                }
                                val yearDownItr = yearDownCheckboxList.iterator()

                                while (yearDownItr.hasNext()){
                                    val itrNext = yearDownItr.next()
                                    if(data.documentName == itrNext.fileName){
                                        yearDownItr.remove()

                                    }
                                }
                                val yearItr = yearCheckboxList.iterator()

                                while (yearItr.hasNext()){
                                    val itrNext = yearItr.next()
                                    if(data.documentName == itrNext.fileName){
                                        yearItr.remove()

                                    }
                                }
                                val twoYearItr = twoYearCheckboxList.iterator()

                                while (twoYearItr.hasNext()){
                                    val itrNext = twoYearItr.next()
                                    if(data.documentName == itrNext.fileName){
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

                            RepositoryImpl.setDocList(checkboxList)

            }
        }
    }


    fun setCheckAll(boolean: Boolean, position: Int){
        for(check in docList) {
            if (check.documentChoose == !boolean) {
                check.documentChoose = boolean
                checkboxList.add(
                    CheckDocData(
                        docName = check.documentName,
                        isSelected = check.documentChoose,
                        position = position, File= check.documentFile, diffTime = check.diffTime, size = check.documentSize

                    )
                )
                for(diff in checkboxList) {
                    if ((0 < (diff.diffTime / Actions.sec) && 60 > (diff.diffTime / Actions.sec)) || (0 <= (diff.diffTime / Actions.min) && 24 > (diff.diffTime / Actions.min))) {
                        todayCheckboxList.add(CheckStorageSecondData(fileName = diff.docName, isSelected = diff.isSelected, position = position, File= diff.File, diffTime = diff.diffTime, fileSize = diff.size))
                    } else if (0 < (diff.diffTime / Actions.hour) && 1 >= (diff.diffTime / Actions.hour)) {
                        yesterdayCheckboxList.add(CheckStorageSecondData(fileName = diff.docName, isSelected = diff.isSelected, position = position, File= diff.File, diffTime = diff.diffTime, fileSize = diff.size))
                    } else if (30 > (diff.diffTime / Actions.hour)) {
                        monthCheckboxList.add(CheckStorageSecondData(fileName = diff.docName, isSelected = diff.isSelected, position = position, File= diff.File, diffTime = diff.diffTime, fileSize = diff.size))
                    } else if (0 < (diff.diffTime / Actions.day) && 3 > (diff.diffTime / Actions.day)) {
                        threeMonthCheckboxList.add(CheckStorageSecondData(fileName = diff.docName, isSelected = diff.isSelected, position = position, File= diff.File, diffTime = diff.diffTime, fileSize = diff.size))
                    } else if (3 <= (diff.diffTime / Actions.day) && 6 > (diff.diffTime / Actions.day)) {
                        sixMonthCheckboxList.add(CheckStorageSecondData(fileName = diff.docName, isSelected = diff.isSelected, position = position, File= diff.File, diffTime = diff.diffTime, fileSize = diff.size))
                    } else if (6 <= (diff.diffTime / Actions.day) && 12 > (diff.diffTime / Actions.day)) {
                        yearDownCheckboxList.add(CheckStorageSecondData(fileName = diff.docName, isSelected = diff.isSelected, position = position, File= diff.File, diffTime = diff.diffTime, fileSize = diff.size))
                    } else if (2 > (diff.diffTime / Actions.month)) {
                        yearCheckboxList.add(CheckStorageSecondData(fileName = diff.docName, isSelected = diff.isSelected, position = position, File= diff.File, diffTime = diff.diffTime, fileSize = diff.size))
                    } else {
                        twoYearCheckboxList.add(CheckStorageSecondData(fileName = diff.docName, isSelected = diff.isSelected, position = position, File= diff.File, diffTime = diff.diffTime, fileSize = diff.size))
                    }
                }
            }
        }

        RepositoryImpl.setDocList(checkboxList)
        RepositoryImpl.setFileToday(todayCheckboxList)
        RepositoryImpl.setFileYesterday(yesterdayCheckboxList)
        RepositoryImpl.setFileMonth(monthCheckboxList)
        RepositoryImpl.setFileThreeMonth(threeMonthCheckboxList)
        RepositoryImpl.setFileSixMonth(sixMonthCheckboxList)
        RepositoryImpl.setFileYearDown(yearDownCheckboxList)
        RepositoryImpl.setFileYear(yearCheckboxList)
        RepositoryImpl.setFileTwoYear(twoYearCheckboxList)

        notifyDataSetChanged()
    }

}