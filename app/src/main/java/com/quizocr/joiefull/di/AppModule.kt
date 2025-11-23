package com.quizocr.joiefull.di

import com.quizocr.joiefull.data.remote.ClothingApi
import com.quizocr.joiefull.data.repository.ClothingRepositoryImpl
import com.quizocr.joiefull.domain.repository.ClothingRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideClothingApi(): ClothingApi {
        return Retrofit.Builder()
            .baseUrl(ClothingApi.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ClothingApi::class.java)
    }

    @Provides
    @Singleton
    fun provideClothingRepository(api: ClothingApi): ClothingRepository {
        return ClothingRepositoryImpl(api)
    }
}