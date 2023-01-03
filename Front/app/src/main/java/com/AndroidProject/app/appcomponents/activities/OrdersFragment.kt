package com.androidproject.app.appcomponents.activities

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
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

        requireActivity().window.setFlags(
            WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
            WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
        )

        apiInterface.commandesUser(user.id!!).enqueue(object :
            Callback<List<Order>> {
            override fun onResponse(call: Call<List<Order>>, response: Response<List<Order>>) {
                requireActivity().window.clearFlags( WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)

                if (response.body() != null){
                    orders = response.body()!!
                    val adapter = OrderAdapter(orders.asReversed(),requireActivity(),user)
                    rv.adapter = adapter
                }else{
                    Toast.makeText(requireContext(), "Empty!", Toast.LENGTH_SHORT).show()
                }
            }
            override fun onFailure(call: Call<List<Order>>, t: Throwable) {
                requireActivity().window.clearFlags( WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)

                println(t.printStackTrace())
                Toast.makeText(requireContext(), "Connexion error!", Toast.LENGTH_SHORT).show()
            }
        })
    }
}