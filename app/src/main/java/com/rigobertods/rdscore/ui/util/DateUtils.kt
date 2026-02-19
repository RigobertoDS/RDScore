package com.rigobertods.rdscore.ui.util

import java.time.LocalDate
import java.time.format.DateTimeFormatter

object DateUtils {
    private val apiFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
    private val displayFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")

    /**
     * Convierte una fecha de formato API (yyyy-MM-dd) a formato legible (dd/MM/yyyy).
     * Si el formato es inválido o falla, devuelve la cadena original.
     */
    fun formatearFechaParaDisplay(fechaApi: String): String {
        return try {
            val date = LocalDate.parse(fechaApi, apiFormatter)
            date.format(displayFormatter)
        } catch (_: Exception) {
            fechaApi // Fallback al original si no es formato API
        }
    }
}
