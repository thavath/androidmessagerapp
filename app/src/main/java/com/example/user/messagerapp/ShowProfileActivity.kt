package com.example.user.messagerapp

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.SearchView
import android.view.Menu
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.ProgressBar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso

class ShowRoomActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_show_profile)

        var profileImage = findViewById<ImageView>(R.id.profile_image_detail)
        var progressB = findViewById<ProgressBar>(R.id.progressbarDetail)
        var txtUserName = findViewById<EditText>(R.id.txt_profile_username)
        var txtPhone = findViewById<EditText>(R.id.txt_profile_phone)
        var txtUserEmail = findViewById<EditText>(R.id.txt_profile_email)
        var txtUserPosition = findViewById<EditText>(R.id.txt_profile_position)
        var txtUserAddress = findViewById<EditText>(R.id.txt_profile_address)


        val uid = FirebaseAuth.getInstance().uid
        val ref = FirebaseDatabase.getInstance().getReference("/users")
        ref.addListenerForSingleValueEvent(object: ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onDataChange(p0: DataSnapshot) {
                p0.children.forEach {
                    var user = it.getValue(User::class.java) as User
                    if (uid == user.uid){
                        Picasso.get().load(user.imageUri).into(profileImage)
                        txtUserName.setText(user.username)
                        txtPhone.setText(user.phone)
                        txtUserEmail.setText(user.email)
                        txtUserPosition.setText(user.position)
                        txtUserAddress.setText(user.address)
                        progressB.visibility = View.INVISIBLE
                        supportActionBar?.title = """${user.username.capitalize()}'s Profile"""
                    }
                }
            }
        })
    }

//    override fun onCreateOptionsMenu(menu: Menu): Boolean {
//        menuInflater.inflate(R.menu.search_room, menu)
//        val searchRoom = menu.findItem(R.id.search_room)
//
//        if (searchRoom != null){
//            val searchView = searchRoom.actionView as SearchView
//            searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
//                override fun onQueryTextSubmit(query: String?): Boolean {
//                    return true
//                }
//
//                override fun onQueryTextChange(newText: String?): Boolean {
//                    return true
//                }
//            })
//        }
//        return super.onCreateOptionsMenu(menu)
//    }
}
