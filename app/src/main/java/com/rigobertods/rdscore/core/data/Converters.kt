package com.rigobertods.rdscore.core.data

import androidx.room.TypeConverter
import com.google.gson.GsonBuilder
import com.rigobertods.rdscore.core.network.adapters.PrediccionAdapter
import com.rigobertods.rdscore.features.partidos.data.Equipo
import com.rigobertods.rdscore.features.partidos.data.TipoPrediccion

class Converters {
    private val gson = GsonBuilder()
        .registerTypeAdapter(TipoPrediccion::class.java, PrediccionAdapter())
        .create()

    @TypeConverter
    fun fromEquipo(equipo: Equipo): String {
        return gson.toJson(equipo)
    }

    @TypeConverter
    fun toEquipo(json: String): Equipo {
        return gson.fromJson(json, Equipo::class.java)
    }

    @TypeConverter
    fun fromTipoPrediccion(prediccion: TipoPrediccion): String {
        val json = gson.toJson(prediccion)
        return json
    }

    @TypeConverter
    fun toTipoPrediccion(json: String): TipoPrediccion {
        if (json.isBlank()) {
            return TipoPrediccion.NoDisponible
        }
        val result = gson.fromJson(json, TipoPrediccion::class.java)
        return result
    }
}
