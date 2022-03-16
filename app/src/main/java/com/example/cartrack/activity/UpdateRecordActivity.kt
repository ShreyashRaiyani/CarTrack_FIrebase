@file:Suppress("DEPRECATION")

package com.example.cartrack.activity

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.annotation.Nullable
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProviders
import com.example.cartrack.MainActivity
import com.example.cartrack.R
import com.example.cartrack.model.Items
import com.google.firebase.database.*
import java.text.SimpleDateFormat
import java.util.*


class UpdateRecordActivity : AppCompatActivity(), AdapterView.OnItemSelectedListener {

    lateinit var spinnerUnit: Spinner
    lateinit var spinneResult: Spinner
    lateinit var spinneFees: Spinner
    lateinit var parentRelative: RelativeLayout
    lateinit var etTestDate: EditText
    lateinit var etReTestDate: EditText
    lateinit var etName: EditText
    lateinit var etMobile: EditText
    lateinit var etVehicalNo: EditText
    lateinit var etFee: EditText
    lateinit var btnUpdate: TextView
    lateinit var progressBar: ProgressBar
    private var unit: String = ""
    private var result: String = ""
    private var ispaid: String = ""
    val myCalendar = Calendar.getInstance()
    private var _id: String = ""

    var firebaseDatabase: FirebaseDatabase? = null
    var databaseReference: DatabaseReference? = null
    var customerInfo: Items? = null
    var recordListData = Items()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update_record)
        val toolbar: Toolbar = findViewById<View>(R.id.toolbar) as Toolbar
        toolbar.title = "Update Customer Record"
        toolbar.setTitleTextColor(resources.getColor(R.color.white))
        toolbar.setNavigationOnClickListener {
            onBackPressed()
        }

        init()
    }

    private fun init() {

        var bundle: Bundle
        bundle = intent.extras!!
        recordListData = (bundle.getSerializable("update") as Items?)!!

        firebaseDatabase = FirebaseDatabase.getInstance()
        databaseReference = firebaseDatabase!!.getReference("CustomerInfo")
        customerInfo = Items()

        parentRelative = findViewById(R.id.parentRelative)
        spinnerUnit = findViewById(R.id.spinnerUnit)
        spinneResult = findViewById(R.id.spinneResult)
        spinneFees = findViewById(R.id.spinneFees)
        etTestDate = findViewById(R.id.etTestDate)
        etReTestDate = findViewById(R.id.etReTestDate)
        etName = findViewById(R.id.etName)
        etMobile = findViewById(R.id.etMobile)
        etVehicalNo = findViewById(R.id.etVehicalNo)
        etFee = findViewById(R.id.etFee)
        btnUpdate = findViewById(R.id.btnUpdate)
        progressBar = findViewById(R.id.progressBar)

        _id = recordListData.id.toString()
        etName.setText(recordListData.name)
        etMobile.setText(recordListData.mobile)
        etVehicalNo.setText(recordListData.vehical_no)
        etTestDate.setText(recordListData.test_date)
        unit = recordListData.unit.toString()
        result = recordListData.result.toString()
        etReTestDate.setText(recordListData.re_test_date)
        ispaid = recordListData.paid_free.toString()
        etFee.setText(recordListData.fee)

        val date = DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
            myCalendar[Calendar.YEAR] = year
            myCalendar[Calendar.MONTH] = monthOfYear
            myCalendar[Calendar.DAY_OF_MONTH] = dayOfMonth
            updateLabel("1")
        }

        etTestDate.setOnClickListener {
            DatePickerDialog(
                this, date, myCalendar[Calendar.YEAR],
                myCalendar[Calendar.MONTH],
                myCalendar[Calendar.DAY_OF_MONTH]
            ).show()
        }

        val Redate = DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
            myCalendar[Calendar.YEAR] = year
            myCalendar[Calendar.MONTH] = monthOfYear
            myCalendar[Calendar.DAY_OF_MONTH] = dayOfMonth
            updateLabel("2")
        }

        etReTestDate.setOnClickListener {
            DatePickerDialog(
                this, Redate, myCalendar[Calendar.YEAR],
                myCalendar[Calendar.MONTH],
                myCalendar[Calendar.DAY_OF_MONTH]
            ).show()
        }

        spinerOfUnit()
        spinerOfResult()
        spinerOfFees()

        btnUpdate.setOnClickListener {
            if (isValidate()) {
                val b = Items()
                b.name = etName.text.toString()
                b.mobile = etMobile.text.toString()
                b.vehical_no = etVehicalNo.text.toString()
                b.test_date = etTestDate.text.toString()
                b.re_test_date = etReTestDate.text.toString()
                b.fee = etFee.text.toString()
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
        if (etName.text.toString().trim() == "") {
            etName.error = resources.getText(R.string.should_not_blank)
            return false
        } else {
            etName.error = null
            etName.clearFocus()
        }

        if (etMobile.text.toString().trim() == "") {
            etMobile.error = resources.getText(R.string.should_not_blank)
            return false
        } else {
            etMobile.error = null
            etMobile.clearFocus()
        }

        if (etVehicalNo.text.toString().trim() == "") {
            etVehicalNo.error = resources.getText(R.string.should_not_blank)
            return false
        } else {
            etVehicalNo.error = null
            etVehicalNo.clearFocus()
        }

        if (etTestDate.text.toString().trim() == "") {
            etTestDate.error = resources.getText(R.string.should_not_blank)
            return false
        } else {
            etTestDate.error = null
            etTestDate.clearFocus()
        }

        if (etFee.text.toString().trim() == "") {
            etFee.error = resources.getText(R.string.should_not_blank)
            return false
        } else {
            etFee.error = null
            etFee.clearFocus()
        }


        return true
    }

    private fun updateLabel(s: String) {
        val myFormat = "dd/MM/yyyy"

        val sdf = SimpleDateFormat(myFormat, Locale.US)

        if (s.equals("1")) {
            etTestDate.setText(sdf.format(myCalendar.time))
        } else {
            etReTestDate.setText(sdf.format(myCalendar.time))
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
        spinnerUnit.adapter = dataAdapter
        spinnerUnit.onItemSelectedListener = this
        if (unit.equals("TWO")) {
            spinnerUnit.setSelection(0)
        } else {
            spinnerUnit.setSelection(1)
        }
    }

    private fun spinerOfResult() {
        // Spinner Drop down elements
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
        spinneResult.adapter = dataAdapter
        spinneResult.onItemSelectedListener = this
        if (result.equals("FAIL")) {
            spinneResult.setSelection(0)
        } else {
            spinneResult.setSelection(1)
        }
    }

    private fun spinerOfFees() {
        // Spinner Drop down elements
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
        spinneFees.adapter = dataAdapter
        spinneFees.onItemSelectedListener = this
        if (ispaid.equals("PAID")) {
            spinneFees.setSelection(0)
        } else {
            spinneFees.setSelection(1)
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
                if (message.equals("FREE")) {
                    etFee.setText("0")
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

        progressBar.visibility = View.VISIBLE
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
                    progressBar.visibility = View.GONE
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    Toast.makeText(this@UpdateRecordActivity, "Fail to update the Record...", Toast.LENGTH_SHORT).show()
                    progressBar.visibility = View.GONE
                }
            })
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }
}