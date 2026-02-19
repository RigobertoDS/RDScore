package com.rigobertods.rdscore.core.common

import com.rigobertods.rdscore.R
import com.rigobertods.rdscore.features.partidos.data.Equipo
import com.rigobertods.rdscore.features.partidos.data.StatItem
import com.rigobertods.rdscore.features.partidos.data.StatItemEquipo
import java.util.Locale

fun mapEquipoToStatItemsEquipo(equipo: Equipo): List<StatItemEquipo> {
    return listOf(
        // -- Récord General --
        StatItemEquipo(R.string.stat_total_matches, equipo.partidosTotales.toString()),
        StatItemEquipo(
            R.string.stat_record_total,
            "${equipo.victoriasTotales}-${equipo.empatesTotales}-${equipo.derrotasTotales}"
        ),

        // -- Récord Casa --
        StatItemEquipo(R.string.stat_home_matches, equipo.partidosCasa.toString()),
        StatItemEquipo(
            R.string.stat_record_home,
            "${equipo.victoriasCasa}-${equipo.empatesCasa}-${equipo.derrotasCasa}"
        ),

        // -- Récord Fuera --
        StatItemEquipo(R.string.stat_away_matches, equipo.partidosFuera.toString()),
        StatItemEquipo(
            R.string.stat_record_away,
            "${equipo.victoriasFuera}-${equipo.empatesFuera}-${equipo.derrotasFuera}"
        ),

        // -- Goles  General --
        StatItemEquipo(R.string.stat_goals_for, equipo.golesFavor.toString()),
        StatItemEquipo(R.string.stat_goals_against, equipo.golesContra.toString()),
        StatItemEquipo(R.string.stat_goal_diff, equipo.diferenciaGoles.toString()),
        StatItemEquipo(
            R.string.stat_goals_for_per_match,
            String.format(Locale.getDefault(), "%.2f", equipo.golesFavorPorPartido)
        ),
        StatItemEquipo(
            R.string.stat_goals_against_per_match,
            String.format(Locale.getDefault(), "%.2f", equipo.golesContraPorPartido)
        ),

        // -- Goles  Casa --
        StatItemEquipo(R.string.stat_goals_for_home, equipo.golesFavorCasa.toString()),
        StatItemEquipo(R.string.stat_goals_against_home, equipo.golesContraCasa.toString()),
        StatItemEquipo(R.string.stat_goal_diff_home, equipo.diferenciaGolesCasa.toString()),
        StatItemEquipo(
            R.string.stat_goals_for_per_match_home,
            String.format(Locale.getDefault(), "%.2f", equipo.golesFavorCasaPorPartido)
        ),
        StatItemEquipo(
            R.string.stat_goals_against_per_match_home,
            String.format(Locale.getDefault(), "%.2f", equipo.golesContraCasaPorPartido)
        ),

        // -- Goles Fuera --
        StatItemEquipo(R.string.stat_goals_for_away, equipo.golesFavorFuera.toString()),
        StatItemEquipo(R.string.stat_goals_against_away, equipo.golesContraFuera.toString()),
        StatItemEquipo(R.string.stat_goal_diff_away, equipo.diferenciaGolesFuera.toString()),
        StatItemEquipo(
            R.string.stat_goals_for_per_match_away,
            String.format(Locale.getDefault(), "%.2f", equipo.golesFavorFueraPorPartido)
        ),
        StatItemEquipo(
            R.string.stat_goals_against_per_match_away,
            String.format(Locale.getDefault(), "%.2f", equipo.golesContraFueraPorPartido)
        )
    )
}

fun mapEquiposToStatItems(local: Equipo, visitante: Equipo): List<StatItem> {
    return listOf(
        // -- General --
        StatItem(R.string.stat_position, local.posicion.toString(), visitante.posicion.toString()),
        StatItem(R.string.stat_points, local.puntos.toString(), visitante.puntos.toString()),
        StatItem(
            R.string.stat_form,
            transformarForma(local.ultimosPartidos),
            transformarForma(visitante.ultimosPartidos)
        ),

        // -- Récord General --
        StatItem(R.string.stat_total_matches, local.partidosTotales.toString(), visitante.partidosTotales.toString()),
        StatItem(
            R.string.stat_record_total,
            "${local.victoriasTotales}-${local.empatesTotales}-${local.derrotasTotales}",
            "${visitante.victoriasTotales}-${visitante.empatesTotales}-${visitante.derrotasTotales}"
        ),

        // -- Récord Casa/Fuera --
        StatItem(R.string.stat_home_away_matches, local.partidosCasa.toString(), visitante.partidosFuera.toString()),
        StatItem(
            R.string.stat_record_home_away,
            "${local.victoriasCasa}-${local.empatesCasa}-${local.derrotasCasa}",
            "${visitante.victoriasFuera}-${visitante.empatesFuera}-${visitante.derrotasFuera}"
        ),

        // -- Goles  General --
        StatItem(R.string.stat_goals_for, local.golesFavor.toString(), visitante.golesFavor.toString()),
        StatItem(R.string.stat_goals_against, local.golesContra.toString(), visitante.golesContra.toString()),
        StatItem(R.string.stat_goal_diff, local.diferenciaGoles.toString(), visitante.diferenciaGoles.toString()),
        StatItem(
            R.string.stat_goals_for_per_match,
            String.format(Locale.getDefault(), "%.2f", local.golesFavorPorPartido),
            String.format(Locale.getDefault(), "%.2f", visitante.golesFavorPorPartido)
        ),
        StatItem(
            R.string.stat_goals_against_per_match,
            String.format(Locale.getDefault(), "%.2f", local.golesContraPorPartido),
            String.format(Locale.getDefault(), "%.2f", visitante.golesContraPorPartido)
        ),

        // -- Goles  Casa/Fuera --
        StatItem(R.string.stat_goals_for_home_away, local.golesFavorCasa.toString(), visitante.golesFavorFuera.toString()),
        StatItem(R.string.stat_goals_against_home_away, local.golesContraCasa.toString(), visitante.golesContraFuera.toString()),
        StatItem(R.string.stat_goal_diff_home_away, local.diferenciaGolesCasa.toString(), visitante.diferenciaGolesFuera.toString()),
        StatItem(
            R.string.stat_goals_for_per_match_home_away,
            String.format(Locale.getDefault(), "%.2f", local.golesFavorCasaPorPartido),
            String.format(Locale.getDefault(), "%.2f", visitante.golesFavorFueraPorPartido)
        ),
        StatItem(
            R.string.stat_goals_against_per_match_home_away,
            String.format(Locale.getDefault(), "%.2f", local.golesContraCasaPorPartido),
            String.format(Locale.getDefault(), "%.2f", visitante.golesContraFueraPorPartido)
        )
    )
}

fun transformarForma(forma: String): String {
    return forma.map { char ->
        when (char) {
            'W' -> 'V'
            'D' -> 'E'
            'L' -> 'D'
            else -> char
        }
    }.joinToString("")
}
