package com.quizocr.joiefull.domain.use_case

import com.quizocr.joiefull.domain.model.ClothingItem
import com.quizocr.joiefull.domain.repository.ClothingRepository

class GetClothingItemUseCase(private val repository: ClothingRepository) {

    suspend operator fun invoke(id: Int): ClothingItem? {
        // In a real app, this would fetch a single item from the API.
        // For this example, we'll just find it in the list.
        return repository.getClothingItems().find { it.id == id }
    }
}