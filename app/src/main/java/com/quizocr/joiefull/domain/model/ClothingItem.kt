package com.quizocr.joiefull.domain.model

data class ClothingItem(
    val id: Int,
    val picture: Picture,
    val name: String,
    val category: String,
    val likes: Int,
    val price: Double,
    val originalPrice: Double
)