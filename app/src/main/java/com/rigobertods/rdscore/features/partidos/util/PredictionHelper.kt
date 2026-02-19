package com.rigobertods.rdscore.features.partidos.util

import com.rigobertods.rdscore.features.partidos.data.Partido
import com.rigobertods.rdscore.features.partidos.data.TipoPrediccion

object PredictionHelper {

    /**
     * Verifica si una predicción fue acertada basándose en el resultado real del partido.
     * @param partido El partido con resultado final (estado "FT")
     * @param mercado 0 para Resultado 1x2, 1 para BTTS, 2 para Over 2.5
     * @return true si acertó, false si falló, null si no hay datos suficientes
     */
    fun verificarAcierto(partido: Partido, mercado: Int): Boolean? {
        if (partido.estado != "FT") return null
        
        val prediccionDetallada = (partido.prediccion as? TipoPrediccion.Detallada)?.datos ?: return null

        return when (mercado) {
            0 -> { // Resultado 1x2
                val pred = prediccionDetallada.resultado1x2.prediccion // "Local", "Empate", "Visitante"
                val real = partido.resultado // 1: Local, 0: Empate, 2: Visitante
                
                when (pred) {
                    "Local" -> real == 1
                    "Empate" -> real == 0
                    "Visitante" -> real == 2
                    else -> null
                }
            }
            1 -> { // BTTS
                val pred = prediccionDetallada.btts.prediccion // "Sí" o "No"
                val real = partido.golesLocal > 0 && partido.golesVisitante > 0
                
                if (pred == "Sí") real else if (pred == "No") !real else null
            }
            2 -> { // Over 2.5
                val pred = prediccionDetallada.over25.prediccion // "Over" o "Under"
                val real = (partido.golesLocal + partido.golesVisitante) > 2
                
                if (pred == "Over") real else if (pred == "Under") !real else null
            }
            else -> null
        }
    }

    /**
     * Verifica si una predicción fue recomendada por el modelo (tiene valor).
     */
    fun esRecomendada(partido: Partido, mercado: Int): Boolean {
        val prediccionDetallada = (partido.prediccion as? TipoPrediccion.Detallada)?.datos ?: return false
        
        val recomendacion = when (mercado) {
            0 -> prediccionDetallada.resultado1x2.recomendacion
            1 -> prediccionDetallada.btts.recomendacion
            2 -> prediccionDetallada.over25.recomendacion
            else -> return false
        }
        
        return recomendacion.conservadora == 1 || recomendacion.moderada == 1 || recomendacion.arriesgada == 1
    }
}
