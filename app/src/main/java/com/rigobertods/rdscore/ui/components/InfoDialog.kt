package com.rigobertods.rdscore.ui.components

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.rigobertods.rdscore.R

@Composable
fun InfoDialog(
    show: Boolean,
    onDismiss: () -> Unit
) {
    if (show) {
        AlertDialog(
            onDismissRequest = onDismiss,
            title = {
                Text(stringResource(R.string.info_recommendations))
            },
            text = {
                Text(stringResource(R.string.info_dialog_message))
            },
            confirmButton = {
                TextButton(onClick = onDismiss) {
                    Text(stringResource(R.string.understood), color = MaterialTheme.colorScheme.onBackground)
                }
            }
        )
    }
}

