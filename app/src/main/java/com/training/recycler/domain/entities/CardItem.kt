package com.training.recycler.domain.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class CardItem(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val username: String,
    val title: String,
    val price: Double,
    val imageUrl: String
)