package com.example.shaadidemo.data.repository

import android.util.Log
import com.example.shaadidemo.data.DAO.MatchDao
import com.example.shaadidemo.data.entity.ProfilesEntity
import com.example.shaadidemo.data.mapper.mapToEntity
import com.example.shaadidemo.data.service.MatchService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class MatchRepository(
    private val service: MatchService,
    private val matchDao: MatchDao
) {

    val allProfiles: Flow<List<ProfilesEntity>> = matchDao.getAllProfiles()

    suspend fun fetchAndRefreshCache() {
        withContext(Dispatchers.IO) {
            try {
                val response = service.getMatches()
                if (response.isSuccessful) {
                    val remoteResults = response.body()?.results ?: emptyList()
                    val entities = remoteResults.map { mapToEntity(it) }
                    matchDao.insertProfiles(entities)
                }
            } catch (e: Exception) {
                Log.e("Network", "API failed: ${e.message}")
            }
        }
    }

    suspend fun updateStatus(uuid: String, status: String) {
        matchDao.updateMatchStatus(uuid, status)
    }

    suspend fun getFilteredProfiles(education: String?, religion: String?): Flow<List<ProfilesEntity>> {
        return matchDao.getFilteredProfiles(education, religion)
    }
}