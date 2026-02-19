package com.rigobertods.rdscore.ui.navigation

sealed class NavRoutes(val route: String) {
    data object Main : NavRoutes("main")
    data object Perfil : NavRoutes("perfil")
    data object DetallePartido : NavRoutes("detalle_partido/{partidoId}") {
        fun createRoute(partidoId: Int) = "detalle_partido/$partidoId"
    }
    data object DetalleEquipo : NavRoutes("detalle_equipo/{equipoId}/{ligaId}") {
        fun createRoute(equipoId: Int, ligaId: Int) = "detalle_equipo/$equipoId/$ligaId"
    }
    data object EditarPerfil : NavRoutes("editar_perfil/{username}/{email}") {
        fun createRoute(username: String, email: String) = "editar_perfil/$username/$email"
    }
    data object EditarPassword : NavRoutes("editar_password")
    data object Resumen : NavRoutes("resumen")
    data object CuotasCalientes : NavRoutes("cuotas_calientes")
    
    // Auth Routes
    data object Login : NavRoutes("login")
    data object Registro : NavRoutes("registro")
    data object RecuperarCuenta : NavRoutes("recuperar_cuenta")
    data object RestablecerPassword : NavRoutes("restablecer_password/{email}") {
        fun createRoute(email: String) = "restablecer_password/$email"
    }
}
