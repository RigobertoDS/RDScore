package com.rigobertods.rdscore.ui.components

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class FiltroState(
    val resultadoConservador: Boolean = false,
    val resultadoModerado: Boolean = false,
    val resultadoAgresivo: Boolean = false,
    val bttsConservador: Boolean = false,
    val bttsModerado: Boolean = false,
    val bttsAgresivo: Boolean = false,
    val overUnderConservador: Boolean = false,
    val overUnderModerado: Boolean = false,
    val overUnderAgresivo: Boolean = false
) : Parcelable {
    val algunFiltroActivo: Boolean
        get() = resultadoConservador || resultadoModerado || resultadoAgresivo ||
                bttsConservador || bttsModerado || bttsAgresivo ||
                overUnderConservador || overUnderModerado || overUnderAgresivo

    fun marcarTodo(): FiltroState {
        return copy(
            resultadoConservador = true, resultadoModerado = true, resultadoAgresivo = true,
            bttsConservador = true, bttsModerado = true, bttsAgresivo = true,
            overUnderConservador = true, overUnderModerado = true, overUnderAgresivo = true
        )
    }

    fun desmarcarTodo(): FiltroState {
        return copy(
            resultadoConservador = false, resultadoModerado = false, resultadoAgresivo = false,
            bttsConservador = false, bttsModerado = false, bttsAgresivo = false,
            overUnderConservador = false, overUnderModerado = false, overUnderAgresivo = false
        )
    }

    fun esTodoMarcado(): Boolean {
        return resultadoConservador && resultadoModerado && resultadoAgresivo &&
                bttsConservador && bttsModerado && bttsAgresivo &&
                overUnderConservador && overUnderModerado && overUnderAgresivo
    }

    fun marcarSoloResultado(): FiltroState {
        return copy(
            resultadoConservador = true, resultadoModerado = true, resultadoAgresivo = true
        )
    }

    fun desmarcarSoloResultado(): FiltroState {
        return copy(
            resultadoConservador = false, resultadoModerado = false, resultadoAgresivo = false
        )
    }

    fun esSoloResultadoMarcado(): Boolean {
        return resultadoConservador && resultadoModerado && resultadoAgresivo
    }

    fun marcarSoloBTTS(): FiltroState {
        return copy(
            bttsConservador = true, bttsModerado = true, bttsAgresivo = true
        )
    }

    fun desmarcarSoloBTTS(): FiltroState {
        return copy(
            bttsConservador = false, bttsModerado = false, bttsAgresivo = false
        )
    }

    fun esSoloBTTSMarcado(): Boolean {
        return bttsConservador && bttsModerado && bttsAgresivo
    }

    fun marcarSoloOverUnder(): FiltroState {
        return copy(
            overUnderConservador = true, overUnderModerado = true, overUnderAgresivo = true
        )
    }

    fun desmarcarSoloOverUnder(): FiltroState {
        return copy(
            overUnderConservador = false, overUnderModerado = false, overUnderAgresivo = false
        )
    }

    fun esSoloOverUnderMarcado(): Boolean {
        return overUnderConservador && overUnderModerado && overUnderAgresivo
    }
}
