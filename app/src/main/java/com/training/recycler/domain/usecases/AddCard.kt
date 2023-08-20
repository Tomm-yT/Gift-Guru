package com.training.recycler.domain.usecases

import androidx.lifecycle.viewModelScope
import com.training.recycler.domain.repositories.CardRepository
import com.training.recycler.domain.entities.CardItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AddCard(
    private val cardRepository: CardRepository
) {
    suspend fun execute(card: CardItem) {
        cardRepository.addCard(card)
    }
}