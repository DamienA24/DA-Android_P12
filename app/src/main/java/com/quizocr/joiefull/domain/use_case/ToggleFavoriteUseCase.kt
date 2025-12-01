package com.quizocr.joiefull.domain.use_case

import com.quizocr.joiefull.domain.repository.ClothingRepository
import javax.inject.Inject

class ToggleFavoriteUseCase @Inject constructor(
    private val repository: ClothingRepository
) {
    suspend operator fun invoke(itemId: Int) {
        repository.toggleFavorite(itemId)
    }
}