package com.androidproject.app.appcomponents.connection

import com.androidproject.app.appcomponents.models.User
import retrofit2.Call
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

interface ApiInterface {

    @POST("login")
    fun seConnecter(@Body map: Map<String,String> ): Call<User>

    @POST("register")
    fun signup(@Body map: Map<String,String> ): Call<User>

    companion object {

        var BASE_URL = "http://172.24.112.1:9090/"

        fun create() : ApiInterface {

            val retrofit = Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(BASE_URL)
                .build()
            return retrofit.create(ApiInterface::class.java)

        }
    }



}