package com.androidproject.app.appcomponents.activities

import android.content.Context.MODE_PRIVATE
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.androidproject.app.R
import com.androidproject.app.appcomponents.connection.ApiInterface
import com.androidproject.app.appcomponents.models.Order
import com.androidproject.app.appcomponents.models.Product
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.reflect.Type


@Suppress("UNCHECKED_CAST")
class ProductDetailsFragment : Fragment() {

    private lateinit var name: TextView
    private lateinit var price: TextView
    private lateinit var image: ImageView
    private lateinit var cmdButton: Button
    private lateinit var cmdFAD: FloatingActionButton

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_product_details, container, false)
        }
    override fun onViewCreated(itemView: View, savedInstanceState: Bundle?) {
        val product: Product = requireArguments().getSerializable("EXTRA_PRODUCT") as Product

        name = view?.findViewById(R.id.name_cmd)!!
        price = view?.findViewById(R.id.price_cmd)!!
        image = view?.findViewById(R.id.image_cmd)!!
        cmdButton = view?.findViewById(R.id.btn_cmd)!!
        cmdFAD = view?.findViewById(R.id.add_to_cart_fab)!!

        val imageBytes = Base64.decode(product.image, 0)
        val imageBit = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)

        name.text = product.name.toString()
        price.text = product.price.toString()
        image.setImageBitmap(imageBit)
        cmdButton.setOnClickListener {
            val products= ArrayList<Product>()
            val quantity = ArrayList<Int>()
            products.add(product)
            quantity.add(1)
            val myOrder = Order(id = null,
                (this.activity as FragmentContainerActivity?)?.getLogin(),products ,quantity,"Pending",
                product.price?.toFloat(), responder = null)
            orderProducts(myOrder)
            orderProducts(myOrder)
        }
        cmdFAD.setOnClickListener {
            val sharedPreferences = requireActivity().getSharedPreferences("shared preferences", MODE_PRIVATE)

            val gson = Gson()

            var jsonProduct = sharedPreferences.getString("products", null)
            var jsonQuantity = sharedPreferences.getString("quantity", null)


            val typeProduct: Type = object : TypeToken<ArrayList<Product?>?>() {}.type
            val typeQuantity: Type = object : TypeToken<ArrayList<Int?>?>() {}.type

            val productList = gson.fromJson<Any>(jsonProduct, typeProduct) as ArrayList<Product>
            val quantityList = gson.fromJson<Any>(jsonQuantity, typeQuantity) as ArrayList<Int>

            if (!productList.contains(product)) {
                productList.add(product)
                quantityList.add(1)

                val editor = sharedPreferences.edit()

                jsonProduct = gson.toJson(productList)
                jsonQuantity = gson.toJson(quantityList)

                editor.putString("products", jsonProduct)
                editor.apply()

                editor.putString("quantity", jsonQuantity)
                editor.apply()

                val fm: FragmentManager = (context as AppCompatActivity).supportFragmentManager
                fm.beginTransaction().replace(R.id.fragmentContainerView, CartFragment()).addToBackStack(null).commit()
            }
        }
    }
    private fun orderProducts(myOrder: Order) {
        val apiInterface  = ApiInterface.create()
        apiInterface.commandesAdd(myOrder).enqueue(object :
            Callback<Order> {

            override fun onResponse(call: Call<Order>, response: Response<Order>) {
                if (response.body() != null){

                    Toast.makeText(requireContext(), "Success!", Toast.LENGTH_SHORT).show()
                    val fm: FragmentManager = (context as AppCompatActivity).supportFragmentManager
                    fm.beginTransaction().replace(R.id.fragmentContainerView, OrdersFragment()).addToBackStack(null).commit()


                }else{

                    Toast.makeText(requireContext(), "Error!", Toast.LENGTH_SHORT).show()

                }
            }

            override fun onFailure(call: Call<Order>, t: Throwable) {

                println(t.printStackTrace())
                Toast.makeText(requireContext(), "Connexion error!", Toast.LENGTH_SHORT).show()

            }

        })

    }
}