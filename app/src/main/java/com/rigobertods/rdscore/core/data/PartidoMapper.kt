package com.rigobertods.rdscore.core.data

import com.rigobertods.rdscore.core.data.entities.PartidoEntity
import com.rigobertods.rdscore.features.partidos.data.Partido

fun mapPartidoToEntity(domain: Partido, cacheDate: String): PartidoEntity {
    return PartidoEntity(
        idPartido = domain.idPartido,
        estado = domain.estado,
        idLiga = domain.idLiga,
        temporada = domain.temporada,
        jornada = domain.jornada,
        nombreEquipoLocal = domain.nombreEquipoLocal,
        nombreEquipoVisitante = domain.nombreEquipoVisitante,
        estadisticasLocal = domain.estadisticasLocal,
        estadisticasVisitante = domain.estadisticasVisitante,
        fecha = domain.fecha,
        hora = domain.hora,
        ciudad = domain.ciudad,
        estadio = domain.estadio,
        arbitro = domain.arbitro,
        cuotaLocal = domain.cuotaLocal,
        cuotaEmpate = domain.cuotaEmpate,
        cuotaVisitante = domain.cuotaVisitante,
        cuotaOver = domain.cuotaOver,
        cuotaUnder = domain.cuotaUnder,
        cuotaBtts = domain.cuotaBtts,
        cuotaBttsNo = domain.cuotaBttsNo,
        golesLocal = domain.golesLocal,
        golesVisitante = domain.golesVisitante,
        resultado = domain.resultado,
        ambosMarcan = domain.ambosMarcan,
        localMarca = domain.localMarca,
        visitanteMarca = domain.visitanteMarca,
        mas25 = domain.mas25,
        prediccion = domain.prediccion,
        cacheDate = cacheDate
    )
}

fun mapEntityToDomain(entity: PartidoEntity): Partido {
    return Partido(
        idPartido = entity.idPartido,
        estado = entity.estado,
        idLiga = entity.idLiga,
        temporada = entity.temporada,
        jornada = entity.jornada,
        nombreEquipoLocal = entity.nombreEquipoLocal,
        nombreEquipoVisitante = entity.nombreEquipoVisitante,
        estadisticasLocal = entity.estadisticasLocal,
        estadisticasVisitante = entity.estadisticasVisitante,
        fecha = entity.fecha,
        hora = entity.hora,
        ciudad = entity.ciudad,
        estadio = entity.estadio,
        arbitro = entity.arbitro,
        cuotaLocal = entity.cuotaLocal,
        cuotaEmpate = entity.cuotaEmpate,
        cuotaVisitante = entity.cuotaVisitante,
        cuotaOver = entity.cuotaOver,
        cuotaUnder = entity.cuotaUnder,
        cuotaBtts = entity.cuotaBtts,
        cuotaBttsNo = entity.cuotaBttsNo,
        golesLocal = entity.golesLocal,
        golesVisitante = entity.golesVisitante,
        resultado = entity.resultado,
        ambosMarcan = entity.ambosMarcan,
        localMarca = entity.localMarca,
        visitanteMarca = entity.visitanteMarca,
        mas25 = entity.mas25,
        prediccion = entity.prediccion
    )
}
