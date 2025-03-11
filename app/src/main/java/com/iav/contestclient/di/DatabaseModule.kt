package com.iav.contestclient.di

import android.content.Context
import androidx.room.Room
import com.iav.contestclient.data.AppDatabase
import com.iav.contestclient.data.RandomStringDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext appContext: Context): AppDatabase {
        return Room.databaseBuilder(
            appContext,
            AppDatabase::class.java,
            "random_strings_db"
        ).build()
    }

    @Provides
    fun provideRandomStringDao(db: AppDatabase): RandomStringDao {
        return db.randomStringDao()
    }
}
