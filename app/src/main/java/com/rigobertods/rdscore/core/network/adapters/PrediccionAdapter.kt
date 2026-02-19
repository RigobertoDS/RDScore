package com.rigobertods.rdscore.core.network.adapters

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.JsonNull
import com.google.gson.JsonPrimitive
import com.google.gson.JsonSerializationContext
import com.google.gson.JsonSerializer
import com.rigobertods.rdscore.features.partidos.data.Prediccion
import com.rigobertods.rdscore.features.partidos.data.TipoPrediccion
import java.lang.reflect.Type

class PrediccionAdapter : JsonSerializer<TipoPrediccion>, JsonDeserializer<TipoPrediccion> {

    override fun serialize(
        src: TipoPrediccion?,
        typeOfSrc: Type?,
        context: JsonSerializationContext?
    ): JsonElement {
        if (src == null || context == null) {
            return JsonNull.INSTANCE
        }
        val result = when (src) {
            is TipoPrediccion.Detallada -> context.serialize(src.datos)
            is TipoPrediccion.Simple -> JsonPrimitive(src.mensaje)
            TipoPrediccion.NoDisponible -> JsonNull.INSTANCE
        }
        return result
    }

    override fun deserialize(
        json: JsonElement?,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): TipoPrediccion {
        if (json == null || json.isJsonNull) {
            return TipoPrediccion.NoDisponible
        }

        // Si es un primitivo (como un String), lo parseamos como una predicción simple.
        if (json.isJsonPrimitive) {
            val mensaje = json.asString
            return TipoPrediccion.Simple(mensaje)
        }

        // Si el elemento JSON es un objeto
        if (json.isJsonObject && context != null) {
            val jsonObject = json.asJsonObject
            
            // Caso 1: Viene de Room con formato {"mensaje":"..."}
            if (jsonObject.has("mensaje") && !jsonObject.has("datos")) {
                val mensaje = jsonObject.get("mensaje").asString
                return TipoPrediccion.Simple(mensaje)
            }
            
            // Caso 2: Viene de Room con formato {"datos":{...}} (formato wrapper)
            if (jsonObject.has("datos")) {
                return try {
                    val datosJson = jsonObject.get("datos")
                    val datosPrediccion = context.deserialize<Prediccion>(datosJson, Prediccion::class.java)
                    TipoPrediccion.Detallada(datosPrediccion)
                } catch (_: Exception) {
                    TipoPrediccion.NoDisponible
                }
            }
            
            // Caso 3: Formato directo del API {"btts":...,"goles_esperados":...,"over25":...,"resultado_1x2":...}
            return try {
                val datosPrediccion = context.deserialize<Prediccion>(json, Prediccion::class.java)
                TipoPrediccion.Detallada(datosPrediccion)
            } catch (_: Exception) {
                TipoPrediccion.NoDisponible
            }
        }

        // Como fallback
        return TipoPrediccion.NoDisponible
    }
}
