package com.example.user.messagerapp

import android.support.v7.app.AppCompatActivity
import android.os.Bundle

class AddNewBuildingActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_new_building)
        supportActionBar?.title = "Add New Building"
    }
}
