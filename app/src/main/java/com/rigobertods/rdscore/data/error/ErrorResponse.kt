package com.rigobertods.rdscore.data.error

import com.google.gson.annotations.SerializedName

data class ErrorResponse (
    val error: String,
    @SerializedName("error_code")
    val errorCode: String? = null,
    val mensaje: String? = null,
)
