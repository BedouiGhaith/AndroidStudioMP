package com.androidproject.app.appcomponents.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.androidproject.app.R
import com.androidproject.app.appcomponents.connection.ApiInterface
import com.androidproject.app.appcomponents.models.Order
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class MyOrderFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_order_details, container, false)
    }
    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        val orderId = view.findViewById<TextView>(R.id.pending_order_id)
        val orderUser = view.findViewById<TextView>(R.id.username_of_order)
        val orderPrice = view.findViewById<TextView>(R.id.price_of_order)
        val orderAddress = view.findViewById<TextView>(R.id.address_of_order)
        val orderPhone = view.findViewById<TextView>(R.id.phone_of_order)
        val orderResponderPhone = view.findViewById<TextView>(R.id.responder_phone_order)

        val accept = view.findViewById<Button>(R.id.accept_pending_order)
        val cancel = view.findViewById<Button>(R.id.cancel_pending_order)
        val finish = view.findViewById<Button>(R.id.finish_pending_order)


        val user = (this.activity as FragmentContainerActivity).getLogin()

        val order: Order = requireArguments().getSerializable("EXTRA_ORDER") as Order

        orderId.text = order.id
        orderUser.text = order.user?.username
        orderPrice.text= order.price.toString()
        orderAddress.text= order.user?.address
        orderPhone.text = order.user?.phone
        if (order.status != "Pending") {
            orderResponderPhone.text = order.responder?.phone.toString()
            orderResponderPhone.setOnClickListener {
                val callIntent = Intent(Intent.ACTION_DIAL)
                callIntent.data = Uri.parse("tel:"+order.responder?.phone.toString())
                startActivity(callIntent)
            }
        }else orderResponderPhone.text = "Waiting..."


        val apiInterface  = ApiInterface.create()
        var result: Order

        cancel.setOnClickListener {
            order.status="Canceled"
            order.responder = null
            requireActivity().window.setFlags(
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
            )
            apiInterface.commandeAccept(order).enqueue(object :
                Callback<Order> {
                override fun onResponse(call: Call<Order>, response: Response<Order>) {
                    requireActivity().window.clearFlags( WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)

                    println("retrofit "+response.raw())

                    if (response.body() != null){

                        result = response.body()!!
                        Toast.makeText(requireContext(), "Canceled! " + result.id!!, Toast.LENGTH_SHORT).show()
                        orderResponderPhone.text = "Waiting..."
                        orderResponderPhone.setOnClickListener(null)

                    }else{

                        Toast.makeText(requireContext(), "Try Again!", Toast.LENGTH_SHORT).show()

                    }
                }

                override fun onFailure(call: Call<Order>, t: Throwable) {
                    requireActivity().window.clearFlags( WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)

                    println(t.printStackTrace())
                    Toast.makeText(requireContext(), "Connexion error!", Toast.LENGTH_SHORT).show()

                }

            })

        }

        finish.visibility = View.GONE
        accept.visibility = View.GONE
    }
}