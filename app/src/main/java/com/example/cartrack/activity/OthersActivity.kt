package com.example.cartrack.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import androidx.appcompat.widget.Toolbar
import androidx.databinding.DataBindingUtil
import com.example.cartrack.R
import com.example.cartrack.databinding.ActivityAddRecordBinding
import com.example.cartrack.databinding.ActivityOthersBinding

class OthersActivity : AppCompatActivity() {

    private lateinit var binding: ActivityOthersBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_others)

        binding.toolbar.title = "Other Functions"
        binding.toolbar.setTitleTextColor(resources.getColor(R.color.white))
        binding.toolbar.setNavigationOnClickListener {
            onBackPressed()
        }

        init()
    }

    private fun init() {

        binding.llTodaysRecord.setOnClickListener {
            startActivity(Intent(this, TodayTestDateActivity::class.java))
        }

        binding.llSearch.setOnClickListener {
            startActivity(Intent(this, SearchRecordActivity::class.java))
        }
    }
}