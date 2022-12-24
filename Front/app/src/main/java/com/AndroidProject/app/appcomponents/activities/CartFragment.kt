package com.androidproject.app.appcomponents.activities

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.androidproject.app.R
import com.androidproject.app.appcomponents.adapters.CartAdapter
import com.androidproject.app.appcomponents.connection.ApiInterface
import com.androidproject.app.appcomponents.models.Order
import com.androidproject.app.appcomponents.models.Product
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.reflect.Type
import java.text.DecimalFormat


class CartFragment : Fragment() {



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val sharedPreferences= requireActivity().getSharedPreferences("shared preferences",
            Context.MODE_PRIVATE)

        val gson = Gson()

        val jsonProduct = sharedPreferences.getString("products", null)
        val jsonQuantity = sharedPreferences.getString("quantity", null)

        val typeProduct: Type = object : TypeToken<ArrayList<Product?>?>() {}.type
        val typeQuantity: Type = object : TypeToken<ArrayList<Int?>?>() {}.type

        val productList = gson.fromJson<Any>(jsonProduct, typeProduct) as ArrayList<Product>
        val quantityList = gson.fromJson<Int>(jsonQuantity, typeQuantity) as ArrayList<Int>
        return if(productList.isNotEmpty())
            inflater.inflate(R.layout.fragment_cart, container, false)
        else inflater.inflate(R.layout.activity_cart_empty_state,container,false)
    }
    override fun onViewCreated(itemView: View, savedInstanceState: Bundle?) {

        val sharedPreferences= requireActivity().getSharedPreferences("shared preferences",
            Context.MODE_PRIVATE)

        val gson = Gson()

        val jsonProduct = sharedPreferences.getString("products", null)
        val jsonQuantity = sharedPreferences.getString("quantity", null)

        val typeProduct: Type = object : TypeToken<ArrayList<Product?>?>() {}.type
        val typeQuantity: Type = object : TypeToken<ArrayList<Int?>?>() {}.type

        val productList = gson.fromJson<Any>(jsonProduct, typeProduct) as ArrayList<Product>
        val quantityList = gson.fromJson<Int>(jsonQuantity, typeQuantity) as ArrayList<Int>

        if(productList.isNotEmpty()){

            val recyclerview: RecyclerView = view?.findViewById(R.id.cart_recycler)!!

            val orderTxt: TextView = view?.findViewById(R.id.order_txt)!!
            val totalTxt: TextView = view?.findViewById(R.id.total_txt)!!

            val order:Button = view?.findViewById(R.id.validate_cart)!!
            var clear:Button = view?.findViewById(R.id.clear_cart)!!


            recyclerview.layoutManager = LinearLayoutManager(requireActivity())


            val adapter = CartAdapter(productList, quantityList,requireContext(),childFragmentManager)

            recyclerview.adapter = adapter

            calculate(orderTxt, totalTxt, productList, quantityList)

            recyclerview.setOnClickListener() {
                calculate(orderTxt, totalTxt, productList, quantityList)
            }

            adapter.registerAdapterDataObserver(object : RecyclerView.AdapterDataObserver() {
                override fun onChanged() {
                    calculate(orderTxt, totalTxt, productList, quantityList)
                }
            })

            order.setOnClickListener {
                val myOrder = Order(id = null,
                    (this.activity as FragmentContainerActivity?)?.getLogin(), productList, quantityList,"Pending", responder = null)
                orderProducts(myOrder)
            }


        }else requireActivity().onBackPressed()

    }

    private fun orderProducts(myOrder: Order) {
        val apiInterface  = ApiInterface.create()
        apiInterface.commandesAdd(myOrder).enqueue(object :
            Callback<Order> {

            override fun onResponse(call: Call<Order>, response: Response<Order>) {
                println("retrofit "+response.raw())
                if (response.body() != null){

                    Toast.makeText(requireContext(), "Success!", Toast.LENGTH_SHORT).show()

                }else{

                    Toast.makeText(requireContext(), "Error!", Toast.LENGTH_SHORT).show()

                }
            }

            override fun onFailure(call: Call<Order>, t: Throwable) {

                println(t.printStackTrace())
                Toast.makeText(requireContext(), "Connexion error!", Toast.LENGTH_SHORT).show()

            }

        })

    }

    fun calculate(orderTxt: TextView, totalTxt: TextView, productList:ArrayList<Product>, quantityList: ArrayList<Int>) {
        var total = 0F
        val dec = DecimalFormat("#,###.00")
        var i = 0
        productList.forEach { p ->
            total += p.price?.toFloat()?.times(quantityList[i])!!
            i++
        }
        var totalF = dec.format(total)
        orderTxt.text=totalF.toString()
        total=+2F
        totalF = dec.format(total)
        totalTxt.text = totalF.toString()
    }
}