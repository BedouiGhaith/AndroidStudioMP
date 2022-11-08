package com.androidproject.app.appcomponents.models

data class User(
    var id: Int? = null,
    var username: String? = null,
    var password: String? = null,
    var email: String? = null,
    var phone: String? = null,
    var address : String? = null,
    var token : String? = null
)