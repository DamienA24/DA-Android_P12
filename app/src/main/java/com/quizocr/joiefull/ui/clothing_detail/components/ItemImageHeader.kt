package com.quizocr.joiefull.ui.clothing_detail.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.quizocr.joiefull.domain.model.ClothingItem
import com.quizocr.joiefull.domain.model.Picture
import com.quizocr.joiefull.ui.components.FavoriteCounter
import com.quizocr.joiefull.R // <--- VÉRIFIEZ ET AJOUTEZ CETTE LIGNE
@Composable
fun ItemImageHeader(item: ClothingItem, onBackClicked: () -> Unit, onShareClicked: () -> Unit, onFavoriteClicked: () -> Unit, showBackButton: Boolean = true, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier,
    ) {
        AsyncImage(
            model = item.picture.url,
            placeholder = painterResource(id = R.drawable.joie_full_icone),
            contentDescription = item.picture.description,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxSize()
                .clip(RoundedCornerShape(16.dp))
        )
        Row(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .fillMaxWidth()
                .statusBarsPadding()
                .padding(horizontal = 16.dp, vertical = 18.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (showBackButton) {
                IconButton(onClick = onBackClicked) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Retour", tint = Color.White)
                }
            } else {
                Spacer(modifier = Modifier.width(48.dp))
            }
            IconButton(onClick = onShareClicked) {
                Icon(Icons.Default.Share, contentDescription = "Partager cet article", tint = Color.White)
            }
        }
        FavoriteCounter(
            likes = item.likes,
            isFavorited = item.isFavorited,
            name = item.name,
            onFavoriteClicked = onFavoriteClicked,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(8.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun ItemImageHeaderPreview() {
    val mockItem = ClothingItem(
        id = 1,
        name = "Superbe Pendentif",
        price = 19.99,
        originalPrice = 69.99,
        likes = 70,
        isFavorited = true,
        picture = Picture(
            url = "https://picsum.photos/200",
            description = "Un pendentif bleu"
        ),
        category = "ACCESSORIES"
    )

    Box(modifier = Modifier.height(300.dp)) {
        ItemImageHeader(
            item = mockItem,
            onBackClicked = {},
            onShareClicked = {},
            onFavoriteClicked = {},
            showBackButton = true
        )
    }
}

@Preview(showBackground = true)
@Composable
fun ItemImageHeaderNoBackPreview() {
    val mockItem = ClothingItem(
        id = 1,
        name = "Superbe Pendentif",
        price = 19.99,
        originalPrice = 69.99,
        likes = 70,
        isFavorited = false,
        picture = Picture(
            url = "https://picsum.photos/200",
            description = "Un pendentif bleu"
        ),
        category = "ACCESSORIES"
    )

    Box(modifier = Modifier.height(300.dp)) {
        ItemImageHeader(
            item = mockItem,
            onBackClicked = {},
            onShareClicked = {},
            onFavoriteClicked = {},
            showBackButton = false
        )
    }
}