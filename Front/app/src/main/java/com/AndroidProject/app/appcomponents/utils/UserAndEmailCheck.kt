package com.androidproject.app.appcomponents.utils


import com.androidproject.app.appcomponents.connection.ApiInterface
import com.google.android.material.textfield.TextInputLayout
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UserAndEmailCheck {

    fun checkUsername(username: String, layout: TextInputLayout){

        println("checking")
        val apiInterface = ApiInterface.create()

        apiInterface.checkUsername(username).enqueue(object :
            Callback<Int> {
            override fun onResponse(call: Call<Int>, response: Response<Int>) {
                println("checking 2")
                if (response.body() != null){
                    println("checking 3")
                    if (response.body()!! >0){
                        layout.error= "Username Already Exists!"
                    }else{
                        layout.isErrorEnabled= false
                    }
                }
            }
            override fun onFailure(call: Call<Int>, t: Throwable) {

            }
        })
    }


    fun checkEmail(email: String, layout: TextInputLayout){
        val apiInterface = ApiInterface.create()

        apiInterface.checkEmail(email).enqueue(object :
            Callback<Int> {
            override fun onResponse(call: Call<Int>, response: Response<Int>) {
                if (response.body() != null){
                    if (response.body()!! >0){
                        layout.error= "Email Already Exists!"
                    }else{
                        layout.isErrorEnabled= false
                    }
                }
            }
            override fun onFailure(call: Call<Int>, t: Throwable) {

            }
        })
    }
}