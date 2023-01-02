package com.androidproject.app.appcomponents.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import com.androidproject.app.R
import com.androidproject.app.appcomponents.activities.ProductDetailsFragment
import com.androidproject.app.appcomponents.activities.admin.ModifyProductFragment
import com.androidproject.app.appcomponents.models.Product
import com.androidproject.app.appcomponents.models.User
import java.io.Serializable


class ProductAdapter(
    private var dataSet: ArrayList<Product>,
    val context: Context,
    val user: User,
) :
    RecyclerView.Adapter<ProductAdapter.ViewHolder>()  {
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view)  {
        val name: TextView
        val image: ImageView
        val price: TextView
        init {
            name = view.findViewById(R.id.item_product_name)
            image = view.findViewById(R.id.item_product_image)
            price = view.findViewById(R.id.item_product_price)
        }
    }
    @SuppressLint("NotifyDataSetChanged")
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.row_home, viewGroup, false)


        return ViewHolder(view)
    }
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {

        val imageBytes = Base64.decode(dataSet[position].image, 0)
        val image = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)

        viewHolder.name.text = dataSet[position].name.toString()
        viewHolder.image.setImageBitmap(image)
        viewHolder.price.text = dataSet[position].price.toString()

        if(user.role=="user") {
            viewHolder.itemView.setOnClickListener {
                val intent = Intent(context, ProductDetailsFragment::class.java)
                intent.putExtra("EXTRA_PRODUCT", dataSet[position] as Serializable)
                val bundle = Bundle()
                bundle.putSerializable("EXTRA_PRODUCT", dataSet[position])

                val fragobj: Fragment = ProductDetailsFragment()
                fragobj.arguments = bundle
                val fm: FragmentManager = (context as AppCompatActivity).supportFragmentManager
                fm.beginTransaction()
                    .replace(R.id.fragmentContainerView, fragobj)
                    .addToBackStack(null).commit()
            }
        }
        if(user.role=="admin") {
            viewHolder.itemView.setOnClickListener {
                val intent = Intent(context, ProductDetailsFragment::class.java)
                intent.putExtra("EXTRA_PRODUCT", dataSet[position] as Serializable)
                val bundle = Bundle()
                bundle.putSerializable("EXTRA_PRODUCT", dataSet[position])

                val fragobj: Fragment = ModifyProductFragment()
                fragobj.arguments = bundle
                val fm: FragmentManager = (context as AppCompatActivity).supportFragmentManager
                fm.beginTransaction()
                    .replace(R.id.admin_fc, fragobj)
                    .addToBackStack(null).commit()
            }
        }
    }
    override fun getItemCount() = dataSet.size

    fun filterList(filterlist: ArrayList<Product>) {
        dataSet = filterlist
        notifyDataSetChanged()
    }

}
