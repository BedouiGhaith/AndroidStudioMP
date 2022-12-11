package com.androidproject.app.appcomponents.activities

import android.annotation.SuppressLint
import android.media.Image
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import androidx.core.os.bundleOf
import androidx.fragment.app.add
import androidx.fragment.app.commit
import com.androidproject.app.R
import com.androidproject.app.appcomponents.models.Product
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type

class FragmentContainerActivity : AppCompatActivity() {

    var productList: ArrayList<Product> = ArrayList()
    var quantityList: ArrayList<Int> = ArrayList()

    lateinit var home: ImageView
    lateinit var notifs: ImageView
    lateinit var cart: ImageView
    lateinit var profile: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fragment_container)

        home= findViewById(R.id.imageHome)
        notifs = findViewById(R.id.imageNotification)
        cart = findViewById(R.id.imageBag)
        profile = findViewById(R.id.imageUser)

        val sharedPreferences = getSharedPreferences("shared preferences", MODE_PRIVATE)

        val editor = sharedPreferences.edit()

        val gson = Gson()

        val jsonProduct: String = gson.toJson(productList)
        val jsonQuanutity: String = gson.toJson(quantityList)

        editor.putString("products", jsonProduct)
        editor.apply()

        editor.putString("quantity", jsonQuanutity)
        editor.apply()

        supportFragmentManager.beginTransaction().add(R.id.fragmentContainerView, ProductsFragment()).commit()

        home.setOnClickListener {
            supportFragmentManager.beginTransaction().replace(R.id.fragmentContainerView, ProductsFragment()).addToBackStack(null).commit()
        }
        notifs.setOnClickListener {  }
        cart.setOnClickListener {
            supportFragmentManager.beginTransaction().replace(R.id.fragmentContainerView, CartFragment()).addToBackStack(null).commit()
        }
        profile.setOnClickListener {  }

    }
}