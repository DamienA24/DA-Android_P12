package com.quizocr.joiefull.domain.use_case

import app.cash.turbine.test
import com.quizocr.joiefull.domain.model.ClothingItem
import com.quizocr.joiefull.domain.model.Picture
import com.quizocr.joiefull.domain.repository.ClothingRepository
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test

class GetClothingItemUseCaseTest {

    private lateinit var repository: ClothingRepository
    private lateinit var useCase: GetClothingItemUseCase

    private val testItem = ClothingItem(
        id = 1,
        picture = Picture(url = "url1", description = "desc1"),
        name = "T-Shirt",
        category = "Shirts",
        likes = 10,
        price = 19.99,
        originalPrice = 29.99,
        isFavorited = false
    )

    @Before
    fun setUp() {
        repository = mockk()
        useCase = GetClothingItemUseCase(repository)
    }

    @Test
    fun `invoke should return flow of clothing item from repository`() = runTest {
        // Given
        every { repository.getClothingItem(1) } returns flowOf(testItem)

        // When
        useCase(1).test {
            // Then
            val item = awaitItem()
            assertEquals(1, item?.id)
            assertEquals("T-Shirt", item?.name)
            assertEquals(10, item?.likes)
            awaitComplete()
        }

        verify(exactly = 1) { repository.getClothingItem(1) }
    }

    @Test
    fun `invoke should return null when item not found`() = runTest {
        // Given
        every { repository.getClothingItem(999) } returns flowOf(null)

        // When
        useCase(999).test {
            // Then
            val item = awaitItem()
            assertNull(item)
            awaitComplete()
        }
    }

    @Test
    fun `invoke should call repository with correct id`() = runTest {
        // Given
        val itemId = 42
        every { repository.getClothingItem(itemId) } returns flowOf(testItem.copy(id = itemId))

        // When
        useCase(itemId).test {
            awaitItem()
            awaitComplete()
        }

        // Then
        verify(exactly = 1) { repository.getClothingItem(itemId) }
    }
}
