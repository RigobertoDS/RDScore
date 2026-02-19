package com.rigobertods.rdscore.core.common

import android.content.Context
import com.rigobertods.rdscore.core.util.LocaleHelper
import com.rigobertods.rdscore.data.SessionManager
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LanguageManager @Inject constructor(
    @param:ApplicationContext private val context: Context,
    private val sessionManager: SessionManager
) {
    companion object {
        val SUPPORTED_LANGUAGES = listOf("es", "en", "de", "fr", "it")
        const val DEFAULT_LANGUAGE = "es"
    }

    /**
     * Obtiene el idioma efectivo a usar:
     * 1. Si hay idioma guardado, lo usa
     * 2. Si el idioma del dispositivo es soportado, lo usa
     * 3. Por defecto: español
     */
    fun getEffectiveLanguage(): String {
        // 1. Try Memory Cache from SessionManager (Fastest)
        val savedLanguage = sessionManager.getLanguageCode()
        if (savedLanguage != null) {
            return savedLanguage
        }

        // 2. Try SharedPreferences (Synchronous fallback for cold start)
        // This fixes the race condition where DataStore isn't ready in MainActivity.onCreate
        val persistedLanguage = LocaleHelper.getPersistedLanguage(context)
        if (persistedLanguage != null && persistedLanguage in SUPPORTED_LANGUAGES) {
            return persistedLanguage
        }

        // 3. Fallback to Device or Default
        val deviceLanguage = getDeviceLanguage()
        if (deviceLanguage in SUPPORTED_LANGUAGES) {
            return deviceLanguage
        }

        return DEFAULT_LANGUAGE
    }

    /**
     * Obtiene el código de idioma del dispositivo
     */
    fun getDeviceLanguage(): String {
        return android.content.res.Resources.getSystem().configuration.locales[0].language
    }

    /**
     * Guarda la preferencia de idioma en AMBOS storages:
     * - DataStore (para lectura reactiva en la app)
     * - SharedPreferences (para Application.attachBaseContext en cold start)
     */
    fun setLanguage(languageCode: String) {
        if (languageCode in SUPPORTED_LANGUAGES) {
            sessionManager.saveLanguage(languageCode)
            // Also persist to SharedPreferences for cold start locale
            LocaleHelper.setLocale(context, languageCode)
        }
    }

    /**
     * Devuelve la lista de idiomas soportados con sus nombres para mostrar en UI
     */
    fun getSupportedLanguagesWithNames(): List<LanguageInfo> {
        return listOf(
            LanguageInfo("es", "Español", "\uD83C\uDDEA\uD83C\uDDF8"),
            LanguageInfo("en", "English", "\uD83C\uDDEC\uD83C\uDDE7"),
            LanguageInfo("de", "Deutsch", "\uD83C\uDDE9\uD83C\uDDEA"),
            LanguageInfo("fr", "Français", "\uD83C\uDDEB\uD83C\uDDF7"),
            LanguageInfo("it", "Italiano", "\uD83C\uDDEE\uD83C\uDDF9")
        )
    }
}

data class LanguageInfo(
    val code: String,
    val displayName: String,
    val flag: String
)
