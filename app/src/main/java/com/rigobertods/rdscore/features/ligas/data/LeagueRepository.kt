package com.rigobertods.rdscore.features.ligas.data

import android.util.Log
import com.rigobertods.rdscore.core.network.ApiService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LeagueRepository @Inject constructor(
    private val apiService: ApiService
) {
    private val _leagues = MutableStateFlow<Map<Int, Liga>>(emptyMap())
    val leagues: StateFlow<Map<Int, Liga>> = _leagues.asStateFlow()

    init {
        fetchLeagues()
    }

    private fun fetchLeagues() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = apiService.getLigas()
                if (response.isSuccessful) {
                    val ligaList = response.body()?.ligas ?: emptyList()
                    _leagues.value = ligaList.associateBy { it.id }
                    Log.d("LeagueRepository", "Loaded ${ligaList.size} leagues")
                } else {
                    Log.e("LeagueRepository", "Error fetching leagues: ${response.code()}")
                }
            } catch (e: Exception) {
                Log.e("LeagueRepository", "Exception fetching leagues", e)
            }
        }
    }


}
