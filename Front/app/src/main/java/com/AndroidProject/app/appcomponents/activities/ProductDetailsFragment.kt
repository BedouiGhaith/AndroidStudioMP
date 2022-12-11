package com.androidproject.app.appcomponents.activities

import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.androidproject.app.R
import com.androidproject.app.appcomponents.models.Product
import com.bumptech.glide.Glide
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.IOException
import java.lang.reflect.Type


class ProductDetailsFragment : Fragment() {

    lateinit var name: TextView
    lateinit var price: TextView
    lateinit var image: ImageView
    lateinit var cmdButton: Button
    lateinit var cmdFAD: FloatingActionButton



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {



        return inflater.inflate(R.layout.fragment_product_details, container, false)

        }
    override fun onViewCreated(itemView: View, savedInstanceState: Bundle?) {
        val product = requireArguments().getSerializable("EXTRA_PRODUCT") as Product

        name = view?.findViewById(R.id.name_cmd)!!
        price = view?.findViewById(R.id.price_cmd)!!
        image = view?.findViewById(R.id.image_cmd)!!
        cmdButton = view?.findViewById(R.id.btn_cmd)!!
        cmdFAD = view?.findViewById(R.id.add_to_cart_fab)!!

        name.text = product.name.toString()
        price.text = product.price.toString()
        Glide.with(this)
            .load("https://images.pexels.com/photos/1152359/pexels-photo-1152359.jpeg?auto=compress&cs=tinysrgb&w=600")
            .into(image)
        cmdButton.setOnClickListener {

        }
        cmdFAD.setOnClickListener {
            val sharedPreferences = requireActivity().getSharedPreferences("shared preferences", MODE_PRIVATE)

            // creating a variable for gson.
            val gson = Gson()

            // below line is to get to string present from our
            // shared prefs if not present setting it as null.
            var jsonProduct = sharedPreferences.getString("products", null)
            var jsonQuantity = sharedPreferences.getString("quantity", null)

            // below line is to get the type of our array list.
            val typeProduct: Type = object : TypeToken<ArrayList<Product?>?>() {}.type
            val typeQuantity: Type = object : TypeToken<ArrayList<Int?>?>() {}.type


            // in below line we are getting data from gson
            // and saving it to our array list
            val productList = gson.fromJson<Any>(jsonProduct, typeProduct) as ArrayList<Product>
            val quantityList = gson.fromJson<Any>(jsonQuantity, typeQuantity) as ArrayList<Int>

            if (!productList.contains(product)) {
                productList.add(product)
                quantityList.add(1)

                val editor = sharedPreferences.edit()

                jsonProduct = gson.toJson(productList)
                jsonQuantity = gson.toJson(quantityList)

                editor.putString("products", jsonProduct)
                editor.apply()

                editor.putString("quantity", jsonQuantity)
                editor.apply()

                val fm: FragmentManager = (context as AppCompatActivity).supportFragmentManager
                fm.beginTransaction().replace(R.id.fragmentContainerView, CartFragment()).addToBackStack(null).commit()
            }
        }
    }
}