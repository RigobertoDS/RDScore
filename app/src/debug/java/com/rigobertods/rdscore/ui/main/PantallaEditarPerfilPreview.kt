package com.rigobertods.rdscore.ui.main

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.rigobertods.rdscore.core.ui.RDScoreTheme
import com.rigobertods.rdscore.features.perfil.ui.PantallaEditarPerfil

// Preview para ver el diseño en Android Studio en diferentes estados

@Preview(showBackground = true, name = "Estado por Defecto")
@Composable
fun PreviewPantallaEditarPerfil() {
    RDScoreTheme {
        PantallaEditarPerfil(
            initialUsername = "Rigo",
            initialEmail = "rigo@example.com",
            isLoading = false,
            snackbarHostState = SnackbarHostState(),
            onGuardarClick = { _, _ -> },
            onCancelarClick = {},
            onNavigateBack = {}
        )
    }
}

@Preview(showBackground = true, name = "Estado de Carga")
@Composable
fun PreviewPantallaEditarPerfilCargando() {
    RDScoreTheme {
        PantallaEditarPerfil(
            initialUsername = "Rigo",
            initialEmail = "rigo@example.com",
            isLoading = true, // <-- El Composable en modo carga
            snackbarHostState = SnackbarHostState(),
            onGuardarClick = { _, _ -> },
            onCancelarClick = {},
            onNavigateBack = {}
        )
    }
}
