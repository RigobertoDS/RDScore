package com.rigobertods.rdscore

import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTagsAsResourceId
import androidx.core.content.edit
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.rigobertods.rdscore.core.common.LanguageManager
import com.rigobertods.rdscore.core.common.ThemeManager
import com.rigobertods.rdscore.core.ui.RDScoreTheme
import com.rigobertods.rdscore.data.SessionManager
import com.rigobertods.rdscore.features.auth.ui.LoginScreen
import com.rigobertods.rdscore.features.auth.ui.RecoverPasswordScreen
import com.rigobertods.rdscore.features.auth.ui.RegisterScreen
import com.rigobertods.rdscore.features.auth.ui.ResetPasswordScreen
import com.rigobertods.rdscore.features.cuotascalientes.ui.CuotasCalientesScreen
import com.rigobertods.rdscore.features.equipo.ui.DetalleEquipoScreen
import com.rigobertods.rdscore.features.partidos.ui.DetallePartidoScreen
import com.rigobertods.rdscore.features.partidos.ui.MainScreen
import com.rigobertods.rdscore.features.perfil.ui.EditarPasswordScreen
import com.rigobertods.rdscore.features.perfil.ui.EditarPerfilScreen
import com.rigobertods.rdscore.features.perfil.ui.PerfilScreen
import com.rigobertods.rdscore.features.resumen.ui.ResumenScreen
import com.rigobertods.rdscore.ui.navigation.NavRoutes
import dagger.hilt.android.AndroidEntryPoint
import java.util.Locale
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @Inject
    lateinit var themeManager: ThemeManager
    
    @Inject
    lateinit var languageManager: LanguageManager

    @Inject
    lateinit var sessionManager: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // --- BOMBA DE LIMPIEZA / MIGRATION BOMB (v5.00) ---
        // Se asegura de que la primera vez que se ejecute la v5.00, se borre el rastro anterior
        // para evitar conflictos con la nueva API y BD.
        val sharedPrefs = getSharedPreferences("app_migration", MODE_PRIVATE)
        val migrationDone = sharedPrefs.getBoolean("migration_v5_00_done", false)

        if (!migrationDone) {
            // 1. Borrar datos de sesión y base de datos local
            sessionManager.clearAllDataSync()
            
            // 2. Marcar migración como completada
            sharedPrefs.edit { putBoolean("migration_v5_00_done", true) }
            
            // 3. (Opcional) Log para debug
            android.util.Log.d("RDScoreMigration", "Migración v5.00 completada: Datos borrados.")
        }
        
        // Apply language on startup
        applyLanguage(languageManager.getEffectiveLanguage())

        setContent {
            val isDarkTheme by themeManager.isDarkTheme.collectAsStateWithLifecycle()
            
            // Track current language for recomposition
            var currentLanguage by remember { mutableStateOf(languageManager.getEffectiveLanguage()) }
            val supportedLanguages = languageManager.getSupportedLanguagesWithNames()



            
            // Create configuration for current language
            val locale = Locale.forLanguageTag(currentLanguage)
            val configuration = Configuration(LocalConfiguration.current)
            configuration.setLocale(locale)
            
    @OptIn(androidx.compose.ui.ExperimentalComposeUiApi::class)
        CompositionLocalProvider(LocalConfiguration provides configuration) {
            
            // Move navController here to be accessible for session observation
            val navController = rememberNavController()

            // Observe session state
            val isUserLoggedIn by sessionManager.isUserLoggedIn.collectAsStateWithLifecycle()

            // Effect to handle logout navigation
            androidx.compose.runtime.LaunchedEffect(isUserLoggedIn) {
                if (!isUserLoggedIn) {
                    navController.navigate(NavRoutes.Login.route) {
                        popUpTo(0) { inclusive = true } // Clear backstack completely
                    }
                }
            }

            androidx.compose.foundation.layout.Box(
                modifier = androidx.compose.ui.Modifier.fillMaxSize().semantics { testTagsAsResourceId = true }
            ) {
                RDScoreTheme(darkTheme = isDarkTheme) {
                    
                    // --- MANTENIMIENTO / MAINTENANCE MODE ---
                    // Set to true to block app access during server updates
                    val isMaintenanceActive = false

                    if (isMaintenanceActive) {
                        /*
                        MaintenanceScreen(
                            currentLanguage = currentLanguage,
                            supportedLanguages = supportedLanguages,
                            onLanguageChange = { newLanguage ->
                                languageManager.setLanguage(newLanguage)
                                currentLanguage = newLanguage
                                applyLanguage(newLanguage)
                                recreate()
                            }
                        )
                        */
                    } else {
                        NavHost(navController = navController, startDestination = NavRoutes.Login.route) {
                    
                    composable(NavRoutes.Login.route) {
                        val snackbarMessage = intent.getStringExtra("snackbar_message")
                        LoginScreen(
                            snackbarMessage = snackbarMessage,
                            onNavigateToMain = {
                                navController.navigate(NavRoutes.Main.route) {
                                    popUpTo(NavRoutes.Login.route) { inclusive = true }
                                }
                            },
                            onNavigateToRegister = { navController.navigate(NavRoutes.Registro.route) },
                            onNavigateToRecover = { navController.navigate(NavRoutes.RecuperarCuenta.route) }
                        )
                    }

                    composable(NavRoutes.Registro.route) {
                        RegisterScreen(
                            onNavigateToLogin = {
                                navController.navigate(NavRoutes.Login.route) {
                                    popUpTo(NavRoutes.Registro.route) { inclusive = true }
                                }
                            },
                            onNavigateBack = { navController.popBackStack() }
                        )
                    }

                    composable(NavRoutes.RecuperarCuenta.route) {
                        RecoverPasswordScreen(
                            onNavigateToResetPassword = { email ->
                                navController.navigate(
                                    NavRoutes.RestablecerPassword.createRoute(
                                        email
                                    )
                                )
                            },
                            onNavigateBack = { navController.popBackStack() }
                        )
                    }

                    composable(
                        route = NavRoutes.RestablecerPassword.route,
                        arguments = listOf(navArgument("email") { type = NavType.StringType })
                    ) { backStackEntry ->
                        val email = backStackEntry.arguments?.getString("email") ?: ""
                        ResetPasswordScreen(
                            email = email,
                            onNavigateToLogin = {
                                navController.navigate(NavRoutes.Login.route) {
                                    popUpTo(NavRoutes.Login.route) { inclusive = true }
                                }
                            },
                            onNavigateBack = { navController.popBackStack() }
                        )
                    }

                    composable(NavRoutes.Main.route) {
                        MainScreen(
                            isDarkTheme = isDarkTheme,
                            onToggleTheme = { themeManager.toggleTheme() },
                            onNavigateToDetalle = { partido ->
                                navController.navigate(NavRoutes.DetallePartido.createRoute(partido.idPartido))
                            },
                            onNavigateToPerfil = { navController.navigate(NavRoutes.Perfil.route) },
                            onNavigateToResumen = { navController.navigate(NavRoutes.Resumen.route) },
                            onNavigateToCuotas = { navController.navigate(NavRoutes.CuotasCalientes.route) },
                            currentLanguage = currentLanguage,
                            supportedLanguages = supportedLanguages,
                            onLanguageChange = { newLanguage ->
                                languageManager.setLanguage(newLanguage)
                                currentLanguage = newLanguage
                                applyLanguage(newLanguage)
                                // Recreate activity to apply new locale fully
                                recreate()
                            }
                        )
                    }

                    composable(NavRoutes.Perfil.route) {
                        // User logout handling inside PerfilScreen needs a way to navigate to Log in
                        // But PerfilScreen callback is not yet updated to handle logout navigation explicitly if it relies on activity finish.
                        // We check if PerfilScreen or its ViewModel handles logout.
                        // PerfilViewModel.logout() calls SessionManager.logout().
                        // SessionManager.logout clears data and starts InicioActivity via context.startActivity.
                        // THIS IS A PROBLEM. SessionManager should not start Activity directly in Single-Activity mode.
                        
                        PerfilScreen(
                            onNavigateToEditProfile = { username, email ->
                                navController.navigate(NavRoutes.EditarPerfil.createRoute(username, email))
                            },
                            onNavigateToEditPassword = { navController.navigate(NavRoutes.EditarPassword.route) },
                            onNavigateBack = { navController.popBackStack() }
                        )
                    }

                    composable(
                        route = NavRoutes.DetallePartido.route,
                        arguments = listOf(navArgument("partidoId") { type = NavType.IntType })
                    ) {
                        DetallePartidoScreen(
                            onNavigateToEquipo = { equipoId, ligaId ->
                                navController.navigate(NavRoutes.DetalleEquipo.createRoute(equipoId, ligaId))
                            },
                            onNavigateBack = { navController.popBackStack() }
                        )
                    }
                    
                    composable(
                        route = NavRoutes.DetalleEquipo.route,
                        arguments = listOf(
                            navArgument("equipoId") { type = NavType.IntType },
                            navArgument("ligaId") { type = NavType.IntType }
                        )
                    ) { backStackEntry ->
                        val equipoId = backStackEntry.arguments?.getInt("equipoId") ?: 0
                        val ligaId = backStackEntry.arguments?.getInt("ligaId") ?: 0
                        
                        DetalleEquipoScreen(
                            idEquipo = equipoId,
                            idLiga = ligaId,
                            onNavigateBack = { navController.popBackStack() },
                            onEquipoClick = { nuevoEquipoId, nuevoLigaId ->
                                navController.navigate(NavRoutes.DetalleEquipo.createRoute(nuevoEquipoId, nuevoLigaId))
                            },
                            onPartidoClick = { partidoId ->
                                navController.navigate(NavRoutes.DetallePartido.createRoute(partidoId))
                            }
                        )
                    }

                    composable(
                        route = NavRoutes.EditarPerfil.route,
                        arguments = listOf(
                            navArgument("username") { type = NavType.StringType },
                            navArgument("email") { type = NavType.StringType }
                        )
                    ) { backStackEntry ->
                        val username = backStackEntry.arguments?.getString("username") ?: ""
                        val email = backStackEntry.arguments?.getString("email") ?: ""
                        
                        EditarPerfilScreen(
                            initialUsername = username,
                            initialEmail = email,
                            onNavigateBack = { navController.popBackStack() },
                            onProfileUpdated = { navController.popBackStack() }
                        )
                    }

                    composable(NavRoutes.EditarPassword.route) {
                        EditarPasswordScreen(
                            onNavigateBack = { navController.popBackStack() },
                            onPasswordUpdated = { navController.popBackStack() }
                        )
                    }

                    composable(NavRoutes.Resumen.route) {
                        ResumenScreen(
                            onNavigateBack = { navController.popBackStack() }
                        )
                    }
                    
                    composable(NavRoutes.CuotasCalientes.route) {
                        CuotasCalientesScreen(
                            onNavigateToDetalle = { partido ->
                                navController.navigate(NavRoutes.DetallePartido.createRoute(partido.idPartido))
                            },
                            onNavigateBack = { navController.popBackStack() }
                        )
                    }

                }
                    }
                    }
                }
            }
        }
    }
    
    private fun applyLanguage(languageCode: String) {
        val locale = Locale.forLanguageTag(languageCode)
        Locale.setDefault(locale)
        val config = Configuration(resources.configuration)
        config.setLocale(locale)
        @Suppress("DEPRECATION")
        resources.updateConfiguration(config, resources.displayMetrics)
    }
}
