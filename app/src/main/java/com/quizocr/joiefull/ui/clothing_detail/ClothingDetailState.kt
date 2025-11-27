package com.quizocr.joiefull.ui.clothing_detail

import com.quizocr.joiefull.domain.model.ClothingItem

data class ClothingDetailState(
    val clothingItem: ClothingItem? = null,
    val isLoading: Boolean = false,
    val error: String? = null,
    val userRating: Int = 0,
    val userComment: String = "",
    val comments: List<String> = emptyList(),
    val showShareDialog: Boolean = false,
    val shareMessage: String = ""
)