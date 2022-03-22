package com.example.cartrack.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.cartrack.R
import com.example.cartrack.databinding.ItemViewRecordBinding
import com.example.cartrack.model.Items

class AdapterSearchList(var arrList: ArrayList<Items>, var context: Context) :
    RecyclerView.Adapter<AdapterSearchList.ViewHolder>() {

    var itemClick: OnItemClick? = null


    interface OnItemClick {
        fun onItemClick(arrList: Items)
    }

    fun filterList(filterdNames: ArrayList<Items>) {
        this.arrList = filterdNames
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding: ItemViewRecordBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.item_view_record, parent, false)
        return ViewHolder(binding)

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.model = arrList[position]
        holder.binding.imgView.setOnClickListener {
            itemClick?.onItemClick(arrList[position])
        }

    }


    override fun getItemCount(): Int {
        return arrList.size
    }

    class ViewHolder(var binding: ItemViewRecordBinding) : RecyclerView.ViewHolder(binding.root)
}