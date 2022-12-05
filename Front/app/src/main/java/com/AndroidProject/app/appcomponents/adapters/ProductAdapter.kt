package com.androidproject.app.appcomponents.adapters

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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


class ProductAdapter(
    private val dataSet: ArrayList<Product>,
    val context: Context,
    val supportFragmentManager: FragmentManager,
) :
    RecyclerView.Adapter<ProductAdapter.ViewHolder>()  {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view)  {
        val name: TextView
        val image: ImageView
        val price: TextView


        init {
            // Define click listener for the ViewHolder's View.
            name = view.findViewById(R.id.item_product_name)
            image = view.findViewById(R.id.item_product_image)
            price = view.findViewById(R.id.item_product_price)
        }
    }

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        // Create a new view, which defines the UI of the list item
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.row_home, viewGroup, false)

        return ViewHolder(view)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {

        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        viewHolder.name.text = dataSet[position].name.toString()
        Glide.with(context)
            .load("https://images.pexels.com/photos/1152359/pexels-photo-1152359.jpeg?auto=compress&cs=tinysrgb&w=600").apply(
                RequestOptions().override(viewHolder.image.width, viewHolder.image.height))
            .into(viewHolder.image)
        viewHolder.price.text = dataSet[position].price.toString()


        viewHolder.itemView.setOnClickListener {
            Toast.makeText(context, "Item clicked:$position", Toast.LENGTH_SHORT).show()
            val intent = Intent(context, ProductDetailsFragment::class.java)
            intent.putExtra("EXTRA_PRODUCT", dataSet[position] as Serializable)
            val bundle = Bundle()
            bundle.putSerializable("EXTRA_PRODUCT", dataSet[position])

            val fragobj : Fragment = ProductDetailsFragment()
            fragobj.arguments = bundle
            val fm: FragmentManager = (context as AppCompatActivity).supportFragmentManager
            fm.beginTransaction().replace(R.id.fragmentContainerView, fragobj).addToBackStack(null).commit()

        }


    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = dataSet.size

}
