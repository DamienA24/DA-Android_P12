package com.quizocr.joiefull.data.repository

import com.quizocr.joiefull.data.remote.ClothingApi
import com.quizocr.joiefull.data.remote.dto.toClothingItem
import com.quizocr.joiefull.domain.model.ClothingItem
import com.quizocr.joiefull.domain.repository.ClothingRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ClothingRepositoryImpl @Inject constructor(
    private val api: ClothingApi
) : ClothingRepository {

    private val _clothingItems = MutableStateFlow<List<ClothingItem>>(emptyList())

    override suspend fun getClothingItems(): Flow<List<ClothingItem>> {
        if (_clothingItems.value.isEmpty()) {
            val items = api.getClothingItems().map { it.toClothingItem() }
            _clothingItems.value = items
        }
        return _clothingItems
    }

    override fun getClothingItem(id: Int): Flow<ClothingItem?> {
        return _clothingItems.map { items -> items.find { it.id == id } }
    }

    override suspend fun toggleFavorite(itemId: Int) {
        val currentItems = _clothingItems.value
        val updatedItems = currentItems.map {
            if (it.id == itemId) {
                it.copy(
                    isFavorited = !it.isFavorited,
                    likes = if (it.isFavorited) it.likes - 1 else it.likes + 1
                )
            } else {
                it
            }
        }
        _clothingItems.value = updatedItems
    }
}