package com.rigobertods.rdscore.core.network

import com.rigobertods.rdscore.data.SessionManager
import dagger.Lazy
import kotlinx.coroutines.runBlocking
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route
import javax.inject.Inject

class AuthAuthenticator @Inject constructor(
    private val sessionManager: SessionManager,
    private val apiService: Lazy<ApiService>
) : Authenticator {

    override fun authenticate(route: Route?, response: Response): Request? {
        // Si la petición original que falló fue a 'login', 'register', o endpoints de cambio de password,
        // no intentes refrescar el token. Simplemente cancela.
        val originalPath = response.request.url.encodedPath
        if (originalPath.endsWith("/login") || 
            originalPath.endsWith("/register") ||
            originalPath.endsWith("/refresh") ||
            originalPath.endsWith("/cambiar-password") ||
            originalPath.endsWith("/reset-password") ||
            originalPath.endsWith("/forgot-password")) {
            return null // Devuelve null para no reintentar y dejar que la Activity maneje el error 401.
        }

        // Optimizacion: Si ya estamos en proceso de logout, no hacemos nada
        if (sessionManager.isLoggingOutState()) {
            return null
        }

        // sessionManager is now injected
        val refreshToken = sessionManager.getRefreshToken()

        // Si no tenemos token de refresco, no podemos hacer nada, cerramos sesión.
        if (refreshToken == null) {
            sessionManager.logout()
            return null
        }

        // Bloqueamos la ejecución para hacer la llamada de refresco de forma síncrona.
        // runBlocking es adecuado aquí porque authenticate se ejecuta en un hilo de fondo de OkHttp.
        return runBlocking {
            // Hacemos la llamada a la API para refrescar el token
            val refreshResponse = apiService.get().refreshToken()

            if (refreshResponse.isSuccessful && refreshResponse.body() != null) {
                // Si el refresco fue exitoso, guardamos el nuevo token
                val newAccessToken = refreshResponse.body()!!.accessToken
                sessionManager.saveAccessToken(newAccessToken)

                // Reintentamos la petición original que falló con el nuevo token
                response.request.newBuilder()
                    .header("Authorization", "Bearer $newAccessToken")
                    .build()
            } else {
                // Si el refresco falla (ej: refresh token caducado), cerramos sesión.
                
                // Usamos logout con mensaje para que la UI se encargue de mostrar el error
                // y evitamos duplicidades gracias al control interno de SessionManager
                sessionManager.logout()
                null // Devolvemos null para cancelar la cadena de peticiones.
            }
        }
    }
}
