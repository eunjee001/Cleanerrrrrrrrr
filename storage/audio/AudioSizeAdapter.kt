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

class AudioSizeAdapter(private var audioSizeList: ArrayList<AudioSizeData>, val context:Context) : RecyclerView.Adapter<AudioSizeAdapter.Holder>() {

    companion object {
        var checkboxList = arrayListOf<CheckAudioData>()
        val twoGBCheckboxList =arrayListOf<CheckStorageData>()
        val oneGBCheckboxList = arrayListOf<CheckStorageData>()
        val oneGBDownCheckboxList = arrayListOf<CheckStorageData>()
        val fiveMBDownCheckboxList = arrayListOf<CheckStorageData>()
        val oneMBDownCheckboxList = arrayListOf<CheckStorageData>()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        return Holder(
            LayoutInflater.from(parent.context).inflate(R.layout.recycler_audio, parent, false)
        )
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {

        holder.audioIcon!!.setImageResource(audioSizeList[position].audioIcon)
        holder.audioSize!!.text = Formatter.formatFileSize(context, audioSizeList[position].audioSize)
        holder.audioName!!.text = audioSizeList[position].audioName.toString()
        holder.audioTime!!.text = audioSizeList[position].audioTime.toString()
        holder.audioCheck.isChecked = audioSizeList[position].audioChoose

        holder.bind(audioSizeList[position], position)

    }

    override fun getItemCount(): Int {
        return audioSizeList.size
    }
    fun set(sixMonthListCategory: java.util.ArrayList<AudioSizeData>) {
        audioSizeList = sixMonthListCategory
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

        fun bind(data: AudioSizeData, position: Int) {

            audioCheck.setOnClickListener {
                    if(audioCheck.isChecked)  {
                        checkboxList.add(CheckAudioData(audioName = data.audioName, isSelected = audioCheck.isSelected, position = position, audioUri = data.audioUri, diffTime = 0, audioSize = data.audioSize))
                        data.audioChoose = true

                        for (diff in checkboxList) {
                            if ( (0 <= diff.audioSize) || 0 < (diff.audioSize / Actions.MB) && 100 > (diff.audioSize  / Actions.MB)) {
                                oneMBDownCheckboxList.add(CheckStorageData(galleryName = diff.audioName ,isSelected = diff.isSelected, position = 0, gallerySize = diff.audioSize,diffTime = diff.diffTime, gallery = diff.audioUri))
                            } else if (100 <= (diff.audioSize  / Actions.MB) && 500 > (diff.audioSize  / Actions.MB)) {
                                fiveMBDownCheckboxList.add(CheckStorageData(galleryName = diff.audioName ,isSelected = diff.isSelected, position = 0, gallerySize = diff.audioSize,diffTime = diff.diffTime, gallery = diff.audioUri))
                            } else if (500 <= (diff.audioSize  / Actions.MB) && 1 > (diff.audioSize  / Actions.GB)) {
                                oneGBDownCheckboxList.add(CheckStorageData(galleryName = diff.audioName ,isSelected = diff.isSelected, position = 0, gallerySize = diff.audioSize,diffTime = diff.diffTime, gallery = diff.audioUri))
                            } else if (1 <= (diff.audioSize  / Actions.GB) && 2 > (diff.audioSize  / Actions.GB)) {
                                oneGBCheckboxList.add(CheckStorageData(galleryName = diff.audioName ,isSelected = diff.isSelected, position = 0, gallerySize = diff.audioSize,diffTime = diff.diffTime, gallery = diff.audioUri))
                            } else if (2 <= (diff.audioSize  / Actions.GB)) {
                                twoGBCheckboxList.add(CheckStorageData(galleryName = diff.audioName ,isSelected = diff.isSelected, position = 0, gallerySize = diff.audioSize,diffTime = diff.diffTime, gallery = diff.audioUri))
                            }
                        }
                    }else{
                        val itr = checkboxList.iterator()

                        while (itr.hasNext()){
                            val itrNext = itr.next()
                            if(data.audioName == itrNext.audioName){
                                itr.remove()

                            }
                        }
                        val oneMBItr = oneMBDownCheckboxList.iterator()

                        while (oneMBItr.hasNext()){
                            val itrNext = oneMBItr.next()
                            if(data.audioName == itrNext.galleryName){
                                oneMBItr.remove()

                            }
                        }
                        val fiveMBItr = fiveMBDownCheckboxList.iterator()

                        while (fiveMBItr.hasNext()){
                            val itrNext = fiveMBItr.next()
                            if(data.audioName == itrNext.galleryName){
                                fiveMBItr.remove()

                            }
                        }
                        val oneGBDownItr = oneGBDownCheckboxList.iterator()

                        while (oneGBDownItr.hasNext()){
                            val itrNext = oneGBDownItr.next()
                            if(data.audioName == itrNext.galleryName){
                                oneGBDownItr.remove()

                            }
                        }
                        val oneGBItr = oneGBCheckboxList.iterator()

                        while (oneGBItr.hasNext()){
                            val itrNext = oneGBItr.next()
                            if(data.audioName == itrNext.galleryName){
                                oneGBItr.remove()

                            }
                        }
                        val twoGBItr = twoGBCheckboxList.iterator()

                        while (twoGBItr.hasNext()){
                            val itrNext = twoGBItr.next()
                            if(data.audioName == itrNext.galleryName){
                                twoGBItr.remove()

                            }
                        }

                    }

                    RepositoryImpl.setAudioList(checkboxList)
                RepositoryImpl.setOneMB(oneMBDownCheckboxList)
                RepositoryImpl.setFiveMB(fiveMBDownCheckboxList)
                RepositoryImpl.setOneGBDown(oneGBDownCheckboxList)
                RepositoryImpl.setOneGB(oneGBCheckboxList)
                RepositoryImpl.setTwoGB(twoGBCheckboxList)

            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setCheckAll(boolean: Boolean, position: Int){
        for(check in audioSizeList) {
            if (check.audioChoose == !boolean) {
                check.audioChoose = boolean
                checkboxList.add(
                    CheckAudioData(
                        audioName = check.audioName,
                        isSelected = check.audioChoose,
                        position = position,
                        audioSize = check.audioSize,
                        diffTime = 0,
                        audioUri = check.audioUri
                    )
                )
                for (diff in checkboxList) {
                    if ( (0 <= diff.audioSize) || 0 < (diff.audioSize / Actions.MB) && 100 > (diff.audioSize  / Actions.MB)) {
                        oneMBDownCheckboxList.add(CheckStorageData(galleryName = diff.audioName ,isSelected = diff.isSelected, position = 0, gallerySize = diff.audioSize,diffTime = diff.diffTime, gallery = diff.audioUri))
                    } else if (100 <= (diff.audioSize  / Actions.MB) && 500 > (diff.audioSize  / Actions.MB)) {
                        fiveMBDownCheckboxList.add(CheckStorageData(galleryName = diff.audioName ,isSelected = diff.isSelected, position = 0, gallerySize = diff.audioSize,diffTime = diff.diffTime, gallery = diff.audioUri))
                    } else if (500 <= (diff.audioSize  / Actions.MB) && 1 > (diff.audioSize  / Actions.GB)) {
                        oneGBDownCheckboxList.add(CheckStorageData(galleryName = diff.audioName ,isSelected = diff.isSelected, position = 0, gallerySize = diff.audioSize,diffTime = diff.diffTime, gallery = diff.audioUri))
                    } else if (1 <= (diff.audioSize  / Actions.GB) && 2 > (diff.audioSize  / Actions.GB)) {
                        oneGBCheckboxList.add(CheckStorageData(galleryName = diff.audioName ,isSelected = diff.isSelected, position = 0, gallerySize = diff.audioSize,diffTime = diff.diffTime, gallery = diff.audioUri))
                    } else if (2 <= (diff.audioSize  / Actions.GB)) {
                        twoGBCheckboxList.add(CheckStorageData(galleryName = diff.audioName ,isSelected = diff.isSelected, position = 0, gallerySize = diff.audioSize,diffTime = diff.diffTime, gallery = diff.audioUri))
                    }
                }
            }


        }
        RepositoryImpl.setOneMB(oneMBDownCheckboxList)
        RepositoryImpl.setFiveMB(fiveMBDownCheckboxList)
        RepositoryImpl.setOneGBDown(oneGBDownCheckboxList)
        RepositoryImpl.setOneGB(oneGBCheckboxList)
        RepositoryImpl.setTwoGB(twoGBCheckboxList)
        RepositoryImpl.setAudioList(checkboxList)

        notifyDataSetChanged()
    }

}