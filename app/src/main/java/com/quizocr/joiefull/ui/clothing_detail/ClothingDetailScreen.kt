package com.quizocr.joiefull.ui.clothing_detail

import android.content.Intent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.quizocr.joiefull.ui.components.PriceDisplay
import com.quizocr.joiefull.ui.components.PriceStyle
import com.quizocr.joiefull.ui.clothing_detail.components.CommentInputSection
import com.quizocr.joiefull.ui.clothing_detail.components.ItemImageHeader
import com.quizocr.joiefull.ui.clothing_detail.components.RatingSection
import kotlinx.coroutines.flow.collectLatest

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ClothingDetailScreen(
    navController: NavController,
    viewModel: ClothingDetailViewModel = hiltViewModel()
) {
    val state = viewModel.state.value
    val context = LocalContext.current

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

    Box(modifier = Modifier.fillMaxSize()) {
        if (state.isLoading) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
        }
        state.error?.let {
            Text(text = it, modifier = Modifier.align(Alignment.Center))
        }
        state.clothingItem?.let { item ->
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                item {
                    ItemImageHeader(
                        item = item,
                        onBackClicked = { navController.popBackStack() },
                        onShareClicked = { viewModel.onShareClicked() }
                    )
                    Text(text = item.name, modifier = Modifier.padding(16.dp), style = MaterialTheme.typography.headlineMedium)
                    
                    PriceDisplay(
                        price = item.price,
                        originalPrice = item.originalPrice,
                        style = PriceStyle.LARGE,
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )

                    Text(text = item.picture.description, modifier = Modifier.padding(16.dp), style = MaterialTheme.typography.bodyMedium)

                    RatingSection(
                        rating = state.userRating,
                        onRatingChanged = { viewModel.onRatingChanged(it) },
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )

                    CommentInputSection(
                        comment = state.userComment,
                        onCommentChanged = { viewModel.onCommentChanged(it) },
                        onSubmit = { viewModel.submitComment() }
                    )
                }

                // Submitted Comments
                items(state.comments) { comment ->
                    Card(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 4.dp)) {
                        Text(text = comment, modifier = Modifier.padding(16.dp))
                    }
                }
            }
        }
    }
}