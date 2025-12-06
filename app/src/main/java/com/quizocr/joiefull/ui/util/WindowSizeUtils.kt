package com.quizocr.joiefull.ui.util

import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * Calculate the number of grid columns based on window size class
 */
fun calculateGridColumns(windowSizeClass: WindowSizeClass): Int {
    return when (windowSizeClass.widthSizeClass) {
        WindowWidthSizeClass.Compact -> 2
        WindowWidthSizeClass.Medium -> 2
        WindowWidthSizeClass.Expanded -> 3
        else -> 2
    }
}

/**
 * Check if the device is in expanded mode (large tablet)
 */
fun WindowSizeClass.isExpanded(): Boolean {
    return widthSizeClass == WindowWidthSizeClass.Expanded
}
