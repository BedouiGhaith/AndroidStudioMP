package com.androidproject.app.appcomponents.activities

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.androidproject.app.R
import com.androidproject.app.appcomponents.activities.admin.AdminFragmentContainer
import com.androidproject.app.appcomponents.activities.transporter.OrderFragmentContainer
import com.androidproject.app.appcomponents.models.User
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type

@Suppress("DEPRECATION")
@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_one)
        val sharedPreferences= getSharedPreferences("login",
            Context.MODE_PRIVATE)

        Handler().postDelayed({

        if(sharedPreferences != null) {
            if (sharedPreferences.contains("user")) {

                val sharedPreferencesL = getSharedPreferences("login", MODE_PRIVATE)

                val jsonUser = sharedPreferencesL.getString("user", null)

                val gson = Gson()

                val typeUser: Type = object : TypeToken<User?>() {}.type

                val user = gson.fromJson<Any>(jsonUser, typeUser) as User

                when (user.role) {
                    "user" -> {
                        val start = Intent(this, FragmentContainerActivity::class.java)
                        startActivity(start)
                        finish()
                    }
                    "transporter" -> {
                        val start = Intent(this, OrderFragmentContainer::class.java)
                        startActivity(start)
                        finish()
                    }
                    "admin" -> {
                        val start = Intent(this, AdminFragmentContainer::class.java)
                        startActivity(start)
                        finish()
                    }
                    else -> {
                        val start = Intent(this, LoginActivity::class.java)
                        startActivity(start)
                        finish()
                    }
                }
            } else {
                val start = Intent(this, LoginActivity::class.java)
                startActivity(start)
                finish()
            }
            }else {
            val start = Intent(this, LoginActivity::class.java)
            startActivity(start)
            finish()}
        }, 3000)
    }
}