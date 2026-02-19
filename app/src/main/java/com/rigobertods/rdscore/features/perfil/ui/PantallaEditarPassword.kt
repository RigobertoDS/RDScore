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
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
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
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rigobertods.rdscore.R
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaEditarPassword(
    isLoading: Boolean,
    snackbarHostState: SnackbarHostState,
    onGuardarClick: (contrasenaActual: String, nuevaContrasena: String) -> Unit,
    onCancelarClick: () -> Unit,
    onNavigateBack: () -> Unit
) {
    var contrasenaActual by remember { mutableStateOf("") }
    var nuevaContrasena by remember { mutableStateOf("") }

    var isContrasenaActualError by remember { mutableStateOf(false) }
    var isNuevaContrasenaError by remember { mutableStateOf(false) }

    var contrasenaActualVisible by remember { mutableStateOf(false) }
    var nuevaContrasenaVisible by remember { mutableStateOf(false) }

    // Resources
    val errorCompleteFields = stringResource(R.string.error_complete_fields)

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
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.cd_back),
                            tint = Color.White
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
                    text = stringResource(id = R.string.tv_bienvenida_editar_password),
                    fontSize = 28.sp
                )

                Spacer(modifier = Modifier.height(32.dp))

                // Campo Contraseña Actual
                OutlinedTextField(
                    value = contrasenaActual,
                    onValueChange = {
                        contrasenaActual = it
                        if (isContrasenaActualError) isContrasenaActualError = false
                    },
                    label = { Text(stringResource(id = R.string.et_contrasena_actual_editar_password)) },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    isError = isContrasenaActualError,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Password,
                        imeAction = ImeAction.Next
                    ),
                    visualTransformation = if (contrasenaActualVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    trailingIcon = {
                        val image = if (contrasenaActualVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff
                        IconButton(onClick = { contrasenaActualVisible = !contrasenaActualVisible }) {
                            Icon(image, "Toggle password visibility")
                        }
                    }
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Campo Nueva Contraseña
                OutlinedTextField(
                    value = nuevaContrasena,
                    onValueChange = {
                        nuevaContrasena = it
                        if (isNuevaContrasenaError) isNuevaContrasenaError = false
                    },
                    label = { Text(stringResource(id = R.string.et_nueva_contrasena_editar_password)) },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    isError = isNuevaContrasenaError,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Password,
                        imeAction = ImeAction.Done
                    ),
                    visualTransformation = if (nuevaContrasenaVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    trailingIcon = {
                        val image = if (nuevaContrasenaVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff
                        IconButton(onClick = { nuevaContrasenaVisible = !nuevaContrasenaVisible }) {
                            Icon(image, "Toggle password visibility")
                        }
                    }
                )

                Spacer(modifier = Modifier.height(24.dp))

                // Botón Guardar
                Button(
                    onClick = {
                        isContrasenaActualError = contrasenaActual.isBlank()
                        isNuevaContrasenaError = nuevaContrasena.isBlank()

                        when {
                            isContrasenaActualError || isNuevaContrasenaError -> scope.launch {
                                snackbarHostState.showSnackbar(errorCompleteFields)
                            }
                            else -> {
                                onGuardarClick(contrasenaActual, nuevaContrasena)
                            }
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = !isLoading
                ) {
                    Text(stringResource(id = R.string.bt_guardar_editar_password))
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Botón Cancelar
                OutlinedButton(
                    onClick = { onCancelarClick() },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = !isLoading
                ) {
                    Text(stringResource(id = R.string.bt_cancelar_editar_password), color = MaterialTheme.colorScheme.onBackground)
                }
            }

            if (isLoading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            }
        }
    }
}
