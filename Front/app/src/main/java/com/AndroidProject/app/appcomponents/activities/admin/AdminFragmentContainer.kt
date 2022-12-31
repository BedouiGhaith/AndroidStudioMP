package com.androidproject.app.appcomponents.activities.admin

import android.os.Bundle
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import com.androidproject.app.R
import com.androidproject.app.appcomponents.models.User
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type

class AdminFragmentContainer : AppCompatActivity() {

    lateinit var user: User

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_fragment_container)

        val orderbb = findViewById<LinearLayout>(R.id.admin_order_bb)
        val productsbb = findViewById<LinearLayout>(R.id.admin_products_bb)
        val usersbb = findViewById<LinearLayout>(R.id.admin_users_bb)
        val adminbb = findViewById<LinearLayout>(R.id.imageAdmin)

        val sharedPreferencesL = getSharedPreferences("login", MODE_PRIVATE)

        val gson = Gson()

        val jsonUser = sharedPreferencesL.getString("user", null)

        val typeUser: Type = object : TypeToken<User?>() {}.type

        user = gson.fromJson<Any>(jsonUser, typeUser) as User


        supportFragmentManager.beginTransaction().add(R.id.admin_fc, AdminProductsFragment()).commit()

        productsbb.setOnClickListener {
            supportFragmentManager.beginTransaction().replace(R.id.admin_fc, AdminProductsFragment()).addToBackStack(null).commit()
        }
        adminbb.setOnClickListener {
            supportFragmentManager.beginTransaction().replace(R.id.admin_fc, AdminProfileFragment()).addToBackStack(null).commit()
        }
        orderbb.setOnClickListener {
            supportFragmentManager.beginTransaction().replace(R.id.admin_fc, AdminOrdersFragment()).addToBackStack(null).commit()
        }
        usersbb.setOnClickListener {
            supportFragmentManager.beginTransaction().replace(R.id.admin_fc, AdminUserFragment()).addToBackStack(null).commit()
        }

    }

    fun getLogin():User {
        return user
    }
}