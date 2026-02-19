package com.rigobertods.rdscore.features.auth.data

import com.rigobertods.rdscore.core.network.ApiService
import com.rigobertods.rdscore.core.network.NetworkUtils
import javax.inject.Inject

class AuthRepository @Inject constructor(private val apiService: ApiService) {

    suspend fun login(loginRequest: LoginRequest): Result<LoginResponse> {
        return NetworkUtils.safeApiCall { apiService.login(loginRequest) }
    }

    suspend fun register(registerRequest: RegisterRequest): Result<RegisterResponse> {
        return NetworkUtils.safeApiCall { apiService.register(registerRequest) }
    }

    suspend fun forgotPassword(request: ForgotPasswordRequest): Result<ForgotPasswordResponse> {
        return NetworkUtils.safeApiCall { apiService.forgotPassword(request) }
    }

    suspend fun resetPassword(request: ResetPasswordRequest): Result<ResetPasswordResponse> {
        return NetworkUtils.safeApiCall { apiService.resetPassword(request) }
    }
}
