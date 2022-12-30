package com.androidproject.app.appcomponents.activities

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.androidproject.app.R
import com.androidproject.app.appcomponents.adapters.ProductAdapter
import com.androidproject.app.appcomponents.connection.ApiInterface
import com.androidproject.app.appcomponents.models.Product
import com.androidproject.app.appcomponents.models.User
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.reflect.Type


class ProductsFragment : Fragment() {

    lateinit var recyclerview: RecyclerView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        return inflater.inflate(R.layout.fragment_products, container, false)
    }
    override fun onViewCreated(itemView: View, savedInstanceState: Bundle?) {

        recyclerview = view?.findViewById(R.id.p_recycler)!!

        val sharedPreferencesL = requireActivity().getSharedPreferences("login", AppCompatActivity.MODE_PRIVATE)

        val gson = Gson()

        val jsonUser = sharedPreferencesL.getString("user", null)

        val typeUser: Type = object : TypeToken<User?>() {}.type

        val user = gson.fromJson<Any>(jsonUser, typeUser) as User



        recyclerview.layoutManager = GridLayoutManager(requireContext(),2, GridLayoutManager.VERTICAL, false)

        val apiInterface = ApiInterface.create()

        apiInterface.products().enqueue(object :
            Callback<List<Product>> {

            override fun onResponse(call: Call<List<Product>>, response: Response<List<Product>>) {

                val products = response.body()

                println(""+response.raw())
                if (products != null){
                    val data = ArrayList<Product>()

                    for (p in products) {
                        data.add(Product(p.id, p.name, p.image, p.price, p.pharmacy))
                    }

                    val adapter = ProductAdapter(data,requireActivity(), user)

                    recyclerview.adapter = adapter
                }else{
                    Toast.makeText(context, "Error!", Toast.LENGTH_SHORT).show()
                }

            }

            override fun onFailure(call: Call<List<Product>>, t: Throwable) {
                println(t.printStackTrace())
                Toast.makeText(context, "Connexion error!", Toast.LENGTH_SHORT).show()

            }

        })


    }
    }