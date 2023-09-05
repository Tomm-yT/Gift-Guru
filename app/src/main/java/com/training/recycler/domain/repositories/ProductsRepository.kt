package com.training.recycler.domain.repositories

import com.training.recycler.data.ProductResponse

object ProductsRepository {

    var allProducts: List<ProductResponse> = listOf()
    var mensClothingProducts: List<ProductResponse> = listOf()
    var electronicsProducts: List<ProductResponse> = listOf()
    var womensClothingProducts: List<ProductResponse> = listOf()
    var jeweleryProducts: List<ProductResponse> = listOf()
}