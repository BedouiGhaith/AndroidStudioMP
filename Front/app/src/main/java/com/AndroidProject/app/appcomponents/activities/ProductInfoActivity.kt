package com.androidproject.app.appcomponents.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.androidproject.app.R
import com.androidproject.app.appcomponents.models.Product

class ProductInfoActivity : AppCompatActivity() {

    private lateinit var name: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_info)

        val product = intent.extras?.get("EXTRA_PRODUCT") as Product

        name = findViewById(R.id.product_info_name)

        name.text = product.name
    }
}