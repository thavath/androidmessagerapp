package com.example.user.messagerapp

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class LoginActivity : AppCompatActivity() {

    private var mAuth: FirebaseAuth? = null
    private var currentUser: FirebaseUser? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        var btnLogin = findViewById<Button>(R.id.btn_login)
        var txtEmail = findViewById<EditText>(R.id.txt_email)
        var txtPassword = findViewById<EditText>(R.id.txt_password)
        var txtSignUp = findViewById<TextView>(R.id.txt_sign_up)
        var progressBar = findViewById<ProgressBar>(R.id.signin_progressbar)
        progressBar.visibility = View.INVISIBLE

        mAuth = FirebaseAuth.getInstance()

        btnLogin.setOnClickListener {

            var email = txtEmail.text.toString().trim()
            var password = txtPassword.text.toString().trim()
            progressBar.visibility = View.VISIBLE
            mAuth!!.signInWithEmailAndPassword(email!!, password!!)
                .addOnCompleteListener(this){ task: Task<AuthResult> ->
                    if (task.isSuccessful){
                        progressBar.visibility = View.INVISIBLE
                        Toast.makeText(this, "Logged in successfully.",Toast.LENGTH_SHORT).show()
                        var intent = Intent(this, WelcomeActivity::class.java)
//                        intent.putExtra("email", email)
//                        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
                        startActivity(intent)
                        finish()
                    }else{
                        Toast.makeText(this, "Invalid Email and Password.",Toast.LENGTH_SHORT).show()
                    }
                }
        }
        txtSignUp.setOnClickListener {
            val intent = Intent(this, SignupActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onStart() {
        super.onStart()
        currentUser = mAuth!!.currentUser
//        if (currentUser != null){
//            Toast.makeText(this, "User is logged in.",Toast.LENGTH_LONG).show()
//        }else{
//            Toast.makeText(this, "User is logged out.",Toast.LENGTH_LONG).show()
//        }
    }
}
