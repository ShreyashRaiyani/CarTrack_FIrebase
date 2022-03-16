package com.example.cartrack.activity

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.RecyclerView
import com.example.cartrack.R
import com.example.cartrack.adapter.AdapterSearchList
import com.example.cartrack.model.Items
import com.google.firebase.database.*
import com.google.gson.Gson
import java.util.stream.Collectors


class SearchRecordActivity : AppCompatActivity(), AdapterSearchList.OnItemClick {

    lateinit var editSearch: EditText
    lateinit var btnSearch: TextView
    lateinit var rvRecordList: RecyclerView
    lateinit var tv_empty: TextView
    var adapterRecordList: AdapterSearchList? = null
    var firebaseDatabase: FirebaseDatabase? = null
    var databaseReference: DatabaseReference? = null
    var customerInfo: Items? = null
    var todaysList: ArrayList<Items> = ArrayList()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_record)
        val toolbar: Toolbar = findViewById<View>(R.id.toolbar) as Toolbar
        toolbar.title = "Find Customers Record"
        toolbar.setTitleTextColor(resources.getColor(R.color.white))
        toolbar.setNavigationOnClickListener {
            onBackPressed()
        }

        init()
    }

    private fun init() {

        firebaseDatabase = FirebaseDatabase.getInstance()
        databaseReference = firebaseDatabase!!.getReference("CustomerInfo")
        customerInfo = Items()

        editSearch = findViewById(R.id.editSearch)
        btnSearch = findViewById(R.id.btnSearch)
        tv_empty = findViewById(R.id.tv_empty)


        rvRecordList = findViewById(R.id.rvRecordList)


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
                        rvRecordList.adapter = adapterRecordList
                        adapterRecordList!!.notifyDataSetChanged()
                        adapterRecordList?.itemClick = this@SearchRecordActivity
                        rvRecordList.visibility = View.VISIBLE
                        tv_empty.visibility = View.GONE

                        btnSearch.setOnClickListener {
                            filter(editSearch.text.toString(), recordList)
                        }


                    } else {
                        tv_empty.visibility = View.VISIBLE
                        rvRecordList.visibility = View.GONE
                    }
                } else {
                    tv_empty.visibility = View.VISIBLE
                    rvRecordList.visibility = View.GONE
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