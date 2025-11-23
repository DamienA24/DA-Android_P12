package com.quizocr.joiefull.domain.use_case

import com.quizocr.joiefull.domain.model.ClothingItem
import com.quizocr.joiefull.domain.repository.ClothingRepository
import javax.inject.Inject

class GetClothingItemsUseCase @Inject constructor(private val repository: ClothingRepository) {

    suspend operator fun invoke(): List<ClothingItem> {
        return repository.getClothingItems()
    }
}