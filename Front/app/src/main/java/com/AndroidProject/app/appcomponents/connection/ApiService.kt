package com.androidproject.app.appcomponents.connection

import com.androidproject.app.appcomponents.models.User
import retrofit2.Response
import retrofit2.http.*

interface ApiService {

    @GET("users")
    suspend fun getUsers(): Response<MutableList<User>>

}