package com.example.user.messagerapp

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import com.example.user.messagerapp.model.Booked
import com.example.user.messagerapp.model.Room
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_add_new_building.*
import java.text.DateFormat
import java.util.*

class BookingActivity : AppCompatActivity() {

    var uid = FirebaseAuth.getInstance().uid

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_booking)
        supportActionBar?.title = "Room Booking"
        verifyUserIsLogin()
        var data = intent.getSerializableExtra("room") as Room
//        get datepicker

        var btnCheckout = findViewById<Button>(R.id.btn_check_out)
        var progress = findViewById<ProgressBar>(R.id.progressBookingSave)
        progress.visibility = View.INVISIBLE

        var txtStayDate = findViewById<EditText>(R.id.stayDate)
        var txtLeaveDate = findViewById<EditText>(R.id.leaveDate)
        var txtMonths = findViewById<EditText>(R.id.total_months_of_stay)
        var txtPeople= findViewById<EditText>(R.id.total_people_of_stay)
        var userID : String? = uid
        var roomID : String? = null
        val bookingID = UUID.randomUUID().toString() + "booked"

        if (data != null){
            roomID = data.uid
        }
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)
        val currentDate = DateFormat.getDateInstance(DateFormat.FULL).format(c.time) as String

        btnCheckout.setOnClickListener {

            progress.visibility = View.VISIBLE
            val ref = FirebaseDatabase.getInstance().getReference("booking/$bookingID")
            val refRoom = FirebaseDatabase.getInstance().getReference("rooms")
            // update room status to busy
            data.roomStatus = true
            refRoom.child(data.uid).setValue(data)

            var booked = Booked(bookingID, roomID.toString(), userID.toString(), txtStayDate.text.toString(), txtLeaveDate.text.toString(),
                currentDate, txtMonths.text.toString().toDouble(), txtPeople.text.toString().toInt())
            ref.setValue(booked)
                .addOnSuccessListener {
                    // start room list activity
                    progress.visibility = View.INVISIBLE
                    var intent = Intent(this, NewMessageActivity::class.java)
    //              intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
    //                    progressBar.visibility = View.INVISIBLE
                    startActivity(intent)
                    Toast.makeText(this, "Successfully created booking.", Toast.LENGTH_SHORT).show()
                    finish()
                }
                .addOnFailureListener {
                    Toast.makeText(this, "Can not create booking. Something went wrong" , Toast.LENGTH_SHORT).show()
                    progress.visibility = View.INVISIBLE
                }
        }

        txtStayDate.setOnClickListener {
            val dpd = DatePickerDialog(this, DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                // Display Selected date in textbox
                txtStayDate.setText(""+ dayOfMonth + "/" + monthOfYear + "/" + year)
            }, year, month, day)
            dpd.show()
        }

        txtLeaveDate.setOnClickListener {
            val dpd = DatePickerDialog(this, DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                // Display Selected date in textbox
                txtLeaveDate.setText(""+ dayOfMonth + "/" + monthOfYear + "/" + year)
            }, year, month, day)
            dpd.show()
        }
    }
    private fun verifyUserIsLogin(){
        if (uid == null){
            val intent = Intent(this, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
            finish()
        }
    }
}
