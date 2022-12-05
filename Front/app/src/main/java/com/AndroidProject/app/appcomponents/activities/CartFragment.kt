package com.androidproject.app.appcomponents.activities

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.androidproject.app.R
import com.androidproject.app.appcomponents.adapters.CartAdapter
import com.androidproject.app.appcomponents.models.Product
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type


class CartFragment : Fragment() {

    lateinit var recyclerview: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_cart, container, false)
    }
    override fun onViewCreated(itemView: View, savedInstanceState: Bundle?) {

        recyclerview = view?.findViewById(R.id.cart_recycler)!!


        recyclerview.layoutManager = LinearLayoutManager(requireActivity())

        val sharedPreferences = requireActivity().getSharedPreferences("shared preferences",
            Context.MODE_PRIVATE
        )

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
        val quantityList = gson.fromJson<Int>(jsonQuantity, typeQuantity) as ArrayList<Int>

        val adapter = CartAdapter(productList, quantityList,requireContext(),childFragmentManager)

        recyclerview.adapter = adapter


    }
}