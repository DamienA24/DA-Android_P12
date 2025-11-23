package com.quizocr.joiefull.ui.clothing_detail

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.quizocr.joiefull.domain.use_case.GetClothingItemUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ClothingDetailViewModel @Inject constructor(
    private val getClothingItemUseCase: GetClothingItemUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _state = mutableStateOf(ClothingDetailState())
    val state: State<ClothingDetailState> = _state

    init {
        savedStateHandle.get<Int>("clothingId")?.let {
            getClothingItem(it)
        }
    }

    fun onRatingChanged(rating: Int) {
        _state.value = _state.value.copy(userRating = rating)
    }

    fun onCommentChanged(comment: String) {
        _state.value = _state.value.copy(userComment = comment)
    }

    fun submitComment() {
        val currentState = _state.value
        if (currentState.userComment.isNotBlank()) {
            val newComments = currentState.comments + currentState.userComment
            _state.value = currentState.copy(
                comments = newComments,
                userComment = "", // Reset the input field
                userRating = 0 // Reset the rating
            )
        }
    }

    private fun getClothingItem(id: Int) {
        viewModelScope.launch {
            try {
                _state.value = _state.value.copy(isLoading = true)
                val clothingItem = getClothingItemUseCase(id)
                _state.value = _state.value.copy(
                    isLoading = false,
                    clothingItem = clothingItem
                )
            } catch (e: Exception) {
                _state.value = _state.value.copy(
                    isLoading = false,
                    error = e.localizedMessage ?: "An unexpected error occurred"
                )
            }
        }
    }
}