package com.example.shaadidemo.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.shaadidemo.data.DAO.MatchDao
import com.example.shaadidemo.data.entity.ProfilesEntity

@Database(entities = [ProfilesEntity::class], version = 1, exportSchema = false)
abstract class AppDataBase: RoomDatabase() {

    abstract fun matchDao(): MatchDao

    companion object {
        @Volatile
        private var INSTANCE: AppDataBase? = null

        fun getDatabase(context: Context): AppDataBase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDataBase::class.java,
                    "shaadi_db"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}