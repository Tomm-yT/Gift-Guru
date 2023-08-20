package com.training.recycler.domain.repositories

import com.training.recycler.domain.entities.CardItem

interface CardRepository {
    suspend fun addCard(card: CardItem)
    suspend fun getAllRightCards(): List<CardItem>
    suspend fun getAllLeftCards(): List<CardItem>
    suspend fun clearAllCards()
}