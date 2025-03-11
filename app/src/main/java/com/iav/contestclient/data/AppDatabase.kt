package com.iav.contestclient.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.iav.contestclient.data.model.RandomStringEntity

@Database(entities = [RandomStringEntity::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun randomStringDao(): RandomStringDao
}
