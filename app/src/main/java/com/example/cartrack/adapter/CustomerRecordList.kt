package com.example.cartrack.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.NonNull
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.cartrack.R
import com.example.cartrack.databinding.ItemViewRecordBinding
import com.example.cartrack.model.Items
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions


class CustomerRecordList(@NonNull options: FirebaseRecyclerOptions<Items>) :
    FirebaseRecyclerAdapter<Items, CustomerRecordList.ViewHolder>(options!!) {

    var itemClick: OnItemClick? = null

    interface OnItemClick {
        fun onItemClick(arrList: Items)
    }

    override fun onBindViewHolder(
        @NonNull holder: ViewHolder,
        position: Int, @NonNull arrList: Items
    ) {

        holder.binding.tvname.text = arrList.name
        holder.binding.tvMobile.text = arrList.mobile
        holder.binding.tvVehicalNo.text = arrList.vehical_no
        holder.binding.tvTestDate.text = arrList.test_date
        holder.binding.tvUnit.text = arrList.unit
        holder.binding.tvResult.text = arrList.result
        holder.binding.tvReTestDate.text = arrList.re_test_date
        holder.binding.tvIsPaid.text = arrList.paid_free
        holder.binding.tvFee.text = arrList.fee

        holder.binding.imgView.setOnClickListener {
            itemClick?.onItemClick(arrList)
        }
    }

    @NonNull
    override fun onCreateViewHolder(
        @NonNull parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val binding: ItemViewRecordBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.item_view_record, parent, false)
        return ViewHolder(binding)
    }


    class ViewHolder(var binding: ItemViewRecordBinding) : RecyclerView.ViewHolder(binding.root)
}