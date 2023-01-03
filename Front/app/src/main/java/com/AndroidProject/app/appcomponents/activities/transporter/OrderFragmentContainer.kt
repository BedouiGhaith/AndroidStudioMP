package com.androidproject.app.appcomponents.activities.transporter

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.LinearLayout
import com.androidproject.app.R
import com.androidproject.app.appcomponents.models.User
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type

class OrderFragmentContainer : AppCompatActivity() {

    lateinit var user:User

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order_fragment_container)

        val pending = findViewById<LinearLayout>(R.id.transpo_pending)
        val finished = findViewById<LinearLayout>(R.id.transpo_finished)
        val profile = findViewById<LinearLayout>(R.id.imageTranspo)
        val enroute = findViewById<LinearLayout>(R.id.transpo_enroute)



        val sharedPreferencesL = getSharedPreferences("login", MODE_PRIVATE)

        val gson = Gson()

        val jsonUser = sharedPreferencesL.getString("user", null)

        val typeUser: Type = object : TypeToken<User?>() {}.type

        user = gson.fromJson<Any>(jsonUser, typeUser) as User

        supportFragmentManager.beginTransaction().add(R.id.fragmentContainerView3, PendingOrdersFragment()).commit()

        pending.setOnClickListener {
            supportFragmentManager.beginTransaction().replace(R.id.fragmentContainerView3, PendingOrdersFragment()).addToBackStack(null).commit()
        }
        finished.setOnClickListener {
            supportFragmentManager.beginTransaction().replace(R.id.fragmentContainerView3, AllOrdersFragment()).addToBackStack(null).commit()
        }
        profile.setOnClickListener {
            supportFragmentManager.beginTransaction().replace(R.id.fragmentContainerView3, TransporterProfileFragment()).addToBackStack(null).commit()
        }
        enroute.setOnClickListener {
            supportFragmentManager.beginTransaction().replace(R.id.fragmentContainerView3, OrdersTakenFragment()).addToBackStack(null).commit()
        }

    }
    fun getLogin():User {
        return user
    }
    fun setLogin(user: User){
        this.user = user
    }
}