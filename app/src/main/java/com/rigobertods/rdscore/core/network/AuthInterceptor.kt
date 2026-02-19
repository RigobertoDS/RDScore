package com.rigobertods.rdscore.core.network

import com.rigobertods.rdscore.data.SessionManager
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class AuthInterceptor @Inject constructor(private val sessionManager: SessionManager) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()

        // Determinamos qué token usar
        val token = if (originalRequest.url.encodedPath.endsWith("/refresh")) {
            sessionManager.getRefreshToken()
        } else {
            sessionManager.getAccessToken()
        }

        // Si no hay token, simplemente continuamos con la petición original
        if (token == null) {
            return chain.proceed(originalRequest)
        }

        // Añadimos la cabecera Authorization con el token correspondiente
        val newRequest = originalRequest.newBuilder()
            .header("Authorization", "Bearer $token")
            .build()

        return chain.proceed(newRequest)
    }
}
