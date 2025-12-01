package com.quizocr.joiefull.domain.use_case

import com.quizocr.joiefull.domain.repository.ClothingRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class ToggleFavoriteUseCaseTest {

    private lateinit var repository: ClothingRepository
    private lateinit var useCase: ToggleFavoriteUseCase

    @Before
    fun setUp() {
        repository = mockk()
        useCase = ToggleFavoriteUseCase(repository)
    }

    @Test
    fun `invoke should call repository toggleFavorite with correct id`() = runTest {
        // Given
        val itemId = 1
        coEvery { repository.toggleFavorite(itemId) } just runs

        // When
        useCase(itemId)

        // Then
        coVerify(exactly = 1) { repository.toggleFavorite(itemId) }
    }

    @Test
    fun `invoke should call repository for different item ids`() = runTest {
        // Given
        coEvery { repository.toggleFavorite(any()) } just runs

        // When
        useCase(1)
        useCase(2)
        useCase(3)

        // Then
        coVerify(exactly = 1) { repository.toggleFavorite(1) }
        coVerify(exactly = 1) { repository.toggleFavorite(2) }
        coVerify(exactly = 1) { repository.toggleFavorite(3) }
    }
}
