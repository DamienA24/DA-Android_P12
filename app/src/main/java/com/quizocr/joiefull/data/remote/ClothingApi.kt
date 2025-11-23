package com.quizocr.joiefull.data.remote

import com.quizocr.joiefull.data.remote.dto.ClothingItemDto
import retrofit2.http.GET

interface ClothingApi {

    @GET("/OpenClassrooms-Student-Center/D-velopper-une-interface-accessible-en-Jetpack-Compose/main/api/clothes.json")
    suspend fun getClothingItems(): List<ClothingItemDto>

    companion object {
        const val BASE_URL = "https://raw.githubusercontent.com/"
    }
}