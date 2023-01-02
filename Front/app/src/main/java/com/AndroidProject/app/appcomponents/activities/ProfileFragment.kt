package com.androidproject.app.appcomponents.activities

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.text.isDigitsOnly
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

    private lateinit var logout:LinearLayout

    private lateinit var emailEdit:TextInputEditText
    private lateinit var emailLayout: TextInputLayout

    private lateinit var usernameEdit:TextInputEditText
    private lateinit var usernameLayout: TextInputLayout

    private lateinit var passwordEdit:TextInputEditText
    private lateinit var passwordLayout: TextInputLayout

    private lateinit var confirmEdit:TextInputEditText
    private lateinit var confirmLayout: TextInputLayout

    private lateinit var addressEdit:TextInputEditText
    private lateinit var addressLayout: TextInputLayout

    private lateinit var phoneEdit:TextInputEditText
    private lateinit var phoneLayout: TextInputLayout

    private lateinit var edit:Button

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
            if (validator(
                    emailEdit, emailLayout,
                    usernameEdit, usernameLayout,
                    passwordEdit, passwordLayout,
                    confirmEdit, confirmLayout,
                    addressEdit, addressLayout,
                    phoneEdit, phoneLayout
                )
            ) {

                val newUser = User(
                    id = user.id,
                    username = usernameEdit.text.toString(),
                    password = passwordEdit.text.toString(),
                    email = emailEdit.text.toString(),
                    address = addressEdit.text.toString(),
                    phone = phoneEdit.text.toString(),
                    role = user.role,
                    token = user.token
                )

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
                            showAlertWithTextInputLayout(
                                requireContext(),
                                requireActivity(),
                                newUser,
                                code
                            )

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
        textInputLayout.setPadding(resources.getDimensionPixelOffset(R.dimen._19sp))
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
    private fun validator(
        emailEdit: TextInputEditText,
        emailLayout: TextInputLayout,
        usernameEdit: TextInputEditText,
        usernameLayout: TextInputLayout,
        passwordEdit: TextInputEditText,
        passwordLayout: TextInputLayout,
        confirmEdit: TextInputEditText,
        confirmLayout: TextInputLayout,
        addressEdit: TextInputEditText,
        addressLayout: TextInputLayout,
        phoneEdit: TextInputEditText,
        phoneLayout: TextInputLayout
    ): Boolean {

        var result = true

        if(!Patterns.EMAIL_ADDRESS.matcher(emailEdit.text.toString()).matches()|| emailEdit.text.toString().isEmpty()) {
            emailLayout.error= "Email not valid!"
            result = false
        }/*else if{}*/else emailLayout.isErrorEnabled= false
        if(usernameEdit.text.toString().length<5 ) {
            usernameLayout.isErrorEnabled= false
            result = false
        }else
            if(!usernameEdit.text.toString().matches(Regex("^[a-zA-Z0-9]+$"))) {
                usernameLayout.error = "Username must be only digits and numbers!"
                result = false
            }/*else if{}*/else usernameLayout.isErrorEnabled= false
        if (isValidPassword(passwordEdit.text.toString())!= "valid"){
            passwordLayout.error= isValidPassword(passwordEdit.text.toString())
            result = false
        }else passwordLayout.isErrorEnabled= false
        if(confirmEdit.text.toString()!=passwordEdit.text.toString()){
            confirmLayout.error = "Entry is different from the entered Password!"
            result = false
        }else confirmLayout.isErrorEnabled= false
        if(addressEdit.text.toString().isEmpty()){
            addressLayout.error = "Enter Address!"
            result = false
        }else addressLayout.isErrorEnabled= false
        if(phoneEdit.text.toString().isEmpty()||!phoneEdit.text.toString().isDigitsOnly()){
            phoneLayout.error = "Enter a valid phone number!"
            result = false
        }else phoneLayout.isErrorEnabled= false
        return result
    }

    private fun isValidPassword(password: String): String {
        println("password $password")
        if (password.length < 8) return "Password must be at least 8 characters!"
        if (password.firstOrNull { it.isDigit() } == null) return "It must have at least 1 digit!"
        if (password.filter { it.isLetter() }.firstOrNull { it.isUpperCase() } == null) return "Password must have at least 1 uppercase letter!"
        if (password.filter { it.isLetter() }.firstOrNull { it.isLowerCase() } == null) return "Password must have at least 1 lowercase letter!"
        if (password.firstOrNull { !it.isLetterOrDigit() } != null) return "Password must only have letters and digits!"

        return "valid"
    }
}