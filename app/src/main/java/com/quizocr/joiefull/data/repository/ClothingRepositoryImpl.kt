package com.quizocr.joiefull.data.repository

import com.quizocr.joiefull.data.remote.ClothingApi
import com.quizocr.joiefull.data.remote.dto.toClothingItem
import com.quizocr.joiefull.domain.model.ClothingItem
import com.quizocr.joiefull.domain.repository.ClothingRepository

class ClothingRepositoryImpl(
    private val api: ClothingApi
) : ClothingRepository {

    override suspend fun getClothingItems(): List<ClothingItem> {
        return api.getClothingItems().map { it.toClothingItem() }
    }
}