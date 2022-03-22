package com.example.cartrack.activity

import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.NonNull
import androidx.annotation.Nullable
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.example.cartrack.MainActivity
import com.example.cartrack.R
import com.example.cartrack.databinding.ActivityAddRecordBinding
import com.example.cartrack.model.Items
import com.google.firebase.database.*
import java.text.SimpleDateFormat
import java.util.*


class AddRecordActivity : AppCompatActivity(), AdapterView.OnItemSelectedListener {

    private lateinit var binding: ActivityAddRecordBinding

    private var unit: String = "TWO"
    private var result: String = "FAIL"
    private var ispaid: String = "PAID"
    val myCalendar = Calendar.getInstance()

    var firebaseDatabase: FirebaseDatabase? = null
    var databaseReference: DatabaseReference? = null
    var customerInfo: Items? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_add_record)

        binding.toolbar.title = "Add new Customer Record"
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


        val date = OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
            myCalendar[Calendar.YEAR] = year
            myCalendar[Calendar.MONTH] = monthOfYear
            myCalendar[Calendar.DAY_OF_MONTH] = dayOfMonth
            updateLabel("1")
        }

        binding.etTestDate.setOnClickListener {
            DatePickerDialog(
                this, date, myCalendar[Calendar.YEAR],
                myCalendar[Calendar.MONTH],
                myCalendar[Calendar.DAY_OF_MONTH]
            ).show()
        }

        val Redate = OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
            myCalendar[Calendar.YEAR] = year
            myCalendar[Calendar.MONTH] = monthOfYear
            myCalendar[Calendar.DAY_OF_MONTH] = dayOfMonth
            updateLabel("2")
        }

        binding.etReTestDate.setOnClickListener {
            DatePickerDialog(
                this, Redate, myCalendar[Calendar.YEAR],
                myCalendar[Calendar.MONTH],
                myCalendar[Calendar.DAY_OF_MONTH]
            ).show()
        }

        spinerOfUnit()
        spinerOfResult()
        spinerOfFees()


        binding.btnSubmit.setOnClickListener {
            if (isValidate()) {
                val b = Items()
                b.id = databaseReference!!.push().key.toString()
                b.name = binding.etName.text.toString()
                b.mobile = binding.etMobile.text.toString()
                b.vehical_no = binding.etVehicalNo.text.toString()
                b.test_date = binding.etTestDate.text.toString()
                b.unit = unit
                b.result = result
                b.re_test_date = binding.etReTestDate.text.toString()
                b.paid_free = ispaid
                b.fee = binding.etFee.text.toString()
                addDatatoFirebase(
                    b.id,
                    b.name,
                    b.mobile!!,
                    b.vehical_no!!,
                    b.test_date!!,
                    b.unit!!,
                    b.result!!,
                    b.re_test_date!!,
                    b.paid_free!!,
                    b.fee!!
                )
            }
        }
    }


    private fun isValidate(): Boolean {
        if (binding.etName.text.toString().trim() == "") {
            binding.etName.error = resources.getText(R.string.should_not_blank)
            return false
        } else {
            binding.etName.error = null
            binding.etName.clearFocus()
        }

        if (binding.etMobile.text.toString().trim() == "") {
            binding.etMobile.error = resources.getText(R.string.should_not_blank)
            return false
        } else {
            binding.etMobile.error = null
            binding.etMobile.clearFocus()
        }

        if (binding.etVehicalNo.text.toString().trim() == "") {
            binding.etVehicalNo.error = resources.getText(R.string.should_not_blank)
            return false
        } else {
            binding.etVehicalNo.error = null
            binding.etVehicalNo.clearFocus()
        }

        if (binding.etTestDate.text.toString().trim() == "") {
            binding.etTestDate.error = resources.getText(R.string.should_not_blank)
            return false
        } else {
            binding.etTestDate.error = null
            binding.etTestDate.clearFocus()
        }

        if (binding.etFee.text.toString().trim() == "") {
            binding.etFee.error = resources.getText(R.string.should_not_blank)
            return false
        } else {
            binding.etFee.error = null
            binding.etFee.clearFocus()
        }


        return true
    }

    private fun updateLabel(s: String) {
        val myFormat = "dd/MM/yyyy"

        val sdf = SimpleDateFormat(myFormat, Locale.US)

        if (s.equals("1")) {
            binding.etTestDate.setText(sdf.format(myCalendar.time))
        } else {
            binding.etReTestDate.setText(sdf.format(myCalendar.time))
        }

    }


    private fun spinerOfUnit() {
        val categories: MutableList<String> = ArrayList()
        categories.add("TWO")
        categories.add("FOUR")

        val dataAdapter =
            object : ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categories) {
                override fun getView(
                    position: Int,
                    @Nullable convertView: View?,
                    parent: ViewGroup
                ): View {
                    val view = super.getView(position, convertView, parent)
                    val listItem = view.findViewById<TextView>(android.R.id.text1)
                    listItem.setTextColor(
                        ContextCompat.getColor(
                            this@AddRecordActivity,
                            R.color.colorSecondary
                        )
                    )
                    listItem.textSize = 17f
                    return view
                }
            }
        dataAdapter.setDropDownViewResource(R.layout.spinner_layout)
        binding.spinnerUnit.adapter = dataAdapter
        binding.spinnerUnit.onItemSelectedListener = this
    }

    private fun spinerOfResult() {
        val categories: MutableList<String> = ArrayList()
        categories.add("FAIL")
        categories.add("PASS")

        val dataAdapter =
            object : ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categories) {
                override fun getView(
                    position: Int,
                    @Nullable convertView: View?,
                    parent: ViewGroup
                ): View {
                    val view = super.getView(position, convertView, parent)
                    val listItem = view.findViewById<TextView>(android.R.id.text1)
                    listItem.setTextColor(
                        ContextCompat.getColor(
                            this@AddRecordActivity,
                            R.color.colorSecondary
                        )
                    )
                    listItem.textSize = 17f
                    return view
                }
            }
        dataAdapter.setDropDownViewResource(R.layout.spinner_layout)
        binding.spinneResult.adapter = dataAdapter
        binding.spinneResult.onItemSelectedListener = this
    }

    private fun spinerOfFees() {
        val categories: MutableList<String> = ArrayList()
        categories.add("PAID")
        categories.add("FREE")

        val dataAdapter =
            object : ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categories) {
                override fun getView(
                    position: Int,
                    @Nullable convertView: View?,
                    parent: ViewGroup
                ): View {
                    val view = super.getView(position, convertView, parent)
                    val listItem = view.findViewById<TextView>(android.R.id.text1)
                    listItem.setTextColor(
                        ContextCompat.getColor(
                            this@AddRecordActivity,
                            R.color.colorSecondary
                        )
                    )
                    listItem.textSize = 17f
                    return view
                }
            }
        dataAdapter.setDropDownViewResource(R.layout.spinner_layout)
        binding.spinneFees.adapter = dataAdapter
        binding.spinneFees.onItemSelectedListener = this
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {

    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {

        when {
            parent!!.id == R.id.spinnerUnit -> {
                val message = parent.getItemAtPosition(position).toString()
                unit = message
            }
            parent.id == R.id.spinneResult -> {
                val message = parent.getItemAtPosition(position).toString()
                result = message
            }
            parent.id == R.id.spinneFees -> {
                val message = parent.getItemAtPosition(position).toString()
                ispaid = message
                if (message.equals("FREE")) {
                    binding.etFee.setText("0")
                } else {
                    binding.etFee.setText("")
                }
            }
        }
    }


    private fun addDatatoFirebase(
        id: String?,
        name: String?,
        mobile: String,
        vehicalNo: String,
        testDate: String,
        unit: String,
        result: String,
        reTestDate: String,
        paidFree: String,
        fee: String
    ) {

        binding.progressBar.visibility = View.VISIBLE

        customerInfo!!.id = id
        customerInfo!!.name = name
        customerInfo!!.mobile = mobile
        customerInfo!!.vehical_no = vehicalNo
        customerInfo!!.test_date = testDate
        customerInfo!!.unit = unit
        customerInfo!!.result = result
        customerInfo!!.re_test_date = reTestDate
        customerInfo!!.paid_free = paidFree
        customerInfo!!.fee = fee

        databaseReference!!.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(@NonNull snapshot: DataSnapshot) {

                databaseReference!!.child(id!!).setValue(customerInfo)

                Toast.makeText(
                    this@AddRecordActivity,
                    "Record Added Sucessfully",
                    Toast.LENGTH_SHORT
                ).show()
                binding.progressBar.visibility = View.GONE

                val intent = Intent(this@AddRecordActivity, MainActivity::class.java)
                startActivity(intent)
                finish()
            }

            override fun onCancelled(@NonNull error: DatabaseError) {
                Toast.makeText(
                    this@AddRecordActivity,
                    "Fail to add data $error",
                    Toast.LENGTH_SHORT
                ).show()
                binding.progressBar.visibility = View.GONE
            }
        })
    }

    override fun onBackPressed() {
        super.onBackPressed()
    }

}