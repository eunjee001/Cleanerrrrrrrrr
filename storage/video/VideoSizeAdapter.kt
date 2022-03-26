package com.kyoungss.cleaner.storage.video

import android.annotation.SuppressLint
import android.content.ContentResolver
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.provider.MediaStore
import android.text.format.Formatter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.kyoungss.cleaner.Actions
import com.kyoungss.cleaner.R
import com.kyoungss.cleaner.data.RepositoryImpl
import com.kyoungss.cleaner.storage.CheckStorageData
import kotlin.collections.ArrayList

class VideoSizeAdapter(private var videoList: ArrayList<VideoSizeData>, val context:Context) : RecyclerView.Adapter<VideoSizeAdapter.Holder>() {

    companion object {
        var checkboxList = arrayListOf<CheckVideoData>()
        val twoGBCheckboxList =arrayListOf<CheckStorageData>()
        val oneGBCheckboxList = arrayListOf<CheckStorageData>()
        val oneGBDownCheckboxList = arrayListOf<CheckStorageData>()
        val fiveMBDownCheckboxList = arrayListOf<CheckStorageData>()
        val oneMBDownCheckboxList = arrayListOf<CheckStorageData>()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        return Holder(
            LayoutInflater.from(parent.context).inflate(R.layout.recycler_video_grid, parent, false)
        )
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {

        val contentResolve : ContentResolver = context.contentResolver
        val option : BitmapFactory.Options = BitmapFactory.Options()
        option.inSampleSize = 1
        var bitmap: Bitmap? = null
        bitmap = MediaStore.Video.Thumbnails.getThumbnail(contentResolve, videoList[position].videoName.toLong(), MediaStore.Video.Thumbnails.MINI_KIND, option)

        holder.video!!.setImageBitmap(bitmap)
        holder.videoSize!!.text = Formatter.formatFileSize(context,videoList[position].videoSize)

        holder.videoTime!!.text = videoList[position].videoTime
        holder.selectBtn.isChecked = videoList[position].videoChoose

        holder.bind(videoList[position], position)

    }

    override fun getItemCount(): Int {
        return videoList.size
    }
    fun set(sixMonthListCategory: java.util.ArrayList<VideoSizeData>) {
        videoList = sixMonthListCategory
        notifyDataSetChanged()
    }

    class Holder(itemView: View?) : RecyclerView.ViewHolder(itemView!!) {
        var video: ImageView? = itemView!!.findViewById(R.id.grid_item_video)
        var videoSize: TextView? = itemView?.findViewById(R.id.grid_size)
        var videoTime: TextView? = itemView?.findViewById(R.id.grid_time)
        var selectBtn: CheckBox = itemView!!.findViewById(R.id.grid_selected)
        var videoLayout : RelativeLayout? = itemView?.findViewById(R.id.grid_round_selected)

        fun bind(data: VideoSizeData, position: Int) {

            selectBtn.setOnClickListener {
                    if(selectBtn.isChecked)  {
                        checkboxList.add(CheckVideoData(videoName = data.videoName, isSelected = selectBtn.isSelected, position = position,videoSize = data.videoSize, diffTime = 0, video = data.video))
                        data.videoChoose = true
                        videoLayout!!.setBackgroundResource(R.drawable.box_blue)
                        for (diff in checkboxList) {
                            if (0 < (diff.videoSize / Actions.MB) && 100 > (diff.videoSize  / Actions.MB)) {
                                oneMBDownCheckboxList.add(CheckStorageData(galleryName = diff.videoName ,isSelected = diff.isSelected, position = 0, gallerySize = diff.videoSize,diffTime = diff.diffTime, gallery = diff.video))

                            } else if (100 <= (diff.videoSize  / Actions.MB) && 500 > (diff.videoSize  / Actions.MB)) {
                                fiveMBDownCheckboxList.add(CheckStorageData(galleryName = diff.videoName ,isSelected = diff.isSelected, position = 0, gallerySize = diff.videoSize,diffTime = diff.diffTime, gallery = diff.video))
                            } else if (500 <= (diff.videoSize  / Actions.MB) && 1 > (diff.videoSize  / Actions.GB)) {
                                oneGBDownCheckboxList.add(CheckStorageData(galleryName = diff.videoName ,isSelected = diff.isSelected, position = 0, gallerySize = diff.videoSize,diffTime = diff.diffTime, gallery = diff.video))
                            } else if (1 <= (diff.videoSize  / Actions.GB) && 2 > (diff.videoSize  / Actions.GB)) {
                                oneGBCheckboxList.add(CheckStorageData(galleryName = diff.videoName ,isSelected = diff.isSelected, position = 0, gallerySize = diff.videoSize,diffTime = diff.diffTime, gallery = diff.video))
                            } else if (2 <= (diff.videoSize  / Actions.GB)) {
                                twoGBCheckboxList.add(CheckStorageData(galleryName = diff.videoName ,isSelected = diff.isSelected, position = 0, gallerySize = diff.videoSize,diffTime = diff.diffTime, gallery = diff.video))
                            }
                        }
                    }else{
                        val itr = checkboxList.iterator()
                        while (itr.hasNext()) {
                            val itrNext = itr.next()
                            if (data.videoName == itrNext.videoName) {
                                videoLayout!!.setBackgroundResource(R.drawable.box)

                                itr.remove()

                            }
                        }
                        val oneMBItr = oneMBDownCheckboxList.iterator()

                        while (oneMBItr.hasNext()){
                            val itrNext = oneMBItr.next()
                            if(data.videoName == itrNext.galleryName){
                                oneMBItr.remove()

                            }
                        }
                        val fiveMBItr = fiveMBDownCheckboxList.iterator()

                        while (fiveMBItr.hasNext()){
                            val itrNext = fiveMBItr.next()
                            if(data.videoName == itrNext.galleryName){
                                fiveMBItr.remove()

                            }
                        }
                        val oneGBDownItr = oneGBDownCheckboxList.iterator()

                        while (oneGBDownItr.hasNext()){
                            val itrNext = oneGBDownItr.next()
                            if(data.videoName == itrNext.galleryName){
                                oneGBDownItr.remove()

                            }
                        }
                        val oneGBItr = oneGBCheckboxList.iterator()

                        while (oneGBItr.hasNext()){
                            val itrNext = oneGBItr.next()
                            if(data.videoName == itrNext.galleryName){
                                oneGBItr.remove()

                            }
                        }
                        val twoGBItr = twoGBCheckboxList.iterator()

                        while (twoGBItr.hasNext()){
                            val itrNext = twoGBItr.next()
                            if(data.videoName == itrNext.galleryName){
                                twoGBItr.remove()

                            }
                        }

                    }

                RepositoryImpl.setOneMB(oneMBDownCheckboxList)
                RepositoryImpl.setFiveMB(fiveMBDownCheckboxList)
                RepositoryImpl.setOneGBDown(oneGBDownCheckboxList)
                RepositoryImpl.setOneGB(oneGBCheckboxList)
                RepositoryImpl.setTwoGB(twoGBCheckboxList)
                    RepositoryImpl.setVideoList(checkboxList)

            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setCheckAll(boolean: Boolean, position: Int){
        for(check in videoList) {
            if (check.videoChoose == !boolean) {
                check.videoChoose = boolean
                checkboxList.add(
                    CheckVideoData(
                        videoName = check.videoName,
                        isSelected = check.videoChoose,
                        position = position,
                        videoSize = check.videoSize,
                        diffTime = 0,
                        video = check.video
                    )
                )
                for (diff in checkboxList) {
                    if (0 < (diff.videoSize / Actions.MB) && 100 > (diff.videoSize  / Actions.MB)) {
                        oneMBDownCheckboxList.add(CheckStorageData(galleryName = diff.videoName ,isSelected = diff.isSelected, position = 0, gallerySize = diff.videoSize,diffTime = diff.diffTime, gallery = diff.video))
                    } else if (100 <= (diff.videoSize  / Actions.MB) && 500 > (diff.videoSize  / Actions.MB)) {
                        fiveMBDownCheckboxList.add(CheckStorageData(galleryName = diff.videoName ,isSelected = diff.isSelected, position = 0, gallerySize = diff.videoSize,diffTime = diff.diffTime, gallery = diff.video))
                    } else if (500 <= (diff.videoSize  / Actions.MB) && 1 > (diff.videoSize  / Actions.GB)) {
                        oneGBDownCheckboxList.add(CheckStorageData(galleryName = diff.videoName ,isSelected = diff.isSelected, position = 0, gallerySize = diff.videoSize,diffTime = diff.diffTime, gallery = diff.video))
                    } else if (1 <= (diff.videoSize  / Actions.GB) && 2 > (diff.videoSize  / Actions.GB)) {
                        oneGBCheckboxList.add(CheckStorageData(galleryName = diff.videoName ,isSelected = diff.isSelected, position = 0, gallerySize = diff.videoSize,diffTime = diff.diffTime, gallery = diff.video))
                    } else if (2 <= (diff.videoSize  / Actions.GB)) {
                        twoGBCheckboxList.add(CheckStorageData(galleryName = diff.videoName ,isSelected = diff.isSelected, position = 0, gallerySize = diff.videoSize,diffTime = diff.diffTime, gallery = diff.video))
                    }
                }
                RepositoryImpl.setVideoList(checkboxList)
                RepositoryImpl.setOneMB(oneMBDownCheckboxList)
                RepositoryImpl.setFiveMB(fiveMBDownCheckboxList)
                RepositoryImpl.setOneGBDown(oneGBDownCheckboxList)
                RepositoryImpl.setOneGB(oneGBCheckboxList)
                RepositoryImpl.setTwoGB(twoGBCheckboxList)
                notifyDataSetChanged()

            }
        }

    }

}