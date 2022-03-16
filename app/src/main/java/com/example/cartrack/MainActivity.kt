package com.example.cartrack

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.example.cartrack.activity.AddRecordActivity
import com.example.cartrack.activity.OthersActivity
import com.example.cartrack.activity.SearchRecordActivity
import com.example.cartrack.activity.ViewRecordActivity


class MainActivity : AppCompatActivity() {

    lateinit var llAdd: LinearLayout
    lateinit var llView: LinearLayout
    lateinit var llOthers: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val toolbar: Toolbar = findViewById<View>(R.id.toolbar) as Toolbar
        toolbar.title = "Home"
        toolbar.setTitleTextColor(resources.getColor(R.color.white))

        init()
    }

    private fun init() {


        llAdd = findViewById(R.id.lladd)
        llView = findViewById(R.id.llView)
        llOthers = findViewById(R.id.llOthers)

        llAdd.setOnClickListener {
            startActivity(Intent(this, AddRecordActivity::class.java))
        }

        llView.setOnClickListener {
            startActivity(Intent(this, ViewRecordActivity::class.java))
        }

        llOthers.setOnClickListener {
            startActivity(Intent(this, OthersActivity::class.java))
        }

    }
}