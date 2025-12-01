package com.quizocr.joiefull.domain.use_case

import app.cash.turbine.test
import com.quizocr.joiefull.domain.model.ClothingItem
import com.quizocr.joiefull.domain.model.Picture
import com.quizocr.joiefull.domain.repository.ClothingRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class GetClothingItemsUseCaseTest {

    private lateinit var repository: ClothingRepository
    private lateinit var useCase: GetClothingItemsUseCase

    private val testItems = listOf(
        ClothingItem(
            id = 1,
            picture = Picture(url = "url1", description = "desc1"),
            name = "T-Shirt",
            category = "Shirts",
            likes = 10,
            price = 19.99,
            originalPrice = 29.99,
            isFavorited = false
        ),
        ClothingItem(
            id = 2,
            picture = Picture(url = "url2", description = "desc2"),
            name = "Jeans",
            category = "Pants",
            likes = 25,
            price = 49.99,
            originalPrice = 79.99,
            isFavorited = true
        )
    )

    @Before
    fun setUp() {
        repository = mockk()
        useCase = GetClothingItemsUseCase(repository)
    }

    @Test
    fun `invoke should return flow of clothing items from repository`() = runTest {
        // Given
        coEvery { repository.getClothingItems() } returns flowOf(testItems)

        // When
        useCase().test {
            // Then
            val items = awaitItem()
            assertEquals(2, items.size)
            assertEquals("T-Shirt", items[0].name)
            assertEquals("Jeans", items[1].name)
            awaitComplete()
        }

        coVerify(exactly = 1) { repository.getClothingItems() }
    }

    @Test
    fun `invoke should return empty list when repository returns empty`() = runTest {
        // Given
        coEvery { repository.getClothingItems() } returns flowOf(emptyList())

        // When
        useCase().test {
            // Then
            val items = awaitItem()
            assertEquals(0, items.size)
            awaitComplete()
        }
    }
}
