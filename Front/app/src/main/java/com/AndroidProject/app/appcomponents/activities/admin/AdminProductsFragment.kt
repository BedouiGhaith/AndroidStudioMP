package com.androidproject.app.appcomponents.activities.admin

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.androidproject.app.R
import com.androidproject.app.appcomponents.adapters.ProductAdapter
import com.androidproject.app.appcomponents.connection.ApiInterface
import com.androidproject.app.appcomponents.models.Product
import com.androidproject.app.appcomponents.models.User
import com.google.android.material.floatingactionbutton.FloatingActionButton
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class AdminProductsFragment : Fragment() {

    lateinit var user: User

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_admin_products, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        user = (this.activity as AdminFragmentContainer?)?.getLogin()!!

        val rv = view.findViewById<RecyclerView>(R.id.admin_product_rv)
        val add = view.findViewById<FloatingActionButton>(R.id.admin_add_p_fab)


        add.setOnClickListener {
            (context as AppCompatActivity).supportFragmentManager.beginTransaction().replace(R.id.admin_fc, AddProductFragment()).addToBackStack(null).commit()
        }
        rv.layoutManager = GridLayoutManager(requireContext(),2, GridLayoutManager.VERTICAL, false)

        val apiInterface = ApiInterface.create()

        requireActivity().window.setFlags(
            WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
            WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
        )

        apiInterface.products().enqueue(object :
            Callback<List<Product>> {

                override fun onResponse(call: Call<List<Product>>, response: Response<List<Product>>) {
                    requireActivity().window.clearFlags( WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)

                    val products = response.body()

                    println(""+response.raw())
                    if (products != null){
                        val data = ArrayList<Product>()

                        for (p in products) {
                            data.add(Product(p.id, p.name, p.image, p.price, p.pharmacy))
                        }

                        val adapter = ProductAdapter(data,requireActivity(), user)

                        rv.adapter = adapter
                    }else{
                        Toast.makeText(context, "Error!", Toast.LENGTH_SHORT).show()
                    }

                }

                override fun onFailure(call: Call<List<Product>>, t: Throwable) {
                    requireActivity().window.clearFlags( WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)

                    println(t.printStackTrace())
                    Toast.makeText(context, "Connexion error!", Toast.LENGTH_SHORT).show()

                }

            })

    }
}