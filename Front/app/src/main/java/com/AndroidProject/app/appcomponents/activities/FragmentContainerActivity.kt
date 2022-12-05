package com.androidproject.app.appcomponents.activities

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
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

    @SuppressLint("CommitPrefEdits")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fragment_container)

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

    }
}