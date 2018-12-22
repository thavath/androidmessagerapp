package com.example.user.messagerapp

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle

class SplashScreenActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        var background = object : Thread() {
            override fun run() {
                super.run()
                try {
                    Thread.sleep((1000).toLong())
                    var i = Intent(baseContext, WelcomeActivity::class.java)
                    startActivity(i)
                    finish()
                }catch (e: Exception){
                    e.printStackTrace()
                }
            }
        }
        background.start()
    }
}
