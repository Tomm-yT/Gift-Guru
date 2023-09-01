package com.training.recycler.dependancy

import com.training.recycler.data.CardDao
import com.training.recycler.data.network.ProductService
import com.training.recycler.data.repositories.CardRepositoryImpl
import com.training.recycler.domain.repositories.CardRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun provideCardRepository(
        cardDao: CardDao,
        service: ProductService // Added productService as a parameter
    ): CardRepository = CardRepositoryImpl(cardDao, service) // Passed productService to CardRepositoryImpl
}
