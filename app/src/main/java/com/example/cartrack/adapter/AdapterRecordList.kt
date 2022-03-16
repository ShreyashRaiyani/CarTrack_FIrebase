package com.example.cartrack.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.cartrack.R
import com.example.cartrack.model.Items

class AdapterRecordList(var arrList: ArrayList<Items>, var context: Context) :
    RecyclerView.Adapter<AdapterRecordList.ViewHolder>() {

    var itemClick: OnItemClick? = null


    interface OnItemClick {
        fun onItemClick(arrList: Items)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v =
            LayoutInflater.from(parent.context).inflate(R.layout.item_view_record, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {


        holder.tvname.text = arrList[position].name
        holder.tvMobile.text = arrList[position].mobile
        holder.tvVehicalNo.text = arrList[position].vehical_no
        holder.tvTestDate.text = arrList[position].test_date
        holder.tvUnit.text = arrList[position].unit
        holder.tvResult.text = arrList[position].result
        holder.tvReTestDate.text = arrList[position].re_test_date
        holder.tvIsPaid.text = arrList[position].paid_free
        holder.tvFee.text = arrList[position].fee

        holder.imgView.setOnClickListener {
            itemClick?.onItemClick(arrList[position])
        }


    }


    override fun getItemCount(): Int {
        return arrList.size
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var tvname: TextView = view.findViewById(R.id.tvname)
        var tvMobile: TextView = view.findViewById(R.id.tvMobile)
        var tvVehicalNo: TextView = view.findViewById(R.id.tvVehicalNo)
        var tvIsPaid: TextView = view.findViewById(R.id.tvIsPaid)
        var tvTestDate: TextView = view.findViewById(R.id.tvTestDate)
        var tvReTestDate: TextView = view.findViewById(R.id.tvReTestDate)
        var tvResult: TextView = view.findViewById(R.id.tvResult)
        var tvUnit: TextView = view.findViewById(R.id.tvUnit)
        var tvFee: TextView = view.findViewById(R.id.tvFee)
        var imgView: ImageView = view.findViewById(R.id.imgView)
    }
}