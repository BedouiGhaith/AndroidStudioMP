package com.androidproject.app.appcomponents.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.androidproject.app.R
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.SignInButton
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task

class EmailActivity: AppCompatActivity() {

    val RC_SIGN_IN = 123

    lateinit var sign_in_google : SignInButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welcome)

        sign_in_google = findViewById(R.id.sign_up_google)
        sign_in_google.setSize(SignInButton.SIZE_STANDARD)

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .build()

        val mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        sign_in_google.visibility = View.VISIBLE

        sign_in_google.setOnClickListener{
            val signInIntent = mGoogleSignInClient.signInIntent
            startActivityForResult(signInIntent, RC_SIGN_IN)
        }
        val acct = GoogleSignIn.getLastSignedInAccount(this)
        if (acct != null) {
            sign_in_google.visibility = View.VISIBLE
        }

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

            sign_in_google.visibility = View.GONE

            val mIntent = Intent(this, EmailAndAddressActivity::class.java)
            mIntent.putExtra("email", account.email);
            mIntent.putExtra("idToken", account.idToken);
            mIntent.putExtra("serverAuthCode", account.serverAuthCode);
            startActivity(mIntent);



            Toast.makeText(this@EmailActivity, "Login Success " + account.id, Toast.LENGTH_SHORT).show()

        } catch (e: ApiException) {

            sign_in_google.visibility = View.VISIBLE
            println(e.printStackTrace())

        }

    }
}