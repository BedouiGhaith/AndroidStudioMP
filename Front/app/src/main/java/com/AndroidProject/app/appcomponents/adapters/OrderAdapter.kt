package com.androidproject.app.appcomponents.adapters

 import android.annotation.SuppressLint
 import android.content.Context
 import android.content.Intent
 import android.os.Bundle
 import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import android.widget.SimpleAdapter
import android.widget.TextView
 import androidx.appcompat.app.AppCompatActivity
 import androidx.fragment.app.Fragment
 import androidx.fragment.app.FragmentManager
 import androidx.recyclerview.widget.RecyclerView
import com.androidproject.app.R
 import com.androidproject.app.appcomponents.activities.MyOrderFragment
 import com.androidproject.app.appcomponents.activities.transporter.OrderDetailsFragment
 import com.androidproject.app.appcomponents.models.Order
 import com.androidproject.app.appcomponents.models.User
 import java.io.Serializable


class OrderAdapter(
    private val dataSet: List<Order>,
    val context: Context,
    val user: User): RecyclerView.Adapter<OrderAdapter.ViewHolder>() {



    class ViewHolder(view: View) : RecyclerView.ViewHolder(view)  {

        val name: TextView
        var address: TextView
        var status: TextView
        var productsList: ListView

        init{
            name = view.findViewById(R.id.user_order)
            address= view.findViewById(R.id.order_address)
            status= view.findViewById(R.id.order_status)
            productsList= view.findViewById(R.id.order_product_list)
        }

    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.order_row, viewGroup, false)
        return ViewHolder(view)
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.name.text =dataSet[position].user?.username
        holder.address.text =dataSet[position].user?.address
        holder.status.text =dataSet[position].status

        holder.setIsRecyclable(false)

        val list = ArrayList<HashMap<String?, String?>>()

        var item: HashMap<String?, String?>
        for (i in dataSet[position].product?.indices!!) {
            item = HashMap()
            item["line1"] = dataSet[position].product?.get(i)?.name
            item["line2"] = dataSet[position].quantity?.get(i)?.toString()
            list.add(item)
        }

        val sa = SimpleAdapter(
            context, list,
            android.R.layout.simple_list_item_2, arrayOf("line1", "line2"), intArrayOf(android.R.id.text1, android.R.id.text2)
        )
        holder.productsList.adapter = sa
        holder.productsList.post {
            if (willMyListScroll(holder.productsList)) {
                holder.productsList.setOnTouchListener { v, _ ->
                    v.parent.requestDisallowInterceptTouchEvent(true)
                    println("position: $position")
                    false
                }
            }
        }
        holder.itemView.setOnClickListener {
            val bundle: Bundle?
            val fragobj : Fragment
            val view: Int
            if (user.role=="transporter") {
                val intent = Intent(context, OrderDetailsFragment::class.java)
                intent.putExtra("EXTRA_ORDER", dataSet[position] as Serializable)
                bundle = Bundle()
                bundle.putSerializable("EXTRA_ORDER", dataSet[position])
                fragobj = OrderDetailsFragment()
                view = R.id.fragmentContainerView3

            }else {
                val intent = Intent(context, MyOrderFragment::class.java)
                intent.putExtra("EXTRA_ORDER", dataSet[position] as Serializable)
                bundle = Bundle()
                bundle.putSerializable("EXTRA_ORDER", dataSet[position])
                fragobj = MyOrderFragment()
                view = R.id.fragmentContainerView
            }


            fragobj.arguments = bundle
            val fm: FragmentManager = (context as AppCompatActivity).supportFragmentManager
            fm.beginTransaction().replace(view, fragobj).addToBackStack(null).commit()
        }
    }

    override fun getItemCount()= dataSet.size

    private fun willMyListScroll(listView: ListView): Boolean {

        return getViewByPosition(listView.count-1,listView)?.measuredHeight!! <listView.height
    }
    private fun getViewByPosition(pos: Int, listView: ListView): View? {
        val firstListItemPosition = listView.firstVisiblePosition
        val lastListItemPosition = firstListItemPosition + listView.childCount - 1
        return if (pos < firstListItemPosition || pos > lastListItemPosition) {
            listView.adapter.getView(pos, null, listView)
        } else {
            val childIndex = pos - firstListItemPosition
            listView.getChildAt(childIndex)
        }
    }
}

