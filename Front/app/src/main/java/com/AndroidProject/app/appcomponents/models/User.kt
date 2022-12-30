package com.androidproject.app.appcomponents.models

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class User(
    @SerializedName("_id")
    var id: String? = null,
    @SerializedName("username")
    var username: String? = null,
    @SerializedName("password")
    var password: String? = null,
    @SerializedName("email")
    var email: String? = null,
    @SerializedName("phone")
    var phone: String? = null,
    @SerializedName("address")
    var address : String? = null,
    @SerializedName("token")
    var token : String? = null,
    @SerializedName("role")
    var role: String? = null,
): Serializable