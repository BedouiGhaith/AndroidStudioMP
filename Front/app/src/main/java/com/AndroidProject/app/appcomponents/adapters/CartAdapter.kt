package com.androidproject.app.appcomponents.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.BitmapFactory
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.androidproject.app.R
import com.androidproject.app.appcomponents.models.Product
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type

@Suppress("UNCHECKED_CAST", "CAST_NEVER_SUCCEEDS", "NAME_SHADOWING")
class CartAdapter (private val dataSet: ArrayList<Product>,
                   private val Quantity: ArrayList<Int>,
                   val context: Context) :
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

    @SuppressLint("NotifyDataSetChanged")
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {

        val imageBytes = Base64.decode(dataSet[position].image, 0)
        val image = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)

        viewHolder.name.text = dataSet[position].name.toString()
        viewHolder.image.setImageBitmap(image)
        viewHolder.price.text = dataSet[position].price.toString()
        viewHolder.quantity.text = Quantity[position].toString()

        val sharedPreferences = context.getSharedPreferences("shared preferences",
            Context.MODE_PRIVATE
        )

        val gson = Gson()

        val jsonProduct = sharedPreferences.getString("products", null)
        val jsonQuantity = sharedPreferences.getString("quantity", null)

        val typeProduct: Type = object : TypeToken<ArrayList<Product?>?>() {}.type
        val typeQuantity: Type = object : TypeToken<ArrayList<Int?>?>() {}.type

        val productList = gson.fromJson<Any>(jsonProduct, typeProduct) as ArrayList<Product>
        val quantityList = gson.fromJson<Int>(jsonQuantity, typeQuantity) as ArrayList<Int>

        viewHolder.minus.setOnClickListener {
            if(Quantity[position] > 1){
                quantityList[position] = quantityList[position]-1
                Quantity[position]= Quantity[position]-1

                val editor = sharedPreferences.edit()

                val jsonQuanutity: String = gson.toJson(quantityList)

                editor.putString("quantity", jsonQuanutity)
                editor.apply()

                notifyDataSetChanged()
            }
        }
        viewHolder.plus.setOnClickListener {
            Quantity[position]= Quantity[position]+1
            quantityList[position]= quantityList[position]+(1)

            val editor = sharedPreferences.edit()

            val jsonQuantity: String = gson.toJson(quantityList)

            editor.putString("quantity", jsonQuantity)
            editor.apply()

            notifyDataSetChanged()
        }
        viewHolder.remove.setOnClickListener {
            productList.removeAt(position)
            quantityList.removeAt(position)
            dataSet.removeAt(position)
            Quantity.removeAt(position)

            val editor = sharedPreferences.edit()


            val jsonProduct: String = gson.toJson(productList)
            val jsonQuanutity: String = gson.toJson(quantityList)

            editor.putString("products", jsonProduct)
            editor.apply()

            editor.putString("quantity", jsonQuanutity)
            editor.apply()

            Toast.makeText(context, ""+position, Toast.LENGTH_SHORT).show()

            notifyDataSetChanged()
            notifyItemRangeChanged(position,dataSet.size)

        }

    }

    override fun getItemCount() = dataSet.size
}