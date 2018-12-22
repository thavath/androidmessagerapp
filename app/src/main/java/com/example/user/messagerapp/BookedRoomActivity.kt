package com.example.user.messagerapp

import android.support.v7.app.AppCompatActivity
import android.os.Bundle

class BookedRoomActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_booked_room)
        supportActionBar?.title = "List of Room Booked"
    }
}
