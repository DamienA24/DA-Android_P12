package com.quizocr.joiefull.domain.repository

import com.quizocr.joiefull.domain.model.ClothingItem
import kotlinx.coroutines.flow.Flow

interface ClothingRepository {
    suspend fun getClothingItems(): Flow<List<ClothingItem>>
    fun getClothingItem(id: Int): Flow<ClothingItem?>
    suspend fun toggleFavorite(itemId: Int)
}