package com.tt.invoicecreator.ui.screens

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import com.tt.invoicecreator.data.AppBarState
import com.tt.invoicecreator.viewmodel.AppViewModel

@Composable
fun AddItemScreen(
    viewModel: AppViewModel,
    ignoredOnComposing: (AppBarState) -> Unit
) {
    LaunchedEffect(key1 = true) {
        ignoredOnComposing(
            AppBarState(
                action = null
            )
        )
    }
}