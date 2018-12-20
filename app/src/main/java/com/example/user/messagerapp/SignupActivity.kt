package com.example.user.messagerapp

import android.app.Activity
import android.app.Notification
import android.content.Intent
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.*
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.activity_new_message.*
import kotlinx.android.synthetic.main.activity_signup.*
import java.io.Serializable
import java.util.*

class SignupActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        var txtBackLogin = findViewById<TextView>(R.id.txt_login)
//        var txtUserName = findViewById<EditText>(R.id.txt_r_username)
        var txtEmail = findViewById<EditText>(R.id.txt_r_email)
        var txtPassword = findViewById<EditText>(R.id.txt_r_password)
        var btnRegister = findViewById<Button>(R.id.btn_register)
        var imageSelector = findViewById<ImageView>(R.id.r_profile_image)
        var progressBar = findViewById<ProgressBar>(R.id.signup_progressbar)

        progressBar.visibility = View.INVISIBLE
        var mAuth = FirebaseAuth.getInstance()



        btnRegister.setOnClickListener {
            var email = txtEmail.text.toString().trim()
            var password = txtPassword.text.toString().trim()
            progressBar.visibility = View.VISIBLE
            mAuth!!.createUserWithEmailAndPassword(email!!,password!!)
                .addOnCompleteListener(this) { task :Task<AuthResult> ->
                    if (task.isSuccessful){
                         // uploading image function
                        uploadImageToFirebaseStorage()
                        // start new activity when user registered
                    }else{
                        Toast.makeText(this, "Authentication Failed", Toast.LENGTH_SHORT).show()
                    }
                }
        }

        txtBackLogin.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }

        imageSelector.setOnClickListener {

            // Pick photo from phone
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent, 0)
        }
    }

    private fun uploadImageToFirebaseStorage() {

        if (selectedPhotoUri == null) return
        val filename = UUID.randomUUID().toString()
        val ref = FirebaseStorage.getInstance().getReference("/images/$filename")
        ref.putFile(selectedPhotoUri!!)
            .addOnSuccessListener {
                ref.downloadUrl
                    .addOnSuccessListener {
                        Log.d("Image", "Uri : "+it)
                        saveUserToFirebase(it)
                    }
            }
            .addOnFailureListener {
                Toast.makeText(this, "Error accured", Toast.LENGTH_SHORT).show()
            }
    }

    private fun saveUserToFirebase(profileImageUri: Uri) {
        val uid = FirebaseAuth.getInstance().uid ?: ""
        val ref = FirebaseDatabase.getInstance().getReference("users/$uid")

        var user = User(uid, txt_r_username.text.toString(), txt_r_email.text.toString() , profileImageUri.toString())
        ref.setValue(user)
            .addOnSuccessListener {
                // start home activity
                progressBar.visibility = View.INVISIBLE
                var intent = Intent(this, HomeActivity::class.java)
                intent.putExtra("user", user)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
                Toast.makeText(this, "Successfully created user.", Toast.LENGTH_SHORT).show()
                finish()
            }
            .addOnFailureListener {

                Toast.makeText(this, "Can not create user. Something went wrong" , Toast.LENGTH_SHORT).show()
            }

    }

    var selectedPhotoUri : Uri? = null

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 0 && resultCode == Activity.RESULT_OK && data != null){
            // get data from pick photo
            selectedPhotoUri = data.data
            var bitmap = MediaStore.Images.Media.getBitmap(contentResolver, selectedPhotoUri)
//            val bimapBitmapDrawable = BitmapDrawable(bitmap)
            r_profile_image_roaded.setImageBitmap(bitmap)
            r_profile_image.alpha = 0f
            Toast.makeText(this, "Image selected", Toast.LENGTH_SHORT).show()
        }
    }
}
class User(var uid: String, var username: String, var email: String, var imageUri: String) : Serializable{
    constructor() :this ("", "", "","")
}
