package com.example.cartrack.activity

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.cartrack.R
import com.example.cartrack.adapter.AdapterRecordList
import com.example.cartrack.adapter.CustomerRecordList
import com.example.cartrack.databinding.ActivityViewRecordBinding
import com.example.cartrack.model.Items
import com.google.firebase.database.*
import com.google.gson.Gson
import java.util.stream.Collectors

class ViewRecordActivity : AppCompatActivity(), AdapterRecordList.OnItemClick,
    CustomerRecordList.OnItemClick {
    private lateinit var binding: ActivityViewRecordBinding

    var adapterRecordList: AdapterRecordList? = null
    var firebaseDatabase: FirebaseDatabase? = null
    var databaseReference: DatabaseReference? = null
    var customerInfo: Items? = null

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_view_record)

        binding.toolbar.title = "Customers Record List"
        binding.toolbar.setTitleTextColor(resources.getColor(R.color.white))
        binding.toolbar.setNavigationOnClickListener {
            onBackPressed()
        }

        firebaseDatabase = FirebaseDatabase.getInstance()
        databaseReference = firebaseDatabase!!.getReference("CustomerInfo")
        customerInfo = Items()


        Handler().postDelayed({
            binding.progressBar.visibility = View.GONE
        }, 2000)

        databaseReference!!.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {

                if (dataSnapshot.childrenCount.toInt() != 0) {
                    val items: HashMap<String, Items> = dataSnapshot.getValue() as HashMap<String, Items>

                    val list = items.values.stream().collect(Collectors.toCollection { ArrayList() })
                    val gson = Gson()

                    val recordList: ArrayList<Items> = ArrayList()
                    for (i in 0 until list.size) {
                        val json = gson.toJsonTree(list[i]).asJsonObject
                        recordList.add(gson.fromJson(json, Items::class.java))
                    }

                    if (!recordList.isNullOrEmpty()) {
                        adapterRecordList = AdapterRecordList(recordList, this@ViewRecordActivity)
                        binding.rvRecordList.adapter = adapterRecordList
                        adapterRecordList!!.notifyDataSetChanged()
                        adapterRecordList?.itemClick = this@ViewRecordActivity

                        binding.tvEmpty.visibility = View.GONE
                        binding.rvRecordList.visibility = View.VISIBLE

                    } else {
                        binding.tvEmpty.visibility = View.VISIBLE
                        binding.rvRecordList.visibility = View.GONE
                    }
                } else {
                    binding.tvEmpty.visibility = View.VISIBLE
                    binding.rvRecordList.visibility = View.GONE
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.w("TAG", "Failed to read value.", error.toException())
            }
        })

    }

    override fun onItemClick(arrList: Items) {
        val intent = Intent(this, ViewSingleRecordActivity::class.java)
        intent.putExtra("recordList", arrList)
        startActivity(intent)
    }
}