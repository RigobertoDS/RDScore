package com.rigobertods.rdscore.features.auth.data

import com.google.gson.annotations.SerializedName

data class ResetPasswordRequest (
    val code: String,
    @SerializedName("new_password")
    val newPassword: String
)
