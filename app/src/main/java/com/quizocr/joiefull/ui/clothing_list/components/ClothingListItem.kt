package com.quizocr.joiefull.ui.clothing_list.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.quizocr.joiefull.domain.model.ClothingItem

@Composable
fun ClothingListItem(item: ClothingItem, modifier: Modifier = Modifier) {
    Card(modifier = modifier) {
        Column {
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
            Column(modifier = Modifier.padding(8.dp)) {
                Text(text = item.name, style = MaterialTheme.typography.bodyLarge)
                Spacer(modifier = Modifier.width(4.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = "${item.price}€", style = MaterialTheme.typography.bodyMedium)
                    Text(
                        text = "${item.originalPrice}€",
                        style = MaterialTheme.typography.bodySmall.copy(textDecoration = TextDecoration.LineThrough),
                        color = Color.Gray
                    )
                }
            }
        }
    }
}