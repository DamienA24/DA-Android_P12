package com.quizocr.joiefull.ui.clothing_list.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.quizocr.joiefull.domain.model.ClothingItem

@Composable
fun ClothingListItem(item: ClothingItem, modifier: Modifier = Modifier) {
    Card(modifier = modifier) {
        Column {
            AsyncImage(
                model = item.picture.url,
                contentDescription = item.picture.description,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f)
            )
            Text(text = item.name, modifier = Modifier.padding(start = 8.dp, end = 8.dp, top = 8.dp))
            Text(text = "${item.price}€", modifier = Modifier.padding(start = 8.dp, end = 8.dp, bottom = 8.dp))
        }
    }
}