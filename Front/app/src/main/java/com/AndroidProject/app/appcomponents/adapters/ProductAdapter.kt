package com.androidproject.app.appcomponents.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.androidproject.app.R
import com.androidproject.app.appcomponents.activities.ProductInfoActivity
import com.androidproject.app.appcomponents.models.Product
import java.io.Serializable


class ProductAdapter (private val dataSet: ArrayList<Product>, val context: Context) :
    RecyclerView.Adapter<ProductAdapter.ViewHolder>()  {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view)  {
        val name: TextView
        val image: TextView
        val price: TextView
        val pharmacyName: TextView
        val pharmacyaddress: TextView

        init {
            // Define click listener for the ViewHolder's View.
            name = view.findViewById(R.id.product_item_name)
            image = view.findViewById(R.id.product_item_image)
            price = view.findViewById(R.id.product_item_price)
            pharmacyName = view.findViewById(R.id.product_item_pharmacy_name)
            pharmacyaddress = view.findViewById(R.id.product_item_pharmacy_address)
        }
    }

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        // Create a new view, which defines the UI of the list item
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.product_item, viewGroup, false)

        return ViewHolder(view)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {

        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        viewHolder.name.text = dataSet[position].name.toString()
        viewHolder.image.text = dataSet[position].image.toString()
        viewHolder.price.text = dataSet[position].price.toString()
        viewHolder.pharmacyName.text = dataSet[position].pharmacy?.get(0)?.name.toString()
        viewHolder.pharmacyaddress.text = dataSet[position].pharmacy?.get(0)?.owner?.get(0)?.email.toString()

        viewHolder.itemView.setOnClickListener {
            Toast.makeText(context, "Item clicked:$position", Toast.LENGTH_SHORT).show()
            val intent = Intent(context, ProductInfoActivity::class.java)
            intent.putExtra("EXTRA_PRODUCT", dataSet[position] as Serializable)
            context.startActivity(intent)

        }


    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = dataSet.size

}
