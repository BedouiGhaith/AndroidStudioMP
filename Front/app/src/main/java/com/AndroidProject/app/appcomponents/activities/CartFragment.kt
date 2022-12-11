package com.androidproject.app.appcomponents.activities

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.androidproject.app.R
import com.androidproject.app.appcomponents.adapters.CartAdapter
import com.androidproject.app.appcomponents.models.Product
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type
import java.text.DecimalFormat


class CartFragment : Fragment() {

    lateinit var recyclerview: RecyclerView

    lateinit var orderTxt: TextView
    lateinit var totalTxt: TextView
    lateinit var clear:Button
    lateinit var order:Button

    val sharedPreferences = requireActivity().getSharedPreferences("shared preferences",
        Context.MODE_PRIVATE
    )

    val gson = Gson()

    var jsonProduct = sharedPreferences.getString("products", null)
    var jsonQuantity = sharedPreferences.getString("quantity", null)

    val typeProduct: Type = object : TypeToken<ArrayList<Product?>?>() {}.type
    val typeQuantity: Type = object : TypeToken<ArrayList<Int?>?>() {}.type

    val productList = gson.fromJson<Any>(jsonProduct, typeProduct) as ArrayList<Product>
    val quantityList = gson.fromJson<Int>(jsonQuantity, typeQuantity) as ArrayList<Int>


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return if(productList.isNotEmpty())
            inflater.inflate(R.layout.fragment_cart, container, false)
        else inflater.inflate(R.layout.activity_cart_empty_state,container,false)
    }
    override fun onViewCreated(itemView: View, savedInstanceState: Bundle?) {

        if(productList.isNotEmpty()){
        recyclerview = view?.findViewById(R.id.cart_recycler)!!


        recyclerview.layoutManager = LinearLayoutManager(requireActivity())


        val adapter = CartAdapter(productList, quantityList,requireContext(),childFragmentManager)

        recyclerview.adapter = adapter

        order.setOnClickListener {  }


    }
    }
    fun calculate(orderTxt: TextView, totalTxt: TextView) {
        var total = 0F
        val dec = DecimalFormat("# ###.00")
        productList.forEach { p ->
            total += p.price?.toFloat()!!
        }
        var totalF = dec.format(total)
        orderTxt.text=totalF.toString()
        total=+2F
        totalF = dec.format(total)
        totalTxt.text = totalF.toString()
    }
}