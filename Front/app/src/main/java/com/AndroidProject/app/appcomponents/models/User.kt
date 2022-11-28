package com.androidproject.app.appcomponents.models

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class User(
    @SerializedName("_id")
    var id: String? = null,
    @SerializedName("username")
    var username: String? = null,
    var password: String? = null,
    var email: String? = null,
    var phone: String? = null,
    var address : String? = null,
    var token : String? = null,
    var role: String? = null,
): Serializable