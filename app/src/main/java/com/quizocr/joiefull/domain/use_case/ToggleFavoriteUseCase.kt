package com.quizocr.joiefull.domain.use_case

import com.quizocr.joiefull.domain.model.ClothingItem
import javax.inject.Inject

class ToggleFavoriteUseCase @Inject constructor() {
    operator fun invoke(items: List<ClothingItem>, itemId: Int): List<ClothingItem> {
        return items.map {
            if (it.id == itemId) {
                it.copy(isFavorited = !it.isFavorited, likes = if(it.isFavorited) it.likes -1 else it.likes + 1)
            } else {
                it
            }
        }
    }
}