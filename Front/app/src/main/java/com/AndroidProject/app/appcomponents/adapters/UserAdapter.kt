package com.androidproject.app.appcomponents.adapters

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import com.androidproject.app.R
import com.androidproject.app.appcomponents.activities.admin.UserDetailsFragment
import com.androidproject.app.appcomponents.models.User
import java.io.Serializable

class UserAdapter(private val dataSet: List<User>,
                  val context: Context,
                  val user: User,
) :
    RecyclerView.Adapter<UserAdapter.ViewHolder>()  {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view)  {
        val username: TextView
        val email: TextView
        val id: TextView


        init {
            username = view.findViewById(R.id.admin_users_username)
            email = view.findViewById(R.id.admin_users_email)
            id = view.findViewById(R.id.admin_users_id)
        }
    }
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.row_user, viewGroup, false)
        return ViewHolder(view)
    }
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {

        viewHolder.username.text = dataSet[position].username.toString()
        viewHolder.email.text = dataSet[position].email.toString()
        viewHolder.id.text = dataSet[position].id.toString()


        viewHolder.itemView.setOnClickListener {
                val intent = Intent(context, UserDetailsFragment::class.java)
                intent.putExtra("EXTRA_USER", dataSet[position] as Serializable)
                val bundle = Bundle()
                bundle.putSerializable("EXTRA_USER", dataSet[position])

                val fragobj: Fragment = UserDetailsFragment()
                fragobj.arguments = bundle
                val fm: FragmentManager = (context as AppCompatActivity).supportFragmentManager
                fm.beginTransaction()
                    .replace(R.id.admin_fc, fragobj)
                    .addToBackStack(null).commit()
            }
    }
    override fun getItemCount() = dataSet.size

}
