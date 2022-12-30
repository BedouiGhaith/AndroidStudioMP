package com.androidproject.app.appcomponents.activities.admin

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.androidproject.app.R
import com.androidproject.app.appcomponents.activities.transporter.OrderFragmentContainer
import com.androidproject.app.appcomponents.adapters.OrderAdapter
import com.androidproject.app.appcomponents.adapters.UserAdapter
import com.androidproject.app.appcomponents.connection.ApiInterface
import com.androidproject.app.appcomponents.models.Order
import com.androidproject.app.appcomponents.models.User
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AdminUserFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_admin_user, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        val rv = view.findViewById<RecyclerView>(R.id.admin_user_rv)

        val user = (this.activity as AdminFragmentContainer).getLogin()

        println(user)

        rv.layoutManager = LinearLayoutManager(requireActivity())

        val apiInterface  = ApiInterface.create()
        var users: List<User>

        apiInterface.users(user).enqueue(object :
            Callback<List<User>> {
            override fun onResponse(call: Call<List<User>>, response: Response<List<User>>) {

                println("retrofit "+response.raw())

                if (response.body() != null){

                    users = response.body()!!
                    Toast.makeText(requireContext(), "All Users.", Toast.LENGTH_SHORT).show()

                    val adapter = UserAdapter(users,requireActivity(),user)
                    rv.adapter = adapter

                }else{

                    Toast.makeText(requireContext(), "Error $response", Toast.LENGTH_SHORT).show()

                }
            }

            override fun onFailure(call: Call<List<User>>, t: Throwable) {

                println(t.printStackTrace())
                Toast.makeText(requireContext(), "Connexion error!", Toast.LENGTH_SHORT).show()

            }

        })
    }
}