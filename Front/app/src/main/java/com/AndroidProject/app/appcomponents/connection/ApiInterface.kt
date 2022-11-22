package com.androidproject.app.appcomponents.connection

import com.androidproject.app.appcomponents.models.User
import org.json.JSONObject
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

    @POST("user/reset")
    fun reset(@Body map: Map<String,String>): Call<String>

    @POST("user/reset/validate")
    fun validate(@Body map: Map<String, String?>): Call<String>

    companion object {

        private var BASE_URL = "http://172.30.96.1:9090/"

        fun create() : ApiInterface {

            val retrofit = Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(BASE_URL)
                .build()
            return retrofit.create(ApiInterface::class.java)

        }
    }



}