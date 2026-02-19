package com.rigobertods.rdscore.features.partidos.data

import android.util.Log
import com.rigobertods.rdscore.core.data.dao.PartidoDao
import com.rigobertods.rdscore.core.data.mapEntityToDomain
import com.rigobertods.rdscore.core.data.mapPartidoToEntity
import com.rigobertods.rdscore.core.network.ApiService
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PartidosRepository @Inject constructor(
    private val apiService: ApiService,
    private val partidoDao: PartidoDao
) {

    /**
     * Obtiene los partidos de una fecha. 
     * Prioriza los datos en caché para carga instantánea, pero refresca desde la red.
     */
    suspend fun getPartidosPorFecha(fechaString: String): Result<List<Partido>> {
        // 1. Intentar cargar desde caché (una sola vez para el resultado inmediato)
        val cachedPartidos = partidoDao.getPartidosPorFechaOnce(fechaString)
        
        return try {
            // 2. Intentar refrescar desde la red
            val response = apiService.getGamesByDate(fechaString)

            if (response.isSuccessful) {
                val partidos = response.body()?.partidosList ?: emptyList()
                
                // Filtrar equipos no encontrados (negocio)
                val filtrados = partidos.filter { 
                    it.nombreEquipoLocal != "Equipo no encontrado" && 
                    it.nombreEquipoVisitante != "Equipo no encontrado" 
                }
                
                // 3. Guardar en caché
                if (filtrados.isNotEmpty()) {
                    partidoDao.deletePartidosPorFecha(fechaString)
                    partidoDao.insertPartidos(filtrados.map { mapPartidoToEntity(it, fechaString) })
                }
                
                Result.success(filtrados)
            } else {
                // Si la red falla pero tenemos caché, devolvemos el caché
                if (cachedPartidos.isNotEmpty()) {
                    Result.success(cachedPartidos.map { mapEntityToDomain(it) })
                } else {
                    Result.failure(Exception("Error del servidor: ${response.code()}"))
                }
            }
        } catch (e: Exception) {
            Log.e("PartidosRepository", "Error de red, intentando usar caché", e)
            // Si hay error de red pero tenemos caché, devolvemos el caché
            if (cachedPartidos.isNotEmpty()) {
                Result.success(cachedPartidos.map { mapEntityToDomain(it) })
            } else {
                Result.failure(e)
            }
        }
    }

    /**
     * Obtiene un partido por su ID desde la caché local.
     */
    suspend fun getPartidoById(id: Int): Partido? {
        return partidoDao.getPartidoById(id)?.let { mapEntityToDomain(it) }
    }

    /**
     * Obtiene partidos de una fecha SOLO desde la caché local (Room).
     * No hace llamadas de red. Usado para mostrar datos pre-cargados instantáneamente.
     * Devuelve null si no hay datos en caché.
     */
    suspend fun getPartidosPorFechaCached(fechaString: String): List<Partido>? {
        val cached = partidoDao.getPartidosPorFechaOnce(fechaString)
        return if (cached.isNotEmpty()) {
            cached.map { mapEntityToDomain(it) }
        } else {
            null
        }
    }

    /**
     * Cachea una lista de partidos en Room para que puedan ser accedidos por ID.
     * Usado por historial y cuotas calientes que obtienen partidos de otras APIs.
     * 
     * IMPORTANT: We use each partido's own fecha as cacheDate (not a shared key)
     * to ensure partidos remain queryable by their real date. Using a different
     * cacheKey would overwrite the cacheDate and make the partido "disappear"
     * from date-based queries due to Room's REPLACE conflict strategy.
     */
    suspend fun cachePartidos(partidos: List<Partido>) {
        if (partidos.isEmpty()) return
        try {
            // Use each partido's fecha as its cacheDate
            partidoDao.insertPartidos(partidos.map { mapPartidoToEntity(it, it.fecha) })
        } catch (e: Exception) {
            Log.d("PartidosRepository", "Error cacheando partidos: ${e.message}")
        }
    }

    /**
     * Precarga silenciosa de partidos para una fecha.
     * No afecta el estado de UI, solo actualiza la caché.
     * Falla silenciosamente si hay errores de red.
     */
    suspend fun prefetchPartidosPorFecha(fechaString: String) {
        try {
            // Si ya tenemos datos en caché, no hacemos nada
            val cached = partidoDao.getPartidosPorFechaOnce(fechaString)
            if (cached.isNotEmpty()) return

            val response = apiService.getGamesByDate(fechaString)
            if (response.isSuccessful) {
                val partidos = response.body()?.partidosList ?: return
                val filtrados = partidos.filter {
                    it.nombreEquipoLocal != "Equipo no encontrado" &&
                    it.nombreEquipoVisitante != "Equipo no encontrado"
                }
                if (filtrados.isNotEmpty()) {
                    partidoDao.insertPartidos(filtrados.map { mapPartidoToEntity(it, fechaString) })
                }
            }
        } catch (e: Exception) {
            // Prefetch falla silenciosamente
            Log.d("PartidosRepository", "Prefetch silencioso falló para $fechaString: ${e.message}")
        }
    }

    /**
     * Enriquece un partido con datos de la red.
     * Si el partido proviene de un endpoint que no incluye predicciones (ej: datos-equipo),
     * intenta cargar el partido completo desde el endpoint principal por fecha.
     * @param partidoOriginal El partido del cache que puede tener predicciones vacías
     * @return El partido enriquecido con predicciones, o el original si falla
     */
    suspend fun enriquecerPartidoDesdeRed(partidoOriginal: Partido): Partido {
        return try {
            val fecha = partidoOriginal.fecha // Formato: "yyyy-MM-dd"
            val response = apiService.getGamesByDate(fecha)
            
            if (response.isSuccessful) {
                val partidos = response.body()?.partidosList ?: return partidoOriginal
                
                // Buscar el partido por ID en la respuesta
                val partidoCompleto = partidos.find { it.idPartido == partidoOriginal.idPartido }
                
                if (partidoCompleto != null) {
                    // Actualizar el cache con el partido completo
                    partidoDao.insertPartidos(listOf(mapPartidoToEntity(partidoCompleto, fecha)))
                    partidoCompleto
                } else {
                    partidoOriginal
                }
            } else {
                partidoOriginal
            }
        } catch (e: Exception) {
            Log.d("PartidosRepository", "Error enriqueciendo partido: ${e.message}")
            partidoOriginal
        }
    }
}
