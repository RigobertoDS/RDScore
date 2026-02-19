# =================================================================================
# REGLAS DE PROGUARD/R8 PARA LA APP RDSCORE
# Estas reglas son VITALES para que el APK de producción funcione.
# =================================================================================

# --- Reglas para Gson ---
# Conserva los nombres de los campos de las clases que usan la anotación @SerializedName.
# Esto es CRUCIAL para que la conversión de JSON a objetos funcione.
-keepclassmembers,allowobfuscation class * {
    @com.google.gson.annotations.SerializedName <fields>;
}

# --- Reglas para tus Clases de Datos (Data Classes) ---
# Es la forma más segura de asegurar que R8 no elimina ni ofusca tus modelos.
# Esto le dice a R8: "No toques NADA dentro de cualquier clase que esté en los
# paquetes de 'data' y sus subpaquetes".

# Paquete legacy (por si queda algo)
-keep class com.rigobertods.rdscore.data.** { *; }

# Nuevos paquetes después de la modularización
-keep class com.rigobertods.rdscore.core.data.** { *; }
-keep class com.rigobertods.rdscore.core.network.** { *; }
-keep class com.rigobertods.rdscore.core.common.** { *; }
-keep class com.rigobertods.rdscore.features.auth.data.** { *; }
-keep class com.rigobertods.rdscore.features.partidos.data.** { *; }
-keep class com.rigobertods.rdscore.features.perfil.data.** { *; }
-keep class com.rigobertods.rdscore.features.historial.data.** { *; }
-keep class com.rigobertods.rdscore.features.cuotascalientes.data.** { *; }
-keep class com.rigobertods.rdscore.features.resumen.data.** { *; }
-keep class com.rigobertods.rdscore.features.equipo.data.** { *; }
-keep class com.rigobertods.rdscore.features.ligas.data.** { *; }

# --- Reglas para Retrofit y OkHttp ---
# Evita problemas con las interfaces y clases internas de estas librerías,
# ya que también usan reflexión.
-dontwarn retrofit2.**
-keep class retrofit2.** { *; }
-keep interface retrofit2.** { *; }
-keep @retrofit2.http.POST class * { <methods>; }
-keep @retrofit2.http.GET class * { <methods>; }
# Añade más si usas @PUT, @DELETE, etc.

-dontwarn okhttp3.**
-keep class okhttp3.** { *; }
-keep interface okhttp3.** { *; }

# --- Reglas para Coroutines ---
# Conserva metadatos necesarios para el funcionamiento interno de las corrutinas,
# especialmente cuando se usan con otras librerías.
-keepattributes Signature
-keepattributes *Annotation*
-keepattributes InnerClasses
-keep,allowobfuscation,allowshrinking class kotlinx.coroutines.flow.internal.SafeCollector

# --- Regla para Javax Inject ---
# Conserva las anotaciones de inyección de dependencias.
-keep @javax.inject.Inject class * { *; }

    