package com.rigobertods.rdscore.features.auth.data

import com.google.gson.annotations.SerializedName

data class ForgotPasswordResponse(
    @SerializedName("mensaje")
    val message: String,
    @SerializedName("success_code")
    val successCode: String? = null
)
