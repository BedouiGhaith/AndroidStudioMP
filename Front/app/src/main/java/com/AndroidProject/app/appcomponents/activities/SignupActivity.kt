package com.androidproject.app.appcomponents.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import com.androidproject.app.R
import com.androidproject.app.appcomponents.connection.ApiInterface
import com.androidproject.app.appcomponents.models.User
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SignupActivity : AppCompatActivity() {

    private lateinit var username: EditText
    private lateinit var password: EditText
    private lateinit var confirmPassword: EditText
    private lateinit var email: EditText
    private lateinit var phone: EditText
    private lateinit var address: EditText
    private lateinit var validate: AppCompatButton

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

        validate.setOnClickListener { register() }



    }
    private fun register(){
        if (validate()){
            val apiInterface = ApiInterface.create()

            window.setFlags(
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
            )

            val requestBody = mapOf("username" to username.text.toString()
                , "password" to password.text.toString()
                , "email" to email.text.toString()
                , "phone" to phone.text.toString()
                , "address" to address.text.toString())

            apiInterface.signup(requestBody).enqueue(object :
                Callback<User> {

                override fun onResponse(call: Call<User>, response: Response<User>) {

                    val user = response.body()
                    System.out.println(""+response.raw())
                    if (user != null){
                        Toast.makeText(this@SignupActivity, "Account Created Successfully", Toast.LENGTH_SHORT).show()
                    }else{
                        Toast.makeText(this@SignupActivity, "Error!", Toast.LENGTH_SHORT).show()
                    }

                    window.clearFlags( WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
                }

                override fun onFailure(call: Call<User>, t: Throwable) {
                    System.out.println(t.printStackTrace())
                    Toast.makeText(this@SignupActivity, "Connexion error!", Toast.LENGTH_SHORT).show()

                    window.clearFlags( WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
                }

            })

        }

    }
    private fun validate(): Boolean {

        if (username.text!!.isEmpty()){
            return false
        }

        if (password.text!!.isEmpty()){
            return false
        }
        if (phone.text!!.isEmpty()){
            return false
        }
        if (email.text!!.isEmpty()){
            return false
        }
        if (confirmPassword.text!!.isEmpty()){
            return false
        }
        if (address.text!!.isEmpty()){
            return false
        }

        return true
    }
}