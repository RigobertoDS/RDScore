package com.rigobertods.rdscore.core.util

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import java.util.Locale
import androidx.core.content.edit

/**
 * Helper class to manage app locale/language settings.
 * Persists the user's language preference and applies it to the app context.
 * 
 * Note: The SuppressLint for AppBundleLocaleChanges is added because this app
 * includes all language resources in the APK and does not use Play Core's
 * on-demand language delivery feature.
 */
object LocaleHelper {
    
    private const val PREFS_NAME = "locale_prefs"
    private const val KEY_LANGUAGE = "app_language"
    
    /**
     * Sets the app locale and persists the preference.
     * @param context The context to update
     * @param language The language code (e.g., "es", "en", "de", "fra", "it")
     */
    fun setLocale(context: Context, language: String) {
        persist(context, language)
        updateResources(context, language)
    }
    
    /**
     * Gets the persisted language preference.
     * @param context The context to read from
     * @return The persisted language code, or null if not set
     */
    fun getPersistedLanguage(context: Context): String? {
        val prefs: SharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        return prefs.getString(KEY_LANGUAGE, null)
    }
    
    /**
     * Applies the persisted locale to the context if one exists.
     * Should be called in Application.attachBaseContext()
     * @param context The base context
     * @return The updated context with the persisted locale applied
     */
    fun onAttach(context: Context): Context {
        val lang = getPersistedLanguage(context)
        return if (lang != null) {
            updateResources(context, lang)
            context
        } else {
            context
        }
    }
    
    private fun persist(context: Context, language: String) {
        val prefs: SharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        prefs.edit { putString(KEY_LANGUAGE, language) }
    }
    
    @SuppressLint("AppBundleLocaleChanges")
    private fun updateResources(context: Context, language: String) {
        val locale = Locale.forLanguageTag(language)
        Locale.setDefault(locale)
        
        val config = context.resources.configuration
        config.setLocale(locale)
        @Suppress("DEPRECATION")
        context.resources.updateConfiguration(config, context.resources.displayMetrics)
    }
}

