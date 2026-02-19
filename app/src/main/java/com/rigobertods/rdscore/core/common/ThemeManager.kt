package com.rigobertods.rdscore.core.common

import com.rigobertods.rdscore.data.SessionManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ThemeManager @Inject constructor(
    private val sessionManager: SessionManager
) {
    private val scope = CoroutineScope(Dispatchers.Main + SupervisorJob())

    val isDarkTheme: StateFlow<Boolean> = sessionManager.isDarkThemeFlow
        .stateIn(
            scope = scope,
            started = SharingStarted.Eagerly,
            initialValue = true
        )

    fun toggleTheme() {
        sessionManager.saveTheme(!isDarkTheme.value)
    }
}
