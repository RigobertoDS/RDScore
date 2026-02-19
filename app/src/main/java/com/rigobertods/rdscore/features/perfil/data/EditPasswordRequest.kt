package com.rigobertods.rdscore.features.perfil.data

import com.google.gson.annotations.SerializedName

data class EditPasswordRequest (
    @SerializedName("current_password")
    val oldPassword: String,
    @SerializedName("new_password")
    val newPassword: String,
)
