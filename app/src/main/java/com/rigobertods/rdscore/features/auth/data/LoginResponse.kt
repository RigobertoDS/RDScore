package com.rigobertods.rdscore.features.auth.data

import com.google.gson.annotations.SerializedName

data class LoginResponse(
    @SerializedName("access_token")
    val accessToken: String,
    @SerializedName("refresh_token")
    val refreshToken: String,
    @SerializedName("mensaje")
    val message: String? = null,
    @SerializedName("success_code")
    val successCode: String? = null
)
