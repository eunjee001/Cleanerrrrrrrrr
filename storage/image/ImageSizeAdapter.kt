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

class ImageSizeAdapter(private var galleryList: ArrayList<ImageData>, var context: Context) : RecyclerView.Adapter<ImageSizeAdapter.Holder>() {

    companion object {
        var checkboxList = arrayListOf<CheckImageData>()
        var galleryList = arrayListOf<ImageData>()
        val twoGBCheckboxList =arrayListOf<CheckStorageData>()
        val oneGBCheckboxList = arrayListOf<CheckStorageData>()
        val oneGBDownCheckboxList = arrayListOf<CheckStorageData>()
        val fiveMBDownCheckboxList = arrayListOf<CheckStorageData>()
        val oneMBDownCheckboxList = arrayListOf<CheckStorageData>()
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
                    if(selectBtn.isChecked)  {
                        checkboxList.add(CheckImageData(imageName = data.imageName, isSelected = selectBtn.isSelected, position = position, image= data.image, diffTime = data.diffTime, size = data.imageSize))

                        data.imageChoose = true
                        for (diff in checkboxList) {
                            if ((diff.size > 0) || 0 < (diff.size / Actions.MB) && 100 > (diff.size  / Actions.MB)) {
                                oneMBDownCheckboxList.add(CheckStorageData(galleryName = diff.imageName ,isSelected = diff.isSelected, position = 0, gallerySize = diff.size,diffTime = diff.diffTime, gallery = diff.image))
                            } else if (100 <= (diff.size  / Actions.MB) && 500 > (diff.size  / Actions.MB)) {
                                fiveMBDownCheckboxList.add(CheckStorageData(galleryName = diff.imageName ,isSelected = diff.isSelected, position = 0, gallerySize = diff.size,diffTime = diff.diffTime, gallery = diff.image))
                            } else if (500 <= (diff.size  / Actions.MB) && 1 > (diff.size  / Actions.GB)) {
                                oneGBDownCheckboxList.add(CheckStorageData(galleryName = diff.imageName ,isSelected = diff.isSelected, position = 0, gallerySize = diff.size,diffTime = diff.diffTime, gallery = diff.image))
                            } else if (1 <= (diff.size  / Actions.GB) && 2 > (diff.size  / Actions.GB)) {
                                oneGBCheckboxList.add(CheckStorageData(galleryName = diff.imageName ,isSelected = diff.isSelected, position = 0, gallerySize = diff.size,diffTime = diff.diffTime, gallery = diff.image))
                            } else if (2 <= (diff.size  / Actions.GB)) {
                                twoGBCheckboxList.add(CheckStorageData(galleryName = diff.imageName ,isSelected = diff.isSelected, position = 0, gallerySize = diff.size,diffTime = diff.diffTime, gallery = diff.image))
                            }
                        }
                        if (selectBtn.isChecked == data.imageChoose) {
                            imageLayout!!.setBackgroundResource(R.drawable.box_blue)
                        }

                    }else{
                        val itr = checkboxList.iterator()

                        while (itr.hasNext()){
                            val itrNext = itr.next()
                            if(data.imageName == itrNext.imageName){
                                imageLayout!!.setBackgroundResource(R.drawable.box)
                                itr.remove()

                            }
                        }
                        val oneMBItr = oneMBDownCheckboxList.iterator()

                        while (oneMBItr.hasNext()){
                            val itrNext = oneMBItr.next()
                            if(data.imageName == itrNext.galleryName){
                                oneMBItr.remove()

                            }
                        }
                        val fiveMBItr = fiveMBDownCheckboxList.iterator()

                        while (fiveMBItr.hasNext()){
                            val itrNext = fiveMBItr.next()
                            if(data.imageName == itrNext.galleryName){
                                fiveMBItr.remove()

                            }
                        }
                        val oneGBDownItr = oneGBDownCheckboxList.iterator()

                        while (oneGBDownItr.hasNext()){
                            val itrNext = oneGBDownItr.next()
                            if(data.imageName == itrNext.galleryName){
                                oneGBDownItr.remove()

                            }
                        }
                        val oneGBItr = oneGBCheckboxList.iterator()

                        while (oneGBItr.hasNext()){
                            val itrNext = oneGBItr.next()
                            if(data.imageName == itrNext.galleryName){
                                oneGBItr.remove()

                            }
                        }
                        val twoGBItr = twoGBCheckboxList.iterator()

                        while (twoGBItr.hasNext()){
                            val itrNext = twoGBItr.next()
                            if(data.imageName == itrNext.galleryName){
                                twoGBItr.remove()

                            }
                        }

                    }

                RepositoryImpl.setOneMB(oneMBDownCheckboxList)
                RepositoryImpl.setFiveMB(fiveMBDownCheckboxList)
                RepositoryImpl.setOneGBDown(oneGBDownCheckboxList)
                RepositoryImpl.setOneGB(oneGBCheckboxList)
                RepositoryImpl.setTwoGB(twoGBCheckboxList)
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
                    if ((diff.size > 0) || 0 < (diff.size / Actions.MB) && 100 > (diff.size  / Actions.MB)) {
                        oneMBDownCheckboxList.add(CheckStorageData(galleryName = diff.imageName ,isSelected = diff.isSelected, position = 0, gallerySize = diff.size,diffTime = diff.diffTime, gallery = diff.image))
                    } else if (100 <= (diff.size  / Actions.MB) && 500 > (diff.size  / Actions.MB)) {
                        fiveMBDownCheckboxList.add(CheckStorageData(galleryName = diff.imageName ,isSelected = diff.isSelected, position = 0, gallerySize = diff.size,diffTime = diff.diffTime, gallery = diff.image))
                    } else if (500 <= (diff.size  / Actions.MB) && 1 > (diff.size  / Actions.GB)) {
                        oneGBDownCheckboxList.add(CheckStorageData(galleryName = diff.imageName ,isSelected = diff.isSelected, position = 0, gallerySize = diff.size,diffTime = diff.diffTime, gallery = diff.image))
                    } else if (1 <= (diff.size  / Actions.GB) && 2 > (diff.size  / Actions.GB)) {
                        oneGBCheckboxList.add(CheckStorageData(galleryName = diff.imageName ,isSelected = diff.isSelected, position = 0, gallerySize = diff.size,diffTime = diff.diffTime, gallery = diff.image))
                    } else if (2 <= (diff.size  / Actions.GB)) {
                        twoGBCheckboxList.add(CheckStorageData(galleryName = diff.imageName ,isSelected = diff.isSelected, position = 0, gallerySize = diff.size,diffTime = diff.diffTime, gallery = diff.image))
                    }
                }
            }
            RepositoryImpl.setOneMB(oneMBDownCheckboxList)
            RepositoryImpl.setFiveMB(fiveMBDownCheckboxList)
            RepositoryImpl.setOneGBDown(oneGBDownCheckboxList)
            RepositoryImpl.setOneGB(oneGBCheckboxList)
            RepositoryImpl.setTwoGB(twoGBCheckboxList)
            RepositoryImpl.setImageList(checkboxList)
            notifyDataSetChanged()


        }

    }

}