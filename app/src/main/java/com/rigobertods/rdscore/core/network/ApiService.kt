package com.rigobertods.rdscore.core.network

import com.rigobertods.rdscore.features.cuotascalientes.data.CuotaCalienteResponse
import com.rigobertods.rdscore.features.equipo.data.DatosEquipoResponse
import com.rigobertods.rdscore.features.perfil.data.DeleteAccountResponse
import com.rigobertods.rdscore.features.perfil.data.EditPasswordRequest
import com.rigobertods.rdscore.features.perfil.data.EditPasswordResponse
import com.rigobertods.rdscore.features.perfil.data.EditProfileRequest
import com.rigobertods.rdscore.features.perfil.data.EditProfileResponse
import com.rigobertods.rdscore.features.auth.data.ForgotPasswordRequest
import com.rigobertods.rdscore.features.auth.data.ForgotPasswordResponse
import com.rigobertods.rdscore.features.partidos.data.GamesByDateResponse
import com.rigobertods.rdscore.features.perfil.data.GetProfileResponse
import com.rigobertods.rdscore.features.auth.data.LoginRequest
import com.rigobertods.rdscore.features.auth.data.LoginResponse
import com.rigobertods.rdscore.features.auth.data.LogoutResponse
import com.rigobertods.rdscore.features.resumen.data.PrecisionResponse
import com.rigobertods.rdscore.features.resumen.data.PrecisionMercadoResponse
import com.rigobertods.rdscore.features.auth.data.RefreshTokenResponse
import com.rigobertods.rdscore.features.auth.data.RegisterRequest
import com.rigobertods.rdscore.features.auth.data.RegisterResponse
import com.rigobertods.rdscore.features.auth.data.ResetPasswordRequest
import com.rigobertods.rdscore.features.auth.data.ResetPasswordResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    // Define el endpoint para el registro
    @POST("register")
    suspend fun register(
        @Body request: RegisterRequest
    ): Response<RegisterResponse>

    // Define el endpoint para el login
    @POST("login")
    suspend fun login(
        @Body request: LoginRequest
    ): Response<LoginResponse>

    // Define el endpoint para cerrar sesión
    @POST("logout")
    suspend fun logout(
    ): Response<LogoutResponse>


    // Define el endpoint para recuperar la cuenta
    @POST("forgot-password")
    suspend fun forgotPassword(
        @Body request: ForgotPasswordRequest
    ): Response<ForgotPasswordResponse>

    // Define el endpoint para restablecer la contraseña
    @POST("reset-password")
    suspend fun resetPassword(
        @Body request: ResetPasswordRequest
    ): Response<ResetPasswordResponse>

    // Define el endpoint para refrescar el token
    @POST("refresh")
    suspend fun refreshToken(
    ): Response<RefreshTokenResponse>

    // Define el endpoint para obtener los partidos por fecha (V1 SQL backed)
    @GET("api/v1/partidos")
    suspend fun getGamesByDate(
        @Query("fecha") request: String
    ): Response<GamesByDateResponse>

    // Define el endpoint para obtener el perfil
    @GET("profile")
    suspend fun getProfile(
    ): Response<GetProfileResponse>

    // Define el endpoint para editar el perfil
    @PUT("modificar-datos")
    suspend fun editProfile(
        @Body request: EditProfileRequest
    ): Response<EditProfileResponse>

    // Define el endpoint para editar la contraseña
    @PUT("cambiar-password")
    suspend fun editPassword(
        @Body request: EditPasswordRequest
    ): Response<EditPasswordResponse>

    // Define el endpoint para eliminar la cuenta
    @DELETE("eliminar-cuenta")
    suspend fun deleteAccount(
    ): Response<DeleteAccountResponse>

    // Define el endpoint para obtener la precisión (V1)
    @GET("api/v1/precision")
    suspend fun getPrecision(
    ): Response<PrecisionResponse>

    // Define el endpoint para obtener la precisión por mercado (V1)
    @GET("api/v1/precision-apuesta")
    suspend fun getPrecisionPorMercado(
    ): Response<PrecisionMercadoResponse>

    // Define el endpoint para obtener las cuotas calientes (V1)
    @GET("api/v1/cuotas-calientes")
    suspend fun getCuotasCalientes(
    ): Response<CuotaCalienteResponse>

    // Define el endpoint para obtener los datos de un equipo (V1)
    @GET("api/v1/datos-equipo/{id_equipo}")
    suspend fun getDatosEquipo(
        @Path("id_equipo") request: Int
    ): Response<DatosEquipoResponse>

    // Define el endpoint para obtener la lista de ligas (V1)
    @GET("api/v1/ligas")
    suspend fun getLigas(): Response<com.rigobertods.rdscore.features.ligas.data.LigaResponse>

}
