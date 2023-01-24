package com.androidproject.app.appcomponents.activities

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Patterns
import android.view.WindowManager
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.core.text.isDigitsOnly
import androidx.core.view.setPadding
import com.androidproject.app.R
import com.androidproject.app.appcomponents.connection.ApiInterface
import com.androidproject.app.appcomponents.models.User
import com.androidproject.app.appcomponents.utils.UserAndEmailCheck
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SignupActivity : AppCompatActivity() {

    private lateinit var username: TextInputEditText
    private lateinit var password: TextInputEditText
    private lateinit var confirmPassword: TextInputEditText
    private lateinit var email: TextInputEditText
    private lateinit var phone: TextInputEditText
    private lateinit var address: TextInputEditText

    private lateinit var usernameLayout: TextInputLayout
    private lateinit var passwordLayout: TextInputLayout
    private lateinit var confirmPasswordLayout: TextInputLayout
    private lateinit var emailLayout: TextInputLayout
    private lateinit var phoneLayout: TextInputLayout
    private lateinit var addressLayout: TextInputLayout

    private lateinit var validate: AppCompatButton
    private lateinit var emailSignUp: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_account)

        username = findViewById(R.id.r_username)
        password = findViewById(R.id.r_password_txt)
        confirmPassword = findViewById(R.id.r_confirmPass)
        email = findViewById(R.id.r_email)
        phone = findViewById(R.id.r_mobile)
        address = findViewById(R.id.r_address)
        validate = findViewById((R.id.btnCreateAccount))

        usernameLayout = findViewById(R.id.r_username_layout)
        passwordLayout = findViewById(R.id.r_password_txt_layout)
        confirmPasswordLayout = findViewById(R.id.r_confirmPass_layout)
        emailLayout = findViewById(R.id.r_email_layout)
        phoneLayout = findViewById(R.id.r_mobile_layout)
        addressLayout = findViewById(R.id.r_address_layout)


        emailSignUp = findViewById(R.id.email_sign_up)

        email.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    UserAndEmailCheck().checkEmail(email.text.toString(),emailLayout)
            }
        })
        username.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    UserAndEmailCheck().checkUsername(username.text.toString(),usernameLayout)
            }
        })

        validate.setOnClickListener {
            if(validator(email,emailLayout,
                    username,usernameLayout,
                    password,passwordLayout,
                    confirmPassword,confirmPasswordLayout,
                    address,addressLayout,
                    phone,phoneLayout)
            ){
                val requestBody = mapOf("email" to email.text.toString())

                val apiInterface = ApiInterface.create()

                window.setFlags(
                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
                )

                apiInterface.reset(requestBody).enqueue(object : Callback<String> {

                    override fun onResponse(call: Call<String>, response: Response<String>) {
                        val code = response.body()
                        println("" + response.raw())
                        if (code != null) {
                            Toast.makeText(
                                this@SignupActivity,
                                "Code Sent Successfully, check your Email!",
                                Toast.LENGTH_SHORT
                            ).show()
                            showAlertWithTextInputLayout(this@SignupActivity, this@SignupActivity, code)
                        } else {
                            Toast.makeText(
                                this@SignupActivity,
                                "No Such Email Exist",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                        window.clearFlags( WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
                    }

                    override fun onFailure(call: Call<String>, t: Throwable) {
                        println(t.printStackTrace())
                        Toast.makeText(
                            this@SignupActivity,
                            "Connexion error!",
                            Toast.LENGTH_SHORT
                        ).show()
                        window.clearFlags( WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
                    }
                })
            }
        }
    }
    private fun register(){

            val apiInterface = ApiInterface.create()

            window.setFlags(
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
            )

            val requestBody = mapOf("username" to username.text.toString()
                , "password" to password.text.toString()
                , "email" to email.text.toString()
                , "phone" to phone.text.toString()
                , "address" to address.text.toString()
                , "role" to "user")

            apiInterface.signup(requestBody).enqueue(object :
                Callback<User> {

                override fun onResponse(call: Call<User>, response: Response<User>) {

                    val user = response.body()
                    println(""+response.raw())
                    if (user != null){
                        Toast.makeText(this@SignupActivity, "Account Created Successfully", Toast.LENGTH_SHORT).show()
                        val intent = Intent(this@SignupActivity,LoginActivity::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        startActivity(intent)
                    }else{
                        Toast.makeText(this@SignupActivity, "Error!", Toast.LENGTH_SHORT).show()
                    }

                    window.clearFlags( WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
                }

                override fun onFailure(call: Call<User>, t: Throwable) {
                    println(t.printStackTrace())
                    Toast.makeText(this@SignupActivity, "Connexion error!", Toast.LENGTH_SHORT).show()

                    window.clearFlags( WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
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
            }else{
            UserAndEmailCheck().checkEmail(emailEdit.text.toString(),emailLayout)
            if(emailLayout.isErrorEnabled){
                result=  false
            }}
        if(usernameEdit.text.toString().length<5 ) {
            usernameLayout.error = "Username is too short!"
            result = false
        } else if(!usernameEdit.text.toString().matches(Regex("^[a-zA-Z0-9]+$"))) {
            usernameLayout.error = "Username must be only digits and numbers!"
            result = false
        }else {
            UserAndEmailCheck().checkUsername(usernameEdit.text.toString(), usernameLayout)
            if(usernameLayout.isErrorEnabled){
                result = false
            }
        }
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
    private fun showAlertWithTextInputLayout(context: Context, activity: Activity, code: String) {
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
                    register()
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

}