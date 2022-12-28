package com.androidproject.app.appcomponents.models

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Order(
    @SerializedName("_id")
    var id: String? = null,
    @SerializedName("user")
    var user: User? = null,
    @SerializedName("product")
    var product: ArrayList<Product>? = null,
    @SerializedName("quantity")
    var quantity: ArrayList<Int>? = null,
    @SerializedName("status")
    var status: String? = null,
    @SerializedName("price")
    var price: Float? = null,
    @SerializedName("responder")
    var responder: User?= null
):Serializable
