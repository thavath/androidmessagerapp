package com.example.user.messagerapp
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ProgressBar
import com.example.user.messagerapp.adapter.RoomAdapter
//import android.widget.Toast
import com.example.user.messagerapp.adapter.UserAdapter
import com.example.user.messagerapp.model.Room
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_room_avaiable.*

class NewMessageActivity : AppCompatActivity() {

    private var roomList: ArrayList<Room>? = null
    private var layoutManager: RecyclerView.LayoutManager? = null
    private var adapter: RoomAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_room_avaiable)
//        var recyclerView = findViewById<>(R.id.preview_new_message)
        var progressBar = findViewById<ProgressBar>(R.id.progressBar)
        supportActionBar?.title = "Available Room"
        progressBar.visibility = View.VISIBLE
        roomList = ArrayList()

        val ref = FirebaseDatabase.getInstance().getReference("/rooms")
        ref.addListenerForSingleValueEvent(object: ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onDataChange(p0: DataSnapshot) {
                p0.children.forEach {
                    var room = it.getValue(Room::class.java) as Room
                    if (room != null){
                        if (!room.roomStatus){
                            roomList!!.add(room)
                        }
                    }
                }
                layoutManager = LinearLayoutManager(this@NewMessageActivity)
                adapter = RoomAdapter(roomList!!, this@NewMessageActivity)
                preview_new_message.layoutManager = layoutManager
                preview_new_message.adapter = adapter
                progressBar.visibility = View.INVISIBLE
                adapter!!.notifyDataSetChanged()
            }
        })
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {

        when(item?.itemId){
            R.id.action_settings -> {
                val uid = FirebaseAuth.getInstance().uid
                if (uid != null){
                    val intent = Intent(this, HomeActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
                    startActivity(intent)
                }else{
                    var intent = Intent(this@NewMessageActivity, LoginActivity::class.java)
                    startActivity(intent)
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.dashboard, menu)
        return super.onCreateOptionsMenu(menu)
    }
}
