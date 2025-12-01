package com.quizocr.joiefull.ui.clothing_list

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.quizocr.joiefull.ui.clothing_list.components.ClothingListItem

@Composable
fun ClothingListScreen(
    navController: NavController,
    viewModel: ClothingListViewModel = hiltViewModel()
) {
    val state = viewModel.state.value
    val groupedItems = state.clothingItems.groupBy { it.category }

    Box(modifier = Modifier.fillMaxSize()) {
        if (state.isLoading) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
        }
        state.error?.let {
            Text(text = it, modifier = Modifier.align(Alignment.Center))
        }

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            groupedItems.forEach { (category, items) ->
                item {
                    Text(
                        text = category,
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                        style = MaterialTheme.typography.headlineMedium,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }

                items(items.chunked(2)) { rowItems ->
                    Row(
                        modifier = Modifier
                            .height(IntrinsicSize.Min) // Ensures items in a row have the same height
                            .fillMaxWidth()
                            .padding(horizontal = 8.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        rowItems.forEach { item ->
                            ClothingListItem(
                                item = item,
                                onFavoriteClicked = { viewModel.onFavoriteClicked(item.id) },
                                modifier = Modifier
                                    .weight(1f)
                                    .fillMaxHeight() // Fills the height of the row
                                    .clickable { navController.navigate("clothing_detail/${item.id}") }
                            )
                        }

                        if (rowItems.size < 2) {
                            Spacer(modifier = Modifier.weight(1f))
                        }
                    }
                }
            }
        }
    }
}
