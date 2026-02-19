package com.rigobertods.rdscore.features.partidos.data

import com.google.gson.annotations.SerializedName

data class ResumenMercado (
    @SerializedName("resultado")
    val resultado: ModeloResumenMercado = ModeloResumenMercado(),

    @SerializedName("btts")
    val btts: ModeloResumenMercado = ModeloResumenMercado(),

    @SerializedName("over")
    val over: ModeloResumenMercado = ModeloResumenMercado()
)

data class ModeloResumenMercado(
    @SerializedName("agresivo")
    val agresivo: ModeloResumen = ModeloResumen(),

    @SerializedName("moderado")
    val moderado: ModeloResumen = ModeloResumen(),

    @SerializedName("conservador")
    val conservador: ModeloResumen = ModeloResumen()
)
