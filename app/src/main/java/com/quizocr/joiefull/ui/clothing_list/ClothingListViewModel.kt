package com.quizocr.joiefull.ui.clothing_list

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.quizocr.joiefull.domain.use_case.GetClothingItemsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ClothingListViewModel @Inject constructor(
    private val getClothingItemsUseCase: GetClothingItemsUseCase
) : ViewModel() {

    private val _state = mutableStateOf(ClothingListState())
    val state: State<ClothingListState> = _state

    init {
        getClothingItems()
    }

    private fun getClothingItems() {
        viewModelScope.launch {
            try {
                _state.value = ClothingListState(isLoading = true)
                val clothingItems = getClothingItemsUseCase()
                _state.value = ClothingListState(clothingItems = clothingItems)
            } catch (e: Exception) {
                _state.value = ClothingListState(error = e.localizedMessage ?: "An unexpected error occurred")
            }
        }
    }
}