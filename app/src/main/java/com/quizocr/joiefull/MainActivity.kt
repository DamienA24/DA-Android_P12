package com.quizocr.joiefull

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.quizocr.joiefull.ui.clothing_detail.ClothingDetailScreen
import com.quizocr.joiefull.ui.clothing_list.ClothingListScreen
import com.quizocr.joiefull.ui.theme.JoieFullTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            JoieFullTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()
                    val windowSizeClass = calculateWindowSizeClass(this)
                    NavHost(
                        navController = navController,
                        startDestination = "clothing_list"
                    ) {
                        composable("clothing_list") {
                            ClothingListScreen(
                                navController = navController,
                                windowSizeClass = windowSizeClass
                            )
                        }
                        composable(
                            route = "clothing_detail/{clothingId}",
                            arguments = listOf(navArgument("clothingId") { type = NavType.IntType })
                        ) {
                            ClothingDetailScreen(navController = navController)
                        }
                    }
                }
            }
        }
    }
}