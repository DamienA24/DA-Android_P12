package com.quizocr.joiefull.ui.clothing_detail

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.quizocr.joiefull.ui.ViewModelFactory

@Composable
fun ClothingDetailScreen(
    viewModel: ClothingDetailViewModel = viewModel(factory = ViewModelFactory())
) {
    val state = viewModel.state.value

    Box(modifier = Modifier.fillMaxSize()) {
        if (state.isLoading) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
        }
        state.error?.let {
            Text(text = it, modifier = Modifier.align(Alignment.Center))
        }
        state.clothingItem?.let {
            Column {
                Text(text = it.name)
                Text(text = it.picture.description)
            }
        }
    }
}