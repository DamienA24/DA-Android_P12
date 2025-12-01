package com.quizocr.joiefull.ui.clothing_detail

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.SavedStateHandle
import app.cash.turbine.test
import com.quizocr.joiefull.domain.model.ClothingItem
import com.quizocr.joiefull.domain.model.Picture
import com.quizocr.joiefull.domain.use_case.GetClothingItemUseCase
import com.quizocr.joiefull.domain.use_case.ToggleFavoriteUseCase
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
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
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class ClothingDetailViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var getClothingItemUseCase: GetClothingItemUseCase
    private lateinit var toggleFavoriteUseCase: ToggleFavoriteUseCase
    private lateinit var savedStateHandle: SavedStateHandle
    private lateinit var viewModel: ClothingDetailViewModel

    private val testDispatcher = StandardTestDispatcher()

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
        Dispatchers.setMain(testDispatcher)
        getClothingItemUseCase = mockk()
        toggleFavoriteUseCase = mockk()
        savedStateHandle = mockk(relaxed = true)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `init should load clothing item from savedStateHandle`() = runTest {
        // Given
        every { savedStateHandle.get<Int>("clothingId") } returns 1
        every { getClothingItemUseCase(1) } returns flowOf(testItem)

        // When
        viewModel = ClothingDetailViewModel(
            getClothingItemUseCase,
            toggleFavoriteUseCase,
            savedStateHandle
        )
        advanceUntilIdle()

        // Then
        val state = viewModel.state.value
        assertNotNull(state.clothingItem)
        assertEquals(1, state.clothingItem?.id)
        assertEquals("T-Shirt", state.clothingItem?.name)
    }

    @Test
    fun `init should not load item when clothingId is null`() = runTest {
        // Given
        every { savedStateHandle.get<Int>("clothingId") } returns null

        // When
        viewModel = ClothingDetailViewModel(
            getClothingItemUseCase,
            toggleFavoriteUseCase,
            savedStateHandle
        )
        advanceUntilIdle()

        // Then
        val state = viewModel.state.value
        assertNull(state.clothingItem)
    }

    @Test
    fun `onFavoriteClicked should call toggleFavorite when item exists`() = runTest {
        // Given
        every { savedStateHandle.get<Int>("clothingId") } returns 1
        every { getClothingItemUseCase(1) } returns flowOf(testItem)
        coEvery { toggleFavoriteUseCase(1) } just runs

        viewModel = ClothingDetailViewModel(
            getClothingItemUseCase,
            toggleFavoriteUseCase,
            savedStateHandle
        )
        advanceUntilIdle()

        // When
        viewModel.onFavoriteClicked()
        advanceUntilIdle()

        // Then
        coVerify(exactly = 1) { toggleFavoriteUseCase(1) }
    }

    @Test
    fun `onFavoriteClicked should not call toggleFavorite when item is null`() = runTest {
        // Given
        every { savedStateHandle.get<Int>("clothingId") } returns null

        viewModel = ClothingDetailViewModel(
            getClothingItemUseCase,
            toggleFavoriteUseCase,
            savedStateHandle
        )
        advanceUntilIdle()

        // When
        viewModel.onFavoriteClicked()
        advanceUntilIdle()

        // Then
        coVerify(exactly = 0) { toggleFavoriteUseCase(any()) }
    }

    @Test
    fun `onRatingChanged should update userRating in state`() = runTest {
        // Given
        every { savedStateHandle.get<Int>("clothingId") } returns null
        viewModel = ClothingDetailViewModel(
            getClothingItemUseCase,
            toggleFavoriteUseCase,
            savedStateHandle
        )

        // When
        viewModel.onRatingChanged(4)

        // Then
        assertEquals(4, viewModel.state.value.userRating)
    }

    @Test
    fun `onCommentChanged should update userComment in state`() = runTest {
        // Given
        every { savedStateHandle.get<Int>("clothingId") } returns null
        viewModel = ClothingDetailViewModel(
            getClothingItemUseCase,
            toggleFavoriteUseCase,
            savedStateHandle
        )

        // When
        viewModel.onCommentChanged("Great product!")

        // Then
        assertEquals("Great product!", viewModel.state.value.userComment)
    }

    @Test
    fun `onShareMessageChanged should update shareMessage in state`() = runTest {
        // Given
        every { savedStateHandle.get<Int>("clothingId") } returns null
        viewModel = ClothingDetailViewModel(
            getClothingItemUseCase,
            toggleFavoriteUseCase,
            savedStateHandle
        )

        // When
        viewModel.onShareMessageChanged("Check this out!")

        // Then
        assertEquals("Check this out!", viewModel.state.value.shareMessage)
    }

    @Test
    fun `onShareClicked should set showShareDialog to true`() = runTest {
        // Given
        every { savedStateHandle.get<Int>("clothingId") } returns null
        viewModel = ClothingDetailViewModel(
            getClothingItemUseCase,
            toggleFavoriteUseCase,
            savedStateHandle
        )

        // When
        viewModel.onShareClicked()

        // Then
        assertTrue(viewModel.state.value.showShareDialog)
    }

    @Test
    fun `onShareDialogDismissed should reset share state`() = runTest {
        // Given
        every { savedStateHandle.get<Int>("clothingId") } returns null
        viewModel = ClothingDetailViewModel(
            getClothingItemUseCase,
            toggleFavoriteUseCase,
            savedStateHandle
        )

        viewModel.onShareMessageChanged("Test message")
        viewModel.onShareClicked()

        // When
        viewModel.onShareDialogDismissed()

        // Then
        val state = viewModel.state.value
        assertFalse(state.showShareDialog)
        assertEquals("", state.shareMessage)
    }

    @Test
    fun `onConfirmShareClicked should emit share event and dismiss dialog`() = runTest {
        // Given
        every { savedStateHandle.get<Int>("clothingId") } returns 1
        every { getClothingItemUseCase(1) } returns flowOf(testItem)

        viewModel = ClothingDetailViewModel(
            getClothingItemUseCase,
            toggleFavoriteUseCase,
            savedStateHandle
        )
        advanceUntilIdle()

        viewModel.onShareMessageChanged("Check this out!")
        viewModel.onShareClicked()

        // When & Then
        viewModel.eventFlow.test {
            viewModel.onConfirmShareClicked()
            advanceUntilIdle()

            val event = awaitItem() as ClothingDetailViewModel.UiEvent.Share
            assertEquals(testItem, event.clothingItem)
            assertEquals("Check this out!", event.message)

            // Verify dialog is dismissed
            assertFalse(viewModel.state.value.showShareDialog)
            assertEquals("", viewModel.state.value.shareMessage)
        }
    }

    @Test
    fun `submitComment should add comment to list and reset input`() = runTest {
        // Given
        every { savedStateHandle.get<Int>("clothingId") } returns null
        viewModel = ClothingDetailViewModel(
            getClothingItemUseCase,
            toggleFavoriteUseCase,
            savedStateHandle
        )

        viewModel.onCommentChanged("Great product!")
        viewModel.onRatingChanged(5)

        // When
        viewModel.submitComment()

        // Then
        val state = viewModel.state.value
        assertEquals(1, state.comments.size)
        assertEquals("Great product!", state.comments[0])
        assertEquals("", state.userComment)
        assertEquals(0, state.userRating)
    }

    @Test
    fun `submitComment should not add blank comment`() = runTest {
        // Given
        every { savedStateHandle.get<Int>("clothingId") } returns null
        viewModel = ClothingDetailViewModel(
            getClothingItemUseCase,
            toggleFavoriteUseCase,
            savedStateHandle
        )

        viewModel.onCommentChanged("   ")

        // When
        viewModel.submitComment()

        // Then
        val state = viewModel.state.value
        assertTrue(state.comments.isEmpty())
    }

    @Test
    fun `submitComment should accumulate multiple comments`() = runTest {
        // Given
        every { savedStateHandle.get<Int>("clothingId") } returns null
        viewModel = ClothingDetailViewModel(
            getClothingItemUseCase,
            toggleFavoriteUseCase,
            savedStateHandle
        )

        // When
        viewModel.onCommentChanged("First comment")
        viewModel.submitComment()

        viewModel.onCommentChanged("Second comment")
        viewModel.submitComment()

        // Then
        val state = viewModel.state.value
        assertEquals(2, state.comments.size)
        assertEquals("First comment", state.comments[0])
        assertEquals("Second comment", state.comments[1])
    }
}
