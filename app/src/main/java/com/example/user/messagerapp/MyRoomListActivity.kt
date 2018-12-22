package com.example.user.messagerapp

import android.support.v7.app.AppCompatActivity
import android.os.Bundle

class MyRoomListActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_room_list)
        supportActionBar?.title = "Room List"
    }
}
