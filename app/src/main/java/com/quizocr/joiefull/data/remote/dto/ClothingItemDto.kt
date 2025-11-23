package com.quizocr.joiefull.data.remote.dto

import com.google.gson.annotations.SerializedName
import com.quizocr.joiefull.domain.model.ClothingItem
import com.quizocr.joiefull.domain.model.Picture

data class ClothingItemDto(
    val id: Int,
    val picture: PictureDto,
    val name: String,
    val category: String,
    val likes: Int,
    val price: Double,
    @SerializedName("original_price")
    val originalPrice: Double
)

fun ClothingItemDto.toClothingItem(): ClothingItem {
    return ClothingItem(
        id = id,
        picture = Picture(url = picture.url, description = picture.description),
        name = name,
        category = category,
        likes = likes,
        price = price,
        original_price = originalPrice
    )
}