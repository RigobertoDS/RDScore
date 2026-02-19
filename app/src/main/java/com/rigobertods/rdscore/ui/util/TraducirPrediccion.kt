package com.rigobertods.rdscore.ui.util

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.rigobertods.rdscore.R

/**
 * Traduce los valores de predicción del backend al idioma del usuario.
 * Mapea: Local, Empate, Visitante, Sí, No, Over, Under
 */
@Composable
fun traducirPrediccion(valor: String): String {
    return when (valor) {
        // Resultado 1X2
        "Local" -> stringResource(R.string.pred_local)
        "Empate" -> stringResource(R.string.pred_draw)
        "Visitante" -> stringResource(R.string.pred_away)
        // BTTS
        "Sí" -> stringResource(R.string.pred_yes)
        "No" -> stringResource(R.string.pred_no)
        // Over/Under
        "Over" -> stringResource(R.string.pred_over)
        "Under" -> stringResource(R.string.pred_under)
        // Si no coincide, devolver original
        else -> valor
    }
}

/**
 * Traduce los valores de mercado del backend al idioma del usuario.
 * Mapea: Ganador, BTTS, Over 2.5
 */
@Composable
fun traducirMercado(valor: String): String {
    return when (valor) {
        "Ganador" -> stringResource(R.string.market_winner)
        "BTTS" -> stringResource(R.string.market_btts)
        "Over 2.5" -> stringResource(R.string.market_over_under)
        // Fallbacks para otros formatos posibles
        "Resultado" -> stringResource(R.string.market_result)
        "Ambos Marcan" -> stringResource(R.string.market_btts_full)
        "Más/Menos 2.5" -> stringResource(R.string.market_over_under_full)
        // Si no coincide, devolver original
        else -> valor
    }
}

/**
 * Traduce los valores de modelo del backend al idioma del usuario.
 * Mapea: Agresivo, Moderado, Conservador
 */
@Composable
fun traducirModelo(valor: String): String {
    return when (valor) {
        "Agresivo" -> stringResource(R.string.model_aggressive_full)
        "Moderado" -> stringResource(R.string.model_moderate_full)
        "Conservador" -> stringResource(R.string.model_conservative_full)
        // Si no coincide, devolver original
        else -> valor
    }
}

/**
 * Obtiene el texto de recomendación traducido.
 * Mapea: 1 -> Apostar, 0 -> No apostar, else -> No hay datos
 */
@Composable
fun obtenerRecomendacion(recomendacion: Int): String {
    return when (recomendacion) {
        1 -> stringResource(R.string.rec_bet)
        0 -> stringResource(R.string.rec_no_bet)
        else -> stringResource(R.string.rec_no_data)
    }
}

/**
 * Versión no-composable de traducirMensajeServidor para usar en callbacks.
 * @param context El contexto de Android para obtener los string resources
 * @param mensaje El mensaje o código del servidor a traducir
 * @return El mensaje traducido al idioma actual
 */
fun traducirMensaje(context: Context, mensaje: String): String {
    // Intentar buscar por código
    val resId = MessageMapper.getResourceId(mensaje)
    if (resId != null) {
        return context.getString(resId)
    }

    // Fallback: devolver el mensaje original si no es un código conocido
    return mensaje
}

/**
 * Versión composable de traducirMensajeServidor para usar directamente en UI.
 * @param mensaje El mensaje o código del servidor a traducir
 * @return El mensaje traducido al idioma actual
 */
@Composable
fun traducirMensajeUi(mensaje: String): String {
    // Intentar buscar por código
    val resId = MessageMapper.getResourceId(mensaje)
    if (resId != null) {
        return stringResource(resId)
    }

    // Fallback: devolver el mensaje original
    return mensaje
}



