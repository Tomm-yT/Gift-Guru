package com.training.recycler.presentation

import android.annotation.SuppressLint
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.training.recycler.data.CardDao
import com.training.recycler.data.ProductResponse
import com.training.recycler.domain.entities.CardItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject
import com.training.recycler.domain.repositories.CardRepository
import com.training.recycler.domain.repositories.ProductsRepository
import com.training.recycler.domain.usecases.GetProducts
import kotlinx.coroutines.CoroutineScope


@SuppressLint("SuspiciousIndentation")
@HiltViewModel
class CardViewModel @Inject constructor(private val repository: CardRepository) : ViewModel() {

    var selectedProfileVM = ""

    private val currentCardItemsLeft = MutableLiveData<MutableList<CardItem>?>(mutableListOf())

    suspend fun loadProductsToRepo(){
        Log.d("", "Fetching now!!!!")
        ProductsRepository.allProducts = repository.fetchProducts()
        ProductsRepository.mensClothingProducts = repository.fetchMensCloths()
        ProductsRepository.electronicsProducts = repository.fetchElectronics()
        ProductsRepository.womensClothingProducts = repository.fetchWomensCloths()
        ProductsRepository.jeweleryProducts = repository.fetchJewelery()
        Log.d("", "Fetching Complete!")
    }

    suspend fun loadSaved(): List<CardItem> {
        return repository.getAllCards()
    }

    fun setSelectedProfile(username: String) {
        selectedProfileVM = username
    }

    fun deleteCard(card: CardItem) {
        viewModelScope.launch(Dispatchers.IO) {
            // Assuming USERNAME is a constant available in this context
            Log.d("VIEWMODEL", "View Model is deleting with $selectedProfileVM")
            repository.deleteCardByUsernameAndTitle(selectedProfileVM, card.title)
        }
    }

    fun deleteCardByName(username: String, card: CardItem) {
        viewModelScope.launch(Dispatchers.IO) {
            // Assuming USERNAME is a constant available in this context
            Log.d("VIEWMODEL", "View Model is deleting with $selectedProfileVM")
            repository.deleteCardByUsernameAndTitle(username, card.title)
        }
    }

    //Adds a card
    fun saveCard(card: CardItem) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.addCard(card)
            val updatedList = currentCardItemsLeft.value?.toMutableList()
            updatedList?.add(card)
            withContext(Dispatchers.Main) {
                currentCardItemsLeft.postValue(updatedList)
            }
        }
    }

    //Deletes all cards
    fun clearAllCards() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.clearAllCards()
        }
    }

    fun fetchProducts(): List<ProductResponse> {
        return ProductsRepository.allProducts
    }

    fun fetchJeweleryProducts(): List<ProductResponse> {
        return ProductsRepository.jeweleryProducts
    }

    fun fetchElectronics(): List<ProductResponse> {
        return ProductsRepository.electronicsProducts
    }

    fun fetchWomensCloths(): List<ProductResponse> {
        return ProductsRepository.womensClothingProducts
    }

    fun fetchMensCloths(): List<ProductResponse> {
        return ProductsRepository.mensClothingProducts
    }
}