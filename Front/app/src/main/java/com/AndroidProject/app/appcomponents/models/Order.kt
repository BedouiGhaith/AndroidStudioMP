package com.androidproject.app.appcomponents.models

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Order(
    @SerializedName("_id")
    var id: String? = null,
    var user: User? = null,
    var product: ArrayList<Product>? = null,
    var status: String? = null,
    var responder: User?= null
):Serializable
