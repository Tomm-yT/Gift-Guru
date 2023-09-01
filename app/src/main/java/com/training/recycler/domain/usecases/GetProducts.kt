package com.training.recycler.domain.usecases

import com.training.recycler.data.ProductResponse
import com.training.recycler.domain.repositories.CardRepository

// Define a Use Case in the Domain layer
class GetProducts(private val productRepository: CardRepository) {
    suspend fun execute(): List<ProductResponse> {
        return productRepository.fetchProducts()
    }
}
