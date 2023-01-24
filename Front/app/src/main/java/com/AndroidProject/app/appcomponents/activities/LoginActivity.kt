package com.androidproject.app.appcomponents.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import com.androidproject.app.R
import com.androidproject.app.appcomponents.activities.admin.AdminFragmentContainer
import com.androidproject.app.appcomponents.activities.transporter.OrderFragmentContainer
import com.androidproject.app.appcomponents.connection.ApiInterface
import com.androidproject.app.appcomponents.models.User
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.SignInButton
import com.google.android.gms.common.api.ApiException
import com.google.android.material.progressindicator.CircularProgressIndicator
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


@Suppress("DEPRECATION")
class LoginActivity : AppCompatActivity() {

    private lateinit var username: TextInputEditText
    private lateinit var password: TextInputEditText
    private lateinit var usernameLayout: TextInputLayout
    private lateinit var passwordLayout: TextInputLayout
    private lateinit var loginBtn: AppCompatButton
    private lateinit var progBar: CircularProgressIndicator
    private lateinit var signup: TextView

    private lateinit var signInButton: SignInButton
    private lateinit var forgotPassword: TextView

    private val rcSignIn = 123

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        username = findViewById(R.id.username)
        password = findViewById(R.id.password)

        usernameLayout = findViewById(R.id.layout_username)
        passwordLayout = findViewById(R.id.layout_password)

        loginBtn = findViewById(R.id.btnLogin)

        progBar = findViewById(R.id.progBar)

        signup = findViewById(R.id.txtSignup)

        signInButton = findViewById(R.id.sign_in_button)
        signInButton.setSize(SignInButton.SIZE_STANDARD)

        forgotPassword = findViewById(R.id.txtForgotPassword)


        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .build()

        val mGoogleSignInClient = GoogleSignIn.getClient(this, gso)

        signInButton.visibility = View.INVISIBLE
        signInButton.setOnClickListener{
            mGoogleSignInClient.signOut()
            val signInIntent = mGoogleSignInClient.signInIntent
            startActivityForResult(signInIntent, rcSignIn)
        }
        val acct = GoogleSignIn.getLastSignedInAccount(this)
        if (acct != null) {
            signInButton.visibility = View.VISIBLE

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
    private fun login() {
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

                        val sharedPreferences = getSharedPreferences("login", MODE_PRIVATE)

                        val gson = Gson()

                        val editor = sharedPreferences.edit()

                        val jsonUser: String = gson.toJson(user)

                        editor.putString("user", jsonUser)

                        editor.apply()

                        if(user.role == "user") {
                            val intent =
                                Intent(this@LoginActivity, FragmentContainerActivity::class.java)
                            startActivity(intent)
                        }
                        if(user.role == "transporter") {
                            val intent =
                                Intent(this@LoginActivity, OrderFragmentContainer::class.java)
                            startActivity(intent)
                        }
                        if(user.role == "admin") {
                            val intent =
                                Intent(this@LoginActivity, AdminFragmentContainer::class.java)
                            startActivity(intent)
                        }

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

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == rcSignIn) {

            handleSignInResult()
        }
    }
    private fun handleSignInResult() {
        try {
            Toast.makeText(this@LoginActivity, "Login Success", Toast.LENGTH_SHORT).show()

        } catch (e: ApiException) {

            signInButton.visibility = View.VISIBLE
            println(e.printStackTrace())

        }

    }
}