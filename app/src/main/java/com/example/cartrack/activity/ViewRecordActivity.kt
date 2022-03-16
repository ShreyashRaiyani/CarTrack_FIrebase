package com.example.cartrack.activity

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.RecyclerView
import com.example.cartrack.MainActivity
import com.example.cartrack.R
import com.example.cartrack.adapter.AdapterRecordList
import com.example.cartrack.adapter.CustomerRecordList
import com.example.cartrack.model.Items
import com.google.firebase.database.*
import com.google.gson.Gson
import java.util.stream.Collectors


class ViewRecordActivity : AppCompatActivity(), AdapterRecordList.OnItemClick,
    CustomerRecordList.OnItemClick {

    lateinit var rvRecordList: RecyclerView
    lateinit var tv_empty: TextView
    lateinit var progressBar: ProgressBar
    var adapterRecordList: AdapterRecordList? = null
    var firebaseDatabase: FirebaseDatabase? = null
    var databaseReference: DatabaseReference? = null
    var customerInfo: Items? = null
    var list: ArrayList<Items> = ArrayList()

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_record)
        val toolbar: Toolbar = findViewById<View>(R.id.toolbar) as Toolbar
        toolbar.title = "Customers Record List"
        toolbar.setTitleTextColor(resources.getColor(R.color.white))
        toolbar.setNavigationOnClickListener {
            onBackPressed()
        }

        firebaseDatabase = FirebaseDatabase.getInstance()
        databaseReference = firebaseDatabase!!.getReference("CustomerInfo")
        customerInfo = Items()


        tv_empty = findViewById(R.id.tv_empty)
        progressBar = findViewById(R.id.progressBar)
        rvRecordList = findViewById(R.id.rvRecordList)

        Handler().postDelayed({
            progressBar.visibility = View.GONE
        }, 2000)

        databaseReference!!.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {

                if (dataSnapshot.childrenCount.toInt() != 0){
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
                        rvRecordList.adapter = adapterRecordList
                        adapterRecordList!!.notifyDataSetChanged()
                        adapterRecordList?.itemClick = this@ViewRecordActivity

                        tv_empty.visibility = View.GONE
                        rvRecordList.visibility = View.VISIBLE

                    } else {
                        tv_empty.visibility = View.VISIBLE
                        rvRecordList.visibility = View.GONE
                    }
                }else {
                    tv_empty.visibility = View.VISIBLE
                    rvRecordList.visibility = View.GONE
                }




                Log.d("itemssssss", list.toString())
            }

            override fun onCancelled(error: DatabaseError) {
                // Failed to read value
                Log.w("TAG", "Failed to read value.", error.toException())
            }
        })


    }


    override fun onItemClick(arrList: Items) {
        val intent = Intent(this, ViewSingleRecordActivity::class.java)
        intent.putExtra("recordList", arrList)
        startActivity(intent)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

}