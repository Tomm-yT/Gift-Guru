package com.training.recycler.data.network

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@Module
@InstallIn(SingletonComponent::class)
object Network {
    //https://www.googleapis.com/books/v1/volumes?q=adventure&maxResults=2&printType=magazines
    //https://fakestoreapi.com/products
    const val BASE_URL = "https://fakestoreapi.com"
    const val ENDPOINT = "products"
    //const val BOOK_TITLE_ARG = "q"
    //const val BOOK_TYPE_ARG = "printType"
    //const val BOOK_MAX_RESULTS = "maxResults"

    @Provides
    fun provideProductService(retrofit: Retrofit): ProductService{
        return retrofit.create(ProductService::class.java)
    }

    @Provides
    fun provideRetrofit(): Retrofit {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(createClient())
            .build()

        return retrofit
    }

    private fun createClient(): OkHttpClient {
        val okhttpLogging = HttpLoggingInterceptor()
        okhttpLogging.level = HttpLoggingInterceptor.Level.BASIC

        val client = OkHttpClient.Builder().addInterceptor(okhttpLogging).build()
        return client
    }
}
