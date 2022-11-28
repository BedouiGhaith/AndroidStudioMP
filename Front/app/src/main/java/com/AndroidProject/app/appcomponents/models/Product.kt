package com.androidproject.app.appcomponents.models

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Product(
    @SerializedName("_id")
    var id: String? = null,
    var name: String? = null,
    var image: String? = null,
    var price: String? = null,
    var pharmacy: List<Pharmacy>? = null,
    ): Serializable
