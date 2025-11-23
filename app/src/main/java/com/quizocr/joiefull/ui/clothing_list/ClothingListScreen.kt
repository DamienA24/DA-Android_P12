package com.quizocr.joiefull.ui.clothing_list

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.quizocr.joiefull.ui.ViewModelFactory

@Composable
fun ClothingListScreen(
    viewModel: ClothingListViewModel = viewModel(factory = ViewModelFactory())
) {
    val state = viewModel.state.value

    Box(modifier = Modifier.fillMaxSize()) {
        if (state.isLoading) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
        }
        state.error?.let {
            Text(text = it, modifier = Modifier.align(Alignment.Center))
        }
        LazyColumn(modifier = Modifier.fillMaxSize()) {
            items(state.clothingItems) { item ->
                Text(text = item.name, modifier = Modifier.padding(16.dp))
            }
        }
    }
}