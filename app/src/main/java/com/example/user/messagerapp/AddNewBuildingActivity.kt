package com.example.user.messagerapp

import android.app.Activity
import android.content.Intent
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.*
import com.example.user.messagerapp.model.Building
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.activity_add_new_building.*
import java.util.*
import java.text.DateFormat


class AddNewBuildingActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_new_building)
        supportActionBar?.title = "Add New Building"

        var showDate = findViewById<TextView>(R.id.show_select_building_image)
//        showDate.text = currentDate

        var btnSelectPhoto = findViewById<ImageView>(R.id.building_profile_image)
        var buildingName = findViewById<EditText>(R.id.add_building_name)
        var buildingLocation = findViewById<EditText>(R.id.add_building_address)
        var btnAdd = findViewById<Button>(R.id.btn_add_building)

        var progress = findViewById<ProgressBar>(R.id.buildingProgress)
        var btnCancel = findViewById<Button>(R.id.btn_cancel_building)

        progress.visibility = View.INVISIBLE


        btnSelectPhoto.setOnClickListener {
            // Pick photo from phone
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent, 1)
        }

        btnAdd.setOnClickListener {
            uploadImageToFirebaseStorage()
        }
    }

    private fun uploadImageToFirebaseStorage() {
        buildingProgress.visibility = View.VISIBLE
        if (selectedImageUri == null) return
        val filename = UUID.randomUUID().toString()+ "buildingImage"
        val ref = FirebaseStorage.getInstance().getReference("/building/$filename")
        ref.putFile(selectedImageUri!!)
                .addOnSuccessListener {
                    ref.downloadUrl
                            .addOnSuccessListener {
                                Log.d("Image Building", "Uri : "+it)
                                saveUserToFirebase(it)
                            }
                }
                .addOnFailureListener {
                    buildingProgress.visibility = View.INVISIBLE
                    Toast.makeText(this, "Error accured", Toast.LENGTH_SHORT).show()
                }
    }

    private fun saveUserToFirebase(selectedImageUil: Uri) {
        val userID = FirebaseAuth.getInstance().uid ?: ""
        val buildingID = UUID.randomUUID().toString() + "building"
        val ref = FirebaseDatabase.getInstance().getReference("building/$buildingID")
        val calendar = Calendar.getInstance()
        // get current data.
        val currentDate = DateFormat.getDateInstance(DateFormat.FULL).format(calendar.time) as String
        var building = Building(buildingID, userID, add_building_name.text.toString(), currentDate, add_building_address.text.toString(),false, selectedImageUil.toString())

        ref.setValue(building)
                .addOnSuccessListener {
                    // start room list activity
                    buildingProgress.visibility = View.INVISIBLE
                    var intent = Intent(this, MyRoomListActivity::class.java)
//              intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
//                    progressBar.visibility = View.INVISIBLE
                    startActivity(intent)
                    Toast.makeText(this, "Successfully created building.", Toast.LENGTH_SHORT).show()
                    finish()
                }
                .addOnFailureListener {
                    Toast.makeText(this, "Can not create building. Something went wrong" , Toast.LENGTH_SHORT).show()
//                    progressBar.visibility = View.INVISIBLE
                }

    }

    var selectedImageUri : Uri? = null

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1 && resultCode == Activity.RESULT_OK && data != null){
            // get data from pick photo
            selectedImageUri = data.data
//            var bitmap = MediaStore.Images.Media.getBitmap(contentResolver, selectedImageUri)
//            val bimapBitmapDrawable = BitmapDrawable(bitmap)
            building_profile_image.setImageURI(selectedImageUri)
            Toast.makeText(this, "Image selected", Toast.LENGTH_SHORT).show()
        }
    }
}
