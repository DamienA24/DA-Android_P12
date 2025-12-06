package com.quizocr.joiefull.ui.clothing_detail

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.quizocr.joiefull.domain.model.ClothingItem
import com.quizocr.joiefull.domain.use_case.GetClothingItemUseCase
import com.quizocr.joiefull.domain.use_case.ToggleFavoriteUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ClothingDetailViewModel @Inject constructor(
    private val getClothingItemUseCase: GetClothingItemUseCase,
    private val toggleFavoriteUseCase: ToggleFavoriteUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _state = mutableStateOf(ClothingDetailState())
    val state: State<ClothingDetailState> = _state

    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    init {
        savedStateHandle.get<Int>("clothingId")?.let {
            getClothingItem(it)
        }
    }

    fun onFavoriteClicked() {
        viewModelScope.launch {
            _state.value.clothingItem?.let { toggleFavoriteUseCase(it.id) }
        }
    }

    fun onRatingChanged(rating: Int) {
        _state.value = _state.value.copy(userRating = rating)
    }

    fun onCommentChanged(comment: String) {
        _state.value = _state.value.copy(userComment = comment)
    }

    fun onShareMessageChanged(message: String) {
        _state.value = _state.value.copy(shareMessage = message)
    }

    fun onShareDialogDismissed() {
        _state.value = _state.value.copy(showShareDialog = false, shareMessage = "")
    }

    fun onShareClicked() {
        _state.value = _state.value.copy(showShareDialog = true)
    }

    fun onConfirmShareClicked() {
        viewModelScope.launch {
            _state.value.clothingItem?.let {
                _eventFlow.emit(UiEvent.Share(it, _state.value.shareMessage))
            }
            onShareDialogDismissed()
        }
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
        getClothingItemUseCase(id).onEach { item ->
            _state.value = _state.value.copy(clothingItem = item)
        }.launchIn(viewModelScope)
    }

    fun loadClothingItem(id: Int) {
        getClothingItem(id)
    }

    sealed class UiEvent {
        data class Share(val clothingItem: ClothingItem, val message: String) : UiEvent()
    }
}