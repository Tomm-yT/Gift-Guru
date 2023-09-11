package com.training.recycler.domain.repositories

import com.training.recycler.data.ProductResponse
import com.training.recycler.domain.entities.CardItem

interface CardRepository {
    suspend fun addCard(card: CardItem)
    suspend fun getAllCards(): List<CardItem>
    suspend fun clearAllCards()
    suspend fun fetchProducts(): List<ProductResponse>
    suspend fun fetchJewelery(): List<ProductResponse>
    suspend fun fetchElectronics(): List<ProductResponse>
    suspend fun fetchWomensCloths(): List<ProductResponse>
    suspend fun fetchMensCloths(): List<ProductResponse>


}
