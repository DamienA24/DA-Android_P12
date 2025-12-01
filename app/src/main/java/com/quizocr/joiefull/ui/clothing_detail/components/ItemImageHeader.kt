package com.quizocr.joiefull.ui.clothing_detail.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.quizocr.joiefull.domain.model.ClothingItem
import com.quizocr.joiefull.ui.components.FavoriteCounter

@Composable
fun ItemImageHeader(
    item: ClothingItem,
    onBackClicked: () -> Unit,
    onShareClicked: () -> Unit,
    onFavoriteClicked: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .aspectRatio(1f)
    ) {
        AsyncImage(
            model = item.picture.url,
            contentDescription = item.picture.description,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxSize()
                .clip(RoundedCornerShape(16.dp))
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBackClicked) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = Color.White)
            }
            IconButton(onClick = onShareClicked) {
                Icon(Icons.Default.Share, contentDescription = "Share", tint = Color.White)
            }
        }
        FavoriteCounter(
            likes = item.likes,
            isFavorited = item.isFavorited,
            onFavoriteClicked = onFavoriteClicked,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(8.dp)
        )
    }
}
