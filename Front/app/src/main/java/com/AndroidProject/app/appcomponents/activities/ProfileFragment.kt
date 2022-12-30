package com.androidproject.app.appcomponents.activities

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.setPadding
import androidx.fragment.app.Fragment
import com.androidproject.app.R
import com.androidproject.app.appcomponents.connection.ApiInterface
import com.androidproject.app.appcomponents.models.User
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


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


        val user:User = ((this.activity as FragmentContainerActivity).getLogin())

        emailEdit.setText(user.email)
        usernameEdit.setText(user.username)
        addressEdit.setText(user.address)
        phoneEdit.setText(user.phone)

        logout.setOnClickListener { showAlertDialog() }

        edit.setOnClickListener {

            val newUser = User(id=user.id,
                username = usernameEdit.text.toString(),
                password = passwordEdit.text.toString(),
                email = emailEdit.text.toString(),
                address = addressEdit.text.toString(),
                phone = phoneEdit.text.toString(),
                role = user.role,
                token = user.token)

            val requestBody = mapOf("email" to emailEdit.text.toString())

            val apiInterface = ApiInterface.create()

            apiInterface.reset(requestBody).enqueue(object : Callback<String> {

                override fun onResponse(call: Call<String>, response: Response<String>) {
                    val code = response.body()
                    println("" + response.raw())
                    if (code != null) {
                        Toast.makeText(
                            requireActivity(),
                            "Code Sent Successfully, check your Email!",
                            Toast.LENGTH_SHORT
                        ).show()
                        showAlertWithTextInputLayout(requireContext(),requireActivity(), newUser, code)

                    } else {
                        Toast.makeText(
                            requireActivity(),
                            "No Such Email Exist",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

                override fun onFailure(call: Call<String>, t: Throwable) {
                    println(t.printStackTrace())
                    Toast.makeText(
                        requireActivity(),
                        "Connexion error!",
                        Toast.LENGTH_SHORT
                    ).show()


                }
            })
        }

    }
    private fun showAlertDialog() {
        val alertDialog: AlertDialog.Builder = AlertDialog.Builder(requireContext())
        alertDialog.setTitle("Logout")
        alertDialog.setMessage("Do you want end this session?")
        alertDialog.setPositiveButton(
            "yes"
        ) { _, _ ->
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
    private fun showAlertWithTextInputLayout(context: Context,activity: Activity,user: User, code: String) {
        val textInputLayout = TextInputLayout(context)
        textInputLayout.setPadding(resources.getDimensionPixelOffset(R.dimen._19sp)) // if you look at android alert_dialog.xml, you will see the message textview have margin 14dp and padding 5dp. This is the reason why I use 19 here 0, resources.getDimensionPixelOffset(R.dimen.dp_19), 0)
        val input = EditText(context)
        textInputLayout.hint = "Email"
        textInputLayout.addView(input)

        val alert = AlertDialog.Builder(context)
            .setTitle("Reset Email")
            .setView(textInputLayout)
            .setMessage("Please enter the code sent to your Email")
            .setPositiveButton("Submit") { dialog, _ ->
                if(input.text.toString()==code){
                    register(activity,user)
                    dialog.cancel()
                }else{
                    Toast.makeText(activity, "Wrong code! Check your email...", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("Cancel") { dialog, _ ->
                dialog.cancel()
            }.create()

        alert.show()
    }
    private fun register(activity: Activity, user: User){

            val apiInterface = ApiInterface.create()

        activity.window.setFlags(
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
            )

            apiInterface.edit(user).enqueue(object :
                Callback<User> {

                override fun onResponse(call: Call<User>, response: Response<User>) {

                    val result = response.body()
                    println(""+response.raw())
                    if (result != null){
                        Toast.makeText(activity, "Account Edited Successfully", Toast.LENGTH_SHORT).show()
                    }else{
                        Toast.makeText(activity, "Error!", Toast.LENGTH_SHORT).show()
                    }

                    activity.window.clearFlags( WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
                }

                override fun onFailure(call: Call<User>, t: Throwable) {
                    Toast.makeText(activity, "Connexion error!", Toast.LENGTH_SHORT).show()

                    activity.window.clearFlags( WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
                }

            })

    }
}