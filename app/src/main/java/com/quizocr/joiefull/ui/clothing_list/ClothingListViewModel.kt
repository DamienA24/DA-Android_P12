package com.quizocr.joiefull.ui.clothing_list

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.quizocr.joiefull.domain.repository.ClothingRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ClothingListViewModel @Inject constructor(
    private val repository: ClothingRepository
) : ViewModel() {

    private val _state = mutableStateOf(ClothingListState())
    val state: State<ClothingListState> = _state

    init {
        getClothingItems()
    }

    fun onFavoriteClicked(itemId: Int) {
        viewModelScope.launch {
            repository.toggleFavorite(itemId)
        }
    }

    private fun getClothingItems() {
        viewModelScope.launch {
            try {
                _state.value = ClothingListState(isLoading = true)
                repository.getClothingItems().onEach { items ->
                    _state.value = ClothingListState(clothingItems = items)
                }.launchIn(viewModelScope)
            } catch (e: Exception) {
                _state.value = ClothingListState(error = e.localizedMessage ?: "An unexpected error occurred")
            }
        }
    }
}