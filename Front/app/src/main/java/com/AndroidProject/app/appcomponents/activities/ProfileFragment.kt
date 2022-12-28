package com.androidproject.app.appcomponents.activities

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.androidproject.app.R
import com.androidproject.app.appcomponents.models.User
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout


class ProfileFragment : Fragment() {

    lateinit var logout:LinearLayout

    private lateinit var emailEdit:TextInputEditText
    private lateinit var emailLayout: TextInputLayout

    private lateinit var usernameEdit:TextInputEditText
    private lateinit var usernameLayout: TextInputLayout

    private lateinit var passwordEdit:TextInputEditText
    private lateinit var passwordLayout: TextInputLayout

    lateinit var confirmEdit:TextInputEditText
    lateinit var confirmLayout: TextInputLayout

    lateinit var addressEdit:TextInputEditText
    lateinit var addressLayout: TextInputLayout

    lateinit var phoneEdit:TextInputEditText
    lateinit var phoneLayout: TextInputLayout

    lateinit var edit:Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        logout = view.findViewById(R.id.logout_btn)

        emailEdit = view.findViewById(R.id.email_edit_p)
        emailLayout = view.findViewById(R.id.email_layout_edit_p)

        usernameEdit = view.findViewById(R.id.username_edit_p)
        usernameLayout = view.findViewById(R.id.username_layout_edit_p)

        passwordEdit = view.findViewById(R.id.password_edit_p)
        passwordLayout = view.findViewById(R.id.password_layout_edit_p)

        confirmEdit = view.findViewById(R.id.confirmP_edit_p)
        confirmLayout = view.findViewById(R.id.confirmP_layout_edit_p)

        addressEdit = view.findViewById(R.id.address_edit_p)
        addressLayout = view.findViewById(R.id.address_layout_edit_p)

        phoneEdit = view.findViewById(R.id.phone_edit_p)
        phoneLayout = view.findViewById(R.id.phone_layout_edit_p)

        edit = view.findViewById(R.id.btn_edit_p)


        var user:User = ((this.activity as FragmentContainerActivity).getLogin())

        logout.setOnClickListener { showAlertDialog() }

    }
    private fun showAlertDialog() {
        val alertDialog: AlertDialog.Builder = AlertDialog.Builder(requireContext())
        alertDialog.setTitle("AlertDialog")
        alertDialog.setMessage("Do you wanna close this Dialog?")
        alertDialog.setPositiveButton(
            "yes"
        ) { _, _ ->
            Toast.makeText(requireContext(), "Alert dialog closed.", Toast.LENGTH_LONG).show()
            var sharedPreferences = requireActivity().getSharedPreferences("shared preferences",
                AppCompatActivity.MODE_PRIVATE
            )
            sharedPreferences.edit().clear().apply()
            sharedPreferences = requireActivity().getSharedPreferences("login", AppCompatActivity.MODE_PRIVATE)
            sharedPreferences.edit().clear().apply()
            val intent = Intent(requireActivity(), LoginActivity::class.java)
            intent.flags =
                Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
            startActivity(intent)
        }
        alertDialog.setNegativeButton(
            "No"
        ) { _, _ -> }
        val alert: AlertDialog = alertDialog.create()
        alert.setCanceledOnTouchOutside(true)
        alert.show()
    }
    private fun showAlertWithTextInputLayout(context: Context) {
        val textInputLayout = TextInputLayout(context)
        //textInputLayout.setPadding(resources.getDimensionPixelOffset(R.dimen.dp_19), // if you look at android alert_dialog.xml, you will see the message textview have margin 14dp and padding 5dp. This is the reason why I use 19 here 0, resources.getDimensionPixelOffset(R.dimen.dp_19), 0)
        val input = EditText(context)
        textInputLayout.hint = "Email"
        textInputLayout.addView(input)

        val alert = AlertDialog.Builder(context)
            .setTitle("Reset Email")
            .setView(textInputLayout)
            .setMessage("Please enter the code sent to your Email")
            .setPositiveButton("Submit") { dialog, _ ->
                // do some thing with input.text
                dialog.cancel()
            }
            .setNegativeButton("Cancel") { dialog, _ ->
                dialog.cancel()
            }.create()

        alert.show()
    }
}