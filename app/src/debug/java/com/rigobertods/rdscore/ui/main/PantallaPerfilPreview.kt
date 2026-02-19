package com.rigobertods.rdscore.ui.main

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.rigobertods.rdscore.core.ui.RDScoreTheme
import com.rigobertods.rdscore.features.perfil.ui.PantallaPerfil

@Preview(showBackground = true, name = "Estado por Defecto")
@Composable
fun PreviewPantallaPerfil() {
    RDScoreTheme {
        PantallaPerfil(
            username = "rigobertods",
            email = "rigo.sanchez@email.com",
            isLoading = false,
            snackbarHostState = SnackbarHostState(),
            onEditClick = {},
            onChangePasswordClick = {},
            onLogoutClick = {},
            onDeleteAccountClick = {},
            onNavigateBack = {}
        )
    }
}

@Preview(showBackground = true, name = "Estado de Carga")
@Composable
fun PreviewPantallaPerfilCargando() {
    RDScoreTheme {
        PantallaPerfil(
            username = "",
            email = "",
            isLoading = true,
            snackbarHostState = SnackbarHostState(),
            onEditClick = {},
            onChangePasswordClick = {},
            onLogoutClick = {},
            onDeleteAccountClick = {},
            onNavigateBack = {}
        )
    }
}
