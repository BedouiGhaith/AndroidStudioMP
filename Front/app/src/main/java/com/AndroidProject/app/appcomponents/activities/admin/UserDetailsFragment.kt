package com.androidproject.app.appcomponents.activities.admin

import android.app.Activity
import android.os.Bundle
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import android.widget.Toast
import androidx.core.text.isDigitsOnly
import androidx.fragment.app.Fragment
import com.androidproject.app.R
import com.androidproject.app.appcomponents.connection.ApiInterface
import com.androidproject.app.appcomponents.models.User
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class UserDetailsFragment : Fragment() {

    private lateinit var emailEdit: TextInputEditText
    private lateinit var emailLayout: TextInputLayout

    private lateinit var usernameEdit: TextInputEditText
    private lateinit var usernameLayout: TextInputLayout

    private lateinit var passwordEdit: TextInputEditText
    private lateinit var passwordLayout: TextInputLayout

    private lateinit var confirmEdit: TextInputEditText
    private lateinit var confirmLayout: TextInputLayout

    private lateinit var addressEdit: TextInputEditText
    private lateinit var addressLayout: TextInputLayout

    private lateinit var phoneEdit: TextInputEditText
    private lateinit var phoneLayout: TextInputLayout

    private lateinit var edit: Button

    private lateinit var roles: Spinner

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_user_details, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        val user: User = requireArguments().getSerializable("EXTRA_USER") as User

        emailEdit = view.findViewById(R.id.email_edit_user)
        emailLayout = view.findViewById(R.id.email_layout_edit_user)

        usernameEdit = view.findViewById(R.id.username_edit_user)
        usernameLayout = view.findViewById(R.id.username_layout_edit_user)

        passwordEdit = view.findViewById(R.id.password_edit_user)
        passwordLayout = view.findViewById(R.id.password_layout_edit_user)

        confirmEdit = view.findViewById(R.id.confirmP_edit_user)
        confirmLayout = view.findViewById(R.id.confirmP_layout_edit_user)

        addressEdit = view.findViewById(R.id.address_edit_user)
        addressLayout = view.findViewById(R.id.address_layout_edit_user)

        phoneEdit = view.findViewById(R.id.phone_edit_user)
        phoneLayout = view.findViewById(R.id.phone_layout_edit_user)

        edit = view.findViewById(R.id.btn_edit_user)

        roles = view.findViewById(R.id.role_spinner)

        val spinnerArray: MutableList<String> = ArrayList()
        spinnerArray.add("user")
        spinnerArray.add("transporter")
        spinnerArray.add("admin")


        val adapter: ArrayAdapter<String> = ArrayAdapter<String>(
            requireContext(), android.R.layout.simple_spinner_item, spinnerArray
        )

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        roles.adapter = adapter

        emailEdit.setText(user.email)
        usernameEdit.setText(user.username)
        addressEdit.setText(user.address)
        phoneEdit.setText(user.phone)
        roles.setSelection(adapter.getPosition(user.role))

        edit.setOnClickListener {

            if(validator(emailEdit,emailLayout,
                    usernameEdit,usernameLayout,
                    passwordEdit,passwordLayout,
                    confirmEdit,confirmLayout,
                    addressEdit,addressLayout,
                    phoneEdit,phoneLayout)
            ) {
                val newUser = User(
                    id = user.id,
                    username = usernameEdit.text.toString(),
                    password = passwordEdit.text.toString(),
                    email = emailEdit.text.toString(),
                    address = addressEdit.text.toString(),
                    phone = phoneEdit.text.toString(),
                    role = roles.selectedItem.toString(),
                    token = user.token
                )

                editProfile(requireActivity(), newUser)
            }
        }
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

    private fun editProfile(activity: Activity, user: User){


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