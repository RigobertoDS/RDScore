package com.rigobertods.rdscore.features.partidos.data

import androidx.annotation.StringRes

data class StatItem(
    @param:StringRes val labelResId: Int,
    val valorLocal: String,
    val valorVisitante: String
)
