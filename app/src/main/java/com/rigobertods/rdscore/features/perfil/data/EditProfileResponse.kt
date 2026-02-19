package com.rigobertods.rdscore.features.perfil.data

import com.google.gson.annotations.SerializedName

data class EditProfileResponse (
    val id: Int,
    val username: String,
    val email: String,
    @SerializedName("created_at")
    val createdAt: String,
    @SerializedName("mensaje")
    val message: String? = null,
    @SerializedName("success_code")
    val successCode: String? = null
)
