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
            Toast.makeText(this, code, Toast.LENGTH_SHORT).show()



            if (validateInput(codeText.text.toString(),code)) {

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
                                res.toString(),
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


            }}

    }

    private fun validateInput(c: String, verif: String?): Boolean {
        if(verif == c)
        return true

        return false

    }
}