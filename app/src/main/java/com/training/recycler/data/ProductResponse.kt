package com.training.recycler.data

import com.google.gson.annotations.SerializedName

data class ProductResponse(
    @SerializedName("id") val id: Int,
    @SerializedName("title") val title: String,
    @SerializedName("price") val price: Double,
    @SerializedName("description") val description: String,
    @SerializedName("category") val category: String,
    @SerializedName("image") val image: String,
    @SerializedName("rating") val rating: Rating
)

data class Rating(
    @SerializedName("rate") val rate: Double,
    @SerializedName("count") val count: Int
)




//{
//    "id": 1,
//    "title": "Fjallraven - Foldsack No. 1 Backpack, Fits 15 Laptops",
//    "price": 109.95,
//    "description": "Your perfect pack for everyday use and walks in the forest. Stash your laptop (up to 15 inches) in the padded sleeve, your everyday",
//    "category": "men's clothing",
//    "image": "https://fakestoreapi.com/img/81fPKd-2AYL._AC_SL1500_.jpg",
//    "rating": {
//    "rate": 3.9,
//    "count": 120
//}
//},


