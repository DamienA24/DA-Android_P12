package com.quizocr.joiefull.domain.use_case

import com.quizocr.joiefull.domain.model.ClothingItem
import com.quizocr.joiefull.domain.repository.ClothingRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetClothingItemUseCase @Inject constructor(private val repository: ClothingRepository) {

    operator fun invoke(id: Int): Flow<ClothingItem?> {
        return repository.getClothingItem(id)
    }
}