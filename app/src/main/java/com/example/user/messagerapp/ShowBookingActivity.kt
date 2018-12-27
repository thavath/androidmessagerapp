package com.example.user.messagerapp

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import com.example.user.messagerapp.adapter.RoomAdapter
import com.example.user.messagerapp.model.Room
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_room_avaiable.*

class ShowBookingActivity : AppCompatActivity() {

    private var bookedList: ArrayList<Room>? = null
    private var layoutBookedManager: RecyclerView.LayoutManager? = null
    private var bookedAdapter: RoomAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_show_booking)
         supportActionBar?.title = "Room Booked"

        var progressBar = findViewById<ProgressBar>(R.id.progressShowBooked)
        progressBar.visibility = View.VISIBLE
        bookedList = ArrayList()

        var uid = FirebaseAuth.getInstance().uid
        val ref = FirebaseDatabase.getInstance().getReference("/rooms")
        ref.addListenerForSingleValueEvent(object: ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onDataChange(p0: DataSnapshot) {
                p0.children.forEach {
                    var room = it.getValue(Room::class.java) as Room
                    if (room != null){
                        if (room.roomStatus){
                            bookedList!!.add(room)
                        }else {
                            Toast.makeText(this@ShowBookingActivity, "You don't have any room booked", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
                layoutBookedManager = LinearLayoutManager(this@ShowBookingActivity)
                bookedAdapter = RoomAdapter(bookedList!!, this@ShowBookingActivity)
                preview_new_message.layoutManager = layoutBookedManager
                preview_new_message.adapter = bookedAdapter
                progressBar.visibility = View.INVISIBLE
                bookedAdapter!!.notifyDataSetChanged()
            }
        })
    }
}
