package com.example.cartrack.activity

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.cartrack.MainActivity
import com.example.cartrack.R
import com.example.cartrack.databinding.ActivityViewSingleRecordBinding
import com.example.cartrack.model.Items
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class ViewSingleRecordActivity : AppCompatActivity() {

    private lateinit var binding: ActivityViewSingleRecordBinding

    private var _id: String = ""
    var recordListData = Items()
    var firebaseDatabase: FirebaseDatabase? = null
    var databaseReference: DatabaseReference? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_view_single_record)

        binding.toolbar.title = "Customer Data"
        binding.toolbar.setTitleTextColor(resources.getColor(R.color.white))
        binding.toolbar.setNavigationOnClickListener {
            onBackPressed()
        }
        setSupportActionBar(binding.toolbar)
        init()
    }

    private fun init() {
        firebaseDatabase = FirebaseDatabase.getInstance()
        databaseReference = firebaseDatabase!!.getReference("CustomerInfo")

        val bundle: Bundle = intent.extras!!
        recordListData = (bundle.getSerializable("recordList") as Items?)!!

        _id = recordListData.id.toString()
        binding.tvName.text = recordListData.name.toString()
        binding.tvMobile.text = recordListData.mobile.toString()
        binding.tvVehicalNo.text = recordListData.vehical_no.toString()
        binding.tvTestDate.text = recordListData.test_date.toString()
        binding.tvUnit.text = recordListData.unit.toString()
        binding.tvResult.text = recordListData.result.toString()
        binding.tvReTestDate.text = recordListData.re_test_date.toString()
        binding.tvIsPaid.text = recordListData.paid_free.toString()
        binding.tvFee.text = recordListData.fee.toString()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return true
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        return when (id) {
            R.id.item_edit -> {
                val intent = Intent(this, UpdateRecordActivity::class.java)
                intent.putExtra("update", recordListData)
                startActivity(intent)
                true
            }
            R.id.item_delete -> {
                databaseReference!!.child(_id).removeValue()
                Toast.makeText(this, "Record Deleted SuccessFully!!", Toast.LENGTH_SHORT).show()
                Handler().postDelayed({
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                }, 700)
                true
            }
            else -> super.onOptionsItemSelected(item)

        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }
}