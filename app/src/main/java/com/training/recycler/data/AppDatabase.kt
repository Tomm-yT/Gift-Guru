package com.training.recycler.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.training.recycler.domain.entities.CardItem
import com.training.recycler.domain.entities.UserProfile

@Database(entities = [CardItem::class, UserProfile::class], version = 9)
abstract class AppDatabase : RoomDatabase() {
    abstract fun cardDao(): CardDao
    abstract fun userProfileDao(): UserProfileDao
}