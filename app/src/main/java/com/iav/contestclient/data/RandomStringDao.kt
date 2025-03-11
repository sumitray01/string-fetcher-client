package com.iav.contestclient.data

import androidx.room.*
import com.iav.contestclient.data.model.RandomStringEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface RandomStringDao {

    @Query("SELECT * FROM random_strings ORDER BY id DESC")
    fun getAllRandomStrings(): Flow<List<RandomStringEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRandomString(randomString: RandomStringEntity)

    @Delete
    suspend fun deleteRandomString(randomString: RandomStringEntity)

    @Query("DELETE FROM random_strings")
    suspend fun deleteAllRandomStrings()
}
