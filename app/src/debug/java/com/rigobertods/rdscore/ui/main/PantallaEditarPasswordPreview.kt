package com.rigobertods.rdscore.ui.main

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.rigobertods.rdscore.core.ui.RDScoreTheme
import com.rigobertods.rdscore.features.perfil.ui.PantallaEditarPassword

@Preview(showBackground = true, name = "Estado por Defecto")
@Composable
fun PreviewPantallaEditarPassword() {
    RDScoreTheme {
        PantallaEditarPassword(
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
fun PreviewPantallaEditarPasswordCargando() {
    RDScoreTheme {
        PantallaEditarPassword(
            isLoading = true,
            snackbarHostState = SnackbarHostState(),
            onGuardarClick = { _, _ -> },
            onCancelarClick = {},
            onNavigateBack = {}
        )
    }
}
