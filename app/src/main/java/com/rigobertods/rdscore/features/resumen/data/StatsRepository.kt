package com.rigobertods.rdscore.features.resumen.data

import android.util.Log
import com.rigobertods.rdscore.features.partidos.data.Resumen
import com.rigobertods.rdscore.features.partidos.data.ResumenMercado
import com.rigobertods.rdscore.core.network.ApiService
import com.rigobertods.rdscore.features.cuotascalientes.data.CuotaCalienteResponse
import javax.inject.Inject

class StatsRepository @Inject constructor(private val apiService: ApiService) {

    suspend fun getPrecision(): Result<Resumen> {
        return try {
            val response = apiService.getPrecision()
            if (response.isSuccessful) {
                val body = response.body()
                if (body != null) {
                    Result.success(body.mensaje)
                } else {
                    Result.failure(Exception("Respuesta vacía del servidor"))
                }
            } else {
                Result.failure(Exception("Error: ${response.code()} - ${response.message()}"))
            }
        } catch (e: Exception) {
            Log.e("StatsRepository", "Error fetching precision", e)
            Result.failure(e)
        }
    }

    suspend fun getPrecisionPorMercado(): Result<ResumenMercado> {
        return try {
            val response = apiService.getPrecisionPorMercado()
            if (response.isSuccessful) {
                val body = response.body()
                if (body != null) {
                    Result.success(body.mensaje)
                } else {
                    Result.failure(Exception("Respuesta vacía del servidor"))
                }
            } else {
                Result.failure(Exception("Error: ${response.code()} - ${response.message()}"))
            }
        } catch (e: Exception) {
            Log.e("StatsRepository", "Error fetching market precision", e)
            Result.failure(e)
        }
    }

    suspend fun getCuotasCalientes(): Result<CuotaCalienteResponse> {
        return try {
            val response = apiService.getCuotasCalientes()
            if (response.isSuccessful) {
                val body = response.body()
                if (body != null) {
                    Result.success(body)
                } else {
                    Result.failure(Exception("Respuesta vacía del servidor"))
                }
            } else {
                Result.failure(Exception("Error: ${response.code()} - ${response.message()}"))
            }
        } catch (e: Exception) {
            Log.e("StatsRepository", "Error fetching hot odds", e)
            Result.failure(e)
        }
    }
}
