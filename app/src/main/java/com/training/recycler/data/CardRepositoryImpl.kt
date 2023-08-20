package com.training.recycler.data.repositories

import androidx.lifecycle.viewModelScope
import com.training.recycler.data.CardDao
import com.training.recycler.domain.entities.CardItem
import com.training.recycler.domain.repositories.CardRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class CardRepositoryImpl @Inject constructor(private val cardDao: CardDao) : CardRepository {

    override suspend fun addCard(card: CardItem) {
        cardDao.insert(card)
    }

    override suspend fun getAllRightCards(): List<CardItem> {
        return cardDao.getAllCards().filter { it.side == "R" }
    }

    override suspend fun getAllLeftCards(): List<CardItem> {
        return cardDao.getAllCards().filter { it.side == "L" }
    }

    override suspend fun clearAllCards() {
        cardDao.deleteAllCards()
    }
}
