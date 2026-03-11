package com.example.shaadidemo.data.DAO

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.shaadidemo.data.entity.ProfilesEntity

@Dao
interface MatchDao {

    @Query("SELECT * FROM profiles")
    fun getAllProfiles(): LiveData<List<ProfilesEntity>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertProfiles(profiles: List<ProfilesEntity>)

    @Query("UPDATE profiles SET matchStatus = :status WHERE uuid = :uuid")
    suspend fun updateMatchStatus(uuid: String, status: String)
}