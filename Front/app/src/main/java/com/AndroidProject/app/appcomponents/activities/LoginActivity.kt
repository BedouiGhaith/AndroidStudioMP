package com.androidproject.app.appcomponents.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import com.androidproject.app.R
import com.androidproject.app.appcomponents.connection.ApiInterface
import com.androidproject.app.appcomponents.models.User
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.SignInButton
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
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

    lateinit var gso: GoogleSignInOptions
    lateinit var gsc: GoogleSignInClient
    lateinit var sign_in_button: SignInButton
    lateinit var forgotPassword: TextView


    val RC_SIGN_IN = 123

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        username = findViewById(R.id.username)
        password = findViewById(R.id.password)

        loginBtn = findViewById(R.id.btnLogin)

        progBar = findViewById(R.id.progBar)

        signup = findViewById(R.id.txtSignup)

        sign_in_button = findViewById(R.id.sign_in_button)
        sign_in_button.setSize(SignInButton.SIZE_STANDARD)

        forgotPassword = findViewById(R.id.txtForgotPassword)


        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .build()

        val mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        sign_in_button.visibility = View.VISIBLE
        sign_in_button.setOnClickListener{
            mGoogleSignInClient.signOut()
            val signInIntent = mGoogleSignInClient.signInIntent
            startActivityForResult(signInIntent, RC_SIGN_IN)
        }
        val acct = GoogleSignIn.getLastSignedInAccount(this)
        if (acct != null) {
            sign_in_button.visibility = View.VISIBLE

        }

        forgotPassword.setOnClickListener {
            val i = Intent(this, ForgotPasswordActivity::class.java)
            startActivity(i)
        }



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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN) {

            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            handleSignInResult(task)
        }
    }
    private fun handleSignInResult(completedTask: Task<GoogleSignInAccount>) {
        try {
            val account = completedTask.getResult(ApiException::class.java)

            //sign_in_button.visibility = View.GONE
            Toast.makeText(this@LoginActivity, "Login Success", Toast.LENGTH_SHORT).show()

        } catch (e: ApiException) {

            sign_in_button.visibility = View.VISIBLE
            println(e.printStackTrace())

        }

    }
}