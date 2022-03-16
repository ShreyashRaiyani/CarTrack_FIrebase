package com.example.cartrack.activity

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import android.widget.ScrollView
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SwitchCompat
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.RecyclerView
import com.example.cartrack.R
import com.example.cartrack.adapter.AdapterSearchList
import com.example.cartrack.adapter.AdapterTodaySearchList
import com.example.cartrack.model.Items
import com.google.firebase.database.*
import com.google.gson.Gson
import net.ozaydin.serkan.easy_csv.EasyCsv
import net.ozaydin.serkan.easy_csv.FileCallback
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import java.util.stream.Collectors


class TodayTestDateActivity : AppCompatActivity(), AdapterSearchList.OnItemClick,
    AdapterTodaySearchList.OnItemClick {
    val WRITE_PERMISSON_REQUEST_CODE = 1
    var formatter: SimpleDateFormat? = null
    var date: Date? = null
    var dataList: MutableList<String> = ArrayList()
    var textString: String? = null
    var simpleSwitch: SwitchCompat? = null
    var firebaseDatabase: FirebaseDatabase? = null
    var databaseReference: DatabaseReference? = null
    var customerInfo: Items? = null

    lateinit var rvTodayRecordList: RecyclerView
    lateinit var tv_empty: TextView
    lateinit var main: ScrollView
    lateinit var btnCreate: TextView
    lateinit var tvDate: TextView
    lateinit var progressBar: ProgressBar
    var adapterRecordList: AdapterTodaySearchList? = null
    var todaysList: ArrayList<Items> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_today_test_date)
        val toolbar: Toolbar = findViewById<View>(R.id.toolbar) as Toolbar
        toolbar.title = "Today's Customer Test Date"
        toolbar.setTitleTextColor(resources.getColor(R.color.white))
        setSupportActionBar(toolbar)

        init()
    }

    private fun init() {

        firebaseDatabase = FirebaseDatabase.getInstance()
        databaseReference = firebaseDatabase!!.getReference("CustomerInfo")
        customerInfo = Items()

        main = findViewById(R.id.main)
        tv_empty = findViewById(R.id.tv_empty)
        btnCreate = findViewById(R.id.btnCreate)
        rvTodayRecordList = findViewById(R.id.rvTodayRecordList)

        formatter = SimpleDateFormat("dd/MM/yyyy")
        date = Date()

        val easyCsv = EasyCsv(this)
        val headerList: MutableList<String> = ArrayList()
        headerList.add("Name#Mobile#Vehical Number#Test Date#TWO/FOUR#FAIL/PASS#Re Test Date#PAID/FREE#Fee-")

        tvDate = findViewById(R.id.tvDate)
        progressBar = findViewById(R.id.progressBar)

        tvDate.text = formatter!!.format(date)

        simpleSwitch = findViewById(R.id.simpleSwitch)

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

                        itemSearch(recordList, 0)

                        simpleSwitch!!.setOnCheckedChangeListener { compoundButton, b ->
                            if (b) {
                                itemSearch(recordList, 1)
                            } else {
                                itemSearch(recordList, 2)
                            }
                        }

                    }
                } else {
                    Handler().postDelayed({
                        progressBar.visibility = View.GONE
                    }, 2000)
                    tv_empty.visibility = View.VISIBLE
                    main.visibility = View.GONE
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // Failed to read value
                Log.w("TAG", "Failed to read value.", error.toException())
            }
        })

        easyCsv.setSeparatorColumn("#")
        easyCsv.setSeperatorLine("-")

        val fileName = "TrackReport"


        btnCreate.setOnClickListener {

            easyCsv.createCsvFile(
                fileName,
                headerList,
                dataList,
                WRITE_PERMISSON_REQUEST_CODE,
                object : FileCallback {
                    override fun onSuccess(file: File) {
                        Toast.makeText(
                            this@TodayTestDateActivity,
                            "Saved file in Download Folder",
                            Toast.LENGTH_SHORT
                        )
                            .show()
                    }

                    override fun onFail(err: String) {
                        Toast.makeText(this@TodayTestDateActivity, err, Toast.LENGTH_SHORT).show()
                    }
                })
        }

    }

    private fun itemSearch(recordList: ArrayList<Items>, x: Int) {

        Handler().postDelayed({
            progressBar.visibility = View.GONE
        }, 2000)

        val formatter = SimpleDateFormat("dd/MM/yyyy")
        val date = Date()
        todaysList = ArrayList()

        for (i in 0 until recordList.size) {
            if (x == 0) {
                if (recordList[i].test_date == formatter!!.format(date)) {
                    todaysList.add(recordList[i])
                }
            } else if (x == 1) {
                if (recordList[i].paid_free == "FREE") {
                    todaysList.add(recordList[i])
                }
            } else if (x == 2) {
                if (recordList[i].paid_free == "PAID") {
                    todaysList.add(recordList[i])
                }
            }

        }

        if (!todaysList.isNullOrEmpty()) {

            dataList = ArrayList()
            for (i in todaysList.indices) {
                dataList.add(
                    "${todaysList[i].name}#${todaysList[i].mobile}#${todaysList[i].vehical_no}#${todaysList[i].test_date}#${todaysList[i].unit}#${todaysList[i].result}#${todaysList[i].re_test_date}#${todaysList[i].paid_free}#${todaysList[i].fee}-"
                )
            }

            adapterRecordList = AdapterTodaySearchList(todaysList, this@TodayTestDateActivity)
            rvTodayRecordList.adapter = adapterRecordList
            adapterRecordList!!.notifyDataSetChanged()
            adapterRecordList?.itemClick = this@TodayTestDateActivity

            tv_empty.visibility = View.GONE
            main.visibility = View.VISIBLE

        } else {
            tv_empty.visibility = View.VISIBLE
            main.visibility = View.GONE
        }


    }

    override fun onItemClick(arrList: Items) {
        val receiver_number = arrList.mobile

        val whatsappIntent = Intent(Intent.ACTION_SEND)
        whatsappIntent.setPackage("com.whatsapp")
        whatsappIntent.type = "text/plain"
        whatsappIntent.putExtra(
            Intent.EXTRA_TEXT,
            "Hello Mr ${arrList.name} Today is your test date so please approched on time."
        )
        whatsappIntent.putExtra("jid", "$receiver_number@s.whatsapp.net")
        if (whatsappIntent.resolveActivity(packageManager) == null) {
            Toast.makeText(this, "Whatsap not installed on your phone", Toast.LENGTH_SHORT).show()
        } else {
            startActivity(whatsappIntent)
        }
    }

}