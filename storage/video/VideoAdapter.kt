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

class VideoAdapter(private var videoList: ArrayList<VideoData>,val context: Context) : RecyclerView.Adapter<VideoAdapter.Holder>() {

    companion object {
        var checkboxList = arrayListOf<CheckVideoData>()
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
    fun set(sixMonthListCategory: java.util.ArrayList<VideoData>) {
        videoList = sixMonthListCategory
        notifyDataSetChanged()
    }

    class Holder(itemView: View?) : RecyclerView.ViewHolder(itemView!!) {
        var video: ImageView? = itemView!!.findViewById(R.id.grid_item_video)
        var videoSize: TextView? = itemView?.findViewById(R.id.grid_size)
        var videoTime: TextView? = itemView?.findViewById(R.id.grid_time)
        var videoLayout : RelativeLayout? = itemView?.findViewById(R.id.grid_round_selected)
        //val data=ImageData
        var selectBtn: CheckBox = itemView!!.findViewById(R.id.grid_selected)
       // var dateAdapter: ImageAdapter = ImageAdapter(galleryListCategory)

        fun bind(data: VideoData, position: Int) {

            selectBtn.setOnClickListener {

                    if(selectBtn.isChecked)  {
                        checkboxList.add(CheckVideoData(videoName = data.videoName, isSelected = selectBtn.isSelected, position = position, diffTime = data.diffTime, video = data.video, videoSize = data.videoSize))
                        data.videoChoose = true
                        videoLayout!!.setBackgroundResource(R.drawable.box_blue)
                        for(diff in checkboxList) {
                            if ((0 < (diff.diffTime / Actions.sec) && 60 > (diff.diffTime / Actions.sec)) || (0 <= (diff.diffTime / Actions.min) && 24 > (diff.diffTime / Actions.min))) {
                                todayCheckboxList.add(CheckStorageData(galleryName = diff.videoName, isSelected = selectBtn.isSelected, position = position, gallery= diff.video, diffTime = diff.diffTime, gallerySize = diff.videoSize))
                            } else if (0 < (diff.diffTime / Actions.hour) && 1 >= (diff.diffTime / Actions.hour)) {
                                yesterdayCheckboxList.add(CheckStorageData(galleryName = diff.videoName, isSelected = selectBtn.isSelected, position = position, gallery= diff.video, diffTime = diff.diffTime, gallerySize = diff.videoSize))
                            } else if (30 > (diff.diffTime / Actions.hour)) {
                                monthCheckboxList.add(CheckStorageData(galleryName = diff.videoName, isSelected = selectBtn.isSelected, position = position, gallery= diff.video, diffTime = diff.diffTime, gallerySize = diff.videoSize))
                            } else if (0 < (diff.diffTime / Actions.day) && 3 > (diff.diffTime / Actions.day)) {
                                threeMonthCheckboxList.add(CheckStorageData(galleryName = diff.videoName, isSelected = selectBtn.isSelected, position = position, gallery= diff.video, diffTime = diff.diffTime, gallerySize = diff.videoSize))
                            } else if (3 <= (diff.diffTime / Actions.day) && 6 > (diff.diffTime / Actions.day)) {
                                sixMonthCheckboxList.add(CheckStorageData(galleryName = diff.videoName, isSelected = selectBtn.isSelected, position = position, gallery= diff.video, diffTime = diff.diffTime, gallerySize = diff.videoSize))
                            } else if (6 <= (diff.diffTime / Actions.day) && 12 > (diff.diffTime / Actions.day)) {
                                yearDownCheckboxList.add(CheckStorageData(galleryName = diff.videoName, isSelected = selectBtn.isSelected, position = position, gallery= diff.video, diffTime = diff.diffTime, gallerySize = diff.videoSize))
                            } else if (2 > (diff.diffTime / Actions.month)) {
                                yearCheckboxList.add(CheckStorageData(galleryName = diff.videoName, isSelected = selectBtn.isSelected, position = position, gallery= diff.video, diffTime = diff.diffTime, gallerySize = diff.videoSize))
                            } else {
                                twoYearCheckboxList.add(CheckStorageData(galleryName = diff.videoName, isSelected = selectBtn.isSelected, position = position, gallery= diff.video, diffTime = diff.diffTime, gallerySize = diff.videoSize))
                            }
                        }
                        if (selectBtn.isChecked == data.videoChoose) {
                            videoLayout!!.setBackgroundResource(R.drawable.box_blue)
                        }

                    }else{
                        val itr = checkboxList.iterator()
                        while (itr.hasNext()){
                            val itrNext = itr.next()
                            if (data.videoName == itrNext.videoName){
                                videoLayout!!.setBackgroundResource(R.drawable.box)
                                itr.remove()
                            }
                        }
                        val todayItr = todayCheckboxList.iterator()

                        while (todayItr.hasNext()) {
                            val itrNext = todayItr.next()
                            if (data.videoName == itrNext.galleryName) {
                                todayItr.remove()

                            }
                        }
                        val yesterdayItr = yesterdayCheckboxList.iterator()

                        while (yesterdayItr.hasNext()) {
                            val itrNext = yesterdayItr.next()
                            if (data.videoName == itrNext.galleryName) {
                                yesterdayItr.remove()

                            }
                        }
                        val monthItr = monthCheckboxList.iterator()

                        while (monthItr.hasNext()) {
                            val itrNext = monthItr.next()
                            if (data.videoName == itrNext.galleryName) {
                                monthItr.remove()

                            }
                        }
                        val threeMonthItr = threeMonthCheckboxList.iterator()

                        while (threeMonthItr.hasNext()) {
                            val itrNext = threeMonthItr.next()
                            if (data.videoName == itrNext.galleryName) {
                                threeMonthItr.remove()

                            }
                        }
                        val sixMonthItr = sixMonthCheckboxList.iterator()

                        while (sixMonthItr.hasNext()) {
                            val itrNext = sixMonthItr.next()
                            if (data.videoName == itrNext.galleryName) {
                                sixMonthItr.remove()

                            }
                        }
                        val yearDownItr = yearDownCheckboxList.iterator()

                        while (yearDownItr.hasNext()) {
                            val itrNext = yearDownItr.next()
                            if (data.videoName == itrNext.galleryName) {
                                yearDownItr.remove()

                            }
                        }
                        val yearItr = yearCheckboxList.iterator()

                        while (yearItr.hasNext()) {
                            val itrNext = yearItr.next()
                            if (data.videoName == itrNext.galleryName) {
                                yearItr.remove()

                            }
                        }
                        val twoYearItr = twoYearCheckboxList.iterator()

                        while (twoYearItr.hasNext()) {
                            val itrNext = twoYearItr.next()
                            if (data.videoName == itrNext.galleryName) {
                                twoYearItr.remove()

                            }
                        }


                    }
                RepositoryImpl.setVideoList(checkboxList)
                RepositoryImpl.setToday(todayCheckboxList)
                RepositoryImpl.setYesterday(yesterdayCheckboxList)
                RepositoryImpl.setMonth(monthCheckboxList)
                RepositoryImpl.setThreeMonth(threeMonthCheckboxList)
                RepositoryImpl.setSixMonth(sixMonthCheckboxList)
                RepositoryImpl.setYearDown(yearDownCheckboxList)
                RepositoryImpl.setYear(yearCheckboxList)
                RepositoryImpl.setTwoYear(twoYearCheckboxList)
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
                        diffTime = check.diffTime,
                        video = check.video,
                        videoSize = check.videoSize
                    )
                )
                for(diff in checkboxList) {
                    if ((0 < (diff.diffTime / Actions.sec) && 60 > (diff.diffTime / Actions.sec)) || (0 <= (diff.diffTime / Actions.min) && 24 > (diff.diffTime / Actions.min))) {
                        todayCheckboxList.add(CheckStorageData(galleryName = diff.videoName, isSelected = diff.isSelected, position = position, gallery= diff.video, diffTime = diff.diffTime, gallerySize = diff.videoSize))
                    } else if (0 < (diff.diffTime / Actions.hour) && 1 >= (diff.diffTime / Actions.hour)) {
                        yesterdayCheckboxList.add(CheckStorageData(galleryName = diff.videoName, isSelected = diff.isSelected, position = position, gallery= diff.video, diffTime = diff.diffTime, gallerySize = diff.videoSize))
                    } else if (30 > (diff.diffTime / Actions.hour)) {
                        monthCheckboxList.add(CheckStorageData(galleryName = diff.videoName, isSelected = diff.isSelected, position = position, gallery= diff.video, diffTime = diff.diffTime, gallerySize = diff.videoSize))
                    } else if (0 < (diff.diffTime / Actions.day) && 3 > (diff.diffTime / Actions.day)) {
                        threeMonthCheckboxList.add(CheckStorageData(galleryName = diff.videoName, isSelected = diff.isSelected, position = position, gallery= diff.video, diffTime = diff.diffTime, gallerySize = diff.videoSize))
                    } else if (3 <= (diff.diffTime / Actions.day) && 6 > (diff.diffTime / Actions.day)) {
                        sixMonthCheckboxList.add(CheckStorageData(galleryName = diff.videoName, isSelected = diff.isSelected, position = position, gallery= diff.video, diffTime = diff.diffTime, gallerySize = diff.videoSize))
                    } else if (6 <= (diff.diffTime / Actions.day) && 12 > (diff.diffTime / Actions.day)) {
                        yearDownCheckboxList.add(CheckStorageData(galleryName = diff.videoName, isSelected = diff.isSelected, position = position, gallery= diff.video, diffTime = diff.diffTime, gallerySize = diff.videoSize))
                    } else if (2 > (diff.diffTime / Actions.month)) {
                        yearCheckboxList.add(CheckStorageData(galleryName = diff.videoName, isSelected = diff.isSelected, position = position, gallery= diff.video, diffTime = diff.diffTime, gallerySize = diff.videoSize))
                    } else {
                        twoYearCheckboxList.add(CheckStorageData(galleryName = diff.videoName, isSelected = diff.isSelected, position = position, gallery= diff.video, diffTime = diff.diffTime, gallerySize = diff.videoSize))
                    }
                }
            }
        }

        RepositoryImpl.setVideoList(checkboxList)
        RepositoryImpl.setToday(todayCheckboxList)
        RepositoryImpl.setYesterday(yesterdayCheckboxList)
        RepositoryImpl.setMonth(monthCheckboxList)
        RepositoryImpl.setThreeMonth(threeMonthCheckboxList)
        RepositoryImpl.setSixMonth(sixMonthCheckboxList)
        RepositoryImpl.setYearDown(yearDownCheckboxList)
        RepositoryImpl.setYear(yearCheckboxList)
        RepositoryImpl.setTwoYear(twoYearCheckboxList)
        notifyDataSetChanged()
    }


}