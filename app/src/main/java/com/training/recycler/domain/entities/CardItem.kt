package com.training.recycler.domain.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class CardItem(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val text: String,
    val side: String,
    val imageUrl: String
)