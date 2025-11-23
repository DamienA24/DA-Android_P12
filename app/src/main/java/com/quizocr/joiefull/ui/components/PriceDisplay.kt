package com.quizocr.joiefull.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextDecoration

enum class PriceStyle {
    LARGE, MEDIUM
}

@Composable
fun PriceDisplay(
    price: Double,
    originalPrice: Double,
    modifier: Modifier = Modifier,
    style: PriceStyle = PriceStyle.MEDIUM
) {
    val priceTextStyle: TextStyle
    val originalPriceTextStyle: TextStyle

    if (style == PriceStyle.LARGE) {
        priceTextStyle = MaterialTheme.typography.titleLarge
        originalPriceTextStyle = MaterialTheme.typography.titleLarge
    } else {
        priceTextStyle = MaterialTheme.typography.bodyMedium
        originalPriceTextStyle = MaterialTheme.typography.bodySmall
    }

    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = "${price}€", style = priceTextStyle)
        Text(
            text = "${originalPrice}€",
            style = originalPriceTextStyle.copy(textDecoration = TextDecoration.LineThrough),
            color = Color.Gray
        )
    }
}