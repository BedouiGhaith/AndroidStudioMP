package com.androidproject.app.appcomponents.activities

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Adapter
import android.widget.SearchView
import android.widget.SearchView.OnQueryTextListener
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
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
    private lateinit var search: SearchView


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        return inflater.inflate(R.layout.fragment_products, container, false)
    }
    override fun onViewCreated(itemView: View, savedInstanceState: Bundle?) {

        recyclerview = itemView.findViewById(R.id.p_recycler)
        search = itemView.findViewById(R.id.search_view)

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
                if (products != null){
                    val data = ArrayList<Product>()

                    for (p in products) {
                        data.add(Product(p.id, p.name, p.image, p.price, p.pharmacy))
                    }

                    val adapter = ProductAdapter(data,requireActivity(), user)

                    recyclerview.adapter = adapter

                    search.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
                        override fun onQueryTextSubmit(p0: String?): Boolean {
                            return false
                        }

                        override fun onQueryTextChange(msg: String): Boolean {
                            val filteredlist: ArrayList<Product> = ArrayList()
                            for (item in products) {
                                if (item.name!!.lowercase().contains(search.query.toString().lowercase())) {
                                    filteredlist.add(item)
                                }
                            }
                            if (filteredlist.isEmpty()) {
                                Toast.makeText(requireContext(), "No Data Found..", Toast.LENGTH_SHORT).show()
                            } else {
                                adapter.filterList(filteredlist)
                            }
                            return false
                        }
                    })
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
    private fun filter(text: String, product: ArrayList<Product>,rv: ProductAdapter) {
        // creating a new array list to filter our data.
        val filteredlist: ArrayList<Product> = ArrayList()

        // running a for loop to compare elements.
        for (item in product) {
            // checking if the entered string matched with any item of our recycler view.
            if (item.name!!.lowercase().contains(text.lowercase())) {
                // if the item is matched we are
                // adding it to our filtered list.
                filteredlist.add(item)
            }
        }
        if (filteredlist.isEmpty()) {
            // if no item is added in filtered list we are
            // displaying a toast message as no data found.
            Toast.makeText(requireContext(), "No Data Found..", Toast.LENGTH_SHORT).show()
        } else {
            // at last we are passing that filtered
            // list to our adapter class.
            rv.filterList(filteredlist)
        }
    }
}