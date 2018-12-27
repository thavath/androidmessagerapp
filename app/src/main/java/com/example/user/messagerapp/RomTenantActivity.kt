package com.example.user.messagerapp

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import com.example.user.messagerapp.adapter.RoomAdapter
import com.example.user.messagerapp.model.Room
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_room_tenant.*

class HomeActivity : AppCompatActivity() {


    private var roomList: ArrayList<Room>? = null
    private var layoutRoomManager: RecyclerView.LayoutManager? = null
    private var roomAdapter: RoomAdapter? = null

    val ref = FirebaseDatabase.getInstance().getReference("/users")
    val uid = FirebaseAuth.getInstance().uid
    var username: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_room_tenant)
//        var textViewEmail = findViewById<TextView>(R.id.text_view_email)
        // get Data From Intent
//        var i = intent
//        var data = i.getSerializableExtra("user") as User
//        textViewEmail.text = data.username
        verifyUserIsLogin()


        roomList = ArrayList()
        val refRoom = FirebaseDatabase.getInstance().getReference("/rooms")
        refRoom.addListenerForSingleValueEvent(object: ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onDataChange(p0: DataSnapshot) {
                p0.children.forEach {
                    var room = it.getValue(Room::class.java) as Room
                    if (room != null){
                        if(!room.roomStatus){
                            roomList!!.add(room)
                        }
                    }
                }
                layoutRoomManager = LinearLayoutManager(this@HomeActivity)
                roomAdapter = RoomAdapter(roomList!!, this@HomeActivity)
                room_rental_list.layoutManager = layoutRoomManager
                room_rental_list.adapter = roomAdapter
                roomAdapter!!.notifyDataSetChanged()
            }
        })

    }
    private fun verifyUserIsLogin(){
        if (uid == null){
            val intent = Intent(this, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when(item?.itemId){
            R.id.menu_new_message -> {
                val intent = Intent(this, NewMessageActivity::class.java)
                startActivity(intent)
                finish()
            }
            R.id.dashboard_owner -> {
                ref.addListenerForSingleValueEvent(object: ValueEventListener {
                    override fun onCancelled(p0: DatabaseError) {
                        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                    }
                    override fun onDataChange(p0: DataSnapshot) {
                        p0.children.forEach {
                            var user = it.getValue(User::class.java) as User
                            if (user.uid == uid){
                                if (user.position.equals(("Room Tenant").toString().trim())){
                                    username = user.username
                                    supportActionBar?.title = "${username?.capitalize()}'s Dashboard"
                                    Toast.makeText(this@HomeActivity , "Your are room tenant not room owner.", Toast.LENGTH_SHORT).show()
                                }
                                if (user.position.equals(("Room Owner").toString().trim())){
                                    var intent = Intent(this@HomeActivity, WelcomeActivity::class.java)
                                    startActivity(intent)
                                    finish()
                                }
                            }
                        }
                    }
                })
            }
            R.id.menu_sign_up -> {
                val intent = Intent(this, LoginActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
                FirebaseAuth.getInstance().signOut()
                finish()
            }
            R.id.menu_show_booked -> {
                val intent = Intent(this, ShowBookingActivity::class.java)
                startActivity(intent)
            }
        }
        return super.onOptionsItemSelected(item)
    }
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.nav_menu, menu)
        return super.onCreateOptionsMenu(menu)


    }
}
