package com.example.cartrack.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.NonNull
import androidx.recyclerview.widget.RecyclerView
import com.example.cartrack.R
import com.example.cartrack.model.Items
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import java.text.SimpleDateFormat
import java.util.*


class CustomerRecordList(@NonNull options: FirebaseRecyclerOptions<Items>) :
    FirebaseRecyclerAdapter<Items, CustomerRecordList.personsViewholder>(options!!) {

    var itemClick: OnItemClick? = null

    interface OnItemClick {
        fun onItemClick(arrList: Items)
    }

    override fun onBindViewHolder(
        @NonNull holder: personsViewholder,
        position: Int, @NonNull arrList: Items
    ) {

        val formatter = SimpleDateFormat("dd/MM/yyyy")
        val date = Date()

        if (arrList.test_date!! == formatter!!.format(date)) {
            holder.tvname.text = arrList.name
            holder.tvMobile.text = arrList.mobile
            holder.tvVehicalNo.text = arrList.vehical_no
            holder.tvTestDate.text = arrList.test_date
            holder.tvUnit.text = arrList.unit
            holder.tvResult.text = arrList.result
            holder.tvReTestDate.text = arrList.re_test_date
            holder.tvIsPaid.text = arrList.paid_free
            holder.tvFee.text = arrList.fee

            holder.imgView.setOnClickListener {
                itemClick?.onItemClick(arrList)
            }
        }


    }

    @NonNull
    override fun onCreateViewHolder(
        @NonNull parent: ViewGroup,
        viewType: Int
    ): personsViewholder {
        val view: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_view_record, parent, false)
        return personsViewholder(view)
    }


    inner class personsViewholder(@NonNull view: View) :
        RecyclerView.ViewHolder(view) {
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