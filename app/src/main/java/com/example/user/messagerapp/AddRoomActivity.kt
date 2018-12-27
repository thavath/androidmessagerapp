package com.example.user.messagerapp

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import com.example.user.messagerapp.model.Room
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.activity_add_room.*
import kotlinx.android.synthetic.main.activity_show_profile.*
import java.util.*

class AddRoomActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_room)
        supportActionBar?.title = "Add New Room"

        var progress = findViewById<ProgressBar>(R.id.progress_upload_room)
        var btnSeclectedPhoto = findViewById<ImageView>(R.id.room_profile_image)
        var btnAddRoom = findViewById<Button>(R.id.btn_add_room)

        progress.visibility = View.INVISIBLE

        btnSeclectedPhoto.setOnClickListener {
            // Pick photo from phone
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent, 2)
        }

        btnAddRoom.setOnClickListener {
            uploadImageToFirebaseStorage()
        }
    }
    var selectedImageUri : Uri? = null

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 2 && resultCode == Activity.RESULT_OK && data != null){
            // get data from pick photo
            selectedImageUri = data.data

            room_profile_image.setImageURI(selectedImageUri)
            Toast.makeText(this, "Image selected", Toast.LENGTH_SHORT).show()
        }
    }
    private fun uploadImageToFirebaseStorage() {
        progress_upload_room.visibility = View.VISIBLE
        if (selectedImageUri == null) return
        val filename = UUID.randomUUID().toString()+ "room"
        val ref = FirebaseStorage.getInstance().getReference("/rooms/$filename")
        ref.putFile(selectedImageUri!!)
            .addOnSuccessListener {
                ref.downloadUrl
                    .addOnSuccessListener {
                        Log.d("Image room", "Uri : "+it)
                        saveUserToFirebase(it)
                    }
            }
            .addOnFailureListener {
                Toast.makeText(this, "Error accured", Toast.LENGTH_SHORT).show()
            }
    }

    private fun saveUserToFirebase(selectedImageUil: Uri) {
        val userID = FirebaseAuth.getInstance().uid ?: ""
        val roomID = UUID.randomUUID().toString() + "room"
        val ref = FirebaseDatabase.getInstance().getReference("rooms/$roomID")
//        val calendar = Calendar.getInstance()
        // get current data.
//        val currentDate = DateFormat.getDateInstance(DateFormat.FULL).format(calendar.time) as String
        var room = Room(roomID, add_room_building.text.toString(),userID , add_room_price.text.toString().toDouble(),selectedImageUil.toString(),
            add_room_size.text.toString(), add_room_description.text.toString(), false)
        ref.setValue(room)
            .addOnSuccessListener {
                var intent = Intent(this, MyRoomListActivity::class.java)
//              intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
                progress_upload_room.visibility = View.INVISIBLE
                startActivity(intent)
                Toast.makeText(this, "Successfully created room.", Toast.LENGTH_SHORT).show()
                finish()
            }
            .addOnFailureListener {
                Toast.makeText(this, "Can not create new room. Something went wrong" , Toast.LENGTH_SHORT).show()
                    profile_image_detail.visibility = View.INVISIBLE
            }

    }
}
