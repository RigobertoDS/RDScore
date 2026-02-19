package com.rigobertods.rdscore.features.partidos.data

import com.google.gson.annotations.SerializedName

data class Resumen(
    @SerializedName("modelo_arriesgado")
    val modeloArriesgado: ModeloResumen = ModeloResumen(),

    @SerializedName("modelo_conservador")
    val modeloConservador: ModeloResumen = ModeloResumen(),

    @SerializedName("modelo_moderado")
    val modeloModerado: ModeloResumen = ModeloResumen(),

    @SerializedName("modelo_global")
    val modeloGlobal: ModeloResumen = ModeloResumen(),

    @SerializedName("partidos_totales")
    val partidosTotales: Int = 0
)

data class ModeloResumen(
    val aciertos: Double = 0.0,
    @SerializedName("aciertos_brutos")
    val aciertosBrutos: Int = 0,
    val apuestas: Int = 0,
    val beneficio: Double = 0.0,
    val inversion: Double = 0.0,
    val roi: Double = 0.0
)
