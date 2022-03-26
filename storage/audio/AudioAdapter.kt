package com.kyoungss.cleaner.storage.audio

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
import com.kyoungss.cleaner.storage.CheckStorageData
import kotlin.collections.ArrayList

class AudioAdapter(private var audioList: ArrayList<AudioData>, var context:Context) : RecyclerView.Adapter<AudioAdapter.Holder>() {

    companion object {
        var checkboxList = arrayListOf<CheckAudioData>()
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
            LayoutInflater.from(parent.context).inflate(R.layout.recycler_audio, parent, false)
        )
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {

        holder.audioIcon!!.setImageResource(audioList[position].audioIcon)
        holder.audioSize!!.text = Formatter.formatFileSize(context, audioList[position].audioSize)
        holder.audioName!!.text = audioList[position].audioName.toString()
        holder.audioTime!!.text = audioList[position].audioTime.toString()

        holder.audioCheck.isChecked = audioList[position].audioChoose
        holder.bind(audioList[position], position)

    }

    override fun getItemCount(): Int {
        return audioList.size
    }
    fun set(sixMonthListCategory: java.util.ArrayList<AudioData>) {
        audioList = sixMonthListCategory
        notifyDataSetChanged()
    }

    class Holder(itemView: View?) : RecyclerView.ViewHolder(itemView!!) {
        var audioIcon : ImageView?= itemView!!.findViewById(R.id.audio_icon)
        var audioName: TextView? = itemView!!.findViewById(R.id.audio_name)
        var audioTime: TextView? = itemView?.findViewById(R.id.audio_time)
        var audioSize: TextView? = itemView?.findViewById(R.id.audio_size)
        //val data=ImageData
        var audioCheck: CheckBox = itemView!!.findViewById(R.id.audio_check)
       // var dateAdapter: ImageAdapter = ImageAdapter(galleryListCategory)

        fun bind(data: AudioData, position: Int) {

            audioCheck.setOnClickListener {

                    if(audioCheck.isChecked)  {
                        checkboxList.add(CheckAudioData(audioName = data.audioName, isSelected = audioCheck.isSelected, position = position, diffTime = data.diffTime, audioUri = data.audioUri, audioSize = data.audioSize))
                        data.audioChoose =true
                        for(diff in checkboxList) {
                            if ((0 < (diff.diffTime / Actions.sec) && 60 > (diff.diffTime / Actions.sec)) || (0 <= (diff.diffTime / Actions.min) && 24 > (diff.diffTime / Actions.min))) {
                                todayCheckboxList.add(CheckStorageData(galleryName = diff.audioName, isSelected = audioCheck.isSelected, position = position, gallery= diff.audioUri, diffTime = diff.diffTime, gallerySize = diff.audioSize))
                            } else if (0 < (diff.diffTime / Actions.hour) && 1 >= (diff.diffTime / Actions.hour)) {
                                yesterdayCheckboxList.add(CheckStorageData(galleryName = diff.audioName, isSelected = audioCheck.isSelected, position = position, gallery= diff.audioUri, diffTime = diff.diffTime, gallerySize = diff.audioSize))
                            } else if (30 > (diff.diffTime / Actions.hour)) {
                                monthCheckboxList.add(CheckStorageData(galleryName = diff.audioName, isSelected = audioCheck.isSelected, position = position, gallery= diff.audioUri, diffTime = diff.diffTime, gallerySize = diff.audioSize))
                            } else if (0 < (diff.diffTime / Actions.day) && 3 > (diff.diffTime / Actions.day)) {
                                threeMonthCheckboxList.add(CheckStorageData(galleryName = diff.audioName, isSelected = audioCheck.isSelected, position = position, gallery= diff.audioUri, diffTime = diff.diffTime, gallerySize = diff.audioSize))
                            } else if (3 <= (diff.diffTime / Actions.day) && 6 > (diff.diffTime / Actions.day)) {
                                sixMonthCheckboxList.add(CheckStorageData(galleryName = diff.audioName, isSelected = audioCheck.isSelected, position = position, gallery= diff.audioUri, diffTime = diff.diffTime, gallerySize = diff.audioSize))
                            } else if (6 <= (diff.diffTime / Actions.day) && 12 > (diff.diffTime / Actions.day)) {
                                yearDownCheckboxList.add(CheckStorageData(galleryName = diff.audioName, isSelected = audioCheck.isSelected, position = position, gallery= diff.audioUri, diffTime = diff.diffTime, gallerySize = diff.audioSize))
                            } else if (2 > (diff.diffTime / Actions.month)) {
                                yearCheckboxList.add(CheckStorageData(galleryName = diff.audioName, isSelected = audioCheck.isSelected, position = position, gallery= diff.audioUri, diffTime = diff.diffTime, gallerySize = diff.audioSize))
                            } else if(2< (diff.diffTime/Actions.month)){
                                twoYearCheckboxList.add(CheckStorageData(galleryName = diff.audioName, isSelected = audioCheck.isSelected, position = position, gallery= diff.audioUri, diffTime = diff.diffTime, gallerySize = diff.audioSize))
                            }
                        }
                    }else{

                        val itr = checkboxList.iterator()
                        while (itr.hasNext()){
                            val itrNext = itr.next()
                            if (data.audioName == itrNext.audioName){
                                itr.remove()
                            }
                        }

                        val todayItr = todayCheckboxList.iterator()

                        while (todayItr.hasNext()){
                            val itrNext = todayItr.next()
                            if(data.audioName == itrNext.galleryName){
                                todayItr.remove()

                            }
                        }
                        val yesterdayItr = yesterdayCheckboxList.iterator()

                        while (yesterdayItr.hasNext()){
                            val itrNext = yesterdayItr.next()
                            if(data.audioName == itrNext.galleryName){
                                yesterdayItr.remove()

                            }
                        }
                        val monthItr = monthCheckboxList.iterator()

                        while (monthItr.hasNext()){
                            val itrNext = monthItr.next()
                            if(data.audioName == itrNext.galleryName){
                                monthItr.remove()

                            }
                        }
                        val threeMonthItr = threeMonthCheckboxList.iterator()

                        while (threeMonthItr.hasNext()){
                            val itrNext = threeMonthItr.next()
                            if(data.audioName == itrNext.galleryName){
                                threeMonthItr.remove()

                            }
                        }
                        val sixMonthItr = sixMonthCheckboxList.iterator()

                        while (sixMonthItr.hasNext()){
                            val itrNext = sixMonthItr.next()
                            if(data.audioName == itrNext.galleryName){
                                sixMonthItr.remove()

                            }
                        }
                        val yearDownItr = yearDownCheckboxList.iterator()

                        while (yearDownItr.hasNext()){
                            val itrNext = yearDownItr.next()
                            if(data.audioName == itrNext.galleryName){
                                yearDownItr.remove()

                            }
                        }
                        val yearItr = yearCheckboxList.iterator()

                        while (yearItr.hasNext()){
                            val itrNext = yearItr.next()
                            if(data.audioName == itrNext.galleryName){
                                yearItr.remove()

                            }
                        }
                        val twoYearItr = twoYearCheckboxList.iterator()

                        while (twoYearItr.hasNext()){
                            val itrNext = twoYearItr.next()
                            if(data.audioName == itrNext.galleryName){
                                twoYearItr.remove()

                            }
                        }


                    }
                RepositoryImpl.setAudioList(checkboxList)
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
        for(check in audioList) {
            if (check.audioChoose == !boolean) {
                check.audioChoose = boolean

                checkboxList.add(
                    CheckAudioData(
                        audioName = check.audioName,
                        isSelected = check.audioChoose,
                        position = position,
                        diffTime = check.diffTime,
                        audioSize = check.audioSize,
                        audioUri = check.audioUri
                    )
                )
                for(diff in checkboxList) {
                    if ((0 < (diff.diffTime / Actions.sec) && 60 > (diff.diffTime / Actions.sec)) || (0 <= (diff.diffTime / Actions.min) && 24 > (diff.diffTime / Actions.min))) {
                        todayCheckboxList.add(CheckStorageData(galleryName = diff.audioName, isSelected = diff.isSelected, position = position, gallery= diff.audioUri, diffTime = diff.diffTime, gallerySize = diff.audioSize))
                    } else if (0 < (diff.diffTime / Actions.hour) && 1 >= (diff.diffTime / Actions.hour)) {
                        yesterdayCheckboxList.add(CheckStorageData(galleryName = diff.audioName, isSelected = diff.isSelected, position = position, gallery= diff.audioUri, diffTime = diff.diffTime, gallerySize = diff.audioSize))
                    } else if (30 > (diff.diffTime / Actions.hour)) {
                        monthCheckboxList.add(CheckStorageData(galleryName = diff.audioName, isSelected = diff.isSelected, position = position, gallery= diff.audioUri, diffTime = diff.diffTime, gallerySize = diff.audioSize))
                    } else if (0 < (diff.diffTime / Actions.day) && 3 > (diff.diffTime / Actions.day)) {
                        threeMonthCheckboxList.add(CheckStorageData(galleryName = diff.audioName, isSelected = diff.isSelected, position = position, gallery= diff.audioUri, diffTime = diff.diffTime, gallerySize = diff.audioSize))
                    } else if (3 <= (diff.diffTime / Actions.day) && 6 > (diff.diffTime / Actions.day)) {
                        sixMonthCheckboxList.add(CheckStorageData(galleryName = diff.audioName, isSelected = diff.isSelected, position = position, gallery= diff.audioUri, diffTime = diff.diffTime, gallerySize = diff.audioSize))
                    } else if (6 <= (diff.diffTime / Actions.day) && 12 > (diff.diffTime / Actions.day)) {
                        yearDownCheckboxList.add(CheckStorageData(galleryName = diff.audioName, isSelected = diff.isSelected, position = position, gallery= diff.audioUri, diffTime = diff.diffTime, gallerySize = diff.audioSize))
                    } else if (2 > (diff.diffTime / Actions.month)) {
                        yearCheckboxList.add(CheckStorageData(galleryName = diff.audioName, isSelected = diff.isSelected, position = position, gallery= diff.audioUri, diffTime = diff.diffTime, gallerySize = diff.audioSize))
                    } else if(2< (diff.diffTime/Actions.month)){
                        twoYearCheckboxList.add(CheckStorageData(galleryName = diff.audioName, isSelected = diff.isSelected, position = position, gallery= diff.audioUri, diffTime = diff.diffTime, gallerySize = diff.audioSize))
                    }
                }
            }

        }
        RepositoryImpl.setAudioList(checkboxList)
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