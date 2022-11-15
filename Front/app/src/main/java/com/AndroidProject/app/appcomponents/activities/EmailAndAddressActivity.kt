package com.androidproject.app.appcomponents.activities

import android.os.Bundle
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.androidproject.app.R
import com.androidproject.app.appcomponents.connection.ApiInterface
import com.androidproject.app.appcomponents.models.User
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class EmailAndAddressActivity : AppCompatActivity() {

    private lateinit var phone: EditText
    private lateinit var address: EditText
    private lateinit var confirm: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_email_and_address)

        phone = findViewById(R.id.phone_c)
        address = findViewById(R.id.address_e)
        confirm = findViewById(R.id.confirm)

        confirm.setOnClickListener { register() }


    }


    private fun validate(): Boolean {

        if (phone.text!!.isEmpty()) {
            return false
        }

        if (address.text!!.isEmpty()) {
            return false
        }

        return true
    }

    private fun register() {
        if (validate()) {
            val apiInterface = ApiInterface.create()

            val email: String
            val idToken: String
            val serverAuthCode: String

            val extras = intent.extras
            if (extras != null) {
                email = extras.getString("email").toString()
                idToken = extras.getString("idToken").toString()
                serverAuthCode = extras.getString("serverAuthCode").toString()
            } else return

            window.setFlags(
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
            )

            val requestBody = mapOf(
                "username" to email,
                "password" to "isdvfsdvdgsfgdTervgervoevrkvbebeebefbneef",
                "email" to email,
                "phone" to phone.text.toString(),
                "address" to address.text.toString()
            )

            apiInterface.signup(requestBody).enqueue(object :
                Callback<User> {

                override fun onResponse(call: Call<User>, response: Response<User>) {

                    val user = response.body()
                    System.out.println("" + response.raw())
                    if (user != null) {
                        Toast.makeText(
                            this@EmailAndAddressActivity,
                            "Account Created Successfully",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        Toast.makeText(this@EmailAndAddressActivity, "Error!", Toast.LENGTH_SHORT)
                            .show()
                    }

                    window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
                }

                override fun onFailure(call: Call<User>, t: Throwable) {
                    System.out.println(t.printStackTrace())
                    Toast.makeText(
                        this@EmailAndAddressActivity,
                        "Connexion error!",
                        Toast.LENGTH_SHORT
                    ).show()

                    window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
                }

            })

        }
    }
}