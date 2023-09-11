package com.training.recycler

import android.app.Application
import android.content.Context
import androidx.room.Room
import com.training.recycler.data.AppDatabase
import com.training.recycler.data.CardDao
import com.training.recycler.data.UserProfileDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.HiltAndroidApp
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@HiltAndroidApp
class DatabaseInitializer : Application() {

    @Module
    @InstallIn(SingletonComponent::class)
    object DatabaseModule {

        @Provides
        @Singleton
        fun provideDatabase(@ApplicationContext context: Context): AppDatabase {
            return Room.databaseBuilder(
                context.applicationContext,
                AppDatabase::class.java,
                "app_database"
            ).build()
        }

        @Provides
        fun provideCardDao(database: AppDatabase): CardDao {
            return database.cardDao()
        }
    }
}