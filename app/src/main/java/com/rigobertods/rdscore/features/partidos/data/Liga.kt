package com.rigobertods.rdscore.features.partidos.data

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Liga(
    @SerializedName("id")
    val id: Int = 0,

    @SerializedName("nombre")
    val nombre: String = "",

    @SerializedName("pais")
    val pais: String = "",

    @SerializedName("bandera")
    val bandera: String = "",

    @SerializedName("logo")
    val logo: String = ""
) : Parcelable
