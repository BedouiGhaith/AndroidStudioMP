package com.androidproject.app.appcomponents.activities

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.androidproject.app.R

@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_one)
        Handler().postDelayed({
            val start = Intent(this, LoginActivity::class.java)
            startActivity(start)
            finish()
        }, 3000)
    }
}