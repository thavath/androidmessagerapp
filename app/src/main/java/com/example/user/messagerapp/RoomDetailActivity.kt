package com.example.user.messagerapp

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.example.user.messagerapp.model.Room
import com.squareup.picasso.Picasso
import org.w3c.dom.Text

class ChatLogActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_room_detail)

        supportActionBar?.title = "Room Detail"

        var bookingRoom = findViewById<Button>(R.id.btn_booking_room)
        var roomImage = findViewById<ImageView>(R.id.imageView_room_image)
        var roomPrice = findViewById<TextView>(R.id.txt_show_room_price)
        var roomSize = findViewById<TextView>(R.id.txt_show_room_size)
        var roomDescriptions = findViewById<TextView>(R.id.txt_show_room_description)
        var roomBuildingName = findViewById<TextView>(R.id.txt_show_room_building_name)
        var roomStatus = findViewById<TextView>(R.id.txt_show_room_status)

        var data = intent.getSerializableExtra("room") as Room


        bookingRoom.setOnClickListener {
            var intent = Intent(this, BookingActivity::class.java)
            intent.putExtra("room", data)
            startActivity(intent)
            finish()
        }
        if(data != null){
            Picasso.get().load(data.roomImageUri).into(roomImage)
            roomPrice.text = "Room Price ($): " + data.price
            roomSize.text = "Size : " + data.roomSize
            roomDescriptions.text = "Description : " + data.roomDescription
            roomBuildingName.text = "Building Name : " + data.buildingName

            if(!data.roomStatus){
                roomStatus.text = "Room Status : This room is available"
            }else{
                roomStatus.text = "Room Status : This room is not available"
            }
        }

    }
}
