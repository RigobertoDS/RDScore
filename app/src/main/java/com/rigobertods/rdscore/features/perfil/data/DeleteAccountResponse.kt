package com.rigobertods.rdscore.features.perfil.data

import com.google.gson.annotations.SerializedName

data class DeleteAccountResponse (
    @SerializedName("mensaje")
    val mensaje: String,
    @SerializedName("success_code")
    val successCode: String? = null
)
