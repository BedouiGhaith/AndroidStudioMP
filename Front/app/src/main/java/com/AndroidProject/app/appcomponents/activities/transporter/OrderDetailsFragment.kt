package com.androidproject.app.appcomponents.activities.transporter

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.androidproject.app.R
import com.androidproject.app.appcomponents.adapters.OrderAdapter
import com.androidproject.app.appcomponents.connection.ApiInterface
import com.androidproject.app.appcomponents.models.Order
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class OrderDetailsFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_order_details, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        val orderId = view.findViewById<TextView>(R.id.pending_order_id)
        val accept = view.findViewById<Button>(R.id.accept_pending_order)


        val user = (this.activity as OrderFragmentContainer?)?.getLogin()

        val order: Order = requireArguments().getSerializable("EXTRA_ORDER") as Order

        orderId.text = order.id

        order.responder = user
        order.status="On Route"
        val apiInterface  = ApiInterface.create()
        var result: Order

        accept.setOnClickListener {
            apiInterface.commandeAccept(order).enqueue(object :
                Callback<Order> {
                override fun onResponse(call: Call<Order>, response: Response<Order>) {

                    println("retrofit "+response.raw())

                    if (response.body() != null){

                        result = response.body()!!
                        Toast.makeText(requireContext(), "Engaged", Toast.LENGTH_SHORT).show()

                    }else{

                        Toast.makeText(requireContext(), "Try Again", Toast.LENGTH_SHORT).show()

                    }
                }

                override fun onFailure(call: Call<Order>, t: Throwable) {

                    println(t.printStackTrace())
                    Toast.makeText(requireContext(), "Connexion error!", Toast.LENGTH_SHORT).show()

                }

            })
        }
    }
}