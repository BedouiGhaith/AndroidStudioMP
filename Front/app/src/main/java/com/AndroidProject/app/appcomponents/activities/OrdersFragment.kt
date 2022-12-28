package com.androidproject.app.appcomponents.activities

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


class OrdersFragment : Fragment() {

    lateinit var  rv: RecyclerView


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_orders, container, false)
    }
    override fun onViewCreated(itemView: View, savedInstanceState: Bundle?) {

        val user = (this.activity as FragmentContainerActivity).getLogin()


        rv = view?.findViewById(R.id.f_orders_rv)!!
        rv.layoutManager = LinearLayoutManager(requireActivity())


        val apiInterface  = ApiInterface.create()
        var orders: List<Order>

        apiInterface.commandesUser(user.id!!).enqueue(object :
            Callback<List<Order>> {
            override fun onResponse(call: Call<List<Order>>, response: Response<List<Order>>) {

                println("retrofit "+response.raw())

                if (response.body() != null){

                    orders = response.body()!!
                    Toast.makeText(requireContext(), "My Orders.", Toast.LENGTH_SHORT).show()

                    val adapter = OrderAdapter(orders.asReversed(),requireActivity(),user)
                    rv.adapter = adapter

                }else{

                    Toast.makeText(requireContext(), "Your first order is yet to be made!", Toast.LENGTH_SHORT).show()

                }
            }

            override fun onFailure(call: Call<List<Order>>, t: Throwable) {

                println(t.printStackTrace())
                Toast.makeText(requireContext(), "Connexion error!", Toast.LENGTH_SHORT).show()

            }

        })
    }
}