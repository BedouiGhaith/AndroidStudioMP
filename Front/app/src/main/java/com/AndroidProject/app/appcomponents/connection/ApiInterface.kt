package com.androidproject.app.appcomponents.connection

import com.androidproject.app.appcomponents.models.Order
import com.androidproject.app.appcomponents.models.Pharmacy
import com.androidproject.app.appcomponents.models.Product
import com.androidproject.app.appcomponents.models.User
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*


interface ApiInterface {

    @POST("login")
    fun seConnecter(@Body map: Map<String,String> ): Call<User>

    @POST("register")
    fun signup(@Body map: Map<String,String> ): Call<User>

    @POST("user/users")
    fun users(@Body user: User): Call<List<User>>

    @POST("user/edit")
    fun edit(@Body user: User ): Call<User>

    @POST("user/reset")
    fun reset(@Body map: Map<String,String>): Call<String>

    @POST("user/reset/validate")
    fun validate(@Body map: Map<String, String?>): Call<String>

    @GET("product")
    fun products(): Call<List<Product>>

    @POST("product")
    fun addProducts(@Body product: Product): Call<Product>

    @POST("product/modify")
    fun modifyProducts(@Body product: Product): Call<Product>

    @POST("product/delete")
    fun deleteProducts(@Body product: Product): Call<Product>

    @GET("pharmacy")
    fun pharmacies(): Call<List<Pharmacy>>

    @GET("orders")
    fun commandes(): Call<List<Order>>

    @GET("orders/myorders/{id}")
    fun commandesUser(@Path("id") id:String): Call<List<Order>>

    @Headers("Content-Type: application/json")
    @POST("orders/add")
    fun commandesAdd(@Body order: Order): Call<Order>

    @GET("orders/transporter/{status}")
    fun commandesStatus(@Path("status") status: String): Call<List<Order>>

    @POST("orders/transporter/accept")
    fun commandeAccept(@Body order: Order): Call<Order>

    @GET("orders/transporter/onroute/{id}")
    fun commandesOnRoute(@Path("id") id:String): Call<List<Order>>

    @GET("orders/transporter/finished/{id}")
    fun commandesFinished(@Path("id") id:String): Call<List<Order>>

    companion object {

        private var BASE_URL = "http://192.168.1.3:9090/"

        fun create() : ApiInterface {

            val retrofit = Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(BASE_URL)
                .build()


            return retrofit.create(ApiInterface::class.java)

        }
    }



}