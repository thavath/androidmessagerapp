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
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_room_avaiable.*
import kotlinx.android.synthetic.main.activity_signup.*
import java.io.Serializable
import java.util.*

class SignupActivity : AppCompatActivity() {

    private var accountsType: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)


        //        spinner set up
        var user_type = arrayOf("User Type" ,"Room Owner", "Room Tenant")
        var spinner_users : Spinner? = this.spinner_user_type_register
//        spinner_user!!.setOnItemClickListener { parent, view, position, id ->
//            Toast.makeText(this,  "Selected : "+ user_type[position], Toast.LENGTH_SHORT).show()
//        }
        spinner_users!!.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                accountsType = user_type[position]
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                accountsType = null
            }
        }
        // Create an ArrayAdapter using a simple spinner layout and languages array
        val aa = ArrayAdapter(this, android.R.layout.simple_spinner_item, user_type)
        // Set layout to use when the list of choices appear
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        // Set Adapter to Spinner
        spinner_users.adapter = aa

        var txtBackLogin = findViewById<TextView>(R.id.txt_login)
        var txtUserName = findViewById<EditText>(R.id.txt_r_username)
        var txtEmail = findViewById<EditText>(R.id.txt_r_email)
        var txtPhone = findViewById<EditText>(R.id.txt_r_phone)
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

           if(!email.isEmpty() && !password.isEmpty() && !txtPhone.text.toString().isEmpty() && !txtUserName.text.toString().isEmpty() && !accountsType.equals(user_type[0])){
               mAuth!!.createUserWithEmailAndPassword(email,password)
                   .addOnCompleteListener(this) { task :Task<AuthResult> ->
                       if (task.isSuccessful){
                           // uploading image function
                           uploadImageToFirebaseStorage()
                           // start new activity when user registered
                       }else{
                           Toast.makeText(this, "Authentication Failed", Toast.LENGTH_SHORT).show()
                           progressBar.visibility = View.INVISIBLE
                       }
                   }
           }else {
               Toast.makeText(this, "Please Enter invalid data.", Toast.LENGTH_SHORT).show()
               progressBar.visibility = View.INVISIBLE
           }
            if (accountsType.equals(user_type[0])){
                Toast.makeText(this, "Please Select User Type", Toast.LENGTH_SHORT).show()
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

        var user = User(uid, txt_r_username.text.toString(),txt_r_phone.text.toString(),"Phnom Penh", txt_r_email.text.toString() , profileImageUri.toString(), accountsType.toString().trim())
        ref.setValue(user)
            .addOnSuccessListener {
                // start home activity
                var intent = Intent(this, LoginActivity::class.java)
//              intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
                progressBar.visibility = View.INVISIBLE
                startActivity(intent)
                FirebaseAuth.getInstance().signOut()
                Toast.makeText(this, "Successfully created user.", Toast.LENGTH_SHORT).show()
                finish()
            }
            .addOnFailureListener {
                Toast.makeText(this, "Can not create user. Something went wrong" , Toast.LENGTH_SHORT).show()
                progressBar.visibility = View.INVISIBLE
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
class User(var uid: String, var username: String, var phone: String, var address: String, var email: String, var imageUri: String, var position: String) : Serializable{
    constructor() :this ("", "","","", "","","")
}
