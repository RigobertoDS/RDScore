package com.rigobertods.rdscore.features.auth.data

data class RegisterRequest (
    val username: String,
    val password: String,
    val email: String
)
