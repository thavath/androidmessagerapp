package com.example.user.messagerapp

import android.support.v7.app.AppCompatActivity
import android.os.Bundle

class ChatLogActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_room_detail)

        supportActionBar?.title = "Room Detail"
    }
}
