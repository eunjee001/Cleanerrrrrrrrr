package com.kyoungss.cleaner.storage.image

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
import com.kyoungss.cleaner.R
import com.kyoungss.cleaner.data.RepositoryImpl
import com.kyoungss.cleaner.storage.CheckStorageData
import kotlin.collections.ArrayList

class ImageAdapter(private var galleryList: ArrayList<ImageData>, var context: Context) : RecyclerView.Adapter<ImageAdapter.Holder>() {

    companion object {
        var checkboxList = arrayListOf<CheckImageData>()
        var galleryList = arrayListOf<ImageData>()
        var todayCheckboxList =arrayListOf<CheckStorageData>()
        var yesterdayCheckboxList =arrayListOf<CheckStorageData>()
        var monthCheckboxList =arrayListOf<CheckStorageData>()
        var threeMonthCheckboxList =arrayListOf<CheckStorageData>()
        var sixMonthCheckboxList =arrayListOf<CheckStorageData>()
        var yearDownCheckboxList =arrayListOf<CheckStorageData>()
        var yearCheckboxList =arrayListOf<CheckStorageData>()
        var twoYearCheckboxList =arrayListOf<CheckStorageData>()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        return Holder(
            LayoutInflater.from(parent.context).inflate(R.layout.recycler_image_grid, parent, false)
        )
    }

    private lateinit var itemClickListener : OnItemClickListener
    interface ItemClickListener{
        fun onClick(view:View, position:Int)
    }

    fun setItemClickListener(onItemClickListener: OnItemClickListener){
        this.itemClickListener = onItemClickListener
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {


        holder.image!!.setImageURI(galleryList[position].image)
        holder.imageSize!!.text = Formatter.formatFileSize(context, galleryList[position].imageSize)

        holder.selectBtn.isChecked = galleryList[position].imageChoose
        holder.bind(galleryList[position], position)
        /*holder.selectBtn.setOnCheckedChangeListener { buttonView, isChecked ->
            val galleryItr = galleryList.iterator()
            while (galleryItr.hasNext()) {
                val galleryListItr = galleryItr.next()
                itemClickListener.onItemLongClick(galleryListItr.imagePackage)
                holder.imageLayout!!.setBackgroundResource(R.drawable.box_blue)

            }
        }*/

    }

    override fun getItemCount(): Int {
        return galleryList.size
    }
    fun set(sixMonthListCategory: java.util.ArrayList<ImageData>) {
        galleryList = sixMonthListCategory
        notifyDataSetChanged()
    }

    class Holder(itemView: View?) : RecyclerView.ViewHolder(itemView!!) {
        var image: ImageView? = itemView!!.findViewById(R.id.grid_item_image)
        var imageSize: TextView? = itemView?.findViewById(R.id.grid_size)

        var imageLayout : RelativeLayout? = itemView?.findViewById(R.id.grid_round_selected)
        var selectBtn: CheckBox = itemView!!.findViewById(R.id.grid_selected)

        fun bind(data: ImageData, position: Int) {

            selectBtn.setOnClickListener {
                if (selectBtn.isChecked) {
                    checkboxList.add(
                        CheckImageData(
                            imageName = data.imageName,
                            isSelected = selectBtn.isSelected,
                            position = position,
                            image = data.image,
                            diffTime = data.diffTime,
                            size = data.imageSize
                        )
                    )

                    data.imageChoose = true
                    val itr = checkboxList.iterator()
                    for (diff in checkboxList) {
                        if ((0 < (diff.diffTime / Actions.sec) && 60 > (diff.diffTime / Actions.sec)) || (0 <= (diff.diffTime / Actions.min) && 24 > (diff.diffTime / Actions.min))) {
                            todayCheckboxList.add(
                                CheckStorageData(
                                    galleryName = diff.imageName,
                                    isSelected = selectBtn.isSelected,
                                    position = position,
                                    gallery = diff.image,
                                    diffTime = diff.diffTime,
                                    gallerySize = 0
                                )
                            )
                        } else if (0 < (diff.diffTime / Actions.hour) && 1 >= (diff.diffTime / Actions.hour)) {
                            yesterdayCheckboxList.add(
                                CheckStorageData(
                                    galleryName = diff.imageName,
                                    isSelected = selectBtn.isSelected,
                                    position = position,
                                    gallery = diff.image,
                                    diffTime = diff.diffTime,
                                    gallerySize = 0
                                )
                            )
                        } else if (30 > (diff.diffTime / Actions.hour)) {
                            monthCheckboxList.add(
                                CheckStorageData(
                                    galleryName = diff.imageName,
                                    isSelected = selectBtn.isSelected,
                                    position = position,
                                    gallery = diff.image,
                                    diffTime = diff.diffTime,
                                    gallerySize = 0
                                )
                            )
                        } else if (0 < (diff.diffTime / Actions.day) && 3 > (diff.diffTime / Actions.day)) {
                            threeMonthCheckboxList.add(
                                CheckStorageData(
                                    galleryName = diff.imageName,
                                    isSelected = selectBtn.isSelected,
                                    position = position,
                                    gallery = diff.image,
                                    diffTime = diff.diffTime,
                                    gallerySize = 0
                                )
                            )
                        } else if (3 <= (diff.diffTime / Actions.day) && 6 > (diff.diffTime / Actions.day)) {
                            sixMonthCheckboxList.add(
                                CheckStorageData(
                                    galleryName = diff.imageName,
                                    isSelected = selectBtn.isSelected,
                                    position = position,
                                    gallery = diff.image,
                                    diffTime = diff.diffTime,
                                    gallerySize = 0
                                )
                            )
                        } else if (6 <= (diff.diffTime / Actions.day) && 12 > (diff.diffTime / Actions.day)) {
                            yearDownCheckboxList.add(
                                CheckStorageData(
                                    galleryName = diff.imageName,
                                    isSelected = selectBtn.isSelected,
                                    position = position,
                                    gallery = diff.image,
                                    diffTime = diff.diffTime,
                                    gallerySize = 0
                                )
                            )
                        } else if (2 > (diff.diffTime / Actions.month)) {
                            yearCheckboxList.add(
                                CheckStorageData(
                                    galleryName = diff.imageName,
                                    isSelected = selectBtn.isSelected,
                                    position = position,
                                    gallery = diff.image,
                                    diffTime = diff.diffTime,
                                    gallerySize = 0
                                )
                            )
                        } else {
                            twoYearCheckboxList.add(
                                CheckStorageData(
                                    galleryName = diff.imageName,
                                    isSelected = selectBtn.isSelected,
                                    position = position,
                                    gallery = diff.image,
                                    diffTime = diff.diffTime,
                                    gallerySize = 0
                                )
                            )
                        }
                    }
                    if (selectBtn.isChecked == data.imageChoose) {
                        imageLayout!!.setBackgroundResource(R.drawable.box_blue)
                    }

                } else {
                    val itr = checkboxList.iterator()

                    while (itr.hasNext()) {
                        val itrNext = itr.next()
                        if (data.imageName == itrNext.imageName) {
                            imageLayout!!.setBackgroundResource(R.drawable.box)
                            itr.remove()

                        }
                    }


                    val todayItr = todayCheckboxList.iterator()

                    while (todayItr.hasNext()) {
                        val itrNext = todayItr.next()
                        if (data.imageName == itrNext.galleryName) {
                            todayItr.remove()

                        }
                    }
                    val yesterdayItr = yesterdayCheckboxList.iterator()

                    while (yesterdayItr.hasNext()) {
                        val itrNext = yesterdayItr.next()
                        if (data.imageName == itrNext.galleryName) {
                            yesterdayItr.remove()

                        }
                    }
                    val monthItr = monthCheckboxList.iterator()

                    while (monthItr.hasNext()) {
                        val itrNext = monthItr.next()
                        if (data.imageName == itrNext.galleryName) {
                            monthItr.remove()

                        }
                    }
                    val threeMonthItr = threeMonthCheckboxList.iterator()

                    while (threeMonthItr.hasNext()) {
                        val itrNext = threeMonthItr.next()
                        if (data.imageName == itrNext.galleryName) {
                            threeMonthItr.remove()

                        }
                    }
                    val sixMonthItr = sixMonthCheckboxList.iterator()

                    while (sixMonthItr.hasNext()) {
                        val itrNext = sixMonthItr.next()
                        if (data.imageName == itrNext.galleryName) {
                            sixMonthItr.remove()

                        }
                    }
                    val yearDownItr = yearDownCheckboxList.iterator()

                    while (yearDownItr.hasNext()) {
                        val itrNext = yearDownItr.next()
                        if (data.imageName == itrNext.galleryName) {
                            yearDownItr.remove()

                        }
                    }
                    val yearItr = yearCheckboxList.iterator()

                    while (yearItr.hasNext()) {
                        val itrNext = yearItr.next()
                        if (data.imageName == itrNext.galleryName) {
                            yearItr.remove()

                        }
                    }
                    val twoYearItr = twoYearCheckboxList.iterator()

                    while (twoYearItr.hasNext()) {
                        val itrNext = twoYearItr.next()
                        if (data.imageName == itrNext.galleryName) {
                            twoYearItr.remove()

                        }
                    }


                }

                RepositoryImpl.setToday(todayCheckboxList)
                RepositoryImpl.setYesterday(yesterdayCheckboxList)
                RepositoryImpl.setMonth(monthCheckboxList)
                RepositoryImpl.setThreeMonth(threeMonthCheckboxList)
                RepositoryImpl.setSixMonth(sixMonthCheckboxList)
                RepositoryImpl.setYearDown(yearDownCheckboxList)
                RepositoryImpl.setYear(yearCheckboxList)
                RepositoryImpl.setTwoYear(twoYearCheckboxList)
                RepositoryImpl.setImageList(checkboxList)
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setCheckAll(boolean: Boolean, position: Int){
        for(check in galleryList) {
            if (check.imageChoose == !boolean) {
                check.imageChoose = boolean
                checkboxList.add(
                    CheckImageData(
                        imageName = check.imageName,
                        isSelected = check.imageChoose,
                        position = position,
                        image = check.image,
                        diffTime = check.diffTime,
                        size = check.imageSize
                    )
                )

                for (diff in checkboxList) {
                    if ((0 < (diff.diffTime / Actions.sec) && 60 > (diff.diffTime / Actions.sec)) || (0 <= (diff.diffTime / Actions.min) && 24 > (diff.diffTime / Actions.min))) {
                        todayCheckboxList.add(
                            CheckStorageData(
                                galleryName = diff.imageName,
                                isSelected = diff.isSelected,
                                position = position,
                                gallery = diff.image,
                                diffTime = diff.diffTime,
                                gallerySize = 0
                            )
                        )
                    } else if (0 < (diff.diffTime / Actions.hour) && 1 >= (diff.diffTime / Actions.hour)) {
                        yesterdayCheckboxList.add(
                            CheckStorageData(
                                galleryName = diff.imageName,
                                isSelected = diff.isSelected,
                                position = position,
                                gallery = diff.image,
                                diffTime = diff.diffTime,
                                gallerySize = 0
                            )
                        )
                    } else if (30 > (diff.diffTime / Actions.hour)) {
                        monthCheckboxList.add(
                            CheckStorageData(
                                galleryName = diff.imageName,
                                isSelected = diff.isSelected,
                                position = position,
                                gallery = diff.image,
                                diffTime = diff.diffTime,
                                gallerySize = 0
                            )
                        )
                    } else if (0 < (diff.diffTime / Actions.day) && 3 > (diff.diffTime / Actions.day)) {
                        threeMonthCheckboxList.add(
                            CheckStorageData(
                                galleryName = diff.imageName,
                                isSelected = diff.isSelected,
                                position = position,
                                gallery = diff.image,
                                diffTime = diff.diffTime,
                                gallerySize = 0
                            )
                        )
                    } else if (3 <= (diff.diffTime / Actions.day) && 6 > (diff.diffTime / Actions.day)) {
                        sixMonthCheckboxList.add(
                            CheckStorageData(
                                galleryName = diff.imageName,
                                isSelected = diff.isSelected,
                                position = position,
                                gallery = diff.image,
                                diffTime = diff.diffTime,
                                gallerySize = 0
                            )
                        )
                    } else if (6 <= (diff.diffTime / Actions.day) && 12 > (diff.diffTime / Actions.day)) {
                        yearDownCheckboxList.add(
                            CheckStorageData(
                                galleryName = diff.imageName,
                                isSelected = diff.isSelected,
                                position = position,
                                gallery = diff.image,
                                diffTime = diff.diffTime,
                                gallerySize = 0
                            )
                        )
                    } else if (2 > (diff.diffTime / Actions.month)) {
                        yearCheckboxList.add(
                            CheckStorageData(
                                galleryName = diff.imageName,
                                isSelected = diff.isSelected,
                                position = position,
                                gallery = diff.image,
                                diffTime = diff.diffTime,
                                gallerySize = 0
                            )
                        )
                    } else {
                        twoYearCheckboxList.add(
                            CheckStorageData(
                                galleryName = diff.imageName,
                                isSelected = diff.isSelected,
                                position = position,
                                gallery = diff.image,
                                diffTime = diff.diffTime,
                                gallerySize = 0
                            )
                        )
                    }
                }
            }
            RepositoryImpl.setImageList(checkboxList)
            RepositoryImpl.setToday(todayCheckboxList)
            RepositoryImpl.setYesterday(yesterdayCheckboxList)
            RepositoryImpl.setMonth(monthCheckboxList)
            RepositoryImpl.setThreeMonth(threeMonthCheckboxList)
            RepositoryImpl.setSixMonth(sixMonthCheckboxList)
            RepositoryImpl.setYearDown(yearDownCheckboxList)
            RepositoryImpl.setYear(yearCheckboxList)
            RepositoryImpl.setTwoYear(twoYearCheckboxList)

        }

        notifyDataSetChanged()
    }

}