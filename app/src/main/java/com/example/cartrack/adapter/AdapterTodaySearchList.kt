package com.example.cartrack.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.cartrack.R
import com.example.cartrack.databinding.ItemViewTodayRecordBinding
import com.example.cartrack.model.Items

class AdapterTodaySearchList(var arrList: List<Items>, var context: Context) :
    RecyclerView.Adapter<AdapterTodaySearchList.ViewHolder>() {

    var itemClick: OnItemClick? = null

    interface OnItemClick {
        fun onItemClick(arrList: Items)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding: ItemViewTodayRecordBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.item_view_today_record, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.model = arrList[position]
        holder.binding.imgShare.setOnClickListener {
            itemClick?.onItemClick(arrList[position])
        }

    }


    override fun getItemCount(): Int {
        return arrList.size
    }

    class ViewHolder(var binding: ItemViewTodayRecordBinding) : RecyclerView.ViewHolder(binding.root)

}