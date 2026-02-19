package com.rigobertods.rdscore.core.network

import com.google.gson.Gson
import com.rigobertods.rdscore.data.error.ErrorResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Response

class BackendException(val code: String?, override val message: String?) : Exception(message)

object NetworkUtils {

    suspend fun <T> safeApiCall(apiCall: suspend () -> Response<T>): Result<T> {
        return withContext(Dispatchers.IO) {
            try {
                val response = apiCall()
                if (response.isSuccessful) {
                    val body = response.body()
                    if (body != null) {
                        Result.success(body)
                    } else {
                        Result.failure(BackendException(null, "Respuesta vacía del servidor"))
                    }
                } else {
                    val errorBody = response.errorBody()?.string()
                    val result = parseBackendError(errorBody)
                    Result.failure(BackendException(result.first, result.second))
                }
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }

    private fun parseBackendError(errorBody: String?): Pair<String?, String> {
        if (errorBody == null) return Pair(null, "Error desconocido")
        
        return try {
            val errorResponse = Gson().fromJson(errorBody, ErrorResponse::class.java)
            // Prioridad: mensaje > error (legacy) > "Error desconocido"
            val message = errorResponse.mensaje ?: errorResponse.error
            val code = errorResponse.errorCode
            Pair(code, message)
        } catch (_: Exception) {
            // Si falla el parseo, devolvemos el body tal cual o un mensaje genérico
            Pair(null, errorBody)
        }
    }
}
