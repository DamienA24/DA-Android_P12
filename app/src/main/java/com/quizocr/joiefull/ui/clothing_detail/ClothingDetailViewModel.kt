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
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _state = mutableStateOf(ClothingDetailState())
    val state: State<ClothingDetailState> = _state

    init {
        savedStateHandle.get<Int>("clothingId")?.let {
            getClothingItem(it)
        }
    }

    private fun getClothingItem(id: Int) {
        viewModelScope.launch {
            try {
                _state.value = ClothingDetailState(isLoading = true)
                val clothingItem = getClothingItemUseCase(id)
                _state.value = ClothingDetailState(clothingItem = clothingItem)
            } catch (e: Exception) {
                _state.value = ClothingDetailState(error = e.localizedMessage ?: "An unexpected error occurred")
            }
        }
    }
}