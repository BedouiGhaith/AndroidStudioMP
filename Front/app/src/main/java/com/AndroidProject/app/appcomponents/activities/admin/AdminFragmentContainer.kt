package com.androidproject.app.appcomponents.activities.admin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.LinearLayout
import androidx.fragment.app.FragmentContainer
import androidx.fragment.app.FragmentContainerView
import com.androidproject.app.R
import com.androidproject.app.appcomponents.activities.ProductsFragment

class AdminFragmentContainer : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_fragment_container)

        val order_bb = findViewById<LinearLayout>(R.id.admin_order_bb)
        val products_bb = findViewById<LinearLayout>(R.id.admin_products_bb)
        val users_bb = findViewById<LinearLayout>(R.id.admin_users_bb)

        supportFragmentManager.beginTransaction().add(R.id.admin_fc, AdminOrdersFragment()).commit()

    }
}