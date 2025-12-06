package com.quizocr.joiefull.ui.clothing_list

import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.quizocr.joiefull.ui.clothing_list.components.ClothingGrid
import com.quizocr.joiefull.ui.clothing_list.components.TabletClothingList
import com.quizocr.joiefull.ui.util.calculateGridColumns

@Composable
fun ClothingListScreen(
    navController: NavController,
    windowSizeClass: WindowSizeClass,
    viewModel: ClothingListViewModel = hiltViewModel()
) {
    val state = viewModel.state.value
    val isExpanded = windowSizeClass.widthSizeClass == WindowWidthSizeClass.Expanded
    var selectedItemId by rememberSaveable { mutableStateOf<Int?>(null) }

    LaunchedEffect(state.clothingItems.size, isExpanded, state.isLoading) {
        if (isExpanded && !state.isLoading && selectedItemId == null && state.clothingItems.isNotEmpty()) {
            selectedItemId = state.clothingItems.first().id
        }
    }

    if (isExpanded) {
        TabletClothingList(
            listState = state,
            columns = calculateGridColumns(windowSizeClass), // ex: 3 ou 4
            selectedItemId = selectedItemId,
            onItemSelected = { itemId -> selectedItemId = itemId },
            onFavoriteClick = { itemId -> viewModel.onFavoriteClicked(itemId) }
        )
    } else {
        // mobile
        ClothingGrid(
            state = state,
            columns = 2,
            onItemClick = { itemId -> navController.navigate("clothing_detail/$itemId") },
            onFavoriteClick = { itemId -> viewModel.onFavoriteClicked(itemId) }
        )
    }
}

