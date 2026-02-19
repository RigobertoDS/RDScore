package com.rigobertods.rdscore.features.perfil.data

import com.rigobertods.rdscore.core.network.ApiService
import com.rigobertods.rdscore.core.network.NetworkUtils
import javax.inject.Inject

class UserRepository @Inject constructor(private val apiService: ApiService) {

    suspend fun getProfile(): Result<GetProfileResponse> {
        return NetworkUtils.safeApiCall { apiService.getProfile() }
    }

    suspend fun deleteAccount(): Result<DeleteAccountResponse> {
        return NetworkUtils.safeApiCall { apiService.deleteAccount() }
    }

    suspend fun editProfile(request: EditProfileRequest): Result<EditProfileResponse> {
        return NetworkUtils.safeApiCall { apiService.editProfile(request) }
    }

    suspend fun editPassword(request: EditPasswordRequest): Result<EditPasswordResponse> {
        return NetworkUtils.safeApiCall { apiService.editPassword(request) }
    }
}
