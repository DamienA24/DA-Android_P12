package com.quizocr.joiefull.di

import com.quizocr.joiefull.data.remote.ClothingApi
import com.quizocr.joiefull.data.repository.ClothingRepositoryImpl
import com.quizocr.joiefull.domain.repository.ClothingRepository
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

interface AppModule {
    val clothingApi: ClothingApi
    val clothingRepository: ClothingRepository
}

class AppModuleImpl : AppModule {

    override val clothingApi: ClothingApi by lazy {
        Retrofit.Builder()
            .baseUrl(ClothingApi.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ClothingApi::class.java)
    }

    override val clothingRepository: ClothingRepository by lazy {
        ClothingRepositoryImpl(clothingApi)
    }
}