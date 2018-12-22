package com.example.user.messagerapp

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.squareup.picasso.Picasso
import org.w3c.dom.Text

class ChatLogActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_room_detail)

        supportActionBar?.title = "Room Detail"

        var roomImage = findViewById<ImageView>(R.id.imageView_room_image)
        var roomPrice = findViewById<TextView>(R.id.txt_show_room_price)

        var data = intent.getSerializableExtra("user") as User

        if(data != null){
            Picasso.get().load(data.imageUri).into(roomImage)
            roomPrice.text = "Room Price ($): " + data.username
        }
    }
}
