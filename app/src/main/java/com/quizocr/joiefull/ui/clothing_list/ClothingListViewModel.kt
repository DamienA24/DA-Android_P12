package com.quizocr.joiefull.ui.clothing_list

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.quizocr.joiefull.domain.use_case.GetClothingItemsUseCase
import com.quizocr.joiefull.domain.use_case.ToggleFavoriteUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ClothingListViewModel @Inject constructor(
    private val getClothingItemsUseCase: GetClothingItemsUseCase,
    private val toggleFavoriteUseCase: ToggleFavoriteUseCase
) : ViewModel() {

    private val _state = mutableStateOf(ClothingListState())
    val state: State<ClothingListState> = _state

    init {
        getClothingItems()
    }

    fun onFavoriteClicked(itemId: Int) {
        viewModelScope.launch {
            toggleFavoriteUseCase(itemId)
        }
    }

    private fun getClothingItems() {
        viewModelScope.launch {
            try {
                _state.value = ClothingListState(isLoading = true)
                getClothingItemsUseCase().onEach { items ->
                    _state.value = ClothingListState(clothingItems = items)
                }.launchIn(viewModelScope)
            } catch (e: Exception) {
                _state.value = ClothingListState(error = e.localizedMessage ?: "An unexpected error occurred")
            }
        }
    }
}