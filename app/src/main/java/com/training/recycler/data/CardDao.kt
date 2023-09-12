package com.training.recycler.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.training.recycler.domain.entities.CardItem

@Dao
interface CardDao {
    @Query("SELECT * FROM CardItem")
    fun getAllCards(): List<CardItem>

    @Query("DELETE FROM CardItem")
    fun deleteAllCards()

    @Insert
    fun insert(card: CardItem): Long

    @Delete
    fun delete(cardItem: CardItem)

    @Query("DELETE FROM CardItem WHERE username = :username AND title = :title")
    fun deleteByUsernameAndTitle(username: String, title: String)

    @Update
    fun update(card: CardItem)
}