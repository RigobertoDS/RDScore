package com.rigobertods.rdscore.data

import android.content.Context
import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.rigobertods.rdscore.core.network.ApiService
import dagger.Lazy
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import javax.inject.Inject
import javax.inject.Singleton

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "app_settings")

@Singleton
class SessionManager @Inject constructor(
    @param:ApplicationContext private val context: Context,
    private val apiService: Lazy<ApiService>
) {
    private val scope = CoroutineScope(Dispatchers.IO + SupervisorJob())
    private val isLoggingOut = java.util.concurrent.atomic.AtomicBoolean(false)

    // In-memory cache for tokens and preferences to avoid runBlocking on Main Thread
    @Volatile
    private var cachedAccessToken: String? = null
    @Volatile
    private var cachedRefreshToken: String? = null
    @Volatile
    private var cachedLanguageCode: String? = null

    // Initial state is false to prevent premature navigation to Main before checking
    private val _isUserLoggedIn = MutableStateFlow(false)
    val isUserLoggedIn: StateFlow<Boolean> = _isUserLoggedIn.asStateFlow()

    companion object {
        private val ACCESS_TOKEN = stringPreferencesKey("access_token")
        private val REFRESH_TOKEN = stringPreferencesKey("refresh_token")
        private val IS_DARK_THEME = booleanPreferencesKey("is_dark_theme")
        private val LANGUAGE_CODE = stringPreferencesKey("language_code")
    }

    init {
        // Collect DataStore to update caches and state
        scope.launch {
            context.dataStore.data.collect { preferences ->
                cachedAccessToken = preferences[ACCESS_TOKEN]
                cachedRefreshToken = preferences[REFRESH_TOKEN]
                cachedLanguageCode = preferences[LANGUAGE_CODE]

                // Update isLoggedIn state based on refresh token presence
                // Note: This makes it reactive. If token is removed, state becomes false.
                _isUserLoggedIn.value = (preferences[REFRESH_TOKEN] != null)
            }
        }
    }

    val isDarkThemeFlow: Flow<Boolean> = context.dataStore.data
        .map { preferences -> preferences[IS_DARK_THEME] ?: true }

    fun saveTokens(accessToken: String, refreshToken: String) {
        isLoggingOut.set(false)
        scope.launch {
            // Update cache immediately
            cachedAccessToken = accessToken
            cachedRefreshToken = refreshToken
            
            context.dataStore.edit { preferences ->
                preferences[ACCESS_TOKEN] = accessToken
                preferences[REFRESH_TOKEN] = refreshToken
            }
        }
    }

    fun saveAccessToken(accessToken: String) {
        isLoggingOut.set(false)
        scope.launch {
            // Update cache immediately
            cachedAccessToken = accessToken
            
            context.dataStore.edit { preferences ->
                preferences[ACCESS_TOKEN] = accessToken
            }
        }
    }

    fun getAccessToken(): String? {
        // Try cache first
        return cachedAccessToken ?: runBlocking {
            context.dataStore.data.map { it[ACCESS_TOKEN] }.first()
        }
    }

    fun getRefreshToken(): String? {
        // Try cache first
        return cachedRefreshToken ?: runBlocking {
            context.dataStore.data.map { it[REFRESH_TOKEN] }.first()
        }
    }

    fun saveTheme(isDark: Boolean) {
        scope.launch {
            context.dataStore.edit { preferences ->
                preferences[IS_DARK_THEME] = isDark
            }
        }
    }

    fun saveLanguage(languageCode: String) {
        // Update cache immediately for instant reads
        cachedLanguageCode = languageCode
        // Persist to DataStore asynchronously
        scope.launch {
            context.dataStore.edit { preferences ->
                preferences[LANGUAGE_CODE] = languageCode
            }
        }
    }

    fun getLanguageCode(): String? = cachedLanguageCode

    fun logout() {
        if (!isLoggingOut.compareAndSet(false, true)) {
            return // Ya se está cerrando sesión, ignorar llamadas concurrentes
        }
        scope.launch {
            try {
                apiService.get().logout()
            } catch (e: Exception) {
                Log.e("SessionManager", "Error al cerrar sesión", e)
            } finally {
                clearLocalData()
                // _isUserLoggedIn update is handled by DataStore collector
            }
        }
    }
    
    fun isLoggingOutState(): Boolean = isLoggingOut.get()

    private suspend fun clearLocalData() {
        // Preserve language and theme settings before clearing
        val savedLanguage = cachedLanguageCode
        val savedTheme = context.dataStore.data.first()[IS_DARK_THEME]
        
        context.dataStore.edit { preferences ->
            preferences.clear()
            // Restore language and theme after clearing auth tokens
            savedLanguage?.let { preferences[LANGUAGE_CODE] = it }
            savedTheme?.let { preferences[IS_DARK_THEME] = it }
        }
    }

    /**
     * Borra TODOS los datos locales (DataStore y Caché/Room) y resetea la app.
     */
    fun clearAllDataSync() {
        runBlocking {
            context.dataStore.edit { it.clear() }
        }
        // Borrar base de datos Room si existe
        context.deleteDatabase("rdscore_database")
    }
}
