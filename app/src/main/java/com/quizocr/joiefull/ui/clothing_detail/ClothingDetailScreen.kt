package com.quizocr.joiefull.ui.clothing_detail

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
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.StarOutline
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.quizocr.joiefull.ui.theme.JoieFullTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ClothingDetailScreen(
    navController: NavController,
    viewModel: ClothingDetailViewModel = hiltViewModel()
) {
    val state = viewModel.state.value
    val keyboardController = LocalSoftwareKeyboardController.current

    Scaffold(
        topBar = {
            TopAppBar(
                title = { },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(onClick = { /* TODO: Implement share functionality */ }) {
                        Icon(Icons.Default.Share, contentDescription = "Share")
                    }
                }
            )
        }
    ) { padding ->
        Box(modifier = Modifier.fillMaxSize().padding(padding)) {
            if (state.isLoading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            }
            state.error?.let {
                Text(text = it, modifier = Modifier.align(Alignment.Center))
            }
            state.clothingItem?.let { item ->
                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    item {
                        Box(modifier = Modifier
                            .fillMaxWidth()
                            .aspectRatio(1f)) {
                            AsyncImage(
                                model = item.picture.url,
                                contentDescription = item.picture.description,
                                contentScale = ContentScale.Crop,
                                modifier = Modifier.fillMaxSize()
                            )
                            Card(
                                modifier = Modifier
                                    .align(Alignment.BottomEnd)
                                    .padding(8.dp),
                                shape = RoundedCornerShape(16.dp),
                                colors = CardDefaults.cardColors(containerColor = Color.White)
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        imageVector = Icons.Outlined.FavoriteBorder,
                                        contentDescription = "Likes",
                                        modifier = Modifier.size(16.dp),
                                        tint = Color.Black
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(text = item.likes.toString(), style = MaterialTheme.typography.bodySmall, color = Color.Black)
                                }
                            }
                        }
                        Text(text = item.name, modifier = Modifier.padding(16.dp), style = MaterialTheme.typography.headlineMedium)
                        
                        // Price Section
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(text = "${item.price}€", style = MaterialTheme.typography.titleLarge)
                            Text(
                                text = "${item.originalPrice}€",
                                style = MaterialTheme.typography.titleLarge.copy(textDecoration = TextDecoration.LineThrough),
                                color = Color.Gray
                            )
                        }

                        Text(text = item.picture.description, modifier = Modifier.padding(16.dp), style = MaterialTheme.typography.bodyMedium)


                        // Rating Section
                        Row(
                            modifier = Modifier.padding(horizontal = 16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            // Avatar
                            Box(
                                modifier = Modifier
                                    .size(40.dp)
                                    .clip(CircleShape)
                                    .background(Color.Gray)
                            )
                            Spacer(modifier = Modifier.width(8.dp))

                            // Stars
                            (1..5).forEach { star ->
                                IconButton(onClick = { viewModel.onRatingChanged(star) }) {
                                    Icon(
                                        imageVector = if (star <= state.userRating) Icons.Filled.Star else Icons.Outlined.StarOutline,
                                        contentDescription = "Rating $star",
                                        tint = if (star <= state.userRating) Color.Yellow else Color.Gray
                                    )
                                }
                            }
                        }

                        // Comment Input
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
}