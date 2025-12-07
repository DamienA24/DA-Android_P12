package com.quizocr.joiefull.ui.clothing_detail

import android.content.Intent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.quizocr.joiefull.ui.components.PriceDisplay
import com.quizocr.joiefull.ui.components.PriceStyle
import com.quizocr.joiefull.ui.clothing_detail.components.CommentInputSection
import com.quizocr.joiefull.ui.clothing_detail.components.ItemImageHeader
import com.quizocr.joiefull.ui.clothing_detail.components.RatingSection
import kotlinx.coroutines.flow.collectLatest

/**
 * Reusable detail content component
 * Can be used both in full-screen detail screen (phone) and in detail pane (tablet)
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ClothingDetailContent(
    clothingId: Int,
    showBackButton: Boolean = true,
    onBackClick: (() -> Unit)? = null,
    viewModel: ClothingDetailViewModel = hiltViewModel()
) {
    val state = viewModel.state.value
    val context = LocalContext.current

    // Load the clothing item when clothingId changes
    LaunchedEffect(key1 = clothingId) {
        viewModel.loadClothingItem(clothingId)
    }

    LaunchedEffect(key1 = true) {
        viewModel.eventFlow.collectLatest { event ->
            when (event) {
                is ClothingDetailViewModel.UiEvent.Share -> {
                    val sendIntent: Intent = Intent().apply {
                        action = Intent.ACTION_SEND
                        putExtra(Intent.EXTRA_TEXT, "${event.message} - Regarde cet article : ${event.clothingItem.name} - https://joiefull.com/clothing/${event.clothingItem.id}")
                        type = "text/plain"
                    }
                    val shareIntent = Intent.createChooser(sendIntent, null)
                    context.startActivity(shareIntent)
                }
            }
        }
    }

    if (state.showShareDialog) {
        AlertDialog(
            onDismissRequest = { viewModel.onShareDialogDismissed() },
            title = { Text("Partager cet article") },
            text = {
                OutlinedTextField(
                    value = state.shareMessage,
                    onValueChange = { viewModel.onShareMessageChanged(it) },
                    label = { Text("Ajouter un message") }
                )
            },
            confirmButton = {
                Button(onClick = { viewModel.onConfirmShareClicked() }) {
                    Text("Partager")
                }
            },
            dismissButton = {
                Button(onClick = { viewModel.onShareDialogDismissed() }) {
                    Text("Annuler")
                }
            }
        )
    }

    Box(modifier = Modifier
        .fillMaxSize()
        .padding(6.dp)) {
        if (state.isLoading) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
        }
        state.error?.let {
            Text(text = it, modifier = Modifier.align(Alignment.Center))
        }
        state.clothingItem?.let { item ->
            if (showBackButton) {
                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    item {
                        ItemImageHeader(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(350.dp),
                            item = item,
                            onBackClicked = { onBackClick?.invoke() },
                            onShareClicked = { viewModel.onShareClicked() },
                            onFavoriteClicked = { viewModel.onFavoriteClicked() },
                            showBackButton = true,
                        )
                        StaticDetailContent(state, viewModel)

                        CommentInputSection(
                            comment = state.userComment,
                            onCommentChanged = { viewModel.onCommentChanged(it) },
                            onSubmit = { viewModel.submitComment() }
                        )
                    }

                    if (state.comments.isNotEmpty()) {
                        item {
                            Text(
                                text = "Commentaires",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(start = 16.dp, top = 16.dp, end = 16.dp, bottom = 8.dp)
                            )
                        }
                    }
                    items(state.comments) { comment ->
                        CommentCard(comment)
                    }
                }
            } else {
                // Mode tablette
                Column(modifier = Modifier.fillMaxSize()) {
                    ItemImageHeader(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(0.60f),
                        item = item,
                        onBackClicked = {},
                        onShareClicked = { viewModel.onShareClicked() },
                        onFavoriteClicked = { viewModel.onFavoriteClicked() },
                        showBackButton = false
                    )

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(0.4f)
                            .verticalScroll(rememberScrollState())
                            .padding(horizontal = 8.dp)
                    ) {
                        StaticDetailContent(state, viewModel)

                        Spacer(modifier = Modifier.padding(4.dp))
                        CommentInputSection(
                            comment = state.userComment,
                            onCommentChanged = { viewModel.onCommentChanged(it) },
                            onSubmit = { viewModel.submitComment() }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun StaticDetailContent(
    state: ClothingDetailState,
    viewModel: ClothingDetailViewModel
) {
    state.clothingItem?.let { item ->
        Text(
            text = item.name,
            modifier = Modifier.padding(start = 16.dp, top = 8.dp, end = 16.dp, bottom = 4.dp),
            style = MaterialTheme.typography.headlineMedium
        )

        PriceDisplay(
            price = item.price,
            originalPrice = item.originalPrice,
            style = PriceStyle.LARGE,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp)
        )

        Text(
            text = item.picture.description,
            modifier = Modifier.padding(start = 16.dp, top = 4.dp, end = 16.dp, bottom = 8.dp),
            style = MaterialTheme.typography.bodyMedium
        )

        RatingSection(
            rating = state.userRating,
            onRatingChanged = { viewModel.onRatingChanged(it) },
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp)
        )
    }
}

@Composable
private fun CommentCard(comment: String) {
    Card(modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 16.dp, vertical = 4.dp)) {
        Text(text = comment, modifier = Modifier.padding(16.dp))
    }
}