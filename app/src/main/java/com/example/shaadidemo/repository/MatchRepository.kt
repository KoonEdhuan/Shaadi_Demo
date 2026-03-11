package com.example.shaadidemo.repository

import androidx.lifecycle.LiveData
import com.example.shaadidemo.data.DAO.MatchDao
import com.example.shaadidemo.data.entity.ProfilesEntity
import kotlin.collections.map
import kotlin.math.abs

class MatchRepository(private val matchDao: MatchDao) {

    val allProfiles: LiveData<List<ProfilesEntity>> = matchDao.getAllProfiles()

//    suspend fun syncRemoteUsers(remoteUsers: List<Result>) {
//        val entities = remoteUsers.map { remote ->
//            // Map raw API data to our flat local Entity
//            ProfilesEntity(
//                uuid = remote.login.uuid,
//                firstName = remote.name.first,
//                lastName = remote.name.last,
//                age = remote.dob.age,
//                city = remote.location.city,
//                country = remote.location.country,
//                imageUrl = remote.picture.large,
//
//                // 1. Requirement: Additional Matrimonial Fields
//                education = listOf("MBA", "B.Tech", "MS", "PhD", "MBBS").random(),
//                religion = listOf("Hindu", "Christian", "Sikh", "Muslim", "Other").random(),
//
//                // 2. Requirement: Local Match Score Algorithm
//                matchScore = calculateMatchScore(remote.dob.age, remote.location.city),
//
//                // Default status
//                matchStatus = "PENDING"
//            )
//        }
//
//        matchDao.insertProfiles(entities)
//    }

    suspend fun updateStatus(uuid: String, status: String) {
        matchDao.updateMatchStatus(uuid, status)
    }

    private fun calculateMatchScore(age: Int, city: String): Int {
        val myAge = 28
        val myCity = "Mumbai"
        val ageDiff = abs(age - myAge)
        var score = 80 - (ageDiff * 2)

        if (city.equals(myCity, ignoreCase = true)) {
            score += 20
        }

        return score.coerceIn(0, 100)
    }
}