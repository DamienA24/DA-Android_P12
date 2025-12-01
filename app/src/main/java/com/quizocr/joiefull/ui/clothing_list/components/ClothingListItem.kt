package com.quizocr.joiefull.ui.clothing_list.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.quizocr.joiefull.domain.model.ClothingItem
import com.quizocr.joiefull.ui.components.FavoriteCounter
import com.quizocr.joiefull.ui.components.PriceDisplay

@Composable
fun ClothingListItem(item: ClothingItem, onFavoriteClicked: () -> Unit, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f)
                    .clip(RoundedCornerShape(16.dp))
            ) {
                AsyncImage(
                    model = item.picture.url,
                    contentDescription = item.picture.description,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
                FavoriteCounter(
                    likes = item.likes,
                    isFavorited = item.isFavorited,
                    onFavoriteClicked = onFavoriteClicked,
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(8.dp)
                )
            }
            Column(modifier = Modifier.padding(8.dp)) {
                Text(text = item.name, style = MaterialTheme.typography.bodyLarge, color = MaterialTheme.colorScheme.onSurface)
                Spacer(modifier = Modifier.width(4.dp))
                PriceDisplay(
                    price = item.price,
                    originalPrice = item.originalPrice
                )
            }
        }
    }
}