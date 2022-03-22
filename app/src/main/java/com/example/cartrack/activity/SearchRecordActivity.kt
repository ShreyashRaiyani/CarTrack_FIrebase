package com.example.cartrack.activity

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.cartrack.R
import com.example.cartrack.adapter.AdapterSearchList
import com.example.cartrack.databinding.ActivitySearchRecordBinding
import com.example.cartrack.model.Items
import com.google.firebase.database.*
import com.google.gson.Gson
import java.util.stream.Collectors


class SearchRecordActivity : AppCompatActivity(), AdapterSearchList.OnItemClick {

    private lateinit var binding: ActivitySearchRecordBinding

    var adapterRecordList: AdapterSearchList? = null
    var firebaseDatabase: FirebaseDatabase? = null
    var databaseReference: DatabaseReference? = null
    var customerInfo: Items? = null
    var todaysList: ArrayList<Items> = ArrayList()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_search_record)

        binding.toolbar.title = "Find Customers Record"
        binding.toolbar.setTitleTextColor(resources.getColor(R.color.white))
        binding.toolbar.setNavigationOnClickListener {
            onBackPressed()
        }

        init()
    }

    private fun init() {

        firebaseDatabase = FirebaseDatabase.getInstance()
        databaseReference = firebaseDatabase!!.getReference("CustomerInfo")
        customerInfo = Items()

        databaseReference!!.addValueEventListener(object : ValueEventListener {
            @RequiresApi(Build.VERSION_CODES.N)
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
                        adapterRecordList = AdapterSearchList(recordList, this@SearchRecordActivity)
                        binding.rvRecordList.adapter = adapterRecordList
                        adapterRecordList!!.notifyDataSetChanged()
                        adapterRecordList?.itemClick = this@SearchRecordActivity
                        binding.rvRecordList.visibility = View.VISIBLE
                        binding.tvEmpty .visibility = View.GONE

                        binding.btnSearch.setOnClickListener {
                            filter(binding.editSearch.text.toString(), recordList)
                        }


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
                // Failed to read value
                Log.w("TAG", "Failed to read value.", error.toException())
            }
        })


    }

    private fun filter(text: String, todaysList: ArrayList<Items>) {
        //new array list that will hold the filtered data
        val filterdNames: ArrayList<Items> = ArrayList()

        //looping through existing elements
        for (s in todaysList) {
            //if the existing elements contains the search input
            if (s.name!!.contains(text) || s.name!!.toLowerCase().contains(text.toLowerCase())) {
                //adding the element to filtered list
                filterdNames.add(s)
            }
            if (s.paid_free!!.contains(text) || s.paid_free!!.toLowerCase().contains(text.toLowerCase())) {
                //adding the element to filtered list
                filterdNames.add(s)
            }
            if (s.test_date!!.contains(text.toLowerCase())) {
                //adding the element to filtered list
                filterdNames.add(s)
            }
            if (s.mobile!!.contains(text.toLowerCase())) {
                //adding the element to filtered list
                filterdNames.add(s)
            }
            if (s.result!!.contains(text) || s.result!!.toLowerCase().contains(text.toLowerCase())) {
                //adding the element to filtered list
                filterdNames.add(s)
            }
            if (s.vehical_no!!.contains(text) || s.vehical_no!!.toLowerCase().contains(text.toLowerCase())) {
                //adding the element to filtered list
                filterdNames.add(s)
            }
            if (s.re_test_date!!.contains(text.toLowerCase())) {
                //adding the element to filtered list
                filterdNames.add(s)
            }
            if (s.unit!!.contains(text) || s.unit!!.toLowerCase().contains(text.toLowerCase())) {
                //adding the element to filtered list
                filterdNames.add(s)
            }
        }

        //calling a method of the adapter class and passing the filtered list
        adapterRecordList!!.filterList(filterdNames)
    }

    override fun onItemClick(arrList: Items) {
        val intent = Intent(this, ViewSingleRecordActivity::class.java)
        intent.putExtra("recordList", arrList)
        startActivity(intent)
    }

}