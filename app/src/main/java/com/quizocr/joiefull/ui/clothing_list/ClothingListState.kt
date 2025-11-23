package com.quizocr.joiefull.ui.clothing_list

import com.quizocr.joiefull.domain.model.ClothingItem

data class ClothingListState(
    val clothingItems: List<ClothingItem> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)