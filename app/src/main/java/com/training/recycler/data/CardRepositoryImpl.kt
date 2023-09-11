package com.training.recycler.data.repositories

import androidx.lifecycle.viewModelScope
import com.training.recycler.data.CardDao
import com.training.recycler.data.ProductResponse
import com.training.recycler.data.network.ProductService
import com.training.recycler.domain.entities.CardItem
import com.training.recycler.domain.repositories.CardRepository
import com.training.recycler.domain.usecases.GetProducts
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class CardRepositoryImpl @Inject constructor(private val cardDao: CardDao, private val service: ProductService) : CardRepository {



    override suspend fun addCard(card: CardItem) {
        cardDao.insert(card)
    }


    override suspend fun getAllCards(): List<CardItem> {
        return cardDao.getAllCards()
    }

    override suspend fun clearAllCards() {
        cardDao.deleteAllCards()
    }

    override suspend fun fetchProducts(): List<ProductResponse> {
        return service.getAllProducts()
    }

    override suspend fun fetchJewelery(): List<ProductResponse> {
        return service.getJewelery()
    }

    override suspend fun fetchElectronics(): List<ProductResponse> {
        return service.getElectronics()
    }

    override suspend fun fetchWomensCloths(): List<ProductResponse> {
        return service.getWomensClothing()
    }

    override suspend fun fetchMensCloths(): List<ProductResponse> {
        return service.getMensClothing()
    }
}

