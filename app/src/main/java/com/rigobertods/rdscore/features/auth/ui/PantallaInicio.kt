package com.rigobertods.rdscore.features.auth.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rigobertods.rdscore.R
import com.rigobertods.rdscore.core.common.LanguageInfo
import com.rigobertods.rdscore.ui.components.LanguageSelectorDialog
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaInicio(
    isLoading: Boolean,
    snackbarHostState: SnackbarHostState,
    snackbarMessage: String?,
    currentLanguage: String,
    onLanguageChange: (String) -> Unit,
    onIniciarSesionClick: (usuario: String, pass: String) -> Unit,
    onRecuperarClick: () -> Unit,
    onRegistrarClick: () -> Unit
) {
    // Estados para los valores de los campos de texto
    var nombreUsuario by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    // Estados para controlar si hay un error en cada campo
    var isNombreUsuarioError by remember { mutableStateOf(false) }
    var isPasswordError by remember { mutableStateOf(false) }
    var passwordVisible by remember { mutableStateOf(false) }

    // Resources
    val errorUsernameEmpty = stringResource(R.string.error_username_empty)
    val errorPasswordEmpty = stringResource(R.string.error_password_empty)

    // Estado para el Snackbar
    val scope = rememberCoroutineScope()

    LaunchedEffect(snackbarMessage) {
        snackbarMessage?.let {
            snackbarHostState.showSnackbar(it)
        }
    }

    // Estado para el selector de idioma
    var showLanguageSelector by remember { mutableStateOf(false) }
    
    // Lista de idiomas soportados
    val supportedLanguages = listOf(
        LanguageInfo("es", "Español", "🇪🇸"),
        LanguageInfo("en", "English", "🇬🇧"),
        LanguageInfo("de", "Deutsch", "🇩🇪"),
        LanguageInfo("fr", "Français", "🇫🇷"),
        LanguageInfo("it", "Italiano", "🇮🇹")
    )

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                title = {
                    Icon(
                        painter = painterResource(id = R.mipmap.ic_launcher),
                        contentDescription = stringResource(R.string.cd_logo),
                        modifier = Modifier.size(40.dp),
                        tint = Color.Unspecified
                    )
                },
                actions = {
                    IconButton(onClick = { showLanguageSelector = true }) {
                        Text(
                            text = when (currentLanguage) {
                                "es" -> "🇪🇸"
                                "en" -> "🇬🇧"
                                "de" -> "🇩🇪"
                                "fr" -> "🇫🇷"
                                "it" -> "🇮🇹"
                                else -> "🌍"
                            },
                            fontSize = 24.sp
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Black)
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentAlignment = Alignment.Center
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = stringResource(id = R.string.tv_bienvenida),
                    fontSize = 28.sp
                )

                Spacer(modifier = Modifier.height(32.dp))

                OutlinedTextField(
                    value = nombreUsuario,
                    onValueChange = {
                        nombreUsuario = it
                        if (isNombreUsuarioError) isNombreUsuarioError = false
                    },
                    label = { Text(stringResource(id = R.string.et_usuario_editar)) },
                    modifier = Modifier.fillMaxWidth().testTag("login_username"),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                    isError = isNombreUsuarioError,
                    supportingText = {
                        if (isNombreUsuarioError) {
                            Text(stringResource(R.string.val_name_empty))
                        }
                    }
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = password,
                    onValueChange = {
                        password = it
                        if (isPasswordError) isPasswordError = false
                    },
                    label = { Text(stringResource(id = R.string.et_contrasena)) },
                    modifier = Modifier.fillMaxWidth().testTag("login_password"),
                    singleLine = true,
                    isError = isPasswordError,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Password,
                        imeAction = ImeAction.Done
                    ),
                    visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    trailingIcon = {
                        val image = if (passwordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff
                        IconButton(onClick = { passwordVisible = !passwordVisible }) {
                            Icon(image, "Toggle password visibility")
                        }
                    }
                )

                TextButton(
                    onClick = onRecuperarClick,
                    modifier = Modifier.align(Alignment.End)
                ) {
                    Text(stringResource(
                        id = R.string.tv_recuperar_contrasena),
                        color = MaterialTheme.colorScheme.onBackground)
                }

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = {
                        // Validar campos y mostrar Snackbar si hay error
                        isNombreUsuarioError = nombreUsuario.isBlank()
                        isPasswordError = password.isBlank()


                        when {
                            isNombreUsuarioError -> scope.launch {
                                snackbarHostState.showSnackbar(errorUsernameEmpty)
                            }
                            isPasswordError -> scope.launch {
                                snackbarHostState.showSnackbar(errorPasswordEmpty)
                            }
                            else -> {
                                // Si es correcto, se procede a guardar
                                onIniciarSesionClick(nombreUsuario, password) }
                            }
                        },
                    modifier = Modifier.fillMaxWidth().testTag("login_button"),
                    enabled = !isLoading
                ) {
                    Text(stringResource(id = R.string.bt_iniciar_sesion))
                }

                TextButton(onClick = onRegistrarClick) {
                    Text(stringResource(
                        id = R.string.tv_registrarse),
                        color = MaterialTheme.colorScheme.onBackground)
                }
            }

            if (isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center),
                    color = MaterialTheme.colorScheme.onBackground
                )
            }
        }
    }
    
    // Language selector dialog
    if (showLanguageSelector) {
        LanguageSelectorDialog(
            currentLanguage = currentLanguage,
            languages = supportedLanguages,
            onLanguageSelected = { languageCode ->
                @Suppress("AssignedValueIsNeverRead")
                showLanguageSelector = false
                scope.launch {
                    delay(300)
                    onLanguageChange(languageCode)
                }
            },
            onDismiss = {
                @Suppress("AssignedValueIsNeverRead")
                showLanguageSelector = false
            }
        )
    }
}
