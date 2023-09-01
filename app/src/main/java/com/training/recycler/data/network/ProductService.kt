package com.training.recycler.data.network

import com.training.recycler.data.ProductResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ProductService {


    @GET("products")
    suspend fun getProducts(): List<ProductResponse>

//    @GET(Network.ENDPOINT)
//    suspend fun getProducts(
//        //@Query(Network.BOOK_TITLE_ARG) BookTitle: String,
//        //@Query(Network.BOOK_TYPE_ARG)bookType: String,
//        //@Query(Network.BOOK_MAX_RESULTS)maxResults: Int
//    ): Response<ProductResponse>
}