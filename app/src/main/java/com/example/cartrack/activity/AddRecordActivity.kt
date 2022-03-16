package com.example.cartrack.activity

import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.annotation.NonNull
import androidx.annotation.Nullable
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import com.example.cartrack.MainActivity
import com.example.cartrack.R
import com.example.cartrack.model.Items
import com.google.firebase.database.*
import java.text.SimpleDateFormat
import java.util.*


class AddRecordActivity : AppCompatActivity(), AdapterView.OnItemSelectedListener {

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
    lateinit var btnSubmit: TextView
    lateinit var progressBar: ProgressBar
    private var unit: String = "TWO"
    private var result: String = "FAIL"
    private var ispaid: String = "PAID"
    val myCalendar = Calendar.getInstance()
    private var currentDate = ""

    var firebaseDatabase: FirebaseDatabase? = null
    var databaseReference: DatabaseReference? = null
    var customerInfo: Items? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_record)
        val toolbar: Toolbar = findViewById<View>(R.id.toolbar) as Toolbar
        toolbar.title = "Add new Customer Record"
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
        btnSubmit = findViewById(R.id.btnSubmit)
        progressBar = findViewById(R.id.progressBar)


        val date = OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
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

        val Redate = OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
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

        val sdf = SimpleDateFormat("dd MMM, yyyy 'at' HH:mm:ss")
        val currentDateandTime = sdf.format(Date())

        btnSubmit.setOnClickListener {
            if (isValidate()) {
                val b = Items()
                b.id = databaseReference!!.push().key.toString()
                b.name = etName.text.toString()
                b.mobile = etMobile.text.toString()
                b.vehical_no = etVehicalNo.text.toString()
                b.test_date = etTestDate.text.toString()
                b.unit = unit
                b.result = result
                b.re_test_date = etReTestDate.text.toString()
                b.paid_free = ispaid
                b.fee = etFee.text.toString()
//                itemViewModel!!.InsertItem(b)
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
//                Toast.makeText(this, "Data Added Successfully!!", Toast.LENGTH_SHORT).show()
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
                            this@AddRecordActivity,
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
        spinneResult.adapter = dataAdapter
        spinneResult.onItemSelectedListener = this
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
        spinneFees.adapter = dataAdapter
        spinneFees.onItemSelectedListener = this
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
                } else {
                    etFee.setText("")
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

        progressBar.visibility = View.VISIBLE

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
                progressBar.visibility = View.GONE

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
                progressBar.visibility = View.GONE
            }
        })
    }

}