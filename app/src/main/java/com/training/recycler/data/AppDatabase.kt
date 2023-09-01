package com.training.recycler.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.training.recycler.domain.entities.CardItem

@Database(entities = [CardItem::class], version = 5)
abstract class AppDatabase : RoomDatabase() {
    abstract fun cardDao(): CardDao
}