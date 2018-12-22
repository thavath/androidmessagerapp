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
import kotlinx.android.synthetic.main.activity_login.*
import android.widget.AdapterView



class LoginActivity : AppCompatActivity() {

    private var mAuth: FirebaseAuth? = null
    private var currentUser: FirebaseUser? = null
    private var accountType: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_login)
        supportActionBar?.title = "     Login to Room Rental App"

//        spinner set up
        var user_type = arrayOf("Account Type" ,"Room Owner", "Room Tenant")
        var spinner_user: Spinner? = this.spinner_user_type
//        spinner_user!!.setOnItemClickListener { parent, view, position, id ->
//            Toast.makeText(this,  "Selected : "+ user_type[position], Toast.LENGTH_SHORT).show()
//        }
        spinner_user!!.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                Toast.makeText(this@LoginActivity,  "Are you sure? You are : "+ user_type[position], Toast.LENGTH_SHORT).show()
                accountType = user_type[position]
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                accountType = null
            }
        }
        // Create an ArrayAdapter using a simple spinner layout and languages array
        val aa = ArrayAdapter(this, android.R.layout.simple_spinner_item, user_type)
        // Set layout to use when the list of choices appear
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        // Set Adapter to Spinner
        spinner_user!!.adapter = aa

        var btnSkip = findViewById<Button>(R.id.btn_skip_login)
        var btnLogin = findViewById<Button>(R.id.btn_login)
        var txtEmail = findViewById<EditText>(R.id.txt_email)
        var txtPassword = findViewById<EditText>(R.id.txt_password)
        var txtSignUp = findViewById<TextView>(R.id.txt_sign_up)
        var progressBar = findViewById<ProgressBar>(R.id.signin_progressbar)
        progressBar.visibility = View.INVISIBLE

        mAuth = FirebaseAuth.getInstance()
        btnSkip.setOnClickListener {
            var intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        btnLogin.setOnClickListener {
            var email = txtEmail.text.toString().trim()
            var password = txtPassword.text.toString().trim()
            progressBar.visibility = View.VISIBLE
            if(!email.isEmpty() && !password.isEmpty()){
                mAuth!!.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this){ task: Task<AuthResult> ->
                        if (task.isSuccessful){
                           if(accountType.equals(user_type[1])){
                               progressBar.visibility = View.INVISIBLE
                               Toast.makeText(this, "Logged in successfully.",Toast.LENGTH_SHORT).show()
                               var intent = Intent(this, WelcomeActivity::class.java)
//                        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
                               startActivity(intent)
                               finish()
                           }else if (accountType.equals(user_type[2])){
                               progressBar.visibility = View.INVISIBLE
                               Toast.makeText(this, "Logged in successfully.",Toast.LENGTH_SHORT).show()
                               var intent = Intent(this, HomeActivity::class.java)
//                        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
                               startActivity(intent)
                               finish()
                           }else {
                               progressBar.visibility = View.INVISIBLE
                               FirebaseAuth.getInstance().signOut()
                               Toast.makeText(this@LoginActivity, "Please Select Account Type", Toast.LENGTH_SHORT).show()
                           }
                        }else{
                            Toast.makeText(this, "Invalid Email or Password.",Toast.LENGTH_SHORT).show()
                            progressBar.visibility = View.INVISIBLE
                        }
                    }
            }else {
                Toast.makeText(this, "Please input information.", Toast.LENGTH_LONG).show()
                progressBar.visibility = View.INVISIBLE
            }
        }
        txtSignUp.setOnClickListener {
            val intent = Intent(this, SignupActivity::class.java)
            startActivity(intent)
            finish()
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
