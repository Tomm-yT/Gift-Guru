package com.training.recycler.presentation

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.training.recycler.data.CardDao
import com.training.recycler.domain.entities.CardItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject
import com.training.recycler.domain.repositories.CardRepository



@HiltViewModel
class CardViewModel @Inject constructor(private val repository: CardRepository) : ViewModel() {

    private val currentCardItemsRight = MutableLiveData<MutableList<CardItem>?>(mutableListOf(
        CardItem(text = "Default Right Card", side = "R")
    ))
    private val currentCardItemsLeft = MutableLiveData<MutableList<CardItem>?>(mutableListOf(
        CardItem(text = "Default Left Card", side = "L")
    ))

    //Adds a Right
    fun addCardRight(card: CardItem) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.addCard(card)
            val updatedList = currentCardItemsRight.value?.toMutableList()
            updatedList?.add(card)
            withContext(Dispatchers.Main) {
                currentCardItemsRight.postValue(updatedList)
            }
        }
    }

    //Adds a Left card
    fun addCardLeft(card: CardItem) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.addCard(card)
            val updatedList = currentCardItemsLeft.value?.toMutableList()
            updatedList?.add(card)
            withContext(Dispatchers.Main) {
                currentCardItemsLeft.postValue(updatedList)
            }
        }
    }

    //Gets all Right cards
    suspend fun getRightCards(): MutableList<CardItem> {
        return withContext(Dispatchers.IO) {
            val rightCards = repository.getAllRightCards()
            rightCards as MutableList<CardItem>
        }
    }

    //Gets all Left cards
    suspend fun getLeftCards(): MutableList<CardItem> {
        return withContext(Dispatchers.IO) {
            val leftCards = repository.getAllLeftCards()
            leftCards as MutableList<CardItem>
        }
    }

    //Deletes all cards
    fun clearAllCards() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.clearAllCards()
        }
    }

}