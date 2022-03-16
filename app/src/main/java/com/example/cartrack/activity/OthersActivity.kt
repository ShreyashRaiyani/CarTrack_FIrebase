package com.example.cartrack.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import androidx.appcompat.widget.Toolbar
import com.example.cartrack.R

class OthersActivity : AppCompatActivity() {

    lateinit var llToday: LinearLayout
    lateinit var llSearch: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_others)
        val toolbar: Toolbar = findViewById<View>(R.id.toolbar) as Toolbar
        toolbar.title = "Other Functions"
        toolbar.setTitleTextColor(resources.getColor(R.color.white))
        toolbar.setNavigationOnClickListener {
            onBackPressed()
        }

        init()
    }

    private fun init() {
        llToday = findViewById(R.id.llTodaysRecord)
        llSearch = findViewById(R.id.llSearch)

        llToday.setOnClickListener {
            startActivity(Intent(this, TodayTestDateActivity::class.java))
        }

        llSearch.setOnClickListener {
            startActivity(Intent(this, SearchRecordActivity::class.java))
        }
    }
}