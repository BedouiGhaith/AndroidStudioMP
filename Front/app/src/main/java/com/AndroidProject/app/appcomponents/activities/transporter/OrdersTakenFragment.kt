package com.androidproject.app.appcomponents.activities.transporter

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.androidproject.app.R
import com.androidproject.app.appcomponents.adapters.OrderAdapter
import com.androidproject.app.appcomponents.connection.ApiInterface
import com.androidproject.app.appcomponents.models.Order
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class OrdersTakenFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_orders_taken, container, false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val user = (this.activity as OrderFragmentContainer?)?.getLogin()

        val rv = view.findViewById<RecyclerView>(R.id.taken_orders_rv)
        rv.layoutManager = LinearLayoutManager(requireActivity())

        val apiInterface  = ApiInterface.create()
        var orders: List<Order>

        apiInterface.commandesOnRoute(user?.id!!).enqueue(object :
            Callback<List<Order>> {
            override fun onResponse(call: Call<List<Order>>, response: Response<List<Order>>) {

                println("retrofit "+response.raw())

                if (response.body() != null){

                    orders = response.body()!!
                    Toast.makeText(requireContext(), "My Orders.", Toast.LENGTH_SHORT).show()

                    val adapter = OrderAdapter(orders.asReversed(),requireActivity(),user)
                    rv.adapter = adapter

                }else{
                    Toast.makeText(requireContext(), "No Orders Yet!", Toast.LENGTH_SHORT).show()
                }
            }
            override fun onFailure(call: Call<List<Order>>, t: Throwable) {

                println(t.printStackTrace())
                Toast.makeText(requireContext(), "Connexion error!", Toast.LENGTH_SHORT).show()

            }

        })
    }
}