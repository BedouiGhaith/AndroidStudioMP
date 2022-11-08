package com.androidproject.app.appcomponents.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import com.androidproject.app.R
import com.androidproject.app.appcomponents.connection.ApiInterface
import com.androidproject.app.appcomponents.models.User
import com.google.android.material.progressindicator.CircularProgressIndicator
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {

    private lateinit var username: EditText
    private lateinit var password: EditText
    private lateinit var loginBtn: AppCompatButton
    private lateinit var progBar: CircularProgressIndicator
    private lateinit var signup: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        username = findViewById(R.id.username)
        password = findViewById(R.id.password)

        loginBtn = findViewById(R.id.btnLogin)

        progBar = findViewById(R.id.progBar)

        signup = findViewById(R.id.txtSignup)

        signup.setOnClickListener {
            val start = Intent(this, SignupActivity::class.java)
            startActivity(start)
        }

        loginBtn.setOnClickListener { login() }



    }
    private fun login(){
        if (validate()){
            val apiInterface = ApiInterface.create()
            progBar.visibility = View.VISIBLE

            window.setFlags(
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
            )


            val requestBody = mapOf("username" to username.text.toString(), "password" to password.text.toString())

            apiInterface.seConnecter(requestBody).enqueue(object :
                Callback<User> {

                override fun onResponse(call: Call<User>, response: Response<User>) {

                    val user = response.body()
                    println(""+response.raw())
                    if (user != null){
                        Toast.makeText(this@LoginActivity, "Login Success", Toast.LENGTH_SHORT).show()
                    }else{
                        Toast.makeText(this@LoginActivity, "User not found", Toast.LENGTH_SHORT).show()
                    }

                    progBar.visibility = View.INVISIBLE
                    window.clearFlags( WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
                }

                override fun onFailure(call: Call<User>, t: Throwable) {
                    println(t.printStackTrace())
                    Toast.makeText(this@LoginActivity, "Connexion error!", Toast.LENGTH_SHORT).show()

                    progBar.visibility = View.INVISIBLE
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

        return true
    }
}