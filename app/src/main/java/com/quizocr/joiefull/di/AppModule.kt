package com.quizocr.joiefull.di

import com.quizocr.joiefull.data.remote.ClothingApi
import com.quizocr.joiefull.data.repository.ClothingRepositoryImpl
import com.quizocr.joiefull.domain.repository.ClothingRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideClothingRepository(api: ClothingApi): ClothingRepository {
        return ClothingRepositoryImpl(api)
    }
}