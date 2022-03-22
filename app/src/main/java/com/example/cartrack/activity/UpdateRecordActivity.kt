@file:Suppress("DEPRECATION")

package com.example.cartrack.activity

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.Nullable
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.example.cartrack.MainActivity
import com.example.cartrack.R
import com.example.cartrack.databinding.ActivityUpdateRecordBinding
import com.example.cartrack.model.Items
import com.google.firebase.database.*
import java.text.SimpleDateFormat
import java.util.*


class UpdateRecordActivity : AppCompatActivity(), AdapterView.OnItemSelectedListener {

    private lateinit var binding: ActivityUpdateRecordBinding

    private var unit: String = ""
    private var result: String = ""
    private var ispaid: String = ""
    val myCalendar: Calendar = Calendar.getInstance()
    private var _id: String = ""

    var firebaseDatabase: FirebaseDatabase? = null
    var databaseReference: DatabaseReference? = null
    var customerInfo: Items? = null
    var recordListData = Items()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_update_record)

        binding.toolbar.title = "Update Customer Record"
        binding.toolbar.setTitleTextColor(resources.getColor(R.color.white))
        binding.toolbar.setNavigationOnClickListener {
            onBackPressed()
        }

        init()
    }

    private fun init() {

        val bundle: Bundle = intent.extras!!
        recordListData = (bundle.getSerializable("update") as Items?)!!

        firebaseDatabase = FirebaseDatabase.getInstance()
        databaseReference = firebaseDatabase!!.getReference("CustomerInfo")
        customerInfo = Items()


        _id = recordListData.id.toString()
        binding.etName.setText(recordListData.name)
        binding.etMobile.setText(recordListData.mobile)
        binding.etVehicalNo.setText(recordListData.vehical_no)
        binding.etTestDate.setText(recordListData.test_date)
        unit = recordListData.unit.toString()
        result = recordListData.result.toString()
        binding.etReTestDate.setText(recordListData.re_test_date)
        ispaid = recordListData.paid_free.toString()
        binding.etFee.setText(recordListData.fee)

        val date = DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
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

        val reDate = DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
            myCalendar[Calendar.YEAR] = year
            myCalendar[Calendar.MONTH] = monthOfYear
            myCalendar[Calendar.DAY_OF_MONTH] = dayOfMonth
            updateLabel("2")
        }

        binding.etReTestDate.setOnClickListener {
            DatePickerDialog(
                this, reDate, myCalendar[Calendar.YEAR],
                myCalendar[Calendar.MONTH],
                myCalendar[Calendar.DAY_OF_MONTH]
            ).show()
        }

        spinerOfUnit()
        spinerOfResult()
        spinerOfFees()

        binding.btnUpdate.setOnClickListener {
            if (isValidate()) {
                val b = Items()
                b.name = binding.etName.text.toString()
                b.mobile = binding.etMobile.text.toString()
                b.vehical_no = binding.etVehicalNo.text.toString()
                b.test_date = binding.etTestDate.text.toString()
                b.re_test_date = binding.etReTestDate.text.toString()
                b.fee = binding.etFee.text.toString()
                b.unit = unit
                b.result = result
                b.paid_free = ispaid

                updateCourses(
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

        if (s == "1") {
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
                            this@UpdateRecordActivity,
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
        if (unit == "TWO") {
            binding.spinnerUnit.setSelection(0)
        } else {
            binding.spinnerUnit.setSelection(1)
        }
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
                            this@UpdateRecordActivity,
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
        if (result == "FAIL") {
            binding.spinneResult.setSelection(0)
        } else {
            binding.spinneResult.setSelection(1)
        }
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
                            this@UpdateRecordActivity,
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
        if (ispaid == "PAID") {
            binding.spinneFees.setSelection(0)
        } else {
            binding.spinneFees.setSelection(1)
        }
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
                if (message == "FREE") {
                    binding.etFee.setText("0")
                }
            }
        }
    }

    private fun updateCourses(
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
        customerInfo!!.id = _id
        customerInfo!!.name = name
        customerInfo!!.mobile = mobile
        customerInfo!!.vehical_no = vehicalNo
        customerInfo!!.test_date = testDate
        customerInfo!!.unit = unit
        customerInfo!!.result = result
        customerInfo!!.re_test_date = reTestDate
        customerInfo!!.paid_free = paidFree
        customerInfo!!.fee = fee


        databaseReference!!.child(_id)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    dataSnapshot.ref.setValue(customerInfo)
                    Toast.makeText(
                        this@UpdateRecordActivity,
                        "Record Updated SuccessFully!!",
                        Toast.LENGTH_SHORT
                    ).show()
                    Handler().postDelayed({
                        val intent = Intent(this@UpdateRecordActivity, MainActivity::class.java)
                        startActivity(intent)
                        finish()
                    }, 700)
                    binding.progressBar.visibility = View.GONE
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    Toast.makeText(this@UpdateRecordActivity, "Fail to update the Record...", Toast.LENGTH_SHORT).show()
                    binding.progressBar.visibility = View.GONE
                }
            })
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }
}