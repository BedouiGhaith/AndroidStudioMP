package com.androidproject.app.appcomponents.activities

import android.content.Intent
import android.os.Bundle
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.androidproject.app.R
import com.androidproject.app.appcomponents.connection.ApiInterface
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class EmailVerify : AppCompatActivity() {

    private lateinit var reset: Button
    private lateinit var codeText : EditText
    lateinit var password : EditText
    private lateinit var passwordv : EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_email_verify)

        val apiInterface = ApiInterface.create()

        reset =findViewById(R.id.reset_btn)
        codeText =findViewById(R.id.code)
        password = findViewById(R.id.editTextTextPassword)
        passwordv = findViewById(R.id.editTextTextPasswordv)

        reset.setOnClickListener {

            val code: String?
            val extras = intent.extras
            code = if (extras == null) {
                ""
            } else {
                extras.getString("verif_code")
            }

            if(password.text.toString()==passwordv.text.toString()){
            if (validateInput(codeText.text.toString(),code,password.text.toString())) {

                val requestBody = mapOf("email" to (extras?.getString("email_verif")), "password" to password.text.toString())

                window.setFlags(
                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
                )

                apiInterface.validate(requestBody).enqueue(object : Callback<String> {


                    override fun onResponse(call: Call<String>, response: Response<String>) {
                        window.clearFlags( WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)

                        val res = response.body()
                        println("" + response.raw())
                        if (res != null) {
                            Toast.makeText(
                                this@EmailVerify,
                                "Successful!",
                                Toast.LENGTH_SHORT
                            ).show()
                            val intent =
                                Intent(this@EmailVerify, LoginActivity::class.java)
                            startActivity(intent)
                        } else {
                            Toast.makeText(
                                this@EmailVerify,
                                "Error",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                    override fun onFailure(call: Call<String>, t: Throwable) {
                        window.clearFlags( WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)

                        println(t.printStackTrace())
                        Toast.makeText(
                            this@EmailVerify,
                            "Connexion error!",
                            Toast.LENGTH_SHORT
                        ).show()


                    }

                })


            }}else Toast.makeText(
                this@EmailVerify,
                "Confirm Password!",
                Toast.LENGTH_SHORT
            ).show()
        }

    }

    private fun validateInput(c: String, verif: String?,password: String): Boolean {
        if (isValidPassword(password)!="valid") {
            Toast.makeText(
                this@EmailVerify,
                isValidPassword(c),
                Toast.LENGTH_SHORT
            ).show()
            return false


        }
        if(verif == c)
        return true

        return false

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