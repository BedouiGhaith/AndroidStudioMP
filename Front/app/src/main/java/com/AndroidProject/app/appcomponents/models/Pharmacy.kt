package com.androidproject.app.appcomponents.models

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Pharmacy(
    @SerializedName("_id")
    var id: String? = null,
    var name: String? = null,
    var address: String? = null,
    var owner: List<User>? = null,
):Serializable
