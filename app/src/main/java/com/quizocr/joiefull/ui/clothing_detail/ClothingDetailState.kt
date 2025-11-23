package com.quizocr.joiefull.ui.clothing_detail

import com.quizocr.joiefull.domain.model.ClothingItem

data class ClothingDetailState(
    val clothingItem: ClothingItem? = null,
    val isLoading: Boolean = false,
    val error: String? = null
)