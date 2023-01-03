package com.androidproject.app.appcomponents.activities

import android.os.Bundle
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import com.androidproject.app.R
import com.androidproject.app.appcomponents.models.Product
import com.androidproject.app.appcomponents.models.User
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type

class FragmentContainerActivity : AppCompatActivity() {

    private var productList: ArrayList<Product> = ArrayList()
    private var quantityList: ArrayList<Int> = ArrayList()

    private lateinit var home: LinearLayout
    lateinit var orders: LinearLayout
    private lateinit var cart: LinearLayout
    private lateinit var profile: LinearLayout

    lateinit var user:User

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fragment_container)

        home= findViewById(R.id.imageHome)
        orders = findViewById(R.id.imageNotification)
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

        val sharedPreferencesL = getSharedPreferences("login", MODE_PRIVATE)

        val jsonUser = sharedPreferencesL.getString("user", null)

        val typeUser: Type = object : TypeToken<User?>() {}.type

        user = gson.fromJson<Any>(jsonUser, typeUser) as User

        supportFragmentManager.beginTransaction().add(R.id.fragmentContainerView, ProductsFragment()).commit()

        home.setOnClickListener {
            supportFragmentManager.beginTransaction().replace(R.id.fragmentContainerView, ProductsFragment()).addToBackStack(null).commit()
        }
        orders.setOnClickListener {
            supportFragmentManager.beginTransaction().replace(R.id.fragmentContainerView, OrdersFragment()).addToBackStack(null).commit()

        }
        cart.setOnClickListener {
            supportFragmentManager.beginTransaction().replace(R.id.fragmentContainerView, CartFragment()).addToBackStack(null).commit()
        }
        profile.setOnClickListener {
            supportFragmentManager.beginTransaction().replace(R.id.fragmentContainerView, ProfileFragment()).addToBackStack(null).commit()
        }
    }
    fun getLogin():User{
        return user
    }
    fun setLogin(user: User){
        this.user = user
    }
}