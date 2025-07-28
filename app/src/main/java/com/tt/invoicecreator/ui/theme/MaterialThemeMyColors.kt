package com.tt.invoicecreator.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable

val MaterialTheme.myColors: CustomColorsPalette
@Composable
@ReadOnlyComposable
get() = LocalColors.current