package com.androidproject.app.appcomponents.adapters

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import com.androidproject.app.R
import com.androidproject.app.appcomponents.activities.ProductDetailsFragment
import com.androidproject.app.appcomponents.models.Product
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import java.io.Serializable

class CartAdapter (private val dataSet: ArrayList<Product>,
                   private val Quantity: ArrayList<Int>,
                   val context: Context,
                   val supportFragmentManager: FragmentManager) :
    RecyclerView.Adapter<CartAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val name: TextView
        val image: ImageView
        val price: TextView
        val minus: FrameLayout
        val plus: FrameLayout
        val quantity: TextView
        val remove: ImageView


        init {
            // Define click listener for the ViewHolder's View.
            name = view.findViewById(R.id.cart_row_name)
            image = view.findViewById(R.id.cart_row_image)
            price = view.findViewById(R.id.cart_row_price)
            minus = view.findViewById(R.id.cart_row_minus)
            plus = view.findViewById(R.id.cart_row_plus)
            quantity = view.findViewById(R.id.cart_row_quantity)
            remove = view.findViewById(R.id.cart_row_remove)
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.row_cart, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        viewHolder.name.text = dataSet[position].name.toString()
        Glide.with(context)
            .load("https://images.pexels.com/photos/1152359/pexels-photo-1152359.jpeg?auto=compress&cs=tinysrgb&w=600").apply(
                RequestOptions().override(viewHolder.image.width, viewHolder.image.height))
            .into(viewHolder.image)
        viewHolder.price.text = dataSet[position].price.toString()
        viewHolder.quantity.text = Quantity[position].toString()

    }

    override fun getItemCount() = dataSet.size
}