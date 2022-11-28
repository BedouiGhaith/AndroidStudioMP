package com.androidproject.app.appcomponents.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.androidproject.app.R
import com.androidproject.app.appcomponents.adapters.ProductAdapter
import com.androidproject.app.appcomponents.connection.ApiInterface
import com.androidproject.app.appcomponents.models.Product
import com.androidproject.app.appcomponents.models.User
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProductsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_products)

        val recyclerview = findViewById<RecyclerView>(R.id.products_recycler)

        recyclerview.layoutManager = LinearLayoutManager(this)

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

                    val adapter = ProductAdapter(data,this@ProductsActivity)

                    recyclerview.adapter = adapter
                }else{
                    Toast.makeText(this@ProductsActivity, "Error!", Toast.LENGTH_SHORT).show()
                }

            }

            override fun onFailure(call: Call<List<Product>>, t: Throwable) {
                println(t.printStackTrace())
                Toast.makeText(this@ProductsActivity, "Connexion error!", Toast.LENGTH_SHORT).show()

            }

        })
    }
}