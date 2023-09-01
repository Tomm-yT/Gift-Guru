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

    override suspend fun getAllRightCards(): List<CardItem> {
        return cardDao.getAllCards().filter { it.side == "R" }
    }

    override suspend fun getAllLeftCards(): List<CardItem> {
        return cardDao.getAllCards().filter { it.side == "L" }
    }

    override suspend fun clearAllCards() {
        cardDao.deleteAllCards()
    }

    override suspend fun fetchProducts(): List<ProductResponse> {
            val response = service.getProducts()
            //emit(UIState.Loading(true))
//            if(response.isSuccessful){
//                response.body()?.let{
//                    emit(UIState.Loading(false))
//                    delay(500)
//                    emit(UIState.Success(it))
//                } ?: emit(UIState.Failure(response.message()))
//            }else{
//                emit(UIState.Loading(false))
//                emit(UIState.Failure(response.message()))
//            }

        return service.getProducts()
    }
}

