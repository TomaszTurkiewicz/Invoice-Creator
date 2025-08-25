package com.tt.invoicecreator.ui.theme

import androidx.compose.material3.ColorScheme
import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color

@Immutable
data class CustomColorsPalette(
    val material: ColorScheme,
    val invoicePrimary: Color,
    val primaryDark: Color
)