package com.rigobertods.rdscore.features.equipo.data

import android.util.Log
import com.rigobertods.rdscore.core.network.ApiService
import javax.inject.Inject

import com.rigobertods.rdscore.features.partidos.data.PartidosRepository

class TeamRepository @Inject constructor(
    private val apiService: ApiService,
    private val partidosRepository: PartidosRepository
) {

    suspend fun getDatosEquipo(idEquipo: Int): Result<DatosEquipoResponse> {
        return try {
            val response = apiService.getDatosEquipo(idEquipo)
            if (response.isSuccessful) {
                val body = response.body()
                if (body != null) {
                    // Cachear partidos para navegación
                    val partidos = body.datosEquipo.partidosEquipo.flatMap { it.partidosLiga }
                    partidosRepository.cachePartidos(partidos)
                    
                    Result.success(body)
                } else {
                    Result.failure(Exception("Respuesta vacía del servidor"))
                }
            } else {
                Result.failure(Exception("Error: ${response.code()} - ${response.message()}"))
            }
        } catch (e: Exception) {
            Log.e("TeamRepository", "Error fetching team data", e)
            Result.failure(e)
        }
    }
}
