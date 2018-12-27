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
import android.widget.Toast
import com.example.user.messagerapp.adapter.BuildingAdapter
import com.example.user.messagerapp.adapter.RoomAdapter
import com.example.user.messagerapp.adapter.UserAdapter
import com.example.user.messagerapp.model.Building
import com.example.user.messagerapp.model.Room
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_my_room_list.*
import kotlinx.android.synthetic.main.activity_room_avaiable.*

class MyRoomListActivity : AppCompatActivity() {


    private var roomList: ArrayList<Room>? = null
    private var layoutRoomManager: RecyclerView.LayoutManager? = null
    private var roomAdapter: RoomAdapter? = null
    private var buildingList: ArrayList<Building>? = null
    private var layoutBuildingManager: RecyclerView.LayoutManager? = null
    private var buildingAdapter: BuildingAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_room_list)
        supportActionBar?.title = "Room List"

        var userIDs = FirebaseAuth.getInstance().uid
        var progressBar = findViewById<ProgressBar>(R.id.progressRoomList)
//        get data from firebase

        progressBar.visibility = View.VISIBLE

        roomList = ArrayList()
        buildingList = ArrayList()

        val refRoom = FirebaseDatabase.getInstance().getReference("/rooms")
        val refBuilding = FirebaseDatabase.getInstance().getReference("/building")

        refBuilding.addListenerForSingleValueEvent(object: ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onDataChange(p0: DataSnapshot) {
                p0.children.forEach {
                    var building = it.getValue(Building::class.java) as Building
                    if (building != null){
                        if(building.userId.equals(userIDs)){
                            buildingList!!.add(building)
                        }else {
                            Toast.makeText(this@MyRoomListActivity , "You don't have any building", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
                layoutBuildingManager = LinearLayoutManager(this@MyRoomListActivity, LinearLayoutManager.HORIZONTAL, false)
                buildingAdapter = BuildingAdapter(buildingList!!, this@MyRoomListActivity)
                preview_building_list.layoutManager = layoutBuildingManager
                preview_building_list.adapter = buildingAdapter
                progressBar.visibility = View.INVISIBLE
                buildingAdapter!!.notifyDataSetChanged()
            }
        })

        refRoom.addListenerForSingleValueEvent(object: ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onDataChange(p0: DataSnapshot) {
                p0.children.forEach {
                    var room = it.getValue(Room::class.java) as Room
                    if (room != null){
                        if (room.userId.equals(userIDs)){
                            roomList!!.add(room)
                        } else{
                            Toast.makeText(this@MyRoomListActivity, "You don't have any room", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
                layoutRoomManager = LinearLayoutManager(this@MyRoomListActivity)
                roomAdapter = RoomAdapter(roomList!!, this@MyRoomListActivity)
                preview_room_list.layoutManager = layoutRoomManager
                preview_room_list.adapter = roomAdapter
                progressBar.visibility = View.INVISIBLE
                roomAdapter!!.notifyDataSetChanged()
            }
        })

    }
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.add_building, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when(item?.itemId){
            R.id.add_building -> {
                var intent = Intent(this, AddNewBuildingActivity::class.java)
                startActivity(intent)
                finish()
            }
            R.id.add_new_room -> {
                var intent = Intent(this, AddRoomActivity::class.java)
                startActivity(intent)
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }
}
