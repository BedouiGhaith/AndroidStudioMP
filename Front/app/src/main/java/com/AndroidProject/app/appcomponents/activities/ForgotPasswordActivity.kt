package com.androidproject.app.appcomponents.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.util.Patterns.*
import android.view.View
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.androidproject.app.R
import com.androidproject.app.appcomponents.connection.ApiInterface
import com.androidproject.app.appcomponents.models.User
import com.androidproject.app.appcomponents.utils.GMailSender
import com.androidproject.app.appcomponents.utils.JavaMailAPI
import com.androidproject.app.appcomponents.utils.JavaMailSender
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class ForgotPasswordActivity : AppCompatActivity() {

    lateinit var etEmail: EditText
    lateinit var btContinue : Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_password)
        viewInitializations()
    }

    private fun viewInitializations() {

        etEmail = findViewById(R.id.et_email)
        btContinue = findViewById(R.id.bt_forget)

        // To show back button in actionbar
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

    }

    private fun validateInput(): Boolean {

        if (etEmail.text.toString().equals("")) {
            etEmail.error = "Please Enter Email"
            return false
        }
        // checking the proper email format
        if (!isEmailValid(etEmail.text.toString())) {
            etEmail.error = "Please Enter Valid Email"
            return false
        }
        return true
    }

    private fun isEmailValid(email: String): Boolean {
        return EMAIL_ADDRESS.matcher(email).matches()
    }

    private val apiInterface = ApiInterface.create()

    fun performForgetPassword (view: View) {
        if (validateInput()) {

            val requestBody = mapOf("email" to etEmail.text.toString())


            apiInterface.reset(requestBody).enqueue(object : Callback<String> {

                override fun onResponse(call: Call<String>, response: Response<String>) {
                    val code = response.body()
                    println(""+response.raw())
                    if (code != null){
                        Toast.makeText(this@ForgotPasswordActivity, "Code Sent Successfully", Toast.LENGTH_SHORT).show()
                        val intent = Intent(this@ForgotPasswordActivity, EmailVerify::class.java)
                        intent.putExtra("verif_code", code)
                        intent.putExtra("email_verif", etEmail.text.toString())
                        startActivity(intent)
                    }else{
                        Toast.makeText(this@ForgotPasswordActivity, "No Such Email Exist", Toast.LENGTH_SHORT).show()
                    }


                }

                override fun onFailure(call: Call<String>, t: Throwable) {
                    println(t.printStackTrace())
                    Toast.makeText(this@ForgotPasswordActivity, "Connexion error!", Toast.LENGTH_SHORT).show()


                }

            })
        }
    }

}


