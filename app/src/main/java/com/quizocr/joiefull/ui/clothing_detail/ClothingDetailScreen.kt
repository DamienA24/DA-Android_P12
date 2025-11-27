package com.quizocr.joiefull.ui.clothing_detail

import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.StarOutline
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.quizocr.joiefull.ui.components.LikesCounter
import com.quizocr.joiefull.ui.components.PriceDisplay
import com.quizocr.joiefull.ui.components.PriceStyle
import com.quizocr.joiefull.ui.theme.JoieFullTheme
import kotlinx.coroutines.flow.collectLatest

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ClothingDetailScreen(
    navController: NavController,
    viewModel: ClothingDetailViewModel = hiltViewModel()
) {
    val state = viewModel.state.value
    val keyboardController = LocalSoftwareKeyboardController.current
    val context = LocalContext.current
    val focusManager = LocalFocusManager.current

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
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .aspectRatio(1f)
                    ) {
                        AsyncImage(
                            model = item.picture.url,
                            contentDescription = item.picture.description,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .fillMaxSize()
                                .clip(RoundedCornerShape(bottomStart = 16.dp, bottomEnd = 16.dp))
                        )
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .statusBarsPadding()
                                .padding(horizontal = 16.dp, vertical = 8.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            IconButton(onClick = { navController.popBackStack() }) {
                                Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = Color.Black)
                            }
                            IconButton(onClick = { viewModel.onShareClicked() }) {
                                Icon(Icons.Default.Share, contentDescription = "Share", tint = Color.Black)
                            }
                        }
                        LikesCounter(
                            likes = item.likes,
                            modifier = Modifier
                                .align(Alignment.BottomEnd)
                                .padding(8.dp)
                        )
                    }
                    Text(text = item.name, modifier = Modifier.padding(16.dp), style = MaterialTheme.typography.headlineMedium)
                    
                    PriceDisplay(
                        price = item.price,
                        originalPrice = item.originalPrice,
                        style = PriceStyle.LARGE,
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )

                    Text(text = item.picture.description, modifier = Modifier.padding(16.dp), style = MaterialTheme.typography.bodyMedium)


                    Row(
                        modifier = Modifier.padding(horizontal = 16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Avatar
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .clip(CircleShape)
                                .background(MaterialTheme.colorScheme.tertiary)
                        )
                        Spacer(modifier = Modifier.width(8.dp))

                        (1..5).forEach { star ->
                            IconButton(onClick = { viewModel.onRatingChanged(star) }) {
                                Icon(
                                    imageVector = if (star <= state.userRating) Icons.Filled.Star else Icons.Outlined.StarOutline,
                                    contentDescription = "Rating $star",
                                    tint = if (star <= state.userRating) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.tertiary
                                )
                            }
                        }
                    }

                    OutlinedTextField(
                        value = state.userComment,
                        onValueChange = { viewModel.onCommentChanged(it) },
                        label = { Text("Partagez ici vos impressions") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        shape = RoundedCornerShape(24.dp),
                        keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
                        keyboardActions = KeyboardActions(onDone = {
                            viewModel.submitComment()
                            keyboardController?.hide()
                            focusManager.clearFocus()
                        })
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