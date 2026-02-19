package com.rigobertods.rdscore.features.cuotascalientes.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.rigobertods.rdscore.R
import com.rigobertods.rdscore.features.partidos.data.CuotaCaliente
import com.rigobertods.rdscore.features.partidos.data.Partido
import com.rigobertods.rdscore.ui.components.CuotaCalienteItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaCuotasCalientes(
    partidos: List<Partido>,
    cuotasCalientes: List<CuotaCaliente>,
    isLoading: Boolean,
    snackbarHostState: SnackbarHostState,
    onNavigateBack: () -> Unit,
    onPartidoClick: (Partido) -> Unit
) {

    Scaffold(
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
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Black,
                    titleContentColor = Color.White
                )
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            if (isLoading && cuotasCalientes.isEmpty()) {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center),
                    color = MaterialTheme.colorScheme.onBackground
                )
            } else {
                if (cuotasCalientes.isEmpty()) {
                    Text(
                        text = stringResource(R.string.no_valuable_picks_today),
                        style = MaterialTheme.typography.titleSmall,
                        modifier = Modifier.padding(16.dp).fillMaxWidth()
                    )
                } else {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Column {
                            // ... UI ...
                            Text(
                                text = stringResource(R.string.hot_odds_disclaimer),
                                style = MaterialTheme.typography.titleSmall,
                                modifier = Modifier.padding(end = 16.dp, start = 16.dp, top = 16.dp)
                            )
                            Text(
                                text = stringResource(R.string.top_picks),
                                style = MaterialTheme.typography.titleSmall,
                                modifier = Modifier.padding(end = 16.dp, start = 16.dp, bottom = 16.dp)
                            )
                            LazyColumn()
                            {
                                items(cuotasCalientes) { cuota ->
                                    val partido = partidos.find { it.idPartido == cuota.id }
                                    if (partido != null) {
                                        CuotaCalienteItem(
                                            onClick = { onPartidoClick(partido) },
                                            partido = partido,
                                            cuotaCaliente = cuota
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
