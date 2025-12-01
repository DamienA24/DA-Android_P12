package com.quizocr.joiefull.data.repository

import app.cash.turbine.test
import com.quizocr.joiefull.data.remote.ClothingApi
import com.quizocr.joiefull.data.remote.dto.ClothingItemDto
import com.quizocr.joiefull.data.remote.dto.PictureDto
import com.quizocr.joiefull.domain.model.ClothingItem
import com.quizocr.joiefull.domain.model.Picture
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class ClothingRepositoryImplTest {

    private lateinit var api: ClothingApi
    private lateinit var repository: ClothingRepositoryImpl

    private val testClothingItemDto1 = ClothingItemDto(
        id = 1,
        picture = PictureDto(url = "url1", description = "desc1"),
        name = "T-Shirt",
        category = "Shirts",
        likes = 10,
        price = 19.99,
        originalPrice = 29.99
    )

    private val testClothingItemDto2 = ClothingItemDto(
        id = 2,
        picture = PictureDto(url = "url2", description = "desc2"),
        name = "Jeans",
        category = "Pants",
        likes = 25,
        price = 49.99,
        originalPrice = 79.99
    )

    @Before
    fun setUp() {
        api = mockk()
        repository = ClothingRepositoryImpl(api)
    }

    @Test
    fun `getClothingItems should fetch from API and emit items`() = runTest {
        // Given
        val dtoList = listOf(testClothingItemDto1, testClothingItemDto2)
        coEvery { api.getClothingItems() } returns dtoList

        // When
        repository.getClothingItems().test {
            // Then
            val items = awaitItem()
            assertEquals(2, items.size)
            assertEquals(1, items[0].id)
            assertEquals("T-Shirt", items[0].name)
            assertEquals(10, items[0].likes)
            assertFalse(items[0].isFavorited)

            coVerify(exactly = 1) { api.getClothingItems() }
        }
    }

    @Test
    fun `getClothingItems should not fetch from API on second call`() = runTest {
        // Given
        val dtoList = listOf(testClothingItemDto1)
        coEvery { api.getClothingItems() } returns dtoList

        // When - First call
        repository.getClothingItems().test {
            awaitItem()
        }

        // When - Second call
        repository.getClothingItems().test {
            awaitItem()
        }

        // Then - API should be called only once
        coVerify(exactly = 1) { api.getClothingItems() }
    }

    @Test
    fun `getClothingItem should return correct item by id`() = runTest {
        // Given
        val dtoList = listOf(testClothingItemDto1, testClothingItemDto2)
        coEvery { api.getClothingItems() } returns dtoList

        // First load the items
        repository.getClothingItems().test { awaitItem() }

        // When
        repository.getClothingItem(1).test {
            // Then
            val item = awaitItem()
            assertEquals(1, item?.id)
            assertEquals("T-Shirt", item?.name)
        }
    }

    @Test
    fun `getClothingItem should return null for non-existent id`() = runTest {
        // Given
        val dtoList = listOf(testClothingItemDto1)
        coEvery { api.getClothingItems() } returns dtoList

        // First load the items
        repository.getClothingItems().test { awaitItem() }

        // When
        repository.getClothingItem(999).test {
            // Then
            val item = awaitItem()
            assertNull(item)
        }
    }

    @Test
    fun `toggleFavorite should toggle isFavorited and update likes`() = runTest {
        // Given
        val dtoList = listOf(testClothingItemDto1)
        coEvery { api.getClothingItems() } returns dtoList

        // Load items
        repository.getClothingItems().test { awaitItem() }

        // When - Toggle favorite
        repository.toggleFavorite(1)

        // Then - Item should be favorited and likes increased
        repository.getClothingItem(1).test {
            val item = awaitItem()
            assertTrue(item?.isFavorited == true)
            assertEquals(11, item?.likes)
        }

        // When - Toggle favorite again
        repository.toggleFavorite(1)

        // Then - Item should not be favorited and likes decreased
        repository.getClothingItem(1).test {
            val item = awaitItem()
            assertFalse(item?.isFavorited == true)
            assertEquals(10, item?.likes)
        }
    }

    @Test
    fun `toggleFavorite should only affect the specific item`() = runTest {
        // Given
        val dtoList = listOf(testClothingItemDto1, testClothingItemDto2)
        coEvery { api.getClothingItems() } returns dtoList

        // Load items
        repository.getClothingItems().test { awaitItem() }

        // When - Toggle favorite on first item
        repository.toggleFavorite(1)

        // Then - Only first item should be affected
        repository.getClothingItem(1).test {
            val item1 = awaitItem()
            assertTrue(item1?.isFavorited == true)
        }

        repository.getClothingItem(2).test {
            val item2 = awaitItem()
            assertFalse(item2?.isFavorited == true)
            assertEquals(25, item2?.likes) // Original likes unchanged
        }
    }
}
