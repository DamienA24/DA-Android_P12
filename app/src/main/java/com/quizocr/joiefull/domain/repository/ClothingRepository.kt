package com.quizocr.joiefull.domain.repository

import com.quizocr.joiefull.domain.model.ClothingItem

interface ClothingRepository {
    suspend fun getClothingItems(): List<ClothingItem>
}