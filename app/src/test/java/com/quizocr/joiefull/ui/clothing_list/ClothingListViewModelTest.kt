package com.quizocr.joiefull.ui.clothing_list

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.quizocr.joiefull.domain.model.ClothingItem
import com.quizocr.joiefull.domain.model.Picture
import com.quizocr.joiefull.domain.use_case.GetClothingItemsUseCase
import com.quizocr.joiefull.domain.use_case.ToggleFavoriteUseCase
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class ClothingListViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var getClothingItemsUseCase: GetClothingItemsUseCase
    private lateinit var toggleFavoriteUseCase: ToggleFavoriteUseCase
    private lateinit var viewModel: ClothingListViewModel

    private val testDispatcher = StandardTestDispatcher()

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
            isFavorited = false
        )
    )

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        getClothingItemsUseCase = mockk()
        toggleFavoriteUseCase = mockk()
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `init should load clothing items and update state`() = runTest {
        // Given
        coEvery { getClothingItemsUseCase() } returns flowOf(testItems)

        // When
        viewModel = ClothingListViewModel(getClothingItemsUseCase, toggleFavoriteUseCase)
        advanceUntilIdle()

        // Then
        val state = viewModel.state.value
        assertEquals(2, state.clothingItems.size)
        assertFalse(state.isLoading)
        assertNull(state.error)
        coVerify(exactly = 1) { getClothingItemsUseCase() }
    }

    @Test
    fun `init should complete loading and set loading to false`() = runTest {
        // Given
        coEvery { getClothingItemsUseCase() } returns flowOf(testItems)

        // When
        viewModel = ClothingListViewModel(getClothingItemsUseCase, toggleFavoriteUseCase)
        advanceUntilIdle()

        // Then - After loading completes, loading should be false
        assertFalse(viewModel.state.value.isLoading)
        assertTrue(viewModel.state.value.clothingItems.isNotEmpty())
    }

    @Test
    fun `init should handle error and update state`() = runTest {
        // Given
        val errorMessage = "Network error"
        coEvery { getClothingItemsUseCase() } throws Exception(errorMessage)

        // When
        viewModel = ClothingListViewModel(getClothingItemsUseCase, toggleFavoriteUseCase)
        advanceUntilIdle()

        // Then
        val state = viewModel.state.value
        assertTrue(state.clothingItems.isEmpty())
        assertFalse(state.isLoading)
        assertEquals(errorMessage, state.error)
    }

    @Test
    fun `init should handle exception with null message`() = runTest {
        // Given
        coEvery { getClothingItemsUseCase() } throws Exception()

        // When
        viewModel = ClothingListViewModel(getClothingItemsUseCase, toggleFavoriteUseCase)
        advanceUntilIdle()

        // Then
        val state = viewModel.state.value
        assertEquals("An unexpected error occurred", state.error)
    }

    @Test
    fun `onFavoriteClicked should call toggleFavorite use case`() = runTest {
        // Given
        coEvery { getClothingItemsUseCase() } returns flowOf(testItems)
        coEvery { toggleFavoriteUseCase(any()) } just runs
        viewModel = ClothingListViewModel(getClothingItemsUseCase, toggleFavoriteUseCase)
        advanceUntilIdle()

        // When
        viewModel.onFavoriteClicked(1)
        advanceUntilIdle()

        // Then
        coVerify(exactly = 1) { toggleFavoriteUseCase(1) }
    }

    @Test
    fun `onFavoriteClicked should call toggleFavorite with correct item id`() = runTest {
        // Given
        coEvery { getClothingItemsUseCase() } returns flowOf(testItems)
        coEvery { toggleFavoriteUseCase(any()) } just runs
        viewModel = ClothingListViewModel(getClothingItemsUseCase, toggleFavoriteUseCase)
        advanceUntilIdle()

        // When
        viewModel.onFavoriteClicked(1)
        advanceUntilIdle()
        viewModel.onFavoriteClicked(2)
        advanceUntilIdle()

        // Then
        coVerify(exactly = 1) { toggleFavoriteUseCase(1) }
        coVerify(exactly = 1) { toggleFavoriteUseCase(2) }
    }

    @Test
    fun `init with empty list should update state correctly`() = runTest {
        // Given
        coEvery { getClothingItemsUseCase() } returns flowOf(emptyList())

        // When
        viewModel = ClothingListViewModel(getClothingItemsUseCase, toggleFavoriteUseCase)
        advanceUntilIdle()

        // Then
        val state = viewModel.state.value
        assertTrue(state.clothingItems.isEmpty())
        assertFalse(state.isLoading)
        assertNull(state.error)
    }
}
