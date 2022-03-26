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

class DocumentSizeAdapter(private var docList: ArrayList<DocData>, var context: Context) : RecyclerView.Adapter<DocumentSizeAdapter.Holder>() {

    companion object {
        var checkboxList = arrayListOf<CheckDocData>()
        var twoGBCheckboxList =arrayListOf<CheckStorageSecondData>()
        var oneGBCheckboxList =arrayListOf<CheckStorageSecondData>()
        var oneGBDownCheckboxList =arrayListOf<CheckStorageSecondData>()
        var fiveMBDownCheckboxList =arrayListOf<CheckStorageSecondData>()
        var oneMBDownCheckboxList =arrayListOf<CheckStorageSecondData>()

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
                if (documentCheck.isChecked) {
                    checkboxList.add(
                        CheckDocData(
                            docName = data.documentName,
                            isSelected = documentCheck.isSelected,
                            position = position,
                            File = data.documentFile,
                            diffTime = data.diffTime,
                            size = data.documentSize
                        )
                    )
                    data.documentChoose = true
                    for (diff in checkboxList) {
                        if ((diff.size > 0) || 0 < (diff.size / Actions.MB) && 100 > (diff.size / Actions.MB)) {
                            oneMBDownCheckboxList.add(
                                CheckStorageSecondData(
                                    fileName = diff.docName,
                                    isSelected = documentCheck.isSelected,
                                    position = position,
                                    File = diff.File,
                                    diffTime = diff.diffTime,
                                    fileSize = diff.size
                                )
                            )
                        } else if (100 <= (diff.size / Actions.MB) && 500 > (diff.size / Actions.MB)) {
                            fiveMBDownCheckboxList.add(
                                CheckStorageSecondData(
                                    fileName = diff.docName,
                                    isSelected = documentCheck.isSelected,
                                    position = position,
                                    File = diff.File,
                                    diffTime = diff.diffTime,
                                    fileSize = diff.size
                                )
                            )
                        } else if (500 <= (diff.size / Actions.MB) && 1 > (diff.size / Actions.GB)) {
                            oneGBDownCheckboxList.add(
                                CheckStorageSecondData(
                                    fileName = diff.docName,
                                    isSelected = documentCheck.isSelected,
                                    position = position,
                                    File = diff.File,
                                    diffTime = diff.diffTime,
                                    fileSize = diff.size
                                )
                            )
                        } else if (1 <= (diff.size / Actions.GB) && 2 > (diff.size / Actions.GB)) {
                            oneGBCheckboxList.add(
                                CheckStorageSecondData(
                                    fileName = diff.docName,
                                    isSelected = documentCheck.isSelected,
                                    position = position,
                                    File = diff.File,
                                    diffTime = diff.diffTime,
                                    fileSize = diff.size
                                )
                            )
                        } else if (2 <= (diff.size / Actions.GB)) {
                            twoGBCheckboxList.add(
                                CheckStorageSecondData(
                                    fileName = diff.docName,
                                    isSelected = documentCheck.isSelected,
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
                        if (data.documentName == itrNext.docName) {
                            itr.remove()

                        }
                    }


                    val oneMBItr = oneMBDownCheckboxList.iterator()

                    while (oneMBItr.hasNext()) {
                        val itrNext = oneMBItr.next()
                        if (data.documentName == itrNext.fileName) {
                            oneMBItr.remove()

                        }
                    }
                    val fiveMBItr = fiveMBDownCheckboxList.iterator()

                    while (fiveMBItr.hasNext()) {
                        val itrNext = fiveMBItr.next()
                        if (data.documentName == itrNext.fileName) {
                            fiveMBItr.remove()

                        }
                    }
                    val oneGBDownItr = oneGBDownCheckboxList.iterator()

                    while (oneGBDownItr.hasNext()) {
                        val itrNext = oneGBDownItr.next()
                        if (data.documentName == itrNext.fileName) {
                            oneGBDownItr.remove()

                        }
                    }
                    val oneGBItr = oneGBCheckboxList.iterator()

                    while (oneGBItr.hasNext()) {
                        val itrNext = oneGBItr.next()
                        if (data.documentName == itrNext.fileName) {
                            oneGBItr.remove()

                        }
                    }
                    val twoGBItr = twoGBCheckboxList.iterator()

                    while (twoGBItr.hasNext()) {
                        val itrNext = twoGBItr.next()
                        if (data.documentName == itrNext.fileName) {
                            twoGBItr.remove()

                        }
                    }

                }

                RepositoryImpl.setOneMBFile(oneMBDownCheckboxList)
                RepositoryImpl.setFiveMBFile(fiveMBDownCheckboxList)
                RepositoryImpl.setOneGBDownFile(oneGBDownCheckboxList)
                RepositoryImpl.setOneGBFile(oneGBCheckboxList)
                RepositoryImpl.setTwoGBFile(twoGBCheckboxList)
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
                for (diff in checkboxList) {
                    if ((diff.size > 0) || 0 < (diff.size / Actions.MB) && 100 > (diff.size / Actions.MB)) {
                        oneMBDownCheckboxList.add(
                            CheckStorageSecondData(
                                fileName = diff.docName,
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
                                fileName = diff.docName,
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
                                fileName = diff.docName,
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
                                fileName = diff.docName,
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
                                fileName = diff.docName,
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
        RepositoryImpl.setDocList(checkboxList)

        notifyDataSetChanged()
    }

}