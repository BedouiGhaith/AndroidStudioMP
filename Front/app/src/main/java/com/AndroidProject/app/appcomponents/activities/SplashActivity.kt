package com.androidproject.app.appcomponents.activities

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.androidproject.app.R
import com.androidproject.app.appcomponents.models.User
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type

@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_one)
        Handler().postDelayed({

            val sharedPreferences= this.getSharedPreferences("login",
                Context.MODE_PRIVATE)

            if (sharedPreferences.contains("user")){
                val start = Intent(this, FragmentContainerActivity::class.java)
                startActivity(start)
                finish()
            }else{
                val start = Intent(this, LoginActivity::class.java)
                startActivity(start)
                finish()
            }
        }, 3000)
    }
}