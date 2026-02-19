package com.rigobertods.rdscore.features.perfil.ui

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
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rigobertods.rdscore.R
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaEditarPerfil(
    initialUsername: String,
    initialEmail: String,
    isLoading: Boolean,
    snackbarHostState: SnackbarHostState,
    onGuardarClick: (nombre: String, email: String) -> Unit,
    onCancelarClick: () -> Unit,
    onNavigateBack: () -> Unit
) {
    // Estados para los valores de los campos de texto
    var nombreUsuario by remember { mutableStateOf(initialUsername) }
    var email by remember { mutableStateOf(initialEmail) }

    // Estados para controlar si hay un error en cada campo
    var isNombreUsuarioError by remember { mutableStateOf(false) }
    var isEmailError by remember { mutableStateOf(false) }

    // Resources
    val errorUsernameEmpty = stringResource(R.string.error_username_empty)
    val errorEmailInvalid = stringResource(R.string.error_email_invalid)

    // Estado para el Snackbar
    val scope = rememberCoroutineScope()

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
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) { // Llama a la lambda
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.cd_back),
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Black,
                    titleContentColor = Color.White
                )
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(32.dp))

                Text(
                    text = stringResource(id = R.string.tv_bienvenida_editar),
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
                    modifier = Modifier.fillMaxWidth(),
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
                    value = email,
                    onValueChange = {
                        email = it
                        if (isEmailError) isEmailError = false
                    },
                    label = { Text(stringResource(id = R.string.et_email_editar)) },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Email,
                        imeAction = ImeAction.Done
                    ),
                    isError = isEmailError,
                    supportingText = {
                        if (isEmailError) {
                            Text(stringResource(R.string.val_email_invalid))
                        }
                    }
                )

                Spacer(modifier = Modifier.height(24.dp))

                Button(
                    onClick = {
                        // Validar campos y mostrar Snackbar si hay error
                        isNombreUsuarioError = nombreUsuario.isBlank()
                        isEmailError = !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()

                        when {
                            isNombreUsuarioError -> scope.launch {
                                snackbarHostState.showSnackbar(errorUsernameEmpty)
                            }
                            isEmailError -> scope.launch {
                                snackbarHostState.showSnackbar(errorEmailInvalid)
                            }
                            else -> {
                                // Si es correcto, se procede a guardar
                                onGuardarClick(nombreUsuario, email)
                            }
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = !isLoading
                ) {
                    Text(stringResource(id = R.string.bt_guardar_editar))
                }

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedButton(
                    onClick = { onCancelarClick() },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = !isLoading
                ) {
                    Text(stringResource(id = R.string.bt_cancelar_editar), color = MaterialTheme.colorScheme.onBackground)
                }
            }

            if (isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center),
                    color = MaterialTheme.colorScheme.onBackground)
            }
        }
    }
}
