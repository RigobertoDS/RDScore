package com.rigobertods.rdscore.features.partidos.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

sealed class TipoPrediccion : Parcelable {
    // Estado 1: La predicción es un objeto detallado
    @Parcelize
    data class Detallada(val datos: Prediccion) : TipoPrediccion()

    // Estado 2: La predicción es un simple mensaje de texto
    @Parcelize
    data class Simple(val mensaje: String) : TipoPrediccion()

    // Estado 3 (Opcional): Para cuando el campo no existe o es nulo
    @Parcelize
    @Suppress("unused", "RedundantSuppression")
    object NoDisponible : TipoPrediccion() {
        private fun readResolve(): Any = NoDisponible
    }
}
